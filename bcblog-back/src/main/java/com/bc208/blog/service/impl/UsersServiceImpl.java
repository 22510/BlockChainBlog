package com.bc208.blog.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.bc208.blog.common.dto.LoginDTO;
import com.bc208.blog.common.dto.Result;
import com.bc208.blog.common.dto.UserRegisterDTO;
import com.bc208.blog.common.dto.wxLinkDTO;
import com.bc208.blog.pojo.User;
import com.bc208.blog.repository.base.mapper.UsersMapper;
import com.bc208.blog.service.CaptchaService;
import com.bc208.blog.service.MailService;
import com.bc208.blog.service.UserService;
import com.bc208.blog.utils.PasswordEncoder;
import com.bc208.blog.utils.UserOpenidHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.bc208.blog.utils.RedisConstants.LOGIN_USER_KEY;
import static com.bc208.blog.utils.RedisConstants.REGISTER_CODE_KEY;

/**
 * @author QingheLi
 */
@Service("UsersServiceImpl")
@Slf4j
public class UsersServiceImpl implements UserService {

    private final UsersMapper usersMapper;

    private final MailService mailService;

    private final CaptchaService captchaService;

    public UsersServiceImpl(UsersMapper usersMapper, MailService mailService, CaptchaService captchaService) {
        this.usersMapper = usersMapper;
        this.mailService = mailService;
        this.captchaService = captchaService;
    }

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 用户登入
     * @param loginDto 账号密码输入
     * @return 返回用户信息
     */
    @Override
    public Result userLogin(LoginDTO loginDto) {

        User user = usersMapper.getUserInfo(loginDto.getEmail());

        if (user.getUserEnable() == 0){
            return Result.fail("您的账号暂未通过申请, 请过段时间尝试登录");
        }

        if (!PasswordEncoder.matches(user.getUserPassword(), loginDto.getPassword())){
            return Result.fail("密码错误");
        }

        String token = UUID.randomUUID().toString(true);

        Map<String, String> userMap = new HashMap<>(4);

        userMap.put("id", String.valueOf(user.getUserId()));
        userMap.put("name", user.getUserName());
        userMap.put("email", user.getUserEmail());
        userMap.put("role", user.getUserRole().toString());

        stringRedisTemplate.opsForHash().putAll(LOGIN_USER_KEY + token, userMap);

        stringRedisTemplate.expire(LOGIN_USER_KEY + token, 30, TimeUnit.MINUTES);

        return Result.success(token);
    }


    /**
     * 用户注册
     * @param userRegisterDto 注册信息
     * @return 注册结果
     */
    @Override
    public Result userRegister(UserRegisterDTO userRegisterDto) {

        if (!captchaService.checkCaptcha(userRegisterDto.getEmail(), REGISTER_CODE_KEY, userRegisterDto.getCaptcha())){
            return Result.fail("验证失败");
        }

        if (usersMapper.queryUserByEmail(userRegisterDto.getEmail()) != null) {
            return Result.fail("用户已存在");
        }

        User user = new User();
        user.setUserName(userRegisterDto.getUsername());
        user.setUserGrade(userRegisterDto.getGrade());
        user.setUserEmail(userRegisterDto.getEmail());
        user.setUserInterest(userRegisterDto.getInterest());
        user.setUserPassword(PasswordEncoder.encode(userRegisterDto.getPassword()));
        user.setUserRole(0);
        user.setUserAuth(0);

        if (usersMapper.registerUser(user) == 1) {
            return Result.success("用户注册成功");
        } else {
            return Result.fail("用户注册失败");
        }
    }

    /**
     * 用户退出
     * @param token 前端token
     * @return 用户退出
     */
    @Override
    public Result userLogout(String token){
        stringRedisTemplate.delete(LOGIN_USER_KEY + token);
        return Result.success();
    }

    /**
     * 用户注册验证码发送
     * @param email 用户邮箱
     * @return 处理结果
     */
    @Override
    public Result sendCaptcha(String email) {
        final String mailSubject = "区块链工作室:用户注册验证码";
        if(!captchaService.sendCaptcha(email, REGISTER_CODE_KEY, mailSubject)){
            return Result.fail("验证码申请出错");
        }
        return Result.success();
    }

    @Value("${app.appid}")
    private String appid;

    @Value("${app.appsecret}")
    private String secret;

    @Override
    public Result userWxLogin(String wxCode) {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid={0}&secret={1}&js_code={2}&grant_type=authorization_code";
        String replaceUrl = url.replace("{0}", appid).replace("{1}", secret).replace("{2}", wxCode);
        String res = HttpUtil.get(replaceUrl);
        JSONObject jsonObject = JSONUtil.parseObj(res);
        String userOpenId = jsonObject.getStr("openid");
        log.warn("userOpenId:"+userOpenId);
        if ( userOpenId == null){
            // 1. 如果微信未通过认证, 就报错, 说明这个wxCode有问题
            return Result.fail("微信登录失败");
        }
        String uuid = UUID.randomUUID().toString(true);
        // 2. 通过微信认证, 接下来要考虑后端.
        // 2.1 微信的openId是独一无二的, 所以如果这玩意在数据库中存在, 说明用户已经登录过该系统, 并绑定了邮箱和密码
        // 如果后端未找到该openid对应的数据条目, 说明该用户未绑定系统, 需要进行绑定
        if(usersMapper.checkUserWx(userOpenId)  == 0){
            if(usersMapper.insertUserAuth("wx", userOpenId, jsonObject.getStr("session_key")) != 1){
                return Result.fail("系统错误, 请联系管理员");
            }
            stringRedisTemplate.opsForValue().set("LOGIN:WEIXIN:OPENID:"+uuid, userOpenId, 5, TimeUnit.MINUTES);
            return Result.fail(uuid);
        }else if (usersMapper.checkUserWxLogined(userOpenId) == -1){
            stringRedisTemplate.opsForValue().set("LOGIN:WEIXIN:OPENID:"+uuid, userOpenId, 5, TimeUnit.MINUTES);
            // 用户未绑定邮箱, 不返回token
            return Result.fail(uuid);
        }
        // 用户通过验证, 并绑定了邮箱, 那登录后就返回token, 让用户得以访问数据

        User user = usersMapper.getUserInfoByOpenid(userOpenId);
        Map<String, String> userMap = new HashMap<>(4);
        userMap.put("id", String.valueOf(user.getUserId()));
        userMap.put("name", user.getUserName());
        userMap.put("email", user.getUserEmail());
        userMap.put("role", user.getUserRole().toString());
        stringRedisTemplate.opsForHash().putAll(LOGIN_USER_KEY + uuid, userMap);
        stringRedisTemplate.expire(LOGIN_USER_KEY + uuid, 30, TimeUnit.MINUTES);
        return Result.success(uuid);
    }

    @Override
    public Result userWxLink(wxLinkDTO wxLinkDTO) {

        // 1. 进来先检查验证码是否正确
        if (!captchaService.checkCaptcha(wxLinkDTO.getEmail(), REGISTER_CODE_KEY, wxLinkDTO.getCaptcha())){
            return Result.fail("验证失败");
        }
        if (usersMapper.queryUserByEmail(wxLinkDTO.getEmail()) == null) {
            return Result.fail("用户不存在, 请到BC208网站注册");
        }

        User user = usersMapper.getUserInfo(wxLinkDTO.getEmail());

        if (user.getUserEnable() == 0){
            return Result.fail("您的账号暂未可用, 请过段时间尝试");
        }

        if (!PasswordEncoder.matches(user.getUserPassword(), wxLinkDTO.getPassword())){
            return Result.fail("密码错误");
        }
        String openid = stringRedisTemplate.opsForValue().get("LOGIN:WEIXIN:OPENID:" + wxLinkDTO.getAuth());
        log.warn("openid:"+openid);
        if (usersMapper.upUserIdForOpenid(openid, user.getUserId()) != 1){
            return Result.fail("系统错误, 请联系管理员");
        }
        // 用完后要移除ThreadLocal
        UserOpenidHolder.removeUser();
        String token = UUID.randomUUID().toString(true);
        Map<String, String> userMap = new HashMap<>(4);
        userMap.put("id", String.valueOf(user.getUserId()));
        userMap.put("name", user.getUserName());
        userMap.put("email", user.getUserEmail());
        userMap.put("role", user.getUserRole().toString());
        stringRedisTemplate.opsForHash().putAll(LOGIN_USER_KEY + token, userMap);
        stringRedisTemplate.expire(LOGIN_USER_KEY + token, 30, TimeUnit.MINUTES);
        return Result.success(token);
    }

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Result getQRCode() {
        String getAccessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appid+"&secret="+secret;
        String accessTokenRes = HttpUtil.get(getAccessTokenUrl);
        JSONObject jsonObject = JSONUtil.parseObj(accessTokenRes);
        String accessToken = jsonObject.getStr("access_token");

        log.warn("accessToken:"+accessToken);
        String getQRCodeUrl0 = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token="+accessToken;
        log.warn("url"+getQRCodeUrl0);
        String getQRCodeUrl = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token={accessToken}";

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);

        // 使用Map设置占位符的值
        Map<String, String> urlVariables = new HashMap<>();
        urlVariables.put("accessToken", accessToken);

        String requestBody = "{\"page\":\"pages/login/login\", \"scene\":\"a=1\"}";
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, requestHeaders);

        // ResponseEntity<byte[]> responseEntity = restTemplate.exchange(getQRCodeUrl, HttpMethod.POST, requestEntity, byte[].class, urlVariables);
        ResponseEntity<byte[]> responseEntity = restTemplate.exchange(getQRCodeUrl0, HttpMethod.POST, requestEntity, byte[].class);
        log.warn("image:"+ Arrays.toString(responseEntity.getBody()));
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            byte[] imageBytes = responseEntity.getBody();
            // 保存图片到文件
            try (FileOutputStream fos = new FileOutputStream("image.jpg")) {
                assert imageBytes != null;
                fos.write(imageBytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
            log.info("图片下载成功并保存到文件");
        } else {
            log.info("图片下载失败，状态码：" + responseEntity.getStatusCode());
        }
        return Result.success();
    }

    @Override
    public Result userForgotPassword(String userEmail) {
        final String newPassword = UUID.randomUUID().toString(true);
        if(usersMapper.getUserInfo(userEmail) == null){
            return Result.fail("用户不存在");
        }
        if (usersMapper.makeDefaultPassword(userEmail, PasswordEncoder.encode(newPassword)) == 0){
            return Result.fail("更新默认密码错误");
        }
        mailService.sendMail(mailService.createMail(userEmail, "密码更新", newPassword));
        return Result.success();
    }
}

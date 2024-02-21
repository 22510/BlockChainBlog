package com.bc208.blog.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.Arrays;

/**
 * @author Qinghe
 * @date 2024/2/16
 */
@Slf4j
@RestController
@Api(tags = "微信接口相关控制器")
@RequestMapping("/wxapi")
public class wxController {

    /**
     * @description 微信公众号服务器配置校验token
     * @author: liyinlong
     * @date 2019-05-09 9:38
     * @return
     */
    @ApiOperation("微信公众号服务器配置校验token")
    @GetMapping("/checkToken")
    public void checkToken(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        log.info("微信公众号Token验证");
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            if (CheckUtil.checkSignature(signature, timestamp, nonce)) {
                out.write(echostr);
                log.info("微信公众号Token验证成");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
    }

    public class CheckUtil {
        private static final String token = "22419090793fjll";
        public static boolean checkSignature(String signature, String timestamp, String nonce) {
            String[] str = new String[]{token, timestamp, nonce};
            //排序
            Arrays.sort(str);
            //拼接字符串
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < str.length; i++) {
                buffer.append(str[i]);
            }
            //进行sha1加密
            String temp = SHA1.encode(buffer.toString());
            //与微信提供的signature进行匹对
            return signature.equals(temp);
        }
    }
    public class SHA1 {
        private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5',
                '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        private static String getFormattedText(byte[] bytes) {
            int len = bytes.length;
            StringBuilder buf = new StringBuilder(len * 2);
            for (int j = 0; j < len; j++) {
                buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
                buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
            }
            return buf.toString();
        }
        public static String encode(String str) {
            if (str == null) {
                return null;
            }
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
                messageDigest.update(str.getBytes());
                return getFormattedText(messageDigest.digest());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}

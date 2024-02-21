package com.bc208.blog.config;

import com.bc208.blog.utils.AuthorityInterceptor;
import com.bc208.blog.utils.LoginInterceptor;
import com.bc208.blog.utils.RefreshTokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author QingheLi
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        // addPathPatterns 拦截所有, 用于已登录者更新token时间
        registry.addInterceptor(new RefreshTokenInterceptor(redisTemplate))
                .addPathPatterns("/**"
                ).order(0);
        // excludePathPatterns 放行部分请求
        registry.addInterceptor(new LoginInterceptor())
                .excludePathPatterns(
                        "/bc208/login",
                        "/bc208/wxLogin",
                        "/bc208/wxLink",
                        "/bc208/captcha",
                        "/bc208/register",
                        "/bc208/wxQRCode",
                        "/admin/login",
                        "/admin/register",
                        "/swagger**/**","/webjars/**","/v3/**","/doc.html",
                        "/wxapi/checkToken"
                ).order(1);
        registry.addInterceptor(new AuthorityInterceptor())
                .excludePathPatterns("/**"
                ).order(2);
    }
}

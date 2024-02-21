package com.bc208.blog.config;

import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Qinghe
 * @date 2024/2/16
 */
@Configuration
public class WxMpConfig {
    @Value("${wx.mp.token}")
    private String token;

    @Value("${wx.mp.appid}")
    private String appid;

    @Value("${wx.mp.secret}")
    private String appSecret;

    @Value("${wx.mp.aesKey}")
    private String aesKey;

    @Bean
    public WxMpService wxMpService() {
        WxMpService wxMpService = new WxMpServiceImpl();
        WxMpDefaultConfigImpl config = new WxMpDefaultConfigImpl();
        config.setAppId(appid);
        config.setSecret(appSecret);
        wxMpService.setWxMpConfigStorage(config);
        return wxMpService;
    }
}
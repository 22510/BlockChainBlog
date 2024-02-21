// package com.bc208.blog.config;
//
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.data.redis.connection.RedisConnectionFactory;
// import org.springframework.data.redis.core.RedisTemplate;
// import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
// import org.springframework.data.redis.serializer.StringRedisSerializer;
//
// /**
//  * @author QingheLi
//  */
// @Configuration
// public class RedisConfig {
//
//     @Bean(name = "byteRedisTemplate")
//     public RedisTemplate<String, byte[]> redisTemplate(RedisConnectionFactory connectionFactory) {
//         RedisTemplate<String, byte[]> template = new RedisTemplate<>();
//         template.setConnectionFactory(connectionFactory);
//         template.setKeySerializer(new StringRedisSerializer());
//         template.setValueSerializer(new JdkSerializationRedisSerializer());
//         return template;
//     }
// }

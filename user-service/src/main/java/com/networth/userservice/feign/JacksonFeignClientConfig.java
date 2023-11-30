package com.networth.userservice.feign;

import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonFeignClientConfig {

    @Bean
    public Encoder jacksonEncoder() {
        return new JacksonEncoder();
    }

    @Bean
    public Decoder jacksonDecoder() {
        return new JacksonDecoder();
    }
}

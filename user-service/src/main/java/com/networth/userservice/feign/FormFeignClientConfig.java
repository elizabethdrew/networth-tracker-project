package com.networth.userservice.feign;

import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FormFeignClientConfig {

    @Bean
    public Encoder encoder() {
        return new SpringFormEncoder(new JacksonEncoder());
    }

    @Bean
    public Decoder formDecoder() {
        return new JacksonDecoder();
    }
}

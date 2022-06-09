package yapp.bestFriend.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import yapp.bestFriend.model.utils.JwtUtil;

@Configuration
public class JwtConfig {

    @Value("${jwt.secret}")
    private String secret;

    @Bean
    public JwtUtil jwtUtil(){
        return new JwtUtil(secret);
    }
}

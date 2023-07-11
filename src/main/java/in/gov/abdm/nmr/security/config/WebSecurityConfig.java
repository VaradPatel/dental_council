package in.gov.abdm.nmr.security.config;

import in.gov.abdm.nmr.security.jwt.JwtAuthenticationProvider;
import in.gov.abdm.nmr.security.username_password.UserPasswordAuthenticationProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

@Configuration
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig {

    private static final Logger LOGGER = LogManager.getLogger();

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        LOGGER.info("Configuring web security");
        return http.csrf().disable().cors().and()
                .headers().xssProtection().and().contentSecurityPolicy("form-action 'self'").and().cacheControl().and().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeHttpRequests(authRequestConfig -> authRequestConfig.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()).build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public KeyStore keyStore(@Value("${spring.profiles.active}") String activeProfile, @Value("${nmr.keystore.pass}") String password) {
        String keystorePath = "classpath:keystore/nhat-900.pfx";
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            Resource keystoreFile = new DefaultResourceLoader().getResource(keystorePath);
            keyStore.load(keystoreFile.getInputStream(), password.toCharArray());
            return keyStore;
        } catch (IOException | CertificateException | NoSuchAlgorithmException | KeyStoreException e) {
            LOGGER.error("Exception occured while loading keystore: {}", keystorePath, e);
        }
        throw new IllegalArgumentException("Unable to load keystore");
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http, UserPasswordAuthenticationProvider userPasswordAuthenticationProvider,
                                             JwtAuthenticationProvider jwtAuthenticationProvider) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(userPasswordAuthenticationProvider);
        authenticationManagerBuilder.authenticationProvider(jwtAuthenticationProvider);
        return authenticationManagerBuilder.build();
    }
}

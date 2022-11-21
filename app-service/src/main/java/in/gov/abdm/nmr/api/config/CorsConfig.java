package in.gov.abdm.nmr.api.config;

import static in.gov.abdm.nmr.common.CustomHeaders.ACCESS_TOKEN;
import static in.gov.abdm.nmr.common.CustomHeaders.REFRESH_TOKEN;
import static in.gov.abdm.nmr.common.CustomHeaders.TRACE_ID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class CorsConfig {

    private static final Logger LOGGER = LogManager.getLogger();

    @Value("${nmr.api.cors.whitelist.urls}")
    private String[] allowedUrls;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        LOGGER.info("Configuring CORS");
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins(allowedUrls).allowedMethods("*").allowCredentials(true).exposedHeaders(ACCESS_TOKEN, REFRESH_TOKEN, TRACE_ID);
            }
        };
    }
}

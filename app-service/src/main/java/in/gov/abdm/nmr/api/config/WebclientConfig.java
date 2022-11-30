package in.gov.abdm.nmr.api.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebclientConfig {

    private static final Logger LOGGER = LogManager.getLogger();

    private String dbBaseUrl;

    public WebclientConfig(@Value("${nmr.db.api.base.url}") String baseUrl) {
        this.dbBaseUrl = baseUrl;
    }

    @Bean
    public WebClient dbWebClient(WebClient.Builder webClientBuilder) {
        LOGGER.info("Configuring WebClient for DB API");
        return webClientBuilder.baseUrl(dbBaseUrl).defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
    }
}

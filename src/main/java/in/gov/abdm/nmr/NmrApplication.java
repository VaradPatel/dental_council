package in.gov.abdm.nmr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class NmrApplication {

    public static void main(String[] args) {
        SpringApplication.run(NmrApplication.class, args);
    }
}

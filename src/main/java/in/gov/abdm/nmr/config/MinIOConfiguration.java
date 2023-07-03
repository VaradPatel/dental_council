package in.gov.abdm.nmr.config;

import in.gov.abdm.minio.connector.service.S3ServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinIOConfiguration {
    @Value("${aws.s3.bucket}")
    private String s3Bucket;
    @Value(("${aws.s3.url}"))
    private String s3Url;
    @Value("${aws.s3.access.key.id}")
    private String accessKey;
    @Value("${aws.s3.secret.access.key}")
    private String secretKey;

    @Bean
    public S3ServiceImpl getS3Service() {
        return new S3ServiceImpl(s3Url, accessKey, secretKey, s3Bucket);
    }
}

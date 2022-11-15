package ir.jashakouri.domain.config.async;

import com.amazonaws.auth.*;
import com.amazonaws.services.s3.*;
import com.amazonaws.client.builder.AwsClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jash
 * @created 02/10/2022 - 18:18
 * @project digital-wallet-backend
 */
@Configuration
@Slf4j(topic = "LOG_StorageCloudConfig")
public class StorageCloudConfig {

    @Value("${amazon.aws.access-key}")
    private String accessKeyId;

    @Value("${amazon.aws.secret-key}")
    private String accessKeySecret;

    @Value("${amazon.aws.region}")
    private String s3RegionName;

    @Value("${amazon.aws.endpoint}")
    private String s3Endpoint;

    @Bean
    public AmazonS3 s3() {

        AWSCredentials arvanCloudCredentials = new BasicAWSCredentials(
                accessKeyId,
                accessKeySecret);

        var endpoint = new AwsClientBuilder.EndpointConfiguration(
                s3Endpoint, s3RegionName);

        var builder = AmazonS3ClientBuilder.standard();

        builder.setEndpointConfiguration(endpoint);
        builder.withCredentials(new AWSStaticCredentialsProvider(arvanCloudCredentials));
        return builder.build();
    }

}

package com.springboot.laptop.config;


import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonS3Config {

        @Value("${aws.accessToken}")
        private String accessToken;


        @Value("${aws.secretToken}")
        private String secretToken;

        @Value("${aws.region}")
        private String region;
        @Bean
        public AmazonS3 s3() {
                AWSCredentials awsCredentials = new BasicAWSCredentials(accessToken, secretToken);
                return AmazonS3ClientBuilder.standard().withRegion(region).withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).build();
        }

}

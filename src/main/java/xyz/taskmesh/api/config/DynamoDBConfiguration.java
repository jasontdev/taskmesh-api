package xyz.taskmesh.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;

@Configuration
public class DynamoDBConfiguration {

    @Value("$(dynamodb.endpoint)")
    private String endpoint;

    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient() {
        final var dynamoDbClient = DynamoDbClient.builder().region(Region.of("ap-southeast-2"))
                .endpointOverride(URI.create(endpoint)).build();
        return DynamoDbEnhancedClient.builder().dynamoDbClient(dynamoDbClient).build();
    }
}

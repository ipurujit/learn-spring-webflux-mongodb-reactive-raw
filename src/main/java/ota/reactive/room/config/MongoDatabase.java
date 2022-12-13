package ota.reactive.room.config;

import com.mongodb.reactivestreams.client.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class MongoDatabase {

    @Value("${spring.data.database}")
    private String databaseName;

    @Autowired
    private MongoClient mongoClient;

    @Bean
    public com.mongodb.reactivestreams.client.MongoDatabase getDatabase() {
        return this.mongoClient.getDatabase(databaseName);
    }
}

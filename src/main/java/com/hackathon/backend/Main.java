package com.hackathon.backend;

import com.hackathon.backend.database.MongoManager;
import com.hackathon.backend.managers.PoolsManager;
import lombok.Getter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    @Getter
    private static MongoManager mongoManager;
    @Getter
    private static PoolsManager poolsManager;

    public static void main(String[] args) {
        mongoManager = new MongoManager();
        poolsManager = new PoolsManager();
        SpringApplication.run(Main.class, args);
    }

}

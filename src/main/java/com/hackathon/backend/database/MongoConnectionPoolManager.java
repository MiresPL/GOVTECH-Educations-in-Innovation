package com.hackathon.backend.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MongoConnectionPoolManager {
    private final MongoClient client;
    private final MongoCollection<Document> pools;

    public MongoConnectionPoolManager() {
        this.client = MongoClients.create("mongodb://localhost/backend");
        final MongoDatabase database = this.client.getDatabase("backend");
        final List<String> collectionNames = database.listCollectionNames().into(new ArrayList<>());

        if (!collectionNames.contains("pools")) {
            database.createCollection("pools");
        }

        this.pools = database.getCollection("pools");
    }
}

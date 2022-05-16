package id.luckynetwork.lyrams.lyralibs.core.database.mongo;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
@RequiredArgsConstructor
public class MongoManager {

    private final Map<String, MongoCollection<Document>> mongoCollections = new HashMap<>();
    private final Logger logger;

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    public MongoManager(MongoConfiguration mongoConfiguration, Logger logger) {
        this.logger = logger;

        if (mongoConfiguration.isUseURI()) {
            MongoClientURI URI = new MongoClientURI(mongoConfiguration.getUri(), MongoClientOptions.builder().sslEnabled(true));

            this.mongoClient = new MongoClient(URI);
            this.mongoDatabase = this.mongoClient.getDatabase(Objects.requireNonNull(URI.getDatabase()));
        } else {
            if (mongoConfiguration.isAuthenticate()) {
                MongoCredential credential = MongoCredential.createCredential(
                        mongoConfiguration.getUsername(),
                        mongoConfiguration.getDatabase(),
                        mongoConfiguration.getPassword().toCharArray()
                );

                MongoClientOptions options = MongoClientOptions.builder().sslEnabled(mongoConfiguration.isUseSSL()).build();
                this.mongoClient = new MongoClient(new ServerAddress(mongoConfiguration.getAddress(), mongoConfiguration.getPort()), credential, options);
            } else {
                this.mongoClient = new MongoClient(new ServerAddress(mongoConfiguration.getAddress(), mongoConfiguration.getPort()));
            }

            this.mongoDatabase = this.mongoClient.getDatabase(mongoConfiguration.getDatabase());
        }
    }

    /**
     * It loads a collection from the database and stores it in a hashmap
     *
     * @param key The name of the collection to load
     */
    public void loadCollection(String key) {
        try {
            this.mongoCollections.put(key, this.mongoDatabase.getCollection(key));
        } catch (Exception e) {
            this.logger.log(Level.SEVERE, "Error while loading collection " + key + ": " + e.getMessage());
        }
    }

    /**
     * If the collection is not loaded, load it
     *
     * @param key The name of the collection you want to get
     * @return A MongoCollection object
     */
    public MongoCollection<Document> getCollection(String key) {
        MongoCollection<Document> collection = this.mongoCollections.get(key);
        if (collection == null) {
            this.loadCollection(key);
            collection = this.mongoCollections.get(key);

            this.logger.log(Level.WARNING, "Collection " + key + " not loaded, loading it now");
        }

        return collection;
    }

}

package id.luckynetwork.lyrams.lyralibs.core.database.mongo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(builderMethodName = "newBuilder")
public class MongoConfiguration {
    private boolean useURI, authenticate, useSSL;
    private String address, username, database, password, uri;
    private int port;
}

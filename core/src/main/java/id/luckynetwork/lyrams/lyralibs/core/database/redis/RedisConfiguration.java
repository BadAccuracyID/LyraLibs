package id.luckynetwork.lyrams.lyralibs.core.database.redis;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(builderMethodName = "newBuilder")
public class RedisConfiguration {
    private final String host;
    private final int port;

    private final int timeout;

    private final boolean auth;
    private final String password;

    private final String clientName;

    private final String channel;
}

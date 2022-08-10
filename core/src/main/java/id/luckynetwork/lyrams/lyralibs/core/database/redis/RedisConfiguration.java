package id.luckynetwork.lyrams.lyralibs.core.database.redis;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
@Builder(builderMethodName = "newBuilder")
public class RedisConfiguration {

    /*
     * The redis host
     */
    private final String host = "localhost";
    /*
     * The redis host's port
     */
    private final int port = 6379;
    /*
     * timeout before a connection is considered failed
     */
    private final int timeout = 30_000;

    /*
     * the redis password, set to null if not needed
     */
    @Nullable
    private final String password = null;
    /*
     * the client name
     */
    private final String clientName = "RedisClient";
    /*
     * the redis channel to use for pub/sub
     */
    private final String channel = "RedisChannel";

}

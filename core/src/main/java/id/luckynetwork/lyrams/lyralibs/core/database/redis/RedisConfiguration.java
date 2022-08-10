package id.luckynetwork.lyrams.lyralibs.core.database.redis;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

@Getter
@RequiredArgsConstructor
public class RedisConfiguration {

    /*
     * The redis host
     */
    private final String host;
    /*
     * The redis host's port
     */
    private final int port;
    /*
     * the redis channel to use for pub/sub
     */
    private final String channel;
    /*
     * the redis password, set to null if not needed
     */
    @Nullable
    private String password = null;
    /*
     * the client name
     */
    private String clientName = "RedisClient";

    /*
     * timeout before a connection is considered failed
     */
    private int timeout = 30_000;

    /**
     * Sets the password for the Redis server.
     * Set to null if not needed.
     *
     * @param password The password to use for authentication.
     * @return The RedisConfiguration object.
     */
    public RedisConfiguration setPassword(@Nullable String password) {
        this.password = password;
        return this;
    }

    /**
     * Sets the client name
     *
     * @param clientName The name of the client.
     * @return The RedisConfiguration object.
     */
    public RedisConfiguration setClientName(String clientName) {
        this.clientName = clientName;
        return this;
    }

    /**
     * Sets the timeout before a connection is considered failed
     * object.
     *
     * @param timeout The timeout for the connection to the Redis server.
     * @return The RedisConfiguration object.
     */
    public RedisConfiguration setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

}

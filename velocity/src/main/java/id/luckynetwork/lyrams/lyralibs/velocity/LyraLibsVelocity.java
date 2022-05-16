package id.luckynetwork.lyrams.lyralibs.velocity;

import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;

public class LyraLibsVelocity {

    @Getter
    private static LyraLibsVelocity instance;

    private final ProxyServer server;
    private String messagePrefix;

    private LyraLibsVelocity(ProxyServer server) {
        this.server = server;
    }

    /**
     * It creates a new instance of LyraLibsVelocity, sets the message prefix, and sets the instance variable to the new
     * instance
     *
     * @param server The ProxyServer instance
     * @param messagePrefix The prefix for all messages sent by the plugin.
     */
    public static void initialize(ProxyServer server, String messagePrefix) {
        LyraLibsVelocity libs = new LyraLibsVelocity(server);
        libs.messagePrefix = messagePrefix;

        instance = libs;
    }

    /**
     * This function returns the instance of the proxy server.
     *
     * @return The server variable is being returned.
     */
    public static ProxyServer getServer() {
        return instance.server;
    }

    /**
     * This function returns the value of the messagePrefix variable.
     *
     * @return The message prefix.
     */
    public static String getMessagePrefix() {
        return instance.messagePrefix;
    }
}

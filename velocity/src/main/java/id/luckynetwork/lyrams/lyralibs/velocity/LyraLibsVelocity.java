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

    public static void initialize(ProxyServer server, String messagePrefix) {
        LyraLibsVelocity libs = new LyraLibsVelocity(server);
        libs.messagePrefix = messagePrefix;

        instance = libs;
    }

    public static ProxyServer getServer() {
        return instance.server;
    }

    public static String getMessagePrefix() {
        return instance.messagePrefix;
    }
}

package id.luckynetwork.lyrams.lyralibs.velocity.utils;

import com.google.common.base.Charsets;
import com.velocitypowered.api.proxy.Player;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class PlayerUtils {

    /**
     * Gets the offline UUID of a player
     *
     * @param playerName The name of the player you want to get the UUID of.
     * @return The offline UUID of the player.
     */
    public UUID getOfflineUUID(String playerName) {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + playerName).getBytes(Charsets.UTF_8));
    }

    /**
     * see {@link PlayerUtils#getOfflineUUID(String)}
     */
    public UUID getOfflineUUID(Player player) {
        return PlayerUtils.getOfflineUUID(player.getUsername());
    }

}

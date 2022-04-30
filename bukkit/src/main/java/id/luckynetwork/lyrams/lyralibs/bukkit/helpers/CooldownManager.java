package id.luckynetwork.lyrams.lyralibs.bukkit.helpers;

import id.luckynetwork.lyrams.lyralibs.bukkit.LyraLibsBukkit;
import id.luckynetwork.lyrams.lyralibs.bukkit.utils.PlayerUtils;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.jetbrains.annotations.Nullable;

@UtilityClass
public class CooldownManager {

    public boolean canExecute(Player player, String cooldownKey, long expectedWait) {
        return canExecute(player, cooldownKey, expectedWait, null);
    }

    public boolean canExecute(Player player, String cooldownKey, long expectedWait, @Nullable String bypassPermission) {
        if (bypassPermission != null) {
            if (player.hasPermission(bypassPermission)) {
                return true;
            }
        }

        MetadataValue metadata = PlayerUtils.getMetadata(player, cooldownKey);
        if (metadata == null) {
            PlayerUtils.applyMetadata(player, cooldownKey, System.currentTimeMillis());
            return true;
        }

        long lastUsed = metadata.asLong();
        if (lastUsed + expectedWait >= System.currentTimeMillis()) {
            player.sendMessage(LyraLibsBukkit.getMessagePrefix() + "§cPlease wait before doing that again!");
            return false;
        }

        PlayerUtils.applyMetadata(player, cooldownKey, System.currentTimeMillis());
        return true;
    }

}

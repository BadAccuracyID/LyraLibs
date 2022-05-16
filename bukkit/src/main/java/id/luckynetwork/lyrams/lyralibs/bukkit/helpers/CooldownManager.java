package id.luckynetwork.lyrams.lyralibs.bukkit.helpers;

import id.luckynetwork.lyrams.lyralibs.bukkit.LyraLibsBukkit;
import id.luckynetwork.lyrams.lyralibs.bukkit.utils.PlayerUtils;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.jetbrains.annotations.Nullable;

@UtilityClass
public class CooldownManager {


    /**
     * {@see CooldownManager#requestCooldown(Callable, CommandSender, boolean, List, String)}
     */
    public boolean canExecute(Player player, String cooldownKey, long expectedWait) {
        return canExecute(player, cooldownKey, expectedWait, null);
    }

    /**
     * If the player has the bypass permission, return true. If the player doesn't have the bypass permission, check if the
     * player has the cooldown metadata. If the player doesn't have the cooldown metadata, apply the cooldown metadata and
     * return true. If the player does have the cooldown metadata, check if the cooldown has expired. If the cooldown has
     * expired, apply the cooldown metadata and return true. If the cooldown hasn't expired, send the player a message and
     * return false
     *
     * @param player           The player to check the cooldown for.
     * @param cooldownKey      The key to store the cooldown under.
     * @param expectedWait     The amount of time in milliseconds that the player must wait before executing the command again.
     * @param bypassPermission If the player has this permission, they will bypass the cooldown.
     * @return A boolean value.
     */
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

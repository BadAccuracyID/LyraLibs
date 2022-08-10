package id.luckynetwork.lyrams.lyralibs.bukkit.utils;

import com.google.common.base.Charsets;
import id.luckynetwork.lyrams.lyralibs.bukkit.LyraLibsBukkit;
import lombok.experimental.UtilityClass;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

@UtilityClass
public class PlayerUtils {

    /**
     * It sets a metadata value for a player
     *
     * @param player        The player to apply the metadata to.
     * @param metadataKey   The key of the metadata.
     * @param metadataValue The value of the metadata.
     */
    public void applyMetadata(Player player, String metadataKey, Object metadataValue) {
        player.setMetadata(metadataKey, new FixedMetadataValue(LyraLibsBukkit.getPlugin(), metadataValue));
    }

    /**
     * If the player has the metadata, return the first value in the list of metadata values
     *
     * @param player      The player to get the metadata from.
     * @param metadataKey The key of the metadata to get.
     * @return The first metadata value of the player with the given key.
     */
    @Nullable
    public MetadataValue getMetadata(Player player, String metadataKey) {
        if (!player.hasMetadata(metadataKey)) {
            return null;
        }

        List<MetadataValue> metadataValues = player.getMetadata(metadataKey);
        if (metadataValues.isEmpty()) {
            return null;
        }

        return metadataValues.get(0);
    }


    /**
     * see {@link PlayerUtils#checkPermission(CommandSender, String, boolean, boolean, String)}
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean checkPermission(CommandSender sender, String permission) {
        return PlayerUtils.checkPermission(sender, permission, false, false, null);
    }

    /**
     * see {@link PlayerUtils#checkPermission(CommandSender, String, boolean, boolean, String)}
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean checkPermission(CommandSender sender, String permission, boolean showPermission) {
        return PlayerUtils.checkPermission(sender, permission, showPermission, false, null);
    }

    /**
     * see {@link PlayerUtils#checkPermission(CommandSender, String, boolean, boolean, String)}
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean checkPermission(CommandSender sender, String permission, boolean showPermission, boolean silent) {
        return PlayerUtils.checkPermission(sender, permission, showPermission, silent, null);
    }

    /**
     * checks if the command target has a certain permission
     *
     * @param target         the target
     * @param permission     the permission
     * @param showPermission should the permission deny message show the required permission?
     * @param silent         should the target be notified of their lack of permission?
     * @param command        the command, if set to null then the permission deny message will show the executed command.
     * @return see {@link CommandSender#hasPermission(String)}
     */
    public boolean checkPermission(CommandSender target, String permission, boolean showPermission, boolean silent, @Nullable String command) {
        permission = permission.toLowerCase();

        if (target.hasPermission(permission)) {
            return true;
        }

        if (!silent) {
            if (showPermission) {
                if (command != null) {
                    target.sendMessage(LyraLibsBukkit.getMessagePrefix() + "§cYou don't have the required permission §l" + permission + " to do §l" + command + "§c!");
                } else {
                    target.sendMessage(LyraLibsBukkit.getMessagePrefix() + "§cYou don't have the required permission §l" + permission + " to do that!");
                }
            } else {
                if (command != null) {
                    target.sendMessage(LyraLibsBukkit.getMessagePrefix() + "§cYou don't have the required permission to do §l" + command + "§c!");
                } else {
                    target.sendMessage(LyraLibsBukkit.getMessagePrefix() + "§cYou don't have the required permission to do that!");
                }
            }
        }

        return false;
    }

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
        return PlayerUtils.getOfflineUUID(player.getName());
    }

}

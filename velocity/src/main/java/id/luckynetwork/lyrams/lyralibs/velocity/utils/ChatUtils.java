package id.luckynetwork.lyrams.lyralibs.velocity.utils;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class ChatUtils {

    private final LegacyComponentSerializer legacyComponentSerializer = LegacyComponentSerializer.builder().build();

    /**
     * It replaces all the color codes in the string with the color codes that the plugin uses
     *
     * @param text The text to be colorized.
     * @return The text with the color codes replaced with the color codes that the plugin uses.
     */
    public String colorize(String text) {
        text = StringUtils.replace(text, "&0", "§0");
        text = StringUtils.replace(text, "&1", "§1");
        text = StringUtils.replace(text, "&2", "§2");
        text = StringUtils.replace(text, "&3", "§3");
        text = StringUtils.replace(text, "&4", "§4");
        text = StringUtils.replace(text, "&5", "§5");
        text = StringUtils.replace(text, "&6", "§6");
        text = StringUtils.replace(text, "&7", "§7");
        text = StringUtils.replace(text, "&8", "§8");
        text = StringUtils.replace(text, "&9", "§9");
        text = StringUtils.replace(text, "&a", "§a");
        text = StringUtils.replace(text, "&b", "§b");
        text = StringUtils.replace(text, "&c", "§c");
        text = StringUtils.replace(text, "&d", "§d");
        text = StringUtils.replace(text, "&e", "§e");
        text = StringUtils.replace(text, "&f", "§f");
        text = StringUtils.replace(text, "&k", "§k");
        text = StringUtils.replace(text, "&l", "§l");

        return text;
    }

    /**
     * It replaces all ampersands in a string with dollar signs
     *
     * @param text The text to colorize
     * @return The text with the ampersand sign replaced with section sign
     */
    public String colorizeFast(String text) {
        return StringUtils.replace(text, "&", "§");
    }

    /**
     * It replaces all the color codes in a string with the color codes that are used by the Magic plugin
     *
     * @param text The text to be colorized
     * @return The text with the color codes replaced with the magic codes.
     */
    public String colorizeNonMagic(String text) {
        text = StringUtils.replace(text, "&0", "§0");
        text = StringUtils.replace(text, "&1", "§1");
        text = StringUtils.replace(text, "&2", "§2");
        text = StringUtils.replace(text, "&3", "§3");
        text = StringUtils.replace(text, "&4", "§4");
        text = StringUtils.replace(text, "&5", "§5");
        text = StringUtils.replace(text, "&6", "§6");
        text = StringUtils.replace(text, "&7", "§7");
        text = StringUtils.replace(text, "&8", "§8");
        text = StringUtils.replace(text, "&9", "§9");
        text = StringUtils.replace(text, "&a", "§a");
        text = StringUtils.replace(text, "&b", "§b");
        text = StringUtils.replace(text, "&c", "§c");
        text = StringUtils.replace(text, "&d", "§d");
        text = StringUtils.replace(text, "&e", "§e");
        text = StringUtils.replace(text, "&f", "§f");
        text = StringUtils.replace(text, "&l", "§l");

        return text;
    }

    /**
     * It removes all the color codes from a string
     *
     * @param text The text to be decolorized.
     * @return The text without the color codes.
     */
    public String decolorize(String text) {
        text = StringUtils.remove(text, "§0");
        text = StringUtils.remove(text, "§1");
        text = StringUtils.remove(text, "§2");
        text = StringUtils.remove(text, "§3");
        text = StringUtils.remove(text, "§4");
        text = StringUtils.remove(text, "§5");
        text = StringUtils.remove(text, "§6");
        text = StringUtils.remove(text, "§7");
        text = StringUtils.remove(text, "§8");
        text = StringUtils.remove(text, "§9");
        text = StringUtils.remove(text, "§a");
        text = StringUtils.remove(text, "§b");
        text = StringUtils.remove(text, "§c");
        text = StringUtils.remove(text, "§d");
        text = StringUtils.remove(text, "§e");
        text = StringUtils.remove(text, "§f");
        text = StringUtils.remove(text, "§k");
        text = StringUtils.remove(text, "§l");
        text = StringUtils.remove(text, "§m");
        text = StringUtils.remove(text, "§n");
        text = StringUtils.remove(text, "§o");

        return text;
    }

    // Sending a message to a player.
    public void sendColoredMessage(Player player, String message) {
        player.sendMessage(LegacyComponentSerializer.legacySection().deserialize(message));
    }

    // Sending a message to a player.
    public void sendMessage(Player player, String message) {
        player.sendMessage(legacyComponentSerializer.deserialize(message));
    }

    // Sending a message to a commandSource.
    public void sendColoredMessage(CommandSource player, String message) {
        player.sendMessage(LegacyComponentSerializer.legacySection().deserialize(message));
    }

    // Sending a message to a commandSource.
    public void sendMessage(CommandSource player, String message) {
        player.sendMessage(legacyComponentSerializer.deserialize(message));
    }

}

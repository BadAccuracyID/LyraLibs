package id.luckynetwork.lyrams.lyralibs.velocity.utils;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class ChatUtils {

    public String colorize(String text) {
        return StringUtils.replace(text, "&", "§");
    }

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

    public void sendMessage(Player player, String message) {
        player.sendMessage(LegacyComponentSerializer.legacySection().deserialize(message));
    }

    public void sendMessage(CommandSource player, String message) {
        player.sendMessage(LegacyComponentSerializer.legacySection().deserialize(message));
    }

}

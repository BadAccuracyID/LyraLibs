package id.luckynetwork.lyrams.lyralibs.bukkit.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

@UtilityClass
public class ChatUtils {

    /**
     * Colorizes string
     *
     * @param text the string to colorize
     * @return colorized string
     */
    public String colorize(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    /**
     * Decolorizes string
     *
     * @param text the string to decolorize
     * @return decolorize string
     */
    public String decolorize(String text) {
        return ChatColor.stripColor(text);
    }

}

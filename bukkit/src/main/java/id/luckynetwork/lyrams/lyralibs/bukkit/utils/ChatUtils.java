package id.luckynetwork.lyrams.lyralibs.bukkit.utils;

import id.luckynetwork.lyrams.lyralibs.bukkit.enums.TrueFalseType;
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

    /**
     * colorizes a string from {@link TrueFalseType} to green if true and to red if false
     *
     * @param state         the state
     * @param trueFalseType see {@link TrueFalseType}
     * @return a green {@link TrueFalseType#getIfTrue()} if true and a red {@link TrueFalseType#getIfFalse()} if false
     */
    public String colorizeTrueFalse(boolean state, TrueFalseType trueFalseType) {
        if (state) {
            return "§a" + trueFalseType.getIfTrue();
        }
        return "§c" + trueFalseType.getIfFalse();
    }

    /**
     * colorizes a string from {@link TrueFalseType} to green if true and to red if false
     *
     * @param state         the state
     * @param trueFalseType see {@link TrueFalseType}
     * @return a green {@link TrueFalseType#getIfTrue()} if true and a red {@link TrueFalseType#getIfFalse()} if false
     */
    public String colorizeTrueFalseBold(boolean state, TrueFalseType trueFalseType) {
        if (state) {
            return "§a§l" + trueFalseType.getIfTrue();
        }
        return "§c§l" + trueFalseType.getIfFalse();
    }

}

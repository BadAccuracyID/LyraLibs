package id.luckynetwork.lyrams.lyralibs.bukkit.utils;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;

@UtilityClass
public class NMSUtils {

    @Getter
    private final String nmsVersion;

    static {
        nmsVersion = Bukkit.getServer().getClass().getName().split("\\.")[3];
    }

}

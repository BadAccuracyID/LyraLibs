package id.luckynetwork.lyrams.lyralibs.bukkit.menus;

import id.luckynetwork.lyrams.lyralibs.bukkit.LyraLibsBukkit;
import id.luckynetwork.lyrams.lyralibs.bukkit.menus.listeners.MenuListener;
import id.luckynetwork.lyrams.lyralibs.bukkit.menus.menu.LyraMenu;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class MenuManager {

    private final Map<UUID, LyraMenu> openedMenus = new HashMap<>();
    private final Map<UUID, LyraMenu> lastOpenedMenus = new HashMap<>();

    public MenuManager() {
        Bukkit.getPluginManager().registerEvents(new MenuListener(), LyraLibsBukkit.getPlugin());
    }

}

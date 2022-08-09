package id.luckynetwork.lyrams.lyralibs.bukkit.menus.listeners;

import id.luckynetwork.lyrams.lyralibs.bukkit.LyraLibsBukkit;
import id.luckynetwork.lyrams.lyralibs.bukkit.menus.menu.LyraMenu;
import id.luckynetwork.lyrams.lyralibs.bukkit.menus.slots.Slot;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@AllArgsConstructor
public class MenuListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        LyraMenu menu = LyraLibsBukkit.getMenuManager().getOpenedMenus().get(player.getUniqueId());
        if (menu != null) {
            if (!menu.getName(player).equals(player.getOpenInventory().getTopInventory().getName())) {
                menu.onClose(player);
                LyraLibsBukkit.getMenuManager().getLastOpenedMenus().remove(player.getUniqueId());
                return;
            }

            event.setCancelled(true);
            if (event.getSlot() != event.getRawSlot()) {
                return;
            }

            Slot slot = menu.getSlot(event.getSlot());
            if (slot != null) {
                slot.onClick(player, event.getSlot(), event.getClick());
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        LyraMenu menu = LyraLibsBukkit.getMenuManager().getOpenedMenus().get(player.getUniqueId());
        if (menu != null) {
            menu.onClose(player);
            LyraLibsBukkit.getMenuManager().getLastOpenedMenus().remove(player.getUniqueId());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        LyraMenu menu = LyraLibsBukkit.getMenuManager().getOpenedMenus().get(player.getUniqueId());
        if (menu != null) {
            menu.onClose(player);
            LyraLibsBukkit.getMenuManager().getLastOpenedMenus().remove(player.getUniqueId());
        }
    }
}

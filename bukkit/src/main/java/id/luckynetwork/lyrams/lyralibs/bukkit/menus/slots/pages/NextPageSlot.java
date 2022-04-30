package id.luckynetwork.lyrams.lyralibs.bukkit.menus.slots.pages;

import id.luckynetwork.lyrams.lyralibs.bukkit.LyraLibsBukkit;
import id.luckynetwork.lyrams.lyralibs.bukkit.menus.menu.SwitchableMenu;
import id.luckynetwork.lyrams.lyralibs.bukkit.menus.slots.Slot;
import id.luckynetwork.lyrams.lyralibs.bukkit.builders.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class NextPageSlot extends Slot {

    private final SwitchableMenu switchableMenu;

    @Override
    public ItemStack getItem(Player player) {
        ItemBuilder item = new ItemBuilder(LyraLibsBukkit.getVersionSupport().getItemByName("HOPPER", 1, (short) 0).getType());
        item.setName("§eNext page");
        item.addLoreLine(" ");

        if (this.switchableMenu.getPage() < this.switchableMenu.getPages(player)) {
            item.addLoreLine("§7Click to go to the next page §7§l►");
            item.addLoreLine(" ");
            item.addLoreLine("§ePage: §7(§d" + this.switchableMenu.getPage() + "§7/§d" + this.switchableMenu.getPages(player) + "§7)");
        } else {
            item.addLoreLine("§cYou're on the last page.");
            item.addLoreLine(" ");
            item.addLoreLine("§ePage: §7(§d" + this.switchableMenu.getPage() + "§7/§d" + this.switchableMenu.getPages(player) + "§7)");
        }

        return item.toItemStack();
    }

    @Override
    public void onClick(Player player, int slot, ClickType clickType) {
        if (this.switchableMenu.getPage() >= this.switchableMenu.getPages(player)) {
            player.sendMessage(LyraLibsBukkit.getMessagePrefix() + "§cYou're on the last page of the menu!");
            return;
        }
        this.switchableMenu.changePage(player, 1);
    }

    @Override
    public int getSlot() {
        return 26;
    }

    @Override
    public int[] getSlots() {
        return null;
    }

}

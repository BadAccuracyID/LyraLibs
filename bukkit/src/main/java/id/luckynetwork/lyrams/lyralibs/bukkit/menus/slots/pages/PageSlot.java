package id.luckynetwork.lyrams.lyralibs.bukkit.menus.slots.pages;

import id.luckynetwork.lyrams.lyralibs.bukkit.LyraLibsBukkit;
import id.luckynetwork.lyrams.lyralibs.bukkit.menus.menu.SwitchableMenu;
import id.luckynetwork.lyrams.lyralibs.bukkit.menus.slots.Slot;
import id.luckynetwork.lyrams.lyralibs.bukkit.builders.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class PageSlot extends Slot {

    private final SwitchableMenu switchableMenu;
    private final int slot;

    @Override
    public ItemStack getItem(Player player) {
        ItemBuilder item = new ItemBuilder(LyraLibsBukkit.getVersionSupport().getItemByName("BOOK", 1, (short) 0).getType());
        item.setName(switchableMenu.getPagesTitle(player));

        item.addLoreLine("");
        item.addLoreLine("§eCurrent page: §d" + switchableMenu.getPage());
        item.addLoreLine("§eMax pages: §d" + switchableMenu.getPages(player));

        return item.toItemStack();
    }

    @Override
    public int getSlot() {
        return slot;
    }

    @Override
    public int[] getSlots() {
        return new int[]{40};
    }

}

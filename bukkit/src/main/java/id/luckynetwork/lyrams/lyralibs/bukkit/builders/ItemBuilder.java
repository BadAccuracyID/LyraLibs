package id.luckynetwork.lyrams.lyralibs.bukkit.builders;

import id.luckynetwork.lyrams.lyralibs.bukkit.LyraLibsBukkit;
import id.luckynetwork.lyrams.lyralibs.bukkit.utils.ChatUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {

    private final ItemStack itemStack;

    public ItemBuilder(Material material) {
        itemStack = new ItemStack(material, 1);
    }

    public ItemBuilder setName(String name) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatUtils.colorize(name));
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder addLoreLine(String line) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> lore = new ArrayList<>();
        if (itemMeta.hasLore()) {
            lore = new ArrayList<>(itemMeta.getLore());
        }

        lore.add(ChatUtils.colorize(line));
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setDurability(short dur) {
        itemStack.setDurability(dur);
        return this;
    }

    public ItemBuilder setDurability(int dur) {
        itemStack.setDurability((short) dur);
        return this;
    }

    public ItemBuilder setSkullOwner(String owner) {
        try {
            SkullMeta itemMeta = (SkullMeta) itemStack.getItemMeta();
            itemMeta.setOwner(owner);
            itemStack.setItemMeta(itemMeta);
        } catch (ClassCastException ignored) {
        }
        return this;
    }

    public ItemBuilder glow() {
        itemStack.addUnsafeEnchantment(LyraLibsBukkit.getVersionSupport().getEnchantmentByName("INFINITY"), 1);

        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(itemMeta);

        return this;
    }

    public ItemStack toItemStack() {
        return itemStack;
    }
}

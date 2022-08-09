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

/**
 * It's a class that allows you to create an ItemStack
 */
public class ItemBuilder {

    private final ItemStack itemStack;

    public ItemBuilder(Material material) {
        itemStack = new ItemStack(material, 1);
    }

    /**
     * Sets the name of the item to the specified name.
     *
     * @param name The name of the item
     * @return The ItemBuilder object.
     */
    public ItemBuilder setName(String name) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatUtils.colorize(name));
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    /**
     * It adds a line to the lore of the item
     *
     * @param line The line to add to the lore.
     * @return The ItemBuilder class
     */
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

    /**
     * It sets the lore of the item
     *
     * @param lore The lore you want to set.
     * @return The ItemBuilder class.
     */
    public ItemBuilder setLore(List<String> lore) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    /**
     * Sets the durability of the item
     *
     * @param dur The durability of the item.
     * @return The ItemBuilder class.
     */
    public ItemBuilder setDurability(short dur) {
        itemStack.setDurability(dur);
        return this;
    }

    /**
     * Sets the durability of the item.
     *
     * @param dur The durability of the item.
     * @return The ItemBuilder object.
     */
    public ItemBuilder setDurability(int dur) {
        itemStack.setDurability((short) dur);
        return this;
    }

    /**
     * It sets the owner of the skull
     *
     * @param owner The name of the player whose head you want to use.
     * @return The ItemBuilder class.
     */
    public ItemBuilder setSkullOwner(String owner) {
        try {
            SkullMeta itemMeta = (SkullMeta) itemStack.getItemMeta();
            itemMeta.setOwner(owner);
            itemStack.setItemMeta(itemMeta);
        } catch (ClassCastException ignored) {
        }
        return this;
    }

    /**
     * Makes the item glow by adding the enchantment
     * "INFINITY" to the item, then hides the enchantment
     *
     * @return The ItemBuilder object.
     */
    public ItemBuilder glow() {
        itemStack.addUnsafeEnchantment(LyraLibsBukkit.getVersionSupport().getEnchantmentByName("INFINITY"), 1);

        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(itemMeta);

        return this;
    }

    /**
     * It returns the item stack that the item is based on
     *
     * @return The itemStack variable is being returned.
     */
    public ItemStack toItemStack() {
        return itemStack;
    }
}

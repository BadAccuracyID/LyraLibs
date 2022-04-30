package id.luckynetwork.lyrams.lyralibs.versionsupport.v1_8_R3;

import id.luckynetwork.lyrams.lyralibs.versionsupport.VersionSupport;
import id.luckynetwork.lyrams.lyralibs.versionsupport.v1_8_R3.enums.LEffects;
import id.luckynetwork.lyrams.lyralibs.versionsupport.v1_8_R3.enums.LEnchants;
import id.luckynetwork.lyrams.lyralibs.versionsupport.v1_8_R3.enums.LItemStack;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;

public class v1_8_R3 extends VersionSupport {

    public v1_8_R3(Plugin plugin) {
        super(plugin);
    }

    @Override
    public PotionEffectType getPotionEffectByName(String name) {
        name = name.toUpperCase();
        PotionEffectType cachedPotionEffect = potionEffectTypeCache.getIfPresent(name);
        if (cachedPotionEffect != null) {
            return cachedPotionEffect;
        }

        PotionEffectType potionEffectType = null;
        try {
            potionEffectType = LEffects.valueOf(name).getEffectType();
        } catch (Exception ignored) {
        }

        if (potionEffectType == null) {
            try {
                potionEffectType = PotionEffectType.getByName(name);
            } catch (Exception ignored) {
            }
        }

        if (potionEffectType != null) {
            potionEffectTypeCache.put(name, potionEffectType);
        }

        return potionEffectType;
    }

    @Override
    public ItemStack getItemByName(String name, int amount, int damage) {
        name = name.toUpperCase();
        ItemStack itemStack = null;
        try {
            itemStack = LItemStack.valueOf(name).getItemStack();
        } catch (Exception ignored) {
        }

        if (itemStack == null) {
            Material material = Material.getMaterial(name);
            if (material != null) {
                itemStack = new ItemStack(material);
            }
        }

        if (itemStack != null) {
            if (amount == -1) {
                itemStack.setAmount(Math.max(itemStack.getMaxStackSize(), 1));
            } else {
                itemStack.setAmount(amount);
            }

            if (damage != 0) {
                itemStack.setDurability((short) damage);
            }
        }

        return itemStack;
    }

    @Override
    public Enchantment getEnchantmentByName(String name) {
        name = name.toUpperCase();
        Enchantment cachedEnchant = enchantmentCache.getIfPresent(name);
        if (cachedEnchant != null) {
            return cachedEnchant;
        }

        Enchantment enchantment = null;
        try {
            enchantment = LEnchants.valueOf(name).getEnchantment();
        } catch (Exception ignored) {
        }

        if (enchantment == null) {
            if (Enchantment.getByName(name) != null) {
                enchantment = Enchantment.getByName(name);
            }
        }

        if (enchantment != null) {
            enchantmentCache.put(name, enchantment);
        }

        return enchantment;
    }
}

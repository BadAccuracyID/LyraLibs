package id.luckynetwork.lyrams.lyralibs.versionsupport;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.TimeUnit;

public abstract class VersionSupport {

    private final Plugin plugin;
    public final Cache<String, Enchantment> enchantmentCache;
    public final Cache<String, PotionEffectType> potionEffectTypeCache;

    public VersionSupport(Plugin plugin) {
        this.plugin = plugin;
        this.enchantmentCache = CacheBuilder.newBuilder()
                .expireAfterAccess(1, TimeUnit.HOURS)
                .build();
        this.potionEffectTypeCache = CacheBuilder.newBuilder()
                .expireAfterAccess(1, TimeUnit.HOURS)
                .build();
    }

    /**
     * Returns the PotionEffectType with the given name
     *
     * @param name The name of the potion effect.
     * @return A PotionEffectType object.
     */
    public abstract PotionEffectType getPotionEffectByName(String name);

    /**
     * It returns an ItemStack of the item with the given name, amount, and damage
     *
     * @param name   The name of the item.
     * @param amount The amount of the item you want to get.
     * @param damage The damage value of the item.
     * @return An ItemStack
     */
    public abstract ItemStack getItemByName(String name, int amount, int damage);

    /**
     * Returns the Enchantment with the given name, or null if it doesn't exist
     *
     * @param name The name of the enchantment.
     * @return The enchantment with the given name.
     */
    public abstract Enchantment getEnchantmentByName(String name);
}

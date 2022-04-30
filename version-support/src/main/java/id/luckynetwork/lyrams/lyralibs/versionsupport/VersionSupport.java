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

    public abstract PotionEffectType getPotionEffectByName(String name);

    public abstract ItemStack getItemByName(String name, int amount, int damage);

    public abstract Enchantment getEnchantmentByName(String name);
}

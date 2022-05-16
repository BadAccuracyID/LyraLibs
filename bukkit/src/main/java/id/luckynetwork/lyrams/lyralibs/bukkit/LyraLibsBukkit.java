package id.luckynetwork.lyrams.lyralibs.bukkit;

import id.luckynetwork.lyrams.lyralibs.bukkit.menus.MenuManager;
import id.luckynetwork.lyrams.lyralibs.bukkit.utils.NMSUtils;
import id.luckynetwork.lyrams.lyralibs.versionsupport.VersionSupport;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;

public class LyraLibsBukkit {

    @Getter
    private static LyraLibsBukkit instance;

    private final Plugin plugin;

    private String messagePrefix;
    private MenuManager menuManager;
    private VersionSupport versionSupport;

    private LyraLibsBukkit(Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * It creates a new instance of LyraLibsBukkit, sets the message prefix, and sets the instance variable to the new
     * instance
     *
     * @param plugin The Plugin instance
     * @param messagePrefix The prefix for all messages sent by the plugin.
     */
    public static void initialize(Plugin plugin, String messagePrefix) {
        LyraLibsBukkit libs = new LyraLibsBukkit(plugin);
        libs.messagePrefix = messagePrefix;
        libs.loadVersionSupport();

        instance = libs;
    }

    /**
     * Initialize the menu manager.
     */
    public static void initializeMenu() {
        instance.menuManager = new MenuManager();
    }

    /**
     * This function returns the plugin instance.
     *
     * @return The plugin instance.
     */
    public static Plugin getPlugin() {
        return instance.plugin;
    }

    /**
     * This function returns the value of the messagePrefix variable.
     *
     * @return The message prefix.
     */
    public static String getMessagePrefix() {
        return instance.messagePrefix;
    }

    /**
     * This function returns the menu manager.
     *
     * @return The menuManager object.
     */
    public static MenuManager getMenuManager() {
        return instance.menuManager;
    }

    /**
     * If the instance is null, create a new instance of the class and return the versionSupport variable.
     *
     * @return The versionSupport field of the instance of the class.
     */
    public static VersionSupport getVersionSupport() {
        return instance.versionSupport;
    }

    /**
     * It loads the version support class based on the server version
     */
    private void loadVersionSupport() {
        String version = NMSUtils.getNmsVersion();
        plugin.getLogger().info("Loading support for " + version);
        try {
            Class<?> support;
            switch (version) {
                case "v1_8_R3": {
                    support = Class.forName("id.luckynetwork.lyrams.lyralibs.versionsupport.v1_8_R3.v1_8_R3");
                    plugin.getLogger().info("Loaded version support v1_8_R3");
                    break;
                }
                case "v1_9_R1":
                case "v1_9_R2":
                case "v1_10_R1":
                case "v1_11_R1":
                case "v1_12_R1": {
                    support = Class.forName("id.luckynetwork.lyrams.lyralibs.versionsupport.v1_12_R1.v1_12_R1");
                    plugin.getLogger().info("Loaded version support v1_12_R1");
                    break;
                }
                case "v1_13_R1":
                case "v1_14_R1":
                case "v1_15_R1":
                case "v1_16_R1":
                case "v1_17_R1":
                case "v1_18_R1": {
                    support = Class.forName("id.luckynetwork.lyrams.lyralibs.versionsupport.v1_13_R1.v1_13_R1");
                    plugin.getLogger().info("Loaded version support v1_13_R1");
                    break;
                }
                default: {
                    plugin.getLogger().severe("Unsupported server version!");
                    Bukkit.getPluginManager().disablePlugin(plugin);
                    return;
                }
            }

            versionSupport = (VersionSupport) support.getConstructor(Class.forName("org.bukkit.plugin.Plugin")).newInstance(plugin);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            plugin.getLogger().severe("Unsupported server version!");
            Bukkit.getPluginManager().disablePlugin(plugin);
        }
    }

}

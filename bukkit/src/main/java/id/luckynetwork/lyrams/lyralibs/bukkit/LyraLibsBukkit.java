package id.luckynetwork.lyrams.lyralibs.bukkit;

import id.luckynetwork.dev.novenag.injectoragent.NovenaInjector;
import id.luckynetwork.lyrams.lyralibs.bukkit.dependency.BukkitDependencyHelper;
import id.luckynetwork.lyrams.lyralibs.bukkit.menus.MenuManager;
import id.luckynetwork.lyrams.lyralibs.bukkit.utils.NMSUtils;
import id.luckynetwork.lyrams.lyralibs.core.dependency.DependencyHelper;
import id.luckynetwork.lyrams.lyralibs.versionsupport.VersionSupport;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.jar.JarFile;

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
     * @param plugin        The Plugin instance
     * @param messagePrefix The prefix for all messages sent by the plugin.
     */
    public static void initialize(Plugin plugin, String messagePrefix) {
        LyraLibsBukkit libs = new LyraLibsBukkit(plugin);
        libs.messagePrefix = messagePrefix;
        libs.loadVersionSupport();

        instance = libs;
    }

    /**
     * It downloads the dependencies from the dependencies.json file and loads them into the plugin's classpath
     */
    public static void downloadDependencies() {
        Map<String, String> dependencies = new HashMap<>();
        dependencies.put("mongo-java-driver-3.12.11.jar", "https://search.maven.org/remotecontent?filepath=org/mongodb/mongo-java-driver/3.12.11/mongo-java-driver-3.12.11.jar");
        dependencies.put("jedis-3.7.0.jar", "https://search.maven.org/remotecontent?filepath=redis/clients/jedis/3.7.0/jedis-3.7.0.jar");
        dependencies.put("gson-2.9.0.jar", "https://search.maven.org/remotecontent?filepath=com/google/code/gson/gson/2.9.0/gson-2.9.0.jar");

        try {
            File librariesDirectory = new File("plugins/" + LyraLibsBukkit.getPlugin().getDescription().getName() + "/libs");
            if (!librariesDirectory.exists()) {
                librariesDirectory.mkdirs();
            }

            if (hasNovenaInjector()) {
                DependencyHelper helper = new DependencyHelper();
                helper.download(dependencies, librariesDirectory.toPath());

                Arrays.stream(Objects.requireNonNull(librariesDirectory.listFiles()))
                        .filter(it -> it.getName().endsWith(".jar"))
                        .map(it -> {
                            try {
                                return new JarFile(it);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .forEach(NovenaInjector::appendJarFile);
            } else {
                BukkitDependencyHelper.loadDependencies(LyraLibsBukkit.getPlugin().getClass().getClassLoader(), dependencies, librariesDirectory);
            }
        } catch (IOException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
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
                case "v1_18_R1":
                case "v1_18_R2": {
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
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            e.printStackTrace();
            plugin.getLogger().severe("Unsupported server version!");
            Bukkit.getPluginManager().disablePlugin(plugin);
        }
    }

    private static boolean hasNovenaInjector() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        List<String> jvmArgs = runtimeMXBean.getInputArguments();

        System.out.println(jvmArgs.toString());

        return jvmArgs.stream().anyMatch(it -> it.contains("NovenaInjector.jar"));
    }

}

package id.luckynetwork.lyrams.lyralibs.bukkit.config;

import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class ConfigFile extends YamlConfiguration {

    @Getter
    private final File file;

    @SneakyThrows
    public ConfigFile(Plugin plugin, String name) {
        this.file = new File(plugin.getDataFolder(), name);

        if (!this.file.exists()) {
            plugin.saveResource(name, false);
        }

        this.load(file);
    }

    /**
     * This function saves the file.
     */
    public void save() {
        try {
            this.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

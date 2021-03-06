package id.luckynetwork.lyrams.lyralibs.bukkit.dependency;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import id.luckynetwork.lyrams.lyralibs.bukkit.LyraLibsBukkit;
import id.luckynetwork.lyrams.lyralibs.core.dependency.DependencyHelper;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

@UtilityClass
public class BukkitDependencyHelper {

    static {
        String property = System.getProperty("java.version");
        if (property.startsWith("1.")) {
            property = property.substring(2, 3);
        } else {
            int dot = property.indexOf(".");
            if (dot != -1) {
                property = property.substring(0, dot);
            }
        }

        LyraLibsBukkit.getPlugin().getLogger().log(Level.INFO, "Java version: " + property);

        int version = Integer.parseInt(property);
        if (version >= 11) {
            LyraLibsBukkit.getPlugin().getLogger().log(Level.WARNING, "You are using Java version " + property + " which might not support " +
                    "dependency injecting. If you encounter errors while injecting dependencies, please add " +
                    "'--add-opens java.base/java.net=ALL-UNNAMED --add-opens java.base/java.lang.invoke=ALL-UNNAMED' to your JVM arguments.");
        }
    }

    /**
     * Loads dependency from map
     *
     * @param classLoader        the classLoader
     * @param dependencyMap      the dependency map
     * @param librariesDirectory where to put the libraries
     * @throws IOException            if you're gay
     * @throws IllegalAccessException if you're gay, probs your java is too high
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void loadDependencies(ClassLoader classLoader, Map<String, String> dependencyMap, File librariesDirectory) throws IOException, IllegalAccessException {
        LyraLibsBukkit.getPlugin().getLogger().log(Level.INFO, "Loading dependencies...");
        if (!librariesDirectory.exists()) {
            librariesDirectory.mkdirs();
        }

        DependencyHelper helper = new DependencyHelper(classLoader);
        helper.download(dependencyMap, librariesDirectory.toPath());
        helper.loadDir(librariesDirectory.toPath());

        LyraLibsBukkit.getPlugin().getLogger().log(Level.INFO, "Dependencies loaded!");
    }

    /**
     * It reads a JSON file from the classpath, parses it into a list of dependencies, and returns a map of dependency
     * names to URLs
     *
     * @param classLoader The class loader to use to load the file.
     * @param fileName    The name of the file that contains the JSON data.
     * @param exclude     A list of dependencies to exclude from the map.
     * @return A map of dependencies.
     */
    @SuppressWarnings("deprecation")
    public Map<String, String> createDependencyMapFromJson(ClassLoader classLoader, String fileName, List<String> exclude) {
        LyraLibsBukkit.getPlugin().getLogger().log(Level.INFO, "Creating dependency map...");
        Map<String, String> dependencyMap = new HashMap<>();

        try (InputStream stream = classLoader.getResourceAsStream(fileName)) {
            assert stream != null;
            try (InputStreamReader reader = new InputStreamReader(stream)) {
                JsonParser parser = new JsonParser();
                JsonArray dependencies = parser.parse(reader).getAsJsonArray();
                if (dependencies.size() == 0) {
                    return dependencyMap;
                }

                for (JsonElement element : dependencies) {
                    JsonObject dependency = element.getAsJsonObject();
                    if (!exclude.contains(dependency.get("name").getAsString())) {
                        dependencyMap.put(
                                dependency.get("name").getAsString(),
                                dependency.get("url").getAsString()
                        );
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return dependencyMap;
    }

    /**
     * > This function takes a classloader and a filename and returns a map of dependencies
     *
     * @param classLoader The classloader to use to load the file.
     * @param fileName    The name of the file that contains the JSON data.
     * @return A map of dependencies.
     */
    public Map<String, String> createDependencyMapFromJson(ClassLoader classLoader, String fileName) {
        return createDependencyMapFromJson(classLoader, fileName, new ArrayList<>());
    }


}

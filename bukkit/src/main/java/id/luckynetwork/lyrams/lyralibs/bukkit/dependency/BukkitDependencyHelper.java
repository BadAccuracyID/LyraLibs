package id.luckynetwork.lyrams.lyralibs.bukkit.dependency;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import id.luckynetwork.lyrams.lyralibs.core.dependency.DependencyHelper;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class BukkitDependencyHelper {

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
        if (!librariesDirectory.exists()) {
            librariesDirectory.mkdirs();
        }

        DependencyHelper helper = new DependencyHelper(classLoader);
        helper.download(dependencyMap, librariesDirectory.toPath());
        helper.loadDir(librariesDirectory.toPath());
    }

    @Nullable
    public Map<String, String> createDependencyMapFromJsonFile(File jsonFile, List<String> exclude) {
        Map<String, String> dependencyMap = new HashMap<>();

        try (InputStream stream = new FileInputStream(jsonFile);
             InputStreamReader reader = new InputStreamReader(stream)) {
            JsonArray dependencies = JsonParser.parseReader(reader).getAsJsonArray();
            if (dependencies.size() == 0) {
                return null;
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
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dependencyMap;
    }


}

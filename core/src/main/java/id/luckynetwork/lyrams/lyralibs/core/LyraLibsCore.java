package id.luckynetwork.lyrams.lyralibs.core;

import id.luckynetwork.lyrams.lyralibs.core.dependency.DependencyHelper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LyraLibsCore {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void downloadDependencies(ClassLoader classLoader, File libsDirectory) {
        Map<String, String> dependencies = new HashMap<>();
        dependencies.put("mongo-java-driver-3.12.11.jar", "https://search.maven.org/remotecontent?filepath=org/mongodb/mongo-java-driver/3.12.11/mongo-java-driver-3.12.11.jar");
        dependencies.put("jedis-3.7.0.jar", "https://search.maven.org/remotecontent?filepath=redis/clients/jedis/3.7.0/jedis-3.7.0.jar");
        dependencies.put("gson-2.9.0.jar", "https://search.maven.org/remotecontent?filepath=com/google/code/gson/gson/2.9.0/gson-2.9.0.jar");
        dependencies.put("HikariCP-5.0.1.jar", "https://search.maven.org/remotecontent?filepath=com/zaxxer/HikariCP/5.0.1/HikariCP-5.0.1.jar");

        try {
            if (!libsDirectory.exists()) {
                libsDirectory.mkdirs();
            }

            DependencyHelper helper = new DependencyHelper(classLoader);
            helper.download(dependencies, libsDirectory.toPath());
            helper.loadDir(libsDirectory.toPath());
        } catch (IOException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}

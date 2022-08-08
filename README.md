## How to add dependency
#### Gradle:
``` gradle
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/BadAccuracyID/LyraLibs")
            credentials {
                username = project.findProperty("gpr.user") ?: System.getenv("GITHUB_USERNAME")
                password = project.findProperty("gpr.key") ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
    
    // choose the module you want to add
    dependencies {
        implementation 'com.badaccuracy.lyralibs:core:CHECK_PACKAGES_VERSION'
        implementation 'com.badaccuracy.lyralibs:bukkit:CHECK_PACKAGES_VERSION'
        implementation 'com.badaccuracy.lyralibs:velocity:CHECK_PACKAGES_VERSION'
    }
```

#### Create gradle.properties:
```
gpr.user=BadAccuracyID
# read-only
gpr.key=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
```

## Credits

- [Midnight by AndyReckt](https://github.com/AndyReckt/Midnight)
- [BedWarsProxy by Andrei1058](https://github.com/andrei1058/BedWarsProxy)
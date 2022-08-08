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
Note: please exclude gradle.properties from the repository, or it'll break the key, k thxbye (yea it's super exploitable, idc, i'll fix it one day)

#### Add github actions secret token
```
USERNAME=BadAccuracyID
TOKEN=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
```

## Credits

- [Midnight by AndyReckt](https://github.com/AndyReckt/Midnight)
- [BedWarsProxy by Andrei1058](https://github.com/andrei1058/BedWarsProxy)
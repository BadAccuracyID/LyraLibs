## How to add dependency

``` gradle
    repositories {
        maven {
            url 'https://BadAccuracyID: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@maven.pkg.github.com/BadAccuracyID/LyraLibs'
        }
    }
    
    // choose the module you want to add
    dependencies {
        implementation 'com.badaccuracy.lyralibs:core:CHECK_PACKAGES_VERSION'
        implementation 'com.badaccuracy.lyralibs:bukkit:CHECK_PACKAGES_VERSION'
        implementation 'com.badaccuracy.lyralibs:velocity:CHECK_PACKAGES_VERSION'
    }
```

## Credits

- [Midnight by AndyReckt](https://github.com/AndyReckt/Midnight)
- [BedWarsProxy by Andrei1058](https://github.com/andrei1058/BedWarsProxy)
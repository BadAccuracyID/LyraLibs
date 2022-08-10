![Build](https://img.shields.io/github/workflow/status/BadAccuracyID/LyraLibs/Java%20CI%20with%20Gradle?style=for-the-badge)

## How to add dependency

### Version

![Core](https://img.shields.io/maven-central/v/id.luckynetwork.lyrams.lyralibs/core?label=core&style=for-the-badge)
![Bukkit](https://img.shields.io/maven-central/v/id.luckynetwork.lyrams.lyralibs/bukkit?label=bukkit&style=for-the-badge)
![Velocity](https://img.shields.io/maven-central/v/id.luckynetwork.lyrams.lyralibs/velocity?label=velocity&style=for-the-badge)

### Maven
```xml

<!-- choose between bukkit or velocity -->
<dependencies>
    <dependency>
        <groupId>id.luckynetwork.lyrams.lyralibs</groupId>
        <artifactId>core</artifactId>
        <version>CHECK_VERSION</version>
    </dependency>
    <dependency>
        <groupId>id.luckynetwork.lyrams.lyralibs</groupId>
        <artifactId>bukkit</artifactId>
        <version>CHECK_VERSION</version>
    </dependency>
    <dependency>
        <groupId>id.luckynetwork.lyrams.lyralibs</groupId>
        <artifactId>velocity</artifactId>
        <version>CHECK_VERSION</version>
    </dependency>
</dependencies>

```

### Gradle
```gradle
    // choose between bukkit or velocity
    dependencies {
        implementation 'id.luckynetwork.lyrams.lyralibs:core:CHECK_VERSION'
        implementation 'id.luckynetwork.lyrams.lyralibs:bukkit:CHECK_VERSION'
        implementation 'id.luckynetwork.lyrams.lyralibs:velocity:CHECK_VERSION'
    }
```

## Credits

- [Midnight by AndyReckt](https://github.com/AndyReckt/Midnight)
- [BedWarsProxy by Andrei1058](https://github.com/andrei1058/BedWarsProxy)
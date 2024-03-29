package id.luckynetwork.lyrams.lyralibs.versionsupport.v1_16_R3.enums;

import lombok.Getter;
import org.bukkit.potion.PotionEffectType;

public enum LEffects {

    ABS(PotionEffectType.ABSORPTION),
    DOLPHINS(PotionEffectType.DOLPHINS_GRACE),
    DOLPHIN(PotionEffectType.DOLPHINS_GRACE),
    DOLPHINSGRACE(PotionEffectType.DOLPHINS_GRACE),
    DOLPHINGRACE(PotionEffectType.DOLPHINS_GRACE),
    GLOW(PotionEffectType.GLOWING),
    FLOAT(PotionEffectType.LEVITATION),
    FLY(PotionEffectType.LEVITATION),
    LUCKY(PotionEffectType.LUCK),
    UNLUCKY(PotionEffectType.UNLUCK),
    BADLUCK(PotionEffectType.UNLUCK),
    BLIND(PotionEffectType.BLINDNESS),
    NAUSEA(PotionEffectType.CONFUSION),
    RESISTANCE(PotionEffectType.DAMAGE_RESISTANCE),
    RES(PotionEffectType.DAMAGE_RESISTANCE),
    FIRERESISTANCE(PotionEffectType.FIRE_RESISTANCE),
    FIRERES(PotionEffectType.FIRE_RESISTANCE),
    FRES(PotionEffectType.FIRE_RESISTANCE),
    HARMING(PotionEffectType.HARM),
    DAMAGE(PotionEffectType.HARM),
    INSTANTDAMAGE(PotionEffectType.HARM),
    INSTANTHEALTH(PotionEffectType.HEAL),
    HEALTHBOOST(PotionEffectType.HEALTH_BOOST),
    HEALTH(PotionEffectType.HEALTH_BOOST),
    STARVATION(PotionEffectType.HUNGER),
    STARVE(PotionEffectType.HUNGER),
    STRENGTH(PotionEffectType.INCREASE_DAMAGE),
    STR(PotionEffectType.INCREASE_DAMAGE),
    INVIS(PotionEffectType.INVISIBILITY),
    GHOST(PotionEffectType.INVISIBILITY),
    JUMPBOOST(PotionEffectType.JUMP),
    NIGHTVISION(PotionEffectType.NIGHT_VISION),
    NIGHT(PotionEffectType.NIGHT_VISION),
    SLOWNESS(PotionEffectType.SLOW),
    SLOWFALL(PotionEffectType.SLOW_FALLING),
    SLOWFALLING(PotionEffectType.SLOW_FALLING),
    MININGFATIGUE(PotionEffectType.SLOW_DIGGING),
    MINERFATIGUE(PotionEffectType.SLOW_DIGGING),
    FATIGUE(PotionEffectType.SLOW_DIGGING),
    REGEN(PotionEffectType.REGENERATION),
    HASTE(PotionEffectType.FAST_DIGGING),
    WATERBREATH(PotionEffectType.WATER_BREATHING),
    WATERBREATHING(PotionEffectType.WATER_BREATHING);

    @Getter
    private final PotionEffectType effectType;

    LEffects(PotionEffectType effectType) {
        this.effectType = effectType;
    }
}

package id.luckynetwork.lyrams.lyralibs.bukkit.enums;

import lombok.Getter;
import lombok.Setter;

// It's a custom enum that can be used to get the string value of a boolean.
public enum TrueFalseType {

    ON_OFF("On", "Off"),
    DEFAULT("True", "False"),
    ENABLED("Enabled", "Disabled");

    @Getter
    @Setter
    String ifTrue, ifFalse;

    TrueFalseType(String ifTrue, String ifFalse) {
        this.ifTrue = ifTrue;
        this.ifFalse = ifFalse;
    }
}

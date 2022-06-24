package id.luckynetwork.lyrams.lyralibs.core.enums;

import lombok.Getter;
import lombok.Setter;

// It's a custom enum that can be used to get the string value of a boolean.
public enum TrueFalseType {

    ON_OFF("On", "Off"),
    DEFAULT("True", "False"),
    ENABLED("Enabled", "Disabled");

    @Getter
    @Setter
    private String ifTrue, ifFalse;

    TrueFalseType(String ifTrue, String ifFalse) {
        this.ifTrue = ifTrue;
        this.ifFalse = ifFalse;
    }

    public String colorizeBold(boolean state) {
        if (state) {
            return "§a§l" + ifTrue;
        }
        return "§c§l" + ifFalse;
    }

    public String colorize(boolean state) {
        if (state) {
            return "§a" + ifTrue;
        }
        return "§c" + ifFalse;
    }
}

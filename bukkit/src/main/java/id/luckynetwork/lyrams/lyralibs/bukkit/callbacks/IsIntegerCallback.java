package id.luckynetwork.lyrams.lyralibs.bukkit.callbacks;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class IsIntegerCallback {

    private boolean isInteger = false;
    private int value = -1;

    public IsIntegerCallback setValue(int value) {
        this.value = value;
        return this;
    }

    public IsIntegerCallback setInteger(boolean isInteger) {
        this.isInteger = isInteger;
        return this;
    }

    /**
     * Checks if a string is parsable to an Integer
     *
     * @param s the string
     * @return {@link IsIntegerCallback}
     */
    public static IsIntegerCallback asInteger(String s) {
        IsIntegerCallback callback = new IsIntegerCallback(false, 0);
        if (s == null) {
            return callback;
        }

        try {
            return callback.setInteger(true).setValue(Integer.parseInt(s));
        } catch (Exception ignored) {
            return callback;
        }
    }
}

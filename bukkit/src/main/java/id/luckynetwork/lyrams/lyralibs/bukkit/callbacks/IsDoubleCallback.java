package id.luckynetwork.lyrams.lyralibs.bukkit.callbacks;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class IsDoubleCallback {

    private boolean isDouble = false;
    private double value = -1.0;

    public IsDoubleCallback setValue(double value) {
        this.value = value;
        return this;
    }

    public IsDoubleCallback setDouble(boolean isDouble) {
        this.isDouble = isDouble;
        return this;
    }

    /**
     * Checks if a string is parsable to a double
     *
     * @param s the string
     * @return {@link IsDoubleCallback}
     */
    public static IsDoubleCallback asDouble(String s) {
        IsDoubleCallback callback = new IsDoubleCallback(false, 0.0);
        if (s == null) {
            return callback;
        }

        try {
            return callback.setDouble(true).setValue(Double.parseDouble(s));
        } catch (Exception ignored) {
            return callback;
        }
    }
}

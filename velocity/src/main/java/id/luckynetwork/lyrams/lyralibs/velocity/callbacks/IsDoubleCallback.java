package id.luckynetwork.lyrams.lyralibs.velocity.callbacks;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class IsDoubleCallback {

    private boolean isDouble = false;
    private double value = -1.0;

    /**
     * This function sets the value of the variable value to the value of the parameter value and returns this.
     *
     * @param value The value to compare against.
     * @return The object itself.
     */
    public IsDoubleCallback setValue(double value) {
        this.value = value;
        return this;
    }

    /**
     * This function sets the isDouble variable to the value of the isDouble parameter.
     *
     * @param isDouble Whether or not the callback is a double callback.
     * @return The object itself.
     */
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

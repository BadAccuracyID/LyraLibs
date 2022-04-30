package id.luckynetwork.lyrams.lyralibs.core.config;

import org.jetbrains.annotations.NotNull;

public class LConfigurationOptions {

    private final LConfiguration configuration;

    private char pathSeparator;
    private boolean copyDefaults;

    /**
     * Constructs a new set of {@link LConfigurationOptions}.
     *
     * @param configuration The {@link LConfiguration} to create the
     *                      {@link LConfigurationOptions} for.
     */
    protected LConfigurationOptions(@NotNull final LConfiguration configuration) {
        this.configuration = configuration;
        this.pathSeparator = '.';
        this.copyDefaults = false;
    }

    /**
     * Returns the {@link LConfiguration} that this object is responsible for.
     *
     * @return Parent configuration
     */
    @NotNull
    public LConfiguration configuration() {
        return this.configuration;
    }

    /**
     * Gets the char that will be used to separate {@link
     * LConfigurationSection}s
     * <p>
     * This value does not affect how the {@link LConfiguration} is stored,
     * only in how you access the data. The default value is '.'.
     *
     * @return Path separator
     */
    public final char pathSeparator() {
        return this.pathSeparator;
    }

    /**
     * Sets the char that will be used to separate {@link
     * LConfigurationSection}s
     * <p>
     * This value does not affect how the {@link LConfiguration} is stored,
     * only in how you access the data. The default value is '.'.
     *
     * @param pathSeparator Path separator
     * @return This object, for chaining
     */
    @NotNull
    public LConfigurationOptions pathSeparator(char pathSeparator) {
        this.pathSeparator = pathSeparator;
        return this;
    }

    /**
     * Checks if the {@link LConfiguration} should copy values from its default
     * {@link LConfiguration} directly.
     * <p>
     * If this is true, all values in the default Configuration will be
     * directly copied, making it impossible to distinguish between values
     * that were set and values that are provided by default. As a result,
     * {@link LConfigurationSection#contains(String)} will always
     * return the same value as {@link
     * LConfigurationSection#isSet(String)}. The default value is
     * false.
     *
     * @return Whether defaults are directly copied
     */
    public final boolean copyDefaults() {
        return this.copyDefaults;
    }

    /**
     * Sets if the {@link LConfiguration} should copy values from its default
     * {@link LConfiguration} directly.
     * <p>
     * If this is true, all values in the default Configuration will be
     * directly copied, making it impossible to distinguish between values
     * that were set and values that are provided by default. As a result,
     * {@link LConfigurationSection#contains(String)} will always
     * return the same value as {@link
     * LConfigurationSection#isSet(String)}. The default value is
     * false.
     *
     * @param copyDefaults Whether defaults are directly copied
     * @return This object, for chaining
     */
    @NotNull
    public LConfigurationOptions copyDefaults(boolean copyDefaults) {
        this.copyDefaults = copyDefaults;
        return this;
    }

}

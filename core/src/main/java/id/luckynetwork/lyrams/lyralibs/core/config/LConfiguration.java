package id.luckynetwork.lyrams.lyralibs.core.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface LConfiguration extends LConfigurationSection {

    /**
     * Sets the default value of the given path as provided.
     * <p>
     * If no source {@link LConfiguration} was provided as a default
     * collection, then a new {@link LMemoryConfiguration} will be created to
     * hold the new default value.
     * <p>
     * If value is null, the value will be removed from the default
     * Configuration source.
     *
     * @param path  Path of the value to set.
     * @param value Value to set the default to.
     * @throws IllegalArgumentException Thrown if path is null.
     */
    @Override
    void addDefault(@NotNull String path, @Nullable Object value);

    /**
     * Sets the default values of the given paths as provided.
     * <p>
     * If no source {@link LConfiguration} was provided as a default
     * collection, then a new {@link LMemoryConfiguration} will be created to
     * hold the new default values.
     *
     * @param defs A map of Path{@literal ->}Values to add to defaults.
     * @throws IllegalArgumentException Thrown if defaults is null.
     */
    void addDefaults(@NotNull Map<String, Object> defs);

    /**
     * Sets the default values of the given paths as provided.
     * <p>
     * If no source {@link LConfiguration} was provided as a default
     * collection, then a new {@link LMemoryConfiguration} will be created to
     * hold the new default value.
     * <p>
     * This method will not hold a reference to the specified Configuration,
     * nor will it automatically update if that Configuration ever changes. If
     * you require this, you should set the default source with {@link
     * #setDefaults(LConfiguration)}.
     *
     * @param defs A configuration holding a list of defaults to copy.
     * @throws IllegalArgumentException Thrown if defaults is null or this.
     */
    void addDefaults(@NotNull LConfiguration defs);

    /**
     * Sets the source of all default values for this {@link LConfiguration}.
     * <p>
     * If a previous source was set, or previous default values were defined,
     * then they will not be copied to the new source.
     *
     * @param defs New source of default values for this configuration.
     * @throws IllegalArgumentException Thrown if defaults is null or this.
     */
    void setDefaults(@NotNull LConfiguration defs);

    /**
     * Gets the source {@link LConfiguration} for this configuration.
     * <p>
     * If no configuration source was set, but default values were added, then
     * a {@link LMemoryConfiguration} will be returned. If no source was set
     * and no defaults were set, then this method will return null.
     *
     * @return Configuration source for default values, or null if none exist.
     */
    @Nullable
    LConfiguration getDefaults();

    /**
     * Gets the {@link LConfigurationOptions} for this {@link LConfiguration}.
     * <p>
     * All setters through this method are chainable.
     *
     * @return Options for this configuration
     */
    @NotNull
    LConfigurationOptions options();

}

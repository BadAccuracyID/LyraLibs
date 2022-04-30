package id.luckynetwork.lyrams.lyralibs.core.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class LMemoryConfiguration extends LMemorySection implements LConfiguration {

    protected LConfiguration defs;
    protected LMemoryConfigurationOptions options;

    /**
     * Creates an empty {@link LMemoryConfiguration} with no default values.
     */
    public LMemoryConfiguration() {
        // Do nothing.
    }

    /**
     * Creates an empty {@link LMemoryConfiguration} using the specified {@link
     * LConfiguration} as a source for all default values.
     *
     * @param defs Default value provider
     * @throws IllegalArgumentException Thrown if defaults is null
     */
    public LMemoryConfiguration(@Nullable final LConfiguration defs) {
        this.defs = defs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addDefault(@NotNull String path, @Nullable Object value) {
        if (this.defs == null) {
            this.defs = new LMemoryConfiguration();
        }
        this.defs.set(path, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addDefaults(@NotNull final Map<String, Object> defs) {
        for (final Map.Entry<String, Object> entry : defs.entrySet()) {
            this.addDefault(entry.getKey(), entry.getValue());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addDefaults(@NotNull final LConfiguration defs) {
        this.addDefaults(defs.getValues(true));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDefaults(@NotNull final LConfiguration defs) {
        this.defs = defs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public LConfiguration getDefaults() {
        return this.defs;
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public @Nullable LConfigurationSection getParent() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public LMemoryConfigurationOptions options() {
        if (this.options == null) {
            this.options = new LMemoryConfigurationOptions(this);
        }
        return this.options;
    }
}

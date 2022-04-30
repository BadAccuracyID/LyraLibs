package id.luckynetwork.lyrams.lyralibs.core.config;

import org.jetbrains.annotations.NotNull;

public class LMemoryConfigurationOptions extends LConfigurationOptions {

    /**
     * Constructs a new set of {@link LMemoryConfigurationOptions}.
     *
     * @param configuration The {@link LMemoryConfiguration} to create the
     *                      {@link LMemoryConfigurationOptions} for.
     * @see LConfigurationOptions#LConfigurationOptions(LConfiguration)
     */
    protected LMemoryConfigurationOptions(@NotNull final LMemoryConfiguration configuration) {
        super(configuration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public LMemoryConfiguration configuration() {
        return (LMemoryConfiguration) super.configuration();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public LMemoryConfigurationOptions pathSeparator(final char pathSeparator) {
        super.pathSeparator(pathSeparator);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public LMemoryConfigurationOptions copyDefaults(final boolean copyDefaults) {
        super.copyDefaults(copyDefaults);
        return this;
    }
}

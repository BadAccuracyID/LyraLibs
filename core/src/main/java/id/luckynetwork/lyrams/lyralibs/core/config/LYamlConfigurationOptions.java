package id.luckynetwork.lyrams.lyralibs.core.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LYamlConfigurationOptions extends LFileConfigurationOptions {

    private int indent;

    /**
     * Constructs a new set of {@link LYamlConfigurationOptions}.
     *
     * @param configuration The {@link LYamlConfiguration} to create the
     *                      {@link LYamlConfigurationOptions} for.
     * @see LFileConfigurationOptions#LFileConfigurationOptions(LMemoryConfiguration)
     */
    protected LYamlConfigurationOptions(@NotNull final LYamlConfiguration configuration) {
        super(configuration);
        this.indent = 2;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public LYamlConfiguration configuration() {
        return (LYamlConfiguration) super.configuration();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public LYamlConfigurationOptions pathSeparator(final char pathSeparator) {
        super.pathSeparator(pathSeparator);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public LYamlConfigurationOptions copyDefaults(final boolean copyDefaults) {
        super.copyDefaults(copyDefaults);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public LYamlConfigurationOptions header(@Nullable final String header) {
        super.header(header);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public LYamlConfigurationOptions copyHeader(final boolean copyHeader) {
        super.copyHeader(copyHeader);
        return this;
    }

    /**
     * Gets how much spaces should be used to indent each line.
     * <p>
     * The minimum value this may be is 2, and the maximum is 9.
     *
     * @return How much to indent by
     */
    public int indent() {
        return this.indent;
    }

    /**
     * Sets how much spaces should be used to indent each line.
     * <p>
     * The minimum value this may be is 2, and the maximum is 9.
     *
     * @param indent New indent
     * @return This object, for chaining
     */
    @NotNull
    public LYamlConfigurationOptions indent(final int indent) {
        if (indent < 2) {
            throw new IllegalArgumentException("Indent must be at least 2 characters.");
        }
        if (indent > 9) {
            throw new IllegalArgumentException("Indent cannot be greater than 9 characters.");
        }
        this.indent = indent;
        return this;
    }
}

package id.luckynetwork.lyrams.lyralibs.core.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LFileConfigurationOptions extends LMemoryConfigurationOptions {

    private String header;
    private boolean copyHeader;

    /**
     * Constructs a new set of {@link LFileConfigurationOptions}.
     *
     * @param configuration The {@link LMemoryConfiguration} to create the
     *                      {@link LFileConfigurationOptions} for.
     * @see LMemoryConfigurationOptions#LMemoryConfigurationOptions(LMemoryConfiguration)
     */
    protected LFileConfigurationOptions(@NotNull final LMemoryConfiguration configuration) {
        super(configuration);
        this.header = null;
        this.copyHeader = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public LFileConfiguration configuration() {
        return (LFileConfiguration) super.configuration();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public LFileConfigurationOptions pathSeparator(final char pathSeparator) {
        super.pathSeparator(pathSeparator);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public LFileConfigurationOptions copyDefaults(final boolean copyDefaults) {
        super.copyDefaults(copyDefaults);
        return this;
    }

    /**
     * Gets the header that will be applied to the top of the saved output.
     * <p>
     * This header will be commented out and applied directly at the top of
     * the generated output of the {@link LFileConfiguration}. It is not
     * required to include a newline at the end of the header as it will
     * automatically be applied, but you may include one if you wish for extra
     * spacing.
     * <p>
     * Null is a valid value which will indicate that no header is to be
     * applied. The default value is null.
     *
     * @return Header
     */
    @Nullable
    public final String header() {
        return this.header;
    }

    /**
     * Sets the header that will be applied to the top of the saved output.
     * <p>
     * This header will be commented out and applied directly at the top of
     * the generated output of the {@link LFileConfiguration}. It is not
     * required to include a newline at the end of the header as it will
     * automatically be applied, but you may include one if you wish for extra
     * spacing.
     * <p>
     * Null is a valid value which will indicate that no header is to be
     * applied.
     *
     * @param header New header
     * @return This object, for chaining
     */
    @NotNull
    public LFileConfigurationOptions header(@Nullable final String header) {
        this.header = header;
        return this;
    }

    /**
     * Gets whether or not the header should be copied from a default source.
     * <p>
     * If this is true, if a default {@link LFileConfiguration} is passed to
     * {@link
     * LFileConfiguration#setDefaults(LConfiguration)}
     * then upon saving it will use the header from that config, instead of
     * the one provided here.
     * <p>
     * If no default is set on the configuration, or the default is not of
     * type FileConfiguration, or that config has no header ({@link #header()}
     * returns null) then the header specified in this configuration will be
     * used.
     * <p>
     * Defaults to true.
     *
     * @return Whether or not to copy the header
     */
    public final boolean copyHeader() {
        return this.copyHeader;
    }

    /**
     * Sets whether or not the header should be copied from a default source.
     * <p>
     * If this is true, if a default {@link LFileConfiguration} is passed to
     * {@link
     * LFileConfiguration#setDefaults(LConfiguration)}
     * then upon saving it will use the header from that config, instead of
     * the one provided here.
     * <p>
     * If no default is set on the configuration, or the default is not of
     * type FileConfiguration, or that config has no header ({@link #header()}
     * returns null) then the header specified in this configuration will be
     * used.
     * <p>
     * Defaults to true.
     *
     * @param copyHeader Whether or not to copy the header
     * @return This object, for chaining
     */
    @NotNull
    public LFileConfigurationOptions copyHeader(final boolean copyHeader) {
        this.copyHeader = copyHeader;
        return this;
    }
}

package id.luckynetwork.lyrams.lyralibs.core.config;

import org.bspfsystems.yamlconfiguration.configuration.InvalidConfigurationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.charset.StandardCharsets;

public abstract class LFileConfiguration extends LMemoryConfiguration {

    /**
     * Creates an empty {@link LFileConfiguration} with no default values.
     */
    public LFileConfiguration() {
        super();
    }

    /**
     * Creates an empty {@link LFileConfiguration} using the specified {@link
     * LConfiguration} as a source for all default values.
     *
     * @param defs Default value provider
     */
    public LFileConfiguration(@Nullable final LConfiguration defs) {
        super(defs);
    }

    /**
     * Saves this {@link LFileConfiguration} to the specified location.
     * <p>
     * If the file does not exist, it will be created. If already exists, it
     * will be overwritten. If it cannot be overwritten or created, an
     * exception will be thrown.
     * <p>
     * This method will save using the system default encoding, or possibly
     * using UTF8.
     *
     * @param file File to save to.
     * @throws IOException              Thrown when the given file cannot be written to for
     *                                  any reason.
     * @throws IllegalArgumentException Thrown when file is null.
     */
    public void save(@NotNull final File file) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }

        final String data = this.saveToString();
        final Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8.name());

        try {
            writer.write(data);
        } finally {
            writer.close();
        }
    }

    /**
     * Saves this {@link LFileConfiguration} to the specified location.
     * <p>
     * If the file does not exist, it will be created. If already exists, it
     * will be overwritten. If it cannot be overwritten or created, an
     * exception will be thrown.
     * <p>
     * This method will save using the system default encoding, or possibly
     * using UTF8.
     *
     * @param path File to save to.
     * @throws IOException              Thrown when the given file cannot be written to for
     *                                  any reason.
     * @throws IllegalArgumentException Thrown when file is null.
     */
    public void save(@NotNull final String path) throws IOException {
        this.save(new File(path));
    }

    /**
     * Saves this {@link LFileConfiguration} to a string, and returns it.
     *
     * @return String containing this LConfiguration.
     */
    @NotNull
    public abstract String saveToString();

    /**
     * Loads this {@link LFileConfiguration} from the specified reader.
     * <p>
     * All the values contained within this LConfiguration will be removed,
     * leaving only settings and defaults, and the new values will be loaded
     * from the given stream.
     *
     * @param reader the reader to load from
     * @throws IOException                   thrown when underlying reader throws an IOException
     * @throws InvalidConfigurationException thrown when the reader does not
     *                                       represent a valid LConfiguration
     * @throws IllegalArgumentException      thrown when reader is null
     */
    public void load(@NotNull Reader reader) throws IOException, InvalidConfigurationException {

        final BufferedReader bufferedReader = reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
        final StringBuilder builder = new StringBuilder();

        try {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
                builder.append('\n');
            }
        } finally {
            bufferedReader.close();
        }

        this.loadFromString(builder.toString());
    }

    /**
     * Loads this {@link LFileConfiguration} from the specified location.
     * <p>
     * All the values contained within this LConfiguration will be removed,
     * leaving only settings and defaults, and the new values will be loaded
     * from the given file.
     * <p>
     * If the file cannot be loaded for any reason, an exception will be
     * thrown.
     *
     * @param file File to load from.
     * @throws FileNotFoundException         Thrown when the given file cannot be
     *                                       opened.
     * @throws IOException                   Thrown when the given file cannot be read.
     * @throws InvalidConfigurationException Thrown when the given file is not
     *                                       a valid LConfiguration.
     * @throws IllegalArgumentException      Thrown when file is null.
     */
    public void load(@NotNull File file) throws FileNotFoundException, IOException, InvalidConfigurationException {
        this.load(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8.name()));
    }

    /**
     * Loads this {@link LFileConfiguration} from the specified location.
     * <p>
     * All the values contained within this LConfiguration will be removed,
     * leaving only settings and defaults, and the new values will be loaded
     * from the given file.
     * <p>
     * If the file cannot be loaded for any reason, an exception will be
     * thrown.
     *
     * @param fileName File to load from.
     * @throws FileNotFoundException         Thrown when the given file cannot be
     *                                       opened.
     * @throws IOException                   Thrown when the given file cannot be read.
     * @throws InvalidConfigurationException Thrown when the given file is not
     *                                       a valid LConfiguration.
     * @throws IllegalArgumentException      Thrown when file is null.
     */
    public void load(@NotNull String fileName) throws FileNotFoundException, IOException, InvalidConfigurationException {
        this.load(new File(fileName));
    }

    /**
     * Loads this {@link LFileConfiguration} from the specified string, as
     * opposed to from file.
     * <p>
     * All the values contained within this LConfiguration will be removed,
     * leaving only settings and defaults, and the new values will be loaded
     * from the given string.
     * <p>
     * If the string is invalid in any way, an exception will be thrown.
     *
     * @param data Contents of a LConfiguration to load.
     * @throws InvalidConfigurationException Thrown if the specified string is
     *                                       invalid.
     * @throws IllegalArgumentException      Thrown if contents is null.
     */
    public abstract void loadFromString(@NotNull String data) throws InvalidConfigurationException;

    /**
     * Compiles the header for this {@link LFileConfiguration} and returns the
     * result.
     * <p>
     * This will use the header from {@link #options()} -&gt; {@link
     * LFileConfigurationOptions#header()}, respecting the rules of {@link
     * LFileConfigurationOptions#copyHeader()} if set.
     *
     * @return Compiled header
     */
    @NotNull
    protected abstract String buildHeader();

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public LFileConfigurationOptions options() {
        if (this.options == null) {
            this.options = new LFileConfigurationOptions(this);
        }
        return (LFileConfigurationOptions) this.options;
    }
}

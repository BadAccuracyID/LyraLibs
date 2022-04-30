package id.luckynetwork.lyrams.lyralibs.core.config;

import org.bspfsystems.yamlconfiguration.configuration.InvalidConfigurationException;
import org.bspfsystems.yamlconfiguration.file.YamlConstructor;
import org.bspfsystems.yamlconfiguration.file.YamlRepresenter;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.representer.Representer;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LYamlConfiguration extends LFileConfiguration {

    protected static final String COMMENT_PREFIX = "# ";
    protected static final String BLANK_CONFIG = "{}\n";

    private final DumperOptions yamlOptions;
    private final Representer yamlRepresenter;
    private final Yaml yaml;

    public LYamlConfiguration() {
        this.yamlOptions = new DumperOptions();
        this.yamlRepresenter = new YamlRepresenter();
        this.yaml = new Yaml(new YamlConstructor(), this.yamlRepresenter, this.yamlOptions);
    }

    @Override
    public @NotNull String saveToString() {
        this.yamlOptions.setIndent(options().indent());
        this.yamlOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        this.yamlRepresenter.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        final String header = buildHeader();
        String dump = this.yaml.dump(this.getValues(false));

        if (dump.equals(LYamlConfiguration.BLANK_CONFIG)) {
            dump = "";
        }

        return header + dump;
    }

    @Override
    public void loadFromString(@NotNull String data) throws InvalidConfigurationException {
        final Map<?, ?> map;
        try {
            map = this.yaml.load(data);
        } catch (YAMLException e) {
            throw new InvalidConfigurationException(e);
        } catch (ClassCastException e) {
            throw new InvalidConfigurationException("Top level is not a Map.", e);
        }

        final String header = this.parseHeader(data);
        if (header.length() > 0) {
            this.options().header(header);
        }

        this.map.clear();

        if (map != null) {
            this.convertMapsToSections(map, this);
        }
    }

    /**
     * Converts the given {@link Map} data into a {@link LConfigurationSection},
     * loading it into the given {@link LConfigurationSection}.
     *
     * @param map     The data to convert.
     * @param section The {@link LConfigurationSection} that will hold the
     *                converted data.
     */
    protected void convertMapsToSections(@NotNull final Map<?, ?> map, @NotNull final LConfigurationSection section) {
        for (final Map.Entry<?, ?> entry : map.entrySet()) {
            final String key = entry.getKey().toString();
            final Object value = entry.getValue();

            if (value instanceof Map) {
                convertMapsToSections((Map<?, ?>) value, section.createSection(key));
            } else {
                section.set(key, value);
            }
        }
    }

    /**
     * Parses the YAML header from the given data.
     *
     * @param data The data to parse the header from.
     * @return The parsed header.
     */
    @NotNull
    protected String parseHeader(@NotNull final String data) {
        final String[] lines = data.split("\r?\n", -1);
        final StringBuilder builder = new StringBuilder();

        boolean readingHeader = true;
        boolean foundHeader = false;

        for (int index = 0; index < lines.length && readingHeader; index++) {
            final String line = lines[index];
            if (line.startsWith(LYamlConfiguration.COMMENT_PREFIX)) {

                if (index > 0) {
                    builder.append("\n");
                }
                if (line.length() > LYamlConfiguration.COMMENT_PREFIX.length()) {
                    builder.append(line.substring(LYamlConfiguration.COMMENT_PREFIX.length()));
                }
                foundHeader = true;
            } else if (foundHeader && line.length() == 0) {
                builder.append("\n");
            } else if (foundHeader) {
                readingHeader = false;
            }
        }

        return builder.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    protected String buildHeader() {
        final String header = this.options().header();
        if (this.options().copyHeader()) {
            final LConfiguration def = this.getDefaults();
            if (def instanceof LFileConfiguration) {

                final LFileConfiguration fileDef = (LFileConfiguration) def;
                final String defHeader = fileDef.buildHeader();

                if (defHeader.length() > 0) {
                    return defHeader;
                }
            }
        }

        if (header == null) {
            return "";
        }

        final StringBuilder builder = new StringBuilder();
        final String[] lines = header.split("\r?\n", -1);

        boolean startedHeader = false;
        for (int index = lines.length - 1; index >= 0; index--) {

            builder.insert(0, "\n");
            if (startedHeader || lines[index].length() != 0) {
                builder.insert(0, lines[index]);
                builder.insert(0, LYamlConfiguration.COMMENT_PREFIX);
                startedHeader = true;
            }
        }

        return builder.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public LYamlConfigurationOptions options() {
        if (this.options == null) {
            this.options = new LYamlConfigurationOptions(this);
        }
        return (LYamlConfigurationOptions) this.options;
    }

    /**
     * Creates a new {@link LYamlConfiguration}, loading from the given file.
     * <p>
     * Any errors loading the Configuration will be logged and then ignored.
     * If the specified input is not a valid config, a blank config will be
     * returned.
     * <p>
     * The encoding used may follow the system dependent default.
     *
     * @param file Input file
     * @return Resulting configuration
     * @throws IllegalArgumentException Thrown if file is null
     */
    @NotNull
    public static LYamlConfiguration loadConfiguration(@NotNull final File file) {
        final LYamlConfiguration config = new LYamlConfiguration();
        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            Logger.getLogger(LYamlConfiguration.class.getName()).log(Level.SEVERE, "Cannot load config from file: " + file, e);
        }

        return config;
    }

    /**
     * Creates a new {@link LYamlConfiguration}, loading from the given reader.
     * <p>
     * Any errors loading the Configuration will be logged and then ignored.
     * If the specified input is not a valid config, a blank config will be
     * returned.
     *
     * @param reader input
     * @return resulting configuration
     * @throws IllegalArgumentException Thrown if stream is null
     */
    @NotNull
    public static LYamlConfiguration loadConfiguration(@NotNull final Reader reader) {

        final LYamlConfiguration config = new LYamlConfiguration();
        try {
            config.load(reader);
        } catch (IOException | InvalidConfigurationException e) {
            Logger.getLogger(LYamlConfiguration.class.getName()).log(Level.SEVERE, "Cannot load config from reader.", e);
        }

        return config;
    }
}

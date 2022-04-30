package id.luckynetwork.lyrams.lyralibs.core.config;

import org.bspfsystems.yamlconfiguration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class LMemorySection implements LConfigurationSection {

    protected final Map<String, Object> map;

    private final LConfiguration root;
    private final LConfigurationSection parent;
    private final String path;
    private final String fullPath;

    /**
     * Creates an empty LMemorySection for use as a root {@link LConfiguration}
     * section.
     * <p>
     * Note that calling this without being yourself a {@link LConfiguration}
     * will throw an exception!
     *
     * @throws IllegalStateException Thrown if this is not a {@link
     *                               LConfiguration} root.
     */
    protected LMemorySection() {
        if (!(this instanceof LConfiguration)) {
            throw new IllegalStateException("Cannot construct a root LMemorySection when this is not a Configuration.");
        }

        this.map = new LinkedHashMap<>();

        this.root = (LConfiguration) this;
        this.parent = null;
        this.path = "";
        this.fullPath = "";
    }

    /**
     * Creates an empty LMemorySection with the specified parent and path.
     *
     * @param parent Parent section that contains this own section.
     * @param path   Path that you may access this section from via the root
     *               {@link LConfiguration}.
     * @throws IllegalArgumentException Thrown is parent or path is null, or
     *                                  if parent contains no root Configuration.
     */
    protected LMemorySection(@NotNull final LConfigurationSection parent, @NotNull final String path) {

        if (parent.getRoot() == null) {
            throw new IllegalArgumentException("Path cannot be orphaned.");
        }

        this.map = new LinkedHashMap<String, Object>();

        this.root = parent.getRoot();
        this.parent = parent;
        this.path = path;
        this.fullPath = createPath(parent, path);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public Set<String> getKeys(final boolean deep) {
        final Set<String> result = new LinkedHashSet<String>();
        final LConfiguration root = this.getRoot();

        if (root != null && root.options().copyDefaults()) {

            final LConfigurationSection defs = this.getDefaultSection();
            if (defs != null) {
                result.addAll(defs.getKeys(deep));
            }
        }

        this.mapChildrenKeys(result, this, deep);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public Map<String, Object> getValues(final boolean deep) {

        final Map<String, Object> result = new LinkedHashMap<String, Object>();
        final LConfiguration root = this.getRoot();

        if (root != null && root.options().copyDefaults()) {

            final LConfigurationSection defs = this.getDefaultSection();
            if (defs != null) {
                result.putAll(defs.getValues(deep));
            }
        }

        this.mapChildrenValues(result, this, deep);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public String getCurrentPath() {
        return this.fullPath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public String getName() {
        return this.path;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public LConfiguration getRoot() {
        return this.root;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public LConfigurationSection getParent() {
        return this.parent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public LConfigurationSection getDefaultSection() {

        final LConfiguration root = this.getRoot();
        final LConfiguration defs = root == null ? null : root.getDefaults();

        if (defs != null) {
            if (defs.isConfigurationSection(this.getCurrentPath())) {
                return defs.getConfigurationSection(this.getCurrentPath());
            }
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addDefault(@NotNull final String path, @Nullable final Object value) {

        final LConfiguration root = this.getRoot();
        if (root == null) {
            throw new IllegalStateException("Cannot add defaults without a root.");
        }
        if (root == this) {
            throw new UnsupportedOperationException("Unsupported addDefault(String, Object) implementation.");
        }

        root.addDefault(LMemorySection.createPath(this, path), value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public LConfigurationSection createSection(@NotNull final String path) {
        if (path.isEmpty()) {
            throw new IllegalArgumentException("Cannot set to an empty path.");
        }

        final LConfiguration root = this.getRoot();
        if (root == null) {
            throw new IllegalStateException("Cannot create section without a root.");
        }

        final char separator = root.options().pathSeparator();
        int lead = -1;
        int tail;

        LConfigurationSection section = this;
        while ((lead = path.indexOf(separator, tail = lead + 1)) != -1) {

            final String node = path.substring(tail, lead);
            final LConfigurationSection subSection = section.getConfigurationSection(node);
            if (subSection == null) {
                section = section.createSection(node);
            } else {
                section = subSection;
            }
        }

        final String key = path.substring(tail);
        if (section == this) {
            final LConfigurationSection result = new LMemorySection(this, key);
            this.map.put(key, result);
            return result;
        }
        return section.createSection(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public LConfigurationSection createSection(@NotNull final String path, @NotNull final Map<?, ?> map) {

        final LConfigurationSection section = createSection(path);
        for (final Map.Entry<?, ?> entry : map.entrySet()) {

            if (entry.getValue() instanceof Map) {
                section.createSection(entry.getKey().toString(), (Map<?, ?>) entry.getValue());
            } else {
                section.set(entry.getKey().toString(), entry.getValue());
            }
        }

        return section;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSet(@NotNull final String path) {

        final LConfiguration root = this.getRoot();
        if (root == null) {
            return false;
        }
        if (root.options().copyDefaults()) {
            return this.contains(path);
        }
        return this.get(path, null) != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(@NotNull final String path, @Nullable final Object value) {

        if (path.isEmpty()) {
            throw new IllegalArgumentException("Cannot set to an empty path.");
        }

        final LConfiguration root = this.getRoot();
        if (root == null) {
            throw new IllegalStateException("Cannot use section without a root.");
        }

        final char separator = root.options().pathSeparator();
        int lead = -1;  // Leading (higher) index.
        int trail;      // Trailing (lower) index.

        LConfigurationSection section = this;
        while ((lead = path.indexOf(separator, trail = lead + 1)) != -1) {

            final String node = path.substring(trail, lead);
            final LConfigurationSection subSection = section.getConfigurationSection(node);

            if (subSection == null) {
                if (value == null) {
                    return;
                }
                section = section.createSection(node);
            } else {
                section = subSection;
            }
        }

        final String key = path.substring(trail);
        if (section == this) {
            if (value == null) {
                this.map.remove(key);
            } else {
                this.map.put(key, value);
            }
        } else {
            section.set(key, value);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(@NotNull final String path) {
        return this.contains(path, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(@NotNull final String path, final boolean ignoreDefault) {
        return ((ignoreDefault) ? this.get(path, null) : this.get(path)) != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public Object get(@NotNull final String path) {
        return this.get(path, this.getDefault(path));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Contract("_, !null -> !null")
    @Nullable
    public Object get(@NotNull final String path, @Nullable final Object def) {

        if (path.length() == 0) {
            return this;
        }

        final LConfiguration root = this.getRoot();
        if (root == null) {
            throw new IllegalStateException("Cannot access section without a root.");
        }

        final char separator = root.options().pathSeparator();
        int lead = -1;
        int tail;

        LConfigurationSection section = this;
        while ((lead = path.indexOf(separator, tail = lead + 1)) != -1) {
            section = section.getConfigurationSection(path.substring(tail, lead));
            if (section == null) {
                return def;
            }
        }

        final String key = path.substring(tail);
        if (section == this) {
            final Object result = this.map.get(key);
            return result == null ? def : result;
        }
        return section.get(key, def);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isBoolean(@NotNull final String path) {
        return this.get(path) instanceof Boolean;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getBoolean(@NotNull final String path) {
        final Object def = this.getDefault(path);
        return this.getBoolean(path, def instanceof Boolean && (Boolean) def);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getBoolean(@NotNull final String path, final boolean def) {
        final Object val = this.get(path, def);
        return val instanceof Boolean ? (Boolean) val : def;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isByte(@NotNull final String path) {
        return this.get(path) instanceof Byte;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte getByte(@NotNull final String path) {
        final Object def = this.getDefault(path);
        return this.getByte(path, def instanceof Number ? ((Number) def).byteValue() : (byte) 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte getByte(@NotNull final String path, final byte def) {
        final Object val = this.get(path, def);
        return val instanceof Number ? ((Number) val).byteValue() : def;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isShort(@NotNull final String path) {
        return this.get(path) instanceof Short;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public short getShort(@NotNull final String path) {
        final Object def = this.getDefault(path);
        return this.getShort(path, def instanceof Number ? ((Number) def).shortValue() : (short) 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public short getShort(@NotNull final String path, final short def) {
        final Object val = this.get(path, def);
        return val instanceof Number ? ((Number) val).shortValue() : def;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInt(@NotNull final String path) {
        return this.get(path) instanceof Integer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getInt(@NotNull final String path) {
        final Object def = this.getDefault(path);
        return this.getInt(path, def instanceof Number ? ((Number) def).intValue() : 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getInt(@NotNull final String path, final int def) {
        final Object val = this.get(path, def);
        return val instanceof Number ? ((Number) val).intValue() : def;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLong(@NotNull final String path) {
        return this.get(path) instanceof Long;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getLong(@NotNull final String path) {
        final Object def = this.getDefault(path);
        return this.getLong(path, def instanceof Number ? (Long) def : 0L);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getLong(@NotNull final String path, final long def) {
        final Object val = this.get(path, def);
        return val instanceof Number ? (Long) val : def;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFloat(@NotNull final String path) {
        return this.get(path) instanceof Float;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getFloat(@NotNull final String path) {
        final Object def = this.getDefault(path);
        return this.getFloat(path, def instanceof Number ? ((Number) def).floatValue() : 0.0F);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getFloat(@NotNull final String path, final float def) {
        final Object val = this.get(path, def);
        return val instanceof Number ? ((Number) val).floatValue() : def;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDouble(@NotNull final String path) {
        return this.get(path) instanceof Double;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getDouble(@NotNull final String path) {
        final Object def = this.getDefault(path);
        return this.getDouble(path, def instanceof Number ? ((Number) def).doubleValue() : 0.0D);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getDouble(@NotNull final String path, final double def) {
        final Object val = this.get(path, def);
        return val instanceof Number ? ((Number) val).doubleValue() : def;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isChar(@NotNull final String path) {
        return this.get(path) instanceof Boolean;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public char getChar(@NotNull final String path) {
        final Object def = this.getDefault(path);
        return this.getChar(path, def instanceof Character ? (Character) def : '\u0000');
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public char getChar(@NotNull final String path, final char def) {
        final Object val = this.get(path, def);
        return val instanceof Character ? (Character) val : def;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isString(@NotNull final String path) {
        return this.get(path) instanceof String;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public String getString(@NotNull final String path) {
        final Object def = this.getDefault(path);
        return this.getString(path, def != null ? def.toString() : null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Contract("_, !null -> !null")
    @Nullable
    public String getString(@NotNull final String path, @Nullable final String def) {
        final Object val = this.get(path, def);
        return val != null ? val.toString() : def;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isList(@NotNull final String path) {
        return this.get(path) instanceof List;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public List<?> getList(@NotNull final String path) {
        final Object def = this.getDefault(path);
        return this.getList(path, def instanceof List ? (List<?>) def : null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Contract("_, !null -> !null")
    @Nullable
    public List<?> getList(@NotNull final String path, @Nullable final List<?> def) {
        final Object val = this.get(path, def);
        return val instanceof List ? (List<?>) val : def;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isConfigurationSection(@NotNull final String path) {
        return this.get(path) instanceof LConfigurationSection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public LConfigurationSection getConfigurationSection(@NotNull final String path) {

        Object val = this.get(path, null);
        if (val != null) {
            return val instanceof LConfigurationSection ? (LConfigurationSection) val : null;
        }

        val = this.get(path, this.getDefault(path));
        return val instanceof LConfigurationSection ? this.createSection(path) : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public <T extends Object> T getObject(@NotNull final String path, @NotNull final Class<T> clazz) {
        final Object def = this.getDefault(path);
        return this.getObject(path, clazz, clazz.isInstance(def) ? clazz.cast(def) : null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Contract("_, _, !null -> !null")
    @Nullable
    public <T extends Object> T getObject(@NotNull final String path, @NotNull final Class<T> clazz, @Nullable final T def) {
        final Object val = this.get(path, def);
        return clazz.isInstance(val) ? clazz.cast(val) : def;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public <T extends ConfigurationSerializable> T getSerializable(@NotNull final String path, @NotNull final Class<T> clazz) {
        return this.getObject(path, clazz);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Contract("_, _, !null -> !null")
    @Nullable
    public <T extends ConfigurationSerializable> T getSerializable(@NotNull final String path, @NotNull final Class<T> clazz, @Nullable final T def) {
        return this.getObject(path, clazz, def);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public List<Boolean> getBooleanList(@NotNull final String path) {

        final List<?> list = this.getList(path);
        if (list == null) {
            return new ArrayList<Boolean>(0);
        }

        final List<Boolean> result = new ArrayList<Boolean>();
        for (final Object object : list) {

            if (object instanceof Boolean) {
                result.add((Boolean) object);
            } else if (object instanceof String) {
                result.add(Boolean.valueOf((String) object));
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public List<Byte> getByteList(@NotNull final String path) {

        final List<?> list = this.getList(path);
        if (list == null) {
            return new ArrayList<Byte>(0);
        }

        final List<Byte> result = new ArrayList<Byte>();
        for (final Object object : list) {

            if (object instanceof Byte) {
                result.add((Byte) object);
            } else if (object instanceof String) {
                try {
                    result.add(Byte.valueOf((String) object));
                } catch (NumberFormatException e) {
                    // Ignore
                }
            } else if (object instanceof Character) {
                try {
                    result.add((byte) ((Character) object).charValue());
                } catch (ClassCastException e) {
                    // Ignore
                }
            } else if (object instanceof Number) {
                result.add(((Number) object).byteValue());
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public List<Short> getShortList(@NotNull final String path) {

        final List<?> list = this.getList(path);
        if (list == null) {
            return new ArrayList<Short>(0);
        }

        final List<Short> result = new ArrayList<Short>();
        for (final Object object : list) {

            if (object instanceof Short) {
                result.add((Short) object);
            } else if (object instanceof String) {
                try {
                    result.add(Short.valueOf((String) object));
                } catch (NumberFormatException e) {
                    // Ignore
                }
            } else if (object instanceof Character) {
                try {
                    result.add((short) ((Character) object).charValue());
                } catch (ClassCastException e) {
                    // Ignore
                }
            } else if (object instanceof Number) {
                result.add(((Number) object).shortValue());
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public List<Integer> getIntList(@NotNull final String path) {

        final List<?> list = this.getList(path);
        if (list == null) {
            return new ArrayList<Integer>(0);
        }

        final List<Integer> result = new ArrayList<Integer>();
        for (final Object object : list) {

            if (object instanceof Integer) {
                result.add((Integer) object);
            } else if (object instanceof String) {
                try {
                    result.add(Integer.valueOf((String) object));
                } catch (NumberFormatException e) {
                    // Ignore
                }
            } else if (object instanceof Character) {
                try {
                    result.add((int) (Character) object);
                } catch (ClassCastException e) {
                    // Ignore
                }
            } else if (object instanceof Number) {
                result.add(((Number) object).intValue());
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public List<Long> getLongList(@NotNull final String path) {

        final List<?> list = this.getList(path);
        if (list == null) {
            return new ArrayList<Long>(0);
        }

        final List<Long> result = new ArrayList<Long>();
        for (final Object object : list) {

            if (object instanceof Long) {
                result.add((Long) object);
            } else if (object instanceof String) {
                try {
                    result.add(Long.valueOf((String) object));
                } catch (NumberFormatException e) {
                    // Ignore
                }
            } else if (object instanceof Character) {
                try {
                    result.add((long) (Character) object);
                } catch (ClassCastException e) {
                    // Ignore
                }
            } else if (object instanceof Number) {
                result.add(((Number) object).longValue());
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public List<Float> getFloatList(@NotNull final String path) {

        final List<?> list = this.getList(path);
        if (list == null) {
            return new ArrayList<Float>(0);
        }

        final List<Float> result = new ArrayList<Float>();
        for (final Object object : list) {

            if (object instanceof Float) {
                result.add((Float) object);
            } else if (object instanceof String) {
                try {
                    result.add(Float.valueOf((String) object));
                } catch (NumberFormatException e) {
                    // Ignore
                }
            } else if (object instanceof Character) {
                try {
                    result.add((float) (Character) object);
                } catch (ClassCastException e) {
                    // Ignore
                }
            } else if (object instanceof Number) {
                result.add(((Number) object).floatValue());
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public List<Double> getDoubleList(@NotNull final String path) {

        final List<?> list = this.getList(path);
        if (list == null) {
            return new ArrayList<Double>(0);
        }

        final List<Double> result = new ArrayList<Double>();
        for (final Object object : list) {

            if (object instanceof Double) {
                result.add((Double) object);
            } else if (object instanceof String) {
                try {
                    result.add(Double.valueOf((String) object));
                } catch (NumberFormatException e) {
                    // Ignore
                }
            } else if (object instanceof Character) {
                try {
                    result.add((double) (Character) object);
                } catch (ClassCastException e) {
                    // Ignore
                }
            } else if (object instanceof Number) {
                result.add(((Number) object).doubleValue());
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public List<Character> getCharList(@NotNull final String path) {

        final List<?> list = this.getList(path);
        if (list == null) {
            return new ArrayList<Character>(0);
        }

        final List<Character> result = new ArrayList<Character>();
        for (final Object object : list) {

            if (object instanceof Character) {
                result.add((Character) object);
            } else if (object instanceof String) {
                if (((String) object).length() == 1) {
                    result.add(((String) object).charAt(0));
                }
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public List<String> getStringList(@NotNull final String path) {
        final List<?> list = this.getList(path);
        if (list == null) {
            return new ArrayList<>(0);
        }

        final List<String> result = new ArrayList<>();
        for (final Object object : list) {
            if (object != null) {
                result.add(object.toString());
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public List<String> getStringList(@NotNull String path, @Nullable List<String> def) {
        final List<?> list = this.getList(path);
        if (list == null) {
            return def;
        }

        final List<String> result = new ArrayList<>();
        for (final Object object : list) {
            if (object != null) {
                result.add(object.toString());
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public List<Map<?, ?>> getMapList(@NotNull final String path) {

        final List<?> list = this.getList(path);
        if (list == null) {
            return new ArrayList<Map<?, ?>>(0);
        }

        final List<Map<?, ?>> result = new ArrayList<Map<?, ?>>();
        for (final Object object : list) {
            if (object instanceof Map) {
                result.add((Map<?, ?>) object);
            }
        }

        return result;
    }

    /**
     * Gets the default {@link Object} for the given path.
     *
     * @param path The path to retrieve the default {@link Object} from.
     * @return The default {@link Object}, or <code>null</code> if the default
     * is <code>null</code>.
     */
    @Nullable
    protected Object getDefault(@NotNull final String path) {

        final LConfiguration root = this.getRoot();
        final LConfiguration defs = root == null ? null : root.getDefaults();
        return defs == null ? null : defs.get(LMemorySection.createPath(this, path));
    }

    /**
     * Maps the keys of the given child {@link LConfigurationSection}.
     *
     * @param output  The mapped keys.
     * @param section The child {@link LConfigurationSection}.
     * @param deep    If <code>true</code>, any children of the given
     *                {@link LConfigurationSection} will have their keys mapped. If
     *                <code>false</code>, only the given
     *                {@link LConfigurationSection}'s keys will be mapped.
     */
    protected void mapChildrenKeys(@NotNull final Set<String> output, @NotNull final LConfigurationSection section, final boolean deep) {
        if (section instanceof LMemorySection) {

            final LMemorySection memorySection = (LMemorySection) section;
            for (final Map.Entry<String, Object> entry : memorySection.map.entrySet()) {
                output.add(LMemorySection.createPath(section, entry.getKey(), this));

                if (deep && entry.getValue() instanceof LConfigurationSection) {
                    this.mapChildrenKeys(output, (LConfigurationSection) entry.getValue(), deep);
                }
            }
        } else {

            final Set<String> keys = section.getKeys(deep);
            for (final String key : keys) {
                output.add(LMemorySection.createPath(section, key, this));
            }
        }
    }

    /**
     * Maps the key-value pairs of the given child {@link LConfigurationSection}.
     *
     * @param output  The mapped key-value pairs.
     * @param section The child {@link LConfigurationSection}.
     * @param deep    If <code>true</code>, any children of the given
     *                {@link LConfigurationSection} will have their key-value pairs
     *                mapped. If <code>false</code>, only the given
     *                {@link LConfigurationSection}'s key-value pairs will be
     *                mapped.
     */
    protected void mapChildrenValues(@NotNull final Map<String, Object> output, @NotNull final LConfigurationSection section, final boolean deep) {
        if (section instanceof LMemorySection) {

            final LMemorySection memorySection = (LMemorySection) section;
            for (final Map.Entry<String, Object> entry : memorySection.map.entrySet()) {

                final String path = LMemorySection.createPath(section, entry.getKey(), this);
                output.remove(path);
                output.put(path, entry.getValue());

                if (deep && entry.getValue() instanceof LConfigurationSection) {
                    this.mapChildrenValues(output, (LConfigurationSection) entry.getValue(), deep);
                }
            }
        } else {

            final Map<String, Object> values = section.getValues(deep);
            for (final Map.Entry<String, Object> entry : values.entrySet()) {
                output.put(LMemorySection.createPath(section, entry.getKey(), this), entry.getValue());
            }
        }
    }

    /**
     * Creates a {@link String} representation of this {@link LMemorySection}.
     *
     * @return A {@link String} representation of this {@link LMemorySection}.
     */
    @Override
    public String toString() {

        final LConfiguration root = this.getRoot();
        final StringBuilder builder = new StringBuilder();

        builder.append(getClass().getSimpleName());
        builder.append("[path='");
        builder.append(this.getCurrentPath());
        builder.append("', root='");
        builder.append(root == null ? null : root.getClass().getSimpleName());
        builder.append("']");

        return builder.toString();
    }

    /**
     * Creates a full path to the given {@link LConfigurationSection} from its
     * root {@link LConfiguration}.
     * <p>
     * You may use this method for any given {@link LConfigurationSection}, not
     * only {@link LMemorySection}.
     *
     * @param section Section to create a path for.
     * @param key     Name of the specified section.
     * @return Full path of the section from its root.
     */
    @NotNull
    public static String createPath(@NotNull final LConfigurationSection section, @Nullable final String key) {
        return LMemorySection.createPath(section, key, section.getRoot());
    }

    /**
     * Creates a relative path to the given {@link LConfigurationSection} from
     * the given relative section.
     * <p>
     * You may use this method for any given {@link LConfigurationSection}, not
     * only {@link LMemorySection}.
     *
     * @param section  Section to create a path for.
     * @param key      Name of the specified section.
     * @param relative Section to create the path relative to.
     * @return Full path of the section from its root.
     */
    @NotNull
    public static String createPath(@NotNull final LConfigurationSection section, @Nullable final String key, @Nullable final LConfigurationSection relative) {

        final LConfiguration root = section.getRoot();
        if (root == null) {
            throw new IllegalStateException("Cannot create path without a root.");
        }

        final char separator = root.options().pathSeparator();
        final StringBuilder builder = new StringBuilder();

        for (LConfigurationSection parent = section; parent != null && parent != relative; parent = parent.getParent()) {

            if (builder.length() > 0) {
                builder.insert(0, separator);
            }
            builder.insert(0, parent.getName());
        }

        if (key != null && key.length() > 0) {
            if (builder.length() > 0) {
                builder.append(separator);
            }
            builder.append(key);
        }

        return builder.toString();
    }

}

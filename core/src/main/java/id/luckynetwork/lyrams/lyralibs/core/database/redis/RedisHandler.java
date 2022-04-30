package id.luckynetwork.lyrams.lyralibs.core.database.redis;

import com.google.gson.*;
import id.luckynetwork.lyrams.lyralibs.core.database.redis.annotations.RedisListener;
import id.luckynetwork.lyrams.lyralibs.core.database.redis.annotations.RedisObject;
import id.luckynetwork.lyrams.lyralibs.core.interfaces.Returnable;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RedisHandler {


    @Getter
    private JedisPool jedisPool, subscriberPool, publisherPool;
    @Getter
    private JedisPubSub pubSub;
    @Setter
    private List<String> serverIdentifier;

    @Getter
    private final AtomicBoolean isReconnecting = new AtomicBoolean(false);

    private final Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().enableComplexMapKeySerialization().create();
    private final Map<String, RedisData> dataMap = new HashMap<>();
    private final Map<String, Class<?>> objectMap = new HashMap<>();
    private final List<ExecutorService> executorServices = new ArrayList<>();

    private final Returnable<ExecutorService> executorServiceReturnable;
    private ExecutorService redisExecutor;
    private final RedisConfiguration redisConfiguration;
    private final Logger logger;

    public RedisHandler(Returnable<ExecutorService> executorServiceReturnable, Logger logger, RedisConfiguration redisConfiguration) throws Exception {
        this.executorServiceReturnable = executorServiceReturnable;
        this.redisExecutor = this.generateExecutorService();
        this.logger = logger;
        this.redisConfiguration = redisConfiguration;
    }

    public boolean connect() {
        try {
            this.jedisPool = this.subscriberPool = this.publisherPool =
                    new JedisPool(
                            new JedisPoolConfig(),
                            this.redisConfiguration.getHost(),
                            this.redisConfiguration.getPort(),
                            this.redisConfiguration.getTimeout(),
                            this.redisConfiguration.getPassword(),
                            0,
                            this.redisConfiguration.getClientName()
                    );

            try (Jedis jedis = this.jedisPool.getResource()) {
                jedis.ping();
            } catch (Exception e) {
                this.logger.log(Level.WARNING, "Failed to connect to redis server", e);
                return false;
            }

            try (Jedis jedis = this.subscriberPool.getResource()) {
                jedis.ping();
            } catch (Exception e) {
                this.logger.log(Level.WARNING, "Failed to connect to redis server", e);
                return false;
            }

            try (Jedis jedis = this.publisherPool.getResource()) {
                jedis.ping();
            } catch (Exception e) {
                this.logger.log(Level.WARNING, "Failed to connect to redis server", e);
                return false;
            }

            this.setupPubSub();
        } catch (Exception e) {
            this.logger.log(Level.WARNING, "Failed to connect to redis server", e);
            return false;
        }

        this.logger.log(Level.INFO, "Connected to redis server");
        return true;
    }

    public boolean isConnected() {
        boolean isConnected = true;

        try (Jedis jedis = this.jedisPool.getResource()) {
            jedis.isConnected();
        } catch (Exception e) {
            this.logger.log(Level.WARNING, "jedisPool is not connected", e);
            isConnected = false;
        }

        try (Jedis jedis = this.subscriberPool.getResource()) {
            jedis.isConnected();
        } catch (Exception e) {
            this.logger.log(Level.WARNING, "subscriberPool is not connected", e);
            isConnected = false;
        }

        try (Jedis jedis = this.publisherPool.getResource()) {
            jedis.isConnected();
        } catch (Exception e) {
            this.logger.log(Level.WARNING, "publisherPool is not connected", e);
            isConnected = false;
        }

        if (!this.pubSub.isSubscribed()) {
            this.logger.log(Level.WARNING, "pubSub is not subscribed");
            isConnected = false;
        }

        return isConnected;
    }

    public CompletableFuture<Void> reconnect() {
        if (this.isReconnecting.get()) {
            throw new RuntimeException("Already reconnecting");
        }

        ExecutorService executor;
        try {
            executor = this.generateExecutorService();
        } catch (Exception e) {
            this.logger.log(Level.WARNING, "Failed to generate executor service", e);
            return CompletableFuture.completedFuture(null);
        }

        return CompletableFuture.runAsync(() -> {
            this.isReconnecting.set(true);
            this.logger.log(Level.INFO, "Reconnecting to redis server");

            this.jedisPool = this.subscriberPool = this.publisherPool = null;

            // unsubscribe from pubSub
            try {
                this.pubSub.unsubscribe();
            } catch (Exception e) {
                this.logger.log(Level.WARNING, "Failed to unsubscribe from redis server", e);
            }

            // flush task
            try {
                this.redisExecutor.shutdownNow();
            } catch (Exception e) {
                this.logger.log(Level.WARNING, "Failed to shutdown redis executor", e);
            }
            try {
                this.redisExecutor = this.generateExecutorService();
            } catch (Exception e) {
                this.logger.log(Level.WARNING, "Failed to get executor service", e);
            }

            try {
                try {
                    this.jedisPool = this.subscriberPool = this.publisherPool =
                            new JedisPool(
                                    new JedisPoolConfig(),
                                    this.redisConfiguration.getHost(),
                                    this.redisConfiguration.getPort(),
                                    this.redisConfiguration.getTimeout(),
                                    this.redisConfiguration.getPassword(),
                                    0,
                                    this.redisConfiguration.getClientName()
                            );
                } catch (Exception e) {
                    this.logger.log(Level.WARNING, "Failed to connect to redis server", e);
                }

                try (Jedis jedis = this.jedisPool.getResource()) {
                    jedis.ping();
                } catch (Exception e) {
                    this.logger.log(Level.WARNING, "Failed to connect to redis server", e);
                }

                try (Jedis jedis = this.subscriberPool.getResource()) {
                    jedis.ping();
                } catch (Exception e) {
                    this.logger.log(Level.WARNING, "Failed to connect to redis server", e);
                }

                try (Jedis jedis = this.publisherPool.getResource()) {
                    jedis.ping();
                } catch (Exception e) {
                    this.logger.log(Level.WARNING, "Failed to connect to redis server", e);
                }

                this.pubSub = null;
                try {
                    this.setupPubSub();
                } catch (Exception e) {
                    this.logger.log(Level.WARNING, "Failed to setup pubSub", e);
                }
            } catch (Exception e) {
                this.logger.log(Level.WARNING, "Failed to reconnect to redis server", e);
                this.jedisPool = this.subscriberPool = this.publisherPool = null;
            } finally {
                this.isReconnecting.set(false);
            }
        }, executor);
    }

    public void registerObject(Class<?> clazz) {
        if (clazz.getAnnotation(RedisObject.class) != null) {
            String packetID = clazz.getAnnotation(RedisObject.class).packetID();

            this.objectMap.put(packetID, clazz);

            this.logger.log(Level.INFO, "Registered redis packet: " + packetID);
        }
    }

    public void registerListener(Object clazz) {
        for (Method method : clazz.getClass().getDeclaredMethods()) {
            if (method.getAnnotation(RedisListener.class) != null) {
                if (method.getParameterTypes().length != 1) {
                    throw new IllegalArgumentException("Method " + method.getName() + " must have one parameter");
                }

                Class<?> aClazz = method.getParameterTypes()[0];
                this.dataMap.put(aClazz.getAnnotation(RedisObject.class).packetID(), new RedisData(clazz, method));

                this.logger.log(Level.INFO, "Registered redis listener: " + method.getName());
            }
        }
    }

    public void broadcastObject(Object object) {
        this.sendObject(object, "none");
    }

    public void sendObject(Object object, @NotNull String targetServer) {
        redisExecutor.execute(() -> {
            RedisObject annotation = object.getClass().getAnnotation(RedisObject.class);
            if (annotation != null) {
                JsonObject jsonObject = new JsonObject();

                String packetID = annotation.packetID();
                jsonObject.addProperty("packetID", packetID);
                jsonObject.addProperty("targetServer", targetServer);

                Class<?> type = objectMap.get(packetID);
                if (type == null) {
                    throw new IllegalArgumentException("No packetID found for " + packetID);
                }
                jsonObject.addProperty("data", gson.toJson(object, type));

                if (this.isReconnecting.get()) {
                    this.logger.log(Level.WARNING, "Cannot send object while reconnecting, packetID: " + packetID);
                    return;
                }

                try (Jedis jedis = this.publisherPool.getResource()) {
                    if (this.redisConfiguration.getPassword() != null) {
                        jedis.auth(this.redisConfiguration.getPassword());
                    }

                    jedis.publish(this.redisConfiguration.getChannel(), jsonObject.toString());
                } catch (Exception e) {
                    this.logger.log(Level.WARNING, "Failed to send object", e);
                    if (this.isReconnecting.get()) {
                        this.logger.log(Level.WARNING, "Cannot send object while reconnecting, packetID: " + packetID);
                        return;
                    }

                    try {
                        this.reconnect()
                                .whenComplete((v, t) -> {
                                    if (t != null) {
                                        this.logger.log(Level.WARNING, "Failed to reconnect to redis server", t);
                                        return;
                                    }

                                    this.logger.log(Level.INFO, "Reconnected to redis server");
                                    this.sendObject(object, targetServer);
                                })
                                .get(15, TimeUnit.SECONDS);
                    } catch (InterruptedException ignored) {
                    } catch (ExecutionException | TimeoutException ex) {
                        this.logger.log(Level.SEVERE, "Failed to reconnect to redis server!", ex);
                    }
                }
            }
        });
    }

    public void close() {
        try {
            this.logger.log(Level.INFO, "Closing redis client");
            if (this.pubSub != null && this.pubSub.isSubscribed()) {
                this.pubSub.unsubscribe();
            }
            if (this.jedisPool != null && !this.jedisPool.isClosed()) {
                this.jedisPool.close();
            }
            if (this.subscriberPool != null && !this.subscriberPool.isClosed()) {
                this.subscriberPool.close();
            }
            if (this.publisherPool != null && !this.publisherPool.isClosed()) {
                this.publisherPool.close();
            }
            this.logger.log(Level.INFO, "Closed redis client");
        } catch (Exception ignored) {
        }
    }

    private void setupPubSub() {
        this.pubSub = new JedisPubSub() {
            @SneakyThrows
            @Override
            public void onMessage(String channel, String message) {
                if (channel.equals(RedisHandler.this.redisConfiguration.getChannel())) {
                    JsonObject jsonObject;

                    try {
                        jsonObject = JsonParser.parseString(message).getAsJsonObject();
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                        return;
                    }

                    String server = jsonObject.get("targetServer").getAsString();
                    if (!server.equals("none") && !serverIdentifier.contains(server)) {
                        return;
                    }

                    String packetID = jsonObject.get("packetID").getAsString();
                    RedisData redisData = dataMap.get(packetID);
                    if (redisData == null) {
                        logger.log(Level.WARNING, "Received unknown packetID: " + packetID);
                        return;
                    }

                    Object clazz = gson.fromJson(jsonObject.get("data"), objectMap.get(packetID));
                    if (clazz == null) {
                        logger.log(Level.WARNING, "Failed to parse data for packetID: " + packetID);
                        return;
                    }

                    redisData.getMethod().invoke(redisData.getObject(), clazz);
                }
            }
        };

        redisExecutor.execute(() -> {
            logger.log(Level.INFO, "Subscribing to redis pubSub channel...");
            try (Jedis jedis = this.subscriberPool.getResource()) {
                jedis.subscribe(this.pubSub, this.redisConfiguration.getChannel());
                logger.log(Level.INFO, "Subscribed to redis pubSub channel.");
            } catch (Exception e) {
                this.subscriberPool = this.publisherPool = null;
            }
        });
    }

    private ExecutorService generateExecutorService() throws Exception {
        if (executorServices.size() > 3) {
            executorServices.get(0).shutdownNow();
            executorServices.remove(0);
        }

        ExecutorService executorService = executorServiceReturnable.get();
        executorServices.add(executorService);
        return executorService;
    }

}

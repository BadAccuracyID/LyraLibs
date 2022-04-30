package id.luckynetwork.lyrams.lyralibs.core.database.redis;

import lombok.Data;

import java.lang.reflect.Method;

@Data
public class RedisData {

    private final Object object;
    private final Method method;

}

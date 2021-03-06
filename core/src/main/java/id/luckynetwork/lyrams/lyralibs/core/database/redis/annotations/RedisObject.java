package id.luckynetwork.lyrams.lyralibs.core.database.redis.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Credit to https://github.com/AndyReckt/Midnight
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RedisObject {

    String packetID();

}

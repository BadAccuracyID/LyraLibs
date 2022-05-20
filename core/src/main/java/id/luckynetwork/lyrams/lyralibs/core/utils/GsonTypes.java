package id.luckynetwork.lyrams.lyralibs.core.utils;

import com.google.gson.reflect.TypeToken;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Type;
import java.util.List;

@UtilityClass
public class GsonTypes {

    public final Type STRING_LIST = new TypeToken<List<String>>() {
    }.getType();
    public final Type INT_LIST = new TypeToken<List<Integer>>() {
    }.getType();
    public final Type LONG_LIST = new TypeToken<List<Long>>() {
    }.getType();


}

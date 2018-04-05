package tk.vn.app.com;

import android.annotation.TargetApi;
import android.os.Build;

import java.util.HashMap;
import java.util.Map;

public class RunTimeStore {

    final static private Map<String,Object> store = new HashMap<>();

    public synchronized static void storeObj(String key, Object value){
        store.put(key,value);
    }

    public synchronized static void removeObj(String key){
        store.remove(key);
    }

    public synchronized static Object getObj(String key){
        return store.get(key);
    }

    @TargetApi(Build.VERSION_CODES.N)
    public synchronized static Object getObj(String key, Object defaultValue){
        return store.getOrDefault(key,defaultValue);
    }

    public static <T> T get(String key){
        return (T)getObj(key);
    }

    public static <T> T get(String key, T defaultValue){
        return (T)getObj(key, defaultValue);
    }

}

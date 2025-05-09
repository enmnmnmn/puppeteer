package top.o_illusions.puppeteer.client.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Input {
    protected static final Map<String, Supplier<Boolean>> keyStates = new HashMap<>();

    public static void register(String key, Supplier<Boolean> getKeyState) {
        if (key == null || getKeyState == null) {
            return;
        }

        keyStates.put(key, getKeyState);
    }

    public static void clear(String key) {
        if (key == null) {
            return;
        }
        keyStates.remove(key);
    }

    public static void clear() {
        keyStates.clear();
    }

    public static Supplier<Boolean> get(String key) {
        if (key == null) {
            return null;
        }
        return keyStates.get(key);
    }

    public static boolean getValue(String key) {
        if (key == null) {
            return false;
        }

        return keyStates.get(key).get();
    }
}

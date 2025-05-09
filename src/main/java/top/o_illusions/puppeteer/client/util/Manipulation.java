package top.o_illusions.puppeteer.client.util;


import java.util.HashMap;
import java.util.Map;

public class Manipulation {
    protected final Map<String, String> commands;
    protected static Manipulation instance = null;

        public static String DUMMY_FORWARD = "dummy.forward";
        public static String DUMMY_BACKWARD = "dummy.backward";
        public static String DUMMY_LEFT = "dummy.left";
        public static String DUMMY_RIGHT = "dummy.right";
        public static String DUMMY_JUMP = "dummy.jump";
        public static String DUMMY_SNEAK = "dummy.sneak";
        public static String DUMMY_STOP = "dummy.stop";
        public static String DUMMY_LOOK = "dummy.look";

    public Manipulation() {
        instance = this;
        this.commands = new HashMap<>();

        this.commands.put(Manipulation.DUMMY_FORWARD, "player %s move forward");
        this.commands.put(Manipulation.DUMMY_BACKWARD, "player %s move backward");
        this.commands.put(Manipulation.DUMMY_LEFT, "player %s move left");
        this.commands.put(Manipulation.DUMMY_RIGHT, "player %s move right");
        this.commands.put(Manipulation.DUMMY_JUMP, "player %s jump continuous");
        this.commands.put(Manipulation.DUMMY_SNEAK, "player %s sneak");
        this.commands.put(Manipulation.DUMMY_STOP, "player %s stop");
        this.commands.put(Manipulation.DUMMY_LOOK, "player %s look at %f %f %f");

        Input.register(Manipulation.DUMMY_FORWARD, () -> {
            return false;
        });
        Input.register(Manipulation.DUMMY_BACKWARD, () -> {
            return false;
        });
        Input.register(Manipulation.DUMMY_LEFT, () -> {
            return false;
        });
        Input.register(Manipulation.DUMMY_RIGHT, () -> {
            return false;
        });
        Input.register(Manipulation.DUMMY_JUMP, () -> {
            return false;
        });
        Input.register(Manipulation.DUMMY_SNEAK, () -> {
            return false;
        });
        Input.register(Manipulation.DUMMY_STOP, () -> {
            return false;
        });
    }

    public String getCommand(String key) {
        if (key == null) {
            return "";
        }

        return this.commands.get(key);
    }

    public static Manipulation getInstance() {
        return instance;
    }
}

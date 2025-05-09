package top.o_illusions.puppeteer.client;

public class KeyState {
    private boolean pressed = false;
    private boolean prevState = false;
    private boolean longPress = false;

    public KeyState(boolean pressed) {
        this.pressed = pressed;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
        this.longPress = pressed && prevState;
        this.prevState = pressed;
    }

    public boolean isPressed() {
        return pressed;
    }

    public boolean isLongPress() {
        return longPress;
    }

    public void setPrevState(boolean prevState) {
        this.prevState = prevState;
    }
}
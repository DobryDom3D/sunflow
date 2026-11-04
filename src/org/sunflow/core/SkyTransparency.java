package org.sunflow.core;

import org.sunflow.image.Color;

// DD3D: Helper used to render the sky transparent when seen through refracting surfaces such as glass
public final class SkyTransparency {
    public static final int NORMAL = 0;
    public static final int ZERO = 1;
    public static final int WHITE = 2;

    private static final class State {
        int mode = NORMAL;
        boolean seen;
    }

    private static final ThreadLocal<State> STATE = new ThreadLocal<State>() {
        protected State initialValue() {
            return new State();
        }
    };

    private SkyTransparency() {
    }

    public static void setMode(int mode) {
        STATE.get().mode = mode;
    }

    /**
     * Clears the flag set when the sky was seen through a refracting surface.
     */
    public static void clearSeen() {
        STATE.get().seen = false;
    }

    /**
     * Returns true if the sky was seen through a refracting surface since last call to clearSeen.
     */
    public static boolean wasSeen() {
        return STATE.get().seen;
    }

    /**
     * Returns the radiance of the sky seen with the given shading state, depending on the current mode
     * if the sky is only seen through refracting surfaces.
     */
    public static Color filter(ShadingState state, Color skyRadiance) {
        if (state.getRefractionDepth() > 0 && state.getReflectionDepth() == 0) {
            State s = STATE.get();
            s.seen = true;
            switch (s.mode) {
                case ZERO:
                    return Color.BLACK;
                case WHITE:
                    return Color.WHITE;
            }
        }
        return skyRadiance;
    }
}

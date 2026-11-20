package com.example.elytraaura;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import org.joml.Vector3f;

public enum WingMode {
    SHADOW("Shadow", new Vector3f(0.45f, 0.1f, 0.7f), ParticleTypes.PORTAL, ParticleTypes.SMOKE),
    LIGHTNING("Lightning", new Vector3f(0.6f, 0.8f, 1.0f), ParticleTypes.END_ROD, ParticleTypes.ELECTRIC_SPARK),
    NATURE("Nature", new Vector3f(0.3f, 0.9f, 0.4f), ParticleTypes.HAPPY_VILLAGER, ParticleTypes.COMPOSTER),
    FROST("Frost", new Vector3f(0.7f, 0.95f, 1.0f), ParticleTypes.SNOWFLAKE, ParticleTypes.END_ROD),
    FIRE("Fire", new Vector3f(1.0f, 0.5f, 0.1f), ParticleTypes.FLAME, ParticleTypes.LAVA);

    public final String displayName;
    public final Vector3f color;
    public final ParticleOptions main;
    public final ParticleOptions accent;

    WingMode(String displayName, Vector3f color, ParticleOptions main, ParticleOptions accent) {
        this.displayName = displayName;
        this.color = color;
        this.main = main;
        this.accent = accent;
    }

    private static WingMode current = SHADOW;

    public static WingMode get() {
        return current;
    }

    public static void cycle() {
        WingMode[] all = values();
        current = all[(current.ordinal() + 1) % all.length];
    }

    public Component label() {
        return Component.literal("Wing mode: " + displayName);
    }
}

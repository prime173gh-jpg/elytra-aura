package com.example.elytraaura;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;

public enum WingMode {
    VOID("void", 0x8A2BE2, ParticleTypes.DRAGON_BREATH),
    COSMOS("cosmos", 0x7B68EE, ParticleTypes.END_ROD),
    LIGHTNING("lightning", 0x5CE1E6, ParticleTypes.ELECTRIC_SPARK),
    NATURE("nature", 0x38E58B, ParticleTypes.HAPPY_VILLAGER),
    ICE("ice", 0x66D9FF, ParticleTypes.SNOWFLAKE),
    FIRE("fire", 0xFF9E2C, ParticleTypes.FLAME);

    public final String id;
    public final int color;
    public final ParticleOptions particle;

    WingMode(String id, int color, ParticleOptions particle) {
        this.id = id;
        this.color = color;
        this.particle = particle;
    }

    public static WingMode byName(String name) {
        for (WingMode mode : values()) {
            if (mode.id.equalsIgnoreCase(name)) {
                return mode;
            }
        }
        return null;
    }
}

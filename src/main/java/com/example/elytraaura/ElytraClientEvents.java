package com.example.elytraaura;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

import java.util.Random;

public class ElytraClientEvents {
    private static final Random RANDOM = new Random();

    public static final KeyMapping SWITCH_KEY = new KeyMapping(
            "key.elytraaura.switch",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_G,
            "key.categories.elytraaura"
    );

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        Level level = mc.level;
        if (player == null || level == null) return;

        while (SWITCH_KEY.consumeClick()) {
            WingMode.cycle();
            player.displayClientMessage(WingMode.get().label(), true);
        }

        if (!player.isFallFlying()) return;

        spawnWingParticles(player, level, WingMode.get());
    }

    private void spawnWingParticles(LocalPlayer player, Level level, WingMode mode) {
        Vec3 look = player.getLookAngle();
        Vec3 right = look.cross(new Vec3(0, 1, 0)).normalize();
        Vec3 base = player.position()
                .add(0, player.getBbHeight() * 0.6, 0)
                .add(look.scale(-0.4));

        DustParticleOptions dust = new DustParticleOptions(mode.color, 1.0f);

        for (int side = -1; side <= 1; side += 2) {
            for (int i = 0; i < 3; i++) {
                double spread = 0.8 + RANDOM.nextDouble() * 1.2;
                double lift = RANDOM.nextDouble() * 0.8 - 0.2;

                Vec3 pos = base.add(right.scale(side * spread)).add(0, lift, 0);

                level.addParticle(dust, pos.x, pos.y, pos.z, 0, 0.02, 0);

                if (RANDOM.nextInt(3) == 0) {
                    level.addParticle(mode.main, pos.x, pos.y, pos.z, 0, 0.05, 0);
                }
                if (RANDOM.nextInt(5) == 0) {
                    level.addParticle(mode.accent, pos.x, pos.y, pos.z, 0, 0.1, 0);
                }
            }
        }
    }
}

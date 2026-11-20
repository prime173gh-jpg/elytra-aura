package com.example.elytraaura;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.joml.Matrix4f;

import java.util.Random;

public class ElytraWingRenderer {
    private static final int FEATHERS = 9;
    private static final Random RANDOM = new Random();

    @SubscribeEvent
    public void onRenderPlayer(RenderPlayerEvent.Post event) {
        Player player = event.getEntity();
        if (!player.isFallFlying()) return;

        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource buffers = event.getMultiBufferSource();
        VertexConsumer vc = buffers.getBuffer(RenderType.lightning());

        float time = player.tickCount + event.getPartialTick();
        WingMode mode = WingMode.get();

        // 0 = مطوية لفوق، 1 = مفتوحة أفقي
        float open = (float) (Math.sin(time * 0.15f) * 0.5 + 0.5);

        poseStack.pushPose();
        poseStack.translate(0.0, 1.25, 0.0);
        Matrix4f matrix = poseStack.last().pose();

        drawWing(vc, matrix, -1, open, time, mode);
        drawWing(vc, matrix, 1, open, time, mode);

        poseStack.popPose();
    }

    private void drawWing(VertexConsumer vc, Matrix4f m, int side, float open, float time, WingMode mode) {
        float openAngle = lerp(1.35f, 0.15f, open);
        float[] root = rootColor(mode);
        float[] tip = {mode.color.x, mode.color.y, mode.color.z};
        float scale = lengthScale(mode);

        for (int i = 0; i < FEATHERS; i++) {
            float f = i / (float) (FEATHERS - 1);

            float angle = openAngle - f * 0.75f;
            float length = (1.15f - f * 0.4f) * (1f + 0.1f * (float) Math.sin(time * 0.3f + i)) * scale;

            // نقطة بداية الريشة على الجسم
            float bx = side * (0.14f + f * 0.12f);
            float by = 0.1f - f * 0.12f;
            float bz = 0.05f;

            // اتجاه الريشة
            float dx = side * (float) Math.cos(angle);
            float dy = (float) Math.sin(angle);

            float tx = bx + dx * length;
            float ty = by + dy * length;
            float tz = bz;

            // البرق بيترعش
            if (mode == WingMode.LIGHTNING) {
                tx += (RANDOM.nextFloat() - 0.5f) * 0.1f;
                ty += (RANDOM.nextFloat() - 0.5f) * 0.1f;
            }

            // عرض الريشة
            float hw = (0.16f * (1f - f * 0.5f)) / 2f;
            float px = -dy * hw;
            float py = dx * hw;

            // أربع نقط: قاعدة الريشة (لونها الجذر) وطرفها (لونه الأساسي للمود)
            vertex(vc, m, bx + px, by + py, bz, root, 0.9f);
            vertex(vc, m, bx - px, by - py, bz, root, 0.9f);
            vertex(vc, m, tx - px * 0.2f, ty - py * 0.2f, tz, tip, 0f);
            vertex(vc, m, tx + px * 0.2f, ty + py * 0.2f, tz, tip, 0f);
        }
    }

    private void vertex(VertexConsumer vc, Matrix4f m, float x, float y, float z, float[] c, float alpha) {
        vc.vertex(m, x, y, z).color(c[0], c[1], c[2], alpha).endVertex();
    }

    private static float[] rootColor(WingMode mode) {
        switch (mode) {
            case SHADOW:    return new float[]{0.05f, 0.0f, 0.1f};
            case LIGHTNING: return new float[]{0.4f, 0.5f, 0.9f};
            case NATURE:    return new float[]{0.1f, 0.4f, 0.2f};
            case FROST:     return new float[]{0.3f, 0.6f, 0.9f};
            case FIRE:      return new float[]{0.9f, 0.25f, 0.05f};
            default:        return new float[]{1f, 1f, 1f};
        }
    }

    private static float lengthScale(WingMode mode) {
        switch (mode) {
            case SHADOW:    return 1.25f;
            case FIRE:      return 1.2f;
            case NATURE:
            case FROST:     return 1.1f;
            default:        return 1.0f;
        }
    }

    private static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }
}

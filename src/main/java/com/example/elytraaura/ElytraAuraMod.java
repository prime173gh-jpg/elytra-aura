package com.example.elytraaura;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(ElytraAuraMod.MOD_ID)
public class ElytraAuraMod {
    public static final String MOD_ID = "elytraaura";

    public ElytraAuraMod() {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            FMLJavaModLoadingContext.get().getModEventBus()
                    .addListener(ElytraAuraMod::registerKeys);
            MinecraftForge.EVENT_BUS.register(new ElytraClientEvents());
        }
    }

    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(ElytraClientEvents.SWITCH_KEY);
    }
}

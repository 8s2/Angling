package com.eightsidedsquare.angling.core;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static com.eightsidedsquare.angling.core.AnglingMod.MOD_ID;

public class AnglingParticles {

    public static DefaultParticleType ALGAE = Registry.register(Registries.PARTICLE_TYPE, new Identifier(MOD_ID, "algae"), FabricParticleTypes.simple(true));
    public static DefaultParticleType WORM = Registry.register(Registries.PARTICLE_TYPE, new Identifier(MOD_ID, "worm"), FabricParticleTypes.simple(true));

    public static void init() {

    }

}

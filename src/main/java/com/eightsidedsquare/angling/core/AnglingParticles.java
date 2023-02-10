package com.eightsidedsquare.angling.core;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static com.eightsidedsquare.angling.core.AnglingMod.MOD_ID;

public class AnglingParticles {

    public static DefaultParticleType ALGAE = Registry.register(Registry.PARTICLE_TYPE, new Identifier(MOD_ID, "algae"), FabricParticleTypes.simple(true));
    public static DefaultParticleType WORM = Registry.register(Registry.PARTICLE_TYPE, new Identifier(MOD_ID, "worm"), FabricParticleTypes.simple(true));

    public static void init() {

    }

}

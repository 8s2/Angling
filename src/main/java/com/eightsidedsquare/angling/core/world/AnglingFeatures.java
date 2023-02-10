package com.eightsidedsquare.angling.core.world;

import com.eightsidedsquare.angling.common.feature.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.*;

import static com.eightsidedsquare.angling.core.AnglingMod.MOD_ID;

public class AnglingFeatures {

    public static final Feature<SimpleBlockFeatureConfig> WATERLOGGABLE_PATCH = register("waterloggable_patch", new WaterloggablePatchFeature(SimpleBlockFeatureConfig.CODEC));
    public static final Feature<DefaultFeatureConfig> WORMY_BLOCK = register("wormy_block", new WormyBlockFeature(DefaultFeatureConfig.CODEC));
    public static final Feature<NoisePatchFeatureConfig> NOISE_PATCH = register("noise_patch", new NoisePatchFeature(NoisePatchFeatureConfig.CODEC));
    public static final Feature<RandomPatchFeatureConfig> WATER_ADJACENT_PATCH = register("water_adjacent_patch", new WaterAdjacentPatchFeature(RandomPatchFeatureConfig.CODEC));

    private static <C extends FeatureConfig, F extends Feature<C>> F register(String name, F feature) {
        return Registry.register(Registry.FEATURE, new Identifier(MOD_ID, name), feature);
    }
}

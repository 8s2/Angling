package com.eightsidedsquare.angling.core.world;

import com.eightsidedsquare.angling.common.feature.WaterloggablePatchFeature;
import com.eightsidedsquare.angling.common.feature.WormyBlockFeature;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.SimpleBlockFeatureConfig;

import static com.eightsidedsquare.angling.core.AnglingMod.MOD_ID;

public class AnglingFeatures {

    public static final Feature<SimpleBlockFeatureConfig> WATERLOGGABLE_PATCH = register("waterloggable_patch", new WaterloggablePatchFeature(SimpleBlockFeatureConfig.CODEC));
    public static final Feature<DefaultFeatureConfig> WORMY_BLOCK = register("wormy_block", new WormyBlockFeature(DefaultFeatureConfig.CODEC));

    private static <C extends FeatureConfig, F extends Feature<C>> F register(String name, F feature) {
        return Registry.register(Registry.FEATURE, new Identifier(MOD_ID, name), feature);
    }
}

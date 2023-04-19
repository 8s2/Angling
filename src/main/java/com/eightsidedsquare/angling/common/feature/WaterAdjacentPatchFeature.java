package com.eightsidedsquare.angling.common.feature;

import com.mojang.serialization.Codec;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.RandomPatchFeature;
import net.minecraft.world.gen.feature.RandomPatchFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class WaterAdjacentPatchFeature extends RandomPatchFeature {

    public WaterAdjacentPatchFeature(Codec<RandomPatchFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<RandomPatchFeatureConfig> ctx) {
        BlockPos pos = ctx.getOrigin();
        StructureWorldAccess world = ctx.getWorld();
        Vec3i offset = new Vec3i(5, 5, 5);
        if(BlockPos.stream(pos.subtract(offset), pos.add(offset)).noneMatch(blockPos -> world.getFluidState(pos).isIn(FluidTags.WATER)))
            return false;
        return super.generate(ctx);
    }
}

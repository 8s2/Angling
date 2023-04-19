package com.eightsidedsquare.angling.common.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.SimpleBlockFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class WaterloggablePatchFeature extends Feature<SimpleBlockFeatureConfig> {
    public WaterloggablePatchFeature(Codec<SimpleBlockFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<SimpleBlockFeatureConfig> ctx) {
        boolean bl = false;
        Random random = ctx.getRandom();
        StructureWorldAccess structureWorldAccess = ctx.getWorld();
        BlockPos blockPos = ctx.getOrigin();
        int count = random.nextBetween(16, 32);
        int range = 6;
        for(int i = 0; i < count; i++) {
            int dx = random.nextInt(range) - random.nextInt(range);
            int dz = random.nextInt(range) - random.nextInt(range);
            int y = structureWorldAccess.getTopY(Heightmap.Type.OCEAN_FLOOR, blockPos.getX() + dx, blockPos.getZ() + dz);
            BlockPos blockPos2 = new BlockPos(blockPos.getX() + dx, y, blockPos.getZ() + dz);
            BlockState state = ctx.getConfig().toPlace().get(random, blockPos2);
            if (state.canPlaceAt(structureWorldAccess, blockPos2) &&
                    !structureWorldAccess.getBlockState(blockPos2.up()).isOf(Blocks.TALL_SEAGRASS) &&
                    !structureWorldAccess.getBlockState(blockPos2.down()).isIn(BlockTags.ICE)) {
                if(state.contains(Properties.WATERLOGGED))
                    state = state.with(Properties.WATERLOGGED, structureWorldAccess.getFluidState(blockPos2).isOf(Fluids.WATER));
                structureWorldAccess.setBlockState(blockPos2, state, Block.NOTIFY_LISTENERS);

                bl = true;
            }
        }

        return bl;
    }
}

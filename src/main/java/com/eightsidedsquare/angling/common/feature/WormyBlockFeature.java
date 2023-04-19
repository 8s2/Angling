package com.eightsidedsquare.angling.common.feature;

import com.eightsidedsquare.angling.common.block.WormyBlock;
import com.eightsidedsquare.angling.core.AnglingBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class WormyBlockFeature extends Feature<DefaultFeatureConfig> {
    public WormyBlockFeature(Codec<DefaultFeatureConfig> configCodec) {
        super(configCodec);
    }

    private boolean surroundedByDirt(BlockPos pos, StructureWorldAccess world) {
        return Direction.stream().allMatch(d -> world.getBlockState(pos.offset(d)).isIn(BlockTags.DIRT));
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> ctx) {
        Random random = ctx.getRandom();
        StructureWorldAccess world = ctx.getWorld();
        BlockPos origin = ctx.getOrigin();
        int y = world.getTopY(Heightmap.Type.OCEAN_FLOOR, origin.getX(), origin.getZ());
        BlockPos wormyBlockPos = new BlockPos(origin.getX(), y - 2, origin.getZ());
        if(surroundedByDirt(wormyBlockPos, world)) {
            int count = random.nextBetween(1, 3);
            BlockState state = world.getBlockState(wormyBlockPos);
            if(state.isOf(Blocks.DIRT)) {
                world.setBlockState(wormyBlockPos, AnglingBlocks.WORMY_DIRT.getDefaultState().with(WormyBlock.WORMS, count), Block.NOTIFY_LISTENERS);
                return true;
            }else if(state.isOf(Blocks.MUD)) {
                world.setBlockState(wormyBlockPos, AnglingBlocks.WORMY_MUD.getDefaultState().with(WormyBlock.WORMS, count), Block.NOTIFY_LISTENERS);
                return true;
            }
        }
        return false;
    }
}

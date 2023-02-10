package com.eightsidedsquare.angling.mixin;

import com.eightsidedsquare.angling.common.block.StarfishBlock;
import com.eightsidedsquare.angling.core.AnglingBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FacingBlock;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.CoralFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CoralFeature.class)
public abstract class CoralFeatureMixin {

    @Inject(method = "generateCoralPiece", at = @At("RETURN"))
    protected void generateCoralPiece(WorldAccess world, Random random, BlockPos pos, BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if(cir.getReturnValue()) {
            Direction.stream()
                    .filter(d -> random.nextFloat() < 0.0125d && canPlace(pos.offset(d), world))
                    .forEach(d -> {
                        world.setBlockState(pos.offset(d),
                                AnglingBlocks.STARFISH.getDefaultState().with(FacingBlock.FACING, d), Block.NOTIFY_LISTENERS);
                        StarfishBlock.randomize(world, pos.offset(d), random);
                    });
            if(random.nextFloat() < 0.01f && canPlace(pos.up(), world)) {
                world.setBlockState(pos.up(), AnglingBlocks.ANEMONE.getDefaultState().with(Properties.WATERLOGGED, true), Block.NOTIFY_LISTENERS);
            }
        }
    }

    private boolean canPlace(BlockPos pos, WorldAccess world) {
        BlockState state = world.getBlockState(pos);
        return state.isIn(BlockTags.CORALS) || state.isIn(BlockTags.WALL_CORALS) || state.isOf(Blocks.WATER);
    }

}

package com.eightsidedsquare.angling.common.block;

import com.eightsidedsquare.angling.core.AnglingItems;
import com.eightsidedsquare.angling.core.AnglingParticles;
import com.eightsidedsquare.angling.core.AnglingSounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public interface WormyBlock {

    IntProperty WORMS = IntProperty.of("worms", 1, 3);

    BlockState getDefaultBlockState();

    default ActionResult addOrRemoveWorms(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if(player.getMainHandStack().isEmpty() && stack.isEmpty()) {
            decrementWorms(state, pos, world);
            player.giveItemStack(new ItemStack(AnglingItems.WORM));
            world.playSound(null, pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5d, AnglingSounds.ITEM_WORM_USE, SoundCategory.BLOCKS, 1, 1);
            return ActionResult.success(world.isClient);
        }else if(stack.isOf(AnglingItems.WORM) && state.get(WORMS) < 3) {
            if(!player.getAbilities().creativeMode)
                stack.decrement(1);
            incrementWorms(state, pos, world);
            world.playSound(null, pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5d, AnglingSounds.ITEM_WORM_USE, SoundCategory.BLOCKS, 1, 1);
            return ActionResult.success(world.isClient);
        }
        return ActionResult.PASS;
    }

    default void incrementWorms(BlockState state, BlockPos pos, World world) {
        int worms = state.get(WORMS);
        if(worms < 3) {
            world.setBlockState(pos, state.with(WORMS, worms + 1));
        }
    }

    default void decrementWorms(BlockState state, BlockPos pos, World world) {
        int worms = state.get(WORMS);
        if(worms == 1) {
            world.setBlockState(pos, getDefaultBlockState());
        }else {
            world.setBlockState(pos, state.with(WORMS, worms - 1));
        }
    }

    default void appendWormProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(WORMS);
    }

    default void tickWorms(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if(state.get(WORMS) < 3 && random.nextInt(8) == 0) {
            if(Direction.stream().filter(d -> world.getBlockState(pos.offset(d)).isIn(BlockTags.DIRT)).toList().size() == Direction.values().length) {
                incrementWorms(state, pos, world);
            }
        }
    }

    default void spawnWormParticles(World world, BlockPos pos, Random random) {
        if(random.nextInt(8) == 0) {
            BlockPos.Mutable mutable = pos.mutableCopy();
            mutable.move(Direction.UP);
            while (world.getBlockState(mutable).isIn(BlockTags.DIRT)) {
                mutable.move(Direction.UP);
            }
            if (!world.getBlockState(mutable).isSolidBlock(world, pos) &&
                    ((world.isRaining() && world.isSkyVisible(mutable.up())) || world.getFluidState(mutable).isIn(FluidTags.WATER))) {
                double x = mutable.getX() + 0.5d + random.nextGaussian() * 0.3f;
                double y = mutable.getY();
                double z = mutable.getZ() + 0.5d + random.nextGaussian() * 0.3f;
                world.addParticle(AnglingParticles.WORM, x, y, z, 0, 0, 0);
            }
        }
    }
}

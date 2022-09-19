package com.eightsidedsquare.angling.common.block;

import net.minecraft.block.BlockState;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public interface FilterFeeder {

    void onFeed(BlockPos pos, BlockState state, ServerWorld world);

    default void createFedParticles(int count, BlockPos pos, ServerWorld world) {
        world.spawnParticles(ParticleTypes.HAPPY_VILLAGER, pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5d, count, 0.25d, 0.25d, 0.25d, 0);
    }
}

package com.eightsidedsquare.angling.common.entity.ai;

import com.eightsidedsquare.angling.common.entity.PelicanEntity;
import com.eightsidedsquare.angling.core.ai.AnglingMemoryModuleTypes;
import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;

public class PelicanEatTask extends MultiTickTask<PelicanEntity> {
    public PelicanEatTask() {
        super(ImmutableMap.of(AnglingMemoryModuleTypes.HAS_TRADED, MemoryModuleState.VALUE_PRESENT));
    }

    @Override
    protected boolean shouldRun(ServerWorld world, PelicanEntity entity) {
        return entity.hasEntityInBeak();
    }

    @Override
    protected void run(ServerWorld world, PelicanEntity entity, long time) {
        entity.setEntityInBeak(new NbtCompound());
        entity.setBeakOpen(false);
        entity.getBrain().forget(AnglingMemoryModuleTypes.HAS_TRADED);
    }
}

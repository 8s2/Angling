package com.eightsidedsquare.angling.common.entity.ai;

import com.eightsidedsquare.angling.common.entity.PelicanEntity;
import com.eightsidedsquare.angling.core.ai.AnglingMemoryModuleTypes;
import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.server.world.ServerWorld;

import java.util.stream.Stream;

public class PelicanTradeTask extends MultiTickTask<PelicanEntity> {
    public PelicanTradeTask() {
        super(ImmutableMap.of(
                MemoryModuleType.WALK_TARGET, MemoryModuleState.VALUE_ABSENT,
                MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleState.VALUE_PRESENT,
                AnglingMemoryModuleTypes.CAN_TRADE, MemoryModuleState.VALUE_PRESENT));
    }

    @Override
    protected boolean shouldRun(ServerWorld world, PelicanEntity entity) {
        return entity.hasEntityInBeak() &&
                entity.getBrain().isMemoryInState(MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleState.VALUE_PRESENT) &&
                entity.getBrain().isMemoryInState(AnglingMemoryModuleTypes.CAN_TRADE, MemoryModuleState.VALUE_PRESENT);
    }

    @Override
    protected void run(ServerWorld world, PelicanEntity entity, long time) {
        entity.setBeakOpen(true);
        PelicanBrain.getPlayerLookTarget(entity).ifPresent(player -> {
            LookTargetUtil.walkTowards(entity, player, 1f, 5);
            if(!entity.isOnGround() && player.getPos().distanceTo(entity.getPos()) < 5 && canLand(world, entity)) {
                entity.addVelocity(0, -0.01, 0);
            }
        });
    }

    private boolean canLand(ServerWorld world, PelicanEntity entity) {
        return Stream.of(entity.getBlockPos().down(), entity.getBlockPos().down(2), entity.getBlockPos().down(3))
                .anyMatch(pos -> world.getBlockState(pos).isSolidBlock(world, pos));
    }

    @Override
    protected void keepRunning(ServerWorld world, PelicanEntity entity, long time) {
        run(world, entity, time);
    }

    @Override
    protected void finishRunning(ServerWorld world, PelicanEntity entity, long time) {
        entity.getBrain().forget(AnglingMemoryModuleTypes.CAN_TRADE);
    }

    @Override
    protected boolean shouldKeepRunning(ServerWorld world, PelicanEntity entity, long time) {
        return shouldRun(world, entity) && entity.getBrain().isMemoryInState(MemoryModuleType.IS_PANICKING, MemoryModuleState.VALUE_ABSENT);
    }
}

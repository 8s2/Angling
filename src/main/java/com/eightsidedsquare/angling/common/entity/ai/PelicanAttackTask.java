package com.eightsidedsquare.angling.common.entity.ai;

import com.eightsidedsquare.angling.common.entity.PelicanEntity;
import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.server.world.ServerWorld;

public class PelicanAttackTask extends MultiTickTask<PelicanEntity> {
    private final long interval;
    private Phase phase;
    private int catchingTicks;

    public PelicanAttackTask(long interval) {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryModuleState.REGISTERED, MemoryModuleType.ATTACK_TARGET, MemoryModuleState.VALUE_PRESENT, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleState.VALUE_ABSENT));
        this.interval = interval;
        phase = Phase.DONE;
    }

    protected LivingEntity getTarget(PelicanEntity entity) {
        return entity.getBrain().getOptionalMemory(MemoryModuleType.ATTACK_TARGET).orElse(null);
    }

    protected boolean hasValidTarget(PelicanEntity entity) {
        LivingEntity target = getTarget(entity);
        if(target != null) {
            return target.isAlive();
        }
        return false;
    }

    @Override
    protected void run(ServerWorld world, PelicanEntity entity, long time) {
        phase = Phase.MOVE_TO_TARGET;
        catchingTicks = 0;
    }

    @Override
    protected void keepRunning(ServerWorld world, PelicanEntity entity, long time) {
        LivingEntity target = getTarget(entity);
        if(target != null) {
            switch (phase) {
                case MOVE_TO_TARGET -> {
                    if(shouldDive(target, entity)) {
                        entity.addVelocity(0, -0.1, 0);
                        entity.setDiving(true);
                    }else {
                        entity.setDiving(false);
                        if(entity.isTouchingWater()) {
                            entity.addVelocity(0, 0.15f, 0);
                        }
                    }
                    LookTargetUtil.lookAt(entity, target);
                    if(target.distanceTo(entity) < 1.5f && PelicanBrain.canPutInBeak(target) && !entity.hasEntityInBeak()) {
                        phase = Phase.CATCHING;
                        entity.setBeakOpen(true);
                        target.setVelocity(target.getPos().relativize(entity.getPos().add(0, 0.5f, 0)).normalize().multiply(0.75D));
                    }else if(!PelicanBrain.canPutInBeak(target) && entity.isInAttackRange(target)){
                        attack(target, entity, world, time);
                    }
                }
                case CATCHING -> {
                    if(catchingTicks++ >= 4) {
                        phase = Phase.DONE;
                        entity.setEntityInBeak(target);
                        target.discard();
                        entity.getBrain().remember(MemoryModuleType.ATTACK_COOLING_DOWN, true, this.interval);
                    }
                }
                case DONE -> {}
            }
        }
    }

    protected boolean shouldDive(LivingEntity target, PelicanEntity entity) {
        return target.isTouchingWater()
                && entity.isFlying()
                && entity.getY() > target.getY() + 0.5f
                && entity.getPos().multiply(1, 0, 1).distanceTo(target.getPos().multiply(1, 0, 1)) < 1.5f;
    }

    protected void attack(LivingEntity target, PelicanEntity entity, ServerWorld world, long time) {
        if(entity.tryAttack(target)) {
            entity.getBrain().remember(MemoryModuleType.ATTACK_COOLING_DOWN, true, this.interval);
            stop(world, entity, time);
        }
    }

    @Override
    protected void finishRunning(ServerWorld world, PelicanEntity entity, long time) {
        entity.setDiving(false);
    }

    @Override
    protected boolean shouldRun(ServerWorld world, PelicanEntity entity) {
        return hasValidTarget(entity) && !entity.hasEntityInBeak();
    }

    @Override
    protected boolean shouldKeepRunning(ServerWorld world, PelicanEntity entity, long time) {
        return phase != Phase.DONE
                && entity.getBrain().isMemoryInState(MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleState.VALUE_ABSENT)
                && !entity.getBrain().hasMemoryModule(MemoryModuleType.IS_PANICKING)
                && hasValidTarget(entity);
    }

    enum Phase {
        MOVE_TO_TARGET,
        CATCHING,
        DONE
    }
}

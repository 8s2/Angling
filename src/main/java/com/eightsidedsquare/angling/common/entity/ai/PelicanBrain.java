package com.eightsidedsquare.angling.common.entity.ai;

import com.eightsidedsquare.angling.common.entity.PelicanEntity;
import com.eightsidedsquare.angling.core.ai.AnglingMemoryModuleTypes;
import com.eightsidedsquare.angling.core.tags.AnglingEntityTypeTags;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.*;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Unit;
import net.minecraft.util.math.intprovider.UniformIntProvider;

import java.util.Objects;
import java.util.Optional;

public class PelicanBrain {

    public static Brain<PelicanEntity> create(Brain<PelicanEntity> brain) {
        addCoreActivities(brain);
        addIdleActivities(brain);
        addFightActivities(brain);
        addSoarActivities(brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.resetPossibleActivities();
        return brain;
    }

    private static void addCoreActivities(Brain<PelicanEntity> brain) {
        /*
        brain.setTaskList(Activity.CORE, 0, ImmutableList.of(
                new StayAboveWaterTask(0.8F),
                new WalkTask(2.5F),
                new LookAroundTask(45, 90),
                new WanderAroundTask(),
                new UpdateAttackTargetTask<>(entity -> entity.getBrain().getOptionalMemory(MemoryModuleType.NEAREST_ATTACKABLE))
        ));
         */
    }

    private static void addIdleActivities(Brain<PelicanEntity> brain) {
        /*
        brain.setTaskList(Activity.IDLE, ImmutableList.of(
                Pair.of(0, new UpdateAttackTargetTask<>(entity -> entity.getBrain().getOptionalMemory(MemoryModuleType.NEAREST_ATTACKABLE))),
                Pair.of(1, new PelicanTradeTask()),
                Pair.of(2, new TimeLimitedTask<>(new FollowMobTask(EntityType.PLAYER, 6.0F), UniformIntProvider.create(30, 60))),
                Pair.of(3, new RandomTask<>(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryModuleState.VALUE_ABSENT), ImmutableList.of(
                        Pair.of(new StrollTask(1f), 1),
                        Pair.of(new GoTowardsLookTarget(1f, 3), 1),
                        Pair.of(new ConditionalTask<>(Entity::isOnGround, new WaitTask(5, 20)), 2),
                        Pair.of(new ConditionalTask<>(PelicanEntity::isFlying, new NoPenaltyStrollTask(1f)), 2))))
        ));

         */
    }

    private static void addFightActivities(Brain<PelicanEntity> brain) {
        /*
        brain.setTaskList(Activity.FIGHT,0, ImmutableList.of(
                new ForgetAttackTargetTask<>(),
                new RangedApproachTask(entity -> 1f),
                new PelicanAttackTask(5)
        ), MemoryModuleType.ATTACK_TARGET);

         */
    }

    private static void addSoarActivities(Brain<PelicanEntity> brain) {
        /*
        brain.setTaskList(Activity.RIDE, ImmutableList.of(
                Pair.of(0, new UpdateAttackTargetTask<>(entity -> entity.getBrain().getOptionalMemory(MemoryModuleType.NEAREST_ATTACKABLE))),
                Pair.of(1, new PelicanEatTask()),
                Pair.of(2, new PelicanSoarTask())
        ), ImmutableSet.of(
                Pair.of(AnglingMemoryModuleTypes.SOARING_COOLDOWN, MemoryModuleState.VALUE_ABSENT),
                Pair.of(AnglingMemoryModuleTypes.CAN_TRADE, MemoryModuleState.VALUE_ABSENT))
        );

         */
    }

    public static void updateActivities(PelicanEntity entity) {
        Activity activityBeforeReset = entity.getBrain().getFirstPossibleNonCoreActivity().orElse(null);
        entity.getBrain().resetPossibleActivities(ImmutableList.of(Activity.FIGHT, Activity.RIDE, Activity.IDLE));
        Activity activityAfterReset = entity.getBrain().getFirstPossibleNonCoreActivity().orElse(null);
        if(Objects.equals(activityBeforeReset, Activity.FIGHT) && !Objects.equals(activityAfterReset, Activity.FIGHT)) {
            entity.getBrain().remember(MemoryModuleType.HAS_HUNTING_COOLDOWN, true, 2400L);
            if(!entity.hasEntityInBeak() && entity.isBeakOpen()) {
                entity.setBeakOpen(false);
            }
        }else if(Objects.equals(activityBeforeReset, Activity.RIDE) && !Objects.equals(activityAfterReset, Activity.RIDE)) {
            entity.getBrain().remember(AnglingMemoryModuleTypes.SOARING_COOLDOWN, Unit.INSTANCE, 1000L);
        }
        if(entity.hasEntityInBeak()
                && entity.getBrain().isMemoryInState(MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleState.VALUE_PRESENT)
                && entity.getBrain().isMemoryInState(AnglingMemoryModuleTypes.CAN_TRADE, MemoryModuleState.VALUE_ABSENT)) {
            entity.getBrain().remember(AnglingMemoryModuleTypes.CAN_TRADE, Unit.INSTANCE, entity.getRandom().nextBetween(600, 1200));
        }
    }

    public static Optional<LookTarget> getPlayerLookTarget(LivingEntity entity) {
        Optional<PlayerEntity> optional = entity.getBrain().getOptionalMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER);
        return optional.map(player -> new EntityLookTarget(player, true));
    }

    public static boolean canPutInBeak(LivingEntity target) {
        return target.getType().isIn(AnglingEntityTypeTags.HUNTED_BY_PELICAN) &&
                (!target.getType().isIn(AnglingEntityTypeTags.HUNTED_BY_PELICAN_WHEN_BABY) || target.isBaby());
    }
}

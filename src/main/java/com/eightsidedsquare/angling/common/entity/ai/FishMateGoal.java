package com.eightsidedsquare.angling.common.entity.ai;

import com.eightsidedsquare.angling.cca.AnglingEntityComponents;
import com.eightsidedsquare.angling.cca.FishSpawningComponent;
import com.eightsidedsquare.angling.core.AnglingUtil;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;

public class FishMateGoal extends Goal {

    private final FishEntity entity;
    @Nullable
    private FishEntity mate;
    private final World world;
    private static final TargetPredicate VALID_MATE_PREDICATE = TargetPredicate.createNonAttackable().setBaseMaxDistance(8.0D).ignoreVisibility();

    public FishMateGoal(WaterCreatureEntity entity) {
        this.entity = (FishEntity) entity;
        this.world = entity.getWorld();
        setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public void tick() {
        if(mate != null) {
            entity.getLookControl().lookAt(mate, entity.getMaxLookYawChange(), entity.getMaxLookPitchChange());
//            double d = 0.005;
//            double min = 0.05;
//            double dX = (mate.getX() - entity.getX()) * d;
//            dX = Math.min(min, Math.abs(dX)) * (dX > 0 ? 1 : -1);
//            double dY = (mate.getY() - entity.getY()) * d;
//            dY = Math.min(min, Math.abs(dY)) * (dY > 0 ? 1 : -1);
//            double dZ = (mate.getZ() - entity.getZ()) * d;
//            dZ = Math.min(min, Math.abs(dZ)) * (dZ > 0 ? 1 : -1);
//            entity.addVelocity(dX, dY, dZ);
            entity.getNavigation().startMovingTo(mate.getX(), mate.getY(), mate.getZ(), 2d);
            if(entity.distanceTo(mate) < 1.5d) {
                FishSpawningComponent component = AnglingEntityComponents.FISH_SPAWNING.get(entity);
                FishSpawningComponent mateComponent = AnglingEntityComponents.FISH_SPAWNING.get(mate);
                component.setLoveTicks(0);
                mateComponent.setLoveTicks(0);
                component.setLoveCooldown(3000);
                mateComponent.setLoveCooldown(3000);
                component.createHeartParticles();
                component.setCarryingRoe(true);
                component.setMateData(AnglingUtil.entityToNbt(mate, true));
                if (world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
                    world.spawnEntity(new ExperienceOrbEntity(world, entity.getX(), entity.getY(), entity.getZ(), entity.getRandom().nextInt(7) + 1));
                }
            }
        }
    }

    @Override
    public boolean shouldContinue() {
        if(mate == null || !mate.isAlive()) {
            return false;
        }
        return entity.isTouchingWater() && mate.isTouchingWater() && canBeBred(entity) && canBeBred(mate);
    }

    @Override
    public boolean canStart() {
        if(!canBeBred(entity) || !entity.isTouchingWater()) {
            return false;
        }
        mate = findMate();
        return mate != null;
    }

    private boolean canBeBred(FishEntity fishEntity) {
        FishSpawningComponent component = AnglingEntityComponents.FISH_SPAWNING.get(fishEntity);
        return component.isInLove() && !component.isCarryingRoe() && !component.hasCooldown();
    }

    @Nullable
    private FishEntity findMate() {

        List<? extends FishEntity> list = world.getTargets(entity.getClass(), VALID_MATE_PREDICATE, entity, entity.getBoundingBox().expand(16.0D));
        double d = 16;
        FishEntity fishEntity = null;

        for (FishEntity testFishEntity : list) {
            if (canBeBred(testFishEntity) && entity.distanceTo(testFishEntity) < d && testFishEntity.getType().equals(entity.getType())) {
                fishEntity = testFishEntity;
                d = entity.distanceTo(testFishEntity);
            }
        }

        return fishEntity;
    }
}

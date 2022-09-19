package com.eightsidedsquare.angling.common.entity.ai;

import com.eightsidedsquare.angling.common.entity.SeaSlugEntity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;

public class SeaSlugMateGoal extends Goal {

    protected final SeaSlugEntity entity;
    protected final World world;
    @Nullable
    protected SeaSlugEntity mate;
    private static final TargetPredicate VALID_MATE_PREDICATE = TargetPredicate.createNonAttackable().setBaseMaxDistance(8.0D).ignoreVisibility();

    public SeaSlugMateGoal(SeaSlugEntity entity) {
        this.entity = entity;
        this.world = entity.world;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    @Override
    public void tick() {
        if(mate != null) {
            entity.getLookControl().lookAt(mate, entity.getMaxLookYawChange(), entity.getMaxLookPitchChange());
            entity.getNavigation().startMovingTo(mate.getX(), mate.getY(), mate.getZ(), 2d);
            if(entity.distanceTo(mate) < 1.5d) {
                entity.setLoveTicks(0);
                mate.setLoveTicks(0);
                entity.setLoveCooldown(3000);
                mate.setLoveCooldown(3000);
                entity.createHeartParticles();
                entity.setHasEggs(true);
                mate.setHasEggs(true);
                entity.setMateData(mate.writeMateData(new NbtCompound()));
                mate.setMateData(entity.writeMateData(new NbtCompound()));
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
        return canBeBred(entity) && canBeBred(mate);
    }

    @Override
    public boolean canStart() {
        if (!canBeBred(entity)) {
            return false;
        } else {
            this.mate = this.findMate();
            return this.mate != null;
        }
    }

    private boolean canBeBred(SeaSlugEntity entity) {
        return !entity.hasEggs() && entity.isInLove() && entity.isTouchingWater();
    }

    @Nullable
    private SeaSlugEntity findMate() {

        List<? extends SeaSlugEntity> list = world.getTargets(entity.getClass(), VALID_MATE_PREDICATE, entity, entity.getBoundingBox().expand(16.0D));
        double d = 16;
        SeaSlugEntity mate = null;

        for (SeaSlugEntity testMate : list) {
            if (canBeBred(testMate) && entity.distanceTo(testMate) < d && testMate.getType().equals(entity.getType())) {
                mate = testMate;
                d = entity.distanceTo(testMate);
            }
        }

        return mate;
    }
}

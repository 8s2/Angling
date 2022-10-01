package com.eightsidedsquare.angling.common.entity.ai;

import net.minecraft.entity.ai.goal.MoveToTargetPosGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

public class GoToWaterGoal extends MoveToTargetPosGoal {

    private final PathAwareEntity entity;

    public GoToWaterGoal(PathAwareEntity mob, double speed, int range) {
        super(mob, speed, range);
        this.entity = mob;
    }

    @Override
    protected int getInterval(PathAwareEntity mob) {
        return toGoalTicks(20 + mob.getRandom().nextInt(20));
    }

    @Override
    public boolean canStart() {
        return super.canStart() && !entity.isInsideWaterOrBubbleColumn();
    }

    @Override
    public boolean shouldContinue() {
        return super.shouldContinue() && !entity.isInsideWaterOrBubbleColumn();
    }

    @Override
    public double getDesiredDistanceToTarget() {
        return 0;
    }

    @Override
    protected boolean isTargetPos(WorldView world, BlockPos pos) {
        return world.getFluidState(pos.up()).isIn(FluidTags.WATER);
    }
}

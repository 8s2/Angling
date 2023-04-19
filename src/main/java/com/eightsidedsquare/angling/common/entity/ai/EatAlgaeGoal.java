package com.eightsidedsquare.angling.common.entity.ai;

import com.eightsidedsquare.angling.common.block.AlgaeBlock;
import com.eightsidedsquare.angling.core.AnglingBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.goal.MoveToTargetPosGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

public class EatAlgaeGoal extends MoveToTargetPosGoal {

    private final PathAwareEntity entity;
    private boolean finished;

    public EatAlgaeGoal(PathAwareEntity mob, double speed, int range) {
        super(mob, speed, range, 5);
        this.entity = mob;
    }

    @Override
    public void start() {
        super.start();
        finished = false;
    }

    @Override
    protected boolean isTargetPos(WorldView world, BlockPos pos) {
        BlockState state = world.getBlockState(pos.up());
        return state.isOf(AnglingBlocks.ALGAE) && state.getFluidState().isIn(FluidTags.WATER);
    }

    @Override
    protected int getInterval(PathAwareEntity mob) {
        return toGoalTicks(20 + mob.getRandom().nextInt(20));
    }

    @Override
    public boolean shouldContinue() {
        return super.shouldContinue() && !finished;
    }

    @Override
    public void tick() {
        super.tick();
        BlockState state = entity.getWorld().getBlockState(entity.getBlockPos());
        if(!finished && state.isOf(AnglingBlocks.ALGAE) && state.getFluidState().isIn(FluidTags.WATER)) {
            AlgaeBlock.deteriorate(entity.getBlockPos(), entity.world);
            finished = true;
        }
    }
}

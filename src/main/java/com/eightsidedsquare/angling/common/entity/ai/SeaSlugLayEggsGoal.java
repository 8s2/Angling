package com.eightsidedsquare.angling.common.entity.ai;

import com.eightsidedsquare.angling.common.entity.SeaSlugEggsBlockEntity;
import com.eightsidedsquare.angling.common.entity.SeaSlugEntity;
import com.eightsidedsquare.angling.core.AnglingBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.goal.MoveToTargetPosGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class SeaSlugLayEggsGoal extends MoveToTargetPosGoal {

    protected final SeaSlugEntity entity;
    protected final World world;

    public SeaSlugLayEggsGoal(SeaSlugEntity entity) {
        super(entity, 1.25d, 6, 6);
        this.entity = entity;
        this.world = entity.world;
    }

    @Override
    public void tick() {
        super.tick();
        entity.getLookControl().lookAt(targetPos.getX() + 0.5d, targetPos.getY() + 0.5d, targetPos.getZ() + 0.5d, entity.getMaxLookYawChange(), entity.getMaxLookPitchChange());

        if(new Vec3d(targetPos.getX() + 0.5d, targetPos.getY() + 1.5d, targetPos.getZ() + 0.5d).distanceTo(entity.getPos()) < 1d) {
            entity.setHasEggs(false);
            world.setBlockState(targetPos.up(), AnglingBlocks.SEA_SLUG_EGGS.getDefaultState().with(Properties.WATERLOGGED, true), Block.NOTIFY_ALL);
            if(world.getBlockEntity(targetPos.up()) instanceof SeaSlugEggsBlockEntity eggsBlockEntity && entity.getMateData() != null){
                eggsBlockEntity.setParentsData(entity.writeMateData(new NbtCompound()), entity.getMateData().copy());
                eggsBlockEntity.setColor(entity.getBaseColor());
            }
        }
    }

    @Override
    protected int getInterval(PathAwareEntity mob) {
        return toGoalTicks(20 + mob.getRandom().nextInt(20));
    }

    @Override
    public double getDesiredDistanceToTarget() {
        return 0d;
    }

    @Override
    public boolean canStart() {
        return super.canStart() && entity.hasEggs() && entity.getMateData() != null;
    }

    @Override
    public boolean shouldContinue() {
        return super.shouldContinue() && entity.hasEggs();
    }

    @Override
    protected boolean isTargetPos(WorldView world, BlockPos pos) {
        BlockPos abovePos = pos.up();
        BlockState aboveState = world.getBlockState(abovePos);
        BlockState state = world.getBlockState(pos);
        return state.isIn(BlockTags.CORAL_BLOCKS) && aboveState.isOf(Blocks.WATER) && aboveState.getFluidState().isOf(Fluids.WATER);
    }
}

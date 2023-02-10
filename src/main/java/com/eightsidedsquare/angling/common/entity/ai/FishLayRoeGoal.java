package com.eightsidedsquare.angling.common.entity.ai;

import com.eightsidedsquare.angling.cca.AnglingEntityComponents;
import com.eightsidedsquare.angling.cca.FishSpawningComponent;
import com.eightsidedsquare.angling.common.block.RoeBlock;
import com.eightsidedsquare.angling.common.entity.RoeBlockEntity;
import com.eightsidedsquare.angling.core.AnglingBlocks;
import com.eightsidedsquare.angling.core.AnglingUtil;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.goal.MoveToTargetPosGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import java.util.EnumSet;

public class FishLayRoeGoal extends MoveToTargetPosGoal {

    protected final FishEntity entity;
    protected final World world;

    public FishLayRoeGoal(WaterCreatureEntity entity) {
        super(entity, 1.25d, 6, 6);
        this.entity = (FishEntity) entity;
        this.world = entity.world;
        this.setControls(EnumSet.of(Control.MOVE));
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
    public void tick() {
        super.tick();
        FishSpawningComponent component = AnglingEntityComponents.FISH_SPAWNING.get(entity);
        entity.getLookControl().lookAt(targetPos.getX() + 0.5d, targetPos.getY() + 0.5d, targetPos.getZ() + 0.5d, entity.getMaxLookYawChange(), entity.getMaxLookPitchChange());

        if(tryingTime % 5 == 0 && new Vec3d(targetPos.getX() + 0.5d, targetPos.getY() + 0.5d, targetPos.getZ() + 0.5d).distanceTo(entity.getPos()) < 1d && noAlgaeNearby(world, targetPos)) {
            component.setCarryingRoe(false);
            world.setBlockState(targetPos.up(), AnglingBlocks.ROE.getDefaultState().with(Properties.WATERLOGGED, true), Block.NOTIFY_ALL);
            if(world.getBlockEntity(targetPos.up()) instanceof RoeBlockEntity roeBlockEntity && component.getMateData() != null){
                roeBlockEntity.setParentsData(AnglingUtil.entityToNbt(entity, true), component.getMateData().copy());
                roeBlockEntity.setEntityType(entity.getType());
                Pair<Integer, Integer> colors = RoeBlock.getRoeColor(entity);
                roeBlockEntity.setColors(colors.getLeft(), colors.getRight());
            }
            stop();
        }else if(targetPos.isWithinDistance(entity.getPos(), 2)) {
            entity.getMoveControl().moveTo(targetPos.getX() + 0.5d, targetPos.getY(), targetPos.getZ() + 0.5d, speed);
        }
    }

    @Override
    public boolean canStart() {
        FishSpawningComponent component = AnglingEntityComponents.FISH_SPAWNING.get(entity);
        return super.canStart() && component.isCarryingRoe() && component.getMateData() != null;
    }

    @Override
    public boolean shouldContinue() {
        return super.shouldContinue() && AnglingEntityComponents.FISH_SPAWNING.get(entity).isCarryingRoe();
    }

    protected boolean noAlgaeNearby(WorldView world, BlockPos pos) {
        int r = 3;
        Iterable<BlockPos> iterable = BlockPos.iterate(pos.add(-r, -r, -r), pos.add(r, r, r));
        for(BlockPos blockPos : iterable) {
            if(world.getBlockState(blockPos).isOf(AnglingBlocks.ALGAE)) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected boolean isTargetPos(WorldView world, BlockPos pos) {
        BlockPos abovePos = pos.up();
        return world.getBlockState(pos).isSideSolidFullSquare(world, pos, Direction.UP)
                && world.getFluidState(abovePos).isOf(Fluids.WATER)
                && world.getBlockState(abovePos).isOf(Blocks.WATER)
                && noAlgaeNearby(world, abovePos);
    }
}

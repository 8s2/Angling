package com.eightsidedsquare.angling.common.entity.ai;

import com.eightsidedsquare.angling.common.entity.PelicanEntity;
import com.eightsidedsquare.angling.core.ai.AnglingMemoryModuleTypes;
import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.WalkTarget;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;

public class PelicanSoarTask extends MultiTickTask<PelicanEntity> {
    public PelicanSoarTask() {
        super(ImmutableMap.of(
                AnglingMemoryModuleTypes.SOARING_COOLDOWN, MemoryModuleState.VALUE_ABSENT,
                MemoryModuleType.WALK_TARGET, MemoryModuleState.VALUE_ABSENT
        ));
    }

    @Override
    protected boolean shouldRun(ServerWorld world, PelicanEntity entity) {
        return entity.isFlying();
    }

    @Override
    protected void run(ServerWorld world, PelicanEntity entity, long time) {
        Vec3d pos = entity.raycast(16, 0, false).getPos();
        int topY = world.getTopY(Heightmap.Type.WORLD_SURFACE, (int) pos.x, (int) pos.z);
        boolean isWater = world.getFluidState(new BlockPos((int) pos.x, topY - 1, (int) pos.z)).isIn(FluidTags.WATER);
        int y = topY + (isWater ? entity.getRandom().nextBetween(3, 8) : entity.getRandom().nextBetween(5, 25));
        BlockPos blockPos = new BlockPos((int) pos.x, y, (int) pos.z);
        if(world.getBlockState(blockPos).isAir()) {
            entity.getBrain().remember(MemoryModuleType.WALK_TARGET, new WalkTarget(blockPos, 1.5f, 1));
        }
    }

}

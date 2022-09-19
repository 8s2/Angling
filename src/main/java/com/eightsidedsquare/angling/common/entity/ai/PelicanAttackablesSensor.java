package com.eightsidedsquare.angling.common.entity.ai;

import com.eightsidedsquare.angling.common.entity.PelicanEntity;
import net.minecraft.entity.Bucketable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.NearestVisibleLivingEntitySensor;
import net.minecraft.entity.mob.MobEntity;

public class PelicanAttackablesSensor extends NearestVisibleLivingEntitySensor {

    @Override
    protected boolean matches(LivingEntity entity, LivingEntity target) {
        return entity instanceof PelicanEntity pelicanEntity &&
                !pelicanEntity.hasEntityInBeak() &&
                !(target instanceof MobEntity mob && (mob.isPersistent() || mob.hasCustomName())) &&
                !(target instanceof Bucketable bucketable && bucketable.isFromBucket()) &&
                target.distanceTo(entity) < 40 &&
                PelicanBrain.canPutInBeak(target) &&
                !entity.getBrain().hasMemoryModule(MemoryModuleType.HAS_HUNTING_COOLDOWN);
    }

    @Override
    protected MemoryModuleType<LivingEntity> getOutputMemoryModule() {
        return MemoryModuleType.NEAREST_ATTACKABLE;
    }
}

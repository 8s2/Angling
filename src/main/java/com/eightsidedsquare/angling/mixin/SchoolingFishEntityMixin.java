package com.eightsidedsquare.angling.mixin;

import com.eightsidedsquare.angling.cca.AnglingEntityComponents;
import com.eightsidedsquare.angling.cca.FishSpawningComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.entity.passive.SchoolingFishEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SchoolingFishEntity.class)
public abstract class SchoolingFishEntityMixin extends FishEntity {

    public SchoolingFishEntityMixin(EntityType<? extends FishEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "hasLeader", at = @At("HEAD"), cancellable = true)
    public void hasLeader(CallbackInfoReturnable<Boolean> cir) {
        FishSpawningComponent component = AnglingEntityComponents.FISH_SPAWNING.get(this);
        if(component.isInLove() || component.isCarryingRoe()) {
            cir.setReturnValue(false);
        }
    }

}

package com.eightsidedsquare.angling.mixin;

import com.eightsidedsquare.angling.cca.AnglingEntityComponents;
import com.eightsidedsquare.angling.cca.FishSpawningComponent;
import net.minecraft.entity.ai.goal.FollowGroupLeaderGoal;
import net.minecraft.entity.passive.SchoolingFishEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FollowGroupLeaderGoal.class)
public abstract class FollowGroupLeaderGoalMixin {

    @Shadow @Final private SchoolingFishEntity fish;

    @Inject(method = "canStart", at = @At("HEAD"), cancellable = true)
    public void canStart(CallbackInfoReturnable<Boolean> cir) {
        FishSpawningComponent component = AnglingEntityComponents.FISH_SPAWNING.get(fish);
        if(component.isInLove() || component.isCarryingRoe()) {
            cir.setReturnValue(false);
        }
    }


    @Inject(method = "shouldContinue", at = @At("HEAD"), cancellable = true)
    public void shouldContinue(CallbackInfoReturnable<Boolean> cir) {
        FishSpawningComponent component = AnglingEntityComponents.FISH_SPAWNING.get(fish);
        if(component.isInLove() || component.isCarryingRoe()) {
            cir.setReturnValue(false);
        }
    }

}

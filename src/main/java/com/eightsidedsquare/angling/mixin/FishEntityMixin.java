package com.eightsidedsquare.angling.mixin;

import com.eightsidedsquare.angling.cca.AnglingEntityComponents;
import com.eightsidedsquare.angling.cca.FishSpawningComponent;
import com.eightsidedsquare.angling.common.entity.ai.FishLayRoeGoal;
import com.eightsidedsquare.angling.common.entity.ai.FishMateGoal;
import com.eightsidedsquare.angling.core.AnglingItems;
import com.eightsidedsquare.angling.core.tags.AnglingEntityTypeTags;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FishEntity.class)
public abstract class FishEntityMixin extends WaterCreatureEntity {

    public FishEntityMixin(EntityType<? extends FishEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tickMovement", at = @At("HEAD"))
    public void tickMovement(CallbackInfo ci) {
        FishSpawningComponent component = AnglingEntityComponents.FISH_SPAWNING.get(this);
        component.tick();
    }

    @Inject(method = "initGoals", at = @At("TAIL"))
    protected void initGoals(CallbackInfo ci) {
        if(getType().isIn(AnglingEntityTypeTags.SPAWNING_FISH)) {
            goalSelector.add(1, new FishLayRoeGoal(this));
            goalSelector.add(3, new FishMateGoal(this));
        }
    }

    @Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
    protected void interactMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack stack = player.getStackInHand(hand);
        FishSpawningComponent component = AnglingEntityComponents.FISH_SPAWNING.get(this);
        if (!component.hasCooldown() && !component.isInLove()
                && stack.isOf(AnglingItems.WORM)
                && getType().isIn(AnglingEntityTypeTags.SPAWNING_FISH)) {
            if(!player.getAbilities().creativeMode)
                stack.decrement(1);
            component.setLoveTicks(600);
            component.setWasFed(true);
            component.createHeartParticles();
            cir.setReturnValue(ActionResult.success(world.isClient));
        }
    }

    @Mixin(FishEntity.SwimToRandomPlaceGoal.class)
    public abstract static class SwimToRandomPlaceGoalMixin {

        @Shadow @Final private FishEntity fish;

        @Inject(method = "canStart", at = @At("HEAD"), cancellable = true)
        public void canStart(CallbackInfoReturnable<Boolean> cir) {
            FishSpawningComponent component = AnglingEntityComponents.FISH_SPAWNING.get(fish);
            if(component.isCarryingRoe())
                cir.setReturnValue(false);
        }
    }

}

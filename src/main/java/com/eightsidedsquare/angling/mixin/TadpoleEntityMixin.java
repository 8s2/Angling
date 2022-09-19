package com.eightsidedsquare.angling.mixin;

import com.eightsidedsquare.angling.cca.AnglingEntityComponents;
import com.eightsidedsquare.angling.cca.FishSpawningComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.entity.passive.TadpoleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TadpoleEntity.class)
public abstract class TadpoleEntityMixin extends FishEntity {

    public TadpoleEntityMixin(EntityType<? extends FishEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "growUp", at = @At("HEAD"), cancellable = true)
    private void growUp(CallbackInfo ci) {
        FishSpawningComponent component = AnglingEntityComponents.FISH_SPAWNING.get(this);
        if(!component.canGrowUp()) {
            ci.cancel();
        }
    }

    @Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
    public void interactMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack stack = player.getStackInHand(hand);
        FishSpawningComponent component = AnglingEntityComponents.FISH_SPAWNING.get(this);
        if(stack.isOf(Items.FERMENTED_SPIDER_EYE) && component.canGrowUp()) {
            if(!player.getAbilities().creativeMode)
                stack.decrement(1);
            component.setCanGrowUp(false);
            cir.setReturnValue(ActionResult.success(world.isClient));
        }
    }

}

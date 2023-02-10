package com.eightsidedsquare.angling.common.entity;

import com.eightsidedsquare.angling.core.AnglingItems;
import com.eightsidedsquare.angling.core.AnglingSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.SchoolingFishEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class BubbleEyeEntity extends SchoolingFishEntity implements IAnimatable {

    AnimationFactory factory = new AnimationFactory(this);

    public BubbleEyeEntity(EntityType<? extends SchoolingFishEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected SoundEvent getFlopSound() {
        return AnglingSounds.ENTITY_BUBBLE_EYE_FLOP;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return AnglingSounds.ENTITY_BUBBLE_EYE_DEATH;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return AnglingSounds.ENTITY_BUBBLE_EYE_HURT;
    }

    @Override
    public ItemStack getBucketItem() {
        return new ItemStack(AnglingItems.BUBBLE_EYE_BUCKET);
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "controller", 2, this::controller));
    }

    private PlayState controller(AnimationEvent<BubbleEyeEntity> event) {
        if(!touchingWater) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.bubble_eye.flop", true));
        }else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.bubble_eye.idle", true));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}

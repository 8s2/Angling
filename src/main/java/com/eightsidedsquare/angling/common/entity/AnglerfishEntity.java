package com.eightsidedsquare.angling.common.entity;

import com.eightsidedsquare.angling.core.AnglingItems;
import com.eightsidedsquare.angling.core.AnglingSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.FishEntity;
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

public class AnglerfishEntity extends FishEntity implements IAnimatable {

    AnimationFactory factory = new AnimationFactory(this);

    public AnglerfishEntity(EntityType<? extends FishEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected SoundEvent getFlopSound() {
        return AnglingSounds.ENTITY_ANGLERFISH_FLOP;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return AnglingSounds.ENTITY_ANGLERFISH_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return AnglingSounds.ENTITY_ANGLERFISH_DEATH;
    }

    @Override
    public ItemStack getBucketItem() {
        return new ItemStack(AnglingItems.ANGLERFISH_BUCKET);
    }
    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "controller", 2, this::controller));
    }

    private PlayState controller(AnimationEvent<AnglerfishEntity> event) {
        if(!touchingWater) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.anglerfish.flop", true));
        }else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.anglerfish.idle", true));
        }
        return PlayState.CONTINUE;
    }
}

package com.eightsidedsquare.angling.common.entity;

import com.eightsidedsquare.angling.core.AnglingItems;
import com.eightsidedsquare.angling.core.AnglingSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
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

public class MahiMahiEntity extends FishEntity implements IAnimatable {

    AnimationFactory factory = new AnimationFactory(this);

    public MahiMahiEntity(EntityType<? extends FishEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected SoundEvent getFlopSound() {
        return AnglingSounds.ENTITY_MAHI_MAHI_FLOP;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return AnglingSounds.ENTITY_MAHI_MAHI_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return AnglingSounds.ENTITY_MAHI_MAHI_DEATH;
    }

    @Override
    public ItemStack getBucketItem() {
        return new ItemStack(AnglingItems.MAHI_MAHI_BUCKET);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 10);
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "controller", 2, this::controller));
    }

    private PlayState controller(AnimationEvent<MahiMahiEntity> event) {
        if(!touchingWater) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.mahi_mahi.flop", true));
        }else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.mahi_mahi.idle", true));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}

package com.eightsidedsquare.angling.common.entity;

import com.eightsidedsquare.angling.common.entity.ai.EatAlgaeGoal;
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

public class CatfishEntity extends FishEntity implements IAnimatable {

    AnimationFactory factory = new AnimationFactory(this);

    public CatfishEntity(EntityType<? extends FishEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(1, new EatAlgaeGoal(this, 1.25d, 12));
    }

    @Override
    protected SoundEvent getFlopSound() {
        return AnglingSounds.ENTITY_CATFISH_FLOP;
    }

    @Nullable @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return AnglingSounds.ENTITY_CATFISH_HURT;
    }

    @Nullable @Override
    protected SoundEvent getDeathSound() {
        return AnglingSounds.ENTITY_CATFISH_DEATH;
    }

    @Override
    public ItemStack getBucketItem() {
        return new ItemStack(AnglingItems.CATFISH_BUCKET);
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "controller", 2, this::controller));
    }

    private PlayState controller(AnimationEvent<CatfishEntity> event) {
        if(!touchingWater) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.catfish.flop", true));
        }else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.catfish.idle", true));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}

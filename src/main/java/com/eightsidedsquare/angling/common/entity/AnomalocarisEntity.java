package com.eightsidedsquare.angling.common.entity;

import com.eightsidedsquare.angling.core.AnglingItems;
import com.eightsidedsquare.angling.core.AnglingSounds;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class AnomalocarisEntity extends FishEntity implements IAnimatable {

    AnimationFactory factory = new AnimationFactory(this);

    public AnomalocarisEntity(EntityType<? extends FishEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected SoundEvent getFlopSound() {
        return AnglingSounds.ENTITY_ANOMALOCARIS_FLOP;
    }

    @Override
    public ItemStack getBucketItem() {
        return new ItemStack(AnglingItems.ANOMALOCARIS_BUCKET);
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return AnglingSounds.ENTITY_ANOMALOCARIS_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return AnglingSounds.ENTITY_ANOMALOCARIS_DEATH;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "controller", 2, this::controller));
    }

    private PlayState controller(AnimationEvent<AnomalocarisEntity> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.anomalocaris.idle", true));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @SuppressWarnings({"deprecation", "unused"})
    public static boolean canSpawn(EntityType<AnomalocarisEntity> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return pos.getY() <= world.getSeaLevel() - 33 && world.getBlockState(pos).isOf(Blocks.WATER);
    }
}

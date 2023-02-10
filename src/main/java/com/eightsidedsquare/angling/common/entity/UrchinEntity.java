package com.eightsidedsquare.angling.common.entity;

import com.eightsidedsquare.angling.common.entity.ai.EatAlgaeGoal;
import com.eightsidedsquare.angling.common.entity.ai.GoToWaterGoal;
import com.eightsidedsquare.angling.common.entity.ai.WanderAroundWaterGoal;
import com.eightsidedsquare.angling.core.AnglingItems;
import com.eightsidedsquare.angling.core.AnglingSounds;
import net.minecraft.entity.Bucketable;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class UrchinEntity extends WaterCreatureEntity implements IAnimatable, Bucketable {

    AnimationFactory factory = new AnimationFactory(this);
    private static final TrackedData<Boolean> FROM_BUCKET = DataTracker.registerData(UrchinEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public UrchinEntity(EntityType<? extends WaterCreatureEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new GoToWaterGoal(this, 2, 6));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.25));
        this.goalSelector.add(3, new EatAlgaeGoal(this, 1, 10));
        this.goalSelector.add(4, new WanderAroundWaterGoal(this, 1));
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 3).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.025d);
    }

    public void travel(Vec3d movementInput) {
        if (this.canMoveVoluntarily() && this.isTouchingWater()) {
            this.updateVelocity(0.01f, movementInput.multiply(10));
            this.move(MovementType.SELF, this.getVelocity());
            this.setVelocity(this.getVelocity().multiply(0.9d));
            if (this.getTarget() == null) {
                this.setVelocity(this.getVelocity().add(0, -0.005d, 0));
            }
        } else {
            super.travel(movementInput);
        }

    }

    @Override
    public void onPlayerCollision(PlayerEntity player) {
        super.onPlayerCollision(player);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        dataTracker.startTracking(FROM_BUCKET, false);
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return AnglingSounds.ENTITY_URCHIN_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return AnglingSounds.ENTITY_URCHIN_DEATH;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "controller", 0, this::controller));
    }

    private PlayState controller(AnimationEvent<UrchinEntity> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.urchin.idle", true));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public boolean isFromBucket() {
        return dataTracker.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean fromBucket) {
        dataTracker.set(FROM_BUCKET, fromBucket);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("FromBucket", isFromBucket());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        setFromBucket(nbt.getBoolean("FromBucket"));
    }

    @Override @SuppressWarnings("deprecation")
    public void copyDataToStack(ItemStack stack) {
        Bucketable.copyDataToStack(this, stack);
    }

    @Override @SuppressWarnings("deprecation")
    public void copyDataFromNbt(NbtCompound nbt) {
        Bucketable.copyDataFromNbt(this, nbt);
    }

    @Override
    public ItemStack getBucketItem() {
        return new ItemStack(AnglingItems.URCHIN_BUCKET);
    }

    @Override
    public SoundEvent getBucketFillSound() {
        return SoundEvents.ITEM_BUCKET_FILL_FISH;
    }
}

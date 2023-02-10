package com.eightsidedsquare.angling.common.entity;

import com.eightsidedsquare.angling.cca.AnglingEntityComponents;
import com.eightsidedsquare.angling.cca.FishSpawningComponent;
import com.eightsidedsquare.angling.core.AnglingItems;
import com.eightsidedsquare.angling.core.AnglingSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.Optional;

public class FryEntity extends FishEntity implements IAnimatable {

    AnimationFactory factory = new AnimationFactory(this);
    private static final TrackedData<Integer> COLOR;
    private static final TrackedData<Integer> AGE;
    private static final TrackedData<NbtCompound> VARIANT;
    private static final TrackedData<String> GROW_UP_TO;

    public FryEntity(EntityType<? extends FishEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.5D).add(EntityAttributes.GENERIC_MAX_HEALTH, 2.0D);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        dataTracker.startTracking(COLOR, 0xffffff);
        dataTracker.startTracking(AGE, -12000);
        dataTracker.startTracking(VARIANT, new NbtCompound());
        dataTracker.startTracking(GROW_UP_TO, "minecraft:cod");
    }

    @Override
    public float getEyeHeight(EntityPose pose) {
        return super.getEyeHeight(pose);
    }

    public void growUp() {
        FishSpawningComponent component = AnglingEntityComponents.FISH_SPAWNING.get(this);
        if(component.canGrowUp()) {
            getGrowUpEntity().ifPresent(entityType -> {
                Entity adult = entityType.create(world);
                if(adult != null) {
                    adult.setPos(getX(), getY(), getZ());
                    if(adult instanceof MobEntity mob) {
                        mob.setPersistent();
                        NbtCompound nbt = mob.writeNbt(new NbtCompound());
                        getVariant().getKeys().forEach(key -> nbt.put(key, getVariant().get(key)));
                        mob.readNbt(nbt);
                        world.spawnEntity(mob);
                    }else {
                        world.spawnEntity(adult);
                    }
                    discard();
                }
            });
        }
    }

    @Override
    public void copyDataToStack(ItemStack stack) {
        super.copyDataToStack(stack);
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putInt("Color", getColor());
        nbt.putInt("Age", getAge());
        nbt.put("Variant", getVariant());
        nbt.putString("GrowUpTo", getGrowUpTo());
    }

    @Override
    public void copyDataFromNbt(NbtCompound nbt) {
        super.copyDataFromNbt(nbt);
        if(nbt.contains("Color")) {
            readCustomDataFromNbt(nbt);
        }
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if(getAge() < 0) {
            setAge(getAge() + 1);
        }
        if(getAge() >= 0) {
            growUp();
        }
    }

    public Optional<EntityType<?>> getGrowUpEntity() {
        return EntityType.get(getGrowUpTo());
    }

    public String getGrowUpTo() {
        return dataTracker.get(GROW_UP_TO);
    }

    public void setGrowUpTo(String type) {
        dataTracker.set(GROW_UP_TO, type);
    }

    public int getColor() {
        return dataTracker.get(COLOR);
    }

    public void setColor(int color) {
        dataTracker.set(COLOR, color);
    }

    public int getAge() {
        return dataTracker.get(AGE);
    }

    public void setAge(int age) {
        dataTracker.set(AGE, age);
    }

    public NbtCompound getVariant() {
        return dataTracker.get(VARIANT);
    }

    public void setVariant(NbtCompound variant) {
        dataTracker.set(VARIANT, variant);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        setColor(nbt.getInt("Color"));
        setAge(nbt.getInt("Age"));
        if(nbt.contains("Variant", NbtElement.COMPOUND_TYPE))
            setVariant(nbt.getCompound("Variant"));
        setGrowUpTo(nbt.getString("GrowUpTo"));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("Color", getColor());
        nbt.putInt("Age", getAge());
        nbt.put("Variant", getVariant());
        nbt.putString("GrowUpTo", getGrowUpTo());
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        FishSpawningComponent component = AnglingEntityComponents.FISH_SPAWNING.get(this);
        if(stack.isOf(Items.FERMENTED_SPIDER_EYE) && component.canGrowUp()) {
            if(!player.getAbilities().creativeMode)
                stack.decrement(1);
            component.setCanGrowUp(false);
            return ActionResult.success(world.isClient);
        }else if(stack.isOf(AnglingItems.WORM) && component.canGrowUp()) {
            if(!player.getAbilities().creativeMode)
                stack.decrement(1);
            component.createGrowUpParticles();
            setAge(getAge() + (int) ((getAge() * -1) * 0.05f));
            return ActionResult.success(world.isClient);
        }
        return super.interactMob(player, hand);
    }

    @Override
    protected SoundEvent getFlopSound() {
        return AnglingSounds.ENTITY_FRY_FLOP;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return AnglingSounds.ENTITY_FRY_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return AnglingSounds.ENTITY_FRY_DEATH;
    }

    @Override
    public boolean shouldDropXp() {
        return false;
    }

    @Override
    public ItemStack getBucketItem() {
        return new ItemStack(AnglingItems.FRY_BUCKET);
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        AnimationController<FryEntity> controller = new AnimationController<>(this, "controller", 2, this::controller);
        animationData.addAnimationController(controller);
    }

    private PlayState controller(AnimationEvent<FryEntity> event) {
        if(!touchingWater) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.fry.flop", true));
        }else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.fry.idle", true));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    static {
        COLOR = DataTracker.registerData(FryEntity.class, TrackedDataHandlerRegistry.INTEGER);
        AGE = DataTracker.registerData(FryEntity.class, TrackedDataHandlerRegistry.INTEGER);
        VARIANT = DataTracker.registerData(FryEntity.class, TrackedDataHandlerRegistry.NBT_COMPOUND);
        GROW_UP_TO = DataTracker.registerData(FryEntity.class, TrackedDataHandlerRegistry.STRING);
    }
}

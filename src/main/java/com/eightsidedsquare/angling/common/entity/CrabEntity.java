package com.eightsidedsquare.angling.common.entity;

import com.eightsidedsquare.angling.common.entity.ai.GoToWaterGoal;
import com.eightsidedsquare.angling.common.entity.util.CrabVariant;
import com.eightsidedsquare.angling.core.AnglingEntities;
import com.eightsidedsquare.angling.core.AnglingItems;
import com.eightsidedsquare.angling.core.AnglingSounds;
import com.eightsidedsquare.angling.core.tags.AnglingBlockTags;
import net.minecraft.entity.Bucketable;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class CrabEntity extends AnimalEntity implements IAnimatable, Bucketable {

    AnimationFactory factory = new AnimationFactory(this);

    private static final TrackedData<CrabVariant> VARIANT = DataTracker.registerData(CrabEntity.class, CrabVariant.TRACKED_DATA_HANDLER);
    private static final TrackedData<Boolean> FROM_BUCKET = DataTracker.registerData(CrabEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public CrabEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
        this.setPathfindingPenalty(PathNodeType.WATER, 0);
    }

    @Override
    public float getScaleFactor() {
        return isBaby() ? 0.35f : 1f;
    }

    public boolean canBreatheInWater() {
        return true;
    }

    public boolean isPushedByFluids() {
        return false;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.25d));
        this.goalSelector.add(2, new GoToWaterGoal(this, 1, 12));
        this.goalSelector.add(3, new AnimalMateGoal(this, 1));
        this.goalSelector.add(4, new TemptGoal(this, 1.2D, Ingredient.ofItems(AnglingItems.WORM), false));
        this.goalSelector.add(5, new FollowParentGoal(this, 1.1d));
        this.goalSelector.add(6, new WanderAroundFarGoal(this, 1));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 6));
        this.goalSelector.add(8, new LookAroundGoal(this));
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        RegistryEntry<Biome> biome = world.getBiome(getBlockPos());
        for(CrabVariant variant : CrabVariant.REGISTRY) {
            if(biome.isIn(variant.biomeTag())) {
                setVariant(variant);
                break;
            }
        }
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        CrabEntity child;
        if((child = AnglingEntities.CRAB.create(world)) != null && entity instanceof CrabEntity mate) {
            child.setVariant((random.nextBoolean() ? this : mate).getVariant());
            child.setPersistent();
            return child;
        }
        return null;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        dataTracker.startTracking(VARIANT, CrabVariant.DUNGENESS);
        dataTracker.startTracking(FROM_BUCKET, false);
    }

    public CrabVariant getVariant() {
        return dataTracker.get(VARIANT);
    }

    public void setVariant(CrabVariant variant) {
        dataTracker.set(VARIANT, variant);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putString("Variant", getVariant().getId().toString());
        nbt.putBoolean("FromBucket", isFromBucket());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        setVariant(CrabVariant.fromId(nbt.getString("Variant")));
        setFromBucket(nbt.getBoolean("FromBucket"));
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "rotation_controller", 6, this::rotationController));
        animationData.addAnimationController(new AnimationController<>(this, "controller", 4, this::controller));
    }

    private PlayState rotationController(AnimationEvent<CrabEntity> event) {
        if(event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.crab.rotated", true));
            return PlayState.CONTINUE;
        }
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.crab.forwards", true));
        return PlayState.CONTINUE;
    }

    private PlayState controller(AnimationEvent<CrabEntity> event) {
        if(event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.crab.moving", true));
            return PlayState.CONTINUE;
        }
        event.getController().clearAnimationCache();
        return PlayState.STOP;
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isOf(AnglingItems.WORM);
    }

    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        return Bucketable.tryBucket(player, hand, this).orElse(super.interactMob(player, hand));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 10).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2d);
    }

    public static boolean canSpawn(EntityType<CrabEntity> entity, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return world.getBlockState(pos.down()).isIn(AnglingBlockTags.CRAB_SPAWNABLE_ON) && isLightLevelValidForNaturalSpawn(world, pos);
    }

    @Override
    public boolean isFromBucket() {
        return dataTracker.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean fromBucket) {
        dataTracker.set(FROM_BUCKET, fromBucket);
    }

    @Override @SuppressWarnings("deprecation")
    public void copyDataToStack(ItemStack stack) {
        Bucketable.copyDataToStack(this, stack);
        writeCustomDataToNbt(stack.getOrCreateNbt());

    }

    @Override @SuppressWarnings("deprecation")
    public void copyDataFromNbt(NbtCompound nbt) {
        Bucketable.copyDataFromNbt(this, nbt);
        if(nbt.contains("Variant", NbtElement.STRING_TYPE)) {
            readCustomDataFromNbt(nbt);
        }
    }

    public boolean cannotDespawn() {
        return super.cannotDespawn() || this.isFromBucket();
    }

    public boolean canImmediatelyDespawn(double distanceSquared) {
        return !this.isFromBucket() && !this.hasCustomName();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return AnglingSounds.ENTITY_CRAB_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return AnglingSounds.ENTITY_CRAB_DEATH;
    }

    @Override
    public ItemStack getBucketItem() {
        return new ItemStack(AnglingItems.CRAB_BUCKET);
    }

    @Override
    public SoundEvent getBucketFillSound() {
        return SoundEvents.ITEM_BUCKET_FILL_FISH;
    }
}

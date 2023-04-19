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
import net.minecraft.entity.MovementType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.AnimalMateGoal;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.entity.ai.goal.FollowParentGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
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
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class CrabEntity extends AnimalEntity implements GeoAnimatable, Bucketable {

    private final RawAnimation rotatedCrabAnimation = RawAnimation.begin().thenLoop("animation.crab.rotated");
    private final RawAnimation crabForwardsAnimation = RawAnimation.begin().thenLoop("animation.crab.forwards");
    private final RawAnimation crabMovingAnimation = RawAnimation.begin().thenLoop("animation.crab.moving");
    AnimatableInstanceCache animatableInstanceCache = GeckoLibUtil.createInstanceCache(this);

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
        this.goalSelector.add(0, new EscapeDangerGoal(this, 1.25d));
        this.goalSelector.add(1, new GoToWaterGoal(this, 1, 12));
        this.goalSelector.add(2, new AnimalMateGoal(this, 1));
        this.goalSelector.add(3, new TemptGoal(this, 1.2D, Ingredient.ofItems(AnglingItems.WORM), false));
        this.goalSelector.add(4, new FollowParentGoal(this, 1.1d));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 6));
        this.goalSelector.add(7, new LookAroundGoal(this));
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

    public void travel(Vec3d movementInput) {
        if (this.canMoveVoluntarily() && this.isTouchingWater()) {
            this.updateVelocity(0.01f, movementInput.multiply(50));
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
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "rotation_controller", 6, this::rotationController));
        controllerRegistrar.add(new AnimationController<>(this, "controller", 4, this::controller));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return animatableInstanceCache;
    }

    @Override
    public double getTick(Object o) {
        return 0;
    }

    private PlayState rotationController(AnimationState<CrabEntity> state) {
        if(state.isMoving()) {
            state.getController().setAnimation(rotatedCrabAnimation);
            return PlayState.CONTINUE;
        }
        state.getController().setAnimation(crabForwardsAnimation);

        return PlayState.CONTINUE;
    }

    private PlayState controller(AnimationState<CrabEntity> state) {
        if(state.isMoving()) {
            state.getController().setAnimation(crabMovingAnimation);
            return PlayState.CONTINUE;
        }
        state.getController().forceAnimationReset();
        return PlayState.STOP;
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isOf(AnglingItems.WORM);
    }

    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        return Bucketable.tryBucket(player, hand, this).orElse(super.interactMob(player, hand));
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
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putString("Variant", getVariant().getId().toString());
        nbt.putBoolean("FromBucket", isFromBucket());

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

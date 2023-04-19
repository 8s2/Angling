package com.eightsidedsquare.angling.common.entity;

import com.eightsidedsquare.angling.common.entity.ai.PelicanBrain;
import com.eightsidedsquare.angling.common.entity.util.PelicanBeakEntityInitializer;
import com.eightsidedsquare.angling.core.AnglingCriteria;
import com.eightsidedsquare.angling.core.AnglingSounds;
import com.eightsidedsquare.angling.core.AnglingUtil;
import com.eightsidedsquare.angling.core.ai.AnglingMemoryModuleTypes;
import com.eightsidedsquare.angling.core.ai.AnglingSensorTypes;
import com.eightsidedsquare.angling.core.tags.AnglingEntityTypeTags;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Bucketable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
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
import net.minecraft.item.EntityBucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Unit;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;

public class PelicanEntity extends AnimalEntity implements GeoAnimatable {

    private final RawAnimation divingAnimation = RawAnimation.begin().thenLoop("animation.pelican.diving");
    private final RawAnimation swimmingAnimation = RawAnimation.begin().thenLoop("animation.pelican.swimming");
    private final RawAnimation flyingAnimation = RawAnimation.begin().thenLoop("animation.pelican.flying");
    private final RawAnimation flappingAnimation = RawAnimation.begin().thenLoop("animation.pelican.flapping");
    private final RawAnimation walkingAnimation = RawAnimation.begin().thenLoop("animation.pelican.walking");
    private final RawAnimation idleAnimation = RawAnimation.begin().thenLoop("animation.pelican.idle");
    private final RawAnimation beakOpened = RawAnimation.begin().thenLoop("animation.pelican.beak_opened");
    AnimatableInstanceCache animatableInstanceCache = GeckoLibUtil.createInstanceCache(this);
    protected static final ImmutableList<SensorType<? extends Sensor<? super PelicanEntity>>> SENSORS;
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_MODULES;

    protected static final TrackedData<Boolean> BEAK_OPEN;
    protected static final TrackedData<Boolean> DIVING;
    protected static final TrackedData<NbtCompound> ENTITY_IN_BEAK;
    protected static final TrackedData<Integer> TIME_OFF_GROUND;

    public PelicanEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new FlightMoveControl(this, 5, false);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if(isBeakOpen() && stack.getItem() instanceof EntityBucketItem bucketItem) {
            bucketItem.playEmptyingSound(player, getWorld(), getBlockPos());
            NbtCompound nbt = stack.getOrCreateNbt().copy();
            nbt.putString("id", Registries.ENTITY_TYPE.getId(bucketItem.entityType).toString());
            if(nbt.contains("BucketVariantTag")) {
                nbt.put("Variant", nbt.get("BucketVariantTag"));
                nbt.remove("BucketVariantTag");
            }
            if(!player.getAbilities().creativeMode)
                player.setStackInHand(hand, new ItemStack(Items.WATER_BUCKET));
            getEntityInBeak().ifPresent(entity -> {
                Vec3d vec3d = getRotationVector(getPitch(), getHeadYaw()).multiply(0.5d).add(getEyePos()).subtract(0, entity.getHeight(), 0);
                entity.setPos(vec3d.x, vec3d.y, vec3d.z);
                entity.setBodyYaw(getHeadYaw());
                entity.setYaw(getHeadYaw());
                if(entity instanceof Bucketable bucketable)
                    bucketable.setFromBucket(true);
                world.spawnEntity(entity);
            });
            setEntityInBeak(nbt);
            setBeakOpen(true);
            getBrain().remember(AnglingMemoryModuleTypes.HAS_TRADED, Unit.INSTANCE);
            getBrain().forget(AnglingMemoryModuleTypes.CAN_TRADE);
            if (player instanceof ServerPlayerEntity serverPlayerEntity)
                AnglingCriteria.TRADED_WITH_PELICAN.trigger(serverPlayerEntity);
            return ActionResult.success(world.isClient);
        }
        return ActionResult.PASS;
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 16.0D)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.1D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.1D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1d);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        dataTracker.startTracking(BEAK_OPEN, false);
        dataTracker.startTracking(DIVING, false);
        dataTracker.startTracking(ENTITY_IN_BEAK, new NbtCompound());
        dataTracker.startTracking(TIME_OFF_GROUND, 0);
    }

    public int getTimeOffGround() {
        return dataTracker.get(TIME_OFF_GROUND);
    }

    public void setTimeOffGround(int timeOffGround) {
        dataTracker.set(TIME_OFF_GROUND, timeOffGround);
    }

    public boolean isBeakOpen() {
        return dataTracker.get(BEAK_OPEN);
    }

    public void setBeakOpen(boolean open) {
        dataTracker.set(BEAK_OPEN, open);
    }

    public boolean isDiving() {
        return dataTracker.get(DIVING);
    }

    public void setDiving(boolean diving) {
        dataTracker.set(DIVING, diving);
    }

    public Optional<Entity> getEntityInBeak() {
        return AnglingUtil.entityFromNbt(getEntityInBeakNbt(), world);
    }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        EntityDimensions entityDimensions = super.getDimensions(pose);
        return this.isFlying() || this.isTouchingWater() ? EntityDimensions.fixed(entityDimensions.width, 0.75F) : entityDimensions;
    }

    @Override
    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return 0.95f * dimensions.height;
    }

    public NbtCompound getEntityInBeakNbt() {
        return dataTracker.get(ENTITY_IN_BEAK);
    }

    public void setEntityInBeak(NbtCompound nbt) {
        dataTracker.set(ENTITY_IN_BEAK, nbt);
    }

    public void setEntityInBeak(Entity entity) {
        setEntityInBeak(AnglingUtil.entityToNbt(entity, true));
    }

    public boolean hasEntityInBeak() {
        return getEntityInBeakNbt().contains("id", NbtElement.STRING_TYPE);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("BeakOpen", isBeakOpen());
        nbt.putBoolean("Diving", isDiving());
        nbt.putInt("TimeOffGround", getTimeOffGround());
        if(getEntityInBeakNbt() != null)
            nbt.put("EntityInBeak", getEntityInBeakNbt());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        setBeakOpen(nbt.getBoolean("BeakOpen"));
        setDiving(nbt.getBoolean("Diving"));
        setTimeOffGround(nbt.getInt("TimeOffGround"));
        if(nbt.contains("EntityInBeak", NbtElement.COMPOUND_TYPE)) {
            setEntityInBeak(nbt.getCompound("EntityInBeak"));
        }
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound nbt) {
        getBrain().remember(AnglingMemoryModuleTypes.SOARING_COOLDOWN, Unit.INSTANCE, 100);
        setEntityInBeak(initializeEntityInBeak());
        setBeakOpen(true);
        return super.initialize(world, difficulty, spawnReason, entityData, nbt);
    }

    private NbtCompound initializeEntityInBeak() {
        NbtCompound nbt = new NbtCompound();
        TagKey<EntityType<?>> tag = random.nextInt(5) == 0 ? AnglingEntityTypeTags.UNCOMMON_ENTITIES_IN_PELICAN_BEAK
                : AnglingEntityTypeTags.COMMON_ENTITIES_IN_PELICAN_BEAK;
        EntityType<?> type = AnglingUtil.getRandomTagValue(world, tag, random);
        nbt.putString("id", Registries.ENTITY_TYPE.getId(type).toString());
        nbt.putBoolean("FromBucket", true);
        if(type.isIn(AnglingEntityTypeTags.HUNTED_BY_PELICAN_WHEN_BABY)) {
            nbt.putInt("Age", -24000);
        }
        return PelicanBeakEntityInitializer.getInitializer(type).initialize(nbt, random, world);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        boolean bl = super.damage(source, amount);
        if(bl) {
            setBeakOpen(false);
        }
        return bl;
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    @Override
    protected void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {
        super.fall(heightDifference, onGround, state, landedPosition);
    }

    protected EntityNavigation createNavigation(World world) {
        BirdNavigation birdNavigation = new BirdNavigation(this, world);
        birdNavigation.setCanPathThroughDoors(false);
        birdNavigation.setCanSwim(true);
        birdNavigation.setCanEnterOpenDoors(true);
        return birdNavigation;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return getBrain().isMemoryInState(AnglingMemoryModuleTypes.CAN_TRADE, MemoryModuleState.VALUE_PRESENT)
                && getBrain().getFirstPossibleNonCoreActivity().orElse(Activity.CORE).equals(Activity.IDLE)
                && getBrain().isMemoryInState(MemoryModuleType.ATTACK_TARGET, MemoryModuleState.VALUE_ABSENT)
                ? AnglingSounds.ENTITY_PELICAN_AMBIENT : null;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return AnglingSounds.ENTITY_PELICAN_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return AnglingSounds.ENTITY_PELICAN_DEATH;
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        setTimeOffGround(onGround ? 0 : getTimeOffGround() + 1);
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        super.onTrackedDataSet(data);
        calculateDimensions();
    }

    public void travel(Vec3d movementInput) {
        if (this.canMoveVoluntarily() || this.isLogicalSideForUpdatingMovement()) {
            if (this.isTouchingWater()) {
                this.updateVelocity(this.getMovementSpeed() * 0.75f, movementInput);
                this.move(MovementType.SELF, this.getVelocity());
                this.setVelocity(this.getVelocity().multiply(0.800000011920929D));
            } else if (this.isInLava()) {
                this.updateVelocity(0.02F, movementInput);
                this.move(MovementType.SELF, this.getVelocity());
                this.setVelocity(this.getVelocity().multiply(0.5D));
            } else {
                this.updateVelocity(this.getMovementSpeed(), movementInput);
                this.move(MovementType.SELF, this.getVelocity());
                this.setVelocity(this.getVelocity().multiply(0.9100000262260437D));
            }
        }

        this.updateLimbs(false);
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }


    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return animatableInstanceCache;
    }

    @Override
    public double getTick(Object o) {
        return 0;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 2, this::controller));
        controllerRegistrar.add(new AnimationController<>(this, "beak_controller", 2, this::beakController));
    }

    public boolean isFlying() {
        return getTimeOffGround() > 5;
    }

    private PlayState controller(AnimationState<PelicanEntity> state) {
        PelicanEntity entity = state.getAnimatable();
        if(entity.isDiving() && entity.isFlying()){
            state.getController().setAnimation(divingAnimation);
        }else if(entity.isTouchingWater()) {
            state.getController().setAnimation(swimmingAnimation);
        }else if(entity.isFlying()) {
            if (Math.abs(entity.getVelocity().y) > 0.05d) {
                state.getController().setAnimation(flyingAnimation);
            } else {
                state.getController().setAnimation(flappingAnimation);
            }
        }else if(state.isMoving()) {
            state.getController().setAnimation(walkingAnimation);
        }else {
            state.getController().setAnimation(idleAnimation);
        }
        return PlayState.CONTINUE;
    }

    private PlayState beakController(AnimationState<PelicanEntity> state) {
        PelicanEntity entity = state.getAnimatable();
        if(entity.isBeakOpen()) {
            state.getController().setAnimation(beakOpened);
            return PlayState.CONTINUE;
        }
        state.getController().forceAnimationReset();
        return PlayState.STOP;
    }

    @Override
    protected Brain.Profile<PelicanEntity> createBrainProfile() {
        return Brain.createProfile(MEMORY_MODULES, SENSORS);
    }

    @Override
    protected Brain<?> deserializeBrain(Dynamic<?> dynamic) {
        return PelicanBrain.create(createBrainProfile().deserialize(dynamic));
    }

    @Override
    public boolean canImmediatelyDespawn(double distanceSquared) {
        return !(hasCustomName() || isPersistent());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Brain<PelicanEntity> getBrain() {
        return (Brain<PelicanEntity>) super.getBrain();
    }

    protected void mobTick() {
        this.world.getProfiler().push("pelicanBrain");
        this.getBrain().tick((ServerWorld)this.world, this);
        this.world.getProfiler().pop();
        this.world.getProfiler().push("pelicanActivityUpdate");
        PelicanBrain.updateActivities(this);
        this.world.getProfiler().pop();
        super.mobTick();
    }

    static {
        SENSORS = ImmutableList.of(
                SensorType.NEAREST_LIVING_ENTITIES,
                SensorType.NEAREST_PLAYERS,
                SensorType.HURT_BY,
                AnglingSensorTypes.PELICAN_ATTACKABLES
        );
        MEMORY_MODULES = ImmutableList.of(
                MemoryModuleType.PATH,
                MemoryModuleType.LOOK_TARGET,
                MemoryModuleType.VISIBLE_MOBS,
                MemoryModuleType.WALK_TARGET,
                MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
                MemoryModuleType.HURT_BY,
                MemoryModuleType.IS_PANICKING,
                MemoryModuleType.HAS_HUNTING_COOLDOWN,
                MemoryModuleType.NEAREST_ATTACKABLE,
                MemoryModuleType.ATTACK_COOLING_DOWN,
                MemoryModuleType.ATTACK_TARGET,
                MemoryModuleType.NEAREST_PLAYERS,
                MemoryModuleType.NEAREST_VISIBLE_PLAYER,
                MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER,
                AnglingMemoryModuleTypes.SOARING_COOLDOWN,
                AnglingMemoryModuleTypes.CAN_TRADE,
                AnglingMemoryModuleTypes.HAS_TRADED
        );
        TIME_OFF_GROUND = DataTracker.registerData(PelicanEntity.class, TrackedDataHandlerRegistry.INTEGER);
        BEAK_OPEN = DataTracker.registerData(PelicanEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
        DIVING = DataTracker.registerData(PelicanEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
        ENTITY_IN_BEAK = DataTracker.registerData(PelicanEntity.class, TrackedDataHandlerRegistry.NBT_COMPOUND);
    }

}

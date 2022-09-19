package com.eightsidedsquare.angling.common.entity;

import com.eightsidedsquare.angling.common.entity.ai.*;
import com.eightsidedsquare.angling.common.entity.util.SeaSlugColor;
import com.eightsidedsquare.angling.common.entity.util.SeaSlugPattern;
import com.eightsidedsquare.angling.core.AnglingBlocks;
import com.eightsidedsquare.angling.core.AnglingItems;
import com.eightsidedsquare.angling.core.AnglingSounds;
import com.eightsidedsquare.angling.core.AnglingUtil;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.pathing.PathNodeType;
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
import net.minecraft.nbt.NbtElement;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
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

public class SeaSlugEntity extends WaterCreatureEntity implements IAnimatable, Bucketable {

    private static final TrackedData<SeaSlugPattern> PATTERN = DataTracker.registerData(SeaSlugEntity.class, SeaSlugPattern.TRACKED_DATA_HANDLER);
    private static final TrackedData<SeaSlugColor> BASE_COLOR = DataTracker.registerData(SeaSlugEntity.class, SeaSlugColor.TRACKED_DATA_HANDLER);
    private static final TrackedData<SeaSlugColor> PATTERN_COLOR = DataTracker.registerData(SeaSlugEntity.class, SeaSlugColor.TRACKED_DATA_HANDLER);
    private static final TrackedData<Boolean> BIOLUMINESCENT = DataTracker.registerData(SeaSlugEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> FROM_BUCKET = DataTracker.registerData(SeaSlugEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> HAS_EGGS = DataTracker.registerData(SeaSlugEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<NbtCompound> MATE_DATA = DataTracker.registerData(SeaSlugEntity.class, TrackedDataHandlerRegistry.NBT_COMPOUND);
    private static final TrackedData<Integer> LOVE_TICKS = DataTracker.registerData(SeaSlugEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> LOVE_COOLDOWN = DataTracker.registerData(SeaSlugEntity.class, TrackedDataHandlerRegistry.INTEGER);

    AnimationFactory factory = new AnimationFactory(this);

    public SeaSlugEntity(EntityType<? extends WaterCreatureEntity> entityType, World world) {
        super(entityType, world);
        setPathfindingPenalty(PathNodeType.WATER, 0);
        getNavigation().setCanSwim(false);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 3).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.05d);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new GoToWaterGoal(this, 2, 6));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.25d));
        this.goalSelector.add(2, new SeaSlugMateGoal(this));
        this.goalSelector.add(3, new SeaSlugLayEggsGoal(this));
        this.goalSelector.add(4, new WanderAroundWaterGoal(this, 1));
        this.goalSelector.add(5, new LookAtEntityGoal(this, PlayerEntity.class, 6));
        this.goalSelector.add(6, new LookAroundGoal(this));
        this.goalSelector.add(7, new EatAlgaeGoal(this, 1.25d, 12));
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

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return AnglingSounds.ENTITY_SEA_SLUG_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return AnglingSounds.ENTITY_SEA_SLUG_DEATH;
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        setPattern(AnglingUtil.getRandomTagValue(getWorld(), SeaSlugPattern.Tag.NATURAL_PATTERNS, random));
        setBaseColor(AnglingUtil.getRandomTagValue(getWorld(), SeaSlugColor.Tag.BASE_COLORS, random));
        setPatternColor(AnglingUtil.getRandomTagValue(getWorld(), SeaSlugColor.Tag.PATTERN_COLORS, random));
        setBioluminescent(random.nextBoolean());
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    public boolean cannotDespawn() {
        return super.cannotDespawn() || this.isFromBucket();
    }

    public boolean canImmediatelyDespawn(double distanceSquared) {
        return !this.isFromBucket() && !this.hasCustomName();
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        dataTracker.startTracking(PATTERN, SeaSlugPattern.NONE);
        dataTracker.startTracking(BASE_COLOR, SeaSlugColor.IVORY);
        dataTracker.startTracking(PATTERN_COLOR, SeaSlugColor.IVORY);
        dataTracker.startTracking(BIOLUMINESCENT, true);
        dataTracker.startTracking(FROM_BUCKET, false);
        dataTracker.startTracking(HAS_EGGS, false);
        dataTracker.startTracking(MATE_DATA, new NbtCompound());
        dataTracker.startTracking(LOVE_TICKS, 0);
        dataTracker.startTracking(LOVE_COOLDOWN, 0);
    }

    public void setBaseColor(SeaSlugColor color) {
        dataTracker.set(BASE_COLOR, color);
    }

    public SeaSlugColor getBaseColor() {
        return dataTracker.get(BASE_COLOR);
    }

    public void setPatternColor(SeaSlugColor color) {
        dataTracker.set(PATTERN_COLOR, color);
    }

    public SeaSlugColor getPatternColor() {
        return dataTracker.get(PATTERN_COLOR);
    }

    public void setPattern(SeaSlugPattern pattern) {
        dataTracker.set(PATTERN, pattern);
    }

    public SeaSlugPattern getPattern() {
        return dataTracker.get(PATTERN);
    }

    public void setBioluminescent(boolean bioluminescent) {
        dataTracker.set(BIOLUMINESCENT, bioluminescent);
    }

    public boolean isBioluminescent() {
        return dataTracker.get(BIOLUMINESCENT);
    }

    public void setHasEggs(boolean hasEggs) {
        dataTracker.set(HAS_EGGS, hasEggs);
    }

    public boolean hasEggs() {
        return dataTracker.get(HAS_EGGS);
    }

    public void setMateData(NbtCompound mateData) {
        dataTracker.set(MATE_DATA, mateData);
    }

    public NbtCompound getMateData() {
        return dataTracker.get(MATE_DATA);
    }

    public void setLoveTicks(int ticks) {
        dataTracker.set(LOVE_TICKS, ticks);
    }

    public int getLoveTicks() {
        return dataTracker.get(LOVE_TICKS);
    }

    public void setLoveCooldown(int cooldown) {
        dataTracker.set(LOVE_COOLDOWN, cooldown);
    }

    public int getLoveCooldown() {
        return dataTracker.get(LOVE_COOLDOWN);
    }

    public boolean isInLove() {
        return getLoveTicks() > 0;
    }

    public boolean hasLoveCooldown() {
        return getLoveCooldown() > 0;
    }

    public NbtCompound writeMateData(NbtCompound nbt) {
        nbt.putString("Pattern", getPattern().getId().toString());
        nbt.putString("BaseColor", getBaseColor().getId().toString());
        nbt.putString("PatternColor", getPatternColor().getId().toString());
        nbt.putBoolean("Bioluminescent", isBioluminescent());
        return nbt;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        writeMateData(nbt);
        nbt.putBoolean("FromBucket", isFromBucket());
        nbt.putBoolean("HasEggs", hasEggs());
        nbt.put("MateData", getMateData());
        nbt.putInt("LoveTicks", getLoveTicks());
        nbt.putInt("LoveCooldown", getLoveCooldown());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        setPattern(SeaSlugPattern.fromId(nbt.getString("Pattern")));
        setBaseColor(SeaSlugColor.fromId(nbt.getString("BaseColor")));
        setPatternColor(SeaSlugColor.fromId(nbt.getString("PatternColor")));
        setBioluminescent(nbt.getBoolean("Bioluminescent"));
        setFromBucket(nbt.getBoolean("FromBucket"));
        setHasEggs(nbt.getBoolean("HasEggs"));
        setLoveTicks(nbt.getInt("LoveTicks"));
        setLoveCooldown(nbt.getInt("LoveCooldown"));
        if(nbt.contains("MateData", NbtElement.COMPOUND_TYPE))
            setMateData(nbt.getCompound("MateData"));
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "ambient_controller", 0, this::ambientController));
        animationData.addAnimationController(new AnimationController<>(this, "controller", 0, this::controller));
    }

    private PlayState ambientController(AnimationEvent<SeaSlugEntity> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.sea_slug.ambient", true));
        return PlayState.CONTINUE;
    }

    private PlayState controller(AnimationEvent<SeaSlugEntity> event) {
        if(new Vec3d(getVelocity().getX(), 0, getVelocity().getZ()).length() > 0.005d) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.sea_slug.moving", true));
            return PlayState.CONTINUE;
        }
        event.getController().clearAnimationCache();
        return PlayState.STOP;
    }

    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if(stack.isOf(AnglingBlocks.ALGAE.asItem()) && !isInLove() && !hasLoveCooldown() && !hasEggs()) {
            setLoveTicks(600);
            createHeartParticles();
            if(!player.getAbilities().creativeMode)
                stack.decrement(1);
            return ActionResult.success(world.isClient);
        }
        return Bucketable.tryBucket(player, hand, this).orElse(super.interactMob(player, hand));
    }

    public void createHeartParticles() {
        if(!world.isClient) {
            ((ServerWorld) world).spawnParticles(ParticleTypes.HEART, getParticleX(1), getRandomBodyY() + 0.5d, getParticleZ(1), 7, 0.25d, 0.25d, 0.25d, 0);
        }
    }

    @Override
    protected void mobTick() {
        if(hasLoveCooldown()) {
            setLoveCooldown(getLoveCooldown() - 1);
        }
        if(isInLove()) {
            setLoveTicks(getLoveTicks() - 1);
        }
        super.mobTick();
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

    @Override @SuppressWarnings("deprecation")
    public void copyDataToStack(ItemStack stack) {
        Bucketable.copyDataToStack(this, stack);
        writeCustomDataToNbt(stack.getOrCreateNbt());

    }

    @Override @SuppressWarnings("deprecation")
    public void copyDataFromNbt(NbtCompound nbt) {
        Bucketable.copyDataFromNbt(this, nbt);
        if(nbt.contains("Pattern", NbtElement.STRING_TYPE)) {
            readCustomDataFromNbt(nbt);
        }
    }

    @Override
    public ItemStack getBucketItem() {
        return new ItemStack(AnglingItems.SEA_SLUG_BUCKET);
    }

    @Override
    public SoundEvent getBucketFillSound() {
        return SoundEvents.ITEM_BUCKET_FILL_FISH;
    }
}

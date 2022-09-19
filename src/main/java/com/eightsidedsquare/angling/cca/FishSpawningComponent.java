package com.eightsidedsquare.angling.cca;

import com.eightsidedsquare.angling.core.AnglingBlocks;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.MultifaceGrowthBlock;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class FishSpawningComponent implements AutoSyncedComponent {

    private final FishEntity entity;
    private int loveTicks;
    private int loveCooldown;
    @Nullable
    private NbtCompound mateData;
    private boolean carryingRoe;
    private boolean canGrowUp;
    private boolean wasFed;

    public FishSpawningComponent(FishEntity entity) {
        this.entity = entity;
        this.canGrowUp = true;
    }

    @Nullable
    public NbtCompound getMateData() {
        return mateData;
    }

    public void setMateData(@Nullable NbtCompound mateData) {
        this.mateData = mateData;
    }

    public boolean isCarryingRoe() {
        return carryingRoe;
    }

    public boolean isInLove() {
        return loveTicks > 0;
    }

    public void setWasFed(boolean wasFed) {
        this.wasFed = wasFed;
    }

    public boolean wasFed() {
        return wasFed;
    }

    public void tick() {
        if(loveCooldown > 0) {
            loveCooldown--;
        }
        if(loveTicks > 0) {
            loveTicks--;
        }
        BlockState state = entity.getBlockStateAtPos();
        if(wasFed() && !entity.getWorld().isClient && entity.getRandom().nextBetween(0, 400) == 0
                && state.isOf(Blocks.WATER)
                && state.getFluidState().isOf(Fluids.WATER)) {
            Util.copyShuffled(Direction.stream(), entity.getRandom()).stream().filter(this::canPlaceAlgaeAt).findFirst().ifPresent(d -> {
                entity.getWorld().setBlockState(entity.getBlockPos(), AnglingBlocks.ALGAE.getDefaultState().with(MultifaceGrowthBlock.getProperty(d), true), Block.NOTIFY_ALL);
                setWasFed(false);
            });
        }
    }

    private boolean canPlaceAlgaeAt(Direction d) {
        BlockPos pos = entity.getBlockPos().offset(d);
        BlockState state = entity.getWorld().getBlockState(pos);
        return MultifaceGrowthBlock.canGrowOn(entity.getWorld(), d, pos, state);
    }

    public boolean canGrowUp() {
        return canGrowUp;
    }

    public void setCanGrowUp(boolean canGrowUp) {
        this.canGrowUp = canGrowUp;
    }

    public boolean hasCooldown() {
        return loveCooldown > 0;
    }

    public void setCarryingRoe(boolean bl) {
        carryingRoe = bl;
    }

    public void setLoveTicks(int ticks) {
        loveTicks = ticks;
    }

    public void setLoveCooldown(int ticks) {
        loveCooldown = ticks;
    }

    public void createHeartParticles() {
        if(!entity.getWorld().isClient) {
            ((ServerWorld) entity.getWorld()).spawnParticles(ParticleTypes.HEART, entity.getParticleX(1), entity.getRandomBodyY() + 0.5d, entity.getParticleZ(1), 7, 0.25d, 0.25d, 0.25d, 0);
        }
    }

    public void createGrowUpParticles() {
        if(!entity.getWorld().isClient) {
            ((ServerWorld) entity.getWorld()).spawnParticles(ParticleTypes.HAPPY_VILLAGER, entity.getParticleX(1), entity.getRandomBodyY() + 0.25d, entity.getParticleZ(1), 1, 0.1d, 0.1d, 0.1d, 0);
        }
    }

    @Override
    public void readFromNbt(@NotNull NbtCompound tag) {
        loveTicks = tag.getInt("LoveTicks");
        loveCooldown = tag.getInt("LoveCooldown");
        carryingRoe = tag.getBoolean("CarryingRoe");
        canGrowUp = tag.getBoolean("CanGrowUp");
        if(tag.contains("MateData", NbtElement.COMPOUND_TYPE)) {
            mateData = tag.getCompound("MateData");
        }
        wasFed = tag.getBoolean("WasFed");
    }

    @Override
    public void writeToNbt(@NotNull NbtCompound tag) {
        tag.putInt("LoveTicks", loveTicks);
        tag.putInt("LoveCooldown", loveCooldown);
        tag.putBoolean("CarryingRoe", carryingRoe);
        tag.putBoolean("CanGrowUp", canGrowUp);
        tag.put("MateData", Objects.requireNonNullElseGet(mateData, NbtCompound::new));
        tag.putBoolean("WasFed", wasFed);
    }
}

package com.eightsidedsquare.angling.common.entity;

import com.eightsidedsquare.angling.core.AnglingEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.InstancedAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class UrchinBlockEntity extends BlockEntity implements GeoAnimatable {

    private ItemStack hat = ItemStack.EMPTY;
    AnimatableInstanceCache animatableInstanceCache = GeckoLibUtil.createInstanceCache(this);

    public UrchinBlockEntity(BlockPos pos, BlockState state) {
        super(AnglingEntities.URCHIN, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.put("Hat", (hat != null ? hat : ItemStack.EMPTY).writeNbt(new NbtCompound()));
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        hat = nbt.contains("Hat", NbtElement.COMPOUND_TYPE) ? ItemStack.fromNbt(nbt.getCompound("Hat")) : ItemStack.EMPTY;
    }

    public ItemStack getHat() {
        return hat;
    }

    public void setHat(ItemStack hat) {
        this.hat = hat;
    }

    public void update() {
        markDirty();
        if(world != null)
            world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_ALL);
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }


    private PlayState controller(AnimationState<UrchinBlockEntity> state) {
        state.getController().setAnimation(RawAnimation.begin().then("animation.urchin.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::controller));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return null;
    }

    @Override
    public double getTick(Object o) {
        return 0;
    }
}

package com.eightsidedsquare.angling.common.entity;

import com.eightsidedsquare.angling.common.block.StarfishBlock;
import com.eightsidedsquare.angling.core.AnglingEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class UrchinBlockEntity extends BlockEntity implements IAnimatable {

    private ItemStack hat = ItemStack.EMPTY;
    AnimationFactory factory = new AnimationFactory(this);

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

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::controller));
    }

    private PlayState controller(AnimationEvent<UrchinBlockEntity> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.urchin.idle", true));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}

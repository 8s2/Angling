package com.eightsidedsquare.angling.common.item;

import com.eightsidedsquare.angling.core.AnglingBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class UrchinBucketItem extends BlockItem {

    public UrchinBucketItem(Settings settings) {
        super(AnglingBlocks.URCHIN, settings);
    }

    @Override
    protected boolean postPlacement(BlockPos pos, World world, @Nullable PlayerEntity player, ItemStack stack, BlockState state) {
        if(player != null && !player.isCreative() && !player.giveItemStack(new ItemStack(Items.BUCKET)))
            player.dropItem(new ItemStack(Items.BUCKET), true);
        world.createAndScheduleFluidTick(pos, world.getFluidState(pos).getFluid(), 1);
        if(world.getDimension().ultrawarm())
            world.setBlockState(pos, state.with(Properties.WATERLOGGED, false));
        return super.postPlacement(pos, world, player, stack, state);
    }

    @Override
    public String getTranslationKey() {
        return getOrCreateTranslationKey();
    }

    @Override
    protected SoundEvent getPlaceSound(BlockState state) {
        return SoundEvents.ITEM_BUCKET_EMPTY;
    }
}

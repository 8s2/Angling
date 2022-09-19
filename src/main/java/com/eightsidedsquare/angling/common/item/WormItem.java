package com.eightsidedsquare.angling.common.item;

import com.eightsidedsquare.angling.core.AnglingBlocks;
import com.eightsidedsquare.angling.core.AnglingSounds;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WormItem extends Item {
    public WormItem(Settings settings) {
        super(settings);
    }

    private ActionResult addWorms(BlockState state, BlockPos pos, ItemStack stack, World world) {
        stack.decrement(1);
        world.setBlockState(pos, state);
        world.playSound(null, pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5d, AnglingSounds.ITEM_WORM_USE, SoundCategory.BLOCKS, 1, 1);
        return ActionResult.success(world.isClient);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext ctx) {
        BlockPos pos = ctx.getBlockPos();
        World world = ctx.getWorld();
        BlockState state = world.getBlockState(pos);
        ItemStack stack = ctx.getStack();
        if(state.isOf(Blocks.DIRT)) {
            return addWorms(AnglingBlocks.WORMY_DIRT.getDefaultState(), pos, stack, world);
        }else if(state.isOf(Blocks.MUD)) {
            return addWorms(AnglingBlocks.WORMY_MUD.getDefaultState(), pos, stack, world);
        }
        return super.useOnBlock(ctx);
    }
}

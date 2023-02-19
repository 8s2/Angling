package com.eightsidedsquare.angling.common.block;

import com.eightsidedsquare.angling.common.entity.UrchinBlockEntity;
import com.eightsidedsquare.angling.core.AnglingItems;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class UrchinBlock extends PlantBlock implements BlockEntityProvider, Waterloggable {

    private static final VoxelShape SHAPE = Block.createCuboidShape(3, 0, 3, 13, 8, 13);
    private static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    public UrchinBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(WATERLOGGED, true));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(hand);
        if(stack.isOf(Items.WATER_BUCKET)) {
            stack.decrement(1);
            player.giveItemStack(new ItemStack(AnglingItems.URCHIN_BUCKET));
            world.playSound(null, pos.getX() + 0.5d, pos.getY(), pos.getZ() + 0.5d, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1, 1);
            world.setBlockState(pos, (state.get(WATERLOGGED) ? Blocks.WATER : Blocks.AIR).getDefaultState(), Block.NOTIFY_ALL);
            return ActionResult.success(world.isClient);
        }else if(world.getBlockEntity(pos) instanceof UrchinBlockEntity entity) {
            if(entity.getHat().isEmpty() && !stack.isEmpty()) {
                ItemStack hatStack = stack.copy();
                hatStack.setCount(1);
                entity.setHat(hatStack);
                if (!player.isCreative())
                    stack.decrement(1);
                player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 1, 1);
                entity.update();
                return ActionResult.success(world.isClient);
            }else if(stack.isEmpty() && !entity.getHat().isEmpty()) {
                player.giveItemStack(entity.getHat().copy());
                entity.setHat(ItemStack.EMPTY);
                player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 1, 1);
                entity.update();
                return ActionResult.success(world.isClient);
            }

        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.createAndScheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        if(!canPlaceAt(state, world, pos)) {
            return Blocks.AIR.getDefaultState();
        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {

        if (!state.isOf(newState.getBlock()) && world.getBlockEntity(pos) instanceof UrchinBlockEntity entity) {
            ItemScatterer.spawn(world, pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5d, entity.getHat().copy());
        }

        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(WATERLOGGED, !ctx.getWorld().getDimension().ultrawarm());
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return !floor.getCollisionShape(world, pos).getFace(Direction.UP).isEmpty() || floor.isSideSolidFullSquare(world, pos, Direction.UP);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return new ItemStack(AnglingItems.URCHIN_BUCKET);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new UrchinBlockEntity(pos, state);
    }
}

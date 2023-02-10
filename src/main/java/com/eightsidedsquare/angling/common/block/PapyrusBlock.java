package com.eightsidedsquare.angling.common.block;

import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class PapyrusBlock extends PlantBlock implements Fertilizable, Waterloggable {

    private static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    private static final IntProperty AGE = Properties.AGE_2;
    private static final VoxelShape AGE_0_SHAPE = Block.createCuboidShape(3, 0, 3, 13, 8, 13);
    private static final VoxelShape AGE_1_SHAPE = Block.createCuboidShape(2, 0, 2, 14, 15, 14);
    private static final VoxelShape AGE_2_SHAPE = Block.createCuboidShape(1, 0, 1, 15, 16, 15);

    public PapyrusBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(AGE, 0).with(WATERLOGGED, false));
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
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Vec3d offset = state.get(AGE) == 0 ? state.getModelOffset(world, pos) : Vec3d.ZERO;
       return (switch (state.get(AGE)) {
           case 0 -> AGE_0_SHAPE;
           case 1 -> AGE_1_SHAPE;
           default -> AGE_2_SHAPE;
       }).offset(offset.x, offset.y, offset.z);
    }

    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        return getDefaultState().with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockState floor = world.getBlockState(pos.down());
        return floor.isIn(BlockTags.DIRT)
                || floor.isOf(Blocks.FARMLAND)
                || floor.isOf(Blocks.GRAVEL)
                || floor.isIn(BlockTags.SAND)
                || floor.isIn(BlockTags.TERRACOTTA);
    }

    @Override
    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        return state.get(AGE) < 2;
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        world.setBlockState(pos, state.with(AGE, state.get(AGE) + 1));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE, WATERLOGGED);
    }

    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }
}

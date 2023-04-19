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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class OystersBlock extends Block implements Waterloggable, FilterFeeder {

    private static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    private static final IntProperty TIMES_FED = IntProperty.of("times_fed", 0, 4);
    private static final VoxelShape SHAPE = Block.createCuboidShape(0, 0, 0, 16, 7, 16);

    public OystersBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(WATERLOGGED, true).with(TIMES_FED, 0));
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        if(!canPlaceAt(state, world, pos)) {
            return Blocks.AIR.getDefaultState();
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        boolean bl = fluidState.getFluid() == Fluids.WATER;
        return getDefaultState().with(WATERLOGGED, bl);
    }

    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockPos belowPos = pos.down();
        BlockState belowState = world.getBlockState(belowPos);
        return Block.isFaceFullSquare(belowState.getSidesShape(world, belowPos), Direction.UP);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, TIMES_FED);
    }

    private void setTimesFed(int timesFed, BlockPos pos, BlockState state, ServerWorld world) {
        world.setBlockState(pos, state.with(TIMES_FED, timesFed), Block.NOTIFY_ALL);
    }

    @Override
    public void onFeed(BlockPos pos, BlockState state, ServerWorld world) {
        int timesFed = state.get(TIMES_FED);
        if(timesFed == 4) {
            Direction.Type.HORIZONTAL.getShuffled(world.getRandom()).stream()
                    .filter(d ->
                            world.getFluidState(pos.offset(d)).isOf(Fluids.WATER) &&
                            world.getBlockState(pos.offset(d)).isOf(Blocks.WATER) &&
                            canPlaceAt(world.getBlockState(pos.offset(d)), world, pos.offset(d)))
                    .findFirst()
                    .ifPresent(d -> {
                        world.setBlockState(pos.offset(d), asBlock().getDefaultState(), Block.NOTIFY_ALL);
                        setTimesFed(0, pos, state, world);
                        createFedParticles(5, pos, world);
                    });
        }else {
            setTimesFed(timesFed + 1, pos, state, world);
            createFedParticles(1, pos, world);
        }
    }
}

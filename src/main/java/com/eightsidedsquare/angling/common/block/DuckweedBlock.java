package com.eightsidedsquare.angling.common.block;

import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

@SuppressWarnings("deprecation")
public class DuckweedBlock extends PlantBlock implements Fertilizable {
    protected static final VoxelShape SHAPE = Block.createCuboidShape(0, 0, 0, 16, 1, 16);

    public DuckweedBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        FluidState fluidState = world.getFluidState(pos);
        FluidState fluidState2 = world.getFluidState(pos.up());
        return fluidState.getFluid() == Fluids.WATER && fluidState2.getFluid() == Fluids.EMPTY;
    }

    @Override
    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        for(Direction d : Direction.Type.HORIZONTAL) {
            BlockPos offsetPos = pos.offset(d).down();
            if(canPlantOnTop(world.getBlockState(offsetPos), world, offsetPos) && world.getBlockState(offsetPos.up()).isAir()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canReplace(BlockState state, ItemPlacementContext context) {
        return true;
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        Direction.Type.HORIZONTAL.getShuffled(random).stream().filter(d -> {
                    BlockPos offsetPos = pos.offset(d).down();
                    return canPlantOnTop(world.getBlockState(offsetPos), world, offsetPos) && world.getBlockState(offsetPos.up()).isAir();
                })
                .findFirst().ifPresent(d -> {
                    BlockPos offsetPos = pos.offset(d);
                    world.setBlockState(offsetPos, asBlock().getDefaultState(), Block.NOTIFY_ALL);
        });
    }
}

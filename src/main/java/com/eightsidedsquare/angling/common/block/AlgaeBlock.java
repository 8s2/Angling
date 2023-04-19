package com.eightsidedsquare.angling.common.block;

import com.eightsidedsquare.angling.cca.AnglingEntityComponents;
import com.eightsidedsquare.angling.core.AnglingBlocks;
import com.eightsidedsquare.angling.core.AnglingParticles;
import com.eightsidedsquare.angling.core.tags.AnglingBlockTags;
import net.minecraft.block.*;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("deprecation")
public class AlgaeBlock extends MultifaceGrowthBlock implements Waterloggable, Fertilizable {

    private static final BooleanProperty WATERLOGGED;
    private final LichenGrower grower;

    public AlgaeBlock(Settings settings) {
        super(settings);
        setDefaultState(super.getDefaultState().with(WATERLOGGED, true));
        grower = new LichenGrower(new GrowChecker(this));
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    public boolean canReplace(BlockState state, ItemPlacementContext context) {
        return !context.getStack().isOf(Items.GLOW_LICHEN) || super.canReplace(state, context);
    }

    public boolean canGrow(BlockView world, BlockPos pos, BlockState state) {
        return state.get(WATERLOGGED) && Direction.stream().anyMatch((direction) -> this.grower.canGrow(state, world, pos, direction.getOpposite()));
    }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state, boolean isClient) {
        return canGrow(world, pos, state);
    }

    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        while(canGrow(world, pos, state))
            this.grower.grow(state, world, pos, random);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = super.getPlacementState(ctx);
        if(state != null) {
            return state.with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid().equals(Fluids.WATER));
        }
        return null;
    }

    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
        return state.getFluidState().isEmpty();
    }

    @Override
    public LichenGrower getGrower() {
        return grower;
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(WATERLOGGED);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if(canGrow(world, pos, state)) {
            world.getOtherEntities(null,
                    new Box(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1).expand(5),
                    entity -> entity instanceof FishEntity fish && AnglingEntityComponents.FISH_SPAWNING.get(fish).wasFed()
            ).stream().findFirst().ifPresent(entity -> {
                AnglingEntityComponents.FISH_SPAWNING.get(entity).setWasFed(false);
                grow(world, random, pos, state);
            });
        }
        if(state.get(WATERLOGGED)) {
            int attempts = random.nextBetween(10, 30);
            int range = 6;
            for (int i = 0; i < attempts; i++) {
                BlockPos testPos = new BlockPos(
                        pos.getX() + (int) (random.nextGaussian() * range),
                        pos.getY() + (int) (random.nextGaussian() * range),
                        pos.getZ() + (int) (random.nextGaussian() * range)
                );
                if (world.getBlockState(testPos).isIn(AnglingBlockTags.FILTER_FEEDERS) && world.getFluidState(testPos).isIn(FluidTags.WATER)) {
                    if (world.getBlockState(testPos).getBlock() instanceof FilterFeeder filterFeeder) {
                        filterFeeder.onFeed(testPos, world.getBlockState(testPos), world);
                    }
                    deteriorate(pos, world);
                }
            }
        }
    }

    public static void deteriorate(BlockPos pos, World world) {
        BlockState state = world.getBlockState(pos);
        if(state.isOf(AnglingBlocks.ALGAE)) {
            List<Direction> faces = Util.copyShuffled(MultifaceGrowthBlock.collectDirections(state).stream(), world.random);
            if (!faces.isEmpty())
                faces.remove(0);
            if (!faces.isEmpty()) {
                BlockState newState = state.getBlock().getDefaultState().with(WATERLOGGED, state.get(WATERLOGGED));
                for (Direction d : faces) {
                    newState = newState.with(MultifaceGrowthBlock.getProperty(d), true);
                }
                world.setBlockState(pos, newState);
            } else {
                world.setBlockState(pos, (state.get(WATERLOGGED) ? Blocks.WATER : Blocks.AIR).getDefaultState());
            }
        }
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        super.randomDisplayTick(state, world, pos, random);
        if(state.get(WATERLOGGED) && random.nextBetween(0, 5) == 0) {
            double x = random.nextGaussian() + pos.getX();
            double y = random.nextGaussian() + pos.getY();
            double z = random.nextGaussian() + pos.getZ();
            if(world.getBlockState(BlockPos.ofFloored(x, y, z)).getFluidState().isIn(FluidTags.WATER)) {
                double velocityX = random.nextGaussian() * 0.01d;
                double velocityY = random.nextGaussian() * 0.01d;
                double velocityZ = random.nextGaussian() * 0.01d;
                world.addParticle(AnglingParticles.ALGAE, x, y, z, velocityX, velocityY, velocityZ);
            }
        }
    }

    static {
        WATERLOGGED = Properties.WATERLOGGED;
    }

    static class GrowChecker extends LichenGrower.LichenGrowChecker {

        public GrowChecker(MultifaceGrowthBlock lichen) {
            super(lichen);
        }

        @Override
        protected boolean canGrow(BlockView world, BlockPos pos, BlockPos growPos, Direction direction, BlockState state) {
            return state.isOf(this.lichen) || state.isOf(Blocks.WATER) && state.getFluidState().isStill();
        }
    }
}

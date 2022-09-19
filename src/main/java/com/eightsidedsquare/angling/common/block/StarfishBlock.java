package com.eightsidedsquare.angling.common.block;

import com.eightsidedsquare.angling.common.entity.StarfishBlockEntity;
import com.eightsidedsquare.angling.core.AnglingBlocks;
import com.eightsidedsquare.angling.core.tags.AnglingBlockTags;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class StarfishBlock extends FacingBlock implements BlockEntityProvider, Waterloggable {

    private final boolean dead;

    private static final ImmutableList<Integer> COLORS = ImmutableList.of(
            0xe05a30,
            0xe0b330,
            0x83e030,
            0xe03030,
            0xe03053,
            0x3059e0,
            0xcaceed,
            0x413854
    );
    private static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    private static final VoxelShape DOWN_SHAPE = Block.createCuboidShape(3, 14, 3, 13, 16, 13);
    private static final VoxelShape UP_SHAPE = Block.createCuboidShape(3, 0, 3, 13, 2, 13);
    private static final VoxelShape EAST_SHAPE = Block.createCuboidShape(0, 3, 3, 2, 13, 13);
    private static final VoxelShape WEST_SHAPE = Block.createCuboidShape(14, 3, 3, 16, 13, 13);
    private static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(3, 3, 0, 13, 13, 2);
    private static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(3, 3, 14, 13, 13, 16);

    public StarfishBlock(Settings settings, boolean dead) {
        super(settings);
        this.dead = dead;
        setDefaultState(getDefaultState().with(FACING, Direction.NORTH).with(WATERLOGGED, true));
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if(!dead && state.get(WATERLOGGED)) {
            Direction.shuffle(random).stream()
                    .filter(d -> world.getBlockState(pos.offset(d)).isIn(AnglingBlockTags.STARFISH_FOOD) && world.getFluidState(pos).isIn(FluidTags.WATER))
                    .findFirst()
                    .ifPresent(d -> {
                        BlockPos childPos = pos.offset(d);
                        world.breakBlock(childPos, false);
                        createChild(childPos, pos, world, random);
                    });
        }
    }

    private void createChild(BlockPos childPos, BlockPos pos, ServerWorld world, Random random) {
        Direction.shuffle(random).stream()
                .filter(direction -> canPlaceAt(getDefaultState().with(FACING, direction.getOpposite()), world, childPos))
                .findFirst().ifPresent(direction -> {
                    world.setBlockState(childPos, asBlock().getDefaultState().with(FACING, direction.getOpposite()), Block.NOTIFY_ALL);
                    if(random.nextFloat() < 0.1f) {
                        randomize(world, childPos, random);
                    }else if(world.getBlockEntity(pos) instanceof StarfishBlockEntity entity &&
                            world.getBlockEntity(childPos) instanceof StarfishBlockEntity childEntity) {
                        childEntity.setColor(entity.getColor());
                        childEntity.setRainbow(entity.isRainbow());
                    }
                    world.spawnParticles(ParticleTypes.HAPPY_VILLAGER, pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5d, 5, 0.25d, 0.25d, 0.25d, 0);
                });
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        NbtCompound nbt = BlockItem.getBlockEntityNbt(stack);
        if(!dead && world.getBlockEntity(pos) instanceof StarfishBlockEntity entity){
            if (nbt != null) {
                entity.setColor(nbt.getInt("Color"));
            } else {
                randomize(world, pos, world.getRandom());
            }
        }
    }

    public static void randomize(WorldAccess world, BlockPos pos, Random random) {
        if(world.getBlockEntity(pos) instanceof StarfishBlockEntity entity) {
            entity.setRandomRotation(random.nextDouble() * 360 - 180);
            if(random.nextFloat() < 0.001) {
                entity.setColor(0xffffff);
                entity.setRainbow(true);
            }else {
                entity.setColor(getRandomColor(random));
            }
        }
    }

    private static int getRandomColor(Random random) {
        return Util.getRandom(COLORS, random);
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        randomize(world, pos, world.getRandom());
        if(shouldDie(state, world, pos)) {
            world.createAndScheduleBlockTick(pos, this, 60 + world.getRandom().nextInt(40));
        }
        super.onBlockAdded(state, world, pos, oldState, notify);
    }

    public boolean isDead() {
        return dead;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    private boolean shouldDie(BlockState state, WorldAccess world, BlockPos pos) {
        return !dead && !state.getFluidState().isIn(FluidTags.WATER)
                && Direction.stream().noneMatch(d -> world.getFluidState(pos.offset(d)).isIn(FluidTags.WATER));
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.createAndScheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        if(!canPlaceAt(state, world, pos)) {
            return Blocks.AIR.getDefaultState();
        }
        if(shouldDie(state, world, pos)) {
            world.createAndScheduleBlockTick(pos, this, 60 + world.getRandom().nextInt(40));
        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if(!dead && !state.getFluidState().isIn(FluidTags.WATER)) {
            double rotation = world.random.nextDouble() * 360 - 180;
            if(world.getBlockEntity(pos) instanceof StarfishBlockEntity entity) {
                rotation = entity.getRandomRotation();
            }
            world.setBlockState(pos, AnglingBlocks.DEAD_STARFISH.getStateWithProperties(state));
            if(world.getBlockEntity(pos) instanceof StarfishBlockEntity entity) {
                entity.setRandomRotation(rotation);
            }
        }
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        Direction d = state.get(FACING).getOpposite();
        BlockPos facingPos = pos.offset(d);
        BlockState facingState = world.getBlockState(facingPos);
        return Block.isFaceFullSquare(facingState.getSidesShape(world, facingPos), d.getOpposite());
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            case UP -> UP_SHAPE;
            case DOWN -> DOWN_SHAPE;
            case NORTH -> NORTH_SHAPE;
            case EAST -> EAST_SHAPE;
            case WEST -> WEST_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
        };
    }

    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        boolean bl = fluidState.getFluid() == Fluids.WATER;
        return getDefaultState().with(WATERLOGGED, bl).with(FACING, ctx.getSide());
    }

    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new StarfishBlockEntity(pos, state);
    }
}

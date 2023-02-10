package com.eightsidedsquare.angling.common.block;

import com.eightsidedsquare.angling.cca.AnglingEntityComponents;
import com.eightsidedsquare.angling.cca.FishSpawningComponent;
import com.eightsidedsquare.angling.common.entity.RoeBlockEntity;
import com.eightsidedsquare.angling.core.AnglingEntities;
import com.eightsidedsquare.angling.core.AnglingUtil;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.entity.passive.TropicalFishEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class RoeBlock extends BlockWithEntity implements Waterloggable {

    private static final BooleanProperty WATERLOGGED;
    private static final VoxelShape SHAPE;

    public RoeBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(WATERLOGGED, false));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return AnglingUtil.runningSodium() ? BlockRenderType.INVISIBLE : super.getRenderType(state);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockState belowState = world.getBlockState(pos.down());
        return Block.isFaceFullSquare(belowState.getSidesShape(world, pos.down()), Direction.UP);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.empty();
    }

    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        boolean bl = fluidState.getFluid() == Fluids.WATER;
        return getDefaultState().with(WATERLOGGED, bl);
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

    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
    }

    public int getHatchTime(Random random) {
        return random.nextBetween(3600, 7200);
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if(state.get(WATERLOGGED)) {
            world.createAndScheduleBlockTick(pos, this, getHatchTime(world.getRandom()));
        }
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if(world.isClient) {
            world.getBlockEntity(pos, AnglingEntities.ROE).ifPresent(entity -> entity.readFrom(itemStack));
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if(state.get(WATERLOGGED)) {
            world.getBlockEntity(pos, AnglingEntities.ROE).ifPresent(entity -> entity.hatch(world));
        }
    }

    public static Pair<Integer, Integer> getRoeColor(FishEntity entity) {
        SpawnEggItem eggItem = SpawnEggItem.forEntity(entity.getType());
        FishSpawningComponent component = AnglingEntityComponents.FISH_SPAWNING.get(entity);
        if(entity instanceof TropicalFishEntity tropicalFishEntity) {
            int parentColor = TropicalFishEntity.getBaseDyeColor(tropicalFishEntity.getVariant()).getSignColor();
            return new Pair<>(parentColor,
                    component.getMateData() != null ? TropicalFishEntity.getBaseDyeColor(component.getMateData().getInt("Variant")).getSignColor() : parentColor);
        }
        if(eggItem != null) {
            return new Pair<>(eggItem.getColor(0), eggItem.getColor(0));
        }
        return new Pair<>(0xffffff, 0xffffff);
    }

    static {
        WATERLOGGED = Properties.WATERLOGGED;
        SHAPE = Block.createCuboidShape(0, 0, 0, 16, 0.75, 16);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new RoeBlockEntity(pos, state);
    }
}

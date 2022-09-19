package com.eightsidedsquare.angling.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.MudBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

@SuppressWarnings("deprecation")
public class WormyMudBlock extends MudBlock implements WormyBlock {
    public WormyMudBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(WORMS, 1));
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return state.get(WORMS) < 3;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        tickWorms(state, world, pos, random);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        spawnWormParticles(world, pos, random);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        appendWormProperties(builder);
    }

    @Override
    public BlockState getDefaultBlockState() {
        return Blocks.MUD.getDefaultState();
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return addOrRemoveWorms(state, world, pos, player, hand);
    }
}

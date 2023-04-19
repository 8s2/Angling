package com.eightsidedsquare.angling.common.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;

public class NoisePatchFeature extends Feature<NoisePatchFeatureConfig> {

    public NoisePatchFeature(Codec<NoisePatchFeatureConfig> configCodec) {
        super(configCodec);
    }


    @Override
    @SuppressWarnings("deprecation")
    public boolean generate(FeatureContext<NoisePatchFeatureConfig> ctx) {
        NoisePatchFeatureConfig config = ctx.getConfig();
        BlockPos pos = ctx.getOrigin();
        StructureWorldAccess world = ctx.getWorld();
        Random random = ctx.getRandom();
        DoublePerlinNoiseSampler sampler = DoublePerlinNoiseSampler.create(random, config.offset, config.octave);
        BlockStateProvider blockStateProvider = config.blockStateProvider;

        int radius = config.radius.get(random);
        double threshold = config.threshold;

        for(int x = -radius; x <= radius; x++) {
            for(int z = -radius; z <= radius; z++) {
                if(x * x + z * z >= radius * radius)
                    continue;
                double value = sampler.sample(pos.getX() + x, pos.getY(), pos.getZ() + z);
                BlockPos blockPos = pos.add(x, 0, z);
                if(value > threshold) {

                    BlockState state = blockStateProvider.get(random, blockPos);
                    if(state.canPlaceAt(world, blockPos))
                        world.setBlockState(blockPos, state, Block.NOTIFY_LISTENERS);
                }
            }
        }

        return true;
    }
}

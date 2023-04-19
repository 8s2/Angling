package com.eightsidedsquare.angling.core.world;

import com.eightsidedsquare.angling.common.feature.NoisePatchFeatureConfig;
import com.eightsidedsquare.angling.core.AnglingBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.state.property.Properties;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.feature.RandomPatchFeatureConfig;
import net.minecraft.world.gen.feature.SimpleBlockFeatureConfig;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;

public class AnglingConfiguredFeatures {

    public static final RegistryKey<ConfiguredFeature<?, ?>> PATCH_DUCKWEED = ConfiguredFeatures.of("patch_duckweed");
    public static final RegistryKey<ConfiguredFeature<?, ?>> PATCH_SARGASSUM = ConfiguredFeatures.of("patch_sargassum");
    public static final RegistryKey<ConfiguredFeature<?, ?>> OYSTER_REEF = ConfiguredFeatures.of("oyster_reef");
    public static final RegistryKey<ConfiguredFeature<?, ?>> CLAMS = ConfiguredFeatures.of("clams");
    public static final RegistryKey<ConfiguredFeature<?, ?>> WORMY_BLOCK = ConfiguredFeatures.of("wormy_block");
    public static final RegistryKey<ConfiguredFeature<?, ?>> PATCH_PAPYRUS = ConfiguredFeatures.of("patch_papyrus");
    private static final WeightedBlockStateProvider PAPYRUS_BLOCK_STATE_PROVIDER = new WeightedBlockStateProvider(
            DataPool.<BlockState>builder()
                    .add(AnglingBlocks.PAPYRUS.getDefaultState().with(Properties.AGE_2, 0), 1)
                    .add(AnglingBlocks.PAPYRUS.getDefaultState().with(Properties.AGE_2, 1), 2)
                    .add(AnglingBlocks.PAPYRUS.getDefaultState().with(Properties.AGE_2, 2), 3).build()
    );

    public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> featureRegisterable) {
        ConfiguredFeatures.register(featureRegisterable, PATCH_DUCKWEED, AnglingFeatures.NOISE_PATCH, new NoisePatchFeatureConfig(BlockStateProvider.of(AnglingBlocks.SARGASSUM), -3, 2d, 0.25d, UniformIntProvider.create(8, 16)));
        ConfiguredFeatures.register(featureRegisterable, PATCH_SARGASSUM, AnglingFeatures.NOISE_PATCH, new NoisePatchFeatureConfig(BlockStateProvider.of(AnglingBlocks.SARGASSUM), -3, 2d, 0.25d, UniformIntProvider.create(8, 16)));
        ConfiguredFeatures.register(featureRegisterable, OYSTER_REEF, AnglingFeatures.WATERLOGGABLE_PATCH, new SimpleBlockFeatureConfig(BlockStateProvider.of(AnglingBlocks.OYSTERS)));
        ConfiguredFeatures.register(featureRegisterable, CLAMS, AnglingFeatures.WATERLOGGABLE_PATCH, new SimpleBlockFeatureConfig(new WeightedBlockStateProvider(DataPool.<BlockState>builder()
                .add(AnglingBlocks.CLAM.getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH), 1)
                .add(AnglingBlocks.CLAM.getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.EAST), 1)
                .add(AnglingBlocks.CLAM.getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.SOUTH), 1)
                .add(AnglingBlocks.CLAM.getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.WEST), 1)
                .build())));
        ConfiguredFeatures.register(featureRegisterable, WORMY_BLOCK, AnglingFeatures.WORMY_BLOCK, new DefaultFeatureConfig());
        ConfiguredFeatures.register(featureRegisterable, PATCH_PAPYRUS, AnglingFeatures.WATER_ADJACENT_PATCH, new RandomPatchFeatureConfig(
                64,
                6,
                2,
                PlacedFeatures.createEntry(Feature.SIMPLE_BLOCK, new SimpleBlockFeatureConfig(PAPYRUS_BLOCK_STATE_PROVIDER))
        ));
    }
}

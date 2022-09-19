package com.eightsidedsquare.angling.core.world;

import com.eightsidedsquare.angling.core.AnglingBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;

import static com.eightsidedsquare.angling.core.AnglingMod.MOD_ID;

public class  AnglingConfiguredFeatures {

    public static final RegistryEntry<ConfiguredFeature<RandomPatchFeatureConfig, ?>> PATCH_DUCKWEED =
            register("patch_duckweed", Feature.RANDOM_PATCH,
                    new RandomPatchFeatureConfig(200, 4, 3,
                            PlacedFeatures.createEntry(Feature.SIMPLE_BLOCK,
                                    new SimpleBlockFeatureConfig(BlockStateProvider.of(AnglingBlocks.DUCKWEED)))));

    public static final RegistryEntry<ConfiguredFeature<SimpleBlockFeatureConfig, ?>> OYSTER_REEF =
            register("oyster_reef", AnglingFeatures.WATERLOGGABLE_PATCH,
                    new SimpleBlockFeatureConfig(BlockStateProvider.of(AnglingBlocks.OYSTERS)));

    public static final RegistryEntry<ConfiguredFeature<SimpleBlockFeatureConfig, ?>> CLAMS =
            register("clams", AnglingFeatures.WATERLOGGABLE_PATCH,
                    new SimpleBlockFeatureConfig(
                            new WeightedBlockStateProvider(DataPool.<BlockState>builder()
                                    .add(AnglingBlocks.CLAM.getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH), 1)
                                    .add(AnglingBlocks.CLAM.getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.EAST), 1)
                                    .add(AnglingBlocks.CLAM.getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.SOUTH), 1)
                                    .add(AnglingBlocks.CLAM.getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.WEST), 1)
                                    .build())));

    public static final RegistryEntry<ConfiguredFeature<DefaultFeatureConfig, ?>> WORMY_BLOCK =
            register("wormy_block", AnglingFeatures.WORMY_BLOCK, new DefaultFeatureConfig());

    public static <FC extends FeatureConfig, F extends Feature<FC>> RegistryEntry<ConfiguredFeature<FC, ?>> register(String id, F feature, FC config) {
        return ConfiguredFeatures.register(MOD_ID + ":" + id, feature, config);
    }
}

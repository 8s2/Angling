package com.eightsidedsquare.angling.core.world;

import com.eightsidedsquare.angling.core.tags.AnglingBiomeTags;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.minecraft.tag.BiomeTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.placementmodifier.*;

import java.util.List;

import static com.eightsidedsquare.angling.core.AnglingMod.MOD_ID;
import static net.minecraft.world.gen.feature.VegetationPlacedFeatures.modifiers;

public class AnglingPlacedFeatures {
    public static final RegistryEntry<PlacedFeature> PATCH_DUCKWEED = register("patch_duckweed",
            AnglingConfiguredFeatures.PATCH_DUCKWEED, modifiers(2));

    public static final RegistryEntry<PlacedFeature> OYSTER_REEF = register("oyster_reef",
                    AnglingConfiguredFeatures.OYSTER_REEF,
            List.of(RarityFilterPlacementModifier.of(14),
                    SquarePlacementModifier.of(),
                    PlacedFeatures.OCEAN_FLOOR_HEIGHTMAP,
                    BiomePlacementModifier.of()));

    public static final RegistryEntry<PlacedFeature> CLAMS = register("clams",
            AnglingConfiguredFeatures.CLAMS,
            List.of(RarityFilterPlacementModifier.of(12),
                    SquarePlacementModifier.of(),
                    PlacedFeatures.OCEAN_FLOOR_HEIGHTMAP,
                    BiomePlacementModifier.of()));

    public static final RegistryEntry<PlacedFeature> WORMY_BLOCK = register("wormy_block",
            AnglingConfiguredFeatures.WORMY_BLOCK,
            List.of(CountPlacementModifier.of(2),
                    SquarePlacementModifier.of(),
                    PlacedFeatures.OCEAN_FLOOR_HEIGHTMAP,
                    BiomePlacementModifier.of()));

    public static RegistryEntry<PlacedFeature> register(String id, RegistryEntry<? extends ConfiguredFeature<?, ?>> registryEntry, List<PlacementModifier> modifiers) {
        return PlacedFeatures.register(MOD_ID + ":" + id, registryEntry, modifiers);
    }

    private static void addFeature(RegistryEntry<PlacedFeature> featureEntry, GenerationStep.Feature step, TagKey<Biome> tag) {
        featureEntry.getKey().ifPresent(key ->
                BiomeModifications.addFeature(ctx -> ctx.getBiomeRegistryEntry().isIn(tag), step, key));
    }

    public static void init() {
        addFeature(OYSTER_REEF, GenerationStep.Feature.VEGETAL_DECORATION, AnglingBiomeTags.OYSTER_REEF_BIOMES);
        addFeature(CLAMS, GenerationStep.Feature.VEGETAL_DECORATION, AnglingBiomeTags.CLAMS_BIOMES);
        addFeature(PATCH_DUCKWEED, GenerationStep.Feature.VEGETAL_DECORATION, AnglingBiomeTags.DUCKWEED_BIOMES);
        addFeature(WORMY_BLOCK, GenerationStep.Feature.UNDERGROUND_ORES, BiomeTags.IS_OVERWORLD);
    }
}

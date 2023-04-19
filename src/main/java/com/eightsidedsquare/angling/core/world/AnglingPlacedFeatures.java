package com.eightsidedsquare.angling.core.world;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.placementmodifier.BiomePlacementModifier;
import net.minecraft.world.gen.placementmodifier.RarityFilterPlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;

public class AnglingPlacedFeatures {
    public static final RegistryKey<PlacedFeature> PATCH_DUCKWEED = PlacedFeatures.of("patch_duckweed");
    public static final RegistryKey<PlacedFeature> PATCH_SARGASSUM = PlacedFeatures.of("patch_sargassum");
    public static final RegistryKey<PlacedFeature> OYSTER_REEF = PlacedFeatures.of("oyster_reef");
    public static final RegistryKey<PlacedFeature> CLAMS = PlacedFeatures.of("clams");
    public static final RegistryKey<PlacedFeature> WORMY_BLOCK = PlacedFeatures.of("wormy_block");
    public static final RegistryKey<PlacedFeature> PATCH_PAPYRUS = PlacedFeatures.of("patch_papyrus");

    public static void bootstrap(Registerable<PlacedFeature> featureRegisterable) {
        RegistryEntryLookup<ConfiguredFeature<?, ?>> registryEntryLookup = featureRegisterable.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);
        RegistryEntry.Reference<ConfiguredFeature<?, ?>> patchDuckweedEntry = registryEntryLookup.getOrThrow(AnglingConfiguredFeatures.PATCH_DUCKWEED);
        RegistryEntry.Reference<ConfiguredFeature<?, ?>> patchSargassumEntry = registryEntryLookup.getOrThrow(AnglingConfiguredFeatures.PATCH_SARGASSUM);
        RegistryEntry.Reference<ConfiguredFeature<?, ?>> oysterReefEntry = registryEntryLookup.getOrThrow(AnglingConfiguredFeatures.OYSTER_REEF);
        RegistryEntry.Reference<ConfiguredFeature<?, ?>> clamsEntry = registryEntryLookup.getOrThrow(AnglingConfiguredFeatures.CLAMS);
        RegistryEntry.Reference<ConfiguredFeature<?, ?>> wormyBlockEntry = registryEntryLookup.getOrThrow(AnglingConfiguredFeatures.WORMY_BLOCK);
        RegistryEntry.Reference<ConfiguredFeature<?, ?>> patchPapyrusEntry = registryEntryLookup.getOrThrow(AnglingConfiguredFeatures.PATCH_PAPYRUS);

        PlacedFeatures.register(featureRegisterable, PATCH_DUCKWEED, patchDuckweedEntry,
                RarityFilterPlacementModifier.of(3),
                SquarePlacementModifier.of(),
                PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP,
                BiomePlacementModifier.of()
        );

        PlacedFeatures.register(featureRegisterable, PATCH_SARGASSUM, patchSargassumEntry,
                RarityFilterPlacementModifier.of(70),
                SquarePlacementModifier.of(),
                PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP,
                BiomePlacementModifier.of()
        );

        PlacedFeatures.register(featureRegisterable, OYSTER_REEF, oysterReefEntry,
                RarityFilterPlacementModifier.of(14),
                SquarePlacementModifier.of(),
                PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP,
                BiomePlacementModifier.of()
        );

        PlacedFeatures.register(featureRegisterable, CLAMS, clamsEntry,
                RarityFilterPlacementModifier.of(12),
                SquarePlacementModifier.of(),
                PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP,
                BiomePlacementModifier.of()
        );

        PlacedFeatures.register(featureRegisterable, WORMY_BLOCK, wormyBlockEntry,
                RarityFilterPlacementModifier.of(2),
                SquarePlacementModifier.of(),
                PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP,
                BiomePlacementModifier.of()
        );

        PlacedFeatures.register(featureRegisterable, PATCH_PAPYRUS, patchPapyrusEntry,
                RarityFilterPlacementModifier.of(2),
                SquarePlacementModifier.of(),
                PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP,
                BiomePlacementModifier.of()
        );
    }
}

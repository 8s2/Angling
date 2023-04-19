package com.eightsidedsquare.angling.core.tags;

import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

public class AnglingBiomeTags {

    public static final TagKey<Biome> SUNFISH_SPAWN_IN = of("sunfish_spawn_in");
    public static final TagKey<Biome> NAUTILUS_SPAWN_IN = of("nautilus_spawn_in");
    public static final TagKey<Biome> SEA_SLUG_SPAWN_IN = of("sea_slug_spawn_in");
    public static final TagKey<Biome> CATFISH_SPAWN_IN = of("catfish_spawn_in");
    public static final TagKey<Biome> SEAHORSE_SPAWN_IN = of("seahorse_spawn_in");
    public static final TagKey<Biome> BUBBLE_EYE_SPAWN_IN = of("bubble_eye_spawn_in");
    public static final TagKey<Biome> ANOMALOCARIS_SPAWN_IN = of("anomalocaris_spawn_in");
    public static final TagKey<Biome> ANGLERFISH_SPAWN_IN = of("anglerfish_spawn_in");
    public static final TagKey<Biome> MAHI_MAHI_SPAWN_IN = of("mahi_mahi_spawn_in");
    public static final TagKey<Biome> CRAB_SPAWN_IN = of("crab_spawn_in");
    public static final TagKey<Biome> DUNGENESS_CRAB_BIOMES = of("dungeness_crab_biomes");
    public static final TagKey<Biome> GHOST_CRAB_BIOMES = of("ghost_crab_biomes");
    public static final TagKey<Biome> BLUE_CLAW_CRAB_BIOMES = of("blue_claw_crab_biomes");
    public static final TagKey<Biome> OYSTER_REEF_BIOMES = of("oyster_reef_biomes");
    public static final TagKey<Biome> CLAMS_BIOMES = of("clams_biomes");
    public static final TagKey<Biome> DUCKWEED_BIOMES = of("duckweed_biomes");
    public static final TagKey<Biome> SARGASSUM_BIOMES = of("sargassum_biomes");
    public static final TagKey<Biome> PAPYRUS_BIOMES = of("papyrus_biomes");

    private static TagKey<Biome> of(String id) {
        return TagKey.of(RegistryKeys.BIOME, new Identifier(id));
    }
}

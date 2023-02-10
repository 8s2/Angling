package com.eightsidedsquare.angling.core.tags;

import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

import static com.eightsidedsquare.angling.core.AnglingMod.MOD_ID;

public class AnglingBiomeTags {

    public static final TagKey<Biome> SUNFISH_SPAWN_IN = create("sunfish_spawn_in");
    public static final TagKey<Biome> NAUTILUS_SPAWN_IN = create("nautilus_spawn_in");
    public static final TagKey<Biome> SEA_SLUG_SPAWN_IN = create("sea_slug_spawn_in");
    public static final TagKey<Biome> CATFISH_SPAWN_IN = create("catfish_spawn_in");
    public static final TagKey<Biome> SEAHORSE_SPAWN_IN = create("seahorse_spawn_in");
    public static final TagKey<Biome> BUBBLE_EYE_SPAWN_IN = create("bubble_eye_spawn_in");
    public static final TagKey<Biome> ANOMALOCARIS_SPAWN_IN = create("anomalocaris_spawn_in");
    public static final TagKey<Biome> ANGLERFISH_SPAWN_IN = create("anglerfish_spawn_in");
    public static final TagKey<Biome> MAHI_MAHI_SPAWN_IN = create("mahi_mahi_spawn_in");
    public static final TagKey<Biome> CRAB_SPAWN_IN = create("crab_spawn_in");
    public static final TagKey<Biome> DUNGENESS_CRAB_BIOMES = create("dungeness_crab_biomes");
    public static final TagKey<Biome> GHOST_CRAB_BIOMES = create("ghost_crab_biomes");
    public static final TagKey<Biome> BLUE_CLAW_CRAB_BIOMES = create("blue_claw_crab_biomes");
    public static final TagKey<Biome> OYSTER_REEF_BIOMES = create("oyster_reef_biomes");
    public static final TagKey<Biome> CLAMS_BIOMES = create("clams_biomes");
    public static final TagKey<Biome> DUCKWEED_BIOMES = create("duckweed_biomes");
    public static final TagKey<Biome> SARGASSUM_BIOMES = create("sargassum_biomes");
    public static final TagKey<Biome> PAPYRUS_BIOMES = create("papyrus_biomes");

    private static TagKey<Biome> create(String id) {
        return TagKey.of(Registry.BIOME_KEY, new Identifier(MOD_ID, id));
    }
}

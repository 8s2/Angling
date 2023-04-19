package com.eightsidedsquare.angling.core;

import com.eightsidedsquare.angling.common.entity.*;
import com.eightsidedsquare.angling.core.tags.AnglingBiomeTags;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.Heightmap;

import static com.eightsidedsquare.angling.core.AnglingMod.MOD_ID;

public class AnglingEntities {

    public static final BlockEntityType<RoeBlockEntity> ROE = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier(MOD_ID, "roe"),
            FabricBlockEntityTypeBuilder
                    .create(RoeBlockEntity::new)
                    .addBlock(AnglingBlocks.ROE)
                    .build()
    );

    public static final BlockEntityType<StarfishBlockEntity> STARFISH = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier(MOD_ID, "starfish"),
            FabricBlockEntityTypeBuilder
                    .create(StarfishBlockEntity::new)
                    .addBlocks(AnglingBlocks.STARFISH, AnglingBlocks.DEAD_STARFISH)
                    .build()
    );

    public static final BlockEntityType<SeaSlugEggsBlockEntity> SEA_SLUG_EGGS = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier(MOD_ID, "sea_slug_eggs"),
            FabricBlockEntityTypeBuilder
                    .create(SeaSlugEggsBlockEntity::new)
                    .addBlock(AnglingBlocks.SEA_SLUG_EGGS)
                    .build()
    );

    public static final BlockEntityType<AnemoneBlockEntity> ANEMONE = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier(MOD_ID, "anemone"),
            FabricBlockEntityTypeBuilder
                    .create(AnemoneBlockEntity::new)
                    .addBlock(AnglingBlocks.ANEMONE)
                    .build()
    );

    public static final BlockEntityType<UrchinBlockEntity> URCHIN = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier(MOD_ID, "urchin"),
            FabricBlockEntityTypeBuilder
                    .create(UrchinBlockEntity::new)
                    .addBlock(AnglingBlocks.URCHIN)
                    .build()
    );

    public static final EntityType<FryEntity> FRY = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(MOD_ID, "fry"),
            FabricEntityTypeBuilder.createMob()
                    .entityFactory(FryEntity::new)
                    .defaultAttributes(FryEntity::createAttributes)
                    .dimensions(EntityDimensions.fixed(0.2f, 0.15f))
                    .spawnGroup(SpawnGroup.WATER_AMBIENT)
                    .spawnRestriction(SpawnRestriction.Location.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, WaterCreatureEntity::canSpawn)
                    .build()
    );

    public static final EntityType<SunfishEntity> SUNFISH = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(MOD_ID, "sunfish"),
            FabricEntityTypeBuilder.createMob()
                    .entityFactory(SunfishEntity::new)
                    .defaultAttributes(FishEntity::createFishAttributes)
                    .dimensions(EntityDimensions.fixed(0.5f, 0.3f))
                    .spawnGroup(SpawnGroup.WATER_AMBIENT)
                    .spawnRestriction(SpawnRestriction.Location.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, WaterCreatureEntity::canSpawn)
                    .build()
    );

    public static final EntityType<PelicanEntity> PELICAN = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(MOD_ID, "pelican"),
            FabricEntityTypeBuilder.createMob()
                    .entityFactory(PelicanEntity::new)
                    .defaultAttributes(PelicanEntity::createAttributes)
                    .dimensions(EntityDimensions.changing(0.7f, 1.65f))
                    .spawnGroup(SpawnGroup.AMBIENT)
                    .spawnRestriction(SpawnRestriction.Location.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canMobSpawn)
                    .build()
    );

    public static final EntityType<NautilusEntity> NAUTILUS = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(MOD_ID, "nautilus"),
            FabricEntityTypeBuilder.createMob()
                    .entityFactory(NautilusEntity::new)
                    .defaultAttributes(FishEntity::createFishAttributes)
                    .dimensions(EntityDimensions.fixed(0.5f, 0.4f))
                    .spawnGroup(SpawnGroup.WATER_AMBIENT)
                    .spawnRestriction(SpawnRestriction.Location.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, NautilusEntity::canSpawn)
                    .build()
    );

    public static final EntityType<SeaSlugEntity> SEA_SLUG = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(MOD_ID, "sea_slug"),
            FabricEntityTypeBuilder.createMob()
                    .entityFactory(SeaSlugEntity::new)
                    .defaultAttributes(SeaSlugEntity::createAttributes)
                    .dimensions(EntityDimensions.fixed(0.5f, 0.3f))
                    .spawnGroup(SpawnGroup.WATER_AMBIENT)
                    .spawnRestriction(SpawnRestriction.Location.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, WaterCreatureEntity::canSpawn)
                    .build()
    );

    public static final EntityType<CrabEntity> CRAB = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(MOD_ID, "crab"),
            FabricEntityTypeBuilder.createMob()
                    .entityFactory(CrabEntity::new)
                    .defaultAttributes(CrabEntity::createAttributes)
                    .dimensions(EntityDimensions.changing(0.7f, 0.4f))
                    .spawnGroup(SpawnGroup.CREATURE)
                    .spawnRestriction(SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, CrabEntity::canSpawn)
                    .build()
    );

    public static final EntityType<DongfishEntity> DONGFISH = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(MOD_ID, "dongfish"),
            FabricEntityTypeBuilder.createMob()
                    .entityFactory(DongfishEntity::new)
                    .defaultAttributes(FishEntity::createFishAttributes)
                    .dimensions(EntityDimensions.fixed(0.4f, 0.3f))
                    .spawnGroup(SpawnGroup.WATER_AMBIENT)
                    .spawnRestriction(SpawnRestriction.Location.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, WaterCreatureEntity::canSpawn)
                    .build()
    );

    public static final EntityType<CatfishEntity> CATFISH = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(MOD_ID, "catfish"),
            FabricEntityTypeBuilder.createMob()
                    .entityFactory(CatfishEntity::new)
                    .defaultAttributes(FishEntity::createFishAttributes)
                    .dimensions(EntityDimensions.fixed(0.6f, 0.4f))
                    .spawnGroup(SpawnGroup.WATER_AMBIENT)
                    .spawnRestriction(SpawnRestriction.Location.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, WaterCreatureEntity::canSpawn)
                    .build()
    );

    public static final EntityType<SeahorseEntity> SEAHORSE = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(MOD_ID, "seahorse"),
            FabricEntityTypeBuilder.createMob()
                    .entityFactory(SeahorseEntity::new)
                    .defaultAttributes(FishEntity::createFishAttributes)
                    .dimensions(EntityDimensions.fixed(0.35f, 0.6f))
                    .spawnGroup(SpawnGroup.WATER_AMBIENT)
                    .spawnRestriction(SpawnRestriction.Location.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, WaterCreatureEntity::canSpawn)
                    .build()
    );

    public static final EntityType<BubbleEyeEntity> BUBBLE_EYE = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(MOD_ID, "bubble_eye"),
            FabricEntityTypeBuilder.createMob()
                    .entityFactory(BubbleEyeEntity::new)
                    .defaultAttributes(FishEntity::createFishAttributes)
                    .dimensions(EntityDimensions.fixed(0.4f, 0.3f))
                    .spawnGroup(SpawnGroup.WATER_AMBIENT)
                    .spawnRestriction(SpawnRestriction.Location.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, WaterCreatureEntity::canSpawn)
                    .build()
    );

    public static final EntityType<AnomalocarisEntity> ANOMALOCARIS = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(MOD_ID, "anomalocaris"),
            FabricEntityTypeBuilder.createMob()
                    .entityFactory(AnomalocarisEntity::new)
                    .defaultAttributes(FishEntity::createFishAttributes)
                    .dimensions(EntityDimensions.fixed(0.8f, 0.3f))
                    .spawnGroup(SpawnGroup.UNDERGROUND_WATER_CREATURE)
                    .spawnRestriction(SpawnRestriction.Location.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnomalocarisEntity::canSpawn)
                    .build()
    );

    public static final EntityType<AnglerfishEntity> ANGLERFISH = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(MOD_ID, "anglerfish"),
            FabricEntityTypeBuilder.createMob()
                    .entityFactory(AnglerfishEntity::new)
                    .defaultAttributes(FishEntity::createFishAttributes)
                    .dimensions(EntityDimensions.fixed(0.8f, 0.5f))
                    .spawnGroup(SpawnGroup.WATER_AMBIENT)
                    .spawnRestriction(SpawnRestriction.Location.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, NautilusEntity::canSpawn)
                    .build()
    );

    public static final EntityType<MahiMahiEntity> MAHI_MAHI = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(MOD_ID, "mahi_mahi"),
            FabricEntityTypeBuilder.createMob()
                    .entityFactory(MahiMahiEntity::new)
                    .defaultAttributes(MahiMahiEntity::createAttributes)
                    .dimensions(EntityDimensions.fixed(1f, 0.8f))
                    .spawnGroup(SpawnGroup.WATER_AMBIENT)
                    .spawnRestriction(SpawnRestriction.Location.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, FishEntity::canSpawn)
                    .build()
    );

    public static void init() {
        BiomeModifications.addSpawn(
                biome -> biome.getBiomeRegistryEntry().isIn(AnglingBiomeTags.SUNFISH_SPAWN_IN),
                SpawnGroup.WATER_AMBIENT, SUNFISH, 5, 2, 5
        );
        BiomeModifications.addSpawn(
                biome -> biome.getBiomeRegistryEntry().isIn(AnglingBiomeTags.SEA_SLUG_SPAWN_IN),
                SpawnGroup.WATER_AMBIENT, SEA_SLUG, 4, 1, 3
        );
        BiomeModifications.addSpawn(
                biome -> biome.getBiomeRegistryEntry().isIn(AnglingBiomeTags.NAUTILUS_SPAWN_IN),
                SpawnGroup.WATER_AMBIENT, NAUTILUS, 4, 1, 3
        );
        BiomeModifications.addSpawn(
                biome -> biome.getBiomeRegistryEntry().isIn(AnglingBiomeTags.CATFISH_SPAWN_IN),
                SpawnGroup.WATER_AMBIENT, CATFISH, 1, 1, 2
        );
        BiomeModifications.addSpawn(
                biome -> biome.getBiomeRegistryEntry().isIn(AnglingBiomeTags.CRAB_SPAWN_IN),
                SpawnGroup.CREATURE, CRAB, 8, 3, 5
        );
        BiomeModifications.addSpawn(
                biome -> biome.getBiomeRegistryEntry().isIn(AnglingBiomeTags.SEAHORSE_SPAWN_IN),
                SpawnGroup.WATER_AMBIENT, SEAHORSE, 4, 3, 8
        );
        BiomeModifications.addSpawn(
                biome -> biome.getBiomeRegistryEntry().isIn(AnglingBiomeTags.BUBBLE_EYE_SPAWN_IN),
                SpawnGroup.WATER_AMBIENT, BUBBLE_EYE, 4, 2, 3
        );
        BiomeModifications.addSpawn(
                biome -> biome.getBiomeRegistryEntry().isIn(AnglingBiomeTags.ANOMALOCARIS_SPAWN_IN),
                SpawnGroup.UNDERGROUND_WATER_CREATURE, ANOMALOCARIS, 20, 1, 2
        );
        BiomeModifications.addSpawn(
                biome -> biome.getBiomeRegistryEntry().isIn(AnglingBiomeTags.ANGLERFISH_SPAWN_IN),
                SpawnGroup.WATER_AMBIENT, ANGLERFISH, 6, 1, 2
        );
        BiomeModifications.addSpawn(
                biome -> biome.getBiomeRegistryEntry().isIn(AnglingBiomeTags.MAHI_MAHI_SPAWN_IN),
                SpawnGroup.WATER_AMBIENT, MAHI_MAHI, 4, 1, 2
        );
    }

}

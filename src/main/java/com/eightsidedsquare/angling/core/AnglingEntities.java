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
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;

import static com.eightsidedsquare.angling.core.AnglingMod.MOD_ID;

public class AnglingEntities {

    public static final BlockEntityType<RoeBlockEntity> ROE = Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            new Identifier(MOD_ID, "roe"),
            FabricBlockEntityTypeBuilder
                    .create(RoeBlockEntity::new)
                    .addBlock(AnglingBlocks.ROE)
                    .build()
    );

    public static final BlockEntityType<StarfishBlockEntity> STARFISH = Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            new Identifier(MOD_ID, "starfish"),
            FabricBlockEntityTypeBuilder
                    .create(StarfishBlockEntity::new)
                    .addBlocks(AnglingBlocks.STARFISH, AnglingBlocks.DEAD_STARFISH)
                    .build()
    );

    public static final BlockEntityType<SeaSlugEggsBlockEntity> SEA_SLUG_EGGS = Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            new Identifier(MOD_ID, "sea_slug_eggs"),
            FabricBlockEntityTypeBuilder
                    .create(SeaSlugEggsBlockEntity::new)
                    .addBlock(AnglingBlocks.SEA_SLUG_EGGS)
                    .build()
    );

    public static final EntityType<FryEntity> FRY = Registry.register(
            Registry.ENTITY_TYPE,
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
            Registry.ENTITY_TYPE,
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
            Registry.ENTITY_TYPE,
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
            Registry.ENTITY_TYPE,
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
            Registry.ENTITY_TYPE,
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
            Registry.ENTITY_TYPE,
            new Identifier(MOD_ID, "crab"),
            FabricEntityTypeBuilder.createMob()
                    .entityFactory(CrabEntity::new)
                    .defaultAttributes(CrabEntity::createAttributes)
                    .dimensions(EntityDimensions.fixed(0.8f, 0.4f))
                    .spawnGroup(SpawnGroup.WATER_CREATURE)
                    .spawnRestriction(SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, CrabEntity::canSpawn)
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
                SpawnGroup.WATER_AMBIENT, NAUTILUS, 12, 1, 3
        );
        BiomeModifications.addSpawn(
                biome -> biome.getBiomeRegistryEntry().isIn(AnglingBiomeTags.CRAB_SPAWN_IN),
                SpawnGroup.WATER_CREATURE, CRAB, 5, 1, 3
        );
    }

}

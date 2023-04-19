package com.eightsidedsquare.angling.core.tags;

import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import static com.eightsidedsquare.angling.core.AnglingMod.MOD_ID;

public class AnglingEntityTypeTags {

    public static final TagKey<EntityType<?>> SPAWNING_FISH = of("spawning_fish");
    public static final TagKey<EntityType<?>> COMMON_ENTITIES_IN_PELICAN_BEAK = of("common_entities_in_pelican_beak");
    public static final TagKey<EntityType<?>> UNCOMMON_ENTITIES_IN_PELICAN_BEAK = of("uncommon_entities_in_pelican_beak");
    public static final TagKey<EntityType<?>> HUNTED_BY_PELICAN = of("hunted_by_pelican");
    public static final TagKey<EntityType<?>> HUNTED_BY_PELICAN_WHEN_BABY = of("hunted_by_pelican_when_baby");

    private static TagKey<EntityType<?>> of(String id) {
        return TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(MOD_ID, id));
    }
}

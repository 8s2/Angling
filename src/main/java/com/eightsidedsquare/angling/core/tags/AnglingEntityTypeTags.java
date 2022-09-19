package com.eightsidedsquare.angling.core.tags;

import net.minecraft.entity.EntityType;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static com.eightsidedsquare.angling.core.AnglingMod.MOD_ID;

public class AnglingEntityTypeTags {

    public static final TagKey<EntityType<?>> SPAWNING_FISH = create("spawning_fish");
    public static final TagKey<EntityType<?>> COMMON_ENTITIES_IN_PELICAN_BEAK = create("common_entities_in_pelican_beak");
    public static final TagKey<EntityType<?>> UNCOMMON_ENTITIES_IN_PELICAN_BEAK = create("uncommon_entities_in_pelican_beak");
    public static final TagKey<EntityType<?>> HUNTED_BY_PELICAN = create("hunted_by_pelican");
    public static final TagKey<EntityType<?>> HUNTED_BY_PELICAN_WHEN_BABY = create("hunted_by_pelican_when_baby");

    private static TagKey<EntityType<?>> create(String id) {
        return TagKey.of(Registry.ENTITY_TYPE_KEY, new Identifier(MOD_ID, id));
    }
}

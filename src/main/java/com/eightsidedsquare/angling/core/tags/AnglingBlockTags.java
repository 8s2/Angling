package com.eightsidedsquare.angling.core.tags;

import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import static com.eightsidedsquare.angling.core.AnglingMod.MOD_ID;

public class AnglingBlockTags {

    public static final TagKey<Block> FILTER_FEEDERS = of("filter_feeders");
    public static final TagKey<Block> STARFISH_FOOD = of("starfish_food");
    public static final TagKey<Block> CRAB_SPAWNABLE_ON = of("crab_spawnable_on");

    private static TagKey<Block> of(String id) {
        return TagKey.of(RegistryKeys.BLOCK, new Identifier(MOD_ID, id));
    }

}

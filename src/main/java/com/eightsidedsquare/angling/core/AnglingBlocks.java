package com.eightsidedsquare.angling.core;

import com.eightsidedsquare.angling.common.block.*;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.eightsidedsquare.angling.core.AnglingMod.MOD_ID;

public class AnglingBlocks {
    private static final Map<Block, Identifier> BLOCKS = new LinkedHashMap<>();
    private static final Map<Item, Identifier> ITEMS = new LinkedHashMap<>();

    public static final Block ROE = create("roe", new RoeBlock(AbstractBlock.Settings.of(Material.FROGSPAWN, MapColor.ORANGE).noCollision().nonOpaque().breakInstantly().sounds(BlockSoundGroup.FROGSPAWN)), null);
    public static final Block SEA_SLUG_EGGS = create("sea_slug_eggs", new SeaSlugEggsBlock(AbstractBlock.Settings.copy(ROE).offsetType(AbstractBlock.OffsetType.XZ).dynamicBounds()), null);
    public static final Block DUCKWEED = create("duckweed", new DuckweedBlock(AbstractBlock.Settings.of(Material.REPLACEABLE_PLANT, MapColor.LIME).breakInstantly().nonOpaque().noCollision().sounds(BlockSoundGroup.WET_GRASS)), null);
    public static final Block ALGAE = create("algae", new AlgaeBlock(AbstractBlock.Settings.of(Material.REPLACEABLE_PLANT, MapColor.GREEN).sounds(BlockSoundGroup.FROGSPAWN).noCollision().nonOpaque().strength(0.1f).ticksRandomly()), ItemGroup.DECORATIONS);
    public static final Block WORMY_DIRT = create("wormy_dirt", new WormyDirtBlock(AbstractBlock.Settings.copy(Blocks.DIRT)), ItemGroup.BUILDING_BLOCKS);
    public static final Block WORMY_MUD = create("wormy_mud", new WormyMudBlock(AbstractBlock.Settings.copy(Blocks.MUD)), ItemGroup.BUILDING_BLOCKS);
    public static final Block OYSTERS = create("oysters", new OystersBlock(AbstractBlock.Settings.of(Material.SOLID_ORGANIC, MapColor.OAK_TAN).strength(0.5f).nonOpaque().sounds(AnglingSounds.SHELL_SOUND_GROUP)), ItemGroup.DECORATIONS);
    public static final Block STARFISH = create("starfish", new StarfishBlock(AbstractBlock.Settings.of(Material.UNDERWATER_PLANT, MapColor.WHITE).strength(0.1f).nonOpaque().noCollision().sounds(AnglingSounds.SHELL_SOUND_GROUP).ticksRandomly(), false), ItemGroup.DECORATIONS);
    public static final Block DEAD_STARFISH = create("dead_starfish", new StarfishBlock(AbstractBlock.Settings.copy(STARFISH), true), ItemGroup.DECORATIONS);
    public static final Block CLAM = create("clam", new ClamBlock(AbstractBlock.Settings.of(Material.UNDERWATER_PLANT, MapColor.WHITE).sounds(AnglingSounds.SHELL_SOUND_GROUP).offsetType(AbstractBlock.OffsetType.XZ).strength(0.05f).nonOpaque()), ItemGroup.DECORATIONS);

    private static <T extends Block> T create(String name, T block, ItemGroup itemGroup) {

        BLOCKS.put(block, new Identifier(MOD_ID, name));
        if (itemGroup != null) {
            ITEMS.put(new BlockItem(block, new Item.Settings().group(itemGroup)), BLOCKS.get(block));
        }
        return block;
    }

    public static void init() {
        BLOCKS.keySet().forEach(block -> Registry.register(Registry.BLOCK, BLOCKS.get(block), block));
        ITEMS.keySet().forEach(item -> Registry.register(Registry.ITEM, ITEMS.get(item), item));
    }
}

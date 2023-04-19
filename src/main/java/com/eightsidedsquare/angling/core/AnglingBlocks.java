package com.eightsidedsquare.angling.core;

import com.eightsidedsquare.angling.common.block.AlgaeBlock;
import com.eightsidedsquare.angling.common.block.AnemoneBlock;
import com.eightsidedsquare.angling.common.block.ClamBlock;
import com.eightsidedsquare.angling.common.block.OystersBlock;
import com.eightsidedsquare.angling.common.block.PapyrusBlock;
import com.eightsidedsquare.angling.common.block.RoeBlock;
import com.eightsidedsquare.angling.common.block.SeaSlugEggsBlock;
import com.eightsidedsquare.angling.common.block.StarfishBlock;
import com.eightsidedsquare.angling.common.block.UrchinBlock;
import com.eightsidedsquare.angling.common.block.WaterFloatingPlant;
import com.eightsidedsquare.angling.common.block.WormyDirtBlock;
import com.eightsidedsquare.angling.common.block.WormyMudBlock;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.eightsidedsquare.angling.core.AnglingMod.MOD_ID;

public class AnglingBlocks {
    private static final Map<Block, Identifier> BLOCKS = new LinkedHashMap<>();
    private static final Map<Item, Identifier> ITEMS = new LinkedHashMap<>();

    public static final Block ROE = create("roe", new RoeBlock(AbstractBlock.Settings.of(Material.FROGSPAWN, MapColor.ORANGE).noCollision().nonOpaque().breakInstantly().sounds(BlockSoundGroup.FROGSPAWN)), null);
    public static final Block SEA_SLUG_EGGS = create("sea_slug_eggs", new SeaSlugEggsBlock(AbstractBlock.Settings.copy(ROE).offset(AbstractBlock.OffsetType.XZ).dynamicBounds()), null);
    public static final Block DUCKWEED = create("duckweed", new WaterFloatingPlant(AbstractBlock.Settings.of(Material.REPLACEABLE_PLANT, MapColor.LIME).breakInstantly().nonOpaque().noCollision().sounds(BlockSoundGroup.WET_GRASS)), null);
    public static final Block ALGAE = create("algae", new AlgaeBlock(AbstractBlock.Settings.of(Material.REPLACEABLE_PLANT, MapColor.GREEN).sounds(BlockSoundGroup.FROGSPAWN).noCollision().nonOpaque().strength(0.1f).ticksRandomly()), ItemGroups.SPAWN_EGGS);
    public static final Block WORMY_DIRT = create("wormy_dirt", new WormyDirtBlock(AbstractBlock.Settings.copy(Blocks.DIRT)), ItemGroups.BUILDING_BLOCKS);
    public static final Block WORMY_MUD = create("wormy_mud", new WormyMudBlock(AbstractBlock.Settings.copy(Blocks.MUD)), ItemGroups.BUILDING_BLOCKS);
    public static final Block OYSTERS = create("oysters", new OystersBlock(AbstractBlock.Settings.of(Material.SOLID_ORGANIC, MapColor.OAK_TAN).strength(0.5f).nonOpaque().sounds(AnglingSounds.SHELL_SOUND_GROUP)), ItemGroups.SPAWN_EGGS);
    public static final Block STARFISH = create("starfish", new StarfishBlock(AbstractBlock.Settings.of(Material.UNDERWATER_PLANT, MapColor.WHITE).strength(0.1f).nonOpaque().noCollision().sounds(AnglingSounds.SHELL_SOUND_GROUP).ticksRandomly(), false), ItemGroups.SPAWN_EGGS);
    public static final Block DEAD_STARFISH = create("dead_starfish", new StarfishBlock(AbstractBlock.Settings.copy(STARFISH), true), ItemGroups.SPAWN_EGGS);
    public static final Block CLAM = create("clam", new ClamBlock(AbstractBlock.Settings.of(Material.UNDERWATER_PLANT, MapColor.WHITE).sounds(AnglingSounds.SHELL_SOUND_GROUP).offset(AbstractBlock.OffsetType.XZ).strength(0.05f).nonOpaque()), ItemGroups.SPAWN_EGGS);
    public static final Block ANEMONE = create("anemone", new AnemoneBlock(AbstractBlock.Settings.of(Material.UNDERWATER_PLANT, MapColor.WHITE).strength(0.1f).nonOpaque().noCollision().sounds(BlockSoundGroup.SLIME).ticksRandomly()), ItemGroups.SPAWN_EGGS);
    public static final Block URCHIN = create("urchin", new UrchinBlock(AbstractBlock.Settings.of(Material.UNDERWATER_PLANT, MapColor.TERRACOTTA_BLUE).strength(0.1f).nonOpaque().noCollision().sounds(AnglingSounds.SHELL_SOUND_GROUP)), null);
    public static final Block SARGASSUM = create("sargassum", new WaterFloatingPlant(AbstractBlock.Settings.of(Material.REPLACEABLE_PLANT, MapColor.YELLOW).breakInstantly().nonOpaque().noCollision().sounds(BlockSoundGroup.WET_GRASS)), null);
    public static final Block PAPYRUS = create("papyrus", new PapyrusBlock(AbstractBlock.Settings.of(Material.UNDERWATER_PLANT, MapColor.GREEN).strength(0).breakInstantly().nonOpaque().noCollision().sounds(BlockSoundGroup.AZALEA).ticksRandomly().offset(AbstractBlock.OffsetType.XZ).dynamicBounds()), ItemGroups.SPAWN_EGGS);

    private static <T extends Block> T create(String name, T block, ItemGroup itemGroup) {
        BLOCKS.put(block, new Identifier(MOD_ID, name));
        if (itemGroup != null) {
            BlockItem blockItem = new BlockItem(block, new Item.Settings());
            ITEMS.put(blockItem, BLOCKS.get(block));
            ItemGroupEvents.modifyEntriesEvent(itemGroup).register(entries -> {
                entries.add(blockItem);
            });
        }
        return block;
    }

    public static void init() {
        BLOCKS.keySet().forEach(block -> Registry.register(Registries.BLOCK, BLOCKS.get(block), block));
        ITEMS.keySet().forEach(item -> Registry.register(Registries.ITEM, ITEMS.get(item), item));
    }
}

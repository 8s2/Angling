package com.eightsidedsquare.angling.core;

import com.eightsidedsquare.angling.common.item.WormItem;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.eightsidedsquare.angling.core.AnglingMod.MOD_ID;

public class AnglingItems {

    private static final Map<Item, Identifier> ITEMS = new LinkedHashMap<>();

    public static final Item FRY_SPAWN_EGG = create("fry_spawn_egg", new SpawnEggItem(AnglingEntities.FRY, 0xffffff, 0xffdd00, new Item.Settings().group(ItemGroup.MISC)));
    public static final Item SUNFISH_SPAWN_EGG = create("sunfish_spawn_egg", new SpawnEggItem(AnglingEntities.SUNFISH, 0x757434, 0x92571c, new Item.Settings().group(ItemGroup.MISC)));
    public static final Item PELICAN_SPAWN_EGG = create("pelican_spawn_egg", new SpawnEggItem(AnglingEntities.PELICAN, 0xe3e1d9, 0x614a29, new Item.Settings().group(ItemGroup.MISC)));
    public static final Item NAUTILUS_SPAWN_EGG = create("nautilus_spawn_egg", new SpawnEggItem(AnglingEntities.NAUTILUS, 0xd4ccc3, 0xae4635, new Item.Settings().group(ItemGroup.MISC)));
    public static final Item SEA_SLUG_SPAWN_EGG = create("sea_slug_spawn_egg", new SpawnEggItem(AnglingEntities.SEA_SLUG, 0x6f4e37, 0xff3800, new Item.Settings().group(ItemGroup.MISC)));
    public static final Item CRAB_SPAWN_EGG = create("crab_spawn_egg", new SpawnEggItem(AnglingEntities.CRAB, 0xbf4927, 0x798696, new Item.Settings().group(ItemGroup.MISC)));
    public static final Item ROE = create("roe", new BlockItem(AnglingBlocks.ROE, new Item.Settings().group(ItemGroup.MISC).maxCount(1)));
    public static final Item SEA_SLUG_EGGS = create("sea_slug_eggs", new BlockItem(AnglingBlocks.SEA_SLUG_EGGS, new Item.Settings().group(ItemGroup.MISC).maxCount(1)));
    public static final Item DUCKWEED = create("duckweed", new PlaceableOnWaterItem(AnglingBlocks.DUCKWEED, new Item.Settings().group(ItemGroup.DECORATIONS)));
    public static final Item SUNFISH_BUCKET = create("sunfish_bucket", new EntityBucketItem(AnglingEntities.SUNFISH, Fluids.WATER, SoundEvents.ITEM_BUCKET_EMPTY_FISH, new Item.Settings().group(ItemGroup.MISC).maxCount(1)));
    public static final Item NAUTILUS_BUCKET = create("nautilus_bucket", new EntityBucketItem(AnglingEntities.NAUTILUS, Fluids.WATER, SoundEvents.ITEM_BUCKET_EMPTY_FISH, new Item.Settings().group(ItemGroup.MISC).maxCount(1)));
    public static final Item FRY_BUCKET = create("fry_bucket", new EntityBucketItem(AnglingEntities.FRY, Fluids.WATER, SoundEvents.ITEM_BUCKET_EMPTY_FISH, new Item.Settings().group(ItemGroup.MISC).maxCount(1)));
    public static final Item SEA_SLUG_BUCKET = create("sea_slug_bucket", new EntityBucketItem(AnglingEntities.SEA_SLUG, Fluids.WATER, SoundEvents.ITEM_BUCKET_EMPTY_FISH, new Item.Settings().group(ItemGroup.MISC).maxCount(1)));
    public static final Item CRAB_BUCKET = create("crab_bucket", new EntityBucketItem(AnglingEntities.CRAB, Fluids.WATER, SoundEvents.ITEM_BUCKET_EMPTY_FISH, new Item.Settings().group(ItemGroup.MISC).maxCount(1)));
    public static final Item WORM = create("worm", new WormItem(new Item.Settings().group(ItemGroup.MISC)));
    public static final Item SUNFISH = create("sunfish", new Item(new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(2).saturationModifier(0.2f).build())));
    public static final Item FRIED_SUNFISH = create("fried_sunfish", new Item(new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(6).saturationModifier(0.9f).build())));

    public static void init() {

        ITEMS.keySet().forEach(item -> Registry.register(Registry.ITEM, ITEMS.get(item), item));
        registerCompostable(DUCKWEED, 0.3f);
        registerCompostable(AnglingBlocks.ALGAE.asItem(), 0.2f);

    }

    private static <T extends Item> T create(String name, T item) {
        ITEMS.put(item, new Identifier(MOD_ID, name));
        return item;
    }

    private static <T extends Item> void registerCompostable(T item, float chance){
        CompostingChanceRegistry.INSTANCE.add(item, chance);
    }
}

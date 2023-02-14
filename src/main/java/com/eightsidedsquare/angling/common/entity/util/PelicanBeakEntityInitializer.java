package com.eightsidedsquare.angling.common.entity.util;

import com.eightsidedsquare.angling.core.AnglingEntities;
import com.eightsidedsquare.angling.core.AnglingUtil;
import com.eightsidedsquare.angling.core.tags.AnglingEntityTypeTags;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AxolotlEntity;
import net.minecraft.entity.passive.TropicalFishEntity;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public abstract class PelicanBeakEntityInitializer {
    private static final Map<EntityType<?>, PelicanBeakEntityInitializer> INITIALIZERS = Util.make(new Object2ObjectOpenHashMap<>(), map -> {});

    public abstract NbtCompound initialize(NbtCompound nbt, Random random, World world);

    public static final PelicanBeakEntityInitializer SIMPLE_INITIALIZER = new PelicanBeakEntityInitializer() {
        @Override
        public NbtCompound initialize(NbtCompound nbt, Random random, World world) {
            return nbt;
        }
    };

    public static void init() {
        registerInitializer(AnglingEntities.DONGFISH, new PelicanBeakEntityInitializer() {
            @Override
            public NbtCompound initialize(NbtCompound nbt, Random random, World world) {
                nbt.putBoolean("HasHorngus", true);
                return nbt;
            }
        });
        registerInitializer(EntityType.PUFFERFISH, new PelicanBeakEntityInitializer() {
            @Override
            public NbtCompound initialize(NbtCompound nbt, Random random, World world) {
                nbt.putInt("PuffState", random.nextInt(3));
                return nbt;
            }
        });
        registerInitializer(EntityType.TROPICAL_FISH, new PelicanBeakEntityInitializer() {
            @Override
            public NbtCompound initialize(NbtCompound nbt, Random random, World world) {
                int variant = random.nextInt(5) == 0 ?
                        (random.nextInt(2) | random.nextInt(6) << 8 | random.nextInt(15) << 16 | random.nextInt(15) << 24)
                        : Util.getRandom(TropicalFishEntity.COMMON_VARIANTS, random);
                nbt.putInt("Variant", variant);
                return nbt;
            }
        });
        registerInitializer(EntityType.AXOLOTL, new PelicanBeakEntityInitializer() {
            @Override
            public NbtCompound initialize(NbtCompound nbt, Random random, World world) {
                nbt.putInt("Variant", AxolotlEntity.Variant.getRandomNatural(random).getId());
                return nbt;
            }
        });
        registerInitializer(EntityType.FROG, new PelicanBeakEntityInitializer() {
            @Override
            public NbtCompound initialize(NbtCompound nbt, Random random, World world) {
                Identifier variant = Registry.FROG_VARIANT.getId(Util.getRandom(Registry.FROG_VARIANT.stream().toList(), random));
                if(variant != null)
                    nbt.putString("variant", variant.toString());
                return nbt;
            }
        });
        registerInitializer(EntityType.RABBIT, new PelicanBeakEntityInitializer() {
            @Override
            public NbtCompound initialize(NbtCompound nbt, Random random, World world) {
                nbt.putInt("RabbitType", random.nextInt(6));
                return nbt;
            }
        });
        registerInitializer(AnglingEntities.SUNFISH, new PelicanBeakEntityInitializer() {
            @Override
            public NbtCompound initialize(NbtCompound nbt, Random random, World world) {
                Identifier variant = SunfishVariant.REGISTRY.getId(
                        AnglingUtil.getRandomTagValue(world, SunfishVariant.Tag.PELICAN_BEAK_VARIANTS, random));
                if(variant != null){
                    nbt.putString("Variant", variant.toString());
                }
                return nbt;
            }
        });
        registerInitializer(AnglingEntities.FRY, new PelicanBeakEntityInitializer() {
            @Override
            public NbtCompound initialize(NbtCompound nbt, Random random, World world) {
                EntityType<?> growUpTo = AnglingUtil.getRandomTagValue(world, AnglingEntityTypeTags.SPAWNING_FISH, random);
                nbt.putString("GrowUpTo", Registry.ENTITY_TYPE.getId(growUpTo).toString());
                NbtCompound variant = new NbtCompound();
                if(!growUpTo.equals(AnglingEntities.FRY))
                    getInitializer(growUpTo).initialize(variant, random, world);
                nbt.put("Variant", variant);
                nbt.putInt("Age", -12000);
                SpawnEggItem spawnEggItem = SpawnEggItem.forEntity(growUpTo);
                if(spawnEggItem != null) {
                    nbt.putInt("Color", spawnEggItem.getColor(0));
                }
                return nbt;
            }
        });
        registerInitializer(AnglingEntities.SEA_SLUG, new PelicanBeakEntityInitializer() {
            @Override
            public NbtCompound initialize(NbtCompound nbt, Random random, World world) {
                nbt.putString("Pattern", AnglingUtil.getRandomTagValue(world, SeaSlugPattern.Tag.NATURAL_PATTERNS, random).getId().toString());
                nbt.putString("BaseColor", AnglingUtil.getRandomTagValue(world, SeaSlugColor.Tag.BASE_COLORS, random).getId().toString());
                nbt.putString("PatternColor", AnglingUtil.getRandomTagValue(world, SeaSlugColor.Tag.PATTERN_COLORS, random).getId().toString());
                nbt.putBoolean("Bioluminescent", random.nextBoolean());
                return nbt;
            }
        });
        registerInitializer(AnglingEntities.CRAB, new PelicanBeakEntityInitializer() {
            @Override
            public NbtCompound initialize(NbtCompound nbt, Random random, World world) {
                nbt.putString("Variant", AnglingUtil.getRandomTagValue(world, CrabVariant.Tag.NATURAL_VARIANTS, random).getId().toString());
                return nbt;
            }
        });
    }

    public static PelicanBeakEntityInitializer getInitializer(EntityType<?> type) {
        return INITIALIZERS.getOrDefault(type, SIMPLE_INITIALIZER);
    }

    public static void registerInitializer(@NotNull EntityType<?> type, @NotNull PelicanBeakEntityInitializer initializer) {
        INITIALIZERS.put(type, initializer);
    }
}

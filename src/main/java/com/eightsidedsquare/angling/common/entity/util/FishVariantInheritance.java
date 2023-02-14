package com.eightsidedsquare.angling.common.entity.util;

import com.eightsidedsquare.angling.core.AnglingEntities;
import com.eightsidedsquare.angling.core.AnglingUtil;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public abstract class FishVariantInheritance {

    private static final Map<EntityType<? extends FishEntity>, FishVariantInheritance> INHERITANCE_TYPES = Util.make(new Object2ObjectOpenHashMap<>(), map -> {});

    public static final FishVariantInheritance SIMPLE_INHERITANCE = new FishVariantInheritance() {
        @Override
        protected NbtCompound getChild(NbtCompound parent, NbtCompound mate, NbtCompound child, World world) {
            return child;
        }
    };

    public static void init() {
        registerVariantInheritance(EntityType.COD, SIMPLE_INHERITANCE);
        registerVariantInheritance(EntityType.SALMON, SIMPLE_INHERITANCE);
        registerVariantInheritance(EntityType.PUFFERFISH, SIMPLE_INHERITANCE);
        registerVariantInheritance(AnglingEntities.DONGFISH, SIMPLE_INHERITANCE);
        registerVariantInheritance(EntityType.TROPICAL_FISH, new FishVariantInheritance() {
            @Override
            protected NbtCompound getChild(NbtCompound parent, NbtCompound mate, NbtCompound child, World world) {
                int parentVariant = parent.getInt("Variant");
                int mateVariant = mate.getInt("Variant");
                Random random = world.getRandom();
                int variant = pickRandomTropicalFishVariantValue(parentVariant, mateVariant, random, 0, 0xffff, false) |
                        pickRandomTropicalFishVariantValue(parentVariant, mateVariant, random, 16, 0xff, true) |
                        pickRandomTropicalFishVariantValue(parentVariant, mateVariant, random, 24, 0xff, true);
                child.putInt("Variant", variant);
                return child;
            }

            private int pickRandomTropicalFishVariantValue(int parentVariant, int mateVariant, Random random, int shift, int size, boolean color) {
                if(random.nextInt(16) == 0) {
                    return (color ? random.nextInt(15) : random.nextInt(2) | (random.nextInt(6) << 8)) << shift;
                }
                return ((((random.nextBoolean() ? parentVariant : mateVariant) >> shift) & size) << shift);
            }
        });
        registerVariantInheritance(AnglingEntities.SUNFISH, new FishVariantInheritance() {
            @Override
            protected NbtCompound getChild(NbtCompound parent, NbtCompound mate, NbtCompound child, World world) {
                SunfishVariant parentVariant = SunfishVariant.fromId(Identifier.tryParse(parent.getString("Variant")));
                SunfishVariant mateVariant = SunfishVariant.fromId(Identifier.tryParse(mate.getString("Variant")));
                SunfishVariant childVariant = world.getRandom().nextBoolean() ? parentVariant : mateVariant;
                if(AnglingUtil.pairsAreEqual(parentVariant, mateVariant, SunfishVariant.BLUEGILL, SunfishVariant.REDBREAST)) {
                    childVariant = SunfishVariant.BLUEGILL_AND_REDBREAST_HYBRID;
                }else if(AnglingUtil.pairsAreEqual(parentVariant, mateVariant, SunfishVariant.BLUEGILL, SunfishVariant.PUMPKINSEED)) {
                    childVariant = SunfishVariant.BLUEGILL_AND_PUMPKINSEED_HYBRID;
                }else if(childVariant.equals(SunfishVariant.DIANSUS_DIANSUR))
                    childVariant = SunfishVariant.GREEN;
                child.putString("Variant", SunfishVariant.getId(childVariant).toString());
                return child;
            }
        });
    }

    public static FishVariantInheritance getVariantInheritance(EntityType<?> entityType) {
        return INHERITANCE_TYPES.getOrDefault(entityType, SIMPLE_INHERITANCE);
    }

    public static <U extends FishEntity> void registerVariantInheritance(@NotNull EntityType<U> type, @NotNull FishVariantInheritance variantBreeder) {
        INHERITANCE_TYPES.put(type, variantBreeder);
    }

    protected abstract NbtCompound getChild(NbtCompound parent, NbtCompound mate, NbtCompound child, World world);

    public NbtCompound getChild(NbtCompound parent, NbtCompound mate, World world) {
        NbtCompound child = new NbtCompound();
        return getChild(parent, mate, child, world);
    }

}

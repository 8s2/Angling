package com.eightsidedsquare.angling.common.entity.util;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.eightsidedsquare.angling.core.AnglingMod.MOD_ID;


public record SunfishVariant(Identifier texture) {

    private static final Map<SunfishVariant, Identifier> VARIANTS = new LinkedHashMap<>();

    public static final SunfishVariant PUMPKINSEED = create("pumpkinseed");
    public static final SunfishVariant LONGEAR = create("longear");
    public static final SunfishVariant BLUEGILL = create("bluegill");
    public static final SunfishVariant REDBREAST = create("redbreast");
    public static final SunfishVariant GREEN = create("green");
    public static final SunfishVariant WARMOUTH = create("warmouth");
    public static final SunfishVariant BLUEGILL_AND_REDBREAST_HYBRID = create("bluegill_and_redbreast_hybrid");
    public static final SunfishVariant BLUEGILL_AND_PUMPKINSEED_HYBRID = create("bluegill_and_pumpkinseed_hybrid");
    public static final SunfishVariant DIANSUS_DIANSUR = create("diansus_diansur");

    public static final Registry<SunfishVariant> REGISTRY = FabricRegistryBuilder
            .createDefaulted(SunfishVariant.class, new Identifier(MOD_ID, "sunfish_variant"), new Identifier(MOD_ID, "pumpkinseed"))
            .attribute(RegistryAttribute.SYNCED).buildAndRegister();

    public static final TrackedDataHandler<SunfishVariant> TRACKED_DATA_HANDLER = TrackedDataHandler.of(REGISTRY);

    public static Identifier getId(SunfishVariant variant) {
        return REGISTRY.getId(variant);
    }

    public static SunfishVariant fromId(String id) {
        return fromId(Identifier.tryParse(id));
    }

    public String getTranslationKey() {
        return "sunfish_variant." + getId(this).getNamespace() + "." + getId(this).getPath();
    }

    public static SunfishVariant fromId(Identifier id) {
        return REGISTRY.get(id);
    }


    private static SunfishVariant create(String name) {
        SunfishVariant variant = new SunfishVariant(new Identifier(MOD_ID, "textures/entity/sunfish/" + name + ".png"));
        VARIANTS.put(variant, new Identifier(MOD_ID, name));
        return variant;
    }

    public static void init() {
        TrackedDataHandlerRegistry.register(TRACKED_DATA_HANDLER);
        VARIANTS.keySet().forEach(variant -> Registry.register(REGISTRY, VARIANTS.get(variant), variant));
    }

    public static class Tag {

        public static final TagKey<SunfishVariant> NATURAL_SUNFISH = of("natural_sunfish");
        public static final TagKey<SunfishVariant> PELICAN_BEAK_VARIANTS = of("pelican_beak_variants");

        private static TagKey<SunfishVariant> of(String id) {
            return of(new Identifier(MOD_ID, id));
        }

        public static TagKey<SunfishVariant> of(Identifier id) {
            return TagKey.of(REGISTRY.getKey(), id);
        }
    }
}

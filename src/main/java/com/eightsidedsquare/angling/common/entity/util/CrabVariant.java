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

public record CrabVariant(Identifier texture) {

    private static final Map<CrabVariant, Identifier> VARIANTS = new LinkedHashMap<>();

    public static final CrabVariant DUNGENESS = create("dungeness");
    public static final CrabVariant GHOST = create("ghost");
    public static final CrabVariant BLUE_CLAW = create("blue_claw");

    public static final Registry<CrabVariant> REGISTRY = FabricRegistryBuilder
            .createDefaulted(CrabVariant.class, new Identifier(MOD_ID, "crab_variant"), new Identifier(MOD_ID, "dungeness"))
            .attribute(RegistryAttribute.SYNCED).buildAndRegister();

    public String getTranslationKey() {
        return "crab_variant." + this.getId().getNamespace() + "." + this.getId().getPath();
    }

    public static final TrackedDataHandler<CrabVariant> TRACKED_DATA_HANDLER = TrackedDataHandler.of(REGISTRY);

    public Identifier getId() {
        return REGISTRY.getId(this);
    }

    public static CrabVariant fromId(String id) {
        return fromId(Identifier.tryParse(id));
    }

    public static CrabVariant fromId(Identifier id) {
        return REGISTRY.get(id);
    }

    private static CrabVariant create(String name) {
        CrabVariant pattern = new CrabVariant(new Identifier(MOD_ID, "textures/entity/crab/" + name + ".png"));
        VARIANTS.put(pattern, new Identifier(MOD_ID, name));
        return pattern;
    }

    public static void init() {
        TrackedDataHandlerRegistry.register(TRACKED_DATA_HANDLER);
        VARIANTS.keySet().forEach(variant -> Registry.register(REGISTRY, VARIANTS.get(variant), variant));
    }

    public static class Tag {

        public static final TagKey<CrabVariant> NATURAL_VARIANTS = of("natural_variants");

        private static TagKey<CrabVariant> of(String id) {
            return of(new Identifier(MOD_ID, id));
        }

        public static TagKey<CrabVariant> of(Identifier id) {
            return TagKey.of(REGISTRY.getKey(), id);
        }
    }

}

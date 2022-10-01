package com.eightsidedsquare.angling.core.ai;

import com.mojang.serialization.Codec;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.util.registry.Registry;

import java.util.Optional;

import static com.eightsidedsquare.angling.core.AnglingMod.MOD_ID;

public class AnglingMemoryModuleTypes {

    public static final MemoryModuleType<Unit> SOARING_COOLDOWN = register("soaring_cooldown", Codec.unit(Unit.INSTANCE));
    public static final MemoryModuleType<Unit> CAN_TRADE = register("can_trade", Codec.unit(Unit.INSTANCE));
    public static final MemoryModuleType<Unit> HAS_TRADED = register("has_traded", Codec.unit(Unit.INSTANCE));

    private static <U> MemoryModuleType<U> register(String id, Codec<U> codec) {
        return Registry.register(Registry.MEMORY_MODULE_TYPE, new Identifier(MOD_ID, id), new MemoryModuleType<>(Optional.of(codec)));
    }
}

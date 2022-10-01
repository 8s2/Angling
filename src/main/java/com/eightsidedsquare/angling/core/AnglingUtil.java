package com.eightsidedsquare.angling.core;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Util;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class AnglingUtil {

    public static <T> List<T> getTagValues(World world, TagKey<T> tagKey) {
        return world.getRegistryManager().get(tagKey.registry()).getEntryList(tagKey).map(entries -> entries.stream().map(RegistryEntry::value).toList()).orElse(List.of());
    }

    public static <T> T getRandomTagValue(World world, TagKey<T> tagKey, Random random) {
        return Util.getRandom(getTagValues(world, tagKey), random);
    }

    public static NbtCompound entityToNbt(Entity entity, boolean stripData) {
        NbtCompound nbt = entity.writeNbt(new NbtCompound());
        nbt.putString("id", Registry.ENTITY_TYPE.getId(entity.getType()).toString());
        if(stripData){
            stripEntityNbt(nbt);
        }
        return nbt;
    }

    public static boolean runningSodium() {
        return FabricLoader.getInstance().isModLoaded("sodium");
    }

    public static void stripEntityNbt(NbtCompound nbt) {
        // Yes, this is cursed. No, I'm not sorry.
        nbt.remove("AbsorptionAmount");
        nbt.remove("Air");
        nbt.remove("ArmorDropChances");
        nbt.remove("ArmorItems");
        nbt.remove("Attributes");
        nbt.remove("CanPickUpLoot");
        nbt.remove("DeathTime");
        nbt.remove("FallDistance");
        nbt.remove("FallFlying");
        nbt.remove("Fire");
        nbt.remove("FromBucket");
        nbt.remove("HandDropChances");
        nbt.remove("HandItems");
        nbt.remove("Health");
        nbt.remove("HurtByTimestamp");
        nbt.remove("HurtTime");
        nbt.remove("Invulnerable");
        nbt.remove("LeftHanded");
        nbt.remove("Motion");
        nbt.remove("OnGround");
        nbt.remove("PersistenceRequired");
        nbt.remove("PortalCooldown");
        nbt.remove("Pos");
        nbt.remove("Rotation");
        nbt.remove("cardinal_components");
        nbt.remove("UUID");
    }

    public static <A, B, C, D> boolean pairsAreEqual(A a1, B a2, C b1, D b2) {
        return (a1.equals(b1) && a2.equals(b2)) || (a1.equals(b2) && a2.equals(b1));
    }

    public static Optional<Entity> entityFromNbt(NbtCompound nbt, World world) {
        if(!nbt.contains("id", NbtElement.STRING_TYPE))
            return Optional.empty();
        return Optional.ofNullable(EntityType.loadEntityWithPassengers(nbt, world, Function.identity()));
    }

    public static boolean isReloadingResources() {
        return MinecraftClient.getInstance().getOverlay() instanceof SplashOverlay splashOverlay && splashOverlay.reloading;
    }

}

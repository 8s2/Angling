package com.eightsidedsquare.angling.core;

import com.eightsidedsquare.angling.common.entity.util.CrabVariant;
import com.eightsidedsquare.angling.common.entity.util.FishVariantInheritance;
import com.eightsidedsquare.angling.common.entity.util.PelicanBeakEntityInitializer;
import com.eightsidedsquare.angling.common.entity.util.SeaSlugColor;
import com.eightsidedsquare.angling.common.entity.util.SeaSlugPattern;
import com.eightsidedsquare.angling.common.entity.util.SunfishVariant;
import com.eightsidedsquare.angling.common.world.PelicanSpawner;
import com.eightsidedsquare.angling.config.AnglingConfig;
import com.eightsidedsquare.angling.core.world.AnglingPlacedFeatures;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import software.bernie.geckolib.GeckoLib;

public class AnglingMod implements ModInitializer {
    public static final String MOD_ID = "angling";
    public static AnglingConfig CONFIG;

    @Override
    public void onInitialize() {
        GeckoLib.initialize();
        AnglingBlocks.init();
        AnglingItems.init();
        AnglingSounds.init();
        AnglingEntities.init();
        SunfishVariant.init();
        SeaSlugPattern.init();
        SeaSlugColor.init();
        CrabVariant.init();
        AnglingCriteria.init();
        AnglingParticles.init();
        FishVariantInheritance.init();
        PelicanBeakEntityInitializer.init();

        PelicanSpawner spawner = new PelicanSpawner();
        ServerTickEvents.END_WORLD_TICK.register(world -> spawner.spawn(world, world.getServer().isMonsterSpawningEnabled(), world.getServer().shouldSpawnAnimals()));

        AutoConfig.register(AnglingConfig.class, JanksonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(AnglingConfig.class).getConfig();
    }
}

package com.eightsidedsquare.angling.core;

import com.eightsidedsquare.angling.common.entity.util.*;
import com.eightsidedsquare.angling.core.world.AnglingPlacedFeatures;
import net.fabricmc.api.ModInitializer;
import software.bernie.geckolib3.GeckoLib;

public class AnglingMod implements ModInitializer {
	public static final String MOD_ID = "angling";

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
		AnglingPlacedFeatures.init();
		FishVariantInheritance.init();
		PelicanBeakEntityInitializer.init();
	}
}

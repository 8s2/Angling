package com.eightsidedsquare.angling.client.model;

import com.eightsidedsquare.angling.common.entity.CrabEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static com.eightsidedsquare.angling.core.AnglingMod.MOD_ID;

public class CrabEntityModel extends AnimatedGeoModel<CrabEntity> {

    @Override
    public Identifier getModelResource(CrabEntity entity) {
        return new Identifier(MOD_ID, "geo/crab.geo.json");
    }

    @Override
    public Identifier getTextureResource(CrabEntity entity) {
        return entity.getVariant().texture();
    }

    @Override
    public Identifier getAnimationResource(CrabEntity entity) {
        return new Identifier(MOD_ID, "animations/crab.animation.json");
    }
}

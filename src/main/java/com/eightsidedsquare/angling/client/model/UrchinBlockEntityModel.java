package com.eightsidedsquare.angling.client.model;

import com.eightsidedsquare.angling.common.entity.UrchinBlockEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static com.eightsidedsquare.angling.core.AnglingMod.MOD_ID;

public class UrchinBlockEntityModel extends AnimatedGeoModel<UrchinBlockEntity> {

    @Override
    public Identifier getModelResource(UrchinBlockEntity object) {
        return new Identifier(MOD_ID, "geo/urchin.geo.json");
    }

    @Override
    public Identifier getTextureResource(UrchinBlockEntity entity) {
        return new Identifier(MOD_ID, "textures/entity/urchin/urchin.png");
    }

    @Override
    public Identifier getAnimationResource(UrchinBlockEntity animatable) {
        return new Identifier(MOD_ID, "animations/urchin.animation.json");
    }

}

package com.eightsidedsquare.angling.client.model;

import com.eightsidedsquare.angling.common.entity.AnemoneBlockEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

import static com.eightsidedsquare.angling.core.AnglingMod.MOD_ID;

public class AnemoneBlockEntityModel extends GeoModel<AnemoneBlockEntity> {
    @Override
    public Identifier getModelResource(AnemoneBlockEntity object) {
        return new Identifier(MOD_ID, "geo/anemone.geo.json");
    }

    @Override
    public Identifier getTextureResource(AnemoneBlockEntity entity) {
        return new Identifier(MOD_ID, "textures/entity/anemone/anemone.png");
    }

    @Override
    public Identifier getAnimationResource(AnemoneBlockEntity animatable) {
        return new Identifier(MOD_ID, "animations/anemone.animation.json");
    }
}

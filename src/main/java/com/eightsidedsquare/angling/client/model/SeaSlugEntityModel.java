package com.eightsidedsquare.angling.client.model;

import com.eightsidedsquare.angling.common.entity.SeaSlugEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static com.eightsidedsquare.angling.core.AnglingMod.MOD_ID;

public class SeaSlugEntityModel extends AnimatedGeoModel<SeaSlugEntity> {
    @Override
    public Identifier getModelResource(SeaSlugEntity object) {
        return new Identifier(MOD_ID, "geo/sea_slug.geo.json");
    }

    @Override
    public Identifier getTextureResource(SeaSlugEntity object) {
        return new Identifier(MOD_ID, "textures/entity/sea_slug/sea_slug.png");
    }

    @Override
    public Identifier getAnimationResource(SeaSlugEntity animatable) {
        return new Identifier(MOD_ID, "animations/sea_slug.animation.json");
    }

    @Override
    public void setLivingAnimations(SeaSlugEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
    }
}

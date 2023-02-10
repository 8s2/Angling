package com.eightsidedsquare.angling.client.model;

import com.eightsidedsquare.angling.common.entity.AnemoneBlockEntity;
import com.eightsidedsquare.angling.core.AnglingUtil;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static com.eightsidedsquare.angling.core.AnglingMod.MOD_ID;

public class AnemoneBlockEntityModel extends AnimatedGeoModel<AnemoneBlockEntity> {
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

    @Override
    public void setLivingAnimations(AnemoneBlockEntity entity, Integer uniqueID) {

        super.setLivingAnimations(entity, uniqueID);
    }
}

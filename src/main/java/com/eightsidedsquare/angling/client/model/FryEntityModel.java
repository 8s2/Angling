package com.eightsidedsquare.angling.client.model;

import com.eightsidedsquare.angling.common.entity.FryEntity;
import com.eightsidedsquare.angling.core.AnglingUtil;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static com.eightsidedsquare.angling.core.AnglingMod.MOD_ID;

public class FryEntityModel extends AnimatedGeoModel<FryEntity> {
    @Override
    public Identifier getModelResource(FryEntity object) {
        return new Identifier(MOD_ID, "geo/fry.geo.json");
    }

    @Override
    public Identifier getTextureResource(FryEntity object) {
        return new Identifier(MOD_ID, "textures/entity/fry/fry_innards.png");
    }

    @Override
    public Identifier getAnimationResource(FryEntity animatable) {
        return new Identifier(MOD_ID, "animations/fry.animation.json");
    }

    @Override
    public void setLivingAnimations(FryEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        if(!AnglingUtil.isReloadingResources()) {
            super.setLivingAnimations(entity, uniqueID, customPredicate);
            IBone root = getAnimationProcessor().getBone("root");
            if(!entity.isTouchingWater() && root != null) {
                root.setRotationZ((float) (Math.PI / -2d));
            }
        }
    }
}

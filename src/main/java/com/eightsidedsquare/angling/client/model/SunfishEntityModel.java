package com.eightsidedsquare.angling.client.model;

import com.eightsidedsquare.angling.common.entity.SunfishEntity;
import com.eightsidedsquare.angling.common.entity.util.SunfishVariant;
import com.eightsidedsquare.angling.core.AnglingUtil;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static com.eightsidedsquare.angling.core.AnglingMod.MOD_ID;

public class SunfishEntityModel extends AnimatedGeoModel<SunfishEntity> {
    @Override
    public Identifier getModelResource(SunfishEntity entity) {
        return new Identifier(MOD_ID, "geo/sunfish.geo.json");
    }

    @Override
    public Identifier getTextureResource(SunfishEntity entity) {
        SunfishVariant variant = entity.getVariant();
        return variant != null ? variant.texture() : SunfishVariant.BLUEGILL.texture();
    }

    @Override
    public Identifier getAnimationResource(SunfishEntity entity) {
        return new Identifier(MOD_ID, "animations/sunfish.animation.json");
    }

    @Override
    public void setLivingAnimations(SunfishEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        if(!AnglingUtil.isReloadingResources()) {
            super.setLivingAnimations(entity, uniqueID, customPredicate);
            IBone root = getAnimationProcessor().getBone("root");
            if(!entity.isTouchingWater() && root != null) {
                root.setRotationZ((float) (Math.PI / -2d));
            }
        }
    }
}

package com.eightsidedsquare.angling.client.model;

import com.eightsidedsquare.angling.common.entity.CrabEntity;
import com.eightsidedsquare.angling.core.AnglingUtil;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

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

    @Override @SuppressWarnings("unchecked")
    public void setLivingAnimations(CrabEntity entity, Integer uniqueID, AnimationEvent event) {
        if(!AnglingUtil.isReloadingResources()) {
            super.setLivingAnimations(entity, uniqueID, event);
            IBone root = getAnimationProcessor().getBone("root");
            IBone eyes = getAnimationProcessor().getBone("eyes");
            EntityModelData extraData = (EntityModelData) event.getExtraDataOfType(EntityModelData.class).get(0);
            if (eyes != null) {
                eyes.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            }
            if(extraData.isChild && root != null) {
                root.setScaleX(0.35f);
                root.setScaleY(0.35f);
                root.setScaleZ(0.35f);
                root.setPositionY(-1.75f);
            }
        }
    }
}

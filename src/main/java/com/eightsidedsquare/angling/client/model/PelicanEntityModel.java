package com.eightsidedsquare.angling.client.model;

import com.eightsidedsquare.angling.common.entity.PelicanEntity;
import com.eightsidedsquare.angling.core.AnglingUtil;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import static com.eightsidedsquare.angling.core.AnglingMod.MOD_ID;

public class PelicanEntityModel extends AnimatedGeoModel<PelicanEntity> {
    @Override
    public Identifier getModelResource(PelicanEntity entity) {
        return new Identifier(MOD_ID, "geo/pelican.geo.json");
    }

    @Override
    public Identifier getTextureResource(PelicanEntity entity) {
        return new Identifier(MOD_ID, "textures/entity/pelican/pelican.png");
    }

    @Override
    public Identifier getAnimationResource(PelicanEntity entity) {
        return new Identifier(MOD_ID, "animations/pelican.animation.json");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setLivingAnimations(PelicanEntity entity, Integer uniqueID, AnimationEvent event) {
        if(!AnglingUtil.isReloadingResources()) {
            super.setLivingAnimations(entity, uniqueID, event);
            IBone headJoint = this.getAnimationProcessor().getBone("head_joint");

            EntityModelData extraData = (EntityModelData) event.getExtraDataOfType(EntityModelData.class).get(0);
            if (headJoint != null) {
                headJoint.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
                headJoint.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
            }
        }
    }
}

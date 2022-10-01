package com.eightsidedsquare.angling.client.model;

import com.eightsidedsquare.angling.common.entity.DongfishEntity;
import com.eightsidedsquare.angling.core.AnglingUtil;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static com.eightsidedsquare.angling.core.AnglingMod.MOD_ID;

public class DongfishEntityModel extends AnimatedGeoModel<DongfishEntity> {
    @Override
    public Identifier getModelResource(DongfishEntity entity) {
        return new Identifier(MOD_ID, "geo/dongfish.geo.json");
    }

    @Override
    public Identifier getTextureResource(DongfishEntity entity) {
        return new Identifier(MOD_ID, "textures/entity/dongfish.png");
    }

    @Override
    public Identifier getAnimationResource(DongfishEntity entity) {
        return new Identifier(MOD_ID, "animations/dongfish.animation.json");
    }

    @Override
    public void setLivingAnimations(DongfishEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        if(!AnglingUtil.isReloadingResources()) {
            super.setLivingAnimations(entity, uniqueID, customPredicate);
            IBone root = getAnimationProcessor().getBone("root");
            IBone scungle = getAnimationProcessor().getBone("scungle");
            if(!entity.isTouchingWater() && root != null) {
                root.setRotationZ((float) (Math.PI / -2d));
            }
            if(scungle != null) {
                scungle.setHidden(!entity.hasHorngus());
            }
        }
    }
}

package com.eightsidedsquare.angling.client.model;

import com.eightsidedsquare.angling.common.entity.NautilusEntity;
import com.eightsidedsquare.angling.core.AnglingUtil;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;

public class NautilusEntityModel extends BasicEntityModel<NautilusEntity> {
    public NautilusEntityModel() {
        super("nautilus", true);
    }

    @Override
    public void setLivingAnimations(NautilusEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        if(!AnglingUtil.isReloadingResources()) {
            super.setLivingAnimations(entity, uniqueID, customPredicate);
            IBone root = getAnimationProcessor().getBone("root");
            if(!entity.isTouchingWater() && root != null) {
                root.setRotationZ((float) (Math.PI / -2d));
                root.setPositionY(-1.5f);
            }
        }
    }
}

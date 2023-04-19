package com.eightsidedsquare.angling.client.model;

import com.eightsidedsquare.angling.common.entity.DongfishEntity;
import com.eightsidedsquare.angling.core.AnglingUtil;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;

public class DongfishEntityModel extends BasicEntityModel<DongfishEntity> {

    public DongfishEntityModel() {
        super("dongfish", true);
    }

    @Override
    public void setCustomAnimations(DongfishEntity animatable, long instanceId, AnimationState<DongfishEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        if(!AnglingUtil.isReloadingResources()) {
            CoreGeoBone scungle = getAnimationProcessor().getBone("scungle");
            if(scungle != null) {
                scungle.setHidden(!animatable.hasHorngus());
            }
        }
    }
}

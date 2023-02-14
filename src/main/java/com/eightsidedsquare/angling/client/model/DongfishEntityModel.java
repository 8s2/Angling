package com.eightsidedsquare.angling.client.model;

import com.eightsidedsquare.angling.common.entity.DongfishEntity;
import com.eightsidedsquare.angling.core.AnglingUtil;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;

public class DongfishEntityModel extends BasicEntityModel<DongfishEntity> {

    public DongfishEntityModel() {
        super("dongfish", true);
    }

    @Override
    public void setLivingAnimations(DongfishEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        if(!AnglingUtil.isReloadingResources()) {
            IBone scungle = getAnimationProcessor().getBone("scungle");
            if(scungle != null) {
                scungle.setHidden(!entity.hasHorngus());
            }
        }
    }
}

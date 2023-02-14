package com.eightsidedsquare.angling.client.model;

import com.eightsidedsquare.angling.common.entity.SunfishEntity;
import com.eightsidedsquare.angling.common.entity.util.SunfishVariant;
import com.eightsidedsquare.angling.core.AnglingUtil;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static com.eightsidedsquare.angling.core.AnglingMod.MOD_ID;

public class SunfishEntityModel extends BasicEntityModel<SunfishEntity> {
    public SunfishEntityModel() {
        super("sunfish", true);
    }
    @Override
    public Identifier getTextureResource(SunfishEntity entity) {
        SunfishVariant variant = entity.getVariant();
        return variant != null ? variant.texture() : SunfishVariant.BLUEGILL.texture();
    }
}

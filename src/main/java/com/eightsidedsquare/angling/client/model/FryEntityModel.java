package com.eightsidedsquare.angling.client.model;

import com.eightsidedsquare.angling.common.entity.FryEntity;
import com.eightsidedsquare.angling.core.AnglingUtil;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static com.eightsidedsquare.angling.core.AnglingMod.MOD_ID;

public class FryEntityModel extends BasicEntityModel<FryEntity> {
    public FryEntityModel() {
        super("fry", true);
    }
    @Override
    public Identifier getTextureResource(FryEntity object) {
        return new Identifier(MOD_ID, "textures/entity/fry/fry_innards.png");
    }
}

package com.eightsidedsquare.angling.client.model;

import com.eightsidedsquare.angling.core.AnglingUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import javax.annotation.Nullable;

import static com.eightsidedsquare.angling.core.AnglingMod.MOD_ID;

public class BasicEntityModel<A extends LivingEntity & IAnimatable> extends AnimatedGeoModel<A> {

    private final Identifier model;
    private final Identifier texture;
    private final Identifier animation;
    private final boolean liesOutOfWater;
    private final @Nullable String head;

    public BasicEntityModel(Identifier model, Identifier texture, Identifier animation, boolean liesOutOfWater, @Nullable String head) {
        this.model = model;
        this.texture = texture;
        this.animation = animation;
        this.liesOutOfWater = liesOutOfWater;
        this.head = head;
    }

    public BasicEntityModel(String name, boolean liesOutOfWater, @Nullable String head) {
        this(
                new Identifier(MOD_ID,"geo/" + name + ".geo.json"),
                new Identifier(MOD_ID, "textures/entity/" + name + "/" + name + ".png"),
                new Identifier(MOD_ID, "animations/" + name + ".animation.json"), liesOutOfWater, head);

    }

    public BasicEntityModel(String name, boolean liesOutOfWater) {
        this(name, liesOutOfWater, null);
    }

    @Override
    public Identifier getModelResource(A entity) {
        return model;
    }

    @Override
    public Identifier getTextureResource(A entity) {
        return texture;
    }

    @Override
    public Identifier getAnimationResource(A entity) {
        return animation;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setLivingAnimations(A entity, Integer uniqueID, AnimationEvent event) {
        if(!AnglingUtil.isReloadingResources()) {
            super.setLivingAnimations(entity, uniqueID, event);
            if(liesOutOfWater) {
                IBone root = getAnimationProcessor().getBone("root");
                if (!entity.isTouchingWater() && root != null) {
                    root.setRotationZ((float) (Math.PI / 2d));
                }
            }
            if(head != null) {
                IBone headBone = this.getAnimationProcessor().getBone(head);
                EntityModelData extraData = (EntityModelData) event.getExtraDataOfType(EntityModelData.class).get(0);
                if (headBone != null) {
                    headBone.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
                    headBone.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
                }
            }
        }
    }
}

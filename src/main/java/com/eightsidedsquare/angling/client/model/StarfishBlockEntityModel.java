package com.eightsidedsquare.angling.client.model;

import com.eightsidedsquare.angling.common.block.StarfishBlock;
import com.eightsidedsquare.angling.common.entity.StarfishBlockEntity;
import com.eightsidedsquare.angling.core.AnglingUtil;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3i;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

import java.util.Optional;

import static com.eightsidedsquare.angling.core.AnglingMod.MOD_ID;

public class StarfishBlockEntityModel extends GeoModel<StarfishBlockEntity> {

    public StarfishBlockEntityModel() {
    }

    @Override
    public Identifier getModelResource(StarfishBlockEntity object) {
        return new Identifier(MOD_ID, "geo/starfish.geo.json");
    }

    @Override
    public Identifier getTextureResource(StarfishBlockEntity entity) {
        if(entity != null && !((StarfishBlock) entity.getCachedState().getBlock()).isDead())
            return new Identifier(MOD_ID, "textures/entity/starfish/starfish.png");
        return new Identifier(MOD_ID, "textures/entity/starfish/dead_starfish.png");
    }

    @Override
    public Identifier getAnimationResource(StarfishBlockEntity animatable) {
        return new Identifier(MOD_ID, "animations/starfish.animation.json");
    }

    @Override
    public void setCustomAnimations(StarfishBlockEntity animatable, long instanceId, AnimationState<StarfishBlockEntity> animationState) {
        if(!AnglingUtil.isReloadingResources()){
            super.setCustomAnimations(animatable, instanceId, animationState);
            Optional.ofNullable(getAnimationProcessor().getBone("root")).ifPresent(bone -> {
                Vec3i rotation = animatable.getRotation();
                bone.setRotX((float) Math.toRadians(rotation.getX()));
                bone.setRotY((float) Math.toRadians(rotation.getY()));
                bone.setRotZ((float) Math.toRadians(rotation.getZ()));
            });
            Optional.ofNullable(getAnimationProcessor().getBone("starfish")).ifPresent(bone ->
                    bone.setRotZ((float) animatable.getRandomRotation()));
        }
    }
}

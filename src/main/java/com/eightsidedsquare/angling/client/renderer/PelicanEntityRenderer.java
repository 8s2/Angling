package com.eightsidedsquare.angling.client.renderer;

import com.eightsidedsquare.angling.client.model.BasicEntityModel;
import com.eightsidedsquare.angling.common.entity.PelicanEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.util.RenderUtils;

public class PelicanEntityRenderer extends BasicEntityRenderer<PelicanEntity> {

    private final EntityRenderDispatcher entityRenderDispatcher;
    @Nullable
    private PelicanEntity pelicanEntity;
    private VertexConsumerProvider vertexConsumerProvider;

    public PelicanEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new BasicEntityModel<>("pelican", false, "head_joint"));
        entityRenderDispatcher = ctx.getRenderDispatcher();
        this.shadowRadius = 0.35f;
    }
/*
    @Override
    public void renderEarly(PelicanEntity entity, MatrixStack stackIn, float ticks, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float tickDelta) {
        this.vertexConsumerProvider = renderTypeBuffer;
        this.pelicanEntity = entity;
        super.renderEarly(entity, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, tickDelta);
    }

    @Override
    public void renderRecursively(GeoBone bone, MatrixStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {

        if(bone.getName().equals("beak_bottom") && pelicanEntity != null && pelicanEntity.isBeakOpen() && pelicanEntity.getEntityInBeak().isPresent()) {
            Entity entityInBeak = pelicanEntity.getEntityInBeak().get();
            Vector3d pos = bone.getPosition();
            stack.push();
            GeoBone parent = bone;
            while(parent != null) {
                RenderUtils.translate(parent, stack);
                if(parent.getName().equals("root")) {
                    if(pelicanEntity.isTouchingWater()) {
                        stack.translate(0, 0.5d, 0.25d);
                    }else if(pelicanEntity.isFlying()) {
                        stack.translate(0, 0.6d, 0.5d);
                    }
                }
                RenderUtils.moveToPivot(parent, stack);
                RenderUtils.rotate(parent, stack);
                if(parent.getName().equals("head_joint")) {
                    stack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(pelicanEntity.getPitch()));
                    stack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(pelicanEntity.getHeadYaw() - pelicanEntity.getBodyYaw()));
                }
                RenderUtils.scale(parent, stack);
                RenderUtils.moveBackFromPivot(parent, stack);
                parent = parent.getParent();
            }
            stack.translate(0, 0.75f, -1.35f);
            stack.scale(0.5f, 0.5f, 0.5f);
            stack.multiply(Quaternion.fromEulerXyzDegrees(new Vec3f(0, 180, 0)));
            entityRenderDispatcher.render(entityInBeak, pos.x, pos.y, pos.z, 0, 0, stack, vertexConsumerProvider, packedLightIn);
            stack.pop();
            bufferIn = vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucent(getTextureResource(pelicanEntity)));
        }

        super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
*/

}

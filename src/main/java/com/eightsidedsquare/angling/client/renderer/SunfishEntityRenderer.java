package com.eightsidedsquare.angling.client.renderer;

import com.eightsidedsquare.angling.client.model.SunfishEntityModel;
import com.eightsidedsquare.angling.common.entity.SunfishEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class SunfishEntityRenderer extends GeoEntityRenderer<SunfishEntity> {
    public SunfishEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new SunfishEntityModel());
    }

    @Override
    public RenderLayer getRenderType(SunfishEntity entity, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
        return RenderLayer.getEntityTranslucent(getTextureResource(entity));
    }
}

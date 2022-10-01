package com.eightsidedsquare.angling.client.renderer;

import com.eightsidedsquare.angling.client.model.CrabEntityModel;
import com.eightsidedsquare.angling.common.entity.CrabEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class CrabEntityRenderer extends GeoEntityRenderer<CrabEntity> {
    public CrabEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new CrabEntityModel());
    }

    @Override
    public RenderLayer getRenderType(CrabEntity animatable, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
        return RenderLayer.getEntityTranslucent(getTextureResource(animatable));
    }
}

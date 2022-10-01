package com.eightsidedsquare.angling.client.renderer;

import com.eightsidedsquare.angling.client.model.DongfishEntityModel;
import com.eightsidedsquare.angling.common.entity.DongfishEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class DongfishEntityRenderer extends GeoEntityRenderer<DongfishEntity> {

    public DongfishEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new DongfishEntityModel());
    }

    @Override
    public RenderLayer getRenderType(DongfishEntity entity, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
        return RenderLayer.getEntityTranslucent(getTextureResource(entity));
    }
}

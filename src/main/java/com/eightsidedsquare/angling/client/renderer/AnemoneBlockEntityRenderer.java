package com.eightsidedsquare.angling.client.renderer;

import com.eightsidedsquare.angling.client.model.AnemoneBlockEntityModel;
import com.eightsidedsquare.angling.common.entity.AnemoneBlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class AnemoneBlockEntityRenderer extends GeoBlockRenderer<AnemoneBlockEntity> {

    public AnemoneBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        super(new AnemoneBlockEntityModel());
    }

    @Override
    public RenderLayer getRenderType(AnemoneBlockEntity animatable, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
        return RenderLayer.getEntityTranslucent(getTextureResource(animatable));
    }
}

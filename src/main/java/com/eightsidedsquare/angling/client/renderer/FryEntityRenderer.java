package com.eightsidedsquare.angling.client.renderer;

import com.eightsidedsquare.angling.client.model.FryEntityModel;
import com.eightsidedsquare.angling.common.entity.FryEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import static com.eightsidedsquare.angling.core.AnglingMod.MOD_ID;

public class FryEntityRenderer extends GeoEntityRenderer<FryEntity> {
    public FryEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new FryEntityModel());
        addLayer(new FryEntityLayerRenderer(this));
    }

    @Override
    public RenderLayer getRenderType(FryEntity animatable, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
        return RenderLayer.getEntityTranslucent(getTextureResource(animatable));
    }

     static class FryEntityLayerRenderer extends GeoLayerRenderer<FryEntity> {
         private static final Identifier OUTSIDE_LAYER = new Identifier(MOD_ID, "textures/entity/fry/fry.png");
         private static final Identifier INSIDE_LAYER = new Identifier(MOD_ID, "textures/entity/fry/fry_innards.png");
         private static final Identifier MODEL = new Identifier(MOD_ID, "geo/fry.geo.json");

        public FryEntityLayerRenderer(IGeoRenderer<FryEntity> entityRendererIn) {
            super(entityRendererIn);
        }

        private void render(Identifier layer, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn, int overlay, FryEntity entity, float partialTicks, float r, float g, float b) {
            RenderLayer renderLayer = RenderLayer.getEntityTranslucent(layer);
            matrixStackIn.push();
            this.getRenderer().render(this.getEntityModel().getModel(MODEL), entity, partialTicks, renderLayer, matrixStackIn, bufferIn,
                    bufferIn.getBuffer(renderLayer), packedLightIn, overlay, r, g, b, 1f);
            matrixStackIn.pop();
        }

        @Override
        public void render(MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn, FryEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            int color = entity.getColor();
            float r = ((color >> 16) & 0xFF) / 255f;
            float g = ((color >> 8) & 0xFF) / 255f;
            float b = (color & 0xFF) / 255f;
            int overlay = OverlayTexture.getUv(0,
                    entity.hurtTime > 0 || entity.deathTime > 0);
            render(INSIDE_LAYER, matrixStackIn, bufferIn, packedLightIn, overlay, entity, partialTicks, r, g, b);
            render(OUTSIDE_LAYER, matrixStackIn, bufferIn, packedLightIn, overlay, entity, partialTicks, 1f, 1f, 1f);
        }
    }
}

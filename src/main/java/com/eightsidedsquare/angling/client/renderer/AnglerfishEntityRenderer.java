package com.eightsidedsquare.angling.client.renderer;

import com.eightsidedsquare.angling.client.model.BasicEntityModel;
import com.eightsidedsquare.angling.common.entity.AnglerfishEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import static com.eightsidedsquare.angling.core.AnglingMod.MOD_ID;

public class AnglerfishEntityRenderer extends GeoEntityRenderer<AnglerfishEntity> {

    private static final Identifier OVERLAY = new Identifier(MOD_ID, "textures/entity/anglerfish/anglerfish_overlay.png");

    public AnglerfishEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new BasicEntityModel<>("anglerfish", true));
        addLayer(new AnglerfishLayerRenderer(this));
    }

    @Override
    public RenderLayer getRenderType(AnglerfishEntity entity, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
        return RenderLayer.getEntityTranslucent(getTextureResource(entity));
    }

    static class AnglerfishLayerRenderer extends GeoLayerRenderer<AnglerfishEntity> {

        public AnglerfishLayerRenderer(IGeoRenderer<AnglerfishEntity> entityRendererIn) {
            super(entityRendererIn);
        }

        public void render(int color, Identifier texture, boolean glow, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn, AnglerfishEntity entity, float partialTicks) {

            if(texture != null) {
                Identifier model = this.getEntityModel().getModelResource(entity);
                float r = ((color >> 16) & 0xff) / 255f;
                float g = ((color >> 8) & 0xff) / 255f;
                float b = (color & 0xff) / 255f;
                int overlay = OverlayTexture.getUv(0,
                        entity.hurtTime > 0 || entity.deathTime > 0);

                this.getRenderer().render(this.getEntityModel().getModel(model), entity, partialTicks, this.getRenderType(texture), matrixStackIn, bufferIn,
                        bufferIn.getBuffer(this.getRenderType(texture)), glow ? LightmapTextureManager.MAX_LIGHT_COORDINATE : packedLightIn, overlay, r, g, b, 1f);
            }
        }

        @Override
        public void render(MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn, AnglerfishEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

            int overlay = OverlayTexture.getUv(0,
                    entity.hurtTime > 0 || entity.deathTime > 0);

            this.getRenderer().render(this.getEntityModel().getModel(getEntityModel().getModelResource(entity)), entity, partialTicks, this.getRenderType(OVERLAY), matrixStackIn, bufferIn,
                    bufferIn.getBuffer(this.getRenderType(OVERLAY)), LightmapTextureManager.MAX_LIGHT_COORDINATE, overlay, 1, 1, 1, 1);
        }

        @Override
        public RenderLayer getRenderType(Identifier texture) {
            return RenderLayer.getEntityCutoutNoCull(texture);
        }
    }
}

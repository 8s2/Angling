package com.eightsidedsquare.angling.client.renderer;

import com.eightsidedsquare.angling.client.model.BasicEntityModel;
import com.eightsidedsquare.angling.common.entity.SeaSlugEntity;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

public class SeaSlugEntityRenderer extends GeoEntityRenderer<SeaSlugEntity> {
    public SeaSlugEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new BasicEntityModel<>("sea_slug", false));
        this.addLayer(new SeaSlugLayerRenderer(this));
        this.shadowRadius = 0.1f;
    }

    static class SeaSlugLayerRenderer extends GeoLayerRenderer<SeaSlugEntity> {

        public SeaSlugLayerRenderer(IGeoRenderer<SeaSlugEntity> entityRendererIn) {
            super(entityRendererIn);
        }

        public void render(int color, Identifier texture, boolean glow, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn, SeaSlugEntity entity, float partialTicks) {

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
        public void render(MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn, SeaSlugEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

            this.render(entity.getBaseColor().color(), this.getEntityTexture(entity), false, matrixStackIn, bufferIn, packedLightIn, entity, partialTicks);
            this.render(entity.getPatternColor().color(), entity.getPattern().texture(), entity.isBioluminescent(), matrixStackIn, bufferIn, packedLightIn, entity, partialTicks);
        }

        @Override
        public RenderLayer getRenderType(Identifier texture) {
            return RenderLayer.getEntityCutoutNoCull(texture);
        }
    }
}

package com.eightsidedsquare.angling.client.renderer;

import com.eightsidedsquare.angling.client.model.BasicEntityModel;
import com.eightsidedsquare.angling.common.entity.AnglerfishEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import static com.eightsidedsquare.angling.core.AnglingMod.MOD_ID;

public class AnglerfishEntityRenderer extends GeoEntityRenderer<AnglerfishEntity> {

    private static final Identifier OVERLAY = new Identifier(MOD_ID, "textures/entity/anglerfish/anglerfish_overlay.png");

    public AnglerfishEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new BasicEntityModel<>("anglerfish", true));
        addRenderLayer(new AnglerfishLayerRenderer(this));
    }

    @Override
    public RenderLayer getRenderType(AnglerfishEntity animatable, Identifier texture, VertexConsumerProvider bufferSource, float partialTick) {
        return RenderLayer.getEntityTranslucent(getTextureLocation(animatable));
    }

    static class AnglerfishLayerRenderer extends GeoRenderLayer<AnglerfishEntity> {

        public AnglerfishLayerRenderer(GeoRenderer<AnglerfishEntity> entityRendererIn) {
            super(entityRendererIn);
        }

        @Override
        public void render(MatrixStack poseStack, AnglerfishEntity animatable, BakedGeoModel bakedModel, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {

            int overlay = OverlayTexture.getUv(0,
                    animatable.hurtTime > 0 || animatable.deathTime > 0);

            super.render(poseStack, animatable, bakedModel, renderType, bufferSource, buffer, partialTick, packedLight, overlay);
        }
    }
}

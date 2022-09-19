package com.eightsidedsquare.angling.client.renderer;

import com.eightsidedsquare.angling.client.model.StarfishBlockEntityModel;
import com.eightsidedsquare.angling.common.entity.StarfishBlockEntity;
import com.eightsidedsquare.angling.core.AnglingBlocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class StarfishBlockEntityRenderer extends GeoBlockRenderer<StarfishBlockEntity> {
    private final AnimatedGeoModel<StarfishBlockEntity> modelProvider;

    public StarfishBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        super(new StarfishBlockEntityModel());
        this.modelProvider = new StarfishBlockEntityModel();
    }

    @Override
    public RenderLayer getRenderType(StarfishBlockEntity animatable, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
        return RenderLayer.getEntityTranslucent(getTextureResource(animatable));
    }

    @Override
    public Color getRenderColor(StarfishBlockEntity entity, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn) {
        return entity.getCachedState().isOf(AnglingBlocks.DEAD_STARFISH) ? Color.WHITE : Color.ofOpaque(entity.isRainbow() ? StarfishBlockEntity.getRainbowColor() : entity.getColor());
    }

    @Override
    public void render(StarfishBlockEntity entity, float partialTicks, MatrixStack stack, VertexConsumerProvider bufferIn,
                       int packedLightIn) {
        if(!entity.isRemoved()){
            GeoModel model = modelProvider.getModel(modelProvider.getModelResource(entity));
            modelProvider.setLivingAnimations(entity, this.getUniqueID(entity));
            stack.push();
            stack.translate(0.5, 0.01, 0.5);

            MinecraftClient.getInstance().getTextureManager().bindTexture(getTextureResource(entity));
            Color renderColor = getRenderColor(entity, partialTicks, stack, bufferIn, null, packedLightIn);
            RenderLayer renderType = getRenderType(entity, partialTicks, stack, bufferIn, null, packedLightIn,
                    getTextureResource(entity));
            render(model, entity, partialTicks, renderType, stack, bufferIn, null, packedLightIn, OverlayTexture.DEFAULT_UV,
                    (float) renderColor.getRed() / 255f, (float) renderColor.getGreen() / 255f,
                    (float) renderColor.getBlue() / 255f, (float) renderColor.getAlpha() / 255);
            stack.pop();
        }
    }
}

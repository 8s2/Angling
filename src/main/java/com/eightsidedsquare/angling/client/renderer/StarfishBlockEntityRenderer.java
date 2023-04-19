package com.eightsidedsquare.angling.client.renderer;

import com.eightsidedsquare.angling.client.model.StarfishBlockEntityModel;
import com.eightsidedsquare.angling.common.entity.StarfishBlockEntity;
import com.eightsidedsquare.angling.core.AnglingBlocks;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.core.object.Color;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class StarfishBlockEntityRenderer extends GeoBlockRenderer<StarfishBlockEntity> {
    private final GeoModel<StarfishBlockEntity> modelProvider;

    public StarfishBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        super(new StarfishBlockEntityModel());
        this.modelProvider = new StarfishBlockEntityModel();
    }
    @Override
    public RenderLayer getRenderType(StarfishBlockEntity animatable, Identifier texture, VertexConsumerProvider bufferSource, float partialTick) {
        return RenderLayer.getEntityTranslucent(getTextureLocation(animatable));
    }

    @Override
    public Color getRenderColor(StarfishBlockEntity animatable, float partialTick, int packedLight) {
        return animatable.getCachedState().isOf(AnglingBlocks.DEAD_STARFISH) ? Color.WHITE : Color.ofOpaque(animatable.isRainbow() ? StarfishBlockEntity.getRainbowColor() : animatable.getColor());
    }

    /*

    @Override
    public void render(StarfishBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if(!entity.isRemoved()){
            BakedGeoModel model = modelProvider.getBakedModel(modelProvider.getModelResource(entity));
            // modelProvider.setCustomAnimations(entity, this.getInstanceId(entity));
            matrices.push();
            matrices.translate(0.5, 0.01, 0.5);

            MinecraftClient.getInstance().getTextureManager().bindTexture(getTextureLocation(entity));
            Color renderColor = getRenderColor(entity, tickDelta, light);
            RenderLayer renderType = getRenderType(entity, getTextureLocation(entity), vertexConsumers, tickDelta);

            matrices.pop();
        }
    }


     */

}

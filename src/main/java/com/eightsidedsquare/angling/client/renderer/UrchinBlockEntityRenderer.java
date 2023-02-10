package com.eightsidedsquare.angling.client.renderer;

import com.eightsidedsquare.angling.client.model.UrchinBlockEntityModel;
import com.eightsidedsquare.angling.common.entity.UrchinBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class UrchinBlockEntityRenderer extends GeoBlockRenderer<UrchinBlockEntity> {

    private UrchinBlockEntity entity;
    private VertexConsumerProvider vertexConsumerProvider;

    public UrchinBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        super(new UrchinBlockEntityModel());
    }

    @Override
    public RenderLayer getRenderType(UrchinBlockEntity entity, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
        return RenderLayer.getEntityTranslucent(getTextureResource(entity));
    }

    @Override
    public void renderEarly(UrchinBlockEntity entity, MatrixStack stackIn, float partialTicks, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.entity = entity;
        this.vertexConsumerProvider = renderTypeBuffer;
        super.renderEarly(entity, stackIn, partialTicks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    @Override
    public void renderRecursively(GeoBone bone, MatrixStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if(bone.getName().equals("root")) {
            ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
            stack.push();
            stack.multiply(Quaternion.fromEulerXyz(new Vec3f(bone.getRotationX(), bone.getRotationY(), bone.getRotationZ())));
            stack.translate(0f, 0.625f, 0f);
            stack.scale(0.5f, 0.5f, 0.5f);
            itemRenderer.renderItem(entity.getHat(), ModelTransformation.Mode.FIXED, packedLightIn, OverlayTexture.DEFAULT_UV, stack, vertexConsumerProvider, 0);
            stack.pop();
            bufferIn = vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucent(getTextureResource(entity)));
        }
        super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}

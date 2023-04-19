package com.eightsidedsquare.angling.client.renderer;

import com.eightsidedsquare.angling.client.model.UrchinBlockEntityModel;
import com.eightsidedsquare.angling.common.entity.StarfishBlockEntity;
import com.eightsidedsquare.angling.common.entity.UrchinBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.joml.Quaternionf;
import org.joml.QuaternionfInterpolator;
import org.joml.Vector3f;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class UrchinBlockEntityRenderer extends GeoBlockRenderer<UrchinBlockEntity> {

    private UrchinBlockEntity entity;
    private VertexConsumerProvider vertexConsumerProvider;

    public UrchinBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        super(new UrchinBlockEntityModel());
    }

    @Override
    public RenderLayer getRenderType(UrchinBlockEntity animatable, Identifier texture, VertexConsumerProvider bufferSource, float partialTick) {
        return RenderLayer.getEntityTranslucent(getTextureLocation(animatable));
    }

    @Override
    public void preRender(MatrixStack poseStack, UrchinBlockEntity animatable, BakedGeoModel model, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.entity = animatable;
        this.vertexConsumerProvider = vertexConsumerProvider;
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void renderRecursively(MatrixStack poseStack, UrchinBlockEntity animatable, GeoBone bone, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if(bone.getName().equals("root")) {
            ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
            poseStack.push();
            // poseStack.multiply(Quaternion.fromEulerXyz(new Vector3f(bone.getRotX(), bone.getRotY(), bone.getRotZ()))); TODO: Fix this
            poseStack.translate(0f, 0.625f, 0f);
            poseStack.scale(0.5f, 0.5f, 0.5f);
            itemRenderer.renderItem(entity.getHat(), ModelTransformationMode.FIXED, packedLight, OverlayTexture.DEFAULT_UV, poseStack, vertexConsumerProvider, null,0);
            poseStack.pop();
            buffer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucent(getTextureLocation(entity)));
        }
        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}

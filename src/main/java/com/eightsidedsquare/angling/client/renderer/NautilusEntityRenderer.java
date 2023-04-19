package com.eightsidedsquare.angling.client.renderer;

import com.eightsidedsquare.angling.client.model.NautilusEntityModel;
import com.eightsidedsquare.angling.common.entity.NautilusEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import software.bernie.geckolib.model.GeoModel;

public class NautilusEntityRenderer extends BasicEntityRenderer<NautilusEntity> {
    public NautilusEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new NautilusEntityModel());
    }

    /*
    @Override
    public void render(GeoModel model, NautilusEntity animatable, float partialTicks, RenderLayer type, MatrixStack matrices, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        matrices.push();
        matrices.scale(0.65f, 0.65f, 0.65f);
        super.render(model, animatable, partialTicks, type, matrices, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        matrices.pop();
    }


     */
}

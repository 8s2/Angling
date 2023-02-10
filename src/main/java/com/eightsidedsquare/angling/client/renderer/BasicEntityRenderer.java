package com.eightsidedsquare.angling.client.renderer;

import com.eightsidedsquare.angling.client.model.BasicEntityModel;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class BasicEntityRenderer<A extends LivingEntity & IAnimatable> extends GeoEntityRenderer<A> {

    public BasicEntityRenderer(EntityRendererFactory.Context ctx, AnimatedGeoModel<A> modelProvider) {
        super(ctx, modelProvider);
    }

    public static <A extends LivingEntity & IAnimatable> EntityRendererFactory<A> create(AnimatedGeoModel<A> model) {
        return ctx -> new BasicEntityRenderer<>(ctx, model);
    }

    public static <A extends LivingEntity & IAnimatable> EntityRendererFactory<A> create(String name, boolean liesOutOfWater) {
        return ctx -> new BasicEntityRenderer<>(ctx, new BasicEntityModel<>(name, liesOutOfWater));
    }

    public static <A extends LivingEntity & IAnimatable> EntityRendererFactory<A> create(String name, boolean liesOutOfWater, String head) {
        return ctx -> new BasicEntityRenderer<>(ctx, new BasicEntityModel<>(name, liesOutOfWater, head));
    }

    @Override
    public RenderLayer getRenderType(A entity, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
        return RenderLayer.getEntityTranslucent(getTextureResource(entity));
    }
}

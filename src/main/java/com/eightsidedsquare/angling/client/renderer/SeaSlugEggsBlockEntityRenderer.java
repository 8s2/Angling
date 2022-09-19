package com.eightsidedsquare.angling.client.renderer;

import com.eightsidedsquare.angling.common.entity.SeaSlugEggsBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;

public class SeaSlugEggsBlockEntityRenderer implements BlockEntityRenderer<SeaSlugEggsBlockEntity> {

    private final BlockRenderManager blockRenderManager;

    public SeaSlugEggsBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        blockRenderManager = ctx.getRenderManager();
    }

    @Override
    public void render(SeaSlugEggsBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        BlockState state = entity.getCachedState();
        if(!entity.isRemoved() && entity.getWorld() != null)
            blockRenderManager.getModelRenderer().render(entity.getWorld(), blockRenderManager.getModel(state), state, entity.getPos(), matrices, vertexConsumers.getBuffer(RenderLayer.getCutout()), false, entity.getWorld().random, 0, 0);
    }
}

package com.eightsidedsquare.angling.client.renderer;

import com.eightsidedsquare.angling.common.entity.RoeBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;

public class RoeBlockEntityRenderer implements BlockEntityRenderer<RoeBlockEntity> {

    private final BlockRenderManager blockRenderManager;

    public RoeBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        blockRenderManager = ctx.getRenderManager();
    }

    @Override
    public void render(RoeBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        BlockState state = entity.getCachedState();
        if(!entity.isRemoved() && entity.getWorld() != null)
            blockRenderManager.getModelRenderer().render(entity.getWorld(), blockRenderManager.getModel(state), state, entity.getPos(), matrices, vertexConsumers.getBuffer(RenderLayer.getCutout()), false, entity.getWorld().random, 0, 0);
    }
}

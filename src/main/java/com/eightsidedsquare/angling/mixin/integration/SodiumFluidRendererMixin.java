package com.eightsidedsquare.angling.mixin.integration;

import me.jellysquid.mods.sodium.client.render.pipeline.FluidRenderer;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBlockTags;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = FluidRenderer.class, remap = false)
public abstract class SodiumFluidRendererMixin {

    @Shadow @Final private BlockPos.Mutable scratchPos;

    @Inject(method = "isFluidOccluded", at = @At("RETURN"), cancellable = true)
    private void isFluidOccluded(BlockRenderView world, int x, int y, int z, Direction dir, Fluid fluid, CallbackInfoReturnable<Boolean> cir) {
        if(!cir.getReturnValue()) {
            BlockState state = world.getBlockState(new BlockPos(scratchPos).offset(dir.getOpposite()));
            BlockState sideState = world.getBlockState(scratchPos);
            if(state.getFluidState().isIn(FluidTags.WATER) && sideState.isIn(ConventionalBlockTags.GLASS_BLOCKS)) {
                cir.setReturnValue(true);
            }
        }
    }

}

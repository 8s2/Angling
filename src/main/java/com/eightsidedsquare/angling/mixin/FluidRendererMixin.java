package com.eightsidedsquare.angling.mixin;

import com.eightsidedsquare.angling.core.AnglingMod;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBlockTags;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.block.FluidRenderer;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FluidRenderer.class)
public abstract class FluidRendererMixin {

    @Inject(method = "shouldRenderSide", at = @At("HEAD"), cancellable = true)
    private static void shouldRenderSide(BlockRenderView world, BlockPos pos, FluidState fluidState, BlockState blockState, Direction direction, FluidState neighborFluidState, CallbackInfoReturnable<Boolean> cir) {
        if(AnglingMod.CONFIG.hideWaterBehindGlass && fluidState.isIn(FluidTags.WATER)) {
            if(world.getBlockState(pos.offset(direction)).isIn(ConventionalBlockTags.GLASS_BLOCKS)) {
                cir.setReturnValue(false);
            }
        }
    }

}

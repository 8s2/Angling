package com.eightsidedsquare.angling.client;

import com.eightsidedsquare.angling.client.particle.AlgaeParticle;
import com.eightsidedsquare.angling.client.particle.WormParticle;
import com.eightsidedsquare.angling.client.renderer.*;
import com.eightsidedsquare.angling.common.entity.RoeBlockEntity;
import com.eightsidedsquare.angling.common.entity.SeaSlugEggsBlockEntity;
import com.eightsidedsquare.angling.common.entity.StarfishBlockEntity;
import com.eightsidedsquare.angling.common.entity.util.SeaSlugColor;
import com.eightsidedsquare.angling.common.entity.util.SeaSlugPattern;
import com.eightsidedsquare.angling.core.AnglingBlocks;
import com.eightsidedsquare.angling.core.AnglingEntities;
import com.eightsidedsquare.angling.core.AnglingItems;
import com.eightsidedsquare.angling.core.AnglingParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TropicalFishEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.util.Color;

import static com.eightsidedsquare.angling.core.AnglingMod.MOD_ID;

public class AnglingClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), AnglingBlocks.ROE, AnglingBlocks.DUCKWEED, AnglingBlocks.OYSTERS, AnglingBlocks.CLAM, AnglingBlocks.SEA_SLUG_EGGS);
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getTranslucent(), AnglingBlocks.ALGAE);

        EntityRendererRegistry.register(AnglingEntities.FRY, FryEntityRenderer::new);
        EntityRendererRegistry.register(AnglingEntities.SUNFISH, SunfishEntityRenderer::new);
        EntityRendererRegistry.register(AnglingEntities.PELICAN, PelicanEntityRenderer::new);
        EntityRendererRegistry.register(AnglingEntities.NAUTILUS, NautilusEntityRenderer::new);
        EntityRendererRegistry.register(AnglingEntities.SEA_SLUG, SeaSlugEntityRenderer::new);
        EntityRendererRegistry.register(AnglingEntities.CRAB, CrabEntityRenderer::new);
        EntityRendererRegistry.register(AnglingEntities.DONGFISH, DongfishEntityRenderer::new);

        BlockEntityRendererRegistry.register(AnglingEntities.STARFISH, StarfishBlockEntityRenderer::new);
        if(FabricLoader.getInstance().isModLoaded("sodium")) {
            BlockEntityRendererRegistry.register(AnglingEntities.ROE, RoeBlockEntityRenderer::new);
            BlockEntityRendererRegistry.register(AnglingEntities.SEA_SLUG_EGGS, SeaSlugEggsBlockEntityRenderer::new);
        }

        ColorProviderRegistry.BLOCK.register(RoeBlockEntity::getColor, AnglingBlocks.ROE);
        ColorProviderRegistry.BLOCK.register(SeaSlugEggsBlockEntity::getColor, AnglingBlocks.SEA_SLUG_EGGS);
        ColorProviderRegistry.BLOCK.register(StarfishBlockEntity::getColor, AnglingBlocks.STARFISH);

        ColorProviderRegistry.ITEM.register(RoeBlockEntity::getItemColor, AnglingItems.ROE);
        ColorProviderRegistry.ITEM.register(SeaSlugEggsBlockEntity::getItemColor, AnglingItems.SEA_SLUG_EGGS);
        ColorProviderRegistry.ITEM.register(StarfishBlockEntity::getItemColor, AnglingBlocks.STARFISH.asItem());
        ColorProviderRegistry.ITEM.register(this::getTropicalFishBucketColor, Items.TROPICAL_FISH_BUCKET);
        ColorProviderRegistry.ITEM.register(this::getFryBucketColor, AnglingItems.FRY_BUCKET);
        ColorProviderRegistry.ITEM.register(this::getSeaSlugBucketColor, AnglingItems.SEA_SLUG_BUCKET);

        ParticleFactoryRegistry.getInstance().register(AnglingParticles.ALGAE, AlgaeParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(AnglingParticles.WORM, WormParticle.Factory::new);

        ModelPredicateProviderRegistry.register(AnglingItems.DONGFISH_BUCKET, new Identifier(MOD_ID, "has_horngus"), this::dongfishBucketItemHasHorngus);


    }

    private float dongfishBucketItemHasHorngus(ItemStack stack, ClientWorld clientWorld, LivingEntity livingEntity, int i) {
        NbtCompound nbt = stack.getOrCreateNbt();
        return !nbt.contains("HasHorngus") || nbt.getBoolean("HasHorngus") ? 1 : 0;

    }

    private int getFryBucketColor(ItemStack stack, int i) {
        if(i == 0)
            return 0xffffff;
        NbtCompound nbt = stack.getOrCreateNbt();
        if(nbt.contains("Color"))
            return stack.getOrCreateNbt().getInt("Color");
        return 0xffffff;
    }

    private int getTropicalFishBucketColor(ItemStack stack, int i) {
        NbtCompound nbt = stack.getOrCreateNbt();
        if(i == 0)
            return 0xffffff;
        float[] colorComponents = (i == 1 ? DyeColor.WHITE : DyeColor.ORANGE).getColorComponents();
        if(nbt != null && nbt.contains("BucketVariantTag")) {
            int variant = nbt.getInt("BucketVariantTag");
            colorComponents = (i == 1 ? TropicalFishEntity.getPatternDyeColor(variant) : TropicalFishEntity.getBaseDyeColor(variant)).getColorComponents();
        }
        return Color.ofRGB(colorComponents[0], colorComponents[1], colorComponents[2]).getColor();
    }

    private int getSeaSlugBucketColor(ItemStack stack, int i) {
        NbtCompound nbt = stack.getOrCreateNbt();
        if(i == 0)
            return 0xffffff;
        else if(i == 1 || (nbt.contains("Pattern", NbtElement.STRING_TYPE) && SeaSlugPattern.fromId(nbt.getString("Pattern")).equals(SeaSlugPattern.NONE)))
            return nbt.contains("BaseColor", NbtElement.STRING_TYPE) ? SeaSlugColor.fromId(nbt.getString("BaseColor")).color() : 0x6f4e37;
        return nbt.contains("PatternColor", NbtElement.STRING_TYPE) ? SeaSlugColor.fromId(nbt.getString("PatternColor")).color() : 0xff3800;

    }
}

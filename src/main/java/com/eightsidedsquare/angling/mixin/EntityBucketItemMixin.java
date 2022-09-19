package com.eightsidedsquare.angling.mixin;

import com.eightsidedsquare.angling.common.entity.util.CrabVariant;
import com.eightsidedsquare.angling.common.entity.util.SeaSlugColor;
import com.eightsidedsquare.angling.common.entity.util.SeaSlugPattern;
import com.eightsidedsquare.angling.common.entity.util.SunfishVariant;
import com.eightsidedsquare.angling.core.AnglingEntities;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityType;
import net.minecraft.item.EntityBucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(EntityBucketItem.class)
public abstract class EntityBucketItemMixin {

    @Shadow @Final
    public EntityType<?> entityType;

    @Inject(method = "appendTooltip", at = @At("HEAD"))
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context, CallbackInfo ci) {
        NbtCompound nbt = stack.getNbt();
        Formatting[] formatting = new Formatting[]{Formatting.ITALIC, Formatting.GRAY};
        if(nbt != null) {
            if (entityType.equals(AnglingEntities.SUNFISH)) {
                if (nbt.contains("BucketVariantTag", NbtElement.STRING_TYPE)) {
                    SunfishVariant variant = SunfishVariant.fromId(nbt.getString("BucketVariantTag"));
                    tooltip.add(Text.translatable(variant.getTranslationKey()).formatted(formatting));
                }
            }else if(entityType.equals(AnglingEntities.SEA_SLUG)) {
                if (nbt.contains("Pattern", NbtElement.STRING_TYPE)) {
                    MutableText text = Text.translatable(SeaSlugColor.fromId(nbt.getString("BaseColor")).getTranslationKey());
                    if (!SeaSlugPattern.fromId(nbt.getString("Pattern")).equals(SeaSlugPattern.NONE)) {
                        tooltip.add(Text.translatable(SeaSlugPattern.fromId(nbt.getString("Pattern")).getTranslationKey()).formatted(formatting));
                        if (!nbt.getString("BaseColor").equals(nbt.getString("PatternColor")))
                            text.append(", ").append(Text.translatable(SeaSlugColor.fromId(nbt.getString("PatternColor")).getTranslationKey()));
                    }
                    tooltip.add(text.formatted(formatting));
                    if (nbt.getBoolean("Bioluminescent"))
                        tooltip.add(Text.translatable("item.angling.sea_slug_bucket.bioluminescent").formatted(formatting));
                }
            }else if(entityType.equals(AnglingEntities.CRAB)) {
                if(nbt.contains("BucketVariantTag", NbtElement.STRING_TYPE)) {
                    CrabVariant variant = CrabVariant.fromId(nbt.getString("BucketVariantTag"));
                    tooltip.add(Text.translatable(variant.getTranslationKey()).formatted(formatting));
                }
            }
        }
    }
}

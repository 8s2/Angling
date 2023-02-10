package com.eightsidedsquare.angling.common.item;

import com.eightsidedsquare.angling.core.AnglingEntities;
import com.eightsidedsquare.angling.core.AnglingItems;
import com.eightsidedsquare.angling.core.tags.AnglingEntityTypeTags;
import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class RoeBlockItem extends BlockItem {

    public RoeBlockItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        NbtCompound nbt = getBlockEntityNbt(stack);
        if(nbt != null && nbt.contains("EntityType", NbtElement.STRING_TYPE)) {
            String key = Registry.ENTITY_TYPE.get(Identifier.tryParse(nbt.getString("EntityType"))).getTranslationKey();
            tooltip.add(Text.translatable(key).formatted(Formatting.GRAY));
        }
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        if(this.isIn(group)) {
            Registry.ENTITY_TYPE.stream()
                    .filter(type -> type.isIn(AnglingEntityTypeTags.SPAWNING_FISH))
                    .map(SpawnEggItem::forEntity)
                    .filter(Objects::nonNull)
                    .forEach(egg -> {
                        ItemStack stack = new ItemStack(AnglingItems.ROE);
                        NbtCompound nbt = new NbtCompound();
                        nbt.putInt("PrimaryColor", egg.getColor(0));
                        nbt.putInt("SecondaryColor", egg.getColor(0));
                        nbt.putString("EntityType", Registry.ENTITY_TYPE.getId(egg.getEntityType(null)).toString());
                        setBlockEntityNbt(stack, AnglingEntities.ROE, nbt);
                        stacks.add(stack);
                    });
        }
    }
}

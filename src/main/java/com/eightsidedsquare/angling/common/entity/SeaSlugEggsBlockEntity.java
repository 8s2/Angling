package com.eightsidedsquare.angling.common.entity;

import com.eightsidedsquare.angling.common.entity.util.SeaSlugColor;
import com.eightsidedsquare.angling.common.entity.util.SeaSlugPattern;
import com.eightsidedsquare.angling.core.AnglingEntities;
import com.eightsidedsquare.angling.core.AnglingSounds;
import com.eightsidedsquare.angling.core.AnglingUtil;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachedBlockView;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachmentBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

public class SeaSlugEggsBlockEntity extends BlockEntity implements RenderAttachmentBlockEntity {

    int color;
    NbtCompound parentData;
    NbtCompound mateData;

    public SeaSlugEggsBlockEntity(BlockPos pos, BlockState state) {
        super(AnglingEntities.SEA_SLUG_EGGS, pos, state);
        setColor(SeaSlugColor.IVORY);
        setParentsData(new NbtCompound(), new NbtCompound());
    }

    public void setParentsData(NbtCompound parentData, NbtCompound mateData) {
        this.parentData = parentData;
        this.mateData = mateData;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setColor(SeaSlugColor color) {
        this.color = color.color();
    }

    public static int getColor(BlockState state, @Nullable BlockRenderView world, BlockPos pos, int tintIndex) {
        if(world != null && ((RenderAttachedBlockView) world).getBlockEntityRenderAttachment(pos) instanceof SeaSlugEggsBlockEntity entity) {
            return entity.color;
        }
        return 0xffffff;
    }

    public void readFrom(ItemStack stack) {
        NbtCompound nbt = BlockItem.getBlockEntityNbt(stack);
        if(nbt != null) {
            setColor(nbt.getInt("Color"));
            if(nbt.contains("ParentData", NbtElement.COMPOUND_TYPE) && nbt.contains("MateData", NbtElement.COMPOUND_TYPE))
                setParentsData(nbt.getCompound("ParentData"), nbt.getCompound("MateData"));
        }
    }

    public static int getItemColor(ItemStack stack, int tintIndex) {
        NbtCompound nbt = BlockItem.getBlockEntityNbt(stack);
        if(nbt != null) {
            return nbt.getInt("Color");
        }
        return 0xffffff;
    }

    private SeaSlugColor pickColor(ServerWorld world, Random random, boolean base) {
        String key = base ? "BaseColor" : "PatternColor";
        if(random.nextInt(16) == 0) {
            TagKey<SeaSlugColor> tag = base ? SeaSlugColor.Tag.BASE_COLORS : SeaSlugColor.Tag.PATTERN_COLORS;
            return AnglingUtil.getRandomTagValue(world, tag, random);
        }
        return SeaSlugColor.fromId((random.nextBoolean() ? parentData : mateData).getString(key));
    }

    private SeaSlugPattern pickPattern(ServerWorld world, Random random) {
        if(random.nextInt(16) == 0) {
            return AnglingUtil.getRandomTagValue(world, SeaSlugPattern.Tag.NATURAL_PATTERNS, random);
        }
        return SeaSlugPattern.fromId((random.nextBoolean() ? parentData : mateData).getString("Pattern"));
    }

    public void hatch(ServerWorld world) {
        Random random = world.getRandom();
        int count = random.nextBetween(1, 3);
        if(mateData != null && parentData != null) {
            for (int i = 0; i < count; i++) {
                SeaSlugEntity entity = AnglingEntities.SEA_SLUG.create(world);
                if (entity != null) {
                    entity.setBaseColor(pickColor(world, random, true));
                    entity.setPatternColor(pickColor(world, random, false));
                    entity.setPattern(pickPattern(world, random));
                    entity.setBioluminescent((random.nextBoolean() ? parentData : mateData).getBoolean("Bioluminescent"));
                    entity.setLoveCooldown(3000);
                    entity.setBodyYaw(random.nextBetween(-180, 180));
                    entity.setHeadYaw(entity.bodyYaw);
                    entity.setPos(pos.getX() + 0.5d, pos.getY() + 0.1d, pos.getZ() + 0.5d);
                    world.spawnEntity(entity);
                }
            }
        }
        world.setBlockState(pos, Blocks.WATER.getDefaultState());
        world.playSound(null, pos, AnglingSounds.BLOCK_SEA_SLUG_EGGS_HATCH, SoundCategory.BLOCKS, 1, 1);
        world.spawnParticles(new BlockStateParticleEffect(ParticleTypes.BLOCK, getCachedState()), pos.getX() + 0.5d, pos.getY(), pos.getZ() + 0.5d, 20, 0.25d, 0.05d, 0.25d, 0);
    }

    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("Color", color);
        nbt.put("ParentData", parentData);
        nbt.put("MateData", mateData);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        color = nbt.getInt("Color");
        if(nbt.contains("ParentData", NbtElement.COMPOUND_TYPE) && nbt.contains("MateData", NbtElement.COMPOUND_TYPE))
            setParentsData(nbt.getCompound("ParentData"), nbt.getCompound("MateData"));
    }

    @Override
    public @Nullable Object getRenderAttachmentData() {
        return this;
    }
}

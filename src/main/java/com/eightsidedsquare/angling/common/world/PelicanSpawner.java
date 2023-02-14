package com.eightsidedsquare.angling.common.world;

import com.eightsidedsquare.angling.common.entity.PelicanEntity;
import com.eightsidedsquare.angling.core.AnglingEntities;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EntityBucketItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.GameRules;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.spawner.Spawner;

import java.util.List;

public class PelicanSpawner implements Spawner {

    private int cooldown;
    private static final int RADIUS = 32;

    @Override
    public int spawn(ServerWorld world, boolean spawnMonsters, boolean spawnAnimals) {
        if(spawnAnimals && world.getGameRules().getBoolean(GameRules.DO_MOB_SPAWNING) && --cooldown <= 0) {
            cooldown = 2400;
            Random random = world.random;
            if(world.isDay() && world.getDimension().hasSkyLight() && random.nextInt(16) == 0) {
                int total = 0;
                List<ServerPlayerEntity> players = world.getPlayers(p -> !p.isSpectator());
                PelicanEntity entity;
                for(ServerPlayerEntity player : players) {
                    BlockPos spawnPos = player.getBlockPos().add(random.nextBetween(-RADIUS, RADIUS), random.nextBetween(20, 35), random.nextBetween(-RADIUS, RADIUS));
                    if(world.isSkyVisible(spawnPos) &&
                            SpawnHelper.isClearForSpawn(world, spawnPos, world.getBlockState(spawnPos), world.getFluidState(spawnPos), AnglingEntities.PELICAN) &&
                            playerHasEntityBucketItem(player) &&
                            (entity = AnglingEntities.PELICAN.create(world)) != null) {
                        entity.refreshPositionAndAngles(spawnPos, 0, 0);
                        entity.initialize(world, world.getLocalDifficulty(spawnPos), SpawnReason.NATURAL, null, null);
                        world.spawnEntityAndPassengers(entity);
                        total++;
                    }
                }
                return total;
            }
        }
        return 0;
    }

    private boolean playerHasEntityBucketItem(PlayerEntity player) {
        for(int i = 0; i < player.getInventory().size(); i++) {
            if(player.getInventory().getStack(i).getItem() instanceof EntityBucketItem)
                return true;
        }
        return false;
    }
}

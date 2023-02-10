package com.eightsidedsquare.angling.client.particle;

import net.minecraft.block.Block;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.*;

public class WormParticle extends SpriteBillboardParticle {

    private final SpriteProvider spriteProvider;

    protected WormParticle(ClientWorld clientWorld, double x, double y, double z, SpriteProvider spriteProvider) {
        super(clientWorld, x, y, z);
        this.spriteProvider = spriteProvider;
        this.scale = 0.3f;
        setMaxAge(200);
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        Vec3d vec3d = camera.getPos();
        float currentX = (float)(MathHelper.lerp(tickDelta, this.prevPosX, this.x) - vec3d.getX());
        float currentY = (float)(MathHelper.lerp(tickDelta, this.prevPosY, this.y) - vec3d.getY());
        float currentZ = (float)(MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - vec3d.getZ());
        Quaternion quaternion = Vec3f.POSITIVE_Y.getDegreesQuaternion(-camera.getYaw());
        Quaternion flip = Vec3f.POSITIVE_Y.getDegreesQuaternion(180 - camera.getYaw());

        float size = this.getSize(tickDelta);
        float minU = this.getMinU();
        float maxU = this.getMaxU();
        float minV = this.getMinV();
        float maxV = this.getMaxV();
        int light = this.getBrightness(tickDelta);

        renderFace(vertexConsumer, quaternion, size, currentX, currentY, currentZ, minU, maxU, minV, maxV, light);
        renderFace(vertexConsumer, flip, size, currentX, currentY, currentZ, minU, maxU, minV, maxV, light);
    }

    private void renderFace(VertexConsumer vertexConsumer, Quaternion quaternion, float size, float x, float y, float z, float minU, float maxU, float minV, float maxV, int light) {
        Vec3f[] vec3fs = new Vec3f[]{new Vec3f(-1.0F, -1.0F, 0.0F), new Vec3f(-1.0F, 1.0F, 0.0F), new Vec3f(1.0F, 1.0F, 0.0F), new Vec3f(1.0F, -1.0F, 0.0F)};

        for(int k = 0; k < 4; ++k) {
            Vec3f vec3f2 = vec3fs[k];
            vec3f2.rotate(quaternion);
            vec3f2.scale(size);
            vec3f2.add(x, y, z);
        }
        vertexConsumer.vertex(vec3fs[0].getX(), vec3fs[0].getY(), vec3fs[0].getZ()).texture(maxU, maxV).color(this.red, this.green, this.blue, this.alpha).light(light).next();
        vertexConsumer.vertex(vec3fs[1].getX(), vec3fs[1].getY(), vec3fs[1].getZ()).texture(maxU, minV).color(this.red, this.green, this.blue, this.alpha).light(light).next();
        vertexConsumer.vertex(vec3fs[2].getX(), vec3fs[2].getY(), vec3fs[2].getZ()).texture(minU, minV).color(this.red, this.green, this.blue, this.alpha).light(light).next();
        vertexConsumer.vertex(vec3fs[3].getX(), vec3fs[3].getY(), vec3fs[3].getZ()).texture(minU, maxV).color(this.red, this.green, this.blue, this.alpha).light(light).next();
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        if(age <= 10) {
            setVelocity(0, 0.023f, 0);
        }else if(age >= maxAge - 10) {
            setVelocity(0, -0.023f, 0);
        }else {
            setVelocity(0, 0, 0);
        }
        super.tick();
        if(!Block.isFaceFullSquare(world.getBlockState(new BlockPos(x, y - 0.5d, z)).getSidesShape(world, new BlockPos(x, y - 0.5d, z)), Direction.UP)) {
            markDead();
        }
        setSpriteForAge(this.spriteProvider);
    }

    public record Factory(
            SpriteProvider spriteProvider) implements ParticleFactory<DefaultParticleType> {

        @Override
        public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            WormParticle particle = new WormParticle(world, x, y, z, this.spriteProvider);
            particle.setSprite(this.spriteProvider);
            return particle;
        }
    }
}

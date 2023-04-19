package com.eightsidedsquare.angling.client.particle;

import net.minecraft.block.Block;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3f;

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
        Quaternionf quaternion = RotationAxis.POSITIVE_Y.rotationDegrees(-camera.getYaw());
        Quaternionf flip = RotationAxis.POSITIVE_Y.rotationDegrees(180 - camera.getYaw());

        float size = this.getSize(tickDelta);
        float minU = this.getMinU();
        float maxU = this.getMaxU();
        float minV = this.getMinV();
        float maxV = this.getMaxV();
        int light = this.getBrightness(tickDelta);

        renderFace(vertexConsumer, quaternion, size, currentX, currentY, currentZ, minU, maxU, minV, maxV, light);
        renderFace(vertexConsumer, flip, size, currentX, currentY, currentZ, minU, maxU, minV, maxV, light);
    }

    private void renderFace(VertexConsumer vertexConsumer, Quaternionf quaternion, float size, float x, float y, float z, float minU, float maxU, float minV, float maxV, int light) {
        Vector3f[] vec3fs = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};

        for(int k = 0; k < 4; ++k) {
            Vector3f vec3f2 = vec3fs[k];
            vec3f2.rotate(quaternion);
            vec3f2.mul(size);
            vec3f2.add(x, y, z);
        }
        vertexConsumer.vertex(vec3fs[0].x(), vec3fs[0].y(), vec3fs[0].z()).texture(maxU, maxV).color(this.red, this.green, this.blue, this.alpha).light(light).next();
        vertexConsumer.vertex(vec3fs[1].x(), vec3fs[1].y(), vec3fs[1].z()).texture(maxU, minV).color(this.red, this.green, this.blue, this.alpha).light(light).next();
        vertexConsumer.vertex(vec3fs[2].x(), vec3fs[2].y(), vec3fs[2].z()).texture(minU, minV).color(this.red, this.green, this.blue, this.alpha).light(light).next();
        vertexConsumer.vertex(vec3fs[3].x(), vec3fs[3].y(), vec3fs[3].z()).texture(minU, maxV).color(this.red, this.green, this.blue, this.alpha).light(light).next();
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
        if(!Block.isFaceFullSquare(world.getBlockState(BlockPos.ofFloored(x, y - 0.5d, z)).getSidesShape(world, BlockPos.ofFloored(x, y - 0.5d, z)), Direction.UP)) {
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

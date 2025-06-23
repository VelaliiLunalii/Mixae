package io.github.velaliilunalii.mixae.particle.custom;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

public class StationaryNormalScytheParticle extends SpriteBillboardParticle {
	final SpriteProvider spriteProvider;
	private final float rotX = (float)Math.random()*20 -10f;
	private final float rotZ = (float)Math.random()*20 -10f;

	StationaryNormalScytheParticle(ClientWorld world, double d, double e, double f, double g, SpriteProvider spriteProvider) {
		super(world, d, e, f, (double)0.0F, (double)0.0F, (double)0.0F);
		this.spriteProvider = spriteProvider;
		this.maxAge = 8;
		float h = 1;
		this.colorRed = h;
		this.colorGreen = h;
		this.colorBlue = h;
		this.scale = 2.5F;
		this.setSpriteForAge(spriteProvider);
	}

	public int getBrightness(float tint) {
		return 15728880;
	}

	public void tick() {
		this.prevPosX = this.x;
		this.prevPosY = this.y;
		this.prevPosZ = this.z;this.angle += (float) 360/8;
		if (this.age++ >= this.maxAge) {
			this.markDead();
		} else {
			this.setSpriteForAge(this.spriteProvider);
		}
	}

	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_LIT;
	}

	@Override
	public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
		Vec3d vec3d = camera.getPos();
		float f = (float)(MathHelper.lerp((double)tickDelta, this.prevPosX, this.x) - vec3d.getX());
		float g = (float)(MathHelper.lerp((double)tickDelta, this.prevPosY, this.y) - vec3d.getY());
		float h = (float)(MathHelper.lerp((double)tickDelta, this.prevPosZ, this.z) - vec3d.getZ());

		Vec3f[] vec3fs = new Vec3f[]{new Vec3f(-1.0F, 0.0F, -1.0F), new Vec3f(-1.0F, 0F, 1.0F), new Vec3f(1.0F, 0F, 1.0F), new Vec3f(1.0F, 0F, -1.0F)};
		float j = this.getSize(tickDelta);

		Quaternion rotationY = new Quaternion(Vec3f.POSITIVE_Y, this.angle, true);
		Quaternion rotationX = new Quaternion(Vec3f.POSITIVE_X, rotX, true);
		Quaternion rotationZ = new Quaternion(Vec3f.POSITIVE_Z, rotZ, true);

		for(int k = 0; k < 4; ++k) {
			Vec3f vec3f2 = vec3fs[k];
			vec3f2.scale(j);
			vec3f2.rotate(rotationY);
			vec3f2.rotate(rotationX);
			vec3f2.rotate(rotationZ);
			vec3f2.add(f, g, h);
		}

		float l = this.getMinU();
		float m = this.getMaxU();
		float n = this.getMinV();
		float o = this.getMaxV();
		int p = this.getBrightness(tickDelta);
		vertexConsumer.vertex(vec3fs[0].getX(), vec3fs[0].getY(), vec3fs[0].getZ()).uv(m, o).color(this.colorRed, this.colorGreen, this.colorBlue, this.colorAlpha).light(p).next();
		vertexConsumer.vertex(vec3fs[1].getX(), vec3fs[1].getY(), vec3fs[1].getZ()).uv(m, n).color(this.colorRed, this.colorGreen, this.colorBlue, this.colorAlpha).light(p).next();
		vertexConsumer.vertex(vec3fs[2].getX(), vec3fs[2].getY(), vec3fs[2].getZ()).uv(l, n).color(this.colorRed, this.colorGreen, this.colorBlue, this.colorAlpha).light(p).next();
		vertexConsumer.vertex(vec3fs[3].getX(), vec3fs[3].getY(), vec3fs[3].getZ()).uv(l, o).color(this.colorRed, this.colorGreen, this.colorBlue, this.colorAlpha).light(p).next();
	}

	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleFactory<DefaultParticleType> {
		private final SpriteProvider spriteProvider;

		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
			return new StationaryNormalScytheParticle(clientWorld, d, e, f, g, this.spriteProvider);
		}
	}
}

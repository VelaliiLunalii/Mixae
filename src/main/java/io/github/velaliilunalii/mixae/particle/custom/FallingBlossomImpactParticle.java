package io.github.velaliilunalii.mixae.particle.custom;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ShriekParticleEffect;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

import java.util.function.Consumer;

public class FallingBlossomImpactParticle extends SpriteBillboardParticle {
	private static final Vec3f field_38334 = (Vec3f) Util.make(new Vec3f(0.5F, 0.5F, 0.5F), Vec3f::normalize);
	private static final Vec3f field_38335 = new Vec3f(-1.0F, -1.0F, 0.0F);
	private static final float PI_THIRDS = 1.0472F;

	FallingBlossomImpactParticle(ClientWorld world, double x, double y, double z) {
		super(world, x, y+0.2, z, (double)0.0F, (double)0.0F, (double)0.0F);
		this.scale = 0.45F;
		this.maxAge = 20;
		this.gravityStrength = 0.0F;
		this.velocityX = (double)0.0F;
		this.velocityY = 0;
		this.velocityZ = (double)0.0F;
	}

	public float getSize(float tickDelta) {
		return this.scale*(((3 * ((float)Math.log(this.age))) + 5)/4);
	}

	public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
		this.pushQuad(vertexConsumer, camera, tickDelta, (angle) -> {
			angle.hamiltonProduct(Vec3f.POSITIVE_Y.getRadialQuaternion(0.0F));
		});
		this.pushQuad(vertexConsumer, camera, tickDelta, (angle) -> {
			angle.hamiltonProduct(Vec3f.POSITIVE_Y.getRadialQuaternion(-(float)Math.PI));
		});
	}

	private void pushQuad(VertexConsumer vertexConsumer, Camera camera, float tickDelta, Consumer<Quaternion> transformer) {
		Vec3d vec3d = camera.getPos();
		float f = (float)(MathHelper.lerp((double)tickDelta, this.prevPosX, this.x) - vec3d.getX());
		float g = (float)(MathHelper.lerp((double)tickDelta, this.prevPosY, this.y) - vec3d.getY());
		float h = (float)(MathHelper.lerp((double)tickDelta, this.prevPosZ, this.z) - vec3d.getZ());
		Quaternion quaternion = Vec3f.POSITIVE_X.getRadialQuaternion(-(float)Math.PI / 2F);
		transformer.accept(quaternion);
		field_38335.rotate(quaternion);
		Vec3f[] vec3fs = new Vec3f[]{new Vec3f(-1.0F, -1.0F, 0.0F), new Vec3f(-1.0F, 1.0F, 0.0F), new Vec3f(1.0F, 1.0F, 0.0F), new Vec3f(1.0F, -1.0F, 0.0F)};
		float i = this.getSize(tickDelta);

		for(int j = 0; j < 4; ++j) {
			Vec3f vec3f = vec3fs[j];
			vec3f.rotate(quaternion);
			vec3f.scale(i);
			vec3f.add(f, g, h);
		}

		int j = this.getBrightness(tickDelta);
		this.pushVertex(vertexConsumer, vec3fs[0], this.getMaxU(), this.getMaxV(), j);
		this.pushVertex(vertexConsumer, vec3fs[1], this.getMaxU(), this.getMinV(), j);
		this.pushVertex(vertexConsumer, vec3fs[2], this.getMinU(), this.getMinV(), j);
		this.pushVertex(vertexConsumer, vec3fs[3], this.getMinU(), this.getMaxV(), j);
	}

	private void pushVertex(VertexConsumer vertexConsumer, Vec3f coordinate, float u, float v, int light) {
		vertexConsumer.vertex((double)coordinate.getX(), (double)coordinate.getY(), (double)coordinate.getZ()).uv(u, v).color(this.colorRed, this.colorGreen, this.colorBlue, this.colorAlpha).light(light).next();
	}

	public int getBrightness(float tint) {
		return 15728880;
	}

	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}

	public void tick() {
		this.colorAlpha = 1.0F - (float)(age/maxAge);
		super.tick();
	}

	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleFactory<DefaultParticleType> {
		private final SpriteProvider spriteProvider;

		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
			FallingBlossomImpactParticle shriekParticle = new FallingBlossomImpactParticle(clientWorld, d, e, f);
			shriekParticle.setSprite(this.spriteProvider);
			shriekParticle.setColorAlpha(1.0F);
			return shriekParticle;
		}
	}
}

package io.github.velaliilunalii.mixae.particle.custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

public class SweepParticle extends SpriteBillboardParticle {
	private final SpriteProvider spriteProvider;

	SweepParticle(ClientWorld world, double d, double e, double f, double g, SpriteProvider spriteProvider) {
		super(world, d, e, f, (double)0.0F, (double)0.0F, (double)0.0F);
		this.spriteProvider = spriteProvider;
		this.maxAge = 4;
		float h = 1;
		this.colorRed = h;
		this.colorGreen = h;
		this.colorBlue = h;
		this.scale = 1.0F - (float)g * 0.5F;
		this.setSpriteForAge(spriteProvider);
	}

	public int getBrightness(float tint) {
		return 15728880;
	}

	public void tick() {
		this.prevPosX = this.x;
		this.prevPosY = this.y;
		this.prevPosZ = this.z;
		if (this.age++ >= this.maxAge) {
			this.markDead();
		} else {
			this.setSpriteForAge(this.spriteProvider);
		}
	}

	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_LIT;
	}

	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleFactory<DefaultParticleType> {
		private final SpriteProvider spriteProvider;

		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
			return new SweepParticle(clientWorld, d, e, f, g, this.spriteProvider);
		}
	}
}

package io.github.velaliilunalii.mixae.particle.custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

public class FallingBlossomTrailParticle extends SpriteBillboardParticle {
	private final SpriteProvider spriteProvider;

	FallingBlossomTrailParticle(ClientWorld world, double d, double e, double f, double g, double h, double i, SpriteProvider spriteProvider) {
		super(world, d, e, f, g, h, i);
		this.spriteProvider = spriteProvider;
		this.maxAge = 10;
		this.colorRed = 1;
		this.colorGreen = 1;
		this.colorBlue = 1;
		this.scale = 0.07F;
		this.setSpriteForAge(spriteProvider);
		this.velocityX = Math.random()/10;
		this.velocityY = Math.random()/2;
		this.velocityZ = Math.random()/10;
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
			this.x += this.velocityX;
			this.y += this.velocityY;
			this.z += this.velocityZ;

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
			return new FallingBlossomTrailParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
		}
	}
}

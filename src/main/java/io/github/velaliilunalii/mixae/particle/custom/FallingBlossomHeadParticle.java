package io.github.velaliilunalii.mixae.particle.custom;

import io.github.velaliilunalii.mixae.particle.ModParticles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

public class FallingBlossomHeadParticle extends SpriteBillboardParticle {
	private final SpriteProvider spriteProvider;

	FallingBlossomHeadParticle(ClientWorld world, double d, double e, double f, double g, double h, double i, SpriteProvider spriteProvider) {
		super(world, d, e, f, g, h, i);
		this.spriteProvider = spriteProvider;
		this.maxAge = 10;
		this.colorRed = 1;
		this.colorGreen = 1;
		this.colorBlue = 1;
		this.scale = 0.2F;
		this.setSpriteForAge(spriteProvider);
		this.velocityX = (double) -1 /10;
		this.velocityY = -1;
		this.velocityZ = (double) -1 /10;
	}

	public int getBrightness(float tint) {
		return 15728880;
	}

	public void tick() {
		this.prevPosX = this.x;
		this.prevPosY = this.y;
		this.prevPosZ = this.z;
		world.addParticle(ModParticles.FALLING_BLOSSOM_TRAIL_PARTICLE, x, y, z, 0, 0, 0);
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
			return new FallingBlossomHeadParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
		}
	}
}

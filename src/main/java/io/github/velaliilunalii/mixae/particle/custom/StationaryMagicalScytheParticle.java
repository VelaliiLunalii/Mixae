package io.github.velaliilunalii.mixae.particle.custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class StationaryMagicalScytheParticle extends StationaryNormalScytheParticle {

	StationaryMagicalScytheParticle(ClientWorld world, double d, double e, double f, double g, SpriteProvider spriteProvider) {
		super(world, d, e, f, g, spriteProvider);
	}

	@Override
	public void tick() {
		this.prevPosX = this.x;
		this.prevPosY = this.y;
		this.prevPosZ = this.z;
		float xVector = -MathHelper.sin(this.angle * ((float)Math.PI / 180F));
		float zVector = MathHelper.cos(this.angle * ((float)Math.PI / 180F));
		float normal = MathHelper.sqrt(xVector * xVector + zVector * zVector);
		Vec3d pos = new Vec3d(this.x, this.y, this.z);
		pos = pos.add(new Vec3d(3*xVector/normal, 0, 3*zVector/normal));
		this.world.addParticle(ParticleTypes.DRAGON_BREATH, pos.getX(), pos.getY(), pos.getZ(), 0, 0, 0);
		this.angle += (float) 360/8;
		if (this.age++ >= this.maxAge) {
			this.markDead();
		} else {
			this.setSpriteForAge(this.spriteProvider);
		}
	}

	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleFactory<DefaultParticleType> {
		private final SpriteProvider spriteProvider;

		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
			return new StationaryMagicalScytheParticle(clientWorld, d, e, f, g, this.spriteProvider);
		}
	}
}

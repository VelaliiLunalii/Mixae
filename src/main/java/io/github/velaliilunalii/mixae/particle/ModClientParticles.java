package io.github.velaliilunalii.mixae.particle;

import io.github.velaliilunalii.mixae.particle.custom.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;

@Environment(EnvType.CLIENT)
public class ModClientParticles {
	public static void register() {
		ParticleFactoryRegistry.getInstance().register(ModParticles.GOLDEN_SWEEP, SweepParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticles.REVERSE_GOLDEN_SWEEP, ReverseSweepParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticles.VITHYLIUM_SWEEP, SweepParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticles.VITHYLIUM_CRIT, SweepParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticles.REVERSE_VITHYLIUM_SWEEP, ReverseSweepParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticles.GOLDEN_SPIN, StationaryNormalScytheParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticles.VITHYLIUM_SPIN, StationaryMagicalScytheParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticles.VITHYLIUM_SPECIAL, LongScytheParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticles.FALLING_BLOSSOM_IMPACT_PARTICLE, FallingBlossomImpactParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticles.FALLING_BLOSSOM_HEAD_PARTICLE, FallingBlossomHeadParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticles.FALLING_BLOSSOM_TRAIL_PARTICLE, FallingBlossomTrailParticle.Factory::new);
	}
}

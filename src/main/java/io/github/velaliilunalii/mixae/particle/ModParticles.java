package io.github.velaliilunalii.mixae.particle;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModParticles {
	public static final DefaultParticleType GOLDEN_SWEEP = FabricParticleTypes.simple();
	public static final DefaultParticleType REVERSE_GOLDEN_SWEEP = FabricParticleTypes.simple();
	public static final DefaultParticleType VITHYLIUM_SWEEP = FabricParticleTypes.simple();
	public static final DefaultParticleType VITHYLIUM_CRIT = FabricParticleTypes.simple();
	public static final DefaultParticleType REVERSE_VITHYLIUM_SWEEP = FabricParticleTypes.simple();
	public static final DefaultParticleType GOLDEN_SPIN = FabricParticleTypes.simple();
	public static final DefaultParticleType VITHYLIUM_SPIN = FabricParticleTypes.simple();
	public static final DefaultParticleType VITHYLIUM_SPECIAL = FabricParticleTypes.simple();
	public static final DefaultParticleType FALLING_BLOSSOM_IMPACT_PARTICLE = FabricParticleTypes.simple();
	public static final DefaultParticleType FALLING_BLOSSOM_HEAD_PARTICLE = FabricParticleTypes.simple();
	public static final DefaultParticleType FALLING_BLOSSOM_TRAIL_PARTICLE = FabricParticleTypes.simple();

	public static void register() {
		Registry.register(Registry.PARTICLE_TYPE, new Identifier("mixae", "golden_sweep"), GOLDEN_SWEEP);
		Registry.register(Registry.PARTICLE_TYPE, new Identifier("mixae", "reverse_golden_sweep"), REVERSE_GOLDEN_SWEEP);
		Registry.register(Registry.PARTICLE_TYPE, new Identifier("mixae", "vithylium_sweep"), VITHYLIUM_SWEEP);
		Registry.register(Registry.PARTICLE_TYPE, new Identifier("mixae", "vithylium_crit"), VITHYLIUM_CRIT);
		Registry.register(Registry.PARTICLE_TYPE, new Identifier("mixae", "reverse_vithylium_sweep"), REVERSE_VITHYLIUM_SWEEP);
		Registry.register(Registry.PARTICLE_TYPE, new Identifier("mixae", "golden_spin"), GOLDEN_SPIN);
		Registry.register(Registry.PARTICLE_TYPE, new Identifier("mixae", "vithylium_spin"), VITHYLIUM_SPIN);
		Registry.register(Registry.PARTICLE_TYPE, new Identifier("mixae", "vithylium_special"), VITHYLIUM_SPECIAL);
		Registry.register(Registry.PARTICLE_TYPE, new Identifier("mixae", "falling_blossom_impact_particle"), FALLING_BLOSSOM_IMPACT_PARTICLE);
		Registry.register(Registry.PARTICLE_TYPE, new Identifier("mixae", "falling_blossom_head_particle"), FALLING_BLOSSOM_HEAD_PARTICLE);
		Registry.register(Registry.PARTICLE_TYPE, new Identifier("mixae", "falling_blossom_trail_particle"), FALLING_BLOSSOM_TRAIL_PARTICLE);
	}
}

package io.github.velaliilunalii.mixae;

import io.github.velaliilunalii.mixae.item.ModItems;
import io.github.velaliilunalii.mixae.networking.ModPackets;
import io.github.velaliilunalii.mixae.particle.ModClientParticles;
import io.github.velaliilunalii.mixae.particle.ModParticles;
import io.github.velaliilunalii.mixae.sound.ModSoundEvents;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Mixae implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("Mixae");

	@Override
	public void onInitialize(ModContainer mod) {
		ModItems.register(mod);
		ModParticles.register();
		ModSoundEvents.initialize();
		ModPackets.registerS2CPackets();
	}
}

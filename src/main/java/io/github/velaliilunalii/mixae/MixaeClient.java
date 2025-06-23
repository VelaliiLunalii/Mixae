package io.github.velaliilunalii.mixae;

import io.github.velaliilunalii.mixae.networking.ModClientPackets;
import io.github.velaliilunalii.mixae.particle.ModClientParticles;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

public class MixaeClient implements ClientModInitializer {
	@Override
	public void onInitializeClient(ModContainer mod) {
		ModClientParticles.register();
		ModClientPackets.registerS2CPackets();
	}
}

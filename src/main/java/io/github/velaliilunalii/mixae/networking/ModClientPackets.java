package io.github.velaliilunalii.mixae.networking;

import io.github.velaliilunalii.mixae.particle.ModParticles;
import net.minecraft.client.world.ClientWorld;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;

public class ModClientPackets {
	public static void registerS2CPackets() {
		ClientPlayNetworking.registerGlobalReceiver(ModPackets.IMPACT_BLOSSOM_PARTICLE_PACKET_ID, (client, handler, buf, responseSender) -> {
			double x = buf.readDouble();
			double y = buf.readDouble();
			double z = buf.readDouble();
			client.execute(() -> {
				ClientWorld world = client.world;
				if (world != null) {
					world.addParticle(ModParticles.FALLING_BLOSSOM_IMPACT_PARTICLE, x, y, z, 0, 0, 0);
				}
			});
		});
		ClientPlayNetworking.registerGlobalReceiver(ModPackets.HEAD_BLOSSOM_PARTICLE_PACKET_ID, (client, handler, buf, responseSender) -> {
			double x = buf.readDouble();
			double y = buf.readDouble();
			double z = buf.readDouble();
			double velocityX = buf.readDouble();
			double velocityY = buf.readDouble();
			double velocityZ= buf.readDouble();
			client.execute(() -> {
				ClientWorld world = client.world;
				if (world != null) {
					world.addParticle(ModParticles.FALLING_BLOSSOM_HEAD_PARTICLE, x, y, z, velocityX, velocityY, velocityZ);
				}
			});
		});
	}
}

package io.github.velaliilunalii.mixae.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

public class SweepHelper {
	public static float getRelativeYawToEntity(PlayerEntity player, Entity target) {
		double dx = target.getX() - player.getX();
		double dz = target.getZ() - player.getZ();

		double angleToTarget = Math.atan2(dz, dx);
		angleToTarget = Math.toDegrees(angleToTarget);

		float playerYaw = player.getYaw();

		float relativeYaw = (float) (playerYaw - angleToTarget +90);

		while (relativeYaw > 180) {
			relativeYaw -= 360;
		}
		while (relativeYaw < -180) {
			relativeYaw += 360;
		}

		return relativeYaw;
	}
}

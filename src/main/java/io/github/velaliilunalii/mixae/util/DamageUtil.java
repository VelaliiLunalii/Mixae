package io.github.velaliilunalii.mixae.util;

import io.github.velaliilunalii.mixae.item.damage_source.ModDamageSources;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class DamageUtil {
	public static void dealTrueDamage(LivingEntity target, float amount) {
		// Reset invulnerability frames
		target.timeUntilRegen = 0;
		target.hurtTime = 0;

		// Apply the damage
		target.damage(ModDamageSources.TRUE_DAMAGE, amount);
	}

	public static BlockHitResult raycastBlock(World world, PlayerEntity player, double maxDistance) {
		Vec3d eyePos = player.getCameraPosVec(1.0F); // eye position
		Vec3d lookVec = player.getRotationVec(1.0F); // look direction
		Vec3d end = eyePos.add(lookVec.multiply(maxDistance)); // end point

		return world.raycast(new RaycastContext(
			eyePos,
			end,
			RaycastContext.ShapeType.OUTLINE, // or COLLIDER for stricter collision
			RaycastContext.FluidHandling.NONE, // or ANY/SOURCE_ONLY if you want fluid hits
			player
		));
	}
}

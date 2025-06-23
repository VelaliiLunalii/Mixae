package io.github.velaliilunalii.mixae.util;

import io.github.velaliilunalii.mixae.networking.ModPackets;
import io.github.velaliilunalii.mixae.sound.ModSoundEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.quiltmc.qsl.networking.api.PacketByteBufs;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;

import java.util.List;

public class DelayedDamageClass implements IEffect{
	private final World world;
	private final PlayerEntity player;
	private final Vec3d center;
	private final float radius;
	private final int damage;
	private boolean usedOnce = false;

	public DelayedDamageClass(World world, PlayerEntity player, Vec3d center, float radius, int damage){
		this.world = world;
		this.player = player;
		this.center = center;
		this.radius = radius;
		this.damage = damage;
	}

	public void effect() {
		if (!usedOnce){
			firstEffect();
		}else{
			secondEffect();
		}
	}

	private void firstEffect(){
		Vec3d meteorPos = center.add(1, 10, 1);

		if (!world.isClient) {
			PacketByteBuf buf = PacketByteBufs.create();
			buf.writeDouble(meteorPos.getX());
			buf.writeDouble(meteorPos.getY());
			buf.writeDouble(meteorPos.getZ());
			buf.writeDouble((double) -1 /10);
			buf.writeDouble(-1);
			buf.writeDouble((double) -1 /10);

			for (PlayerEntity nearbyPlayer : world.getPlayers()) {
				if (nearbyPlayer.squaredDistanceTo(center) < 64 * 64) {
					ServerPlayNetworking.send((ServerPlayerEntity) nearbyPlayer, ModPackets.HEAD_BLOSSOM_PARTICLE_PACKET_ID, buf);
				}
			}
		}
	}

	private void secondEffect(){
		Box box = new Box(
			center.x - this.radius, center.y - 1, center.z - this.radius,
			center.x + this.radius, center.y + 2, center.z + this.radius
		);

		List<LivingEntity> nearbyMobs = player.getWorld().getEntitiesByClass(
			LivingEntity.class, box,
			entity -> entity != player && entity.isAlive() && !entity.isSpectator()
		);

		for (LivingEntity entity : nearbyMobs) {
			DamageUtil.dealTrueDamage(entity, this.damage);
		}
		if (!world.isClient) {
			PacketByteBuf buf = PacketByteBufs.create();
			buf.writeDouble(center.getX());
			buf.writeDouble(center.getY());
			buf.writeDouble(center.getZ());

			for (PlayerEntity nearbyPlayer : world.getPlayers()) {
				if (nearbyPlayer.squaredDistanceTo(center) < 64 * 64) {
					ServerPlayNetworking.send((ServerPlayerEntity) nearbyPlayer, ModPackets.IMPACT_BLOSSOM_PARTICLE_PACKET_ID, buf);
				}
			}
		}
		if(Math.random()<0.3) {
			player.world.playSound(null, center.getX(), center.getY(), center.getZ(),
				ModSoundEvents.ASTRAL_DRIP_HEAVY, SoundCategory.PLAYERS, 1.5F, 1F);
		}else{
			player.world.playSound(null, center.getX(), center.getY(), center.getZ(),
				ModSoundEvents.ASTRAL_DRIP_LIGHT, SoundCategory.PLAYERS, 2F, 1F);
		}
	}

	public void toggleUsedOnce(){
		this.usedOnce = !this.usedOnce;
	}
}

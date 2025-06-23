package io.github.velaliilunalii.mixae.mixin;

import io.github.velaliilunalii.mixae.item.ModItems;
import io.github.velaliilunalii.mixae.item.custom.DaggerItem;
import io.github.velaliilunalii.mixae.item.custom.ScytheItem;
import io.github.velaliilunalii.mixae.particle.ModParticles;
import io.github.velaliilunalii.mixae.sound.ModSoundEvents;
import io.github.velaliilunalii.mixae.util.SweepHelper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PlayerEntity.class)
public class SweepMixin {

	@Inject(method = "attack", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/entity/player/PlayerEntity;spawnSweepAttackParticles()V"
	), cancellable = true)
	private void onSweepAttack(Entity target, CallbackInfo ci) {
		PlayerEntity player = (PlayerEntity)(Object)this;
		ItemStack mainHand = player.getMainHandStack();

		if (mainHand.getItem() instanceof DaggerItem dagger) {
			ci.cancel();
			int counter = dagger.getHitSequence(mainHand);
			spawnSweepAttackParticles(player, counter);
			player.world.playSound(null, player.getX(), player.getY(), player.getZ(), ModSoundEvents.DAGGER_SLASH, SoundCategory.PLAYERS, 1F, 1F);
			dagger.decrementCooldown(player, mainHand);
			dagger.incrementHitSequence(mainHand);
			player.currentScreenHandler.sendContentUpdates();
		}
		if (mainHand.getItem() instanceof ScytheItem scythe) {
			ci.cancel();

			double radius = 4.0;
			Vec3d center = player.getPos();
			float damage = (float) mainHand.getDamage() /2 + EnchantmentHelper.getSweepingMultiplier(player) * 3.0F; // amplify if desired
			int counter = scythe.getHitSequence(mainHand);

			Box box = new Box(
				center.x - radius, center.y - 1, center.z - radius,
				center.x + radius, center.y + 1, center.z + radius
			);

			List<LivingEntity> mobsInCircle = player.getWorld().getEntitiesByClass(
				LivingEntity.class, box,
				entity -> entity != player && entity.isAlive() && !entity.isSpectator()
					&& entity.getPos().distanceTo(center) <= radius
			);

			for (LivingEntity entity : mobsInCircle) {
				if (entity != player && entity != target && !player.isTeammate(entity) &&
					(counter == 2 || Math.abs(SweepHelper.getRelativeYawToEntity(player, entity)) <= 70f)) {
					entity.takeKnockback(0.4, MathHelper.sin(player.getYaw() * (float)Math.PI / 180F), -MathHelper.cos(player.getYaw() * (float)Math.PI / 180F));
					entity.damage(DamageSource.player(player), damage);
				}
			}

			if (counter == 2) {
				scythe.startComboTicksCountdown(mainHand);
				player.world.playSound(null, player.getX(), player.getY(), player.getZ(), ModSoundEvents.SCYTHE_SLASH_HEAVY, SoundCategory.PLAYERS, 1.1F, 0.8F);
				spawnSweepAttackParticles(player, counter);
			}else{
				player.world.playSound(null, player.getX(), player.getY(), player.getZ(), ModSoundEvents.SCYTHE_SLASH_LIGHT, SoundCategory.PLAYERS, 0.9F, 1F);
				spawnSweepAttackParticles(player, counter);
			}

			scythe.incrementHitSequence(mainHand);
			player.currentScreenHandler.sendContentUpdates();
		}
	}

	public void spawnSweepAttackParticles(PlayerEntity player, int counter){
		double d = (double)(-MathHelper.sin(player.getYaw() * ((float)Math.PI / 180F)));
		double e = (double)MathHelper.cos(player.getYaw() * ((float)Math.PI / 180F));
		if (player.world instanceof ServerWorld) {
			if (player.getMainHandStack().getItem() == ModItems.GOLDEN_SCYTHE || player.getMainHandStack().getItem() == ModItems.GOLDEN_DAGGER) {
				if (counter == 2) {
					((ServerWorld) player.world).spawnParticles(ModParticles.GOLDEN_SPIN,
						player.getX(), player.getBodyY((double) 0.5F), player.getZ(), 0, d, (double) 0, e, (double) 0.0F);
				}else if (counter == 1){
					((ServerWorld) player.world).spawnParticles(ModParticles.REVERSE_GOLDEN_SWEEP,
						player.getX() + d, player.getBodyY((double) 0.5F), player.getZ() + e, 0, d, (double) 0.0F, e, (double) 0.0F);
				}else {
					((ServerWorld) player.world).spawnParticles(ModParticles.GOLDEN_SWEEP,
						player.getX() + d, player.getBodyY((double) 0.5F), player.getZ() + e, 0, d, (double) 0.0F, e, (double) 0.0F);
				}
			}else if (player.getMainHandStack().getItem() == ModItems.VITHYLIUM_SCYTHE || player.getMainHandStack().getItem() == ModItems.VITHYLIUM_DAGGER){
				if (counter == 2){
					((ServerWorld) player.world).spawnParticles(ModParticles.VITHYLIUM_SPIN,
						player.getX(), player.getBodyY((double) 0.5F), player.getZ(), 0, d, (double) 0, e, (double) 0.0F);
				}else if (counter == 1){
					((ServerWorld) player.world).spawnParticles(ModParticles.REVERSE_VITHYLIUM_SWEEP,
						player.getX() + d, player.getBodyY((double) 0.5F), player.getZ() + e, 0, d, (double) 0.0F, e, (double) 0.0F);
				}else {
					((ServerWorld) player.world).spawnParticles(ModParticles.VITHYLIUM_SWEEP,
						player.getX() + d, player.getBodyY((double) 0.5F), player.getZ() + e, 0, d, (double) 0.0F, e, (double) 0.0F);
				}
			}
		}
	}

	@Inject(method = "attack", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V",
		ordinal = 2
	))
	private void onCrit(Entity target, CallbackInfo ci) {
		PlayerEntity player = (PlayerEntity)(Object)this;
		ItemStack mainHand = player.getMainHandStack();

		if (mainHand.getItem()  == ModItems.VITHYLIUM_SCYTHE) {
			ScytheItem scythe = (ScytheItem) mainHand.getItem();
			double d = -MathHelper.sin(player.getYaw() * ((float)Math.PI / 180F));
			double e = MathHelper.cos(player.getYaw() * ((float)Math.PI / 180F));
			player.world.playSound(null, player.getX(), player.getY(), player.getZ(), ModSoundEvents.SCYTHE_SLASH_HEAVY, SoundCategory.PLAYERS, 1.1F, 0.8F);
			if (player.world instanceof ServerWorld) {
				((ServerWorld) player.world).spawnParticles(ModParticles.VITHYLIUM_CRIT,
					player.getX() + d, player.getBodyY((double) 0.5F)-0.3, player.getZ() + e, 0, d, (double) 0.0F, e, (double) 0.0F);
			}
			if (scythe.getHitSequence(mainHand)<2){
				scythe.incrementHitSequence(mainHand);
			}
			player.currentScreenHandler.sendContentUpdates();
		}
		if (mainHand.getItem()  == ModItems.VITHYLIUM_DAGGER) {
			DaggerItem dagger = (DaggerItem) mainHand.getItem();
			double d = -MathHelper.sin(player.getYaw() * ((float)Math.PI / 180F));
			double e = MathHelper.cos(player.getYaw() * ((float)Math.PI / 180F));
			player.world.playSound(null, player.getX(), player.getY(), player.getZ(), ModSoundEvents.DAGGER_SLASH, SoundCategory.PLAYERS, 1F, 1F);
			if (player.world instanceof ServerWorld) {
				((ServerWorld) player.world).spawnParticles(ModParticles.VITHYLIUM_CRIT,
					player.getX() + d, player.getBodyY((double) 0.5F)-0.3, player.getZ() + e, 0, d, (double) 0.0F, e, (double) 0.0F);
			}
			dagger.decrementCooldown(player, mainHand);
			dagger.incrementHitSequence(mainHand);
			player.currentScreenHandler.sendContentUpdates();
		}
	}
}


package io.github.velaliilunalii.mixae.item.custom;

import io.github.velaliilunalii.mixae.item.ModItems;
import io.github.velaliilunalii.mixae.particle.ModParticles;
import io.github.velaliilunalii.mixae.sound.ModSoundEvents;
import io.github.velaliilunalii.mixae.util.DamageUtil;
import io.github.velaliilunalii.mixae.util.SweepHelper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class ScytheItem extends SwordItem {

	private boolean hasAttacked;

	public ScytheItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
		super(toolMaterial, attackDamage, attackSpeed, settings);
	}

	public int getHitSequence(ItemStack stack){
		NbtCompound tag = stack.getOrCreateNbt();
		return tag.getInt("hit_sequence_key");
	}

	public void incrementHitSequence(ItemStack stack){
		NbtCompound tag = stack.getOrCreateNbt();
		int counter = tag.getInt("hit_sequence_key");
		tag.putInt("hit_sequence_key", (counter + 1) % 3);
	}

	public int getComboTicksCountdown(ItemStack stack){
		NbtCompound tag = stack.getOrCreateNbt();
		return tag.getInt("combo_ticks_key");
	}

	public void startComboTicksCountdown(ItemStack stack){
		NbtCompound tag = stack.getOrCreateNbt();
		tag.putInt("combo_ticks_key", 20);
	}

	public void decrementComboTicksCountdown(ItemStack stack){
		NbtCompound tag = stack.getOrCreateNbt();
		int cd = tag.getInt("combo_ticks_key");
		tag.putInt("combo_ticks_key", cd-1);
	}

	public void setComboTicksCountdown(ItemStack stack, int cd){
		NbtCompound tag = stack.getOrCreateNbt();
		tag.putInt("combo_ticks_key", cd);
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BOW;
	}

	@Override
	public int getMaxUseTime(ItemStack stack) {
		return 72000;
	}

	@Override
	public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
		if (user instanceof PlayerEntity player && stack.getItem() == ModItems.VITHYLIUM_SCYTHE) {
			int i = this.getMaxUseTime(stack) - remainingUseTicks;
			if (i >= 20 || this.getComboTicksCountdown(stack) > 0) {
				if (this.getComboTicksCountdown(stack) > 0){
					this.setComboTicksCountdown(stack, 0);
				}
				player.getItemCooldownManager().set(this, 140);

				scytheDamage(player, stack, 20);
				playerDash(player);
				spawnParticle(world, player);
				player.swingHand(Hand.MAIN_HAND);
			}
		}
	}

	private void scytheDamage(PlayerEntity player, ItemStack mainHand, int range){
		double radius = 4.0;
		Vec3d center = player.getPos().add(0, 0.5, 0);
		float damage = this.getAttackDamage() + (EnchantmentHelper.getSweepingMultiplier(player) * 3.0F);
		float playerYaw = player.getYaw();
		float xVector = -MathHelper.sin(playerYaw * ((float)Math.PI / 180F));
		float zVector = MathHelper.cos(playerYaw * ((float)Math.PI / 180F));
		float normal = MathHelper.sqrt(xVector * xVector + zVector * zVector);
		Vec3d offset = new Vec3d(xVector/normal, 0, zVector/normal);

		List<LivingEntity> targetMobs = new java.util.ArrayList<>(List.of());
		Vec3d boxCenter = center;

		for (int i = 0; i < range; i++) {
			boxCenter = center;

			Box box = new Box(
				boxCenter.x - radius, boxCenter.y - 1, boxCenter.z - radius,
				boxCenter.x + radius, boxCenter.y + 2, boxCenter.z + radius
			);

			List<LivingEntity> nearbyMobs = player.getWorld().getEntitiesByClass(
				LivingEntity.class, box,
				entity -> entity != player && entity.isAlive() && !entity.isSpectator()
			);

			for (LivingEntity entity : nearbyMobs) {
				if (!targetMobs.contains(entity)) targetMobs.add(entity);

			}

			center = center.add(offset);
		}
		for (LivingEntity entity : targetMobs) {
			entity.takeKnockback(0.4, -xVector, -zVector);
			DamageUtil.dealTrueDamage(entity, damage);
		}
	}

	private void spawnParticle(World world, PlayerEntity player){
		if (world instanceof ServerWorld serverWorld) {
			float playerYaw = player.getYaw();
			float xVector = -MathHelper.sin(playerYaw * ((float)Math.PI / 180F));
			float zVector = MathHelper.cos(playerYaw * ((float)Math.PI / 180F));
			float normal = MathHelper.sqrt(xVector * xVector + zVector * zVector);
			serverWorld.spawnParticles(ModParticles.VITHYLIUM_SPECIAL,
				player.getX(), player.getBodyY(0.2F), player.getZ(), 1,
				xVector/normal, 0f, zVector/normal, 1);
			player.world.playSound(null, player.getX(), player.getY(), player.getZ(),
				ModSoundEvents.SCYTHE_SLASH_HEAVY, SoundCategory.PLAYERS, 1.1F, 0.8F);
		}
	}

	private void playerDash(PlayerEntity player){
		float playerYaw = player.getYaw();
		float playerPitch = player.getPitch();
		float xVector = -MathHelper.sin(playerYaw * ((float)Math.PI / 180F)) * MathHelper.cos(playerPitch * ((float)Math.PI / 180F));
		float yVector = -MathHelper.sin(playerPitch * ((float)Math.PI / 180F));
		float zVector = MathHelper.cos(playerYaw * ((float)Math.PI / 180F)) * MathHelper.cos(playerPitch * ((float)Math.PI / 180F));
		float normal = MathHelper.sqrt(xVector * xVector + yVector * yVector + zVector * zVector);

		xVector *= 1 / normal;
		yVector *= 1 / (2*normal);
		zVector *= 1 / normal;

		yVector += player.isOnGround() ? (float) 0.2 : 0;
		player.addVelocity(xVector, yVector, zVector);
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		if (entity instanceof PlayerEntity player && stack.getItem() == ModItems.VITHYLIUM_SCYTHE){
			if (this.getComboTicksCountdown(stack) > 0){
				this.decrementComboTicksCountdown(stack);
			}
			if (!world.isClient) {
				this.hasAttacked = player.getAttackCooldownProgress(0.5F) >= 0.9F || this.hasAttacked;
				if (player.handSwinging && this.hasAttacked) {
					HitResult blockRaycast = player.raycast(3D, 0.0F, false);
					boolean entityRaycast = this.entityRaycast(world, player, 4);
					if (!entityRaycast && blockRaycast.getType() == HitResult.Type.MISS) {
						sweepAttack(player);
					}
					this.hasAttacked = false;
				}
			}
		}
		super.inventoryTick(stack, world, entity, slot, selected);
	}

	private boolean entityRaycast(World world, PlayerEntity player, float distance) {
		float playerYaw = player.getYaw();
		float playerPitch = player.getPitch();
		double radius = 0.5D;

		float xVec = -MathHelper.sin(playerYaw * ((float) Math.PI / 180F)) * MathHelper.cos(playerPitch * ((float) Math.PI / 180F));
		float yVec = -MathHelper.sin(playerPitch * ((float) Math.PI / 180F));
		float zVec = MathHelper.cos(playerYaw * ((float) Math.PI / 180F)) * MathHelper.cos(playerPitch * ((float) Math.PI / 180F));
		float norm = MathHelper.sqrt(xVec * xVec + yVec * yVec + zVec * zVec);
		Vec3d direction = new Vec3d(xVec / norm, yVec / norm, zVec / norm);

		Vec3d origin = player.getEyePos();
		boolean hit = false;

		for (int i = 0; i < distance; i += 1) {
			Vec3d point = origin.add(direction.multiply(i));
			Box checkBox = new Box(
				point.x - radius, point.y - radius, point.z - radius,
				point.x + radius, point.y + radius, point.z + radius
			);

			List<Entity> entities = world.getOtherEntities(player, checkBox, entity -> entity instanceof LivingEntity);
			if (!entities.isEmpty()) {
				hit = true;
				break;
			}
		}

		return hit;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		if (itemStack.getDamage() >= itemStack.getMaxDamage() - 1 || itemStack.getItem() != ModItems.VITHYLIUM_SCYTHE) {
			return TypedActionResult.fail(itemStack);
		} else {
			user.setCurrentHand(hand);
			return TypedActionResult.consume(itemStack);
		}
	}

	private void sweepAttack(PlayerEntity player){
		ItemStack mainHand = player.getMainHandStack();

		if (mainHand.getItem() instanceof ScytheItem scythe) {

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
				if (entity != player && !player.isTeammate(entity) &&
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
			if (player.getMainHandStack().getItem() == ModItems.GOLDEN_SCYTHE) {
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
			}else if (player.getMainHandStack().getItem() == ModItems.VITHYLIUM_SCYTHE){
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
}

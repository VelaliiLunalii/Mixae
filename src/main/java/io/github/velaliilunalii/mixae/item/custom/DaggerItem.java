package io.github.velaliilunalii.mixae.item.custom;

import io.github.velaliilunalii.mixae.item.ModItems;
import io.github.velaliilunalii.mixae.particle.ModParticles;
import io.github.velaliilunalii.mixae.sound.ModSoundEvents;
import io.github.velaliilunalii.mixae.util.DamageUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class DaggerItem extends SwordItem {
	private int remainingSlashes;
	private LivingEntity target;
	private boolean tickLockout;
	private float currentCooldown;

	public DaggerItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
		super(toolMaterial, attackDamage, attackSpeed, settings);
		this.tickLockout = false;
	}

	public int getHitSequence(ItemStack stack){
		NbtCompound tag = stack.getOrCreateNbt();
		return tag.getInt("hit_sequence_key");
	}

	public void incrementHitSequence(ItemStack stack){
		NbtCompound tag = stack.getOrCreateNbt();
		int counter = tag.getInt("hit_sequence_key");
		tag.putInt("hit_sequence_key", (counter + 1) % 2);
	}

	public void decrementCooldown(PlayerEntity player, ItemStack stack){
		float cd = player.getItemCooldownManager().getCooldownProgress(stack.getItem(), 0);
		if (cd > 0){
			player.getItemCooldownManager().remove(stack.getItem());
			this.currentCooldown *= cd;
			int cooldownReduction = 60;
			player.getItemCooldownManager().set(stack.getItem(), ((int) this.currentCooldown - cooldownReduction));
		}
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		if (this.target != null && this.target.isAlive()) {
			if (this.remainingSlashes > 0 && entity instanceof PlayerEntity player) {
				if(this.tickLockout) {
					this.tickLockout = false;
				}else{
					performSlashes(player, stack, this.target);
					this.remainingSlashes--;
					this.tickLockout = true;
				}
			}
		}
		super.inventoryTick(stack, world, entity, slot, selected);
	}

	@Override
	public ActionResult useOnEntity(ItemStack stack, PlayerEntity player, LivingEntity entity, Hand hand) {
		if(player.getItemCooldownManager().getCooldownProgress(stack.getItem(), 0.5f) <=0
			&& stack.getItem() instanceof DaggerItem dagger && dagger == ModItems.VITHYLIUM_DAGGER){
			this.target = entity;
			this.remainingSlashes = 5;
			this.currentCooldown = 240;
			player.getItemCooldownManager().set(this, (int) this.currentCooldown);
		}
		return super.useOnEntity(stack, player, entity, hand);
	}

	private void performSlashes(PlayerEntity player, ItemStack stack, LivingEntity entity){
		if(stack.getItem() instanceof DaggerItem dagger && dagger == ModItems.VITHYLIUM_DAGGER){
			int counter = dagger.getHitSequence(stack);
			spawnSweepAttackParticles(player, counter);
			player.world.playSound(null, player.getX(), player.getY(), player.getZ(), ModSoundEvents.DAGGER_SLASH, SoundCategory.PLAYERS, 1F, 1F);
			player.swingHand(Hand.MAIN_HAND);
			dagger.incrementHitSequence(stack);
			player.currentScreenHandler.sendContentUpdates();
			DamageUtil.dealTrueDamage(entity, this.getAttackDamage()/2);
		}
	}

	public void spawnSweepAttackParticles(PlayerEntity player, int counter){
		double d = (-MathHelper.sin(player.getYaw() * ((float)Math.PI / 180F)));
		double e = MathHelper.cos(player.getYaw() * ((float)Math.PI / 180F));
		if (player.world instanceof ServerWorld) {
			if (player.getMainHandStack().getItem() == ModItems.GOLDEN_DAGGER) {
				if (counter == 1){
					((ServerWorld) player.world).spawnParticles(ModParticles.REVERSE_GOLDEN_SWEEP,
						player.getX() + d, player.getBodyY((double) 0.5F), player.getZ() + e, 0, d, (double) 0.0F, e, (double) 0.0F);
				}else {
					((ServerWorld) player.world).spawnParticles(ModParticles.GOLDEN_SWEEP,
						player.getX() + d, player.getBodyY((double) 0.5F), player.getZ() + e, 0, d, (double) 0.0F, e, (double) 0.0F);
				}
			}else if (player.getMainHandStack().getItem() == ModItems.VITHYLIUM_DAGGER){
				if (counter == 1){
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

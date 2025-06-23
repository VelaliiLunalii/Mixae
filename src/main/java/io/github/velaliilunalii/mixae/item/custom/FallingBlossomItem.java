package io.github.velaliilunalii.mixae.item.custom;

import io.github.velaliilunalii.mixae.item.ModItems;
import io.github.velaliilunalii.mixae.util.DamageUtil;
import io.github.velaliilunalii.mixae.util.DelayedDamageClass;
import io.github.velaliilunalii.mixae.util.TickingList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.quiltmc.loader.impl.lib.sat4j.core.Vec;

import java.util.ArrayList;
import java.util.List;

public class FallingBlossomItem extends HoeItem {
	private TickingList tickingList;
	private final int maxDistance = 8;

	public FallingBlossomItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
		super(material, attackDamage, attackSpeed, settings);
	}


	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BOW;
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		if (this.tickingList != null && !this.tickingList.isEmpty()){
			this.tickingList.tick();
		}
		if (entity instanceof PlayerEntity player && player.isUsingItem()) {
			Vec3d center;
			BlockHitResult result = DamageUtil.raycastBlock(world, player, maxDistance);
			if (result.getType() != HitResult.Type.MISS){
				center = result.getPos();
			}else{
				float playerYaw = player.getYaw();
				float playerPitch = player.getPitch();
				float xVector = -MathHelper.sin(playerYaw * ((float)Math.PI / 180F)) * MathHelper.cos(playerPitch * ((float)Math.PI / 180F));
				float yVector = -MathHelper.sin(playerPitch * ((float)Math.PI / 180F));
				float zVector = MathHelper.cos(playerYaw * ((float)Math.PI / 180F)) * MathHelper.cos(playerPitch * ((float)Math.PI / 180F));
				center = player.getPos().add(xVector * maxDistance, yVector * maxDistance +0.2, zVector * maxDistance);
			}
			int i = this.getMaxUseTime(stack) - player.getItemUseTimeLeft();
			aimingParticles(world, center, useTimeToRadius(i));
		}

		super.inventoryTick(stack, world, entity, slot, selected);
	}

	private double useTimeToRadius(int i){
		double radius = 0;
		if (i >= 10){
			radius +=2;
		}
		if (i >= 20){
			radius +=2;
		}
		if (i >= 30){
			radius +=2;
		}
		return radius;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		if (itemStack.getDamage() >= itemStack.getMaxDamage() - 1) {
			return TypedActionResult.fail(itemStack);
		} else {
			user.setCurrentHand(hand);
			return TypedActionResult.consume(itemStack);
		}
	}

	@Override
	public int getMaxUseTime(ItemStack stack) {
		return 72000;
	}

	@Override
	public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
		if (user instanceof PlayerEntity player && stack.getItem() == ModItems.FALLING_BLOSSOM) {
			int i = this.getMaxUseTime(stack) - remainingUseTicks;
			if (i >= 10) {
				spell(world, player, useTimeToRadius(i));

				player.getItemCooldownManager().set(this, 100);
				player.swingHand(Hand.MAIN_HAND);
			}
		}
	}

	private void aimingParticles(World world, Vec3d center, double radius){
		for (int i = 0; i < 360; i += 10) {
			double dx = (-MathHelper.sin(i * ((float) Math.PI / 180F)));
			double dz = MathHelper.cos(i * ((float) Math.PI / 180F));
			Vec3d pos = center.add(dx * radius, 0, dz * radius);
			world.addParticle(ParticleTypes.DRAGON_BREATH, pos.getX(), pos.getY(), pos.getZ(), 0, 0, 0);
		}
	}

	private void spell(World world, PlayerEntity player, double radius) {
		Vec3d center;
		BlockHitResult result = DamageUtil.raycastBlock(world, player, maxDistance);
		if (result.getType() != HitResult.Type.MISS){
			center = result.getPos();
		}else{
			float playerYaw = player.getYaw();
			float playerPitch = player.getPitch();
			float xVector = -MathHelper.sin(playerYaw * ((float)Math.PI / 180F)) * MathHelper.cos(playerPitch * ((float)Math.PI / 180F));
			float yVector = -MathHelper.sin(playerPitch * ((float)Math.PI / 180F));
			float zVector = MathHelper.cos(playerYaw * ((float)Math.PI / 180F)) * MathHelper.cos(playerPitch * ((float)Math.PI / 180F));
			center = player.getPos().add(xVector * maxDistance, yVector * maxDistance +0.2, zVector * maxDistance);
		}

		List<DelayedDamageClass> posList = new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			double angle = Math.random() * 2 * Math.PI; // Random angle (0 to 2π)
			double dist = Math.sqrt(Math.random()) * radius; // Random radius (uniform distribution in circle)

			double offsetX = Math.cos(angle) * dist;
			double offsetZ = Math.sin(angle) * dist;

			Vec3d position = new Vec3d(center.getX() + offsetX, center.getY(), center.getZ() + offsetZ);
			DelayedDamageClass element = new DelayedDamageClass(world, player, position, 2f, 2);
			posList.add(element);
		}

		this.tickingList = new TickingList<>(posList);
	}

}

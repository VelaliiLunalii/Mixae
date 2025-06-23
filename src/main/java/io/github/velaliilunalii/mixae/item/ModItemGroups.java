package io.github.velaliilunalii.mixae.item;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ModItemGroups {
	public static final ItemGroup MIXAE_GROUP = FabricItemGroupBuilder.build(
		new Identifier("mixae", "mixae_group"),
		() -> new ItemStack(ModItems.VITHYLIUM_SCYTHE)
	);
}

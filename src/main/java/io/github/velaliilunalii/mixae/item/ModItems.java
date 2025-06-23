package io.github.velaliilunalii.mixae.item;

import io.github.velaliilunalii.mixae.item.custom.DaggerItem;
import io.github.velaliilunalii.mixae.item.custom.FallingBlossomItem;
import io.github.velaliilunalii.mixae.item.custom.ScytheItem;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

public class ModItems {
	public static final Item VITHYLIUM_INGOT = new Item(new QuiltItemSettings().group(ModItemGroups.MIXAE_GROUP));
	public static final Item VITHYLIUM_NUGGET = new Item(new QuiltItemSettings().group(ModItemGroups.MIXAE_GROUP));
	public static final Item GOLDEN_SCYTHE = new ScytheItem(ToolMaterials.GOLD, 6, -3.2F,
		new QuiltItemSettings().group(ModItemGroups.MIXAE_GROUP));
	public static final Item VITHYLIUM_SCYTHE = new ScytheItem(ToolMaterials.DIAMOND, 4, -3.2F,
		new QuiltItemSettings().group(ModItemGroups.MIXAE_GROUP));
	public static final Item GOLDEN_DAGGER = new DaggerItem(ToolMaterials.GOLD, 4, -2.2F,
		new QuiltItemSettings().group(ModItemGroups.MIXAE_GROUP));
	public static final Item VITHYLIUM_DAGGER = new DaggerItem(ToolMaterials.DIAMOND, 2, -2.2F,
		new QuiltItemSettings().group(ModItemGroups.MIXAE_GROUP));
	public static final Item FALLING_BLOSSOM = new FallingBlossomItem(ToolMaterials.NETHERITE, -2, -3,
		new QuiltItemSettings().group(ModItemGroups.MIXAE_GROUP));

	public static void register(ModContainer mod) {
		Registry.register(Registry.ITEM, new Identifier(mod.metadata().id(), "vithylium_ingot"), VITHYLIUM_INGOT);
		Registry.register(Registry.ITEM, new Identifier(mod.metadata().id(), "vithylium_nugget"), VITHYLIUM_NUGGET);
		Registry.register(Registry.ITEM, new Identifier(mod.metadata().id(), "golden_scythe"), GOLDEN_SCYTHE);
		Registry.register(Registry.ITEM, new Identifier(mod.metadata().id(), "vithylium_scythe"), VITHYLIUM_SCYTHE);
		Registry.register(Registry.ITEM, new Identifier(mod.metadata().id(), "golden_dagger"), GOLDEN_DAGGER);
		Registry.register(Registry.ITEM, new Identifier(mod.metadata().id(), "vithylium_dagger"), VITHYLIUM_DAGGER);
		Registry.register(Registry.ITEM, new Identifier(mod.metadata().id(), "falling_blossom"), FALLING_BLOSSOM);
	}
}

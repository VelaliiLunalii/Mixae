package io.github.velaliilunalii.mixae.mixin;

import io.github.velaliilunalii.mixae.Mixae;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {
	@Inject(method = "init", at = @At("TAIL"))
	public void mixae$onInit(CallbackInfo ci) {
		Mixae.LOGGER.info("This line is printed by a Mixae mixin!");
	}
}

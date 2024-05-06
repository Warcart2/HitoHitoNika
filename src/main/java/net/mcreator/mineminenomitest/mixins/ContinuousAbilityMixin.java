package net.mcreator.mineminenomitest.mixins;

import xyz.pixelatedw.mineminenomi.api.abilities.ContinuousAbility;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.entity.player.PlayerEntity;

@Mixin(ContinuousAbility.class)
public class ContinuousAbilityMixin {
	@Inject(method = "stopContinuity", at = {@At(value = "RETURN")}, cancellable = true, remap = false)
	protected void continueStop(PlayerEntity player, CallbackInfo info) {
	}
}

package net.mcreator.mineminenomitest.mixins;

import xyz.pixelatedw.mineminenomi.api.abilities.IAbilityModeSwitcher;
import xyz.pixelatedw.mineminenomi.abilities.haki.HaoshokuHakiInfusionAbility;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.entity.player.PlayerEntity;

@Mixin(HaoshokuHakiInfusionAbility.class)
public class HaoshokuInfusionMixin extends ContinuousAbilityMixin implements IAbilityModeSwitcher {
	@Inject(method = "onStartContinuityEvent", at = {@At(value = "RETURN")}, cancellable = true, remap = false)
	private void start(PlayerEntity player, CallbackInfoReturnable<Boolean> info) {
		HaoshokuHakiInfusionAbility hao = (HaoshokuHakiInfusionAbility) ((Object) this);
		if (info.getReturnValue())
			this.enableModes(player, hao);
	}

	@Override
	protected void continueStop(PlayerEntity player, CallbackInfo info) {
		this.disableModes(player, (HaoshokuHakiInfusionAbility) ((Object) this));
	}
}

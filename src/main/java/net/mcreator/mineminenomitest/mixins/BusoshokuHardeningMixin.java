package net.mcreator.mineminenomitest.mixins;

import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbilityModeSwitcher;
import xyz.pixelatedw.mineminenomi.abilities.haki.BusoshokuHakiHardeningAbility;
import xyz.pixelatedw.mineminenomi.abilities.haki.BusoshokuHakiEmissionAbility;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.entity.player.PlayerEntity;
import xyz.pixelatedw.mineminenomi.abilities.haki.BusoshokuHakiInternalDestructionAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;

@Mixin(BusoshokuHakiHardeningAbility.class)
public class BusoshokuHardeningMixin extends ContinuousAbilityMixin implements IAbilityModeSwitcher {
	@Inject(method = "onStartContinuityEvent", at = {@At(value = "RETURN")}, remap = false)
	private void start(PlayerEntity player, CallbackInfoReturnable<Boolean> info) {
		BusoshokuHakiHardeningAbility buso = (BusoshokuHakiHardeningAbility) ((Object) this);
		if (info.getReturnValue())
			this.enableModes(player, buso);
	}

	@Override
	protected void continueStop(PlayerEntity player, CallbackInfo info) {
		this.disableModes(player, (BusoshokuHakiHardeningAbility) ((Object) this));
	}
}

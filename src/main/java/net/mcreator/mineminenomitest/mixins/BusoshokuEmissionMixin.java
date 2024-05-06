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

@Mixin(BusoshokuHakiEmissionAbility.class)
public class BusoshokuEmissionMixin extends ContinuousAbilityMixin implements IAbilityModeSwitcher {
	@Inject(method = "onStartContinuityEvent", at = {@At(value = "RETURN")}, remap = false)
	private void start(PlayerEntity player, CallbackInfoReturnable<Boolean> info) {
		if (info.getReturnValue()) {
			BusoshokuHakiHardeningAbility hard = AbilityDataCapability.get(player).getUnlockedAbility(BusoshokuHakiHardeningAbility.INSTANCE);
			if (hard != null) {
				this.enableModes(player, hard);
			}
			this.enableModes(player, (BusoshokuHakiEmissionAbility) (Object) this);
		}
	}

	@Override
	protected void continueStop(PlayerEntity player, CallbackInfo info) {
		BusoshokuHakiHardeningAbility hard = AbilityDataCapability.get(player).getUnlockedAbility(BusoshokuHakiHardeningAbility.INSTANCE);
		this.disableModes(player, hard);
	}
}

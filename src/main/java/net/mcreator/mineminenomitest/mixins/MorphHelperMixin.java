package net.mcreator.mineminenomitest.mixins;

import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.api.morph.MorphInfo;
import xyz.pixelatedw.mineminenomi.api.helpers.MorphHelper;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.entity.LivingEntity;

import net.mcreator.mineminenomitest.IMultiModelMorph;
import net.mcreator.mineminenomitest.HitoHitoNoMiNikaMod;

import java.util.Iterator;

@Mixin(MorphHelper.class)
public class MorphHelperMixin {
	@Inject(method = "getZoanInfo", at = {@At(value = "RETURN")}, cancellable = true, remap = false)
	private static void getZoanInfo(LivingEntity entity, CallbackInfoReturnable<MorphInfo> info) {
		IAbilityData abilityProps = AbilityDataCapability.get(entity);
		Iterator<Ability> var3 = abilityProps.getUnlockedAbilities().iterator();
		while (var3.hasNext()) {
			Ability ability = var3.next();
			if (ability != null && ability instanceof IMultiModelMorph) {
				IMultiModelMorph morphAbility = (IMultiModelMorph) ability;
				if (morphAbility.isTransformationActive(entity)) {
					info.setReturnValue(morphAbility.getTransformation(entity));
					//HitoHitoNoMiNikaMod.LOGGER.info(morphAbility.getTransformation(entity).getForm());
					return;
				}
			}
		}
	}
}

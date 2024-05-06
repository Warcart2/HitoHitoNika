package net.mcreator.mineminenomitest;

import xyz.pixelatedw.mineminenomi.api.morph.MorphInfo;
import xyz.pixelatedw.mineminenomi.api.abilities.IMorphAbility;

import net.minecraft.entity.LivingEntity;

public interface IMultiModelMorph extends IMorphAbility {
	public default MorphInfo getTransformation() {
		return null;
	}
	public MorphInfo getTransformation(LivingEntity target);
}

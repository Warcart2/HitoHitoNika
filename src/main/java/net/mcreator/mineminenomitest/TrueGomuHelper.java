package net.mcreator.mineminenomitest;

import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.abilities.haki.BusoshokuHakiHardeningAbility;
import xyz.pixelatedw.mineminenomi.abilities.haki.BusoshokuHakiEmissionAbility;
import xyz.pixelatedw.mineminenomi.abilities.haki.BusoshokuHakiInternalDestructionAbility;

public class TrueGomuHelper {
	public static boolean canActivateGear(IAbilityData props, AbilityCore gear) {
		if (gear.equals(TrueGearSecondAbility.INSTANCE) && (hasGearFourthActive(props) || hasGearFifthActive(props))) {
			return false;
		} else if (gear.equals(TrueGearThirdAbility.INSTANCE) && hasGearFourthActive(props)) {
			return false;
		} else if (gear.equals(TrueGearFourthAbility.INSTANCE) && (hasGearThirdActive(props) || hasGearSecondActive(props))) {
			return false;
		} else if (gear.equals(FifthGearAbility.INSTANCE) && (hasGearThirdActive(props) || hasGearSecondActive(props) || hasGearFourthActive(props))) {
			return false;
		}
		return true;
	}

	public static boolean hasGearSecondActive(IAbilityData props) {
		Ability ability = props.getEquippedAbility(TrueGearSecondAbility.INSTANCE);
		boolean isActive = ability != null && ability.isContinuous();
		return isActive;
	}

	public static boolean hasGearThirdActive(IAbilityData props) {
		Ability ability = props.getEquippedAbility(TrueGearThirdAbility.INSTANCE);
		boolean isActive = ability != null && ability.isContinuous();
		return isActive;
	}

	public static boolean hasGearFourthBoundmanActive(IAbilityData props) {
		Ability ability = props.getEquippedAbility(TrueGearFourthAbility.INSTANCE);
		boolean isActive = ability != null && ability.isContinuous() && ((TrueGearFourthAbility) ability).isBoundman();
		return isActive;
	}

	public static boolean hasGearFourthActive(IAbilityData props) {
		Ability ability = props.getEquippedAbility(TrueGearFourthAbility.INSTANCE);
		boolean isActive = ability != null && ability.isContinuous();
		return isActive;
	}

	public static boolean hasGearFourthSnakemanActive(IAbilityData props) {
		Ability ability = props.getEquippedAbility(TrueGearFourthAbility.INSTANCE);
		boolean isActive = ability != null && ability.isContinuous() && ((TrueGearFourthAbility) ability).isSnakeman();
		return isActive;
	}

	public static boolean hasGearFifthActive(IAbilityData props) {
		Ability ability = props.getEquippedAbility(FifthGearAbility.INSTANCE);
		boolean isActive = ability != null && ability.isContinuous();
		return isActive;
	}

	public static boolean hasHakiEmissionActive(IAbilityData props) {
		return hasAbilityActive(props, BusoshokuHakiEmissionAbility.INSTANCE) || hasAbilityActive(props, BusoshokuHakiInternalDestructionAbility.INSTANCE);
	}

	public static boolean hasAbilityActive(IAbilityData props, AbilityCore ability) {
		Ability abl = props.getEquippedAbility(ability);
		boolean isActive = abl != null && abl.isContinuous();
		return isActive;
	}
}

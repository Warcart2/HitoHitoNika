package net.mcreator.mineminenomitest;

import xyz.pixelatedw.mineminenomi.init.ModI18n;
import xyz.pixelatedw.mineminenomi.init.ModEffects;
import xyz.pixelatedw.mineminenomi.init.ModAttributes;
import xyz.pixelatedw.mineminenomi.data.entity.haki.IHakiData;
import xyz.pixelatedw.mineminenomi.data.entity.haki.HakiDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.EntityStatsCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.api.helpers.AttributeHelper;
import xyz.pixelatedw.mineminenomi.api.abilities.StatsChangeAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.IParallelContinuousAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbilityModeSwitcher;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbilityMode;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCategory;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityAttributeModifier;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.abilities.haki.HaoshokuHakiInfusionAbility;
import xyz.pixelatedw.mineminenomi.abilities.haki.BusoshokuHakiInternalDestructionAbility;
import xyz.pixelatedw.mineminenomi.abilities.haki.BusoshokuHakiHardeningAbility;
import xyz.pixelatedw.mineminenomi.abilities.haki.BusoshokuHakiFullBodyHardeningAbility;
import xyz.pixelatedw.mineminenomi.abilities.haki.BusoshokuHakiEmissionAbility;

import net.minecraftforge.common.ForgeMod;

import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.Util;
import net.minecraft.potion.Effects;
import net.minecraft.potion.EffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.entity.LivingEntity;
import xyz.pixelatedw.mineminenomi.api.abilities.ContinuousAbility;

public class TrueGearThirdAbility extends StatsChangeAbility implements IParallelContinuousAbility, IAbilityModeSwitcher, IAbilityMode {
	public static final AbilityCore<TrueGearThirdAbility> INSTANCE;
	private static final AbilityAttributeModifier SPEED_MODIFIER;
	private static final AbilityAttributeModifier JUMP_MODIFIER;
	private static final AbilityAttributeModifier ARMOR_MODIFIER;
	private static final AbilityAttributeModifier STRENGTH_MODIFIER;
	private static final AbilityAttributeModifier REACH_MODIFIER;
	private static final AbilityAttributeModifier STEP_HEIGHT;
	private static final AbilityAttributeModifier KNOCKBACK_RESISTANCE;
	private static final AbilityAttributeModifier FALL_RESISTANCE_MODIFIER;
	private static final AbilityAttributeModifier TOUGHNESS_MODIFIER;
	private boolean secondGearWas = false;

	public TrueGearThirdAbility(AbilityCore core) {
		super(core);
		this.setDisplayName("Gear Third");
		this.setCustomIcon("Gear Third");
		this.onStartContinuityEvent = this::onStartContinuityEvent;
		this.afterContinuityStopEvent = this::afterContinuityStopEvent;
		this.duringContinuityEvent = this::onTick;
	}

	protected boolean onStartContinuityEvent(PlayerEntity player) {
		IAbilityData props = AbilityDataCapability.get(player);
		double time = EntityStatsCapability.get(player).getDoriki() * .01d;
		if (TrueGomuHelper.hasGearFifthActive(props)) {
			time /= 4;
		}
		if (!TrueGomuHelper.canActivateGear(props, INSTANCE)) {
			player.sendMessage(new TranslationTextComponent(ModI18n.ABILITY_MESSAGE_GEAR_ACTIVE), Util.DUMMY_UUID);
			return false;
		} else if (!canUnlock(player)) {
			player.sendMessage(new TranslationTextComponent("text.mineminenomi.too_weak"), Util.DUMMY_UUID);
			return false;
		} else {
			if (TrueGomuHelper.hasGearSecondActive(props)) {
				((TrueGearSecondAbility) props.getEquippedAbility(TrueGearSecondAbility.INSTANCE)).setThirdGear(true);
				this.secondGearWas = true;
			}
			this.enableModes(player, this);
			Ability fusen = props.getEquippedAbility(GomuFusenAbility.INSTANCE);
			((GomuMorphsAbility) props.getUnlockedAbility(GomuMorphsAbility.INSTANCE)).updateModes();
			if (fusen != null)
				((ContinuousAbility) fusen).tryStoppingContinuity(player);
			if (time >= 500) {
				this.setThreshold(0f);
			} else {
				this.setThreshold(time);
			}
			return true;
		}
	}

	protected void onTick(PlayerEntity player, int passiveTimer) {
		IAbilityData props = AbilityDataCapability.get(player);
		IHakiData haki = HakiDataCapability.get(player);
		if (TrueGomuHelper.hasAbilityActive(props, BusoshokuHakiHardeningAbility.INSTANCE)) {
			haki.alterHakiOveruse(1);
		}
		if (TrueGomuHelper.hasAbilityActive(props, BusoshokuHakiEmissionAbility.INSTANCE)) {
			haki.alterHakiOveruse(2);
		}
		if (TrueGomuHelper.hasAbilityActive(props, BusoshokuHakiInternalDestructionAbility.INSTANCE)) {
			haki.alterHakiOveruse(4);
		}
		if (TrueGomuHelper.hasAbilityActive(props, HaoshokuHakiInfusionAbility.INSTANCE)) {
			haki.alterHakiOveruse(12);
		}
		if (TrueGomuHelper.hasAbilityActive(props, BusoshokuHakiFullBodyHardeningAbility.INSTANCE) && TrueGomuHelper.hasGearFifthActive(props)) {
			haki.alterHakiOveruse(2);
		}
	}

	protected void afterContinuityStopEvent(PlayerEntity player) {
		int cooldown = (int) Math.round((double) this.continueTime / 20.0D);
		this.setMaxCooldown((double) (2 + cooldown));
		if (this.secondGearWas && EntityStatsCapability.get(player).getDoriki() < 3500.0D) {
			player.addPotionEffect(new EffectInstance(ModEffects.UNCONSCIOUS.get(), 300, 1, true, true));
		} else if (EntityStatsCapability.get(player).getDoriki() < 3000.0D) {
			player.addPotionEffect(new EffectInstance(Effects.WEAKNESS, 300, 1, true, true));
			player.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 300, 1, true, true));
		}
		this.disableModes(player, this);
		TrueGearSecondAbility secondGear = (TrueGearSecondAbility) AbilityDataCapability.get(player).getEquippedAbility(TrueGearSecondAbility.INSTANCE);
		if (secondGear != null && secondGear.isContinuous()) {
			secondGear.tryStoppingContinuity(player);
		}
		this.setSecondGear(false);
		IAbilityData props = AbilityDataCapability.get(player);
		((GomuMorphsAbility) props.getUnlockedAbility(GomuMorphsAbility.INSTANCE)).updateModes();
	}

	public void setSecondGear(boolean newWas) {
		this.secondGearWas = newWas;
	}

	public void disableMode(Ability ignored) {
		this.setCustomIcon("Gear Third");
		this.setDisplayName("Gear Third");
		this.removeAllModifier();
	}

	public void enableMode(Ability ignored) {
		this.setCustomIcon("Gigant");
		this.setDisplayName("Gomu Gomu Gigant");
		this.addModifier(Attributes.MOVEMENT_SPEED, SPEED_MODIFIER);
		this.addModifier(ModAttributes.JUMP_HEIGHT, JUMP_MODIFIER);
		this.addModifier(Attributes.ARMOR, ARMOR_MODIFIER);
		this.addModifier(ModAttributes.PUNCH_DAMAGE, STRENGTH_MODIFIER);
		this.addModifier(ForgeMod.REACH_DISTANCE, REACH_MODIFIER);
		this.addModifier(ModAttributes.ATTACK_RANGE, REACH_MODIFIER);
		this.addModifier(ModAttributes.STEP_HEIGHT, STEP_HEIGHT);
		this.addModifier(Attributes.ATTACK_KNOCKBACK, KNOCKBACK_RESISTANCE);
		this.addModifier(ModAttributes.FALL_RESISTANCE, FALL_RESISTANCE_MODIFIER);
		this.addModifier(ModAttributes.TOUGHNESS, TOUGHNESS_MODIFIER);
	}

	public AbilityCore[] getParents() {
		return new AbilityCore[]{FifthGearAbility.INSTANCE};
	}

	private static AbilityCore creation() {
		AbilityCore out = (new AbilityCore.Builder("True Gear Third", AbilityCategory.DEVIL_FRUITS, TrueGearThirdAbility::new)).addDescriptionLine("By blowing air and inflating their body, the user's attacks get bigger and gain incredible strength")
				.setUnlockCheck(TrueGearThirdAbility::canUnlock).build();
		return out;
	}

	protected void removeAllModifier() {
		this.getModifiers().clear();
	}

	protected static boolean canUnlock(LivingEntity user) {
		return EntityStatsCapability.get(user).getDoriki() * .01d >= 20d;
	}

	static {
		INSTANCE = creation();
		SPEED_MODIFIER = new AbilityAttributeModifier(AttributeHelper.MORPH_MOVEMENT_SPEED_UUID, INSTANCE, "Mega Mega Speed Modifier", 1.0199999809265137D, Operation.MULTIPLY_BASE);
		JUMP_MODIFIER = new AbilityAttributeModifier(AttributeHelper.MORPH_JUMP_BOOST_UUID, INSTANCE, "Mega Mega Jump Modifier", 2.0D, Operation.ADDITION);
		ARMOR_MODIFIER = new AbilityAttributeModifier(AttributeHelper.MORPH_ARMOR_UUID, INSTANCE, "Mega Mega Armor Modifier", 5.0D, Operation.ADDITION);
		STRENGTH_MODIFIER = new AbilityAttributeModifier(AttributeHelper.MORPH_STRENGTH_UUID, INSTANCE, "Mega Mega Strength Modifier", 3.0D, Operation.ADDITION);
		REACH_MODIFIER = new AbilityAttributeModifier(AttributeHelper.MORPH_ATTACK_REACH_UUID, INSTANCE, "Mega Mega Reach Modifier", 5.0D, Operation.ADDITION);
		STEP_HEIGHT = new AbilityAttributeModifier(AttributeHelper.MORPH_STEP_HEIGHT_UUID, INSTANCE, "Mega Mega Step Height Modifier", 1.5D, Operation.ADDITION);
		KNOCKBACK_RESISTANCE = new AbilityAttributeModifier(AttributeHelper.MORPH_KNOCKBACK_RESISTANCE_UUID, INSTANCE, "Mega Mega Knockback Resistance Modifier", 1.0D, Operation.ADDITION);
		FALL_RESISTANCE_MODIFIER = new AbilityAttributeModifier(AttributeHelper.MORPH_FALL_RESISTANCE_UUID, INSTANCE, "Mega Mega Fall Resistance Modifier", 10.0D, Operation.ADDITION);
		TOUGHNESS_MODIFIER = new AbilityAttributeModifier(AttributeHelper.MORPH_TOUGHNESS_UUID, INSTANCE, "Mega Mega Toughness Modifier", 4.0D, Operation.ADDITION);
	}
}

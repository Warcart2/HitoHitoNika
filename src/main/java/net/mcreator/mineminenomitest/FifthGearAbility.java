package net.mcreator.mineminenomitest;

import xyz.pixelatedw.mineminenomi.wypi.WyHelper;
import xyz.pixelatedw.mineminenomi.init.ModParticleEffects;
import xyz.pixelatedw.mineminenomi.init.ModI18n;
import xyz.pixelatedw.mineminenomi.init.ModEffects;
import xyz.pixelatedw.mineminenomi.init.ModAttributes;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.EntityStatsCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.api.abilities.IParallelContinuousAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.IBodyOverlayAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbilityModeSwitcher;
import xyz.pixelatedw.mineminenomi.api.abilities.ContinuousAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityOverlay;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCategory;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityAttributeModifier;

import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.Util;
import net.minecraft.util.ResourceLocation;
import net.minecraft.potion.EffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.LivingEntity;

import java.util.UUID;

public class FifthGearAbility extends ContinuousAbility implements IBodyOverlayAbility, IParallelContinuousAbility, IAbilityModeSwitcher {
	public static final AbilityCore<FifthGearAbility> INSTANCE;
	private static final AbilityOverlay OVERLAY;
	private static final AbilityAttributeModifier DAMAGE_REDUCTION_MODIFIER;
	private static final AbilityAttributeModifier STRENGTH_MODIFIER;

	public FifthGearAbility(AbilityCore core) {
		super(core);
		this.onStartContinuityEvent = this::onStartContinuity;
		this.duringContinuityEvent = this::duringContinuity;
		this.afterContinuityStopEvent = this::afterContinuityStop;
	}

	private void duringContinuity(PlayerEntity player, int passiveTimer) {
		if (passiveTimer % 4 == 0) {
			WyHelper.spawnParticleEffect(ModParticleEffects.GEAR_SECOND.get(), player, player.getPosX(), player.getPosY(), player.getPosZ());
		}
	}

	private boolean onStartContinuity(PlayerEntity player) {
		double time = EntityStatsCapability.get(player).getDoriki() * .003d;
		if (!TrueGomuHelper.canActivateGear(AbilityDataCapability.get(player), INSTANCE)) {
			player.sendMessage(new TranslationTextComponent(ModI18n.ABILITY_MESSAGE_GEAR_ACTIVE), Util.DUMMY_UUID);
			return false;
		} else if (!canUnlock(player)) {
			player.sendMessage(new TranslationTextComponent("text.mineminenomi.too_weak"), Util.DUMMY_UUID);
			return false;
		}
		player.getAttribute(ModAttributes.PUNCH_DAMAGE.get()).applyNonPersistentModifier(STRENGTH_MODIFIER);
		player.getAttribute(ModAttributes.DAMAGE_REDUCTION.get()).applyNonPersistentModifier(DAMAGE_REDUCTION_MODIFIER);
		if (time >= 500) {
			this.setThreshold(0f);
		} else {
			this.setThreshold(time);
		}
		this.enableModes(player, this);
		IAbilityData props = AbilityDataCapability.get(player);
		((GomuMorphsAbility) props.getUnlockedAbility(GomuMorphsAbility.INSTANCE)).updateModes();
		return true;
	}

	private void afterContinuityStop(PlayerEntity player) {
		player.getAttribute(ModAttributes.PUNCH_DAMAGE.get()).removeModifier(STRENGTH_MODIFIER);
		player.getAttribute(ModAttributes.DAMAGE_REDUCTION.get()).removeModifier(DAMAGE_REDUCTION_MODIFIER);
		player.addPotionEffect(new EffectInstance(ModEffects.ABILITY_OFF.get(), this.continueTime / 4, 1, true, true));
		player.addPotionEffect(new EffectInstance(ModEffects.PARALYSIS.get(), this.continueTime / 4, 1, true, true));
		this.setMaxCooldown(this.getContinueTime() / 20);
		this.disableModes(player, this);
		IAbilityData props = AbilityDataCapability.get(player);
		((GomuMorphsAbility) props.getUnlockedAbility(GomuMorphsAbility.INSTANCE)).updateModes();
	}

	public AbilityOverlay getBodyOverlay(LivingEntity entity) {
		return OVERLAY;
	}

	protected static boolean canUnlock(LivingEntity user) {
		return EntityStatsCapability.get(user).getDoriki() * .003d >= 25d;
	}

	static {
		INSTANCE = new AbilityCore.Builder("Gear Fifth", AbilityCategory.DEVIL_FRUITS, FifthGearAbility::new).setUnlockCheck(FifthGearAbility::canUnlock)
				.addDescriptionLine("Awakening ability, that makes you insanely powerful\n\nWhile active transforms §2Gomu Gomu no Rocket§r into §2Bajrang Gun§r").build();
		OVERLAY = new AbilityOverlay.Builder().setTexture(new ResourceLocation("hito_hito_no_mi_nika", "textures/models/gear_5_overlay.png")).build();
		STRENGTH_MODIFIER = new AbilityAttributeModifier(UUID.fromString("5fc1a28f-7e59-44bf-9d7a-36953e9c700d"), FifthGearAbility.INSTANCE, "Gear Fifth Attack Damage Modifier", 20.0, AttributeModifier.Operation.ADDITION);
		DAMAGE_REDUCTION_MODIFIER = new AbilityAttributeModifier(UUID.fromString("2efdb212-33d0-4fad-b806-4d39d7091ffd"), FifthGearAbility.INSTANCE, "Gear Fifth Resistance Damage Modifier", 40, AttributeModifier.Operation.ADDITION);
	}
}

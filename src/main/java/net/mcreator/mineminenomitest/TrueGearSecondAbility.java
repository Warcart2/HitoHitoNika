package net.mcreator.mineminenomitest;

import xyz.pixelatedw.mineminenomi.wypi.WyHelper;
import xyz.pixelatedw.mineminenomi.particles.effects.ParticleEffect;
import xyz.pixelatedw.mineminenomi.init.ModSounds;
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
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.Util;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundCategory;
import net.minecraft.potion.Effects;
import net.minecraft.potion.EffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.LivingEntity;

import java.util.UUID;

import java.awt.Color;
import xyz.pixelatedw.mineminenomi.abilities.gomu.GearThirdAbility;

public class TrueGearSecondAbility extends ContinuousAbility implements IBodyOverlayAbility, IParallelContinuousAbility, IAbilityModeSwitcher {
	public static final AbilityCore<TrueGearSecondAbility> INSTANCE;
	private static final AbilityOverlay OVERLAY;
	private static final AbilityAttributeModifier JUMP_HEIGHT;
	private static final AbilityAttributeModifier STRENGTH_MODIFIER;
	private static final AbilityAttributeModifier STEP_HEIGHT;
	private boolean thirdGearWas = false;
	private boolean prevSprintValue = false;

	public TrueGearSecondAbility(AbilityCore core) {
		super(core);
		this.setCustomIcon("Gear Second");
		this.setDisplayName("Gear Second");
		this.onStartContinuityEvent = this::onStartContinuity;
		this.duringContinuityEvent = this::duringContinuity;
		this.afterContinuityStopEvent = this::afterContinuityStopEvent;
	}

	private boolean onStartContinuity(PlayerEntity player) {
		double time = EntityStatsCapability.get(player).getDoriki() * .02d;
		IAbilityData props = AbilityDataCapability.get(player);
		if (!TrueGomuHelper.canActivateGear(props, INSTANCE)) {
			player.sendMessage(new TranslationTextComponent(ModI18n.ABILITY_MESSAGE_GEAR_ACTIVE), Util.DUMMY_UUID);
			return false;
		} else if (!canUnlock(player)) {
			player.sendMessage(new TranslationTextComponent("text.mineminenomi.too_weak"), Util.DUMMY_UUID);
			return false;
		} else {
			if (TrueGomuHelper.hasGearThirdActive(props)) {
				((TrueGearThirdAbility) props.getEquippedAbility(TrueGearThirdAbility.INSTANCE)).setSecondGear(true);
				this.thirdGearWas = true;
			}
			if (time >= 500) {
				this.setThreshold(0f);
			} else {
				this.setThreshold(time);
			}
			this.enableModes(player, this);
			player.getAttribute((Attribute) ModAttributes.STEP_HEIGHT.get()).applyNonPersistentModifier(STEP_HEIGHT);
			player.getAttribute(Attributes.MOVEMENT_SPEED).applyNonPersistentModifier(STEP_HEIGHT);
			player.getAttribute((Attribute) ModAttributes.JUMP_HEIGHT.get()).applyNonPersistentModifier(JUMP_HEIGHT);
			player.getAttribute(Attributes.ATTACK_DAMAGE).applyNonPersistentModifier(STRENGTH_MODIFIER);
			player.getAttribute((Attribute) ModAttributes.PUNCH_DAMAGE.get()).applyNonPersistentModifier(STRENGTH_MODIFIER);
			player.world.playSound((PlayerEntity) null, player.getPosition(), (SoundEvent) ModSounds.GEAR_SECOND_SFX.get(), SoundCategory.PLAYERS, 0.5F, 1.0F);
			this.prevSprintValue = player.isSprinting();
			return true;
		}
	}

	private void duringContinuity(PlayerEntity player, int passiveTimer) {
		if (passiveTimer % 10 == 0) {
			WyHelper.spawnParticleEffect((ParticleEffect) ModParticleEffects.GEAR_SECOND.get(), player, player.getPosX(), player.getPosY(), player.getPosZ());
		}
		if (player.isSprinting()) {
			if (!this.prevSprintValue) {
				player.world.playSound((PlayerEntity) null, player.getPosition(), (SoundEvent) ModSounds.TELEPORT_SFX.get(), SoundCategory.PLAYERS, 0.5F, 1.0F);
			}
			float maxSpeed = 2.2F;
			Vector3d vec = player.getMotion();
			double var10001;
			double var10003;
			if (player.isSprinting()) {
				var10001 = vec.x * (double) maxSpeed;
				var10003 = vec.z * (double) maxSpeed;
				player.setMotion(var10001, player.getMotion().y, var10003);
			} else {
				var10001 = vec.x * (double) maxSpeed * 0.5D;
				var10003 = vec.z * (double) maxSpeed;
				player.setMotion(var10001, player.getMotion().y, var10003 * 0.5D);
			}
			this.prevSprintValue = player.isSprinting();
			player.isAirBorne = true;
		} else {
			this.prevSprintValue = false;
		}
	}

	private void afterContinuityStopEvent(PlayerEntity player) {
		int cooldown = (int) Math.round((double) this.continueTime / 30.0D);
		this.setMaxCooldown((double) (1 + cooldown));
		if (this.thirdGearWas && EntityStatsCapability.get(player).getDoriki() < 3500.0D) {
			player.addPotionEffect(new EffectInstance(ModEffects.UNCONSCIOUS.get(), 300, 1, true, true));
		} else if ((double) this.continueTime > (double) this.getThreshold() / 1.425D && EntityStatsCapability.get(player).getDoriki() < 2000.0D) {
			player.addPotionEffect(new EffectInstance(Effects.HUNGER, 600, 3, true, true));
			player.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 200, 1, true, true));
		}
		this.disableModes(player, this);
		player.getAttribute((Attribute) ModAttributes.STEP_HEIGHT.get()).removeModifier(STEP_HEIGHT);
		player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(STEP_HEIGHT);
		player.getAttribute((Attribute) ModAttributes.JUMP_HEIGHT.get()).removeModifier(JUMP_HEIGHT);
		player.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(STRENGTH_MODIFIER);
		player.getAttribute((Attribute) ModAttributes.PUNCH_DAMAGE.get()).removeModifier(STRENGTH_MODIFIER);
		TrueGearThirdAbility thirdGear = (TrueGearThirdAbility) AbilityDataCapability.get(player).getEquippedAbility(TrueGearThirdAbility.INSTANCE);
		if(thirdGear != null && thirdGear.isContinuous()) {
			thirdGear.tryStoppingContinuity(player);
		}
		this.setThirdGear(false);
	}

	public AbilityOverlay getBodyOverlay(LivingEntity entity) {
		return OVERLAY;
	}

	public void setThirdGear(boolean newWas) {
		this.thirdGearWas = newWas;
	}

	protected static boolean canUnlock(LivingEntity user) {
		return EntityStatsCapability.get(user).getDoriki() * .02d >= 20d;
	}

	static {
		INSTANCE = (new AbilityCore.Builder("True Gear Second", AbilityCategory.DEVIL_FRUITS, TrueGearSecondAbility::new)).addDescriptionLine("By speding up their blood flow, the user gains strength, speed and mobility").setUnlockCheck(TrueGearSecondAbility::canUnlock).build();
		OVERLAY = (new AbilityOverlay.Builder()).setColor(new Color(232, 54, 54, 74)).build();
		JUMP_HEIGHT = new AbilityAttributeModifier(UUID.fromString("a44a9644-369a-4e18-88d9-323727d3d85b"), INSTANCE, "Gear Second Jump Modifier", 5.0D, Operation.ADDITION);
		STRENGTH_MODIFIER = new AbilityAttributeModifier(UUID.fromString("a2337b58-7e6d-4361-a8ca-943feee4f906"), INSTANCE, "Gear Second Attack Damage Modifier", 4.0D, Operation.ADDITION);
		STEP_HEIGHT = new AbilityAttributeModifier(UUID.fromString("eab680cd-a6dc-438a-99d8-46f9eb53a950"), INSTANCE, "Gear Second Step Height Modifier", 1.0D, Operation.ADDITION);
	}
}

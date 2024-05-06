package net.mcreator.mineminenomitest;

import xyz.pixelatedw.mineminenomi.wypi.WyNetwork;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;
import xyz.pixelatedw.mineminenomi.particles.effects.ParticleEffect;
import xyz.pixelatedw.mineminenomi.packets.server.ability.SUpdateEquippedAbilityPacket;
import xyz.pixelatedw.mineminenomi.packets.server.SSyncAbilityDataPacket;
import xyz.pixelatedw.mineminenomi.init.ModResources;
import xyz.pixelatedw.mineminenomi.init.ModParticleEffects;
import xyz.pixelatedw.mineminenomi.init.ModI18n;
import xyz.pixelatedw.mineminenomi.init.ModEffects;
import xyz.pixelatedw.mineminenomi.init.ModAttributes;
import xyz.pixelatedw.mineminenomi.data.entity.haki.HakiDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.EntityStatsCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.api.helpers.HakiHelper;
import xyz.pixelatedw.mineminenomi.api.helpers.AttributeHelper;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.api.enums.HakiType;
import xyz.pixelatedw.mineminenomi.api.abilities.StatsChangeAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.IParallelContinuousAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.IHakiAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.IExtraUpdateData;
import xyz.pixelatedw.mineminenomi.api.abilities.IBodyOverlayAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbilityModeSwitcher;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbilityMode;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityOverlay;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCategory;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityAttributeModifier;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;

import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.Util;
import net.minecraft.util.ResourceLocation;
import net.minecraft.potion.Effects;
import net.minecraft.potion.EffectInstance;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.entity.LivingEntity;

public class TrueGearFourthAbility extends StatsChangeAbility implements IHakiAbility, IBodyOverlayAbility, IParallelContinuousAbility, IAbilityModeSwitcher, IAbilityMode, IExtraUpdateData {
	public static final AbilityCore<TrueGearFourthAbility> INSTANCE;
	private static final AbilityAttributeModifier ARMOR_MODIFIER;
	private static final AbilityAttributeModifier STRENGTH_MODIFIER;
	private static final AbilityAttributeModifier DAMAGE_REDUCTION_MODIFIER;
	private static final AbilityOverlay OVERLAY;
	private static final AbilityOverlay OVERLAY_FIFTH;
	private static final AbilityOverlay OVERLAY_SNAKEMAN;
	protected Mode mode = Mode.BOUNDMAN;
	public float speed = 0.0F;
	private double currentHP = 0.0D;
	protected int targetedTime = 30;
	protected boolean onTargetedTime = false;
	protected boolean isBonusTime = false;

	public TrueGearFourthAbility(AbilityCore core) {
		super(core);
		this.setCustomIcon("Gear Fourth");
		this.setDisplayName("Gear Fourth");
		this.addModifier(Attributes.ARMOR, ARMOR_MODIFIER);
		this.addModifier(Attributes.ARMOR_TOUGHNESS, ARMOR_MODIFIER);
		this.addModifier(ModAttributes.PUNCH_DAMAGE, STRENGTH_MODIFIER);
		this.addModifier(ModAttributes.DAMAGE_REDUCTION, DAMAGE_REDUCTION_MODIFIER);
		this.needsClientSide();
		this.onStartContinuityEvent = this::onStartContinuityEvent;
		this.duringContinuityEvent = this::duringContinuity;
		this.afterContinuityStopEvent = this::afterContinuityStopEvent;
		this.beforeContinuityStopEvent = this::beforeContinuityStopEvent;
	}

	public boolean onStartContinuityEvent(PlayerEntity player) {
		IAbilityData props = AbilityDataCapability.get(player);
		if (player.isCrouching()) {
			if (this.mode == Mode.BOUNDMAN) {
				this.mode = Mode.SNAKEMAN;
			} else {
				this.mode = Mode.BOUNDMAN;
			}
			player.sendMessage(new StringTextComponent("Changed Mode to " + this.mode), Util.DUMMY_UUID);
			WyNetwork.sendTo(new SSyncAbilityDataPacket(player.getEntityId(), props), player);
			return false;
		} else if (!TrueGomuHelper.canActivateGear(props, INSTANCE)) {
			player.sendMessage(new TranslationTextComponent(ModI18n.ABILITY_MESSAGE_GEAR_ACTIVE), Util.DUMMY_UUID);
			return false;
		} else if (!canUnlock(player)) {
			player.sendMessage(new TranslationTextComponent("text.mineminenomi.too_weak"), Util.DUMMY_UUID);
			return false;
		} else if (TrueGomuHelper.hasGearFifthActive(props) && this.isSnakeman()) {
			player.sendMessage(new TranslationTextComponent(ModI18n.ABILITY_MESSAGE_GEAR_ACTIVE), Util.DUMMY_UUID);
			return false;
		} else {
			this.onStartContinuity(player);
			this.enableModes(player, this);
			((GomuMorphsAbility) props.getUnlockedAbility(GomuMorphsAbility.INSTANCE)).updateModes();
			this.currentHP = (double) player.getHealth();
			return true;
		}
	}

	private void onStartContinuity(PlayerEntity player) {
		double time = EntityStatsCapability.get(player).getDoriki() * .005d;
		if (time >= 500) {
			this.setThreshold(0f);
		} else {
			this.setThreshold(time);
		}
	}

	public void duringContinuity(PlayerEntity player, int passiveTimer) {
		int diff;
		if (passiveTimer % 2 == 0 && !this.isSnakeman()) {
			WyHelper.spawnParticleEffect((ParticleEffect) ModParticleEffects.GEAR_SECOND.get(), player, player.getPosX(), player.getPosY() + 1.0D, player.getPosZ());
		}
		boolean isOnMaxOveruse = HakiHelper.checkForHakiOveruse(player, 2);
		if (isOnMaxOveruse || AbilityHelper.isNearbyKairoseki(player)) {
			this.tryStoppingContinuity(player);
		}
		if (this.onTargetedTime) {
			for (int i = 0; i < 3; i++) {
				WyHelper.spawnParticleEffect((ParticleEffect) ModParticleEffects.GEAR_SECOND.get(), player, player.getPosX(), player.getPosY() + 1.0D, player.getPosZ());
			}
			--this.targetedTime;
		}
		if (this.targetedTime <= 0) {
			this.tryStoppingContinuity(player);
		}
		if (this.isBoundman()) {
			player.abilities.allowFlying = true;
		}
		if (!TrueGomuHelper.hasGearFifthActive(AbilityDataCapability.get(player))) {
			HakiDataCapability.get(player).alterHakiOveruse(5);
		}
		if (player.isOnGround() && !(TrueGomuHelper.hasGearFifthActive(AbilityDataCapability.get(player)) || this.isSnakeman())) {
			player.addVelocity(0, 1, 0);
		}
		if (this.currentHP > (double) player.getHealth() + WyHelper.randomWithRange(3, 6)) {
			diff = Math.abs((int) ((double) player.getHealth() - this.currentHP));
			this.setThresholdInTicks(Math.max((int) ((float) this.threshold * (1.0F - (float) diff / 20.0F)), 0));
			IAbilityData props = AbilityDataCapability.get(player);
			WyNetwork.sendTo(new SSyncAbilityDataPacket(player.getEntityId(), props), player);
			WyNetwork.sendToAllTrackingAndSelf(new SUpdateEquippedAbilityPacket(player, this), player);
		}
		this.currentHP = (double) player.getHealth();
	}

	protected boolean beforeContinuityStopEvent(PlayerEntity player) {
		player.abilities.isFlying = false;
		if (this.isBoundman()) {
			player.abilities.allowFlying = false;
		}
		player.sendPlayerAbilities();
		if (this.targetedTime > 0 && this.continueTime >= this.getThreshold() && this.getThreshold() > 0 && !this.isBonusTime) {
			this.setThreshold(0d);
			this.onTargetedTime = true;
			WyNetwork.sendToAllTrackingAndSelf(new SUpdateEquippedAbilityPacket(player, this), player);
			return false;
		}
		if (this.onTargetedTime && this.targetedTime > 0) {
			this.onTargetedTime = false;
			this.isBonusTime = true;
			this.continueTime = 0;
			this.onStartContinuity(player);
			WyNetwork.sendToAllTrackingAndSelf(new SUpdateEquippedAbilityPacket(player, this), player);
			WyNetwork.sendTo(new SSyncAbilityDataPacket(player.getEntityId(), AbilityDataCapability.get(player)), player);
			return false;
		}
		return true;
	}

	public void afterContinuityStopEvent(PlayerEntity player) {
		int duration = (int) (600.0F - HakiDataCapability.get(player).getTotalHakiExp());
		if ((this.continueTime > this.getThreshold() / 10 && this.getThreshold() != 0 && !TrueGomuHelper.hasGearFifthActive(AbilityDataCapability.get(player))) || this.isBonusTime) {
			player.addPotionEffect(new EffectInstance(Effects.SLOWNESS, duration, 3, true, true));
			player.addPotionEffect(new EffectInstance(Effects.HUNGER, duration, 1, true, true));
			player.addPotionEffect(new EffectInstance(Effects.WEAKNESS, duration, 3, true, true));
		}
		if (this.isBonusTime) {
			player.addPotionEffect(new EffectInstance(ModEffects.ABILITY_OFF.get(), duration + 100, 1, true, true));
			player.addPotionEffect(new EffectInstance(ModEffects.PARALYSIS.get(), duration + 100, 1, true, true));
		}
		this.disableModes(player, this);
		IAbilityData props = AbilityDataCapability.get(player);
		((GomuMorphsAbility) props.getUnlockedAbility(GomuMorphsAbility.INSTANCE)).updateModes();
		int cooldown = duration / 60 + (int) Math.round((double) this.continueTime / 30.0D);
		this.setMaxCooldown((double) (1 + cooldown));
		this.targetedTime = 30;
		this.onTargetedTime = false;
		this.isBonusTime = false;
	}

	public AbilityOverlay getBodyOverlay(LivingEntity entity) {
		IAbilityData props = AbilityDataCapability.get(entity);
		Ability abl = props.getEquippedAbility(INSTANCE);
		if (TrueGomuHelper.hasGearFifthActive(props)) {
			return OVERLAY_FIFTH;
		} else if (abl != null && abl instanceof TrueGearFourthAbility && ((TrueGearFourthAbility) abl).getMode() == Mode.SNAKEMAN) {
			return OVERLAY_SNAKEMAN;
		}
		return OVERLAY;
	}

	public HakiType getType() {
		return HakiType.BUSOSHOKU;
	}

	protected static boolean canUnlock(LivingEntity user) {
		return EntityStatsCapability.get(user).getDoriki() * .005d >= 25d && HakiDataCapability.get(user).getBusoshokuHakiExp() > HakiHelper.getBusoshokuFullBodyExpNeeded(user);
	}

	public AbilityCore[] getParents() {
		return new AbilityCore[]{FifthGearAbility.INSTANCE};
	}

	public void disableMode(Ability ignored) {
		this.setCustomIcon("Gear Fourth");
		this.setDisplayName("Gear Fourth");
	}

	public void enableMode(Ability ignored) {
		this.setCustomIcon("G4 Muscles");
		this.setDisplayName("Muscles");
	}

	public void setMode(Mode newMode) {
		this.mode = newMode;
	}

	public Mode getMode() {
		return this.mode;
	}

	public void setExtraData(CompoundNBT tag) {
		this.setMode(Mode.getFromString(tag.getString("g4_mode")));
	}

	public CompoundNBT getExtraData() {
		CompoundNBT out = new CompoundNBT();
		out.putString("g4_mode", "" + this.mode);
		return out;
	}

	public boolean isSnakeman() {
		return this.mode == Mode.SNAKEMAN;
	}

	public boolean isBoundman() {
		return this.mode == Mode.BOUNDMAN;
	}

	static {
		INSTANCE = (new AbilityCore.Builder("True Gear Fourth", AbilityCategory.DEVIL_FRUITS, TrueGearFourthAbility::new))
				.addDescriptionLine("The user inflates their muscle structure to tremendously increase the power of their attacks and also allows flight\n\n§2Uses Haki§r").setUnlockCheck(TrueGearFourthAbility::canUnlock).build();
		ARMOR_MODIFIER = new AbilityAttributeModifier(AttributeHelper.MORPH_ARMOR_UUID, INSTANCE, "Gear Fourth Armor Modifier", 10.0D, Operation.ADDITION);
		STRENGTH_MODIFIER = new AbilityAttributeModifier(AttributeHelper.MORPH_STRENGTH_UUID, INSTANCE, "Gear Fourth Attack Damage Modifier", 15.0D, Operation.ADDITION);
		DAMAGE_REDUCTION_MODIFIER = new AbilityAttributeModifier(AttributeHelper.MORPH_DAMAGE_REDUCTION_UUID, INSTANCE, "Gear Fourth Resistance Damage Modifier", 0.35D, Operation.ADDITION);
		OVERLAY_FIFTH = (new AbilityOverlay.Builder()).setTexture(new ResourceLocation("mineminenomi", "textures/models/zoanmorph/gear_3.png")).build();
		OVERLAY = (new AbilityOverlay.Builder()).setTexture(ModResources.G4_OVERLAY).build();
		OVERLAY_SNAKEMAN = new AbilityOverlay.Builder().setTexture(new ResourceLocation("hito_hito_no_mi_nika", "textures/models/gear_4_snakeman_overlay.png")).build();
	}

	public static enum Mode {
		BOUNDMAN, SNAKEMAN;

		public static Mode getFromString(String s) {
			if (s.equalsIgnoreCase("snakeman")) {
				return SNAKEMAN;
			}
			return BOUNDMAN;
		}
	}
}

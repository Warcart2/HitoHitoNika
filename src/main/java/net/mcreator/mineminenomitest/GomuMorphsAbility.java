package net.mcreator.mineminenomitest;

import xyz.pixelatedw.mineminenomi.wypi.WyNetwork;
import xyz.pixelatedw.mineminenomi.packets.server.ability.SRecalculateEyeHeightPacket;
import xyz.pixelatedw.mineminenomi.packets.server.SSyncDevilFruitPacket;
import xyz.pixelatedw.mineminenomi.packets.server.SSyncAbilityDataPacket;
import xyz.pixelatedw.mineminenomi.packets.client.CSyncZoanPacket;
import xyz.pixelatedw.mineminenomi.items.AkumaNoMiItem;
import xyz.pixelatedw.mineminenomi.entities.zoan.MegaMorphInfo;
import xyz.pixelatedw.mineminenomi.entities.zoan.GearFourthMorphInfo;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.IDevilFruit;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.DevilFruitCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.api.morph.MorphModel;
import xyz.pixelatedw.mineminenomi.api.morph.MorphInfo;
import xyz.pixelatedw.mineminenomi.api.abilities.PassiveAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCategory;

import net.minecraftforge.event.entity.EntityEvent.Size;
import net.minecraftforge.common.MinecraftForge;

import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.EntitySize;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

public class GomuMorphsAbility extends PassiveAbility implements IMultiModelMorph {
	public static final AbilityCore<GomuMorphsAbility> INSTANCE;
	private static final EntitySize STANDING_SIZE_SNAKEMAN = EntitySize.flexible(0.7F, 1.9875F);
	private static final EntitySize CROUCHING_SIZE_SNAKEMAN = EntitySize.flexible(0.8F, 1.8F);
	private static final EntitySize STANDING_SIZE_FUSEN = EntitySize.flexible(1.6F, 2.8F);
	private static final EntitySize CROUCHING_SIZE_FUSEN = EntitySize.flexible(1.7F, 2.6F);
	private static final EntitySize STANDING_SIZE_GIGANT_FUSEN = EntitySize.flexible(3.6F, 4.8F);
	private static final EntitySize CROUCHING_SIZE_GIGANT_FUSEN = EntitySize.flexible(3.7F, 4.6F);
	private static final MorphInfo GIGANT = new MegaMorphInfo() {
		public String getDisplayName() {
			return "Gomu Gomu Gigant";
		}

		public AkumaNoMiItem getDevilFruit() {
			return TrueGomuGomuNoMi.HITO_HITO_NO_MI_NIKA;
		}

		public String getForm() {
			return "gear_3_gigant";
		}
	};
	private static final MorphInfo BOUNDMAN = new GearFourthMorphInfo() {
		public AkumaNoMiItem getDevilFruit() {
			return TrueGomuGomuNoMi.HITO_HITO_NO_MI_NIKA;
		}
	};
	private static final MorphInfo SNAKEMAN = new MorphInfo() {
		public String getDisplayName() {
			return "Gomu Gomu Gear Fourth Snakeman";
		}

		public ResourceLocation getTexture(AbstractClientPlayerEntity entity) {
			return entity.getLocationSkin();
		}

		public MorphModel getModel() {
			return new SnakemanMorphModel();
		}

		public AkumaNoMiItem getDevilFruit() {
			return TrueGomuGomuNoMi.HITO_HITO_NO_MI_NIKA;
		}

		public String getForm() {
			return "gear_4_snakeman";
		}

		public double getEyeHeight() {
			return 1.8;
		}

		public float getShadowSize() {
			return 1.2f;
		}

		public Map<Pose, EntitySize> getSizes() {
			return ImmutableMap.<Pose, EntitySize>builder().put(Pose.STANDING, STANDING_SIZE_SNAKEMAN).put(Pose.CROUCHING, CROUCHING_SIZE_SNAKEMAN).build();
		}
	};
	private static final MorphInfo FUSEN = new MorphInfo() {
		public String getDisplayName() {
			return "Gomu Gomu no Fusen";
		}

		public ResourceLocation getTexture(AbstractClientPlayerEntity entity) {
			return entity.getLocationSkin();
		}

		public MorphModel getModel() {
			return new GomuFusenModel();
		}

		public AkumaNoMiItem getDevilFruit() {
			return TrueGomuGomuNoMi.HITO_HITO_NO_MI_NIKA;
		}

		public String getForm() {
			return "fusen";
		}

		public double getEyeHeight() {
			return 2.8;
		}

		public float getShadowSize() {
			return 1f;
		}

		public Map<Pose, EntitySize> getSizes() {
			return ImmutableMap.<Pose, EntitySize>builder().put(Pose.STANDING, STANDING_SIZE_FUSEN).put(Pose.CROUCHING, CROUCHING_SIZE_FUSEN).build();
		}
	};
	private static final MorphInfo GIGANT_FUSEN = new MorphInfo() {
		public String getDisplayName() {
			return "Gomu Gomu no Gigant Fusen";
		}

		public ResourceLocation getTexture(AbstractClientPlayerEntity entity) {
			return entity.getLocationSkin();
		}

		public MorphModel getModel() {
			return new GomuGigantFusenModel();
		}

		public AkumaNoMiItem getDevilFruit() {
			return TrueGomuGomuNoMi.HITO_HITO_NO_MI_NIKA;
		}

		public String getForm() {
			return "gigant_fusen";
		}

		public double getEyeHeight() {
			return 4.8;
		}

		public float getShadowSize() {
			return 2f;
		}

		public Map<Pose, EntitySize> getSizes() {
			return ImmutableMap.<Pose, EntitySize>builder().put(Pose.STANDING, STANDING_SIZE_GIGANT_FUSEN).put(Pose.CROUCHING, CROUCHING_SIZE_GIGANT_FUSEN).build();
		}
	};
	private boolean needsUpdate = false;

	public GomuMorphsAbility(AbilityCore core) {
		super(core);
		this.duringPassiveEvent = this::update;
	}

	private void update(PlayerEntity player) {
		if (this.needsUpdate) {
			IDevilFruit props = DevilFruitCapability.get(player);
			IAbilityData abilityProps = AbilityDataCapability.get(player);
			if (props.getZoanPoint().isEmpty()) {
				props.setZoanPoint("");
			}
			if (this.isTransformationActive(player)) {
				props.setZoanPoint(this.getTransformation(player).getForm());
			} else {
				props.setZoanPoint("");
			}
			HitoHitoNoMiNikaMod.LOGGER.info(props.getZoanPoint());
			WyNetwork.sendToAll(new SSyncDevilFruitPacket(player.getEntityId(), props));
			WyNetwork.sendToAll(new SSyncAbilityDataPacket(player.getEntityId(), abilityProps));
			WyNetwork.sendToAll(new CSyncZoanPacket(player.getEntityId()));
			MinecraftForge.EVENT_BUS.post(new Size(player, player.getPose(), player.getSize(player.getPose()), player.getEyeHeight()));
			WyNetwork.sendTo(new SRecalculateEyeHeightPacket(player.getEntityId()), player);
			player.recalculateSize();
			this.needsUpdate = false;
		}
	}

	public void updateModes() {
		this.needsUpdate = true;
	}

	public boolean isTransformationActive(LivingEntity target) {
		IAbilityData props = AbilityDataCapability.get(target);
		return ((TrueGomuHelper.hasGearFifthActive(props) && TrueGomuHelper.hasGearThirdActive(props)) || TrueGomuHelper.hasGearFourthActive(props) || TrueGomuHelper.hasAbilityActive(props, GomuFusenAbility.INSTANCE)
				|| (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasAbilityActive(props, TrueGomuRocket.INSTANCE))) && !this.isPaused();
	}

	public MorphInfo getTransformation(LivingEntity target) {
		IAbilityData props = AbilityDataCapability.get(target);
		if (TrueGomuHelper.hasGearFourthActive(props)) {
			TrueGearFourthAbility g4 = props.getEquippedAbility(TrueGearFourthAbility.INSTANCE);
			if (g4.isSnakeman()) {
				return SNAKEMAN;
			} else if (g4.isBoundman()) {
				return BOUNDMAN;
			}
		} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearFifthActive(props)) {
			return GIGANT;
		} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasAbilityActive(props, GomuFusenAbility.INSTANCE)) {
			return GIGANT_FUSEN;
		} else if (TrueGomuHelper.hasAbilityActive(props, GomuFusenAbility.INSTANCE) || (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasAbilityActive(props, TrueGomuRocket.INSTANCE))) {
			return FUSEN;
		}
		return null;
	}

	static {
		INSTANCE = (new AbilityCore.Builder("Gomu Transformations", AbilityCategory.DEVIL_FRUITS, GomuMorphsAbility::new)).setHidden().build();
	}
}

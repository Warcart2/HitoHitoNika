package net.mcreator.mineminenomitest;

import xyz.pixelatedw.mineminenomi.wypi.WyHelper;
import xyz.pixelatedw.mineminenomi.particles.effects.gomu.GearSecondParticleEffect;
import xyz.pixelatedw.mineminenomi.init.ModSounds;
import xyz.pixelatedw.mineminenomi.init.ModParticleEffects;
import xyz.pixelatedw.mineminenomi.entities.projectiles.gomu.GomuGomuNoPistolProjectile;
import xyz.pixelatedw.mineminenomi.entities.projectiles.gomu.GomuGomuNoKongProjectile;
import xyz.pixelatedw.mineminenomi.entities.projectiles.gomu.GomuGomuNoJetPistolProjectile;
import xyz.pixelatedw.mineminenomi.entities.projectiles.gomu.GomuGomuNoElephantGunProjectile;
import xyz.pixelatedw.mineminenomi.entities.projectiles.AbilityProjectileEntity;
import xyz.pixelatedw.mineminenomi.entities.LightningDischargeEntity;
import xyz.pixelatedw.mineminenomi.data.entity.haki.HakiDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.api.helpers.HakiHelper;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceType;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceHakiNature;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbilityMode;
import xyz.pixelatedw.mineminenomi.api.abilities.ExplosionAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.ChargeableAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCategory;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.abilities.haki.HaoshokuHakiInfusionAbility;
import xyz.pixelatedw.mineminenomi.abilities.haki.BusoshokuHakiHardeningAbility;
import xyz.pixelatedw.mineminenomi.abilities.haki.BusoshokuHakiEmissionAbility;

import net.minecraft.util.SoundCategory;
import net.minecraft.entity.player.PlayerEntity;

import java.awt.Color;

public class StrongGomuPistol extends ChargeableAbility implements IAbilityMode {
	public static final AbilityCore<StrongGomuPistol> INSTANCE;
	private GearSet gearSet = new GearSet();

	public StrongGomuPistol(AbilityCore core) {
		super(core);
		this.updateModes();
		this.duringChargingEvent = this::duringContinuityEvent;
		this.onEndChargingEvent = this::beforeContinuityStopEvent;
	}

	private void duringContinuityEvent(PlayerEntity player, int i) {
		IAbilityData props = AbilityDataCapability.get(player);
		if (TrueGomuHelper.hasGearFourthBoundmanActive(props)) {
			HakiDataCapability.get(player).alterHakiOveruse(15);
		}
	}

	private boolean beforeContinuityStopEvent(PlayerEntity player) {
		IAbilityData props = AbilityDataCapability.get(player);
		AbilityProjectileEntity projectile;
		float speed = 2.5F;
		if (TrueGomuHelper.hasAbilityActive(props, GearSixthAbility.INSTANCE)) {
			projectile = new KingBajrangGunProjectile(player.world, player, this);
			speed = 1f;
		} else if (TrueGomuHelper.hasGearFifthActive(props)) {
			projectile = new BajrangGunProjectile(player.world, player, this);
			speed = 3f;
		} else if (TrueGomuHelper.hasGearFourthBoundmanActive(props)) {
			projectile = new KingKongGunProjectile(player.world, player, this);
			speed = 3;
		} else if (TrueGomuHelper.hasGearFourthSnakemanActive(props)) {
			projectile = new GomuGomuNoKongProjectile(player.world, player, this);
			projectile.setDamage(140f);
			speed = 30f;
		} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearSecondActive(props) && TrueGomuHelper.hasHakiEmissionActive(props)) {
			projectile = new GomuGomuNoElephantGunProjectile(player.world, player, this);
			projectile.onTickEvent = () -> {
				for (int i = 0; i < 10; i++) {
					WyHelper.spawnParticleEffect(ModParticleEffects.DAI_ENKAI_1.get(), projectile, projectile.getPosX(), projectile.getPosY(), projectile.getPosZ());
				}
			};
			projectile.setMaxLife(9);
			projectile.setDamage(56f);
		} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearSecondActive(props)) {
			projectile = new GomuGomuNoElephantGunProjectile(player.world, player, this);
			projectile.onTickEvent = () -> {
				if (projectile.ticksExisted % 2 == 0) {
					new GearSecondParticleEffect().spawn(projectile.world, projectile.getPosX(), projectile.getPosY(), projectile.getPosZ(), 0.0D, 0.0D, 0.0D);
				}
			};
			projectile.setMaxLife(9);
			projectile.setDamage(35f);
		} else if (TrueGomuHelper.hasGearThirdActive(props) && HakiHelper.hasHardeningActive(player)) {
			projectile = new GomuGomuNoElephantGunProjectile(player.world, player, this);
			projectile.setCollisionSize(2.5D);
			projectile.setDamage(projectile.getDamage() * 2f);
			projectile.onEntityImpactEvent = (target) -> {
				LightningDischargeEntity lightning = new LightningDischargeEntity(projectile, projectile.getPosX(), projectile.getPosY(), projectile.getPosZ(), projectile.rotationYaw, projectile.rotationYaw);
				lightning.setAliveTicks(20);
				lightning.setUpdateRate(4);
				lightning.setDetails(16);
				lightning.setColor(new Color(255, 255, 40));
				lightning.setOutlineColor(new Color(255, 255, 40, 50));
				lightning.setRenderTransparent();
				lightning.setLightningLength(3);
				lightning.setDensity(15);
				lightning.setSize(3f);
				projectile.world.addEntity(lightning);
			};
			speed = 2F;
		} else if (TrueGomuHelper.hasGearThirdActive(props)) {
			projectile = new GomuGomuNoElephantGunProjectile(player.world, player, this);
			projectile.setCollisionSize(2.5D);
			projectile.setDamage(projectile.getDamage() * 1.5f);
			speed = 2F;
		} else if (TrueGomuHelper.hasGearSecondActive(props) && HakiHelper.hasHardeningActive(player)) {
			projectile = new GomuGomuNoJetPistolProjectile(player.world, player, this);
			speed = 3.5F;
			projectile.onBlockImpactEvent = (pos) -> {
				ExplosionAbility explosion = AbilityHelper.newExplosion(player, player.world, projectile.getPosX(), projectile.getPosY(), projectile.getPosZ(), 1F);
				explosion.setStaticDamage(3.0F);
				explosion.setExplosionSound(false);
				explosion.setDamageOwner(false);
				explosion.setDestroyBlocks(true);
				explosion.setFireAfterExplosion(true);
				explosion.setDamageEntities(true);
				explosion.doExplosion();
			};
			projectile.onTickEvent = () -> {
				for (int i = 0; i < 10; i++) {
					WyHelper.spawnParticleEffect(ModParticleEffects.DAI_ENKAI_1.get(), projectile, projectile.getPosX(), projectile.getPosY(), projectile.getPosZ());
				}
			};
			projectile.setDamage(projectile.getDamage() * 1.5f);
		} else if (TrueGomuHelper.hasGearSecondActive(props)) {
			projectile = new GomuGomuNoJetPistolProjectile(player.world, player, this);
			speed = 3.5F;
			projectile.onBlockImpactEvent = (pos) -> {
				ExplosionAbility explosion = AbilityHelper.newExplosion(player, player.world, projectile.getPosX(), projectile.getPosY(), projectile.getPosZ(), .5F);
				explosion.setStaticDamage(1.0F);
				explosion.setExplosionSound(false);
				explosion.setDamageOwner(true);
				explosion.setDestroyBlocks(true);
				explosion.setFireAfterExplosion(false);
				explosion.setDamageEntities(false);
				explosion.doExplosion();
			};
			projectile.setDamage(projectile.getDamage() * 1.5f);
		} else {
			projectile = new GomuGomuNoPistolProjectile(player.world, player, this);
			projectile.setDamage(projectile.getDamage() * 1.5f);
		}
		player.world.addEntity(projectile);
		projectile.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, speed, 1.0F);
		player.world.playSound((PlayerEntity) null, player.getPosition(), ModSounds.GOMU_SFX.get(), SoundCategory.PLAYERS, 0.5F, 1.0F);
		return true;
	}

	public AbilityCore[] getParents() {
		return new AbilityCore[]{TrueGearSecondAbility.INSTANCE, TrueGearThirdAbility.INSTANCE, TrueGearFourthAbility.INSTANCE, FifthGearAbility.INSTANCE, GearSixthAbility.INSTANCE, BusoshokuHakiHardeningAbility.INSTANCE,
				BusoshokuHakiEmissionAbility.INSTANCE, HaoshokuHakiInfusionAbility.INSTANCE};
	}

	public void enableMode(Ability parent) {
		if (!this.gearSet.contains(parent)) {
			this.gearSet.add(parent);
		}
		this.updateModes();
	}

	public void disableMode(Ability parent) {
		if (this.gearSet.contains(parent)) {
			this.gearSet.remove(parent);
		}
		this.updateModes();
	}

	protected void updateModes() {
		if (this.gearSet.containsAbility(GearSixthAbility.INSTANCE)) {
			this.setMaxChargeTime(25D);
			this.setMaxCooldown(120D);
			this.setDisplayName("Gomu Gomu no King Bajrang Gun");
			this.setCustomIcon("King Bajrang Gun");
		} else if (this.gearSet.containsAbility(FifthGearAbility.INSTANCE)) {
			this.setMaxChargeTime(15D);
			this.setMaxCooldown(40D);
			this.setDisplayName("Gomu Gomu no Bajrang Gun");
			this.setCustomIcon("King Kong Gun");
		} else if (this.gearSet.containsAbility(TrueGearSecondAbility.INSTANCE) && this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE) && this.gearSet.containsAbility(BusoshokuHakiEmissionAbility.INSTANCE)) {
			this.setMaxCooldown(2D);
			this.setMaxChargeTime(0.0D);
			this.setDisplayName("Gomu Gomu no Red Roc");
			this.setCustomIcon("Fire Pistol");
		} else if (this.gearSet.containsAbility(TrueGearSecondAbility.INSTANCE) && this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE)) {
			this.setMaxCooldown(2D);
			this.setMaxChargeTime(0.0D);
			this.setDisplayName("Gomu Gomu no Strong Jet Gigant Pistol");
			this.setCustomIcon("Gomu Gomu no Pistol");
		} else if (this.gearSet.containsAbility(TrueGearSecondAbility.INSTANCE) && this.gearSet.containsAbility(BusoshokuHakiHardeningAbility.INSTANCE)) {
			this.setMaxCooldown(1.5D);
			this.setMaxChargeTime(0.0D);
			this.setDisplayName("Gomu Gomu no Red Hawk");
			this.setCustomIcon("Fire Pistol");
		} else if (this.gearSet.containsAbility(TrueGearSecondAbility.INSTANCE)) {
			this.setMaxCooldown(1.5D);
			this.setMaxChargeTime(0.0D);
			this.setDisplayName("Gomu Gomu no Strong Jet Pistol");
			this.setCustomIcon("Gomu Gomu no Pistol");
		} else if (this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE) && this.gearSet.containsAbility(BusoshokuHakiHardeningAbility.INSTANCE)) {
			this.setMaxCooldown(9D);
			this.setMaxChargeTime(0.0D);
			this.setDisplayName("Gomu Gomu no Thor Elephant Gun");
			this.setCustomIcon("Haki Pistol");
		} else if (this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE)) {
			this.setMaxCooldown(9D);
			this.setMaxChargeTime(0.0D);
			this.setDisplayName("Gomu Gomu no Strong Gigant Pistol");
			this.setCustomIcon("Gomu Gomu no Pistol");
		} else if (this.gearSet.containsAbility(TrueGearFourthAbility.INSTANCE)) {
			TrueGearFourthAbility g4 = (TrueGearFourthAbility) this.gearSet.getAbility(TrueGearFourthAbility.INSTANCE).get();
			if (g4.isSnakeman()) {
				this.setMaxChargeTime(3D);
				this.setMaxCooldown(10D);
				this.setDisplayName("Gomu Gomu no King Cobra");
				this.setCustomIcon("King Cobra");
			} else if (g4.isBoundman() && gearSet.containsAbility(HaoshokuHakiInfusionAbility.INSTANCE)) {
				this.setMaxChargeTime(5D);
				this.setMaxCooldown(15D);
				this.setDisplayName("Gomu Gomu no Over Kong Gun");
				this.setCustomIcon("Over Kong Gun");
			} else if (g4.isBoundman()) {
				this.setMaxChargeTime(10D);
				this.setMaxCooldown(30D);
				this.setDisplayName("Gomu Gomu no King Kong Gun");
				this.setCustomIcon("King Kong Gun");
			}
		} else if (this.gearSet.containsAbility(BusoshokuHakiHardeningAbility.INSTANCE)) {
			this.setMaxCooldown(2D);
			this.setMaxChargeTime(0.0D);
			this.setDisplayName("Gomu Gomu no Strong Hawk Pistol");
			this.setCustomIcon("Haki Pistol");
		} else {
			this.setMaxCooldown(2D);
			this.setMaxChargeTime(0.0D);
			this.setDisplayName("Gomu Gomu no Strong Pistol");
			this.setCustomIcon("Gomu Gomu no Pistol");
		}
	}

	static {
		INSTANCE = (new AbilityCore.Builder("Strong Gomu Gomu no Pistol", AbilityCategory.DEVIL_FRUITS, StrongGomuPistol::new)).addDescriptionLine("Almost same as simple pistol... until You get Haki").setSourceHakiNature(SourceHakiNature.HARDENING)
				.setSourceType(SourceType.FIST).build();
	}
}

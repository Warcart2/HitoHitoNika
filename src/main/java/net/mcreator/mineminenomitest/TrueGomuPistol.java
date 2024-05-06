package net.mcreator.mineminenomitest;

import xyz.pixelatedw.mineminenomi.particles.effects.gomu.GearSecondParticleEffect;
import xyz.pixelatedw.mineminenomi.init.ModSounds;
import xyz.pixelatedw.mineminenomi.entities.projectiles.gomu.GomuGomuNoPistolProjectile;
import xyz.pixelatedw.mineminenomi.entities.projectiles.gomu.GomuGomuNoKongProjectile;
import xyz.pixelatedw.mineminenomi.entities.projectiles.gomu.GomuGomuNoJetPistolProjectile;
import xyz.pixelatedw.mineminenomi.entities.projectiles.gomu.GomuGomuNoElephantGunProjectile;
import xyz.pixelatedw.mineminenomi.entities.projectiles.AbilityProjectileEntity;
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

import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.player.PlayerEntity;

public class TrueGomuPistol extends ChargeableAbility implements IAbilityMode {
	public static final AbilityCore<TrueGomuPistol> INSTANCE;
	private GearSet gearSet = new GearSet();

	public TrueGomuPistol(AbilityCore core) {
		super(core);
		this.setCustomIcon("Gomu Gomu no Pistol");
		this.setDisplayName("Gomu Gomu no Pistol");
		this.setMaxCooldown(1.5D);
		this.onEndChargingEvent = this::onUseEvent;
	}

	private boolean onUseEvent(PlayerEntity player) {
		IAbilityData props = AbilityDataCapability.get(player);
		AbilityProjectileEntity projectile;
		float speed = 2.0F;
		if (TrueGomuHelper.hasAbilityActive(props, GearSixthAbility.INSTANCE)) {
			projectile = new BajrangGunProjectile(player.world, player, this);
			speed = 4f;
		} else if (TrueGomuHelper.hasGearFourthBoundmanActive(props) && TrueGomuHelper.hasGearFifthActive(props)) {
			projectile = new GomuGomuNoKongProjectile(player.world, player, this);
			projectile.setDamage(300);
			speed = 3f;
			projectile.onBlockImpactEvent = (pos) -> {
				ExplosionAbility explosion = AbilityHelper.newExplosion(player, player.world, projectile.getPosX(), projectile.getPosY(), projectile.getPosZ(), 3.0F);
				explosion.setStaticDamage(13F);
				explosion.setExplosionSound(false);
				explosion.setDamageOwner(true);
				explosion.setDestroyBlocks(true);
				explosion.setFireAfterExplosion(false);
				explosion.setDamageEntities(false);
				explosion.doExplosion();
			};
		} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearFifthActive(props)) {
			projectile = new GomuGomuNoElephantGunProjectile(player.world, player, this);
			projectile.setDamage(150);
			speed = 2f;
			projectile.setMaxLife(9);
			projectile.onBlockImpactEvent = (pos) -> {
				ExplosionAbility explosion = AbilityHelper.newExplosion(player, player.world, projectile.getPosX(), projectile.getPosY(), projectile.getPosZ(), 3.0F);
				explosion.setStaticDamage(13F);
				explosion.setExplosionSound(false);
				explosion.setDamageOwner(true);
				explosion.setDestroyBlocks(true);
				explosion.setFireAfterExplosion(false);
				explosion.setDamageEntities(false);
				explosion.doExplosion();
			};
		} else if (TrueGomuHelper.hasGearFifthActive(props)) {
			projectile = new DawnPistolProjectile(player.world, player, this);
			speed = 3f;
		} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearSecondActive(props) && HakiHelper.hasInfusionActive(player) && TrueGomuHelper.hasHakiEmissionActive(props)) {
			projectile = new GomuGomuNoElephantGunProjectile(player.world, player, this);
			((AbilityProjectileEntity) projectile).setCollisionSize(2.5D);
			projectile.setDamage(56f);
			projectile.onTickEvent = () -> {
				if (projectile.ticksExisted % 2 == 0) {
					new GearSecondParticleEffect().spawn(projectile.world, projectile.getPosX(), projectile.getPosY(), projectile.getPosZ(), 0.0D, 0.0D, 0.0D);
				}
			};
			speed = 2.5F;
		} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearSecondActive(props)) {
			projectile = new GomuGomuNoElephantGunProjectile(player.world, player, this);
			projectile.onTickEvent = () -> {
				if (projectile.ticksExisted % 2 == 0) {
					new GearSecondParticleEffect().spawn(projectile.world, projectile.getPosX(), projectile.getPosY(), projectile.getPosZ(), 0.0D, 0.0D, 0.0D);
				}
			};
			projectile.setMaxLife(9);
			projectile.setDamage(28f);
		} else if (TrueGomuHelper.hasGearFourthBoundmanActive(props)) {
			projectile = new GomuGomuNoKongProjectile(player.world, player, this);
			((AbilityProjectileEntity) projectile).setCollisionSize(2.5D);
			speed = 1.8F;
		} else if (TrueGomuHelper.hasGearFourthSnakemanActive(props)) {
			projectile = new GomuGomuNoKongProjectile(player.world, player, this);//JetCulverinProjectile
			projectile.onBlockImpactEvent = (pos) -> {
			};
			projectile.setMaxLife(5);
			speed = 7F;
		} else if (TrueGomuHelper.hasGearThirdActive(props) && HakiHelper.hasInfusionActive(player) && TrueGomuHelper.hasHakiEmissionActive(props)) {
			projectile = new GomuGomuNoElephantGunProjectile(player.world, player, this);
			((AbilityProjectileEntity) projectile).setCollisionSize(2.5D);
			projectile.setDamage(projectile.getDamage() * 2);
			speed = 2F;
		} else if (TrueGomuHelper.hasGearThirdActive(props)) {
			projectile = new GomuGomuNoElephantGunProjectile(player.world, player, this);
			((AbilityProjectileEntity) projectile).setCollisionSize(2.5D);
			speed = 1.8F;
		} else if (TrueGomuHelper.hasGearSecondActive(props)) {
			projectile = new GomuGomuNoJetPistolProjectile(player.world, player, this);
			speed = 2.5F;
			projectile.onBlockImpactEvent = (pos) -> {
				ExplosionAbility explosion = AbilityHelper.newExplosion(player, player.world, projectile.getPosX(), projectile.getPosY(), projectile.getPosZ(), .3F);
				explosion.setStaticDamage(1.0F);
				explosion.setExplosionSound(false);
				explosion.setDamageOwner(true);
				explosion.setDestroyBlocks(true);
				explosion.setFireAfterExplosion(false);
				explosion.setDamageEntities(false);
				explosion.doExplosion();
			};
		} else {
			projectile = new GomuGomuNoPistolProjectile(player.world, player, this);
		}
		player.world.addEntity(projectile);
		projectile.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, speed, 1.0F);
		player.world.playSound((PlayerEntity) null, player.getPosition(), (SoundEvent) ModSounds.GOMU_SFX.get(), SoundCategory.PLAYERS, 0.5F, 1.0F);
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
			this.setMaxCooldown(7.0D);
			this.setMaxChargeTime(3.0D);
			this.setDisplayName("Gomu Gomu no Bajrang Gun");
			this.setCustomIcon("King Kong Gun");
		} else if (this.gearSet.containsAbility(TrueGearSecondAbility.INSTANCE) && this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE) && this.gearSet.containsAbility(BusoshokuHakiEmissionAbility.INSTANCE)
				&& this.gearSet.containsAbility(HaoshokuHakiInfusionAbility.INSTANCE)) {
			this.setMaxCooldown(6D);
			this.setMaxChargeTime(0.0D);
			this.setDisplayName("Gomu Gomu no Roc Gun");
			this.setCustomIcon("Gomu Gomu no Pistol");
		} else if (this.gearSet.containsAbility(TrueGearSecondAbility.INSTANCE) && this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE) && this.gearSet.containsAbility(BusoshokuHakiHardeningAbility.INSTANCE)) {
			this.setMaxCooldown(1.5D);
			this.setMaxChargeTime(0.0D);
			this.setDisplayName("Gomu Gomu no Jet Elephant Gun");
			this.setCustomIcon("Gomu Gomu no Pistol");
		} else if (this.gearSet.containsAbility(TrueGearSecondAbility.INSTANCE) && this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE)) {
			this.setMaxCooldown(1.5D);
			this.setMaxChargeTime(0.0D);
			this.setDisplayName("Gomu Gomu no Jet Gigant Pistol");
			this.setCustomIcon("Gomu Gomu no Pistol");
		} else if (this.gearSet.containsAbility(FifthGearAbility.INSTANCE) && this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE)) {
			this.setMaxCooldown(1.5D);
			this.setMaxChargeTime(0.0D);
			this.setDisplayName("Gomu Gomu no Gigant Dawn Pistol");
			this.setCustomIcon("Gomu Gomu no Pistol");
		} else if (this.gearSet.containsAbility(FifthGearAbility.INSTANCE) && this.gearSet.containsAbility(TrueGearFourthAbility.INSTANCE)) {
			this.setMaxCooldown(4D);
			this.setMaxChargeTime(1D);
			this.setDisplayName("Gomu Gomu no Star Cannon");
			this.setCustomIcon("Star Cannon");
		} else if (this.gearSet.containsAbility(TrueGearSecondAbility.INSTANCE)) {
			this.setMaxCooldown(1.0D);
			this.setMaxChargeTime(0.0D);
			this.setDisplayName("Gomu Gomu no Jet Pistol");
			this.setCustomIcon("Gomu Gomu no Pistol");
		} else if (this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE) && this.gearSet.containsAbility(BusoshokuHakiEmissionAbility.INSTANCE) && this.gearSet.containsAbility(HaoshokuHakiInfusionAbility.INSTANCE)) {
			this.setMaxCooldown(6D);
			this.setMaxChargeTime(0.0D);
			this.setDisplayName("Gomu Gomu no Roc Gun");
			this.setCustomIcon("Gomu Gomu no Pistol");
		} else if (this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE) && this.gearSet.containsAbility(BusoshokuHakiHardeningAbility.INSTANCE)) {
			this.setMaxCooldown(6D);
			this.setMaxChargeTime(0.0D);
			this.setDisplayName("Gomu Gomu no Elephant Gun");
			this.setCustomIcon("Gomu Gomu no Pistol");
		} else if (this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE)) {
			this.setMaxCooldown(6D);
			this.setMaxChargeTime(0.0D);
			this.setDisplayName("Gomu Gomu no Gigant Pistol");
			this.setCustomIcon("Gomu Gomu no Pistol");
		} else if (this.gearSet.containsAbility(TrueGearFourthAbility.INSTANCE)) {
			TrueGearFourthAbility g4 = (TrueGearFourthAbility) this.gearSet.getAbility(TrueGearFourthAbility.INSTANCE).get();
			if (g4.getMode() == TrueGearFourthAbility.Mode.SNAKEMAN) {
				this.setMaxCooldown(2.0D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName("Gomu Gomu no Jet Culverin");
				this.setCustomIcon("Jet Culverine");
			} else {
				this.setMaxCooldown(4.0D);
				this.setMaxChargeTime(0.5D);
				this.setDisplayName("Gomu Gomu no Kong Gun");
				this.setCustomIcon("Haki Pistol");
			}
		} else if (this.gearSet.containsAbility(FifthGearAbility.INSTANCE)) {
			this.setMaxCooldown(1.0D);
			this.setMaxChargeTime(0.0D);
			this.setDisplayName("Gomu Gomu no Dawn Pistol");
			this.setCustomIcon("Gomu Gomu no Pistol");
		} else if (this.gearSet.containsAbility(BusoshokuHakiHardeningAbility.INSTANCE)) {
			this.setMaxCooldown(1.5D);
			this.setMaxChargeTime(0.0D);
			this.setDisplayName("Gomu Gomu no Hawk Pistol");
			this.setCustomIcon("Haki Pistol");
		} else {
			this.setMaxCooldown(1.5D);
			this.setMaxChargeTime(0.0D);
			this.setDisplayName("Gomu Gomu no Pistol");
			this.setCustomIcon("Gomu Gomu no Pistol");
		}
		if (this.getIcon(null).equals(new ResourceLocation("mineminenomi:textures/abilities/gomu_gomu_no_pistol.png")) && this.gearSet.containsAbility(BusoshokuHakiHardeningAbility.INSTANCE)) {
			this.setCustomIcon("Haki Pistol");
		}
	}

	static {
		INSTANCE = (new AbilityCore.Builder("True Gomu Gomu no Pistol", AbilityCategory.DEVIL_FRUITS, TrueGomuPistol::new)).addDescriptionLine("The user stretches their arm to hit the opponent").setSourceHakiNature(SourceHakiNature.HARDENING)
				.setSourceType(SourceType.FIST).build();
	}
}

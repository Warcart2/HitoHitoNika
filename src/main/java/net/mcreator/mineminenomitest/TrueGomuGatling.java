package net.mcreator.mineminenomitest;

import xyz.pixelatedw.mineminenomi.wypi.WyHelper;
import xyz.pixelatedw.mineminenomi.init.ModSounds;
import xyz.pixelatedw.mineminenomi.entities.projectiles.gomu.GomuGomuNoPistolProjectile;
import xyz.pixelatedw.mineminenomi.entities.projectiles.gomu.GomuGomuNoKongProjectile;
import xyz.pixelatedw.mineminenomi.entities.projectiles.gomu.GomuGomuNoJetPistolProjectile;
import xyz.pixelatedw.mineminenomi.entities.projectiles.gomu.GomuGomuNoElephantGunProjectile;
import xyz.pixelatedw.mineminenomi.entities.projectiles.AbilityProjectileEntity;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.EntityStatsCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceType;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceHakiNature;
import xyz.pixelatedw.mineminenomi.api.animations.IAnimation;
import xyz.pixelatedw.mineminenomi.api.abilities.IExtraUpdateData;
import xyz.pixelatedw.mineminenomi.api.abilities.IAnimatedAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbilityMode;
import xyz.pixelatedw.mineminenomi.api.abilities.ExplosionAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.ContinuousAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCategory;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.animations.gomu.GomuGatlingAnimation;
import xyz.pixelatedw.mineminenomi.abilities.haki.HaoshokuHakiInfusionAbility;
import xyz.pixelatedw.mineminenomi.abilities.haki.BusoshokuHakiHardeningAbility;
import xyz.pixelatedw.mineminenomi.abilities.haki.BusoshokuHakiEmissionAbility;

import net.minecraft.util.SoundCategory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.LivingEntity;
import xyz.pixelatedw.mineminenomi.api.helpers.HakiHelper;

public class TrueGomuGatling extends ContinuousAbility implements IAbilityMode, IAnimatedAbility, IExtraUpdateData {
	public static final AbilityCore<TrueGomuGatling> INSTANCE;
	private GearSet gearSet = new GearSet();
	private boolean hasDataSet = false;
	private double leap = 3;

	public TrueGomuGatling(AbilityCore core) {
		super(core);
		this.setDisplayName("Gomu Gomu no Gatling");
		this.setCustomIcon("Gomu Gomu no Gatling");
		this.duringContinuityEvent = this::duringContinuityEvent;
		this.onStartContinuityEvent = this::onStartContinuityEvent;
		this.afterContinuityStopEvent = this::onContinuityStopEvent;
		this.duringCooldownEvent = this::duringCooldownEvent;
	}

	private boolean onStartContinuityEvent(PlayerEntity player) {
		double time = EntityStatsCapability.get(player).getDoriki() / 100;
		IAbilityData props = AbilityDataCapability.get(player);
		double dif;
		if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearFifthActive(props)) {
			this.leap = 2;
			dif = 5;
		} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearSecondActive(props) && HakiHelper.hasInfusionActive(player) && TrueGomuHelper.hasHakiEmissionActive(props)) {
			this.leap = 1;
			dif = 50;
		} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearSecondActive(props)) {
			this.leap = 3;
			dif = 40;
		} else if (TrueGomuHelper.hasGearFifthActive(props)) {
			this.leap = 1;
			dif = 2;
		} else if (TrueGomuHelper.hasGearFourthBoundmanActive(props)) {
			this.leap = 5;
			dif = 20;
		} else if (TrueGomuHelper.hasGearFourthSnakemanActive(props)) {
			this.leap = 0.5;
			dif = 20;
		} else if (TrueGomuHelper.hasGearThirdActive(props)) {
			this.leap = 5;
			dif = 30;
		} else if (TrueGomuHelper.hasGearSecondActive(props)) {
			this.leap = 2;
			dif = 10;
		} else {
			dif = 5;
			this.leap = 3;
		}
		time /= dif;
		time = 3 * (1 + Math.sqrt(time));
		if (time < 500) {
			this.setThreshold(time);
		} else {
			this.setThreshold(0);
		}
		return true;
	}

	private void duringContinuityEvent(PlayerEntity player, int timer) {
		if (leap >= 1) {
			if (timer % (int) leap == 0)
				this.onUseEvent(player);
		} else if (leap > 0) {
			for (int i = 0; i < 1 / leap; i++) {
				this.onUseEvent(player);
			}
		}
	}

	private boolean onUseEvent(PlayerEntity player) {
		IAbilityData props = AbilityDataCapability.get(player);
		AbilityProjectileEntity projectile;
		float speed = 3.0F;
		float projDmageReduction = 0.8F;
		int projectileSpace = 2;
		if (TrueGomuHelper.hasAbilityActive(props, GearSixthAbility.INSTANCE)) {
			projectile = new BajrangGunProjectile(player.world, player, this, 15f);
			if (!this.hasDataSet) {
				projectileSpace = 10;
				speed = 5f;
				this.hasDataSet = true;
			}
		} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearFifthActive(props)) {
			projectile = new GomuGomuNoElephantGunProjectile(player.world, player, this);
			projectile.setDamage(150);
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
			if (!this.hasDataSet) {
				projectileSpace = 9;
				this.hasDataSet = true;
			}
		} else if (TrueGomuHelper.hasGearFifthActive(props)) {
			projectile = new DawnPistolProjectile(player.world, player, this);
			if (!this.hasDataSet) {
				speed = 2;
				this.hasDataSet = true;
			}
		} else if (TrueGomuHelper.hasGearFourthBoundmanActive(props)) {
			projectile = new GomuGomuNoKongProjectile(player.world, player, this);
			((AbilityProjectileEntity) projectile).setCollisionSize(2.5D);
			if (!this.hasDataSet) {
				speed = 2.2F;
				projectileSpace = 6;
				this.hasDataSet = true;
			}
			projDmageReduction = 0.6F;
		} else if (TrueGomuHelper.hasGearFourthSnakemanActive(props)) {
			projectile = new GomuGomuNoKongProjectile(player.world, player, this);
			projectile.onBlockImpactEvent = (pos) -> {
			};
			projectile.setMaxLife(5);
			((AbilityProjectileEntity) projectile).setCollisionSize(2.5D);
			if (!this.hasDataSet) {
				speed = 7F;
				projectileSpace = 6;
				this.hasDataSet = true;
			}
			projDmageReduction = 0.4F;
		} else if (TrueGomuHelper.hasGearThirdActive(props)) {
			projectile = new GomuGomuNoElephantGunProjectile(player.world, player, this);
			((AbilityProjectileEntity) projectile).setCollisionSize(2.5D);
			if (!this.hasDataSet) {
				speed = 2.4F;
				projectileSpace = 9;
				this.hasDataSet = true;
			}
			projDmageReduction = 0.6F;
		} else if (TrueGomuHelper.hasGearSecondActive(props)) {
			projectile = new GomuGomuNoJetPistolProjectile(player.world, player, this);
			projectile.onBlockImpactEvent = (pos) -> {
				ExplosionAbility explosion = AbilityHelper.newExplosion(player, player.world, projectile.getPosX(), projectile.getPosY(), projectile.getPosZ(), .3F);
				explosion.setStaticDamage(1F);
				explosion.setExplosionSound(false);
				explosion.setDamageOwner(true);
				explosion.setDestroyBlocks(true);
				explosion.setFireAfterExplosion(false);
				explosion.setDamageEntities(false);
				explosion.doExplosion();
			};
			if (!this.hasDataSet) {
				speed = 3.6F;
				this.hasDataSet = true;
			}
		} else {
			projectile = new GomuGomuNoPistolProjectile(player.world, player, this);
			if (!this.hasDataSet) {
				this.hasDataSet = true;
			}
		}
		projectile.setDamage(((AbilityProjectileEntity) projectile).getDamage() * (1.0F - projDmageReduction));
		projectile.setMaxLife((int) ((double) ((AbilityProjectileEntity) projectile).getMaxLife() * 0.75D));
		projectile.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, speed, 3.0F);
		projectile.setPositionAndUpdate(player.getPosX() + WyHelper.randomWithRange(-projectileSpace, projectileSpace) + WyHelper.randomDouble(),
				player.getPosY() + player.getHeight() + WyHelper.randomWithRange(0, projectileSpace) + WyHelper.randomDouble(), player.getPosZ() + WyHelper.randomWithRange(-projectileSpace, projectileSpace) + WyHelper.randomDouble());
		player.world.addEntity(projectile);
		player.world.playSound((PlayerEntity) null, player.getPosition(), ModSounds.GOMU_SFX.get(), SoundCategory.PLAYERS, 0.5F, 0.6F + this.random.nextFloat() / 2.0F);
		return true;
	}

	private void duringCooldownEvent(PlayerEntity player, int cooldown) {
		if (cooldown <= 1) {
			this.hasDataSet = false;
		}
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
		if (this.gearSet.containsAbility(TrueGearSecondAbility.INSTANCE) && this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE) && this.gearSet.containsAbility(BusoshokuHakiEmissionAbility.INSTANCE) && this.gearSet.containsAbility(HaoshokuHakiInfusionAbility.INSTANCE)) {
			this.setDisplayName("Gomu Gomu no Roc Gatling");
			this.setCustomIcon("Haki Gatling");
		} else if (this.gearSet.containsAbility(TrueGearSecondAbility.INSTANCE) && this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE)) {
			this.setDisplayName("Gomu Gomu no Jet Elephant Gatling");
			this.setCustomIcon("Gomu Gomu no Gatling");
		} else if (this.gearSet.containsAbility(TrueGearSecondAbility.INSTANCE) && this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE)) {
			this.setDisplayName("Gomu Gomu no Jet Gigant Gatling");
			this.setCustomIcon("Gomu Gomu no Gatling");
		} else if (this.gearSet.containsAbility(FifthGearAbility.INSTANCE) && this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE)) {
			this.setDisplayName("Gomu Gomu no Gigant Dawn Gatling");
			this.setCustomIcon("Gomu Gomu no Gatling");
		} else if (this.gearSet.containsAbility(TrueGearSecondAbility.INSTANCE)) {
			this.setDisplayName("Gomu Gomu no Jet Gatling");
			this.setCustomIcon("Gomu Gomu no Gatling");
		} else if (this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE) && this.gearSet.containsAbility(BusoshokuHakiHardeningAbility.INSTANCE)) {
			this.setDisplayName("Gomu Gomu no Elephant Gatling");
			this.setCustomIcon("Gomu Gomu no Gatling");
		} else if (this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE)) {
			this.setDisplayName("Gomu Gomu no Gigant Gatling");
			this.setCustomIcon("Gomu Gomu no Gatling");
		} else if (this.gearSet.containsAbility(TrueGearFourthAbility.INSTANCE)) {
			TrueGearFourthAbility g4 = (TrueGearFourthAbility) this.gearSet.getAbility(TrueGearFourthAbility.INSTANCE).get();
			if (g4.getMode() == TrueGearFourthAbility.Mode.SNAKEMAN) {
				if (gearSet.containsAbility(HaoshokuHakiInfusionAbility.INSTANCE)) {
					this.setDisplayName("Gomu Gomu no Hydra");
				} else {
					this.setDisplayName("Gomu Gomu no Black Mamba");
				}
				this.setCustomIcon("Black Mamba");
			} else {
				this.setDisplayName("Gomu Gomu no Kong Gatling");
				this.setCustomIcon("Haki Gatling");
			}
		} else if (this.gearSet.containsAbility(FifthGearAbility.INSTANCE)) {
			this.setDisplayName("Gomu Gomu no Dawn Gatling");
			this.setCustomIcon("Gomu Gomu no Gatling");
		} else if (this.gearSet.containsAbility(GearSixthAbility.INSTANCE)) {
			this.setDisplayName("Gomu Gomu no Bajrang Gatling");
			this.setCustomIcon("King Kong Gatling");
		} else if (this.gearSet.containsAbility(BusoshokuHakiHardeningAbility.INSTANCE)) {
			this.setDisplayName("Gomu Gomu no Hawk Gatling");
			this.setCustomIcon("Haki Gatling");
		} else {
			this.setDisplayName("Gomu Gomu no Gatling");
			this.setCustomIcon("Gomu Gomu no Gatling");
		}
		if (this.getIcon(null).equals(new ResourceLocation("mineminenomi:textures/abilities/gomu_gomu_no_gatling.png")) && this.gearSet.containsAbility(BusoshokuHakiHardeningAbility.INSTANCE)) {
			this.setCustomIcon("Haki Gatling");
		}
	}

	private void onContinuityStopEvent(PlayerEntity player) {
		IAbilityData props = AbilityDataCapability.get(player);
		double dif;
		if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearFifthActive(props)) {
			dif = 5;
		} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearSecondActive(props)) {
			dif = 10;
		} else if (TrueGomuHelper.hasGearFifthActive(props)) {
			dif = 2;
		} else if (TrueGomuHelper.hasGearFourthBoundmanActive(props)) {
			dif = 20;
		} else if (TrueGomuHelper.hasGearFourthSnakemanActive(props)) {
			dif = 20;
		} else if (TrueGomuHelper.hasGearThirdActive(props)) {
			dif = 30;
		} else if (TrueGomuHelper.hasGearSecondActive(props)) {
			dif = 3;
		} else {
			dif = 5;
		}
		this.setMaxCooldown(Math.max((this.continueTime / 200) * dif, 3));
	}

	public IAnimation getAnimation() {
		return GomuGatlingAnimation.INSTANCE;
	}

	public boolean isAnimationActive(LivingEntity entity) {
		return this.isContinuous();
	}

	public void setExtraData(CompoundNBT tag) {
		this.leap = tag.getDouble("leap");
	}

	public CompoundNBT getExtraData() {
		CompoundNBT out = new CompoundNBT();
		out.putDouble("leap", this.leap);
		return out;
	}

	static {
		INSTANCE = (new AbilityCore.Builder("True Gomu Gomu no Gatling", AbilityCategory.DEVIL_FRUITS, TrueGomuGatling::new)).addDescriptionLine("Rapidly punches enemies using rubber fists").setSourceHakiNature(SourceHakiNature.HARDENING)
				.setSourceType(SourceType.FIST).build();
	}
}

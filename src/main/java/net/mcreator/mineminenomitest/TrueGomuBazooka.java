package net.mcreator.mineminenomitest;

import xyz.pixelatedw.mineminenomi.init.ModSounds;
import xyz.pixelatedw.mineminenomi.entities.projectiles.gomu.GomuGomuNoLeoBazookaProjectile;
import xyz.pixelatedw.mineminenomi.entities.projectiles.gomu.GomuGomuNoKongProjectile;
import xyz.pixelatedw.mineminenomi.entities.projectiles.gomu.GomuGomuNoJetBazookaProjectile;
import xyz.pixelatedw.mineminenomi.entities.projectiles.gomu.GomuGomuNoGrizzlyMagnumProjectile;
import xyz.pixelatedw.mineminenomi.entities.projectiles.gomu.GomuGomuNoBazookaProjectile;
import xyz.pixelatedw.mineminenomi.entities.projectiles.AbilityProjectileEntity;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceType;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceHakiNature;
import xyz.pixelatedw.mineminenomi.api.animations.IAnimation;
import xyz.pixelatedw.mineminenomi.api.abilities.IAnimatedAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbilityMode;
import xyz.pixelatedw.mineminenomi.api.abilities.ChargeableAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCategory;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.animations.gomu.GomuBazookaAnimation;
import xyz.pixelatedw.mineminenomi.abilities.haki.BusoshokuHakiHardeningAbility;

import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Direction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.LivingEntity;

public class TrueGomuBazooka extends ChargeableAbility implements IAnimatedAbility, IAbilityMode {
	public static final AbilityCore<TrueGomuBazooka> INSTANCE;
	private GearSet gearSet = new GearSet();

	public TrueGomuBazooka(AbilityCore core) {
		super(core);
		this.setCustomIcon("Gomu Gomu no Bazooka");
		this.setDisplayName("Gomu Gomu no Bazooka");
		this.setMaxCooldown(10.0D);
		this.setMaxChargeTime(2.0D);
		this.onEndChargingEvent = this::onEndChargingEvent;
	}

	private boolean onEndChargingEvent(PlayerEntity player) {
		IAbilityData props = AbilityDataCapability.get(player);
		AbilityProjectileEntity projectile1;
		AbilityProjectileEntity projectile2;
		float speed = 2.0F;
		double spacingMod = 1.0D;
		if (TrueGomuHelper.hasAbilityActive(props, GearSixthAbility.INSTANCE)) {
			projectile1 = new BajrangGunProjectile(player.world, player, this);
			projectile2 = new BajrangGunProjectile(player.world, player, this);
			spacingMod = 25d;
			speed = 4f;
		} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearFifthActive(props)) {
			projectile1 = new GomuGomuNoGrizzlyMagnumProjectile(player.world, player, this);
			projectile1.setDamage(180.0F);
			projectile1.setPassThroughEntities();
			projectile1.setHurtTime(10);
			projectile1.setMaxLife(9);
			projectile2 = new GomuGomuNoGrizzlyMagnumProjectile(player.world, player, this);
			projectile2.setDamage(180.0F);
			projectile2.setPassThroughEntities();
			projectile2.setHurtTime(10);
			projectile2.setMaxLife(9);
		} else if (TrueGomuHelper.hasGearFifthActive(props)) {
			projectile1 = new GomuGomuNoBazookaProjectile(player.world, player, this);
			projectile1.setDamage(120.0F);
			projectile1.setPassThroughEntities();
			projectile1.setHurtTime(10);
			projectile2 = new GomuGomuNoBazookaProjectile(player.world, player, this);
			projectile2.setDamage(120.0F);
			projectile2.setPassThroughEntities();
			projectile2.setHurtTime(10);
			speed = 3.0F;
		} else if (TrueGomuHelper.hasGearFourthBoundmanActive(props)) {
			projectile1 = new GomuGomuNoLeoBazookaProjectile(player.world, player, this);
			((AbilityProjectileEntity) projectile1).setCollisionSize(2.5D);
			projectile2 = new GomuGomuNoLeoBazookaProjectile(player.world, player, this);
			((AbilityProjectileEntity) projectile2).setCollisionSize(2.5D);
			speed = 3.0F;
			spacingMod = 2.5D;
		} else if (TrueGomuHelper.hasGearFourthSnakemanActive(props)) {
			projectile1 = new GomuGomuNoKongProjectile(player.world, player, this);
			((AbilityProjectileEntity) projectile1).setCollisionSize(2.5D);
			projectile1.onBlockImpactEvent = (pos) -> {
			};
			projectile2 = new GomuGomuNoKongProjectile(player.world, player, this);
			((AbilityProjectileEntity) projectile2).setCollisionSize(2.5D);
			projectile2.onBlockImpactEvent = (pos) -> {
			};
			speed = 12F;
			spacingMod = 1.5D;
		} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearSecondActive(props)) {
			projectile1 = new GomuGomuNoGrizzlyMagnumProjectile(player.world, player, this);
			projectile1.setDamage(50.0F);
			projectile1.setCollisionSize(2.5D);
			projectile2 = new GomuGomuNoGrizzlyMagnumProjectile(player.world, player, this);
			projectile2.setDamage(50.0F);
			projectile2.setCollisionSize(2.5D);
			speed = 1.8F;
			spacingMod = 2.5D;
		} else if (TrueGomuHelper.hasGearThirdActive(props)) {
			projectile1 = new GomuGomuNoGrizzlyMagnumProjectile(player.world, player, this);
			((AbilityProjectileEntity) projectile1).setCollisionSize(2.5D);
			projectile2 = new GomuGomuNoGrizzlyMagnumProjectile(player.world, player, this);
			((AbilityProjectileEntity) projectile2).setCollisionSize(2.5D);
			speed = 1.8F;
			spacingMod = 2.5D;
		} else if (TrueGomuHelper.hasGearSecondActive(props)) {
			projectile1 = new GomuGomuNoJetBazookaProjectile(player.world, player, this);
			projectile2 = new GomuGomuNoJetBazookaProjectile(player.world, player, this);
			speed = 3.0F;
		} else {
			projectile1 = new GomuGomuNoBazookaProjectile(player.world, player, this);
			projectile2 = new GomuGomuNoBazookaProjectile(player.world, player, this);
		}
		Vector3d dirVec = Vector3d.ZERO;
		Direction dir = Direction.fromAngle((double) player.rotationYaw);
		dirVec = dirVec.add((double) Math.abs(dir.toVector3f().getX()), (double) Math.abs(dir.toVector3f().getY()), (double) Math.abs(dir.toVector3f().getZ())).add(spacingMod, 1.0D, spacingMod);
		double yPos = player.getPosY() + (double) player.getHeight() - 0.7D;
		projectile1.shoot(player, player.rotationPitch, player.rotationYaw, 0f, speed, 0f);
		projectile2.shoot(player, player.rotationPitch, player.rotationYaw, 0f, speed, 0f);
		projectile1.setPositionAndUpdate(player.getPosX() + dirVec.x, yPos, player.getPosZ() + dirVec.z);
		projectile2.setPositionAndUpdate(player.getPosX() - dirVec.x, yPos, player.getPosZ() - dirVec.z);
		player.world.addEntity(projectile1);
		player.world.addEntity(projectile2);
		player.world.playSound((PlayerEntity) null, player.getPosition(), ModSounds.GOMU_SFX.get(), SoundCategory.PLAYERS, 0.5F, 0.75F);
		return true;
	}

	public IAnimation getAnimation() {
		return GomuBazookaAnimation.INSTANCE;
	}

	public boolean isAnimationActive(LivingEntity entity) {
		return this.isCharging();
	}

	public AbilityCore[] getParents() {
		return new AbilityCore[]{TrueGearSecondAbility.INSTANCE, TrueGearThirdAbility.INSTANCE, TrueGearFourthAbility.INSTANCE, FifthGearAbility.INSTANCE, GearSixthAbility.INSTANCE, BusoshokuHakiHardeningAbility.INSTANCE};
	}

	public void enableMode(Ability parent) {
		if (!this.gearSet.containsAbility(parent.getCore())) {
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
		if (this.gearSet.containsAbility(TrueGearSecondAbility.INSTANCE) && this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE) && this.gearSet.containsAbility(BusoshokuHakiHardeningAbility.INSTANCE)) {
			this.setMaxCooldown(10D);
			this.setDisplayName("Gomu Gomu no Jet Grizzly Magnum");
			this.setCustomIcon("Gomu Gomu no Bazooka");
		} else if (this.gearSet.containsAbility(TrueGearSecondAbility.INSTANCE) && this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE)) {
			this.setMaxCooldown(10D);
			this.setDisplayName("Gomu Gomu no Jet Gigant Bazooka");
			this.setCustomIcon("Gomu Gomu no Bazooka");
		} else if (this.gearSet.containsAbility(FifthGearAbility.INSTANCE) && this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE)) {
			this.setMaxCooldown(10D);
			this.setDisplayName("Gomu Gomu no Gigant Dawn Bazooka");
			this.setCustomIcon("Gomu Gomu no Bazooka");
		} else if (this.gearSet.containsAbility(TrueGearSecondAbility.INSTANCE)) {
			this.setMaxCooldown(7D);
			this.setDisplayName("Gomu Gomu no Jet Bazooka");
			this.setCustomIcon("Gomu Gomu no Bazooka");
		} else if (this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE) && this.gearSet.containsAbility(BusoshokuHakiHardeningAbility.INSTANCE)) {
			this.setMaxCooldown(15D);
			this.setDisplayName("Gomu Gomu no Grizzly Magnum");
			this.setCustomIcon("Gomu Gomu no Bazooka");
		} else if (this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE)) {
			this.setMaxCooldown(15D);
			this.setDisplayName("Gomu Gomu no Gigant Bazooka");
			this.setCustomIcon("Gomu Gomu no Bazooka");
		} else if (this.gearSet.containsAbility(TrueGearFourthAbility.INSTANCE)) {
			TrueGearFourthAbility g4 = (TrueGearFourthAbility) this.gearSet.getAbility(TrueGearFourthAbility.INSTANCE).get();
			if (g4.getMode() == TrueGearFourthAbility.Mode.SNAKEMAN) {
				this.setMaxCooldown(5D);
				this.setDisplayName("Gomu Gomu no Twin Jet Culverin");
				this.setCustomIcon("Twin Jet Culverine");
			} else {
				this.setMaxCooldown(12D);
				this.setDisplayName("Gomu Gomu no Leo Bazooka");
				this.setCustomIcon("Haki Bazooka");
			}
		} else if (this.gearSet.containsAbility(FifthGearAbility.INSTANCE)) {
			this.setMaxCooldown(7D);
			this.setDisplayName("Gomu Gomu no Dawn Bazooka");
			this.setCustomIcon("Gomu Gomu no Bazooka");
		} else if (this.gearSet.containsAbility(GearSixthAbility.INSTANCE)) {
			this.setMaxCooldown(20D);
			this.setDisplayName("Gomu Gomu no Double Bajrang Gun");
			this.setCustomIcon("Double King Kong Gun");
		} else if (this.gearSet.containsAbility(BusoshokuHakiHardeningAbility.INSTANCE)) {
			this.setMaxCooldown(10D);
			this.setDisplayName("Gomu Gomu no Eagle Bazooka");
			this.setCustomIcon("Haki Bazooka");
		} else {
			this.setMaxCooldown(10D);
			this.setDisplayName("Gomu Gomu no Bazooka");
			this.setCustomIcon("Gomu Gomu no Bazooka");
		}
		if (this.getIcon(null).equals(new ResourceLocation("mineminenomi:textures/abilities/gomu_gomu_no_bazooka.png")) && this.gearSet.containsAbility(BusoshokuHakiHardeningAbility.INSTANCE)) {
			this.setCustomIcon("Haki Bazooka");
		}
	}

	static {
		INSTANCE = (new AbilityCore.Builder("True Gomu Gomu no Bazooka", AbilityCategory.DEVIL_FRUITS, TrueGomuBazooka::new)).addDescriptionLine("Hits the enemy with both hands to launch them away").setSourceHakiNature(SourceHakiNature.HARDENING)
				.setSourceType(SourceType.FIST).build();
	}
}

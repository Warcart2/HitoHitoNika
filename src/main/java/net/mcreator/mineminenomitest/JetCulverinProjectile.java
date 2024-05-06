package net.mcreator.mineminenomitest;

import xyz.pixelatedw.mineminenomi.entities.projectiles.gomu.GomuProjectiles;
import xyz.pixelatedw.mineminenomi.entities.projectiles.AbilityProjectileEntity;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;

import net.minecraft.world.World;
import net.minecraft.util.math.MathHelper;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Entity;

public class JetCulverinProjectile extends AbilityProjectileEntity {
	protected static final DataParameter<Integer> NEXT_ID = EntityDataManager.createKey(JetCulverinProjectile.class, DataSerializers.VARINT);
	protected static final DataParameter<Integer> PREV_ID = EntityDataManager.createKey(JetCulverinProjectile.class, DataSerializers.VARINT);
	protected Ability master;

	public JetCulverinProjectile(EntityType type, World world) {
		super(type, world);
	}

	public JetCulverinProjectile(World world, LivingEntity player, Ability ability) {
		super(GomuProjectiles.GOMU_NO_KONG_GUN.get(), world, player, ability);
		master = ability;
		this.setMaxLife(5);
		this.setDamage(30F);
		this.setCollisionSize(2.5d);
		this.setPassThroughEntities();
		this.setCanGetStuckInGround();
		this.setDamageSource(this.getDamageSource().getSource());
	}

	public void registerData() {
		super.registerData();
		this.dataManager.register(NEXT_ID, -1);
		this.dataManager.register(PREV_ID, -1);
	}

	public Entity getPrev() {
		return this.world.getEntityByID(this.dataManager.get(PREV_ID));
	}

	public Entity getNext() {
		return this.world.getEntityByID(this.dataManager.get(NEXT_ID));
	}

	public void setPrev(Entity ent) {
		this.dataManager.set(PREV_ID, ent.getEntityId());
	}

	public void setNext(Entity ent) {
		this.dataManager.set(NEXT_ID, ent.getEntityId());
	}

	public void tick() {
		if (this.getLife() <= 0 && this.getMaxLife() != 2) {
			this.setMaxLife(2);
			JetCulverinProjectile projectile = new JetCulverinProjectile(this.world, this.getThrower(), this.master);
			projectile.shoot(this, this.rotationPitch, ((MathHelper.nextInt(rand, -45, 45) + this.rotationYaw + 180) % 360) - 180, 0, this.getSpeedFactor(), 0);
			this.world.addEntity(projectile);
			this.setNext(projectile);
			projectile.setPrev(this);
			projectile.setPositionAndUpdate(this.getPosX(), this.getPosY(), this.getPosZ());
			this.setVelocity(0, 0, 0);
		} else if (this.getMaxLife() == 2 && (this.getNext() == null || !this.getNext().isAlive())) {
			this.setDead();
		} else if (this.getMaxLife() == 2) {
			this.setLife(2);
		}
		super.tick();
		if (this.getMaxLife() == 2 && this.getNext() != null && this.getNext().isAlive()) {
			Entity prev = this.getPrev();
			if (prev == null) {
				prev = this.getThrower();
			}
			double xDif = prev.getPosX() - this.getPosX();
			double yDif = prev.getPosY() - this.getPosY();
			double zDif = prev.getPosZ() - this.getPosZ();
			float pitchSet = (float) (((-180) / Math.PI) * Math.acos(Math.sqrt(Math.pow(xDif, 2) + Math.pow(zDif, 2)) / Math.sqrt(Math.pow(xDif, 2) + Math.pow(yDif, 2) + Math.pow(zDif, 2))));
			float yawSet = (float) (((-180) / Math.PI) * Math.acos(xDif / Math.sqrt(Math.pow(xDif, 2) + Math.pow(zDif, 2))));
			this.setRotation(yawSet, pitchSet);
		}
	}
}

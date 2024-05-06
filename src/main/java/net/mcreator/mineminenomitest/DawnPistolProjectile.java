package net.mcreator.mineminenomitest;

import xyz.pixelatedw.mineminenomi.particles.effects.gomu.GearSecondParticleEffect;
import xyz.pixelatedw.mineminenomi.entities.projectiles.gomu.GomuGomuNoPistolProjectile;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.api.abilities.ExplosionAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;

import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.EntityType;

public class DawnPistolProjectile extends GomuGomuNoPistolProjectile {
	public DawnPistolProjectile(EntityType type, World world) {
		super(type, world);
	}

	public DawnPistolProjectile(World world, LivingEntity player, Ability ability) {
		super(world, player, ability);
		this.setMaxLife(6);
		this.setDamage(100F);
		this.onBlockImpactEvent = this::onBlockImpactEvent;
	}

	private void onBlockImpactEvent(BlockPos hit) {
		ExplosionAbility explosion = AbilityHelper.newExplosion(this.getThrower(), this.world, this.getPosX(), this.getPosY(), this.getPosZ(), 1F);
		explosion.setStaticDamage(5F);
		explosion.setExplosionSound(false);
		explosion.setDamageOwner(false);
		explosion.setDestroyBlocks(true);
		explosion.setFireAfterExplosion(false);
		explosion.setDamageEntities(false);
		explosion.doExplosion();
	}

	private void onTickEvent() {
		new GearSecondParticleEffect().spawn(this.world, this.getPosX(), this.getPosY(), this.getPosZ(), 0.0D, 0.0D, 0.0D);
	}
}

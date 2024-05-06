package net.mcreator.mineminenomitest;

import xyz.pixelatedw.mineminenomi.entities.projectiles.brawler.BrawlerProjectiles;
import xyz.pixelatedw.mineminenomi.entities.projectiles.AbilityProjectileEntity;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.api.abilities.ExplosionAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;

import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.EntityType;

public class KingKongGunProjectile extends AbilityProjectileEntity {
	public KingKongGunProjectile(EntityType type, World world) {
		super(type, world);
	}

	public KingKongGunProjectile(World world, LivingEntity player, Ability ability) {
		super(NikaProjectiles.GOMU_GOMU_NO_KING_KONG_GUN.get(), world, player, ability);
		this.setMaxLife(25);
		this.setDamage(120F);
		this.setCollisionSize(4d);
		this.setPassThroughEntities();
		this.setCanGetStuckInGround();
		this.setDamageSource(this.getDamageSource().getSource());
		this.onBlockImpactEvent = this::onBlockImpactEvent;
	}

	private void onBlockImpactEvent(BlockPos hit) {
		ExplosionAbility explosion = AbilityHelper.newExplosion(this.getThrower(), this.world, this.getPosX(), this.getPosY(), this.getPosZ(), 13F);
		explosion.setStaticDamage(80.0F);
		explosion.setExplosionSound(false);
		explosion.setDamageOwner(false);
		explosion.setDestroyBlocks(true);
		explosion.setFireAfterExplosion(false);
		explosion.setDamageEntities(false);
		explosion.doExplosion();
	}
}

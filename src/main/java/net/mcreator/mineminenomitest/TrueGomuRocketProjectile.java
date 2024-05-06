package net.mcreator.mineminenomitest;

import xyz.pixelatedw.mineminenomi.entities.projectiles.gomu.GomuGomuNoRocketProjectile;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;

import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity;

public class TrueGomuRocketProjectile extends GomuGomuNoRocketProjectile {
	public IOnBlockImpact oldOnImpact;
	public Ability master;

	public TrueGomuRocketProjectile(World world, LivingEntity player, Ability ability) {
		super(world, player, ability);
		this.oldOnImpact = this.onBlockImpactEvent;
		this.onBlockImpactEvent = this::onBlockImpact;
		this.onEntityImpactEvent = this::onEntityImpact;
		this.setDamage(0f);
		this.master = ability;
	}

	private void onBlockImpact(BlockPos pos) {
		this.oldOnImpact.onImpact(pos);
		this.getThrower().isAirBorne = true;
		((TrueGomuRocket) this.master).setFlying();
	}

	private void onEntityImpact(Entity ent) {
		this.onBlockImpact(new BlockPos(ent.getPosX(), ent.getPosY() + (double) ent.getHeight() / 2, ent.getPosZ()));
	}
}

package net.mcreator.mineminenomitest;

import xyz.pixelatedw.mineminenomi.wypi.WyHelper;
import xyz.pixelatedw.mineminenomi.entities.projectiles.AbilityProjectileEntity;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;

import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.block.Blocks;

public class KingBajrangGunProjectile extends AbilityProjectileEntity {
	public KingBajrangGunProjectile(EntityType type, World world) {
		super(type, world);
	}

	public KingBajrangGunProjectile(World world, LivingEntity player, Ability ability) {
		super(NikaProjectiles.GOMU_GOMU_NO_KING_BAJRANG_GUN.get(), world, player, ability);
		this.setMaxLife(250);
		this.setDamage(0F);
		this.setCollisionSize(5);
		this.setPassThroughEntities();
		this.setCanGetStuckInGround();
		this.setDamageSource(this.getDamageSource().getSource());
		this.onTickEvent = this::onTickEvent;
		this.onEntityImpactEvent = this::onEntityImpactEvent;
	}

	private void onTickEvent() {
		for (BlockPos pos : WyHelper.getNearbyBlocks(this, 5)) {
			this.world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
		}
	}

	private void onEntityImpactEvent(LivingEntity hitEnt) {
		hitEnt.remove();
	}
}

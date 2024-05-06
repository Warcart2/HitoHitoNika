package net.mcreator.mineminenomitest;

import xyz.pixelatedw.mineminenomi.items.weapons.CoreSwordItem;
import xyz.pixelatedw.mineminenomi.items.weapons.ClimaTactItem;
import xyz.pixelatedw.mineminenomi.init.ModDamageSource;
import xyz.pixelatedw.mineminenomi.entities.projectiles.hitodaibutsu.ImpactBlastProjectile;
import xyz.pixelatedw.mineminenomi.entities.projectiles.extra.PopGreenProjectile;
import xyz.pixelatedw.mineminenomi.entities.projectiles.extra.NormalBulletProjectile;
import xyz.pixelatedw.mineminenomi.entities.projectiles.extra.CannonBallProjectile;
import xyz.pixelatedw.mineminenomi.entities.projectiles.AbilityProjectileEntity;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.IDevilFruit;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.DevilFruitCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.api.helpers.ItemsHelper;
import xyz.pixelatedw.mineminenomi.api.helpers.HakiHelper;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceElement;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

import net.minecraft.util.DamageSource;
import net.minecraft.item.TridentItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.item.AxeItem;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity;

import java.util.Arrays;
import java.util.ArrayList;
import xyz.pixelatedw.mineminenomi.init.ModAttributes;

@Mod.EventBusSubscriber(modid = "hito_hito_no_mi_nika")
public class TrueGomuPassiveEffects {
	@SubscribeEvent
	public static void onEntityHurt(LivingHurtEvent event) {
		if (event.getEntityLiving() instanceof PlayerEntity) {
			DamageSource source = event.getSource();
			Entity instantSource = source.getImmediateSource();
			Entity trueSource = source.getTrueSource();
			PlayerEntity attacked = (PlayerEntity) event.getEntityLiving();
			IDevilFruit props = DevilFruitCapability.get(attacked);
			if (props.hasDevilFruit(TrueGomuGomuNoMi.HITO_HITO_NO_MI_NIKA) && !source.isUnblockable()) {
				float reduction = 0.0F;
				ArrayList<String> instantSources = new ArrayList(Arrays.asList("mob", "player"));
				boolean a = false;
				if (instantSource instanceof LivingEntity) {
					ItemStack mainhandGear = ((LivingEntity) instantSource).getItemStackFromSlot(EquipmentSlotType.MAINHAND);
					a = trueSource instanceof LivingEntity && !HakiHelper.hasHardeningActive((LivingEntity) instantSource) && instantSources.contains(source.getDamageType()) && !source.isDamageAbsolute()
							&& getGomuDamagingItems(mainhandGear.getItem()) && !ItemsHelper.isKairosekiWeapon(mainhandGear);
				}
				boolean b = source.isProjectile() && instantSource instanceof AbilityProjectileEntity && ((AbilityProjectileEntity) instantSource).isPhysical() && !((AbilityProjectileEntity) instantSource).isAffectedByHaki();
				if ((a || b) && !source.isDamageAbsolute()) {
					reduction = 0.75F;
				}
				if (source instanceof ModDamageSource && ((ModDamageSource) source).getElement() == SourceElement.LIGHTNING) {
					reduction = 1.0F;
				}
				event.setAmount(event.getAmount() * (1.0F - reduction));
			}
			//HitoHitoNoMiNikaMod.LOGGER.info();
			event.setAmount(event.getAmount() / ((float) attacked.getAttributeValue(ModAttributes.DAMAGE_REDUCTION.get()) + 1f));
		}
	}

	@SubscribeEvent
	public static void onEntityAttackEvent(LivingAttackEvent event) {
		if (event.getEntityLiving() instanceof PlayerEntity) {
			PlayerEntity attacked = (PlayerEntity) event.getEntityLiving();
			IAbilityData props = AbilityDataCapability.get(attacked);
			IDevilFruit devilFruitProps = DevilFruitCapability.get(attacked);
			if (devilFruitProps.hasDevilFruit(TrueGomuGomuNoMi.HITO_HITO_NO_MI_NIKA)) {
				DamageSource source = event.getSource();
				Entity instantSource = source.getImmediateSource();
				if (instantSource instanceof NormalBulletProjectile || (instantSource instanceof CannonBallProjectile && TrueGomuHelper.hasAbilityActive(props, GomuFusenAbility.INSTANCE)) || instantSource instanceof PopGreenProjectile
						|| (instantSource instanceof ImpactBlastProjectile && TrueGomuHelper.hasAbilityActive(props, GomuFusenAbility.INSTANCE) && TrueGomuHelper.hasGearThirdActive(props))) {
					AbilityProjectileEntity ablProj = (AbilityProjectileEntity) instantSource;
					if (ablProj.getThrower() != null && ablProj.isAffectedByHaki()) {
						LivingEntity thrower = ablProj.getThrower();
						boolean isImbued = ablProj.isAffectedByImbuing() && HakiHelper.hasImbuingActive(thrower, true);
						if (isImbued) {
							return;
						}
					}
					event.setCanceled(true);
					((AbilityProjectileEntity) instantSource).setThrower(attacked);
					((AbilityProjectileEntity) instantSource).shoot(-instantSource.getMotion().x, -instantSource.getMotion().y, -instantSource.getMotion().z, 0.8F, 20.0F);
				}
			}
		}
	}

	private static boolean getGomuDamagingItems(Item item) {
		if ((!(item instanceof SwordItem) || item instanceof CoreSwordItem) && !(item instanceof PickaxeItem) && !(item instanceof AxeItem) && !(item instanceof TridentItem) && !(item instanceof ClimaTactItem)) {
			return item instanceof CoreSwordItem ? ((CoreSwordItem) item).isBlunt() : true;
		} else {
			return false;
		}
	}
}

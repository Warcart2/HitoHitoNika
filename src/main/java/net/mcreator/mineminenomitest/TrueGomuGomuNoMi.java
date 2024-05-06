package net.mcreator.mineminenomitest;

import xyz.pixelatedw.mineminenomi.wypi.WyRegistry;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;
import xyz.pixelatedw.mineminenomi.items.AkumaNoMiItem;
import xyz.pixelatedw.mineminenomi.init.ModValues;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.IDevilFruit;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.DevilFruitCapability;
import xyz.pixelatedw.mineminenomi.api.helpers.DevilFruitHelper;
import xyz.pixelatedw.mineminenomi.api.enums.FruitType;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.abilities.gomu.BouncyAbility;

import net.minecraftforge.eventbus.api.IEventBus;

import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.LivingEntity;

import java.util.Objects;
import java.util.Arrays;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.wypi.WyNetwork;
import xyz.pixelatedw.mineminenomi.packets.server.SSyncDevilFruitPacket;
import xyz.pixelatedw.mineminenomi.packets.server.SSyncAbilityDataPacket;
import net.minecraft.entity.player.PlayerEntity;

public class TrueGomuGomuNoMi {
	public static final ProgressingAkumaNoMiItem HITO_HITO_NO_MI_NIKA;

	private static <T extends AkumaNoMiItem> T registerFruit(T fruit) {
		if (fruit.type == FruitType.LOGIA) {
			ModValues.logias.add(fruit);
		}
		ModValues.devilfruits.add(fruit);
		String resourceName = WyHelper.getResourceName(fruit.getDevilFruitName());
		WyRegistry.getLangMap().put("item.mineminenomi." + resourceName, fruit.getDevilFruitName());
		WyRegistry.registerItem(fruit.getDevilFruitName(), () -> {
			return fruit;
		});
		if (fruit.getAbilities() != null && fruit.getAbilities().length > 0) {
			registerAbilities(fruit.getAbilities());
		}
		return fruit;
	}

	private static void registerAbilities(AbilityCore[] abilities) {
		Arrays.stream(abilities).filter(Objects::nonNull).forEach((abl) -> {
			WyRegistry.registerAbility(abl);
		});
	}

	public static void register(IEventBus eventBus) {
	}

	private static AbilityCore[] builder() {
		if (WyHelper.isAprilFirst())
			return new AbilityCore[]{TrueGomuPistol.INSTANCE, StrongGomuPistol.INSTANCE, TrueGomuGatling.INSTANCE, TrueGomuBazooka.INSTANCE, GomuFusenAbility.INSTANCE, TrueGomuRocket.INSTANCE, TrueGearSecondAbility.INSTANCE, TrueGearThirdAbility.INSTANCE, TrueGearFourthAbility.INSTANCE,
					FifthGearAbility.INSTANCE, GearSixthAbility.INSTANCE, BouncyAbility.INSTANCE, GomuMorphsAbility.INSTANCE};
		return new AbilityCore[]{TrueGomuPistol.INSTANCE, StrongGomuPistol.INSTANCE, TrueGomuGatling.INSTANCE, TrueGomuBazooka.INSTANCE, GomuFusenAbility.INSTANCE, TrueGomuRocket.INSTANCE, TrueGearSecondAbility.INSTANCE, TrueGearThirdAbility.INSTANCE, TrueGearFourthAbility.INSTANCE,
				FifthGearAbility.INSTANCE, BouncyAbility.INSTANCE, GomuMorphsAbility.INSTANCE};
	}

	static {
		HITO_HITO_NO_MI_NIKA = registerFruit(new ProgressingAkumaNoMiItem("Hito Hito no Mi, Model: Nika", 3, FruitType.MYTHICAL_ZOAN, builder()));
		DevilFruitHelper.ZOAN_MODELS.put("hito_hito_nika", "nika");
	}

	public static class ProgressingAkumaNoMiItem extends AkumaNoMiItem {
		public ProgressingAkumaNoMiItem(String name, int tier, FruitType type, AbilityCore... abilitiesArray) {
			super(name, tier, type, abilitiesArray);
		}

		@Override
		public ItemStack onItemUseFinish(ItemStack stack, World world, LivingEntity entity) {
			ItemStack out = super.onItemUseFinish(stack, world, entity);
			IAbilityData props = AbilityDataCapability.get(entity);
			for (AbilityCore core : this.getAbilities()) {
				props.removeUnlockedAbility(core);
			}
			WyNetwork.sendTo(new SSyncAbilityDataPacket(entity.getEntityId(), props), (PlayerEntity) entity);
			WyNetwork.sendTo(new SSyncDevilFruitPacket(entity.getEntityId(), DevilFruitCapability.get(entity)), (PlayerEntity) entity);
			return out;
		}
	}
}

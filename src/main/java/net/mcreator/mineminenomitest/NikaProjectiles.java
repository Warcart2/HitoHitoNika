package net.mcreator.mineminenomitest;

import xyz.pixelatedw.mineminenomi.wypi.WyRegistry;
import xyz.pixelatedw.mineminenomi.renderers.abilities.StretchingProjectileRenderer;
import xyz.pixelatedw.mineminenomi.renderers.abilities.AbilityProjectileRenderer;
import xyz.pixelatedw.mineminenomi.models.abilities.EntityArmModel;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.EntityType;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class NikaProjectiles {
	public static final RegistryObject<EntityType<KingKongGunProjectile>> GOMU_GOMU_NO_KING_KONG_GUN = WyRegistry.registerEntityType("Gomu Gomu no King Kong Gun", () -> {
		return WyRegistry.createEntityType(KingKongGunProjectile::new).size(8F, 8F).build("mineminenomi:gomu_gomu_no_king_kong_gun");
	});
	public static final RegistryObject<EntityType<BajrangGunProjectile>> GOMU_GOMU_NO_BAJRANG_GUN = WyRegistry.registerEntityType("Gomu Gomu no Bajrang Gun", () -> {
		return WyRegistry.createEntityType(BajrangGunProjectile::new).size(20F, 20F).build("mineminenomi:gomu_gomu_no_bajrang_gun");
	});
	public static final RegistryObject<EntityType<BajrangGunProjectile>> GOMU_GOMU_NO_KING_BAJRANG_GUN = WyHelper.isAprilFirst() ? WyRegistry.registerEntityType("Gomu Gomu no King Bajrang Gun", () -> {
		return WyRegistry.createEntityType(BajrangGunProjectile::new).size(0.5F, 0.5F).build("mineminenomi:gomu_gomu_no_king_bajrang_gun");
	}) : null;

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void registerEntityRenderers(FMLClientSetupEvent event) {
		RenderingRegistry.registerEntityRenderingHandler(GOMU_GOMU_NO_KING_KONG_GUN.get(), (new StretchingProjectileRenderer.Factory(new EntityArmModel())).setStretchScale(25D, 25D, 10D).setPlayerTexture());
		RenderingRegistry.registerEntityRenderingHandler(GOMU_GOMU_NO_BAJRANG_GUN.get(), (new StretchingProjectileRenderer.Factory(new EntityArmModel(), new EntityArmModel())).setStretchScale(4.5D, 4.5D).setScale(150D, 150D, 150D).setPlayerTexture());
		if (WyHelper.isAprilFirst())
			RenderingRegistry.registerEntityRenderingHandler(GOMU_GOMU_NO_KING_BAJRANG_GUN.get(), (new AbilityProjectileRenderer.Factory(new KingBajrangGunModel())).setTexture(new ResourceLocation("hito_hito_no_mi_nika:textures/entities/king_bajrang_gun.png")));
	}
}

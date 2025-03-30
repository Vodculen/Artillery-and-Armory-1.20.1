package net.vodculen.artilleryandarmory.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.vodculen.artilleryandarmory.ArtilleryArmory;
import net.vodculen.artilleryandarmory.entity.custom.KunaiProjectileEntity;

public class ModEntities {
	public static final EntityType<KunaiProjectileEntity> KUNAI = Registry.register(Registries.ENTITY_TYPE, 
	Identifier.of(ArtilleryArmory.MOD_ID, "kunai"),
		EntityType.Builder.<KunaiProjectileEntity>create(KunaiProjectileEntity::new, SpawnGroup.MISC).setDimensions(0.5F, 0.5F).build("kunai")
	);

	public static void registerModEntityTypes() {
		ArtilleryArmory.LOGGER.info("Registering Mod Entity Types for " + ArtilleryArmory.MOD_ID);
	}
}

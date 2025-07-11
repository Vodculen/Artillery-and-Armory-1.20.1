package net.vodculen.artilleryandarmory.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.vodculen.artilleryandarmory.ArtilleryArmory;
import net.vodculen.artilleryandarmory.entity.custom.KunaiProjectileEntity;

public class ModEntities {
	public static final EntityType<KunaiProjectileEntity> KUNAI = registerEntity(
		"kunai",
		EntityType.Builder.<KunaiProjectileEntity>create(KunaiProjectileEntity::new, SpawnGroup.MISC)
			.setDimensions(0.5F, 0.5F)
			.build("kunai")
	);

	
	// Below are helper classes that make defining Items easier as well as making them accessible to the entry class
	private static <T extends Entity> EntityType<T> registerEntity(String name, EntityType<T> entity) {
		return Registry.register(Registries.ENTITY_TYPE, Identifier.of(ArtilleryArmory.MOD_ID, name), entity);
	}

	public static void registerModEntityTypes() {
		ArtilleryArmory.LOGGER.info("Registering Modded Entities");
	}
}

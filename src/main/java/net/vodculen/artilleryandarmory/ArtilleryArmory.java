package net.vodculen.artilleryandarmory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;
import net.vodculen.artilleryandarmory.effect.ModStatusEffects;
import net.vodculen.artilleryandarmory.enchantment.ModEnchantments;
import net.vodculen.artilleryandarmory.entity.ModEntities;
import net.vodculen.artilleryandarmory.item.ModItems;
import net.vodculen.artilleryandarmory.util.ModLootTables;
import net.vodculen.artilleryandarmory.util.ModVillagerTrades;


public class ArtilleryArmory implements ModInitializer {
	public static final String MOD_ID = "artilleryandarmory";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModStatusEffects.registerModEffects();
		ModEntities.registerModEntityTypes();
		ModEnchantments.registerModEnchantments();
		ModVillagerTrades.registerModVillagerTrades();
		ModLootTables.registerLootTables();
	}
}
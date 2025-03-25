package net.vodculen.artilleryandarmory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.vodculen.artilleryandarmory.item.ModItems;
import net.vodculen.artilleryandarmory.util.ModApplyEffects;
import net.vodculen.artilleryandarmory.item.ModItemGroups;

import net.fabricmc.api.ModInitializer;

public class ArtilleryArmory implements ModInitializer {
	public static final String MOD_ID = "artilleryandarmory";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModItemGroups.registerItemGroups();
		ModApplyEffects.applyStunEffect();
	}
}
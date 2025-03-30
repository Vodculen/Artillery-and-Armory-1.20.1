package net.vodculen.artilleryandarmory.item;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.vodculen.artilleryandarmory.ArtilleryArmory;
import net.vodculen.artilleryandarmory.item.weapons.Chamber;
import net.vodculen.artilleryandarmory.item.weapons.Hammer;
import net.vodculen.artilleryandarmory.item.weapons.Kunai;
import net.vodculen.artilleryandarmory.item.weapons.Lance;


public class ModItems {
	public static final Item HAMMER = registerItem("hammer", new Hammer(15, -3.93F, new Item.Settings()));  
	public static final Item CHAMBER = registerItem("chamber", new Chamber(15, -3.93F, new Item.Settings()));  
	public static final Item LANCE = registerItem("lance", new Lance(new Item.Settings().maxDamage(250))); 
	public static final Item KUNAI = registerItem("kunai", new Kunai(new Item.Settings().maxDamage(250))); 
	
	
	private static Item registerItem(String name, Item item) {
		return Registry.register(Registries.ITEM, Identifier.of(ArtilleryArmory.MOD_ID, name), item);
	}

	public static void registerModItems() {
		ArtilleryArmory.LOGGER.info("Registering Mod Items for " + ArtilleryArmory.MOD_ID);
	}
}

package net.vodculen.artilleryandarmory.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.vodculen.artilleryandarmory.ArtilleryArmory;
import net.vodculen.artilleryandarmory.item.weapons.Chamber;
import net.vodculen.artilleryandarmory.item.weapons.Hammer;
import net.vodculen.artilleryandarmory.item.weapons.Kunai;
import net.vodculen.artilleryandarmory.item.weapons.Lance;


public class ModItems {
	// The item definitions 
	public static final Item HAMMER = registerItem("hammer", new Hammer(new Item.Settings().maxDamage(762)));  
	public static final Item CHAMBER = registerItem("chamber", new Chamber(new Item.Settings().maxDamage(1024)));  
	public static final Item LANCE = registerItem("lance", new Lance(new Item.Settings().maxDamage(512))); 
	public static final Item KUNAI = registerItem("kunai", new Kunai(new Item.Settings().maxDamage(762))); 
	
	
	// This allows the Items to be defined easily
	private static Item registerItem(String name, Item item) {
		return Registry.register(Registries.ITEM, Identifier.of(ArtilleryArmory.MOD_ID, name), item);
	}

	// This makes the class known to the mod
	public static void registerModItems() {
		ArtilleryArmory.LOGGER.info("[Artillery & Armory]: Registering Modded Items");

		// It also puts the Items into a pre-existing ItemGroup
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
			entries.addAfter(Items.TRIDENT, ModItems.KUNAI);
			entries.addAfter(ModItems.KUNAI, ModItems.HAMMER);
			entries.addAfter(ModItems.HAMMER, ModItems.CHAMBER);
			entries.addAfter(ModItems.CHAMBER, ModItems.LANCE);
		});
	}
}

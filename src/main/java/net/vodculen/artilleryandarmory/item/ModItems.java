package net.vodculen.artilleryandarmory.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.vodculen.artilleryandarmory.ArtilleryArmory;
import net.vodculen.artilleryandarmory.item.custom.ChamberItem;
import net.vodculen.artilleryandarmory.item.custom.HammerItem;
import net.vodculen.artilleryandarmory.item.custom.KunaiItem;
import net.vodculen.artilleryandarmory.item.custom.LanceItem;


public class ModItems {
	public static final Item HAMMER = registerItem("hammer", new HammerItem(new Item.Settings().maxDamage(762)));  
	public static final Item CHAMBER = registerItem("chamber", new ChamberItem(new Item.Settings().maxDamage(1024)));  
	public static final Item LANCE = registerItem("lance", new LanceItem(new Item.Settings().maxDamage(512))); 
	public static final Item KUNAI = registerItem("kunai", new KunaiItem(new Item.Settings().maxDamage(762)));
	
	// Below are helper classes that make defining Items easier as well as making them accessible to the entry class
	private static Item registerItem(String name, Item item) {
		return Registry.register(Registries.ITEM, Identifier.of(ArtilleryArmory.MOD_ID, name), item);
	}

	public static void registerModItems() {
		ArtilleryArmory.LOGGER.info("Registering Modded Items");

		ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
			entries.addAfter(Items.TRIDENT, ModItems.KUNAI);
			entries.addAfter(ModItems.KUNAI, ModItems.HAMMER);
			entries.addAfter(ModItems.HAMMER, ModItems.CHAMBER);
			entries.addAfter(ModItems.CHAMBER, ModItems.LANCE);
		});
	}
}

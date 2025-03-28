package net.vodculen.artilleryandarmory.item;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.vodculen.artilleryandarmory.ArtilleryArmory;
import net.vodculen.artilleryandarmory.item.weapons.Hammer;
import net.vodculen.artilleryandarmory.item.weapons.Lance;

public class ModItems {
    // Copy-paste this and jut rename stuff so it matches
    // TODO make sure you put with quotes "item.artilleryandarmory.[weapon name]": "[weapon name fancy]" weapon name = lower case version in the "" in the Item class    
    public static final Item HAMMER = registerItem("hammer", new Hammer(15, -3.93F, new Item.Settings())); // this is just extra attack damage and speed for the sword to apply to itself while the rest of the tools will use the values declared in the ModToolMaterials 
    public static final Item LANCE = registerItem("lance", new Lance(2, -2.0F, new Item.Settings())); // this is just extra attack damage and speed for the sword to apply to itself while the rest of the tools will use the values declared in the ModToolMaterials 
    
    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(ArtilleryArmory.MOD_ID, name), item);
    }

    public static void registerModItems() {
        ArtilleryArmory.LOGGER.info("Registering Mod Items for " + ArtilleryArmory.MOD_ID);
    }
}

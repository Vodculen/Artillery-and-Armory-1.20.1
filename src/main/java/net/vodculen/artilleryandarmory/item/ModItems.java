package net.vodculen.artilleryandarmory.item;

import net.vodculen.artilleryandarmory.ArtilleryArmory;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems { 
    public static final Item TEST = registerItem("test", new Item(new Item.Settings()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(ArtilleryArmory.MOD_ID, name), item);
    }

    public static void registerModItems() {
        ArtilleryArmory.LOGGER.info("Registering Mod Items for " + ArtilleryArmory.MOD_ID);
    }
}

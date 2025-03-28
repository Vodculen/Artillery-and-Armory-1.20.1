package net.vodculen.artilleryandarmory.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.vodculen.artilleryandarmory.ArtilleryArmory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.text.Text;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;

public class ModItemGroups {
    public static final ItemGroup ARTILLERY_AND_ARMORY_ITEM_GROUP = Registry.register(Registries.ITEM_GROUP, Identifier.of(ArtilleryArmory.MOD_ID, "artillery_and_armory_item_group"),
        FabricItemGroup.builder().icon(() -> new ItemStack(ModItems.HAMMER)).displayName(Text.translatable("itemgroup.artillery_and_armory_item_group"))
        .entries((displayContext, entries) -> {
            entries.add(ModItems.HAMMER);
            entries.add(ModItems.LANCE);
        }).build());
    

    public static void registerItemGroups() {
        ArtilleryArmory.LOGGER.info("Registering Item Groups for " + ArtilleryArmory.MOD_ID);
    }
}

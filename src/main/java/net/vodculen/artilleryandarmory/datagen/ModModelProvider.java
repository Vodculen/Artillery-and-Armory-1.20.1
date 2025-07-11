package net.vodculen.artilleryandarmory.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.vodculen.artilleryandarmory.item.ModItems;

public class ModModelProvider extends FabricModelProvider {
	public ModModelProvider(FabricDataOutput output) {
		super(output);
	}

	@Override
	public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

	}

	@Override
	public void generateItemModels(ItemModelGenerator itemModelGenerator) {
		itemModelGenerator.register(ModItems.HAMMER, Models.GENERATED);
		itemModelGenerator.register(ModItems.CHAMBER, Models.GENERATED);
		itemModelGenerator.register(ModItems.LANCE, Models.GENERATED);
		itemModelGenerator.register(ModItems.KUNAI, Models.GENERATED);
	}

}

package net.vodculen.artilleryandarmory.datagen;

import java.util.function.Consumer;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.vodculen.artilleryandarmory.item.ModItems;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModItems.HAMMER)
        .pattern(" -+")
        .pattern("_/-")
        .pattern("/  ")
        .input('+', Items.ANVIL)
        .input('-', Items.IRON_INGOT)
        .input('_', Items.RED_CARPET)
        .input('/', Items.STICK)
        .criterion(hasItem(Items.ANVIL), conditionsFromItem(Items.ANVIL))
        .offerTo(exporter);
    }
    
}

package net.vodculen.artilleryandarmory.util;

import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.VillagerProfession;
import net.vodculen.artilleryandarmory.item.ModItems;

public interface ModVillagerTrades {
	public static void registerModVillagerTrades() {
		TradeOfferHelper.registerVillagerOffers(VillagerProfession.WEAPONSMITH, 4, factories -> {
			factories.add((entity, random) -> new TradeOffer(new ItemStack(Items.EMERALD, 20), new ItemStack(ModItems.LANCE, 1), 2, 10, (float) 0.5));
		});
	}
}

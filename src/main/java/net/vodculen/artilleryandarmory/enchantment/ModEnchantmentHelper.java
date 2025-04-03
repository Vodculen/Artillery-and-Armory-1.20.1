package net.vodculen.artilleryandarmory.enchantment;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;

public class ModEnchantmentHelper extends EnchantmentHelper {
    public static int getHorseBack(ItemStack stack) {
		return getLevel(ModEnchantments.HORSE_BACK, stack);
	}

    public static int getExplosives(ItemStack stack) {
		return getLevel(ModEnchantments.EXPLOSIVES, stack);
	}

	public static int getInflict(ItemStack stack) {
		return getLevel(ModEnchantments.INFLICT, stack);
	}

	public static int getJumble(ItemStack stack) {
		return getLevel(ModEnchantments.JUMBLE, stack);
	}
}

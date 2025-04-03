package net.vodculen.artilleryandarmory.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.vodculen.artilleryandarmory.item.ModItems;

public class InflictEnchantment extends Enchantment {
    public InflictEnchantment() {
		super(Enchantment.Rarity.UNCOMMON, EnchantmentTarget.WEAPON, new EquipmentSlot[] {EquipmentSlot.MAINHAND});
	}

	@Override
	public int getMinPower(int level) {
		return 20;
	}

	@Override
	public int getMaxLevel() {
		return 1;
	}

	@Override
	public boolean isAcceptableItem(ItemStack stack) {
		return stack.isOf(ModItems.KUNAI);
	}

	@Override
	public boolean isAvailableForRandomSelection() {
		return true;
	}

	@Override
	public boolean isAvailableForEnchantedBookOffer() {
		return true;
	}
}

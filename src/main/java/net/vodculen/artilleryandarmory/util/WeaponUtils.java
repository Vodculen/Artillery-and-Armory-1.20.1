package net.vodculen.artilleryandarmory.util;

import java.util.function.Predicate;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.vodculen.artilleryandarmory.item.weapons.ExplodingWeaponItem;

public class WeaponUtils {
	public static ItemStack getProjectileTypeForWeapon(ItemStack stack, PlayerEntity player) {
		if (!(stack.getItem() instanceof ExplodingWeaponItem)) {
			return ItemStack.EMPTY;
		} else {
			Predicate<ItemStack> predicate = ((ExplodingWeaponItem)stack.getItem()).getHeldExplosives();
			ItemStack itemStack = ExplodingWeaponItem.getHeldExplosive(player, predicate);

			if (!itemStack.isEmpty()) {
				return itemStack;
			} else {
				predicate = ((ExplodingWeaponItem)stack.getItem()).getExplosives();
				
				for (int i = 0; i < player.getInventory().size(); i++) {
					ItemStack itemStack2 = player.getInventory().getStack(i);
					if (predicate.test(itemStack2)) {
						return itemStack2;
					}
				}
				
				return player.getAbilities().creativeMode ? new ItemStack(Items.GUNPOWDER) : ItemStack.EMPTY;
			}
		}
	}
}

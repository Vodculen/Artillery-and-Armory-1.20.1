package net.vodculen.artilleryandarmory.item.custom;

import java.util.function.Predicate;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

public abstract class ExplodingWeaponItem extends Item {
	public static final Predicate<ItemStack> EXPLODING_PROJECTILES = stack -> stack.isOf(Items.GUNPOWDER);

	public ExplodingWeaponItem(Settings settings) {
		super(settings);
	}

	public Predicate<ItemStack> getHeldExplosives() {
		return this.getExplosives();
	}

	public abstract Predicate<ItemStack> getExplosives();

	public static ItemStack getHeldExplosive(LivingEntity entity, Predicate<ItemStack> predicate) {
		if (predicate.test(entity.getStackInHand(Hand.OFF_HAND))) {
			return entity.getStackInHand(Hand.OFF_HAND);
		} else {
			return predicate.test(entity.getStackInHand(Hand.MAIN_HAND)) ? entity.getStackInHand(Hand.MAIN_HAND) : ItemStack.EMPTY;
		}
	}
}

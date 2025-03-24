package net.vodculen.artilleryandarmory.item.tools;

import java.util.function.Supplier;

import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Lazy;

public enum ModToolMaterials implements ToolMaterial {
	// Material name, copy-paste it and change values
	COOLIUM(3, 1159, 20.0F, 100.0F, 15, () -> Ingredient.ofItems(Items.SEA_PICKLE));

	private final int miningLevel; // 1 = wood, 2 = iron, 3 = diamond, 4 = netherite
	private final int itemDurability; // how many uses 
	private final float miningSpeed; // how fast it breaks stuff
	private final float attackDamage; // how much damage 
	private final int enchantability; // the max amout of ex you can use on the item before "Too Expensive"
	@SuppressWarnings("deprecation")
	private final Lazy<Ingredient> repairIngredient; // what ingredient it uses to repair itself in the anvil


	// TODO Please don't touch anything below this 
	@SuppressWarnings("deprecation")
	private ModToolMaterials(int miningLevel, int itemDurability, float miningSpeed, float attackDamage, int enchantability, Supplier<Ingredient> repairIngredient) {
		this.miningLevel = miningLevel;
		this.itemDurability = itemDurability;
		this.miningSpeed = miningSpeed;
		this.attackDamage = attackDamage;
		this.enchantability = enchantability;
		this.repairIngredient = new Lazy<>(repairIngredient);
	}

	@Override
	public int getDurability() {
		return this.itemDurability;
	}

	@Override
	public float getMiningSpeedMultiplier() {
		return this.miningSpeed;
	}

	@Override
	public float getAttackDamage() {
		return this.attackDamage;
	}

	@Override
	public int getMiningLevel() {
		return this.miningLevel;
	}

	@Override
	public int getEnchantability() {
		return this.enchantability;
	}

	@SuppressWarnings("deprecation")
	@Override
	public Ingredient getRepairIngredient() {
		return this.repairIngredient.get();
	}

}

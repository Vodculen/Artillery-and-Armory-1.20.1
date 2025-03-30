package net.vodculen.artilleryandarmory.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;

public class HorseBackEnchantment extends Enchantment {
	public HorseBackEnchantment() {
		super(Enchantment.Rarity.UNCOMMON, EnchantmentTarget.WEAPON, new EquipmentSlot[] {EquipmentSlot.MAINHAND});
	}

	@Override
	public int getMinPower(int level) {
		return 10;
	}

	@Override
	public int getMaxLevel() {
		return 1;
	}

	public static void applyHorseRidingEffects(PlayerEntity player, int enchantmentLevel) {
		if (player.getVehicle() instanceof HorseEntity horse) {
			modifyHorseSpeed(horse, enchantmentLevel);
			modifyPlayerAttackDamage(player, enchantmentLevel);
		}
	}

	private static void modifyHorseSpeed(HorseEntity horse, int level) {
		double speedMultiplier = 0.5;
		horse.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).addTemporaryModifier(
				new EntityAttributeModifier("HorseSpeedBoost", speedMultiplier, EntityAttributeModifier.Operation.MULTIPLY_BASE)
		);
	}

	private static void modifyPlayerAttackDamage(PlayerEntity player, int level) {
		double attackDamageMultiplier = 5;
		player.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).addTemporaryModifier(
				new EntityAttributeModifier("HorseAttackDamageBoost", attackDamageMultiplier, EntityAttributeModifier.Operation.ADDITION)
		);
	}    
}
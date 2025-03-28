package net.vodculen.artilleryandarmory.item.weapons;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.Vanishable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Lance extends Item implements Vanishable {
	private final float attackDamage;
	private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;
	public static final float MINING_SPEED_MULTIPLIER = 1.5F;
	public static final float KNOCKBACK_RANGE = 3.5F;

	public Lance(int attackDamage, float attackSpeed, Settings settings) {
		super(settings);
		this.attackDamage = attackDamage;
		Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(
			EntityAttributes.GENERIC_ATTACK_DAMAGE,
			new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", (double)this.attackDamage, EntityAttributeModifier.Operation.ADDITION)
		);
		builder.put(
			EntityAttributes.GENERIC_ATTACK_SPEED,
			new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", (double)attackSpeed, EntityAttributeModifier.Operation.ADDITION)
		);
		this.attributeModifiers = builder.build();
	}

	public float getAttackDamage() {
		return this.attackDamage;
	}

	public int getEnchantability() {
		return 15;
	}

	public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		stack.damage(1, attacker, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
		
		if (shouldDealAdditionalDamage(attacker)) {
			attacker.onAttacking(target);
		}
	}

	public boolean canRepair(ItemStack stack, ItemStack ingredient) {
		return ingredient.isOf(Items.IRON_BARS);
	}

	public float getBonusAttackDamage(Entity target, float baseAttackDamage, DamageSource damageSource) {
		Entity var5 = damageSource.getSource();
		if (var5 instanceof LivingEntity livingEntity) {
			if (!shouldDealAdditionalDamage(livingEntity)) {
				return 0.0F;
			} else {
				float speed = livingEntity.horizontalSpeed;
				float totalDamage;

				if (speed <= 3.0F) {
					totalDamage = 4.0F * speed;
				} else if (speed <= 8.0F) {
					totalDamage = 12.0F + 2.0F * (speed - 3.0F);
				} else {
					totalDamage = 22.0F + speed - 8.0F;
				}
				
				return totalDamage;
			}
		} else {
			return 0.0F;
		}
	}

	public static boolean shouldDealAdditionalDamage(LivingEntity attacker) {
		return attacker.horizontalSpeed > 1.0F && !attacker.isCrawling();
	}

	@Override
	public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
		return !miner.isCreative();
	}

	@Override
	public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
		if (state.getHardness(world, pos) != 0.0F) {
			stack.damage(2, miner, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
		}

		return true;
	}

	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
		return slot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getAttributeModifiers(slot);
	}
}

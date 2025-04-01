package net.vodculen.artilleryandarmory.item.weapons;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;

import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Vanishable;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.vodculen.artilleryandarmory.effect.ModEffects;
import net.vodculen.artilleryandarmory.enchantment.ModEnchantmentHelper;
import net.vodculen.artilleryandarmory.item.ModItems;


public class Lance extends Item implements Vanishable {
	public static final int field_30926 = 10;
	public float attackDamage = 5.0F;
	public static final float field_30928 = 2.5F;
	private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

	public Lance(Item.Settings settings) {
		super(settings);
		Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(
			EntityAttributes.GENERIC_ATTACK_DAMAGE,
			new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Tool modifier", (double) attackDamage, EntityAttributeModifier.Operation.ADDITION)
		);
		builder.put(
			EntityAttributes.GENERIC_ATTACK_SPEED,
			new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Tool modifier", -2.9F, EntityAttributeModifier.Operation.ADDITION)
		);
		this.attributeModifiers = builder.build();
	}

	@Override
	public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
		return !miner.isCreative();
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.SPEAR;
	}

	@Override
	public int getMaxUseTime(ItemStack stack) {
		return 72000;
	}

	@Override
	public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
		if (user instanceof PlayerEntity playerEntity) {
			int i = this.getMaxUseTime(stack) - remainingUseTicks;
			if (i >= 10) {
				if (!world.isClient) {
                    if (user instanceof PlayerEntity player) {
                        player.addStatusEffect(new StatusEffectInstance(ModEffects.CHARGE, 100, 1, true, true));

                        stack.damage(1, playerEntity, p -> p.sendToolBreakStatus(user.getActiveHand()));
                        player.getItemCooldownManager().set(this, 300);
                    }
                    
				}

				playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
			}
		}
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		if (itemStack.getDamage() >= itemStack.getMaxDamage() - 1) {
			return TypedActionResult.fail(itemStack);
		} else if (EnchantmentHelper.getRiptide(itemStack) > 0 && !user.isTouchingWaterOrRain()) {
			return TypedActionResult.fail(itemStack);
		} else {
			user.setCurrentHand(hand);
			return TypedActionResult.consume(itemStack);
		}
	}

	@Override
	public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		stack.damage(1, attacker, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
		return true;
	}

	@Override
	public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
		if (state.getHardness(world, pos) != 0.0) {
			stack.damage(2, miner, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
		}

		return true;
	}

	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
		return slot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getAttributeModifiers(slot);
	}

	@Override
	public int getEnchantability() {
		return 1;
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		if (entity instanceof ServerPlayerEntity player) {
			if (player.getVehicle() instanceof HorseEntity horse && stack.isOf(ModItems.LANCE) && selected == true) {
				int level = ModEnchantmentHelper.getHorseBack(stack);
				if (level >= 1 && stack.isOf(ModItems.LANCE)) {
					player.addStatusEffect(new StatusEffectInstance(ModEffects.HORSE_CHARGE, 10, 1, true, true));
					horse.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 10, 1, false, false));
				}
			}
		}
		

		super.inventoryTick(stack, world, entity, slot, selected);
	}
}

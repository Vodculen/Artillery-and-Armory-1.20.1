package net.vodculen.artilleryandarmory.item.weapons;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;

import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.Vanishable;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.vodculen.artilleryandarmory.effect.ModEffects;
import net.vodculen.artilleryandarmory.enchantment.ModEnchantmentHelper;
import net.vodculen.artilleryandarmory.entity.custom.KunaiProjectileEntity;


public class Kunai extends Item implements Vanishable {
	public static final int field_30926 = 10;
	public static final float ATTACK_DAMAGE = 8.0F;
	public static final float field_30928 = 3.5F;
	private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

	public Kunai(Item.Settings settings) {
		super(settings);
		Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(
			EntityAttributes.GENERIC_ATTACK_DAMAGE,
			new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Tool modifier", 6.0, EntityAttributeModifier.Operation.ADDITION)
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
		return 37000;
	}

	@Override
	public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
		if (user instanceof PlayerEntity playerEntity) {
			int i = this.getMaxUseTime(stack) - remainingUseTicks;
			if (i >= 10) {
				if (!world.isClient) {
					stack.damage(1, playerEntity, p -> p.sendToolBreakStatus(user.getActiveHand()));
					int level = ModEnchantmentHelper.getInflict(stack);
					
					KunaiProjectileEntity kunaiEntity = new KunaiProjectileEntity(world, playerEntity, stack);
					kunaiEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 0.5F, 1.0F);


					if (level >= 1) {
						ItemStack itemStack = user.getOffHandStack();
						if (itemStack.isOf(Items.POTION)) {
							Potion potion = PotionUtil.getPotion(itemStack);

							if (potion == Potions.POISON) {
								kunaiEntity.takeEffect(StatusEffects.POISON);
							} else if (potion == Potions.SLOWNESS) {
								kunaiEntity.takeEffect(StatusEffects.SLOWNESS);
							} else if (potion == Potions.SLOWNESS) {
								kunaiEntity.takeEffect(StatusEffects.WEAKNESS);
							} else if (potion == Potions.WEAKNESS) {
								kunaiEntity.takeEffect(StatusEffects.WEAKNESS);
							} else {
								kunaiEntity.takeEffect(ModEffects.FESTERING);
							}

							if (!playerEntity.getAbilities().creativeMode) {
								playerEntity.getInventory().offHand.clear();
							}

							if (playerEntity.getOffHandStack().isEmpty()) {
								playerEntity.getInventory().insertStack(new ItemStack(Items.GLASS_BOTTLE));
							}
						}
					}

					if (playerEntity.getAbilities().creativeMode) {
						kunaiEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
					}

					world.spawnEntity(kunaiEntity);
					world.playSoundFromEntity(null, kunaiEntity, SoundEvents.ITEM_TRIDENT_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F);
					if (!playerEntity.getAbilities().creativeMode) {
						playerEntity.getInventory().removeOne(stack);
					}

					playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
				}
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

	public static boolean hasPoisonPotion(PlayerEntity playerEntity) {
		ItemStack offHand = playerEntity.getOffHandStack();

		if (offHand.getItem() == Items.POTION) {
			Potion potion = PotionUtil.getPotion(offHand);

			return potion == Potions.POISON;
		}

		return false;
	}
}

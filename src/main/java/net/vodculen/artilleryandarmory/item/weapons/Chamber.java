package net.vodculen.artilleryandarmory.item.weapons;

import java.util.function.Predicate;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.Vanishable;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.World.ExplosionSourceType;
import net.vodculen.artilleryandarmory.effect.ModEffects;
import net.vodculen.artilleryandarmory.enchantment.ModEnchantmentHelper;
import net.vodculen.artilleryandarmory.util.WeaponUtils;


public class Chamber extends ExplodingWeaponItem implements Vanishable {
	private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;
	public static final int TICKS_PER_SECOND = 20;
	public static final int RANGE = 15;

	public Chamber(Settings settings) {
		super(settings);
		Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(
			EntityAttributes.GENERIC_ATTACK_DAMAGE,
			new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", 15.0, EntityAttributeModifier.Operation.ADDITION)
		);
		builder.put(
			EntityAttributes.GENERIC_ATTACK_SPEED,
			new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", -3.93, EntityAttributeModifier.Operation.ADDITION)
		);
		this.attributeModifiers = builder.build();
	}

	@Override
	public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
		if (user instanceof PlayerEntity playerEntity) {
			stack.damage(1, playerEntity, p -> p.sendToolBreakStatus(user.getActiveHand()));

			boolean bl = playerEntity.getAbilities().creativeMode;
			ItemStack itemStack = WeaponUtils.getProjectileTypeForWeapon(stack, playerEntity);
			int level = ModEnchantmentHelper.getExplosives(stack);

			if (level < 1) {
				if (!itemStack.isEmpty() || bl) {
					if (itemStack.isEmpty()) {
						itemStack = new ItemStack(Items.GUNPOWDER);
					}

					int i = this.getMaxUseTime(stack) - remainingUseTicks;
					

					
					if (i >= 10) {
						boolean hasAmount = itemStack.getCount() >= 3;
						boolean bl2 = bl && itemStack.isOf(Items.GUNPOWDER) && hasAmount;
						
		
						if (!world.isClient && hasAmount) {
							world.createExplosion(user, user.getX(), user.getY(), user.getZ(), 0.5F, false, ExplosionSourceType.NONE);
		
							((PlayerEntity) user).velocityModified = true;
									
							user.addVelocity(0.0, 1.5, 0.0);
									
							((PlayerEntity) user).getItemCooldownManager().set(this, 60);
						}
		
							
						if (!bl2 && !playerEntity.getAbilities().creativeMode) {
							itemStack.decrement(3);
								
							if (itemStack.isEmpty()) {
								playerEntity.getInventory().removeOne(itemStack);
							}
						}
					}
				}					

				playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
			}
		}
	}

	@Override
	public int getMaxUseTime(ItemStack stack) {
		return 36000;
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BOW;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		ItemStack projectile = WeaponUtils.getProjectileTypeForWeapon(itemStack, user);

		boolean isValidProjectile = !projectile.isEmpty();

		if (!user.getAbilities().creativeMode && !isValidProjectile) {
			return TypedActionResult.fail(itemStack);
		} else {
			user.setCurrentHand(hand);
			return TypedActionResult.consume(itemStack);
		}
	}

	@Override
	public Predicate<ItemStack> getExplosives() {
		return EXPLODING_PROJECTILES;
	}

	@Override
	public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		World world = attacker.getWorld();	

		if (!world.isClient) {
			if (attacker.fallDistance > 0.0F) {
				target.addStatusEffect(new StatusEffectInstance(ModEffects.DAZED, 10, 1, true, true));

				if (attacker instanceof PlayerEntity playerEntity) {
					boolean bl = playerEntity.getAbilities().creativeMode;
					ItemStack itemStack = WeaponUtils.getProjectileTypeForWeapon(stack, playerEntity);
					int level = ModEnchantmentHelper.getExplosives(stack);

					if (level >= 1) {
						if (!itemStack.isEmpty() || bl) {
							if (itemStack.isEmpty()) {
								itemStack = new ItemStack(Items.GUNPOWDER);
							}
		
							
		
							
							boolean bl2 = bl && itemStack.isOf(Items.GUNPOWDER);
			
							if (!world.isClient) {
								world.createExplosion(playerEntity, target.getX(), target.getY(), target.getZ(), 0.5F, false, ExplosionSourceType.NONE);
							}
				
									
							if (!bl2 && !playerEntity.getAbilities().creativeMode) {
								itemStack.decrement(3);
										
								if (itemStack.isEmpty()) {
									playerEntity.getInventory().removeOne(itemStack);
								}
							}
						}					
					}
					
					playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
				}
			}
		}

		stack.damage(1, attacker, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
		
		return true;
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

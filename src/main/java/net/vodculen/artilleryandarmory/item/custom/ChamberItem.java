package net.vodculen.artilleryandarmory.item.custom;

import java.util.function.Predicate;

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
import net.vodculen.artilleryandarmory.effect.ModStatusEffects;
import net.vodculen.artilleryandarmory.enchantment.ModEnchantmentHelper;
import net.vodculen.artilleryandarmory.util.WeaponUtils;


public class ChamberItem extends ExplodingWeaponItem implements Vanishable {
	private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;
	private boolean canExplode = false;
	public static final int TICKS_PER_SECOND = 20;
	public static final int RANGE = 15;

	public ChamberItem(Settings settings) {
		super(settings);

		Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", 9.0, EntityAttributeModifier.Operation.ADDITION));
		builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", -3.4, EntityAttributeModifier.Operation.ADDITION));
		this.attributeModifiers = builder.build();
	}

	@Override
	public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
		if (!world.isClient && user instanceof PlayerEntity player) {
			stack.damage(1, player, p -> p.sendToolBreakStatus(user.getActiveHand()));

			ItemStack itemStack = WeaponUtils.getProjectileTypeForWeapon(stack, player);
			boolean hasAmount = itemStack.getCount() >= 3;
			boolean inCreativeMode = player.getAbilities().creativeMode;
			boolean canUseAbility = inCreativeMode && itemStack.isOf(Items.GUNPOWDER) && hasAmount;
			int level = ModEnchantmentHelper.getExplosives(stack);
			int timeUsed = this.getMaxUseTime(stack) - remainingUseTicks;

			if (level < 1 && (!itemStack.isEmpty() || inCreativeMode)) {
				if (itemStack.isEmpty()) {
					itemStack = new ItemStack(Items.GUNPOWDER);
				}

				if (timeUsed >= 10) {
					if (hasAmount || inCreativeMode) {
						world.createExplosion(user, user.getX(), user.getY(), user.getZ(), 0.5F, false, ExplosionSourceType.NONE);
	
						player.velocityModified = true;
						player.addVelocity(0.0, 1.5, 0.0);
						player.getItemCooldownManager().set(this, 60);
					}
	
						
					if (!canUseAbility) {
						itemStack.decrement(3);
							
						if (itemStack.isEmpty()) {
							player.getInventory().removeOne(itemStack);
						}
					}
				}					

				player.incrementStat(Stats.USED.getOrCreateStat(this));
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

		if (!world.isClient && attacker instanceof PlayerEntity player && attacker.fallDistance > 0.0F && canExplode) {
			target.addStatusEffect(new StatusEffectInstance(ModStatusEffects.DAZED, 10, 1, true, true));

			ItemStack itemStack = WeaponUtils.getProjectileTypeForWeapon(stack, player);
			boolean inCreativeMode = player.getAbilities().creativeMode;
			boolean canUseAbility = inCreativeMode && itemStack.isOf(Items.GUNPOWDER);
			int level = ModEnchantmentHelper.getExplosives(stack);

			if (level >= 1 && (!itemStack.isEmpty() || inCreativeMode)) {
				if (itemStack.isEmpty()) {
					itemStack = new ItemStack(Items.GUNPOWDER);
				}

				world.createExplosion(player, target.getX(), target.getY(), target.getZ(), 0.5F, false, ExplosionSourceType.NONE);
		
				if (!canUseAbility) {
					itemStack.decrement(3);
							
					if (itemStack.isEmpty()) {
						player.getInventory().removeOne(itemStack);
					}
				}			
			}

			player.incrementStat(Stats.USED.getOrCreateStat(this));
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

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		if (entity instanceof PlayerEntity player) {
			if (player.getAttackCooldownProgress(1.0F) > 0.9F) {
				canExplode = true;
			} else {
				canExplode = false;
			}
		}
		
		super.inventoryTick(stack, world, entity, slot, selected);
	}
}

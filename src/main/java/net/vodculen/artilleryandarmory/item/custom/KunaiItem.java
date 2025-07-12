package net.vodculen.artilleryandarmory.item.custom;

import java.util.ArrayList;
import java.util.List;

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
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.Vanishable;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.vodculen.artilleryandarmory.effect.ModStatusEffects;
import net.vodculen.artilleryandarmory.enchantment.ModEnchantmentHelper;
import net.vodculen.artilleryandarmory.entity.custom.KunaiProjectileEntity;


public class KunaiItem extends Item implements Vanishable {
	public static final float ATTACK_DAMAGE = 8.0F;
	private static List<StatusEffectInstance> effect = new ArrayList<StatusEffectInstance>();
	private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

	public KunaiItem(Settings settings) {
		super(settings);

		Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Tool modifier", 6.0, EntityAttributeModifier.Operation.ADDITION));
		builder.put(EntityAttributes.GENERIC_ATTACK_SPEED,new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Tool modifier", -2.9F, EntityAttributeModifier.Operation.ADDITION));
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

	private List<StatusEffectInstance> addFestering() {
		effect.add(new StatusEffectInstance(ModStatusEffects.FESTERING, 200, 0, true, true));
		List<StatusEffectInstance> festering = effect;

		return festering;
	}

	@Override
	public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
		if (!world.isClient && user instanceof PlayerEntity playerEntity) {
			ItemStack itemStack = user.getOffHandStack();
			Potion potion = PotionUtil.getPotion(itemStack);
			int timeUsed = this.getMaxUseTime(stack) - remainingUseTicks;
			int level = ModEnchantmentHelper.getInflict(stack);

			if (timeUsed >= 10) {
				KunaiProjectileEntity kunaiEntity = new KunaiProjectileEntity(world, playerEntity, stack);

				stack.damage(1, playerEntity, p -> p.sendToolBreakStatus(user.getActiveHand()));
				
				kunaiEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 1.0F, 1.0F);

				if (level >= 1 && itemStack.isOf(Items.POTION)) {
					kunaiEntity.takeEffect(potion.getEffects());

					if (!playerEntity.getAbilities().creativeMode) {
						playerEntity.getInventory().offHand.clear();
					}

					if (playerEntity.getOffHandStack().isEmpty()) {
						playerEntity.getInventory().insertStack(new ItemStack(Items.GLASS_BOTTLE));
					}
				} else {
					kunaiEntity.takeEffect(addFestering());
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

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);

		if (itemStack.getDamage() >= itemStack.getMaxDamage() - 1) {
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
}

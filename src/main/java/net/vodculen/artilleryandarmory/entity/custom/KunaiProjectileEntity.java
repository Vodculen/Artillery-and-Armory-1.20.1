package net.vodculen.artilleryandarmory.entity.custom;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.vodculen.artilleryandarmory.damageType.ModDamageTypes;
import net.vodculen.artilleryandarmory.entity.ModEntities;
import net.vodculen.artilleryandarmory.item.ModItems;


public class KunaiProjectileEntity extends PersistentProjectileEntity {
	private static final TrackedData<Boolean> ENCHANTED = DataTracker.registerData(KunaiProjectileEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	private ItemStack kunaiStack = new ItemStack(ModItems.KUNAI);
	private boolean dealtDamage;
	public int returnTimer;
	private List<StatusEffectInstance> applyEffects = new ArrayList<>();

	public KunaiProjectileEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
		super(entityType, world);
	}

	public KunaiProjectileEntity(World world, LivingEntity owner, ItemStack stack) {
		super(ModEntities.KUNAI, owner, world);
		this.kunaiStack = stack.copy();
		this.dataTracker.set(ENCHANTED, stack.hasGlint());
	}


	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(ENCHANTED, false);
	}

	@Override
	public void tick() {
		if (this.inGroundTime > 1) {
			this.dealtDamage = true;
		}

		Entity entity = this.getOwner();
		if ((this.dealtDamage || this.isNoClip()) && entity != null) {
			if (!this.isOwnerAlive()) {
				if (!this.getWorld().isClient && this.pickupType == PersistentProjectileEntity.PickupPermission.ALLOWED) {
					this.dropStack(this.asItemStack(), 0.1F);
				}

				this.discard();
			} else {
				this.setNoClip(true);
				Vec3d vec3d = entity.getEyePos().subtract(this.getPos());
				this.setPos(this.getX(), this.getY() + vec3d.y * 0.015 * 3, this.getZ());
				if (this.getWorld().isClient) {
					this.lastRenderY = this.getY();
				}

				double multiplier = 0.1 * 3;
				this.setVelocity(this.getVelocity().multiply(0.95).add(vec3d.normalize().multiply(multiplier)));
				if (this.returnTimer == 0) {
					this.playSound(SoundEvents.ITEM_TRIDENT_RETURN, 10.0F, 1.0F);
				}

				this.returnTimer++;
			}
		}

		super.tick();
	}

	private boolean isOwnerAlive() {
		Entity entity = this.getOwner();
		return entity == null || !entity.isAlive() ? false : !(entity instanceof ServerPlayerEntity) || !entity.isSpectator();
	}

	@Override
	protected ItemStack asItemStack() {
		return this.kunaiStack.copy();
	}

	public boolean isEnchanted() {
		return this.dataTracker.get(ENCHANTED);
	}

	@Nullable
	@Override
	protected EntityHitResult getEntityCollision(Vec3d currentPosition, Vec3d nextPosition) {
		return this.dealtDamage ? null : super.getEntityCollision(currentPosition, nextPosition);
	}

	@Override
	protected void onEntityHit(EntityHitResult entityHitResult) {
		Entity entity = entityHitResult.getEntity();
		World world = entity.getWorld();
		float damage = 3.0F;

		if (entity instanceof LivingEntity livingEntity) {
			damage += EnchantmentHelper.getAttackDamage(this.kunaiStack, livingEntity.getGroup());
			if (applyEffects != null) {
				for (StatusEffectInstance effect : applyEffects) {
					livingEntity.addStatusEffect(new StatusEffectInstance(effect.getEffectType(), 200, effect.getAmplifier(), true, true));
				}		
			}	
		}

		Entity entity2 = this.getOwner();
		DamageSource damageSource = new DamageSource(
		world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(ModDamageTypes.KUNAIED));
		this.dealtDamage = true;
		SoundEvent soundEvent = SoundEvents.ITEM_TRIDENT_HIT;
		if (entity.damage(damageSource, damage)) {
			if (entity.getType() == EntityType.ENDERMAN) {
				return;
			}

			if (entity instanceof LivingEntity livingEntity2) {
				if (entity2 instanceof LivingEntity livingEntity) {
					EnchantmentHelper.onUserDamaged(livingEntity2, entity2);
					EnchantmentHelper.onTargetDamaged(livingEntity, livingEntity2);
				}

				this.onHit(livingEntity2);
			}
		}

		this.setVelocity(this.getVelocity().multiply(-0.01, -0.1, -0.01));
		float volume = 1.0F;

		this.playSound(soundEvent, volume, 1.0F);
	}

	@Override
	protected boolean tryPickup(PlayerEntity player) {
		return super.tryPickup(player) || this.isNoClip() && this.isOwner(player) && player.getInventory().insertStack(this.asItemStack());
	}

	@Override
	protected SoundEvent getHitSound() {
		return SoundEvents.ITEM_TRIDENT_HIT_GROUND;
	}

	@Override
	public void onPlayerCollision(PlayerEntity player) {
		if (this.isOwner(player) || this.getOwner() == null) {
			super.onPlayerCollision(player);
		}
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		if (nbt.contains("Kunai", NbtElement.COMPOUND_TYPE)) {
			this.kunaiStack = ItemStack.fromNbt(nbt.getCompound("Kunai"));
		}

		this.dealtDamage = nbt.getBoolean("DealtDamage");
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.put("Kunai", this.kunaiStack.writeNbt(new NbtCompound()));
		nbt.putBoolean("DealtDamage", this.dealtDamage);
	}

	@Override
	public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
		return true;
	}

	public List<StatusEffectInstance> takeEffect(List<StatusEffectInstance> effects) {
		return applyEffects = effects;
	}
}

package net.vodculen.artilleryandarmory.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.vodculen.artilleryandarmory.util.ChargeableEntity;

@Mixin(LivingEntity.class)
public abstract class PlayerEntityMixin implements ChargeableEntity {
	@Unique
	private int chargeTicks = 0;

	@Inject(method = "tick", at = @At("HEAD"))
	private void chargingTicks(CallbackInfo callbackInfo) {
		if (chargeTicks > 0) {
			chargeTicks--;

			if ((Object) this instanceof PlayerEntity player) {
				chargeInto(player.getWorld(), player);
			}
		}
	}

	public boolean isCharging() {
		return chargeTicks > 0;
	}

	public void charge(int ticks) {
		this.chargeTicks = ticks;
	}

	public void chargeInto(World world, PlayerEntity charger) {
		Vec3d start = charger.getPos();
		Vec3d forward = charger.getRotationVec(1.0F);
		Vec3d end = start.add(forward.multiply(1.5));
		Box box = new Box(start, end).expand(1.0);
		
		List<Entity> hitEntities = world.getOtherEntities(charger, box, e -> e instanceof LivingEntity && e != charger);
		for (Entity entity : hitEntities) {
			if (entity instanceof LivingEntity target) {
				target.damage(world.getDamageSources().playerAttack(charger), 6.0F);
			}
		}
	}

	public void chargeVelocity(PlayerEntity charger, float speed) {
		float yaw = charger.getYaw();
		float pitch = charger.getPitch();
		float x = -MathHelper.sin(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
		float y = -MathHelper.sin(pitch * 0.017453292F);
		float z = MathHelper.cos(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
		float distance = MathHelper.sqrt((x * x) + (y * y) + (z * z));

		x *= speed / distance;
		y *= speed / distance * (1 / 2);
		z *= speed / distance;

		charger.velocityModified = true;
		charger.addVelocity(x, y, z);
	}
}

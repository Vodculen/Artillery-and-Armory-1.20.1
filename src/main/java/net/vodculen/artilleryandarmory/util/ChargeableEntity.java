package net.vodculen.artilleryandarmory.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public interface ChargeableEntity {
	public boolean isCharging();
	public void charge(int ticks);
	public void chargeInto(World world, PlayerEntity charger);
	public void chargeVelocity(PlayerEntity charger, float speed);
}

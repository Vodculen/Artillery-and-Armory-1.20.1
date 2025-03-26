package net.vodculen.artilleryandarmory.item.weapons;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;

public class Hammer extends AxeItem {
    public Hammer(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    } 

	@Override
	public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		System.out.println("This was called!");

		int stunTick = 0;
		boolean inStun = false;

		if (!attacker.getWorld().isClient) {
			if (!inStun) {
				target.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.0);
				stunTick = 10;
				inStun = true;
			} else if (inStun) {
				target.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).getBaseValue();

			}

			while (stunTick > 0) {
				--stunTick;
			}

			if (stunTick == 0) {
				inStun = false;
			}
		}		
		
		return super.postHit(stack, target, attacker);
	}
}

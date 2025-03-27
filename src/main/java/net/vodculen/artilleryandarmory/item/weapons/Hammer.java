package net.vodculen.artilleryandarmory.item.weapons;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.world.World;

public class Hammer extends AxeItem {
    public Hammer(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    } 

	@Override
	public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		World world = attacker.getWorld();
		
		if (!world.isClient) {
			if (attacker.fallDistance > 0.0F) {
				target.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.0);
			}
		}		
		
		return super.postHit(stack, target, attacker);
	}
}

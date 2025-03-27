package net.vodculen.artilleryandarmory.item.weapons;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import net.vodculen.artilleryandarmory.effect.ModEffects;

public class Hammer extends AxeItem {
    public Hammer(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    } 

	@Override
	public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		World world = attacker.getWorld();	
		if (!world.isClient) {
			if (attacker.fallDistance > 0.0F) {
				target.addStatusEffect(new StatusEffectInstance(ModEffects.DAZED_EFFECT, 10, 1, true, true));
			}
		}
		
		return super.postHit(stack, target, attacker);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		// TODO Auto-generated method stub
		return super.useOnBlock(context);
	}
	
}

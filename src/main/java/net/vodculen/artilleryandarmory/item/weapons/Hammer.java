package net.vodculen.artilleryandarmory.item.weapons;

import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;

public class Hammer extends SwordItem {
    public Hammer(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
            super(toolMaterial, attackDamage, attackSpeed, settings);
        }
    
    // @Override
    // public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
    //     World world = user.getWorld();

    //     if (!world.isClient) {
    //         entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 1000, 254, false, false));
    //     }        

    //     return super.useOnEntity(stack, user, entity, hand);
    // }

}

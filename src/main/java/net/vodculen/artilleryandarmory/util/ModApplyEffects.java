package net.vodculen.artilleryandarmory.util;

import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.vodculen.artilleryandarmory.item.ModItems;

public class ModApplyEffects {
     public static void applyStunEffect() {
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (!world.isClient && entity instanceof LivingEntity target) {
                ItemStack heldItem = player.getStackInHand(hand);

                if (heldItem.getItem() == ModItems.HAMMER) {
                    target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20, 254, true, true));
                }
            }

            return ActionResult.PASS;
        });
    }   
}

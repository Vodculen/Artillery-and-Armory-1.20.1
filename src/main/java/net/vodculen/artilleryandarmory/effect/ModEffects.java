package net.vodculen.artilleryandarmory.effect;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.vodculen.artilleryandarmory.ArtilleryArmory;


public class ModEffects {
    public static final StatusEffect DAZED_EFFECT = registerEffect("dazed", new DazedEffect().addAttributeModifier(
		EntityAttributes.GENERIC_MOVEMENT_SPEED, "91AEAA56-376B-4498-935B-2F7F68070635", -10000.0F, EntityAttributeModifier.Operation.MULTIPLY_BASE
	));
    
    private static StatusEffect registerEffect(String name, StatusEffect statusEffect) {
        return Registry.register(Registries.STATUS_EFFECT, Identifier.of(ArtilleryArmory.MOD_ID, name), statusEffect);
    }

    public static void registerModEffects() {
        ArtilleryArmory.LOGGER.info("Registering Mod Effects for " + ArtilleryArmory.MOD_ID);
    }
}

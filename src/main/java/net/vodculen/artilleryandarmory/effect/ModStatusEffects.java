package net.vodculen.artilleryandarmory.effect;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.entity.effect.StatusEffect;
import net.vodculen.artilleryandarmory.ArtilleryArmory;
import net.vodculen.artilleryandarmory.effect.custom.ChargeEffect;
import net.vodculen.artilleryandarmory.effect.custom.DazedEffect;
import net.vodculen.artilleryandarmory.effect.custom.FesteringEffect;


public class ModStatusEffects {
    public static final StatusEffect DAZED = registerEffect("dazed", new DazedEffect());
    public static final StatusEffect CHARGE = registerEffect("charge", new ChargeEffect());
    public static final StatusEffect FESTERING = registerEffect("festering", new FesteringEffect());
    
    // Below are helper classes that make defining Items easier as well as making them accessible to the entry class
    private static StatusEffect registerEffect(String name, StatusEffect statusEffect) {
        return Registry.register(Registries.STATUS_EFFECT, Identifier.of(ArtilleryArmory.MOD_ID, name), statusEffect);
    }

    public static void registerModEffects() {
        ArtilleryArmory.LOGGER.info("Registering Modded Status Effects");
    }
}

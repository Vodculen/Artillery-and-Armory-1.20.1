package net.vodculen.artilleryandarmory.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class DazedEffect extends StatusEffect {
    protected DazedEffect() {
        super(StatusEffectCategory.HARMFUL, 0xbbcd39);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) { // "91AEAA56-376B-4498-935B-2F7F68070635"

        super.applyUpdateEffect(entity, amplifier);
    }

    

}

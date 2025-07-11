package net.vodculen.artilleryandarmory.effect.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.World;
import net.vodculen.artilleryandarmory.damageType.ModDamageTypes;

public class FesteringEffect extends StatusEffect {
	public FesteringEffect() {
		super(StatusEffectCategory.HARMFUL, 0xb50734);
	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return duration % 10 == 0;
	}

	@Override
	// This allows less logic in the Kunai class and I believe it makes sense detecting it here
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
		World world = entity.getWorld();

		if (!world.isClient) {
			DamageSource damageSource = new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(ModDamageTypes.FESTERING));
			
			entity.damage(damageSource, 0.5F);
		}
		
		super.applyUpdateEffect(entity, amplifier);
	}
}

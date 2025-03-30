package net.vodculen.artilleryandarmory.damageType;

import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModDamageTypes {
	public static final RegistryKey<DamageType> FESTERING = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier("artilleryandarmory", "festering"));
	public static final RegistryKey<DamageType> KUNAIED = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier("artilleryandarmory", "kunaied"));
	public static final RegistryKey<DamageType> HAMMERED = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier("artilleryandarmory", "hammered"));

}

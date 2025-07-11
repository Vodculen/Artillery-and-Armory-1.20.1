package net.vodculen.artilleryandarmory.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.vodculen.artilleryandarmory.ArtilleryArmory;

public class ModEnchantments {
	public static final Enchantment HORSE_BACK = registerEnchantment("horse_back", new HorseBackEnchantment());
	public static final Enchantment EXPLOSIVES = registerEnchantment("explosives", new ExplosivesEnchantment());
	public static final Enchantment INFLICT = registerEnchantment("inflict", new InflictEnchantment());


	// Below are helper classes that make defining Items easier as well as making them accessible to the entry class
    private static Enchantment registerEnchantment(String name, Enchantment enchantment) {
		return Registry.register(Registries.ENCHANTMENT, Identifier.of(ArtilleryArmory.MOD_ID, name), enchantment);
	}

	public static void registerModEnchantments() {
		ArtilleryArmory.LOGGER.info("Registering Modded Enchantments");
	}
}

package net.vodculen.artilleryandarmory;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.vodculen.artilleryandarmory.entity.ModEntities;
import net.vodculen.artilleryandarmory.entity.client.KunaiProjectileModel;
import net.vodculen.artilleryandarmory.entity.client.KunaiProjectileRenderer;

public class ArtilleryArmoryClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		EntityModelLayerRegistry.registerModelLayer(KunaiProjectileModel.KUNAI, KunaiProjectileModel::getTexturedModelData);
		EntityRendererRegistry.register(ModEntities.KUNAI, KunaiProjectileRenderer::new);
	}
	
}

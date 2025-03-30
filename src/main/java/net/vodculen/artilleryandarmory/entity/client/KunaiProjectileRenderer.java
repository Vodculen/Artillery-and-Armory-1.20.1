package net.vodculen.artilleryandarmory.entity.client;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.vodculen.artilleryandarmory.ArtilleryArmory;
import net.vodculen.artilleryandarmory.entity.custom.KunaiProjectileEntity;

public class KunaiProjectileRenderer extends EntityRenderer<KunaiProjectileEntity> {
    protected KunaiProjectileModel model;

    public KunaiProjectileRenderer(Context ctx) {
        super(ctx);
        this.model = new KunaiProjectileModel(ctx.getPart(KunaiProjectileModel.KUNAI));
    }

    @Override
    public void render(KunaiProjectileEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();

        VertexConsumer vertexconsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutout(Identifier.of(ArtilleryArmory.MOD_ID, "textures/entity/kunai/kunai.png")));
        this.model.render(matrices, vertexconsumer, light, OverlayTexture.DEFAULT_UV, light, light, light, light);

        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(KunaiProjectileEntity entity) {
        return Identifier.of(ArtilleryArmory.MOD_ID, "textures/entity/kunai/kunai.png");
    }

}

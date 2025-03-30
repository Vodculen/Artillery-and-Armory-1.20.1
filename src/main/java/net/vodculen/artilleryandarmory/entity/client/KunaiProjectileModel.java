package net.vodculen.artilleryandarmory.entity.client;

import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.vodculen.artilleryandarmory.ArtilleryArmory;
import net.vodculen.artilleryandarmory.entity.custom.KunaiProjectileEntity;

public class KunaiProjectileModel extends EntityModel<KunaiProjectileEntity> {
    public static final EntityModelLayer KUNAI = new EntityModelLayer(Identifier.of(ArtilleryArmory.MOD_ID, "kunai"), "main");
    private final ModelPart kunai;

	public KunaiProjectileModel(ModelPart root) {
		this.kunai = root.getChild("kunai");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData kunai = modelPartData.addChild("kunai", ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -16.0F, -1.0F, 2.0F, 16.0F, 2.0F, new Dilation(0.0F))
		.uv(8, 0).cuboid(-1.0F, -16.0F, -6.0F, 2.0F, 5.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		return TexturedModelData.of(modelData, 32, 32);
	}

	@Override
	public void setAngles(KunaiProjectileEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		kunai.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
}

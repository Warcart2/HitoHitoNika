package net.mcreator.mineminenomitest;

import xyz.pixelatedw.mineminenomi.api.morph.MorphModel;

import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.HandSide;
import net.minecraft.entity.LivingEntity;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.model.ModelRenderer;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.matrix.MatrixStack;

public class GomuGigantFusenModel extends MorphModel {
	private final ModelRenderer Head;
	private final ModelRenderer Body;
	private final ModelRenderer RightArm;
	private final ModelRenderer LeftArm;
	private final ModelRenderer RightLeg;
	private final ModelRenderer LeftLeg;

	public GomuGigantFusenModel() {
		super(0);
		textureWidth = 64;
		textureHeight = 64;
		Head = new ModelRenderer(this);
		Head.setRotationPoint(0.0F, -48.0F, 0.0F);
		Head.setTextureOffset(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
		Body = new ModelRenderer(this);
		Body.setRotationPoint(0.0F, 0.0F, 0.0F);
		Body.setTextureOffset(16, 16).addBox(-4.0F, -24.0F, -2.0F, 8.0F, 12.0F, 4.0F, 24.0F, false);
		RightArm = new ModelRenderer(this);
		RightArm.setRotationPoint(-5.0F, -38.0F, 0.0F);
		RightArm.setTextureOffset(40, 16).addBox(-27.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
		LeftArm = new ModelRenderer(this);
		LeftArm.setRotationPoint(5.0F, -38.0F, 0.0F);
		LeftArm.setTextureOffset(32, 48).addBox(23.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
		RightLeg = new ModelRenderer(this);
		RightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);
		RightLeg.setTextureOffset(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
		LeftLeg = new ModelRenderer(this);
		LeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
		LeftLeg.setTextureOffset(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Head.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		Body.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		RightArm.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		LeftArm.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		RightLeg.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		LeftLeg.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void renderFirstPersonArm(MatrixStack matrixStack, IVertexBuilder vertex, int packedLight, int overlay, float red, float green, float blue, float alpha, HandSide side) {
		if (side == HandSide.RIGHT) {
			matrixStack.translate(0.2D, 0.3D, 0.0D);
			this.RightArm.render(matrixStack, vertex, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 0.7F, 0.0F, 1.0F);
		} else {
			matrixStack.translate(-0.2D, 0.3D, 0.0D);
			this.LeftArm.render(matrixStack, vertex, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 0.7F, 0.0F, 1.0F);
		}
	}

	public void setRotationAngles(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.RightArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * limbSwingAmount;
		this.LeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 1.0F) * -1.0F * limbSwingAmount;
		this.Head.rotateAngleY = netHeadYaw / (180F / (float) Math.PI);
		this.Head.rotateAngleX = headPitch / (180F / (float) Math.PI);
		this.LeftArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * limbSwingAmount;
		this.RightLeg.rotateAngleX = MathHelper.cos(limbSwing * 1.0F) * 1.0F * limbSwingAmount;
	}

	public void renderFirstPersonLeg(MatrixStack matrixStack, IVertexBuilder vertex, int packedLight, int overlay, float red, float green, float blue, float alpha, HandSide side) {
		if (side == HandSide.RIGHT) {
			matrixStack.translate(0.0D, -1.2D, 0.3D);
			matrixStack.scale(1.5F, 1.5F, 1.5F);
			matrixStack.rotate(Vector3f.YP.rotationDegrees(-60.0F));
			this.RightLeg.render(matrixStack, vertex, packedLight, overlay, red, green, blue, alpha);
		} else {
			matrixStack.translate(0.0D, -1.2D, 0.3D);
			matrixStack.scale(1.5F, 1.5F, 1.5F);
			matrixStack.rotate(Vector3f.YP.rotationDegrees(60.0F));
			this.LeftLeg.render(matrixStack, vertex, packedLight, overlay, red, green, blue, alpha);
		}
	}
}

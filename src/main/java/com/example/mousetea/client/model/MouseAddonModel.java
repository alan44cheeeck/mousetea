package com.example.mousetea.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;

/**
 * Отдельная (не связанная с ванильным PlayerModel) процедурная модель для частей мыши,
 * которые "прорастают" по стадиям поверх игрока: уши, хвост, лапы-варежки, усы.
 * Части заданы как кубы, текстура — assets/mousetea/textures/entity/mouse_parts.png (64x32).
 */
public class MouseAddonModel {

    private final ModelPart leftEar;
    private final ModelPart rightEar;
    private final ModelPart tail;
    private final ModelPart whiskers;

    public MouseAddonModel(ModelPart root) {
        this.leftEar = root.getChild("left_ear");
        this.rightEar = root.getChild("right_ear");
        this.tail = root.getChild("tail");
        this.whiskers = root.getChild("whiskers");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // Уши — два кубика-полукруга (упрощённо, кубами), крепятся на голове.
        root.addOrReplaceChild("left_ear",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -3.0F, -1.0F, 3.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(-3.0F, -8.0F, 0.0F, 0.0F, 0.0F, -0.25F));

        root.addOrReplaceChild("right_ear",
                CubeListBuilder.create().texOffs(8, 0).addBox(-2.0F, -3.0F, -1.0F, 3.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(3.0F, -8.0F, 0.0F, 0.0F, 0.0F, 0.25F));

        // Хвост — цепочка из тонких кубов, растущая из спины.
        root.addOrReplaceChild("tail",
                CubeListBuilder.create().texOffs(0, 8).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 10.0F),
                PartPose.offsetAndRotation(0.0F, 2.0F, 3.0F, 0.35F, 0.0F, 0.0F));

        // Усы — тонкие плоские "иглы" по бокам морды.
        root.addOrReplaceChild("whiskers",
                CubeListBuilder.create().texOffs(0, 20).addBox(-4.0F, -0.5F, -0.5F, 8.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, -6.0F, -4.0F));

        return LayerDefinition.create(mesh, 64, 32);
    }

    /**
     * @param stage 0..4, определяет, какие части видимы.
     */
    public void setStage(int stage) {
        leftEar.visible = stage >= 1;
        rightEar.visible = stage >= 1;
        tail.visible = stage >= 2;
        whiskers.visible = stage >= 3;
    }

    /** Части, крепящиеся к голове (вызывать после translateAndRotate(head)). */
    public void renderHeadParts(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay) {
        leftEar.render(poseStack, buffer, packedLight, packedOverlay);
        rightEar.render(poseStack, buffer, packedLight, packedOverlay);
        whiskers.render(poseStack, buffer, packedLight, packedOverlay);
    }

    /** Хвост крепится к телу (вызывать после translateAndRotate(body)). */
    public void renderTail(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay) {
        tail.render(poseStack, buffer, packedLight, packedOverlay);
    }
}

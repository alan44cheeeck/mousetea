package com.example.mousetea.client.renderer;

import com.example.mousetea.MouseTeaMod;
import com.example.mousetea.capability.MouseFormData;
import com.example.mousetea.capability.MouseFormProvider;
import com.example.mousetea.client.model.MouseAddonModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

/**
 * Слой рендера, который Forge вызывает каждый кадр для каждого игрока-модели.
 * Здесь мы читаем стадию превращения из капабилити игрока и рисуем поверх него
 * соответствующие мышиные части (уши/хвост/усы) из MouseAddonModel.
 */
public class MouseFormLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(MouseTeaMod.MOD_ID, "textures/entity/mouse_parts.png");

    private final MouseAddonModel addonModel;

    public MouseFormLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent,
                           EntityModelSet modelSet) {
        super(parent);
        ModelPart root = modelSet.bakeLayer(MouseModelLayers.MOUSE_ADDON);
        this.addonModel = new MouseAddonModel(root);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                        AbstractClientPlayer player, float limbSwing, float limbSwingAmount,
                        float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

        int stage = player.getCapability(MouseFormProvider.MOUSE_FORM)
                .map(MouseFormData::getStage)
                .orElse(0);
        if (stage <= 0) return;

        addonModel.setStage(stage);
        var buf = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));

        // Уши и усы крепятся к голове — двигаются и поворачиваются вместе с ней.
        poseStack.pushPose();
        this.getParentModel().getHead().translateAndRotate(poseStack);
        addonModel.renderHeadParts(poseStack, buf, packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();

        // Хвост крепится к телу (пояснице), а не к голове, иначе он будет "торчать из шеи".
        poseStack.pushPose();
        this.getParentModel().getBody().translateAndRotate(poseStack);
        addonModel.renderTail(poseStack, buf, packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
    }
}

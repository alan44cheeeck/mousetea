package com.example.mousetea.client;

import com.example.mousetea.MouseTeaMod;
import com.example.mousetea.client.model.MouseAddonModel;
import com.example.mousetea.client.renderer.MouseFormLayer;
import com.example.mousetea.client.renderer.MouseModelLayers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Аннотация ниже гарантирует, что этот класс грузится и подписывается на MOD-шину
 * только на клиенте — на сервере он просто не подгружается ClassLoader'ом Forge.
 */
@Mod.EventBusSubscriber(modid = MouseTeaMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(MouseModelLayers.MOUSE_ADDON, MouseAddonModel::createLayer);
    }

    @SubscribeEvent
    public static void addLayers(EntityRenderersEvent.AddLayers event) {
        for (String skin : event.getSkins()) {
            EntityRenderer<? extends AbstractClientPlayer> renderer = event.getSkin(skin);
            if (renderer instanceof PlayerRenderer playerRenderer) {
                playerRenderer.addLayer(new MouseFormLayer(playerRenderer, event.getEntityModels()));
            }
        }
    }
}

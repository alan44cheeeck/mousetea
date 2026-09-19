package com.example.mousetea;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Кладём чай на существующую вкладку "Еда и напитки" в творческом инвентаре,
 * чтобы предмет было легко найти без создания отдельной вкладки.
 */
@Mod.EventBusSubscriber(modid = MouseTeaMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CreativeTabEvents {

    @SubscribeEvent
    public static void onBuildTabs(CreativeModeTabEvent.BuildContents event) {
        if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
            event.accept(ModItems.MOUSE_TEA);
        }
    }
}

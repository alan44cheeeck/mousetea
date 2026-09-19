package com.example.mousetea;

import com.example.mousetea.capability.CapabilityEvents;
import com.example.mousetea.network.NetworkHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MouseTeaMod.MOD_ID)
public class MouseTeaMod {

    public static final String MOD_ID = "mousetea";

    public MouseTeaMod() {
        var modBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.ITEMS.register(modBus);
        NetworkHandler.register();

        // Общие (server+client) события: тик игрока, капабилити, применение чая.
        // Клиентские события (client.ClientEvents) сами подписываются через
        // @Mod.EventBusSubscriber(..., value = Dist.CLIENT) — их не нужно трогать здесь.
        MinecraftForge.EVENT_BUS.register(new CapabilityEvents());
    }
}

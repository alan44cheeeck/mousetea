package com.example.mousetea.network;

import com.example.mousetea.capability.MouseFormProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncMouseFormPacket {

    private final int entityId;
    private final int stage;

    public SyncMouseFormPacket(int entityId, int stage) {
        this.entityId = entityId;
        this.stage = stage;
    }

    public static void encode(SyncMouseFormPacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.entityId);
        buf.writeInt(msg.stage);
    }

    public static SyncMouseFormPacket decode(FriendlyByteBuf buf) {
        return new SyncMouseFormPacket(buf.readInt(), buf.readInt());
    }

    public static void handle(SyncMouseFormPacket msg, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> handleClient(msg));
        ctx.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleClient(SyncMouseFormPacket msg) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;
        Entity entity = mc.level.getEntity(msg.entityId);
        if (entity instanceof Player player) {
            player.getCapability(MouseFormProvider.MOUSE_FORM)
                    .ifPresent(data -> data.setStage(msg.stage));
        }
    }
}

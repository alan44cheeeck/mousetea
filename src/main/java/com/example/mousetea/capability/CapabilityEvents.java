package com.example.mousetea.capability;

import com.example.mousetea.MouseTeaMod;
import com.example.mousetea.network.NetworkHandler;
import com.example.mousetea.network.SyncMouseFormPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.PacketDistributor;

public class CapabilityEvents {

    private static final ResourceLocation MOUSE_FORM_ID =
            new ResourceLocation(MouseTeaMod.MOD_ID, "mouse_form");

    @SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent<net.minecraft.world.entity.Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(MOUSE_FORM_ID, new MouseFormProvider());
        }
    }

    // Сохраняем стадию при возрождении/смене измерения
    @SubscribeEvent
    public void onClone(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) return; // при смерти всё равно сохраняем стадию превращения
        event.getOriginal().getCapability(MouseFormProvider.MOUSE_FORM).ifPresent(oldData ->
                event.getEntity().getCapability(MouseFormProvider.MOUSE_FORM).ifPresent(newData ->
                        newData.copyFrom(oldData)));
    }

    @SubscribeEvent
    public void onPlayerTick(LivingEvent.LivingTickEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.level().isClientSide) return;

        player.getCapability(MouseFormProvider.MOUSE_FORM).ifPresent(data -> {
            int before = data.getStage();
            data.tick();

            applyStageEffects(player, data.getStage());

            // Синхронизируем клиенту раз в секунду и сразу при смене стадии,
            // чтобы рендер-слой (уши/хвост/усы) знал текущее состояние.
            if (data.getTicksInStage() == 0 || player.tickCount % 20 == 0) {
                NetworkHandler.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player),
                        new SyncMouseFormPacket(player.getId(), data.getStage()));
            }
        });
    }

    private void applyStageEffects(Player player, int stage) {
        // Стадия 3+: лёгкая "мышиная" прыгучесть и юркость
        if (stage >= 3) {
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 40, 0, false, false, true));
            player.addEffect(new MobEffectInstance(MobEffects.JUMP, 40, 0, false, false, true));
        }
        // Финальная стадия: игрок становится меньше похож на человека — дополнительно снижаем урон от падения,
        // как у мелкого грызуна.
        if (stage >= 4) {
            player.addEffect(new MobEffectInstance(MobEffects.SATURATION, 40, 0, false, false, false));
        }
    }
}

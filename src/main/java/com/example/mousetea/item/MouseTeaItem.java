package com.example.mousetea.item;

import com.example.mousetea.capability.MouseFormData;
import com.example.mousetea.capability.MouseFormProvider;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

/**
 * Чай, который при выпивании на 1 стадию продвигает превращение игрока в мышь.
 * Стадии (см. MouseFormData.MAX_STAGE):
 *  0 - обычный игрок
 *  1 - появляются уши
 *  2 - + хвост
 *  3 - + лапы/усы, лёгкий бафф на скорость и прыжок
 *  4 - почти полная мышиная форма (максимально изменённый силуэт через рендер-слой)
 */
public class MouseTeaItem extends Item {

    public MouseTeaItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32; // как обычное зелье
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, net.minecraft.world.entity.LivingEntity entity) {
        if (!level.isClientSide && entity instanceof Player player) {
            player.getCapability(MouseFormProvider.MOUSE_FORM).ifPresent(MouseFormData::advanceStage);
            level.playSound(null, player.blockPosition(), SoundEvents.GENERIC_DRINK,
                    SoundSource.PLAYERS, 1.0F, 1.0F);
        }
        if (!(entity instanceof Player p) || !p.getAbilities().instabuild) {
            stack.shrink(1);
        }
        return stack;
    }
}

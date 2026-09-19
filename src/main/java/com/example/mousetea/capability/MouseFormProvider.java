package com.example.mousetea.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MouseFormProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static final Capability<MouseFormData> MOUSE_FORM =
            CapabilityManager.get(new CapabilityToken<>() {});

    private final MouseFormData data = new MouseFormData();
    private final LazyOptional<MouseFormData> optional = LazyOptional.of(() -> data);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == MOUSE_FORM ? optional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("stage", data.getStage());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        data.setStage(tag.getInt("stage"));
    }

    public MouseFormData getData() {
        return data;
    }
}

package com.example.mousetea;

import com.example.mousetea.item.MouseTeaItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MouseTeaMod.MOD_ID);

    public static final RegistryObject<Item> MOUSE_TEA = ITEMS.register("mouse_tea",
            () -> new MouseTeaItem(new Item.Properties().stacksTo(16)));
}

package com.ixeken.easiersheets.tab;

import com.ixeken.easiersheets.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeTabs {
    // Creamos el registro para las pestañas creativas
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "createeasiersheets");

    // Construimos nuestra pestaña
    public static final Supplier<CreativeModeTab> EASIER_SHEETS_TAB = CREATIVE_MODE_TABS.register("easier_sheets_tab",
            () -> CreativeModeTab.builder()
                    // El título que buscará en el archivo de traducciones
                    .title(Component.translatable("creativetab.createeasiersheets"))
                    // El ícono de la pestaña (nuestro martillo)
                    .icon(() -> new ItemStack(ModItems.STURDY_HAMMER.get()))
                    // Los ítems que aparecerán dentro de la pestaña
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.STURDY_HAMMER.get());
                        // Si creamos más ítems en el futuro, los agregaremos aquí
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}

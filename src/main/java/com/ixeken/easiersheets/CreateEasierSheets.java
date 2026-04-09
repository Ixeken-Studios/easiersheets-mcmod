package com.ixeken.easiersheets;

import com.ixeken.easiersheets.item.ModItems;
import com.ixeken.easiersheets.tab.ModCreativeTabs;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(CreateEasierSheets.MODID)
public class CreateEasierSheets {
    public static final String MODID = "createeasiersheets";
    public static final Logger LOGGER = LogUtils.getLogger();

    public CreateEasierSheets(IEventBus modEventBus, ModContainer modContainer) {
        // Registro de los componentes del mod
        ModItems.register(modEventBus);
        ModCreativeTabs.register(modEventBus);

        // Registro de eventos
        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.register(this);

        // Configuración
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("Create: Easier Sheets inicializado correctamente.");
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Servidor de Create: Easier Sheets listo.");
    }
}


package com.ixeken.easiersheets.item;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    // IMPORTANTE: Cambia "createeasiersheets" por el ID exacto de tu mod (en minúsculas)
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems("createeasiersheets");

    // Registramos el martillo dándole una durabilidad máxima (ej. 256 usos)
    public static final DeferredItem<Item> STURDY_HAMMER = ITEMS.register("sturdy_hammer",
            () -> new SturdyHammerItem(new Item.Properties().durability(256)));

    // Este metodo lo llamaremos desde tu clase principal
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}

package com.ixeken.easiersheets;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * Clase de configuración para Create: Easier Sheets.
 * Actualmente no tiene valores configurables, pero se mantiene la estructura para uso futuro.
 */
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // Aquí se pueden añadir configuraciones en el futuro, por ejemplo:
    // public static final ModConfigSpec.IntValue HAMMER_DURABILITY = BUILDER
    //         .comment("Durabilidad máxima del Sturdy Hammer")
    //         .defineInRange("hammerDurability", 1024, 1, Integer.MAX_VALUE);

    static final ModConfigSpec SPEC = BUILDER.build();
}


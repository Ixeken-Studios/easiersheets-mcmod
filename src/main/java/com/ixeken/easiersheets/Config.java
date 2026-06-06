package com.ixeken.easiersheets;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * Configuration class for Create: Easier Sheets.
 */
public class Config {
    // --- CLIENT CONFIGURATION ---
    public static class Client {
        private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

        public static final ModConfigSpec.BooleanValue ENABLE_SOUND;
        public static final ModConfigSpec.IntValue SOUND_VOLUME;
        public static final ModConfigSpec.BooleanValue ENABLE_PARTICLES;

        static {
            BUILDER.comment("Client-only settings").translation("config.createeasiersheets.client").push("client");

            ENABLE_SOUND = BUILDER
                    .comment("Enable or disable sound when using the Sturdy Hammer on a Depot.")
                    .translation("config.createeasiersheets.enableSound")
                    .define("enableSound", true);

            SOUND_VOLUME = BUILDER
                    .comment("Volume of the sound when using the Sturdy Hammer (percentage).")
                    .translation("config.createeasiersheets.soundVolume")
                    .defineInRange("soundVolume", 54, 0, 100);

            ENABLE_PARTICLES = BUILDER
                    .comment("Enable or disable particles when using the Sturdy Hammer on a Depot.")
                    .translation("config.createeasiersheets.enableParticles")
                    .define("enableParticles", true);

            BUILDER.pop();
        }

        public static final ModConfigSpec SPEC = BUILDER.build();
    }

    // --- SERVER CONFIGURATION ---
    public static class Server {
        private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

        public static final ModConfigSpec.IntValue HAMMER_DURABILITY;
        public static final ModConfigSpec.IntValue MALLET_DURABILITY;
        public static final ModConfigSpec.IntValue OBSIDIAN_MALLET_BREAK_CHANCE;

        static {
            BUILDER.comment("Server-side gameplay settings").translation("config.createeasiersheets.server").push("server");

            HAMMER_DURABILITY = BUILDER
                    .comment("Maximum durability of the Sturdy Hammer.")
                    .translation("config.createeasiersheets.hammerDurability")
                    .defineInRange("hammerDurability", 1024, 1, 10000);

            MALLET_DURABILITY = BUILDER
                    .comment("Maximum durability of the Obsidian Mallet.")
                    .translation("config.createeasiersheets.malletDurability")
                    .defineInRange("malletDurability", 16, 1, 10000);

            OBSIDIAN_MALLET_BREAK_CHANCE = BUILDER
                    .comment("Percent chance that the Obsidian Mallet breaks instantly upon use.")
                    .translation("config.createeasiersheets.obsidianMalletBreakChance")
                    .defineInRange("obsidianMalletBreakChance", 10, 0, 100);

            BUILDER.pop();
        }

        public static final ModConfigSpec SPEC = BUILDER.build();
    }
}



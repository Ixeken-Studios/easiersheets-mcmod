package com.ixeken.easiersheets.item;

import com.ixeken.easiersheets.Config;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import com.simibubi.create.content.logistics.depot.DepotBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

public class SturdyHammerItem extends Item {

    public SturdyHammerItem(Item.Properties properties) {
        super(properties);
    }

    // --- NUEVO: Añadimos el Tooltip al ítem ---
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        String key = stack.is(ModItems.OBSIDIAN_MALLET.get()) 
                ? "tooltip.createeasiersheets.obsidian_mallet.usage" 
                : "tooltip.createeasiersheets.sturdy_hammer.usage";
        tooltipComponents.add(Component.translatable(key)
                .withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        // 1. Verificamos si es un Depot
        if (AllBlocks.DEPOT.has(level.getBlockState(pos))) {
            BlockEntity blockEntity = level.getBlockEntity(pos);

            if (blockEntity instanceof DepotBlockEntity depot) {
                ItemStack itemOnDepot = depot.getHeldItem();

                if (!itemOnDepot.isEmpty()) {

                    // 2. MAGIA AQUÍ: Obtenemos todas las recetas de "Prensado" (Mechanical Press) cargadas en el juego
                    List<RecipeHolder<PressingRecipe>> pressingRecipes = level.getRecipeManager().getAllRecipesFor(AllRecipeTypes.PRESSING.getType());

                    // 3. Buscamos si el ítem del Depot encaja en alguna de esas recetas
                    for (RecipeHolder<PressingRecipe> recipe : pressingRecipes) {

                        // .test() verifica automáticamente si el ítem es el correcto o si pertenece al Tag correcto (ej. c:ingots)
                        if (recipe.value().getIngredients().get(0).test(itemOnDepot)) {

                            if (!level.isClientSide()) {
                                // Consumimos el ingrediente
                                itemOnDepot.shrink(1);

                                // Obtenemos el resultado exacto que daría la prensa mecánica
                                ItemStack resultStack = recipe.value().getResultItem(level.registryAccess()).copy();

                                // Colocamos el resultado en el Depot
                                if (itemOnDepot.isEmpty()) {
                                    depot.setHeldItem(resultStack);
                                } else {
                                    Block.popResource(level, pos.above(), resultStack);
                                }

                                // --- FIX DE DESYNC ---
                                // Le decimos al servidor que guarde el bloque y obligue al cliente a redibujarlo
                                depot.setChanged();
                                level.sendBlockUpdated(pos, level.getBlockState(pos), level.getBlockState(pos), 3);
                                // ---------------------

                                // Desgastamos el martillo (con probabilidad de ruptura instantánea para el mazo de obsidiana)
                                if (context.getPlayer() != null) {
                                    ItemStack hammer = context.getItemInHand();
                                    if (hammer.is(ModItems.OBSIDIAN_MALLET.get()) && level.getRandom().nextFloat() < (Config.Server.OBSIDIAN_MALLET_BREAK_CHANCE.get() / 100.0F)) {
                                        int remainingDurability = hammer.getMaxDamage() - hammer.getDamageValue();
                                        hammer.hurtAndBreak(remainingDurability, context.getPlayer(), EquipmentSlot.MAINHAND);
                                    } else {
                                        hammer.hurtAndBreak(1, context.getPlayer(), EquipmentSlot.MAINHAND);
                                    }
                                }

                                // Feedback audiovisual
                                if (Config.Client.ENABLE_SOUND.get()) {
                                    float volume = Config.Client.SOUND_VOLUME.get() / 100.0F;
                                    level.playSound(null, pos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, volume, 1.2F);
                                }
                                // Efecto visual de partículas de llama para mayor inmersión
                                if (Config.Client.ENABLE_PARTICLES.get()) {
                                    ((ServerLevel) level).sendParticles(ParticleTypes.FLAME, 
                                        pos.getX() + 0.5, pos.getY() + 1.1, pos.getZ() + 0.5, 
                                        10, 0.2, 0.2, 0.2, 0.05);
                                }
                            }
                            return InteractionResult.sidedSuccess(level.isClientSide());
                        }
                    }
                }
            }
        }
        return super.useOn(context);
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        if (stack.is(ModItems.STURDY_HAMMER.get())) {
            return Config.Server.HAMMER_DURABILITY.get();
        }
        if (stack.is(ModItems.OBSIDIAN_MALLET.get())) {
            return Config.Server.MALLET_DURABILITY.get();
        }
        return super.getMaxDamage(stack);
    }
}
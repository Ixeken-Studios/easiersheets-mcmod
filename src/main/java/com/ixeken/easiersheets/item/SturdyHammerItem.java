package com.ixeken.easiersheets.item;

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
        // Añadimos una línea de texto gris traducible
        tooltipComponents.add(Component.translatable("tooltip.createeasiersheets.sturdy_hammer.usage")
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

                                // Desgastamos el martillo
                                if (context.getPlayer() != null) {
                                    context.getItemInHand().hurtAndBreak(1, context.getPlayer(), EquipmentSlot.MAINHAND);
                                }

                                // Feedback audiovisual
                                level.playSound(null, pos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 0.8F, 1.2F);
                                ((ServerLevel) level).sendParticles(ParticleTypes.CRIT,
                                        pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5,
                                        15, 0.2, 0.1, 0.2, 0.1);
                            }
                            return InteractionResult.sidedSuccess(level.isClientSide());
                        }
                    }
                }
            }
        }
        return super.useOn(context);
    }
}
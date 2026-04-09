package com.ixeken.easiersheets.integration;

import com.ixeken.easiersheets.CreateEasierSheets;
import com.ixeken.easiersheets.item.ModItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

@JeiPlugin
public class CreateEasierSheetsJeiPlugin implements IModPlugin {
    private static final ResourceLocation PLUGIN_ID = ResourceLocation.fromNamespaceAndPath(CreateEasierSheets.MODID, "jei_plugin");
    public static final RecipeType<PressingRecipe> HAMMERING_TYPE = RecipeType.create(CreateEasierSheets.MODID, "hammering", PressingRecipe.class);

    @Override
    @Nonnull
    public ResourceLocation getPluginUid() {
        return PLUGIN_ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new HammeringCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void registerRecipes(IRecipeRegistration registration) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level != null) {
            List<PressingRecipe> hammeringRecipes = (List) minecraft.level.getRecipeManager()
                    .getAllRecipesFor(AllRecipeTypes.PRESSING.getType())
                    .stream()
                    .map(RecipeHolder::value)
                    .filter(recipe -> recipe.getClass().getSimpleName().equals("PressingRecipe"))
                    .filter(recipe -> isHammerable((PressingRecipe) (Object) recipe))
                    .collect(Collectors.toList());

            registration.addRecipes(HAMMERING_TYPE, hammeringRecipes);
        }
    }

    private boolean isHammerable(PressingRecipe recipe) {
        ItemStack result = recipe.getResultItem(Minecraft.getInstance().level.registryAccess());
        if (result.isEmpty()) return false;
        String path = result.getItemHolder().unwrapKey().get().location().getPath();
        return path.contains("sheet") || path.contains("plate");
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(ModItems.STURDY_HAMMER.get().getDefaultInstance(), HAMMERING_TYPE);
    }

    private static class HammeringCategory implements IRecipeCategory<PressingRecipe> {
        private final IDrawable background;
        private final IDrawable icon;
        private final IDrawable slot;
        private final IDrawable arrow;
        private final Component localizedName;

        public HammeringCategory(IGuiHelper guiHelper) {
            this.background = guiHelper.createBlankDrawable(120, 45);
            this.slot = guiHelper.getSlotDrawable();
            this.icon = guiHelper.createDrawableItemStack(ModItems.STURDY_HAMMER.get().getDefaultInstance());
            this.localizedName = Component.translatable("category.createeasiersheets.hammering");
            
            ResourceLocation furnaceGui = ResourceLocation.withDefaultNamespace("textures/gui/container/furnace.png");
            this.arrow = guiHelper.createDrawable(furnaceGui, 79, 34, 24, 17);
        }

        @Override
        public RecipeType<PressingRecipe> getRecipeType() { return HAMMERING_TYPE; }
        @Override
        public Component getTitle() { return localizedName; }
        @Override
        @SuppressWarnings("removal")
        public IDrawable getBackground() { return background; }
        @Override
        public IDrawable getIcon() { return icon; }

        @Override
        public void draw(PressingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
            slot.draw(guiGraphics, 19, 14);
            slot.draw(guiGraphics, 79, 14);
            
            arrow.draw(guiGraphics, 46, 15);
        }

        @Override
        public void setRecipe(IRecipeLayoutBuilder builder, PressingRecipe recipe, IFocusGroup focuses) {
            builder.addSlot(RecipeIngredientRole.INPUT, 20, 15).addIngredients(recipe.getIngredients().get(0));
            builder.addSlot(RecipeIngredientRole.OUTPUT, 80, 15).addItemStack(recipe.getResultItem(Minecraft.getInstance().level.registryAccess()));
        }
    }
}

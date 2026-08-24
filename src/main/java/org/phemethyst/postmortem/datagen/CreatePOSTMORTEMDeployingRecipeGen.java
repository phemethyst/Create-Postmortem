package org.phemethyst.postmortem.datagen;

import java.util.concurrent.CompletableFuture;

import org.phemethyst.postmortem.AllItems;
import org.phemethyst.postmortem.CreatePOSTMORTEM;
import com.simibubi.create.api.data.recipe.BaseRecipeProvider.GeneratedRecipe;
import com.simibubi.create.api.data.recipe.DeployingRecipeGen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;

/**
 * Deploying recipe generator. A single standalone step, unlike the multi-step
 * sequenced assembly example.
 */
public class CreatePOSTMORTEMDeployingRecipeGen extends DeployingRecipeGen {

    GeneratedRecipe EXAMPLE = create("postmortem_deploying", b -> b
            .require(Items.IRON_INGOT)
            .require(Items.COPPER_INGOT)
            .output(AllItems.EXAMPLE_RESULT.get()));

    public CreatePOSTMORTEMDeployingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, CreatePOSTMORTEM.ID);
    }
}

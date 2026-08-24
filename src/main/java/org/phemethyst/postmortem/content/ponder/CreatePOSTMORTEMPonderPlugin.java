package org.phemethyst.postmortem.content.ponder;

import org.phemethyst.postmortem.AllBlocks;
import org.phemethyst.postmortem.CreatePOSTMORTEM;

import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

/**
 * Ponder plugin for the addon, registered client-side in CreatePOSTMORTEM. registerScenes
 * associates a storyboard with one or more items. Each scene has two parts: a schematic
 * saved as an nbt file under assets/postmortem/ponder, whose name matches the id passed
 * to addStoryBoard, and the storyboard code in CreatePOSTMORTEMPonderScenes.
 */
public class CreatePOSTMORTEMPonderPlugin implements PonderPlugin {

    @Override
    public String getModId() {
        return CreatePOSTMORTEM.ID;
    }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        helper.forComponents(AllBlocks.EXAMPLE_KINETIC_BLOCK.getId())
                .addStoryBoard("postmortem_ponder", CreatePOSTMORTEMPonderScenes::examplePonder);

        helper.forComponents(com.simibubi.create.AllBlocks.DESK_BELL.getId())
                .addStoryBoard("desk_bell", DeskbellScenes::intro);
    }
}

package org.phemethyst.postmortem;

import org.phemethyst.postmortem.content.display.CreatePOSTMORTEMDisplaySource;
import com.simibubi.create.api.behaviour.display.DisplaySource;
import com.tterrag.registrate.util.entry.RegistryEntry;

/**
 * Display source registration. Attach an entry to a block in AllBlocks with
 * transform(DisplaySource.displaySource(entry)).
 */
public class AllDisplaySources {

    public static final RegistryEntry<DisplaySource, CreatePOSTMORTEMDisplaySource> EXAMPLE_SOURCE = CreatePOSTMORTEM.REGISTRATE
            .displaySource("postmortem_source", CreatePOSTMORTEMDisplaySource::new)
            .register();

    public static void register() {
        // Force class loading to trigger Registrate calls
    }
}

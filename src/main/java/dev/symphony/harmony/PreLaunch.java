package dev.symphony.harmony;

import dev.symphony.harmony.config.HarmonyConfig;
import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class PreLaunch implements PreLaunchEntrypoint {

    @Override
    public void onPreLaunch() {
        //Makes MidnightConfig load. It has to load here in order to make sure it loads before mixins and not afterwards so everything works as intended.
        MidnightConfig.init("harmony", HarmonyConfig.class);
    }
}

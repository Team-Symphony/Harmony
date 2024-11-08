package dev.symphony.harmony;

import dev.symphony.harmony.config.HarmonyConfig;
import dev.symphony.harmony.config.HarmonyConfigCondition;
import dev.symphony.harmony.item.ModifyItems;
import dev.symphony.harmony.loot.HarmonyLootContextTypes;
import dev.symphony.harmony.loot.HarmonyLootTables;
import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Harmony implements ModInitializer {
	public static final String MOD_ID = "harmony";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Identifier id (String path) {
		return Identifier.of(Harmony.MOD_ID, path);
	}

	@Override
	public void onInitialize() {
		// Config
		MidnightConfig.init(MOD_ID, HarmonyConfig.class);
		HarmonyConfigCondition.init();

		ResourceConditionType<HarmonyConfigCondition> conditionType = ResourceConditionType.create(Identifier.of(Harmony.MOD_ID, "config"), HarmonyConfigCondition.CODEC);
		ResourceConditions.register(conditionType);


		// gay stuff
		ModifyItems.init();

		HarmonyLootTables.noop();
		HarmonyLootContextTypes.noop();
	}
}
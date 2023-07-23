package net.sydokiddo.odyssey;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.odyssey.misc.config.ModConfig;
import net.sydokiddo.odyssey.registry.OdysseyRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Odyssey implements ModInitializer {

	public static final String MOD_ID = "odyssey";
	private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private static final ModConfig CONFIG = AutoConfig.register(ModConfig.class, GsonConfigSerializer::new).getConfig();

	public static boolean chrysalisInitialized() {
		return FabricLoader.getInstance().isModLoaded(Chrysalis.MOD_ID);
	}

	@Override
	public void onInitialize() {
		if (chrysalisInitialized()) {
			OdysseyRegistry.registerAll();
			LOGGER.info("Thank you for downloading Odyssey!");
		} else {
			LOGGER.error("Failed to initialize mod, Chrysalis is not installed!", new Exception());
		}
	}

	// Mod Config

	public static ModConfig getConfig () {
		return CONFIG;
	}
}

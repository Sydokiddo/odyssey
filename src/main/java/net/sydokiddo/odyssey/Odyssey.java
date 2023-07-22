package net.sydokiddo.odyssey;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.odyssey.misc.config.ModConfig;
import net.sydokiddo.odyssey.registry.OdysseyRegistry;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Odyssey implements ModInitializer {

	public static final String MOD_ID = "odyssey";
	private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private static final ModConfig CONFIG = AutoConfig.register(ModConfig.class, GsonConfigSerializer::new).getConfig();

	public static boolean chrysalisInitialized() {
		return QuiltLoader.isModLoaded(Chrysalis.MOD_ID);
	}

	@Override
	public void onInitialize(ModContainer mod) {
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

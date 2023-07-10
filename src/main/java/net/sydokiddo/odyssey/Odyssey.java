package net.sydokiddo.odyssey;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.sydokiddo.odyssey.misc.config.ModConfig;
import net.sydokiddo.odyssey.registry.OdysseyRegistry;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Odyssey implements ModInitializer {

	public static final String MOD_ID = "odyssey";
	private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private static final ModConfig CONFIG = AutoConfig.register(ModConfig.class, GsonConfigSerializer::new).getConfig();

	@Override
	public void onInitialize(ModContainer mod) {
		OdysseyRegistry.registerAll();
		LOGGER.info("Thank you for downloading Odyssey!");
	}

	// Mod Config

	public static ModConfig getConfig () {
		return CONFIG;
	}
}

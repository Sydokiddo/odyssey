package net.sydokiddo.odyssey;

import net.fabricmc.api.ModInitializer;
import net.sydokiddo.odyssey.registry.OdysseyRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Odyssey implements ModInitializer {

	public static final String MOD_ID = "odyssey";
	private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		OdysseyRegistry.registerAll();
		LOGGER.info("Thank you for downloading Example Odyssey");
	}
}

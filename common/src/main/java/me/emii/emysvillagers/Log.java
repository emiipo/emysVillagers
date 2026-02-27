package me.emii.emysvillagers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log {

	public static final String MOD_NAME = "Emys Villagers";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

	public static void info(String message, Object... args) {
		LOGGER.info(message, args);
	}

	public static void debug(String message, Object... args) {
		LOGGER.debug(message, args);
	}

	public static void warn(String message, Object... args) {
		LOGGER.warn(message, args);
	}

	public static void error(String message, Object... args) {
		LOGGER.error(message, args);
	}
}
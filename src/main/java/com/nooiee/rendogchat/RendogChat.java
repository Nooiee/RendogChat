package com.nooiee.rendogchat;

import com.nooiee.rendogchat.config.ConfigManager;
import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RendogChat implements ModInitializer {
	public static final String MOD_ID = "rendogchat";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// 모드 초기화 시 설정 파일을 로드합니다.
		LOGGER.info("Initializing RendogChat mod...");
		ConfigManager.loadConfig();  // 설정 파일을 로드하거나 생성합니다.
	}

	public static Identifier id(String path) {
		return new Identifier(MOD_ID, path);
	}
}

package com.nooiee.rendogchat.accessor;

import net.minecraft.client.gui.screen.ChatScreen;

public interface ChatScreenAccessor {
    static ChatScreenAccessor from (ChatScreen chatScreen) {
        // 안전한 캐스팅을 위해 instanceof 검사 추가
        if (chatScreen instanceof ChatScreenAccessor) {
            return (ChatScreenAccessor) chatScreen;
        } else {
            throw new IllegalArgumentException("ChatScreen does not implement ChatScreenAccessor.");
        }
    }
}

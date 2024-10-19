package com.nooiee.rendogchat.mixin;

import com.nooiee.rendogchat.accessor.ChatScreenAccessor;
import com.nooiee.rendogchat.config.ConfigManager;
import com.nooiee.rendogchat.gui.ChatTab;
import com.nooiee.rendogchat.gui.SettingMenu;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin extends Screen implements ChatScreenAccessor {
    @Unique private ChatTab tabButton;
    @Unique private SettingMenu settingMenu;

    protected ChatScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    protected void init(CallbackInfo ci) {
        int screenHeight = client.getWindow().getScaledHeight();

        // 저장된 탭 이름 불러오기
        String savedTabName = ConfigManager.getChatTabName();
        if (savedTabName == null || savedTabName.isEmpty()) {
            savedTabName = "Tab";
        }

        // 탭 버튼 생성
        Text buttonText = Text.literal(savedTabName);
        int textWidth = client.textRenderer.getWidth(buttonText);
        int buttonWidth = textWidth + 10;
        int buttonHeight = 15;

        tabButton = new ChatTab(2, screenHeight - 35, buttonWidth, buttonHeight, buttonText, button -> {
            // 좌클릭 동작 (필요한 경우 구현)
        }, button -> {
            // 우클릭 동작: 설정 메뉴 표시 (일단 ChatTab 코드에서 처리)
        });

        addDrawableChild(tabButton);

    }

    @Inject(method = "render", at = @At("TAIL"))
    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        // 설정 메뉴 렌더링
        if (tabButton != null) {
            tabButton.renderWidget(drawContext, mouseX, mouseY, delta);
        }
        if (settingMenu != null && settingMenu.isVisible()) {
            settingMenu.render(drawContext, mouseX, mouseY, delta);
        }
    }

    @Inject(method = "removed", at = @At("TAIL"))
    public void onClose(CallbackInfo ci) {
        // 채팅창이 닫힐 때 설정 메뉴를 닫습니다.
        if (settingMenu != null) {
            settingMenu.setVisible(false);
        }
        //컨피그 저장도 추가
        ConfigManager.saveConfig();
    }
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // 설정 메뉴가 표시되어 있을 때
        if (settingMenu != null && settingMenu.isVisible()) {
            if (settingMenu.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
            // 설정 메뉴 영역 밖을 클릭하면 설정 메뉴 닫기
            settingMenu.setVisible(false);
            return super.mouseClicked(mouseX, mouseY, button);
        }

        // 탭 버튼 클릭 처리
        if (tabButton != null && tabButton.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

}


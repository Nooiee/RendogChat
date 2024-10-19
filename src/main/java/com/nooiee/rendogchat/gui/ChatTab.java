package com.nooiee.rendogchat.gui;

import com.nooiee.rendogchat.config.ConfigManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class ChatTab extends ButtonWidget {
    private final PressAction onLeftClick;
    private final PressAction onRightClick;
    public static boolean toggled = false;
    public static boolean showSettingsMenu = false;
    private static String tabName;
    private SettingMenu menu;



    // 생성자
    public ChatTab(int x, int y, int width, int height, Text message, PressAction leftAction, PressAction rightAction) {
        super(x, y, width, height, message, button -> {}, textSupplier -> null);
        this.onLeftClick = leftAction;
        this.onRightClick = rightAction;

        tabName = ConfigManager.getChatTabName();
        this.setMessage(Text.literal(tabName));
        this.menu = new SettingMenu(MinecraftClient.getInstance(), this);
    }
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.active && this.visible && this.clicked(mouseX, mouseY)) {
            if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
                toggled = !toggled;  // 좌클릭 시 토글 상태 변경
                this.onPress();
                return true;
            } else if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
                boolean newVisibility = !this.menu.isVisible();
                this.menu.setVisible(newVisibility);
                return true;
            }
        }
        if (this.menu.isVisible() && this.menu.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        return false;
    }



    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        // 토글 상태에 따른 배경 색상 변경
        int backgroundColor;
        double backgroundOpacity = MinecraftClient.getInstance().options.getTextBackgroundOpacity().getValue() * 0.5;
        int alpha = (int) (backgroundOpacity * 255) & 0xFF;
        if (toggled) {
            backgroundColor = (alpha << 24) | 0x999999;
        } else {
            backgroundColor = (alpha << 24);  // 검은색에 투명도 적용
        }

        context.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, backgroundColor);
        // 텍스트 렌더링 (기본 배경 렌더링은 제거)
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        int textColor = 0xFFFFFF;
        int textWidth = textRenderer.getWidth(this.getMessage());
        int textX = this.getX() + (this.width - textWidth) / 2;
        int textY = this.getY() + (this.height - textRenderer.fontHeight) / 2 + 1;
        context.drawText(textRenderer, this.getMessage(), textX, textY, textColor, true);
        
        //우클릭 렌더링
        if (this.menu.isVisible()) {
            this.menu.render(context, mouseX, mouseY, delta);
        }
    }
    

    public void setTabName(String name) {
        if (name == null || name.isEmpty()) {
            this.tabName = "Tab";
        } else {
            this.tabName = name;
        }
        this.setMessage(Text.literal(this.tabName));
        //컨피그에 저장
        ConfigManager.setChatTabName(this.tabName);
    }

    public SettingMenu getMenu() {
        return this.menu;
    }

    public boolean isToggled() {
        return toggled;
    }
    public static boolean isSettingsMenuVisible() {
        return showSettingsMenu;
    }
}

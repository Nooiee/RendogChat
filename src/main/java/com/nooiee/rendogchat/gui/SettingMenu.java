package com.nooiee.rendogchat.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.util.function.Consumer;
import java.util.logging.Logger;

public class SettingMenu {
    private MinecraftClient client;
    private final ChatTab parentTab;
    private boolean visible = false;
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private int labelX;
    private int labelY;


    private TextFieldWidget textFieldWidget;
    private SettingButton settingsButton;

    public SettingMenu(MinecraftClient client, ChatTab parentTab) {
        this.client = client;
        this.parentTab = parentTab;
        this.width = 100;
        this.height = 50;
        this.x = parentTab.getX();
        this.y = parentTab.getY() - height - 5;

        //위젯 초기화
        initWidgets();
    }

    private void initWidgets() {
        int spacing = 5;
        TextRenderer textRenderer = client.textRenderer;
        String labelText = "Name:";
        int labelWidth = textRenderer.getWidth(labelText);
        labelX = x + spacing;
        labelY = y + spacing + 3; // 레이블 y 좌표

        int textFieldWidth = width - labelWidth - (spacing * 3);
        int textFieldHeight = 10;
        int textFieldX = labelX + labelWidth + spacing + 1;
        int textFieldY = labelY; // 레이블과 동일한 y 좌표로 설정

        textFieldWidget = new TextFieldWidget(textRenderer, textFieldX, textFieldY, textFieldWidth, textFieldHeight, Text.literal("Name"));
        textFieldWidget.setMaxLength(10);
        textFieldWidget.setDrawsBackground(false);
        textFieldWidget.setEditable(true);
        textFieldWidget.setVisible(true);
        textFieldWidget.setText(parentTab.getMessage().getString());

        // 이름 변경 이벤트 처리
        textFieldWidget.setChangedListener(parentTab::setTabName);
        int settingsButtonWidth = 80;
        int settingsButtonHeight = 20;
        int settingsButtonX = x + (width - settingsButtonWidth) / 2; // 가운데 정렬
        int settingsButtonY = y + height - settingsButtonHeight - spacing;

        settingsButton = new SettingButton(settingsButtonX, settingsButtonY, settingsButtonWidth, settingsButtonHeight, button -> client.setScreen(new SettingScreen(client.currentScreen)));

        settingsButton.setVisible(true);
    }

    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        if (!visible) return;

        // 메뉴 배경 및 테두리 그리기
        double backgroundOpacity = MinecraftClient.getInstance().options.getTextBackgroundOpacity().getValue() * 0.5;
        int alpha = (int) (backgroundOpacity * 255) & 0xFF;
        int backgroundColor = (alpha << 24) | 0xa08662; // 배경 색상
        drawContext.fill(x, y, x + width, y + height, backgroundColor);
        drawContext.drawBorder(x, y, width, height, 0xffc7b08b);

        // 레이블 렌더링
        TextRenderer textRenderer = client.textRenderer;
        drawContext.drawText(textRenderer, "Name:", labelX, labelY, 0xFFFFFF, false);

        // 이름 필드 배경 및 테두리 직접 렌더링
        int bgX = textFieldWidget.getX() - 2 - 1; // 왼쪽으로 1픽셀 이동
        int bgY = textFieldWidget.getY() - 2 - 1; // 위로 1픽셀 이동
        int bgWidth = textFieldWidget.getWidth() + 4;
        int bgHeight = textFieldWidget.getHeight() + 4;

        int fieldBackgroundColor = (alpha << 24); // 투명도 적용된 검은색 배경
        drawContext.fill(bgX, bgY, bgX + bgWidth, bgY + bgHeight, fieldBackgroundColor);
        drawContext.drawBorder(bgX, bgY, bgWidth, bgHeight, 0xffc7b08b);

        // 텍스트 필드 및 설정 버튼 렌더링
        textFieldWidget.render(drawContext, mouseX, mouseY, delta);
        settingsButton.render(drawContext, mouseX, mouseY, delta);
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        textFieldWidget.setVisible(visible);
        settingsButton.setVisible(visible);
    }

    public boolean isVisible() {
        return visible;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        //디버그용
        //System.out.println("SettingMenu clicked at: (" + mouseX + ", " + mouseY + ")");
        if (!visible)
            return false;
        if (isMouseOverMenu(mouseX, mouseY)) {
            if (textFieldWidget.mouseClicked(mouseX, mouseY, button)) {
                textFieldWidget.setFocused(true); //이름 적는 칸에 포커스 설정
                return true;
            } else {
                textFieldWidget.setFocused(false);
            }
            if (settingsButton.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
            return true; // 메뉴 영역 내에서의 클릭은 이벤트 소비
        }
        return false;

    }

    // 키 입력 이벤트 처리 메서드 추가
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!visible) return false;
        if (textFieldWidget.isFocused() && textFieldWidget.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }
        // 추가적으로 settingsButton의 키 입력이 필요하면 여기에 처리 가능
        return false;
    }

    public boolean charTyped(char chr, int modifiers) {
        if (!visible) return false;
        if (textFieldWidget.isFocused() && textFieldWidget.charTyped(chr, modifiers)) {
            return true;
        }
        return false;
    }

    private boolean isMouseOverMenu(double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

}

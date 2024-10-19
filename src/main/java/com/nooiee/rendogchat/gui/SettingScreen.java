package com.nooiee.rendogchat.gui;

import com.nooiee.rendogchat.config.ConfigManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class SettingScreen extends Screen {
    private final Screen parent;
    private final List<ToggleCondition> conditions = new ArrayList<>();

    public SettingScreen(Screen parent) {
        super(Text.of("Chat Filter Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        // 기존 설정값 불러오기
        List<ConfigManager.Config.Condition> savedConditions = ConfigManager.getConfig().conditions;

        // 화면의 요소들이 제목으로 인해 조금 아래로 이동하도록 Y축 기준을 수정
        int baseYOffset = 20;

        for (int i = 0; i < 3; i++) {
            if (i < savedConditions.size()) {
                addConditionWidgets(i, savedConditions.get(i), baseYOffset);
            } else {
                addConditionWidgets(i, null, baseYOffset); // 기존 값이 없는 경우 기본 설정으로 생성
            }
        }

        // Done 버튼 추가 - 설정 저장 후 돌아가기
        this.addDrawableChild(ButtonWidget.builder(Text.of("Done"), button -> {
            saveSettings();
            this.client.setScreen(parent);
        }).dimensions(this.width / 2 - 100, this.height - 40, 200, 20).build());
    }

    private void addConditionWidgets(int index, ConfigManager.Config.Condition savedCondition, int baseYOffset) {
        int y = baseYOffset + 40 + index * 40; // 각 조건의 Y축 위치 조정 (기본 오프셋 추가)

        // 토글 버튼 생성 - 저장된 설정값 반영
        String toggleText = (savedCondition != null && "제외".equals(savedCondition.mode)) ? "제외" : "포함";
        ButtonWidget toggleButton = ButtonWidget.builder(Text.of(toggleText), button -> {
            if ("포함".equals(button.getMessage().getString())) {
                button.setMessage(Text.of("제외"));
            } else {
                button.setMessage(Text.of("포함"));
            }
        }).dimensions(this.width / 2 - 150, y, 50, 20).build();

        // 텍스트 필드 1 생성 - 저장된 설정값 반영
        String text1 = savedCondition != null ? savedCondition.text1 : "";
        TextFieldWidget textField1 = new TextFieldWidget(this.textRenderer, this.width / 2 - 80, y, 60, 20, Text.of(""));
        textField1.setText(text1);

        // 텍스트 필드 2 생성 - 저장된 설정값 반영
        String text2 = savedCondition != null ? savedCondition.text2 : "";
        TextFieldWidget textField2 = new TextFieldWidget(this.textRenderer, this.width / 2 + 20, y, 60, 20, Text.of(""));
        textField2.setText(text2);

        // 텍스트 필드 3 생성 - 저장된 설정값 반영
        String text3 = savedCondition != null ? savedCondition.text3 : "";
        TextFieldWidget textField3 = new TextFieldWidget(this.textRenderer, this.width / 2 + 120, y, 60, 20, Text.of(""));
        textField3.setText(text3);

        // ToggleCondition 객체로 묶어서 관리
        ToggleCondition condition = new ToggleCondition(toggleButton, textField1, textField2, textField3);
        conditions.add(condition);

        // 위젯 추가
        this.addDrawableChild(toggleButton);
        this.addDrawableChild(textField1);
        this.addDrawableChild(textField2);
        this.addDrawableChild(textField3);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);

        // 상단 중앙에 제목 "채팅 필터 설정" 추가
        context.drawText(this.textRenderer, Text.of("채팅 필터 설정"), this.width / 2 - this.textRenderer.getWidth("채팅 필터 설정") / 2, 20, 0xFFFFFF, true);

        // 고정 텍스트 그리기 - "조건 1:", "and"
        int maxConditionsToShow = Math.min(conditions.size(), 3);
        for (int i = 0; i < maxConditionsToShow; i++) {
            int y = 60 + i * 40; // 제목으로 인해 Y축이 조금 더 아래로 이동됨

            // "조건 1:", "조건 2:", "조건 3:" 텍스트
            context.drawText(this.textRenderer, Text.of("조건 " + (i + 1) + ":"), this.width / 2 - 190, y + 7, 0xFFFFFF, false);

            // "and" 텍스트 추가 (텍스트 필드 사이에 위치하도록 조정)
            context.drawText(this.textRenderer, Text.of("and"), this.width / 2 - 10, y + 7, 0xFFFFFF, false);
            context.drawText(this.textRenderer, Text.of("and"), this.width / 2 + 90, y + 7, 0xFFFFFF, false);
        }
    }

    private void renderBackground(DrawContext context) {
        // 필요시 배경을 그리는 코드를 추가할 수 있습니다.
    }

    // 설정 저장
    private void saveSettings() {
        List<ConfigManager.Config.Condition> savedConditions = new ArrayList<>();
        for (ToggleCondition condition : conditions) {
            String mode = condition.toggleButton.getMessage().getString();
            String text1 = condition.textField1.getText();
            String text2 = condition.textField2.getText();
            String text3 = condition.textField3.getText();
            savedConditions.add(new ConfigManager.Config.Condition(mode, text1, text2, text3));
        }
        ConfigManager.getConfig().conditions = savedConditions;
        ConfigManager.saveConfig();
    }

    private static class ToggleCondition {
        ButtonWidget toggleButton;
        TextFieldWidget textField1;
        TextFieldWidget textField2;
        TextFieldWidget textField3;

        public ToggleCondition(ButtonWidget toggleButton, TextFieldWidget textField1, TextFieldWidget textField2, TextFieldWidget textField3) {
            this.toggleButton = toggleButton;
            this.textField1 = textField1;
            this.textField2 = textField2;
            this.textField3 = textField3;
        }
    }
}

package com.abaan404.SimpleCalculator.Gui;

import com.abaan404.SimpleCalculator.Calculate;
import com.abaan404.SimpleCalculator.Main;
import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import java.util.List;

public class CalcGUI extends LightweightGuiDescription {
    public CalcGUI() {
        final int[] i = {0};

        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        WGridPanel panel = new WGridPanel();
        panel.setSize(500,217);
        WScrollPanel scroll = new WScrollPanel(panel).setScrollingVertically(TriState.TRUE).setScrollingHorizontally(TriState.FALSE);
        scroll.setBackgroundPainter(BackgroundPainter.createColorful(0xFF000000));

        WTextField inputField = new WTextField(new TranslatableText("Expression")).setMaxLength(48);

        WButton inputButton = new WButton(new LiteralText("Evaluate").formatted(Formatting.GREEN));


        // adds expression history into panel
        List<String> calcHistory = Main.fetchHistory(0);
        List<String> evalHistory = Main.fetchHistory(1);
        for (int x = 0; x < calcHistory.size(); x++) {
            if (evalHistory.get(x) != "NaN") {
                panel.add(new WLabel(new LiteralText(calcHistory.get(x) + " = " + evalHistory.get(x)).formatted(Formatting.WHITE)),0,i[0]);
            } else {
                panel.add(new WLabel(new LiteralText(calcHistory.get(x) + " = " + "Invalid Operation.").formatted(Formatting.RED)),0,i[0]);
            }
            i[0] = x+1;
        }
        inputButton.setOnClick(() -> {
            String eval = Calculate.eval(inputField.getText());
            if (eval != "NaN") {
                panel.add(new WLabel(new LiteralText(inputField.getText() + " = " + eval).formatted(Formatting.WHITE)),0,i[0]);
                Main.addHistory(inputField.getText(), eval);
            } else {
                panel.add(new WLabel(new LiteralText(inputField.getText() + " = " + "Invalid Operation.").formatted(Formatting.RED)),0,i[0]);
            }
            inputField.requestFocus();
            i[0]++;
        });

        root.add(scroll, 1, 1, 25, 12);
        root.add(inputField, 0, 14, 23, 1);
        root.add(inputButton, 23, 14, 4, 1);
        root.validate(this);

        inputField.requestFocus();
    }
}

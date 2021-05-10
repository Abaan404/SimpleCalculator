package com.abaan404.SimpleCalculator;

import com.abaan404.SimpleCalculator.Gui.CalcGUI;
import com.abaan404.SimpleCalculator.Gui.CalcScreen;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;
import net.fabricmc.api.ModInitializer;

import java.util.ArrayList;
import java.util.List;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;

public class Main implements ModInitializer {

    // [[exp,eval]]
    private static List<List<String>> history = new ArrayList<>();

    @Override
    public void onInitialize() {

        KeyBinding GUIKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("Open GUI", GLFW.GLFW_KEY_O, "SimpleCalculator"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (GUIKey.isPressed()) { MinecraftClient.getInstance().openScreen(new CalcScreen(new CalcGUI())); }
        });

        ClientCommandManager.DISPATCHER.register(ClientCommandManager.literal("calc")
                .then(ClientCommandManager.argument("expression", greedyString())
                        .executes(context -> {
                            eval(context, getString(context, "expression"));
                            return 0;
                        })
                )
                .then(ClientCommandManager.literal("history")
                        .executes(context -> {
                            context.getSource().sendFeedback(new LiteralText("\n  ==== Recent Calculations ==== \n").formatted(Formatting.AQUA));
                            for (int i = 0; i < history.size(); i++) {
                                context.getSource().sendFeedback(new LiteralText("   " + fetchHistory(0).get(i) + " = " + fetchHistory(1).get(i)).formatted(Formatting.GRAY));
                            }
                            context.getSource().sendFeedback(new LiteralText(""));
                            return 0;
                        })
                        .then(ClientCommandManager.literal("clear")
                                .executes(context -> {
                                    context.getSource().sendFeedback(new LiteralText("\nHistory Cleared Successfully!\n").formatted(Formatting.BOLD));
                                    history = new ArrayList<>();
                                    return 0;
                                })
                        )
                )
        );
    }

    public static void eval(CommandContext<FabricClientCommandSource> context, String expression) {
        String output = Calculate.eval(expression);
        if (output == "NaN") {
            context.getSource().sendError(new LiteralText("Invalid Operation.").formatted(Formatting.DARK_RED));
        } else {
            context.getSource().sendFeedback(new LiteralText("Evaluated Expression: " + output).formatted(Formatting.GREEN));
            addHistory(expression, output);
        }
    }

    public static void addHistory(String arg1, String arg2) {
        List<String> list = new ArrayList<>();
        list.add(arg1);
        list.add(arg2);
        history.add(list);
    }

    public static List<String> fetchHistory(int index) {
        List<String> list = new ArrayList<>();
        for (List<String> strings : history) {
            list.add(strings.get(index));
        }
        return list;
    }
}
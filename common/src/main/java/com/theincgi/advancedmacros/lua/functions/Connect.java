package com.theincgi.advancedmacros.lua.functions;

import com.theincgi.advancedmacros.event.TaskDispatcher;
import com.theincgi.advancedmacros.misc.CallableTable;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConnectScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.resource.language.I18n;
import org.luaj.vm2_v3_0_1.LuaValue;
import org.luaj.vm2_v3_0_1.lib.OneArgFunction;

public class Connect extends CallableTable {

    public Connect() {
        super(new String[]{"connect"}, new Op());
    }

    private static class Op extends OneArgFunction {

        @Override
        public LuaValue call(LuaValue arg) {

            TaskDispatcher.addTask(() -> {
                MinecraftClient mc = MinecraftClient.getInstance();

                // Disconnect if already in a world
                if (mc.world != null) {
                    Disconnect.disconnect();
                }

                // Prepare server info
                String serverAddressString = arg.checkjstring();
                ServerInfo serverInfo = new ServerInfo(
                        I18n.translate("selectServer.defaultName"),
                        serverAddressString,
                        false
                );

                ServerAddress serverAddress = ServerAddress.parse(serverInfo.address);
                Screen prevScreen = mc.currentScreen;

                // Start connecting
                ConnectScreen.connect(prevScreen, mc, serverAddress, serverInfo, false);
            });

            return NONE;
        }
    }
}

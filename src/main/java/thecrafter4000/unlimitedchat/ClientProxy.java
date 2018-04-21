package thecrafter4000.unlimitedchat;

import java.lang.reflect.Field;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import net.minecraft.client.gui.GuiChat;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import thecrafter4000.unlimitedchat.client.GuiUnlimitedChat;

public class ClientProxy extends CommonProxy {
	
	public static int ChatLimit = -1;
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);

		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onGuiOpen(GuiOpenEvent event) {
		if(ChatLimit != -1 && event.gui != null && event.gui.getClass() == GuiChat.class) {
			try {
				event.gui = new GuiUnlimitedChat(getText((GuiChat) event.gui));
			} catch (Exception e) {
				UnlimitedChat.Logger.fatal("Reflection error! Report this to the mod author!", e);
			}
//			UnlimitedChat.Logger.info("Successfully redirected Gui request!");
		}
	}
	
	@SubscribeEvent
	public void onWorldLeave(ClientDisconnectionFromServerEvent event) { // makes sure to reset the chatlimit after the client left a server.
		ChatLimit = -1;
	}
	
	public String getText(GuiChat gui) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Field f;
		try {
			f = GuiChat.class.getDeclaredField("field_146409_v");
		}catch(NoSuchFieldException e) {
			f = GuiChat.class.getDeclaredField("defaultInputFieldText");
		}
		
		f.setAccessible(true);
		return (String) f.get(gui);
	}
}

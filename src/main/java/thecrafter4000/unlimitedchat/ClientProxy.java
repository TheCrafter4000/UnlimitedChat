package thecrafter4000.unlimitedchat;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.GuiOpenEvent;
import thecrafter4000.unlimitedchat.client.GuiUnlimitedChat;
import thecrafter4000.unlimitedchat.data.ChatProperties;

import java.lang.reflect.Field;

/**
 * Proxy on client side.
 * @author TheCrafter4000
 */
public class ClientProxy extends CommonProxy {

	public static ChatProperties config;
	
	/**
	 * Called when a GUI is opened. Used to replace the default chat GUI.
	 */
	@SubscribeEvent
	public void onGuiOpen(GuiOpenEvent event) {
		if(event.gui != null && event.gui.getClass() == GuiChat.class && config.modEnabled) {
			try {
				event.gui = new GuiUnlimitedChat(getText(event.gui));
			} catch (Exception e) {
				UnlimitedChat.Logger.fatal("Reflection error! Report this to the mod author!", e);
			}
		}
	}

	/**
	 * Called when the player joins the world. Used to reset the char limit.
	 */
	@SubscribeEvent
	public void onPlayerJoin(FMLNetworkEvent.ClientConnectedToServerEvent event) {
		config = new ChatProperties(); // Resets properties every time the player changes the server.
	}

	/** Internal reflection method */
	private String getText(GuiScreen gui) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
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

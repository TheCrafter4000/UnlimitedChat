package thecrafter4000.unlimitedchat;

import java.lang.reflect.Field;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.GuiOpenEvent;
import thecrafter4000.unlimitedchat.client.GuiUnlimitedChat;

/**
 * Proxy on client side.
 * @author TheCrafter4000
 */
public class ClientProxy extends CommonProxy {
	
	/**
	 * Called when a GUI is opened. Used to replace the default chat GUI.
	 */
	@SubscribeEvent
	public void onGuiOpen(GuiOpenEvent event) {
		if(event.gui != null && event.gui.getClass() == GuiChat.class) {
			try {
				event.gui = new GuiUnlimitedChat(getText(event.gui));
			} catch (Exception e) {
				UnlimitedChat.Logger.fatal("Reflection error! Report this to the mod author!", e);
			}
		}
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
		return (String) f.get((GuiChat) gui);
	}
}

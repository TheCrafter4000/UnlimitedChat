package thecrafter4000.unlimitedchat.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraftforge.client.ClientCommandHandler;
import thecrafter4000.unlimitedchat.UnlimitedChat;
import thecrafter4000.unlimitedchat.data.ChatProperties;
import thecrafter4000.unlimitedchat.network.PacketC01ChatMessage;
import thecrafter4000.unlimitedchat.network.PacketC02ClientCommand;

/**
 * An extended version of the vanilla chat GUI.
 * @author TheCrafter4000
 */
@SideOnly(Side.CLIENT)
public class GuiUnlimitedChat extends GuiChat {

	public GuiUnlimitedChat(String text) {
		super(text); // Makes sure that the "/" hotkey is working.
	}
	
	@Override
	public void initGui() {
		super.initGui();
		this.inputField.setMaxStringLength(ChatProperties.get(Minecraft.getMinecraft().thePlayer).getCharlimit()); // Updates char limit.
	}

	@Override
	public void func_146403_a(String msg) {
        this.mc.ingameGUI.getChatGUI().addToSentMessages(msg); // Add's the message to our last-typed history
        if (ClientCommandHandler.instance.executeCommand(mc.thePlayer, msg) != 0) { // Executes client-side commands	
        	if(ChatProperties.get(Minecraft.getMinecraft().thePlayer).doesSendClientCommands()) { // Send's command to server.
        		UnlimitedChat.PacketHandler.sendToServer(new PacketC02ClientCommand(msg));
        	}
        	return; // Do not send command as normal chat message
        }
        UnlimitedChat.PacketHandler.sendToServer(new PacketC01ChatMessage(msg)); // Sends the text to the server.
	}
}

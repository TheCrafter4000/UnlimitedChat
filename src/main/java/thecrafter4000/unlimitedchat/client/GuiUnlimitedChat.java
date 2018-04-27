package thecrafter4000.unlimitedchat.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiChat;
import net.minecraftforge.client.ClientCommandHandler;
import thecrafter4000.unlimitedchat.ClientProxy;
import thecrafter4000.unlimitedchat.UnlimitedChat;
import thecrafter4000.unlimitedchat.network.PacketC01ChatMessage;

/**
 * An extended version of the vanilla chat GUI.
 * @author TheCrafter4000
 */
@SideOnly(Side.CLIENT)
public class GuiUnlimitedChat extends GuiChat {

	public GuiUnlimitedChat(String text) {
		super(text); // Makes sure that the "/" hotkey is working.
		
		//TODO: Add a way to save previous typed texts and paste them.
	}
	
	@Override
	public void initGui() {
		super.initGui();
		this.inputField.setMaxStringLength(ClientProxy.ChatLimit); // Updates char limit.
	}

	@Override
	public void func_146403_a(String msg) {
        this.mc.ingameGUI.getChatGUI().addToSentMessages(msg); // Add's the message to our last-typed history
        if (ClientCommandHandler.instance.executeCommand(mc.thePlayer, msg) != 0) return; // Executes client-side commands
        UnlimitedChat.PacketHandler.sendToServer(new PacketC01ChatMessage(msg)); // Sends the text to the server.
        
        //TODO: Add a way for admins to see what client commands people execute.
	}
}

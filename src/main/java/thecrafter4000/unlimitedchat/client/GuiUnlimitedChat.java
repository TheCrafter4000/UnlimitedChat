package thecrafter4000.unlimitedchat.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiChat;
import net.minecraftforge.client.ClientCommandHandler;
import thecrafter4000.unlimitedchat.ClientProxy;
import thecrafter4000.unlimitedchat.UnlimitedChat;
import thecrafter4000.unlimitedchat.network.PacketC01ChatMessage;

@SideOnly(Side.CLIENT)
public class GuiUnlimitedChat extends GuiChat {

	public GuiUnlimitedChat(String text) {
		super(text);
	}
	
	@Override
	public void initGui() {
		super.initGui();
		this.inputField.setMaxStringLength(ClientProxy.ChatLimit);
	}

	@Override
	public void func_146403_a(String msg) {
        this.mc.ingameGUI.getChatGUI().addToSentMessages(msg); // Add's the message to our last-typed history
        if (ClientCommandHandler.instance.executeCommand(mc.thePlayer, msg) != 0) return; // Executes client-side commands
        UnlimitedChat.PacketHandler.sendToServer(new PacketC01ChatMessage(msg)); // Sends the text to the server.
        
        // Old code. Had been used in version stable-client-0.1.0
        
//        Matcher m = Pattern.compile("\\G\\s*(.{1,100})(?=\\s|$)", Pattern.DOTALL).matcher(msg); // Custom splitter
//        while (m.find()) {
//        	this.mc.thePlayer.sendChatMessage(m.group(1));
//        }
	}
}

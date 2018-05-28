package thecrafter4000.unlimitedchat.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import thecrafter4000.unlimitedchat.ClientProxy;
import thecrafter4000.unlimitedchat.UnlimitedChat;
import thecrafter4000.unlimitedchat.data.ChatProperties;

/**
 * A packet sent form server to client to configure his chat limitations
 * @author TheCrafter4000
 */
public class PacketS03ChatConfig implements IMessage {

	private int charlimit;
	private boolean clientCommands;

	public PacketS03ChatConfig() {}

	public PacketS03ChatConfig(ChatProperties properties) {
		this.charlimit = properties.charLimit;
		this.clientCommands = properties.sendClientCommands;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(charlimit);
		buf.writeBoolean(clientCommands);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		charlimit = buf.readInt();
		clientCommands = buf.readBoolean();
	}

	/**
	 * Client - sided handler for {@link PacketS03ChatConfig}
	 * @author TheCrafter4000
	 */
	public static class HandlerC03ChatConfig implements IMessageHandler<PacketS03ChatConfig, IMessage> {

		@Override
		public IMessage onMessage(PacketS03ChatConfig message, MessageContext ctx) {
			ClientProxy.config = new ChatProperties(message.charlimit, message.clientCommands);
			UnlimitedChat.Logger.info("Received chat configuration; Char limit is " + message.charlimit + ".");
			return null;
		}
	}
}

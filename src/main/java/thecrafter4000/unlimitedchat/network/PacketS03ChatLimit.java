package thecrafter4000.unlimitedchat.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import thecrafter4000.unlimitedchat.ClientProxy;
import thecrafter4000.unlimitedchat.UnlimitedChat;

/**
 * A packet sent form client to server containing the client's character limitation.
 * @author TheCrafter4000
 */
public class PacketS03ChatLimit implements IMessage {

	private int limit;
	
	public PacketS03ChatLimit() {}
	
	public PacketS03ChatLimit(int limit) {
		this.limit = limit;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(limit);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		limit = buf.readInt();
	}
	
	/**
	 * Client - sided handler for {@link PacketS03ChatLimit}.
	 * @author TheCrafter4000
	 */
	public static class HandlerC03ChatLimit implements IMessageHandler<PacketS03ChatLimit, IMessage>{

		@Override
		public IMessage onMessage(PacketS03ChatLimit message, MessageContext ctx) {
			ClientProxy.ChatLimit = message.limit;
			UnlimitedChat.Logger.info("Recieved chatlimit: " + message.limit + "!");
			return null;
		}
		
	}
}

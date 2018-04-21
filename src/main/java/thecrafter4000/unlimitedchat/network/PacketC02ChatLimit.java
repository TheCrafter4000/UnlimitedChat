package thecrafter4000.unlimitedchat.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import thecrafter4000.unlimitedchat.ClientProxy;
import thecrafter4000.unlimitedchat.UnlimitedChat;

public class PacketC02ChatLimit implements IMessage {

	private int limit;
	
	public PacketC02ChatLimit() {}
	
	public PacketC02ChatLimit(int limit) {
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
	
	public static class HandlerC02ChatLimit implements IMessageHandler<PacketC02ChatLimit, IMessage>{

		@Override
		public IMessage onMessage(PacketC02ChatLimit message, MessageContext ctx) {
			ClientProxy.ChatLimit = message.limit;
			UnlimitedChat.Logger.info("Recieved chatlimit: " + message.limit + "!");
			return null;
		}
		
	}
}

package thecrafter4000.unlimitedchat.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import thecrafter4000.unlimitedchat.ServerProxy;
import thecrafter4000.unlimitedchat.UnlimitedChat;

/**
 * Request packet send from client's to the server to get their char limits.
 * @author TheCrafter4000
 */
public class PacketC02RequestChatLimit implements IMessage {
	
	public PacketC02RequestChatLimit() {}
	
	@Override
	public void toBytes(ByteBuf buf) {}

	@Override
	public void fromBytes(ByteBuf buf) {}
	
	/**
	 * Server - sided handler for {@link PacketC02RequestChatLimit}.
	 * @author TheCrafter4000
	 */
	public static class HandlerS02RequestChatLimit implements IMessageHandler<PacketC02RequestChatLimit, PacketS03ChatLimit>{

		@Override
		public PacketS03ChatLimit onMessage(PacketC02RequestChatLimit message, MessageContext ctx) {
			EntityPlayerMP player = ctx.getServerHandler().playerEntity;
			UnlimitedChat.Logger.debug("Recieved chat limit request from " + player.getDisplayName() + "!");
			int limit = ServerProxy.getChatLimit(player);
			UnlimitedChat.Logger.debug("Chat limit for player " + player.getDisplayName() + " is " + limit + "!");
			return new PacketS03ChatLimit(limit);
		}
		
	}
}

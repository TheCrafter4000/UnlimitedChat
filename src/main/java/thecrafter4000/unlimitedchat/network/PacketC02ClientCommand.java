package thecrafter4000.unlimitedchat.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import thecrafter4000.unlimitedchat.ServerProxy;

/**
 * A packet sent form client to server if he performs an client command
 * @author TheCrafter4000
 */
public class PacketC02ClientCommand implements IMessage {

	private String text = "";
	
	public PacketC02ClientCommand() {}
	
	public PacketC02ClientCommand(String text) {
		this.text = text;
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, text);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		text = ByteBufUtils.readUTF8String(buf);
	}

	/**
	 * Server - sided handler for {@link PacketC02ClientCommand}
	 * @author TheCrafter4000
	 */
	public static class HandlerS02ClientCommand implements IMessageHandler<PacketC02ClientCommand, IMessage>{

		@Override
		public IMessage onMessage(PacketC02ClientCommand message, MessageContext ctx) {
			EntityPlayerMP sender = ctx.getServerHandler().playerEntity; // The person who wants to perform the command on client-side
			
			ChatComponentTranslation msg = new ChatComponentTranslation("chat.type.admin", new Object[] {"ClientCommand", new ChatComponentText(sender.getDisplayName() + " -> " + message.text)});
			msg.getChatStyle().setColor(EnumChatFormatting.GRAY);
	        msg.getChatStyle().setItalic(true);

			MinecraftServer.getServer().getConfigurationManager().playerEntityList.forEach( obj -> {  // All players on the server
				EntityPlayerMP p = (EntityPlayerMP) obj;
				if(ServerProxy.getProperties(p).canSeeClientCommands) {
					p.addChatMessage(msg);
				}
			});
			MinecraftServer.getServer().addChatMessage(msg); // Sends message to the console.
			return null;
		}
	}
}

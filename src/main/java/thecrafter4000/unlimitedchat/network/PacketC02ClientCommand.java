package thecrafter4000.unlimitedchat.network;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.forgeessentials.api.APIRegistry;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.ForgeHooks;
import thecrafter4000.unlimitedchat.CommonProxy;
import thecrafter4000.unlimitedchat.ServerProxy;
import thecrafter4000.unlimitedchat.UnlimitedChat;

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
			MinecraftServer.getServer().getConfigurationManager().playerEntityList.forEach( obj -> {
				EntityPlayerMP p = (EntityPlayerMP) obj; // All players on the server
				if(APIRegistry.perms.checkPermission(p, CommonProxy.PERM_SEECLIENTCOMMANDS)) { // Do not notify anybody.
					ChatComponentTranslation msg = new ChatComponentTranslation("chat.type.admin", new Object[] {"ClientCommand", new ChatComponentText("[" + sender.getDisplayName() + "] " + message.text)});
					msg.getChatStyle().setColor(EnumChatFormatting.GRAY);
			        msg.getChatStyle().setItalic(true);
					p.addChatMessage(msg);
				}
			});
			return null;
		}
	}
}

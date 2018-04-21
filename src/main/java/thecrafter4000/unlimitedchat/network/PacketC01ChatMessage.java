package thecrafter4000.unlimitedchat.network;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.ForgeHooks;
import thecrafter4000.unlimitedchat.CommonProxy;
import thecrafter4000.unlimitedchat.UnlimitedChat;

public class PacketC01ChatMessage implements IMessage {

	private String text = "";
	
	public PacketC01ChatMessage() {}
	
	public PacketC01ChatMessage(String text) {
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

	public static class HandlerC01ChatMessage implements IMessageHandler<PacketC01ChatMessage, IMessage>{

		@Override
		public IMessage onMessage(PacketC01ChatMessage message, MessageContext ctx) {
			try {
				this.processChatMessage(ctx.getServerHandler(), message.text);
			} catch (Exception e) {
				UnlimitedChat.Logger.fatal("Reflection error! Report this to the mod author!", e);
			}
			return null;
		}
		
		private void processChatMessage(NetHandlerPlayServer nhps, String text) throws Exception {
	        if (nhps.playerEntity.func_147096_v() == EntityPlayer.EnumChatVisibility.HIDDEN) // Copied from vanilla. I have no clue what's this code about.
	        {
	            ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("chat.cannotSend", new Object[0]);
	            chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.RED);
	            nhps.sendPacket(new S02PacketChat(chatcomponenttranslation));
	        }
	        else
	        {
	        	if(text.length() > CommonProxy.getChatLimitServer(nhps.playerEntity)) {
	        		nhps.kickPlayerFromServer("Illegal text lenght!");
	        	}
	        	
	        	nhps.playerEntity.func_143004_u(); // Reset's idle timer.

	            for (int i = 0; i < text.length(); ++i) // Check's for forbidden chars.
	            {
	                if (!ChatAllowedCharacters.isAllowedCharacter(text.charAt(i)))
	                {
	                    nhps.kickPlayerFromServer("Illegal characters in chat");
	                    return;
	                }
	            }

	            if (text.startsWith("/"))
	            {
	                this.executeCommand().invoke(nhps, text);
	            }
	            else
	            {
	                ChatComponentTranslation chatcomponenttranslation1 = new ChatComponentTranslation("chat.type.text", new Object[] {nhps.playerEntity.func_145748_c_(), ForgeHooks.newChatWithLinks(text)}); // Fixes chat links
	                chatcomponenttranslation1 = ForgeHooks.onServerChatEvent(nhps, text, chatcomponenttranslation1);
	                if (chatcomponenttranslation1 == null) return;
	                MinecraftServer.getServer().getConfigurationManager().sendChatMsgImpl(chatcomponenttranslation1, false); // Send's the message to all clients
	            }

	            if(!CommonProxy.getIgnoreSpam(nhps.playerEntity)) { // Disables spam check for op's
	            	System.out.println("Spam check is active");
		            int oldChatSpamThreshold = (Integer) this.getChatSpamThreshold().get(nhps); // saves the old value. It makes sure nobody gets kicked after posting the first message.
		            this.getChatSpamThreshold().set(nhps, oldChatSpamThreshold + Math.max((text.length()/100)*40, 20)); // nhps.chatSpamThresholdCount += 20;
		            
		            if (((Integer) this.getChatSpamThreshold().get(nhps)) > 200 && oldChatSpamThreshold != 0) {// Kick after too many traffic, but never after the first message
		            	nhps.kickPlayerFromServer("disconnect.spam");
		            }
	            }
	        }
		}
		
		private Field getChatSpamThreshold() throws NoSuchFieldException {
			Field f;
			try {
				f = NetHandlerPlayServer.class.getDeclaredField("field_147374_l");
			}catch(NoSuchFieldException e) {
				f = NetHandlerPlayServer.class.getDeclaredField("chatSpamThresholdCount");
			}
			f.setAccessible(true);
			return f;
		}
		
		private Method executeCommand() throws NoSuchMethodException {
			Method m;
			try {
				m = NetHandlerPlayServer.class.getDeclaredMethod("func_147361_d", String.class);
			}catch(NoSuchMethodException e) {
				m = NetHandlerPlayServer.class.getDeclaredMethod("handleSlashCommand", String.class);
			}
			m.setAccessible(true);
			return m;
		}

	}
}

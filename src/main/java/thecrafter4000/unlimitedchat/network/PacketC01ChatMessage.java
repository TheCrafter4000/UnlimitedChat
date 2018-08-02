package thecrafter4000.unlimitedchat.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.*;
import net.minecraftforge.common.ForgeHooks;
import thecrafter4000.unlimitedchat.ServerProxy;
import thecrafter4000.unlimitedchat.UnlimitedChat;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A packet sent form client to server containing a chat message.
 * @author TheCrafter4000
 */
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

	/**
	 * Server - sided handler for {@link PacketC01ChatMessage}
	 * @author TheCrafter4000
	 */
	public static class HandlerS01ChatMessage implements IMessageHandler<PacketC01ChatMessage, IMessage>{

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
			
			// Prevent chatting while chat gui is not open.
			if (nhps.playerEntity.func_147096_v() == EntityPlayer.EnumChatVisibility.HIDDEN) { 
				ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("chat.cannotSend");
				chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.RED);
				nhps.sendPacket(new S02PacketChat(chatcomponenttranslation));
				return;
			} 
			
			// Checks for matching char limit.
			if(text.length() > ServerProxy.getProperties(nhps.playerEntity).charLimit) {
				nhps.kickPlayerFromServer("Illegal text length!");
				return;
			}

			 // Reset's idle timer.
			nhps.playerEntity.func_143004_u();

			// Check's for forbidden characters.
			for (int i = 0; i < text.length(); ++i) {
				if (!ChatAllowedCharacters.isAllowedCharacter(text.charAt(i))) {
					nhps.kickPlayerFromServer("Illegal characters in chat!");
					return;
				}
			}

			if (text.startsWith("/")) {
				this.executeCommand().invoke(nhps, text);
			}
			else {
				ChatComponentTranslation chatComponentTranslation = new ChatComponentTranslation("chat.type.text", nhps.playerEntity.func_145748_c_(), newChatWithLinks(text)); // Fixes chat links
				chatComponentTranslation = ForgeHooks.onServerChatEvent(nhps, text, chatComponentTranslation);
				if (chatComponentTranslation == null) return;
				MinecraftServer.getServer().getConfigurationManager().sendChatMsgImpl(chatComponentTranslation, false); // Send's the message to all clients
			}

			if(!ServerProxy.getProperties(nhps.playerEntity).ignoreSpamCheck) { // Disables spam check
				Field chatSpamThreshold = getChatSpamThreshold();
				int oldChatSpamThreshold = (Integer) chatSpamThreshold.get(nhps); // saves the old value. It makes sure nobody gets kicked after posting the first message.
				chatSpamThreshold.set(nhps, oldChatSpamThreshold + Math.max((text.length()/100)*40, 20)); // nhps.chatSpamThresholdCount += 20;

				if (((Integer) chatSpamThreshold.get(nhps)) > 200 && oldChatSpamThreshold != 0) {// Kick after too many traffic, but never after the first message
					nhps.kickPlayerFromServer("disconnect.spam");
				}
			}
		}
		
		/** Internal reflection method */
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
		
		/** Internal reflection method */
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

		// Forge fix - Original see ForgeHooks#newChatWithLinks(String)

		// Includes ipv4 and domain pattern
		// Matches an ip (xx.xxx.xx.xxx) or a domain (something.com) with or
		// without a protocol or path.
		private static final Pattern URL_PATTERN = Pattern.compile(
				//         schema                          ipv4            OR           namespace                 port     path         ends
				//   |-----------------|        |-------------------------|  |----------------------------|    |---------| |--|   |---------------|
				"((?:[a-z0-9]{2,}://)?(?:(?:[0-9]{1,3}\\.){3}[0-9]{1,3}|(?:[-\\w_.]+\\.[a-z]{2,}?))(?::[0-9]{1,5})?.*?(?=[!\"\u00A7 \n]|$))",
				Pattern.CASE_INSENSITIVE);

		public static IChatComponent newChatWithLinks(String string) {
			IChatComponent chatComponent = new ChatComponentText("");
			Matcher matcher = URL_PATTERN.matcher(string);
			int lastEnd = 0;

			// Find all urls
			while (matcher.find()) {
				int start = matcher.start();
				int end = matcher.end();

				// Append the previous left overs.
				chatComponent.appendText(string.substring(lastEnd, start));
				lastEnd = end;
				String url = string.substring(start, end);
				IChatComponent link = new ChatComponentText(url);

				try{
					// Add schema so client doesn't crash.
					if (URI.create(url).getScheme() == null) {
						url = "http://" + url;
					}
				}catch (IllegalArgumentException ignored){} // Catch exception in case of invalid url, e.g. xx.xx.xx.xx:xxxxx

				// Set the click event and append the link.
				ClickEvent click = new ClickEvent(ClickEvent.Action.OPEN_URL, url);
				link.getChatStyle().setChatClickEvent(click);
				chatComponent.appendSibling(link);
			}

			// Append the rest of the message.
			chatComponent.appendText(string.substring(lastEnd));
			return chatComponent;
		}
	}
}

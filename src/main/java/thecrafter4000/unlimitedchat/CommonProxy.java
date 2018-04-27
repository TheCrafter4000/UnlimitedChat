package thecrafter4000.unlimitedchat;

import static thecrafter4000.unlimitedchat.UnlimitedChat.PERM_CHARLIMIT;
import static thecrafter4000.unlimitedchat.UnlimitedChat.PERM_IGNORESPAM;
import static thecrafter4000.unlimitedchat.UnlimitedChat.PacketHandler;

import com.forgeessentials.api.APIRegistry;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.permission.PermissionLevel;
import thecrafter4000.unlimitedchat.network.PacketC01ChatMessage;
import thecrafter4000.unlimitedchat.network.PacketC01ChatMessage.HandlerS01ChatMessage;
import thecrafter4000.unlimitedchat.network.PacketC02RequestChatLimit;
import thecrafter4000.unlimitedchat.network.PacketC02RequestChatLimit.HandlerS02RequestChatLimit;
import thecrafter4000.unlimitedchat.network.PacketS03ChatLimit;
import thecrafter4000.unlimitedchat.network.PacketS03ChatLimit.HandlerC03ChatLimit;

/**
 * Proxy for both sides. 
 * @author TheCrafter4000
 */
public class CommonProxy {

	/** True if Forgeessentials is detected. */
	public static boolean ForgeEssentialsSupport = false;
	
	public void preInit(FMLPreInitializationEvent event) {
		// Register network stuff
		PacketHandler.registerMessage(HandlerS01ChatMessage.class, PacketC01ChatMessage.class, 1, Side.SERVER);
		PacketHandler.registerMessage(HandlerS02RequestChatLimit.class, PacketC02RequestChatLimit.class, 2, Side.SERVER);
		PacketHandler.registerMessage(HandlerC03ChatLimit.class, PacketS03ChatLimit.class, 3, Side.CLIENT);
		
		// All proxies can handle events now.
		FMLCommonHandler.instance().bus().register(this);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	public void init(FMLInitializationEvent event) {}
	
	public void postInit(FMLPostInitializationEvent event) {
		if(Loader.isModLoaded("ForgeEssentials")) { // Checks for ForgeEssentials.
			ForgeEssentialsSupport = true;
			APIRegistry.perms.registerPermissionProperty(PERM_CHARLIMIT, "100", "The charlimit a player's message cannot exceed. ~Max: 32767");
			APIRegistry.perms.registerPermission(PERM_IGNORESPAM, PermissionLevel.OP);
			UnlimitedChat.Logger.info("Enabled ForgeEssentials support.");
		}
	}	
	
	/**
	 * Server side only. Used to send the char limit to all clients.
	 */
	@SubscribeEvent
	public void onPlayerJoinEvent(PlayerLoggedInEvent event) {
		EntityPlayerMP player = (EntityPlayerMP) event.player;
		int limit = ServerProxy.getChatLimit(player);
		UnlimitedChat.PacketHandler.sendTo(new PacketS03ChatLimit(limit), player);
		UnlimitedChat.Logger.debug("Sent chatlimit to " + player.getDisplayName() + ": " + limit);
	}
	
	// Don't like the following...
	
	/** Server side only */
	public static int getChatLimit(EntityPlayerMP player) {
		if(!ForgeEssentialsSupport) { // Makes sure Client's without ForgeEssentials can work in SP
			return 32767;
		}
		
		String value = APIRegistry.perms.getPermissionProperty(player, PERM_CHARLIMIT);
		if(value != null) {
			return Integer.valueOf(value);
		}
		return 100;
	}
	
	/** Server side only */
	public static boolean getIgnoreSpam(EntityPlayerMP player) {
		if(!ForgeEssentialsSupport) { // Makes sure Client's without ForgeEssentials can work in SP
			return true;
		}
		return APIRegistry.perms.checkPermission(player, UnlimitedChat.PERM_IGNORESPAM);
	}
	

}

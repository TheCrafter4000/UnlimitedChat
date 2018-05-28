package thecrafter4000.unlimitedchat;

import com.forgeessentials.api.APIRegistry;
import com.forgeessentials.api.permissions.PermissionEvent.BeforeSave;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.permission.PermissionLevel;
import thecrafter4000.unlimitedchat.data.ChatProperties;
import thecrafter4000.unlimitedchat.network.PacketS03ChatConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ServerProxy extends CommonProxy {

	public static final String PERM_CHARLIMIT = "cu.charlimit";
	public static final String PERM_IGNORESPAM = "cu.ignorespam";
	public static final String PERM_SENDCLIENTCOMMANDS = "cu.clientcommands.send";
	public static final String PERM_SEECLIENTCOMMANDS = "cu.clientcommands.read";
	
	/** True if ForgeEssentials had been detected. */
	public static boolean ForgeEssentialsSupport = false;

	/** Internal buffer. Reduces access to FE methods */
	private static Map<UUID, ChatProperties> buffer = new HashMap<>();
	
	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
		if(Loader.isModLoaded("ForgeEssentials")) { // Checks for ForgeEssentials.
			ForgeEssentialsSupport = true;
			APIRegistry.perms.registerPermissionProperty(PERM_CHARLIMIT, "100", "The character limit a player's message cannot exceed. ~Max: 32767");
			APIRegistry.perms.registerPermission(PERM_IGNORESPAM, PermissionLevel.OP);
			APIRegistry.perms.registerPermission(PERM_SEECLIENTCOMMANDS, PermissionLevel.OP);
			APIRegistry.perms.registerPermission(PERM_SENDCLIENTCOMMANDS, PermissionLevel.FALSE);
			APIRegistry.FE_EVENTBUS.register(this);
			UnlimitedChat.Logger.info("Detected ForgeEssentials.");
		}else {
			UnlimitedChat.Logger.warn("Did not detect ForgeEssentials! This is highly unrecommended!");
		}
	}
	
	/** Updates properties if something changed. */
	@SubscribeEvent
	@Optional.Method(modid = "ForgeEssentials")
	public void onPermissionUpdate(BeforeSave event) {
		buffer.clear();
		MinecraftServer.getServer().getConfigurationManager().playerEntityList.forEach( p -> updatePlayer((EntityPlayerMP) p));
	}

	@SubscribeEvent
	public void onPlayerJoinEvent(PlayerEvent.PlayerLoggedInEvent event) {
		updatePlayer((EntityPlayerMP) event.player);
	}

	private void updatePlayer(EntityPlayerMP player){
		UnlimitedChat.PacketHandler.sendTo(new PacketS03ChatConfig(getProperties(player)), player);
	}

	public static ChatProperties getProperties(EntityPlayerMP player){
		UUID uuid = player.getUniqueID();
		if(!buffer.containsKey(uuid)){
			buffer.put(uuid, new ChatProperties(getChatLimit(player), shouldSendClientCommands(player), canSeeClientCommands(player), shouldIgnoreSpam(player)));
		}
		return buffer.get(uuid);
	}

	// Internal helper functions.

	private static int getChatLimit(EntityPlayerMP player) {
		if(!ForgeEssentialsSupport) {
			return 32767;
		}
		
		String value = APIRegistry.perms.getPermissionProperty(player, PERM_CHARLIMIT);
		if(value != null) {
			try {
				return Integer.valueOf(value);
			} catch(NumberFormatException e) {
				UnlimitedChat.Logger.fatal("Invalid permission value " + '"' + value + '"' + " for player " + player.getDisplayName() + "!");
				UnlimitedChat.Logger.fatal("Please fix your FE permission configuration.");
				e.printStackTrace();
			}
		}
		//Should never happen.
		return 100;
	}

	private static boolean shouldIgnoreSpam(EntityPlayerMP player) {
		if(!ForgeEssentialsSupport) {
			return false;
		}
		return APIRegistry.perms.checkPermission(player, PERM_IGNORESPAM);
	}

	private static boolean shouldSendClientCommands(EntityPlayerMP player) {
		if(!ForgeEssentialsSupport) {
			return false;
		}
		return APIRegistry.perms.checkPermission(player, PERM_SENDCLIENTCOMMANDS);
	}

	private static boolean canSeeClientCommands(EntityPlayerMP player) {
		if(!ForgeEssentialsSupport) {
			return true;
		}

		return APIRegistry.perms.checkPermission(player, PERM_SEECLIENTCOMMANDS);
	}
}
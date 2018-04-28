package thecrafter4000.unlimitedchat;

import com.forgeessentials.api.APIRegistry;
import com.forgeessentials.api.permissions.PermissionEvent;
import com.forgeessentials.api.permissions.PermissionEvent.BeforeSave;
import com.forgeessentials.api.permissions.PermissionEvent.Group.ModifyPermission;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.permission.PermissionLevel;
import thecrafter4000.unlimitedchat.data.ChatProperties;

public class ServerProxy extends CommonProxy {

	public static final String PERM_CHARLIMIT = "cu.charlimit";
	public static final String PERM_IGNORESPAM = "cu.ignorespam";
	public static final String PERM_SENDCLIENTCOMMANDS = "cu.clientcommands.send";
	public static final String PERM_SEECLIENTCOMMANDS = "cu.clientcommands.read";
	
	/** True if ForgeEssentials had been detected. */
	public static boolean ForgeEssentialsSupport = false;
	
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
	
	/** Reloads chatlimit if something changed. */
	@SubscribeEvent
	public void onPermissionUpdate(PermissionEvent.BeforeSave event) {
		MinecraftServer.getServer().getConfigurationManager().playerEntityList.forEach( obj -> ChatProperties.get((EntityPlayer) obj).load());
	}
	
	public static int getChatLimit(EntityPlayerMP player) {
		if(!ForgeEssentialsSupport) { // Makes sure Client's without ForgeEssentials can work in SP
			return 32767;
		}
		
		String value = APIRegistry.perms.getPermissionProperty(player, PERM_CHARLIMIT);
		if(value != null) {
			try {
				return Integer.valueOf(value);
			} catch(NumberFormatException e) { // Sadly can't fix that myself, I don't know what group is causing the error.
				UnlimitedChat.Logger.fatal("Invalid permission value: " + value);
				UnlimitedChat.Logger.fatal("Change it to an number instead.");
			}
		}
		return 100;
	}
	
	public static boolean getIgnoreSpam(EntityPlayerMP player) {
		if(!ForgeEssentialsSupport) { // Makes sure Client's without ForgeEssentials can work in SP
			return true;
		}
		return APIRegistry.perms.checkPermission(player, PERM_IGNORESPAM);
	}
	
	public static boolean shouldSendClientCommands(EntityPlayerMP player) {
		if(!ForgeEssentialsSupport) { // Makes sure Client's without ForgeEssentials can work in SP
			return false;
		}
		return APIRegistry.perms.checkPermission(player, PERM_SENDCLIENTCOMMANDS);
	}
	
	public static boolean canSeeClientCommands(EntityPlayerMP player) {
		if(!ForgeEssentialsSupport) { // Makes sure Client's without ForgeEssentials can work in SP
			return true;
		}

		return APIRegistry.perms.checkPermission(player, PERM_SEECLIENTCOMMANDS);
	}
}
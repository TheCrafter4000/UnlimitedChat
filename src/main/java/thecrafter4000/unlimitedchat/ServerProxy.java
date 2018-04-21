package thecrafter4000.unlimitedchat;

import static thecrafter4000.unlimitedchat.UnlimitedChat.PERM_CHARLIMIT;
import static thecrafter4000.unlimitedchat.UnlimitedChat.PERM_IGNORESPAM;

import com.forgeessentials.api.APIRegistry;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.permission.PermissionLevel;

public class ServerProxy extends CommonProxy {

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		
		APIRegistry.perms.registerPermissionProperty(PERM_CHARLIMIT, "100", "The charlimit a player's message cannot exceed. ~Max: 32767");
		APIRegistry.perms.registerPermission(PERM_IGNORESPAM, PermissionLevel.OP);
	}
}

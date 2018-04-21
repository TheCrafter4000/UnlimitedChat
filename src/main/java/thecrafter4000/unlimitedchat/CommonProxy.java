package thecrafter4000.unlimitedchat;

import static thecrafter4000.unlimitedchat.UnlimitedChat.PERM_CHARLIMIT;
import static thecrafter4000.unlimitedchat.UnlimitedChat.PacketHandler;

import com.forgeessentials.api.APIRegistry;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayerMP;
import thecrafter4000.unlimitedchat.network.PacketC01ChatMessage;
import thecrafter4000.unlimitedchat.network.PacketC02ChatLimit;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		PacketHandler.registerMessage(PacketC01ChatMessage.HandlerC01ChatMessage.class, PacketC01ChatMessage.class, 0, Side.SERVER);
		PacketHandler.registerMessage(PacketC02ChatLimit.HandlerC02ChatLimit.class, PacketC02ChatLimit.class, 1, Side.CLIENT);
		
		FMLCommonHandler.instance().bus().register(this);
	}
	
	public void init(FMLInitializationEvent event) {}
	
	public void postInit(FMLPostInitializationEvent event) {}
	
	@SubscribeEvent
	public void onPlayerJoin(PlayerLoggedInEvent event) {
		EntityPlayerMP player = (EntityPlayerMP) event.player;
		PacketHandler.sendTo(new PacketC02ChatLimit(getChatLimitServer(player)), player);
	}

	public static int getChatLimitServer(EntityPlayerMP player) {
		if(!player.mcServer.isDedicatedServer()) { // Makes sure Client's without ForgeEssentials can work in SP
			return 32767;
		}
		
		String value = APIRegistry.perms.getPermissionProperty(player, PERM_CHARLIMIT);
		if(value != null) {
			return Integer.valueOf(value);
		}
		return 100;
	}
	
	public static boolean getIgnoreSpam(EntityPlayerMP player) {
		if(!player.mcServer.isDedicatedServer()) { // Makes sure Client's without ForgeEssentials can work in SP
			return true;
		}
		return APIRegistry.perms.checkPermission(player, UnlimitedChat.PERM_IGNORESPAM);
	}
}

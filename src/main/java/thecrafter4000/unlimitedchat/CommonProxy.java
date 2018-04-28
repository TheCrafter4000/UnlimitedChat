package thecrafter4000.unlimitedchat;

import static thecrafter4000.unlimitedchat.UnlimitedChat.PacketHandler;

import java.util.List;
import java.util.Map;

import com.forgeessentials.api.APIRegistry;
import com.forgeessentials.api.permissions.PermissionEvent;
import com.google.common.collect.Lists;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.permission.PermissionLevel;
import thecrafter4000.unlimitedchat.data.ChatProperties;
import thecrafter4000.unlimitedchat.network.PacketC01ChatMessage;
import thecrafter4000.unlimitedchat.network.PacketC02ClientCommand;
import thecrafter4000.unlimitedchat.network.PacketC01ChatMessage.HandlerS01ChatMessage;
import thecrafter4000.unlimitedchat.network.PacketC02ClientCommand.HandlerS02ClientCommand;

/**
 * Proxy for both sides. 
 * @author TheCrafter4000
 */
public class CommonProxy {
	
	public void preInit(FMLPreInitializationEvent event) {
		// Register network stuff
		PacketHandler.registerMessage(HandlerS01ChatMessage.class, PacketC01ChatMessage.class, 1, Side.SERVER);
		PacketHandler.registerMessage(HandlerS02ClientCommand.class, PacketC02ClientCommand.class, 2, Side.SERVER);
		
		FMLCommonHandler.instance().bus().register(this);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	public void init(FMLInitializationEvent event) {}
	
	public void postInit(FMLPostInitializationEvent event) {
//		ClientCommandHandler.instance.registerCommand(new ICommand() {			
//			@Override
//			public int compareTo(Object arg0) {
//				return 0;
//			}
//			
//			@Override
//			public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) {
//				UnlimitedChat.Logger.info("Dew it!");
//				
//			}
//			
//			@Override
//			public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
//				return false;
//			}
//			
//			@Override
//			public String getCommandUsage(ICommandSender p_71518_1_) {
//				return "";
//			}
//			
//			@Override
//			public String getCommandName() {
//				return "cctest";
//			}
//			
//			@Override
//			public List getCommandAliases() {
//				return Lists.newArrayList();
//			}
//			
//			@Override
//			public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {
//				return true;
//			}
//			
//			@Override
//			public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_) {
//				return Lists.newArrayList();
//			}
//		});
	}	
	
	/**
	 * Registers the {@link IExtendedEntityProperties}
	 */
	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event) {
		if (event.entity instanceof EntityPlayer && ChatProperties.get((EntityPlayer) event.entity) == null) {
			ChatProperties.register((EntityPlayer) event.entity);
		}
	}
		
	/** Only called server side */
	@SubscribeEvent
	public void onPlayerJoinEvent(PlayerLoggedInEvent event) {
		ChatProperties.get(event.player).load();
	}
}

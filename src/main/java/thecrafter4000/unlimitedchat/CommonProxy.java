package thecrafter4000.unlimitedchat;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraftforge.common.MinecraftForge;
import thecrafter4000.unlimitedchat.network.PacketC01ChatMessage;
import thecrafter4000.unlimitedchat.network.PacketC02ClientCommand;
import thecrafter4000.unlimitedchat.network.PacketS03ChatConfig;

import static thecrafter4000.unlimitedchat.UnlimitedChat.PacketHandler;

/**
 * Proxy for both sides. 
 * @author TheCrafter4000
 */
public class CommonProxy {
	
	public void preInit(FMLPreInitializationEvent event) {
		// Register network stuff
		PacketHandler.registerMessage(PacketC01ChatMessage.HandlerS01ChatMessage.class, PacketC01ChatMessage.class, 1, Side.SERVER);
		PacketHandler.registerMessage(PacketC02ClientCommand.HandlerS02ClientCommand.class, PacketC02ClientCommand.class, 2, Side.SERVER);
		PacketHandler.registerMessage(PacketS03ChatConfig.HandlerC03ChatConfig.class, PacketS03ChatConfig.class, 3, Side.CLIENT);
		
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
}

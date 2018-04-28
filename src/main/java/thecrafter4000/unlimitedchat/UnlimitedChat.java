package thecrafter4000.unlimitedchat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;

/**
 * UnlimitedChat, a mod by TheCrafter4000, removes the annoying char limitation vanilla chat has.
 * @author TheCrafter4000
 */
@Mod(modid = UnlimitedChat.MODID, version = UnlimitedChat.VERSION, name = UnlimitedChat.NAME, acceptedMinecraftVersions = "1.7.10" )
public class UnlimitedChat {
	public static final String MODID = "unlimitedchat";
	public static final String VERSION = "1.0.5.1";
	public static final String NAME = "UnlimitedChat";
	
	@Instance
	public static UnlimitedChat INSTANCE = new UnlimitedChat();
	@SidedProxy(clientSide="thecrafter4000.unlimitedchat.ClientProxy", serverSide="thecrafter4000.unlimitedchat.ServerProxy")
	public static CommonProxy Proxy;
	public static Logger Logger = LogManager.getLogger(NAME);
	public static SimpleNetworkWrapper PacketHandler = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Proxy.preInit(event);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		Proxy.init(event);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		Proxy.postInit(event);
	}
}

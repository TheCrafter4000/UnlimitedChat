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

@Mod(modid = UnlimitedChat.MODID, version = UnlimitedChat.VERSION, name = UnlimitedChat.NAME, acceptedMinecraftVersions = "1.7.10" )
public class UnlimitedChat {
	public static final String MODID = "unlimitedchat";
	public static final String VERSION = "1.0.3";
	public static final String NAME = "UnlimitedChat";
	
	public static final String PERM_CHARLIMIT = "cu.charlimit";
	public static final String PERM_IGNORESPAM = "cu.ignorespam";
	
	@Instance
	public static UnlimitedChat INSTANCE = new UnlimitedChat();
	@SidedProxy(clientSide="thecrafter4000.unlimitedchat.ClientProxy", serverSide="thecrafter4000.unlimitedchat.ServerProxy")
	public static CommonProxy Proxy;
	public static Logger Logger = LogManager.getLogger(MODID);
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

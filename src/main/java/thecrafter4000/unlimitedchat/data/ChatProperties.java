package thecrafter4000.unlimitedchat.data;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import thecrafter4000.unlimitedchat.ServerProxy;

/**
 * {@link IExtendedEntityProperties} implementation holding all important client data. 
 * Any data in this class can be modified by clients - do NOT trust anything in this on server.
 * If you HAVE to access stored data, make sure to reload it beforehand, or access it directly. 
 * @author TheCrafter4000
 */
public class ChatProperties implements IExtendedEntityProperties {

	public static final String EXT_PROP_NAME = "ChatProperties";
	private static final int ID_CHARLIMIT = 20;
	private static final int ID_COMMANDS = 21;
	
	/** Player instance */
	private final EntityPlayer player;
	
	public ChatProperties(EntityPlayer player) {
		this.player = player;
		
		this.player.getDataWatcher().addObject(ID_CHARLIMIT, 100);
		this.player.getDataWatcher().addObject(ID_COMMANDS, 0); // Boolean
	}
	
	/**
	 * Load's stored data on server. 
	 */
	public void load() {
		this.setCharlimit(ServerProxy.getChatLimit((EntityPlayerMP) player));
		this.setSendClientCommands(ServerProxy.shouldSendClientCommands((EntityPlayerMP) player));
	}
	
	// Helper functions
	
	public static void register(EntityPlayer player) {
		player.registerExtendedProperties(EXT_PROP_NAME, new ChatProperties(player));
	}
	
	public static final ChatProperties get(EntityPlayer player) {
		return (ChatProperties) player.getExtendedProperties(EXT_PROP_NAME);
	}
	
	// Getter/Setter
	
	public int getCharlimit() {
		return this.player.getDataWatcher().getWatchableObjectInt(ID_CHARLIMIT);
	}

	public void setCharlimit(int charlimit) {
		this.player.getDataWatcher().updateObject(ID_CHARLIMIT, charlimit);
	}

	public boolean doesSendClientCommands() {
		return this.player.getDataWatcher().getWatchableObjectInt(ID_COMMANDS) == 1;
	}

	public void setSendClientCommands(boolean sendClientCommands) {
		this.player.getDataWatcher().updateObject(ID_COMMANDS, sendClientCommands ? 1 : 0);
	}

	// We don't want to save our client data. 
	
	@Override
	public void saveNBTData(NBTTagCompound compound) {}

	@Override
	public void loadNBTData(NBTTagCompound compound) {}

	@Override
	public void init(Entity entity, World world) {}
}

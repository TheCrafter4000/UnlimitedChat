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
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.permission.PermissionLevel;
import thecrafter4000.unlimitedchat.data.ChatProperties;

public class ServerProxy extends CommonProxy {}
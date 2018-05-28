package thecrafter4000.unlimitedchat.data;

/**
 * An internal storage class for chat data; Used {@link net.minecraftforge.common.IExtendedEntityProperties} in the past, but it won't match the requirement that this mod shouldn't do anything if only on client-side.
 */
public class ChatProperties {
	public int charLimit;
	public boolean sendClientCommands;

	/** Client side only */
	public boolean modEnabled;

	/** Server side only */
	public boolean canSeeClientCommands;
	/** Server side only */
	public boolean ignoreSpamCheck;

	/** Client side only. Used when the mod is not on the server. */
	public ChatProperties() {
		this.charLimit = 100;
		this.sendClientCommands = false;
		this.modEnabled = false;
	}

	/** Client side only. Uses when chat configuration is received*/
	public ChatProperties(int charLimit, boolean sendClientCommands) {
		this.charLimit = charLimit;
		this.sendClientCommands = sendClientCommands;
		this.modEnabled = true;
	}

	/** Server side only */
	public ChatProperties(int charLimit, boolean sendClientCommands, boolean canSeeClientCommands, boolean ignoreSpamCheck) {
		this.charLimit = charLimit;
		this.sendClientCommands = sendClientCommands;
		this.canSeeClientCommands = canSeeClientCommands;
		this.ignoreSpamCheck = ignoreSpamCheck;
	}
}

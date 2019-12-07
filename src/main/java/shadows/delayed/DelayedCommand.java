package shadows.delayed;

import net.minecraft.nbt.NBTTagCompound;

public class DelayedCommand {

	int ticksExisted = 0;
	final int ticks;
	final String cmd;

	DelayedCommand(int ticks, String cmd) {
		this.ticks = ticks;
		this.cmd = cmd;
	}

	public NBTTagCompound write() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("ticks", ticks);
		tag.setInteger("ticksE", ticksExisted);
		tag.setString("cmd", cmd);
		return tag;
	}

	public static DelayedCommand read(NBTTagCompound tag) {
		int ticks = tag.getInteger("ticks");
		int ticksE = tag.getInteger("ticksE");
		String cmd = tag.getString("cmd");
		DelayedCommand dCmd = new DelayedCommand(ticks, cmd);
		dCmd.ticksExisted = ticksE;
		return dCmd;
	}

}

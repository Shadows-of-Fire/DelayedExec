package shadows.delayed;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants.NBT;

public class DelayedData extends WorldSavedData {

	private List<DelayedCommand> commands = new ArrayList<>();

	public DelayedData() {
		super("DelayedExec");
	}

	public DelayedData(String s) {
		super(s);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		commands.clear();
		NBTTagList cmds = tag.getTagList("commands", NBT.TAG_COMPOUND);
		for (NBTBase nbt : cmds) {
			add(DelayedCommand.read((NBTTagCompound) nbt));
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		NBTTagList cmds = new NBTTagList();
		for (DelayedCommand dc : commands) {
			cmds.appendTag(dc.write());
		}
		tag.setTag("commands", cmds);
		return tag;
	}

	public void add(DelayedCommand cmd) {
		commands.add(cmd);
		markDirty();
	}

	public void remove(DelayedCommand cmd) {
		commands.remove(cmd);
		markDirty();
	}

	public void removeAll(List<DelayedCommand> cmds) {
		commands.removeAll(cmds);
		markDirty();
	}

	public List<DelayedCommand> getCommands() {
		return commands;
	}

	public static DelayedData get(World world) {
		MapStorage storage = world.getPerWorldStorage();
		DelayedData instance = (DelayedData) storage.getOrLoadData(DelayedData.class, "DelayedExec");

		if (instance == null) {
			instance = new DelayedData();
			storage.setData("DelayedExec", instance);
		}
		return instance;
	}

}

package shadows.delayed;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;

@Mod(modid = DelayedExec.MODID, name = DelayedExec.MODNAME, version = DelayedExec.VERSION, acceptableRemoteVersions = "*")
public class DelayedExec {

	public static final String MODID = "delayedex";
	public static final String MODNAME = "Delayed Exec";
	public static final String VERSION = "1.0.0";
	public static DelayedData storage;

	@EventHandler
	public void starting(FMLServerStartingEvent e) {
		e.registerServerCommand(new CommandDelay());
		MinecraftForge.EVENT_BUS.register(this);
		storage = DelayedData.get(e.getServer().worlds[0]);
	}

	@EventHandler
	public void starting(FMLServerStoppedEvent e) {
		storage = null;
	}

	@SubscribeEvent
	public void serverTick(ServerTickEvent e) {
		if (e.phase == TickEvent.Phase.START) {
			MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
			List<DelayedCommand> done = new ArrayList<>(2);
			for (DelayedCommand c : storage.getCommands()) {
				c.ticksExisted++;
				if (c.ticksExisted >= c.ticks) {
					done.add(c);
					server.commandManager.executeCommand(server, c.cmd);
				}
			}
			if (!done.isEmpty()) storage.removeAll(done);
		}
	}

}
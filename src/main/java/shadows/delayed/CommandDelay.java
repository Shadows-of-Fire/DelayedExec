package shadows.delayed;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CommandDelay extends CommandBase {

	@Override
	public String getName() {
		return "delay_exec";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/delay_exec <ticks> <command>";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length >= 2) {
			try {
				int time = parseInt(args[0]);
				String[] realCmd = dropFirstString(args);
				String rawCmd = "/";
				for (String s : realCmd)
					rawCmd += s + " ";
				rawCmd = rawCmd.trim();
				DelayedExec.storage.add(new DelayedCommand(time, rawCmd));
				sender.sendMessage(new TextComponentString(String.format("Scheduled %s for execution in %s ticks.", rawCmd, time)));
			} catch (IndexOutOfBoundsException ex) {
				throw new SyntaxErrorException();
			}
		} else {
			throw new WrongUsageException(getUsage(sender));
		}
	}

	private static String[] dropFirstString(String[] input) {
		String[] astring = new String[input.length - 1];
		System.arraycopy(input, 1, astring, 0, input.length - 1);
		return astring;
	}

}

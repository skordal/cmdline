// CmdLine - A library for parsing command line arguments
// (c) Kristian Klomsten Skordal 2017 <kristian.skordal@wafflemail.net>
// Report bugs and issues on <https://github.com/skordal/cmdline/issues>
package net.skordal.cmdline;

/**
 * Exception used to signal that an invalid command has been passed on the command line.
 */
public class InvalidCommandException extends CommandLineException
{
	private final String command;

	/**
	 * Creates a new InvalidCommandException object.
	 * @param command name of the invalid command.
	 */
	public InvalidCommandException(String command)
	{
		super("invalid command specified: " + command);
		this.command = command;
	}

	/**
	 * Gets the name of the invalid command.
	 * @return the name of the invalid command.
	 */
	public String getCommand()
	{
		return command;
	}
}

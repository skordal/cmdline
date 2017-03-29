// CmdLine - A library for parsing command line arguments
// (c) Kristian Klomsten Skordal 2017 <kristian.skordal@wafflemail.net>
// Report bugs and issues on <https://github.com/skordal/cmdline/issues>
package net.skordal.cmdline;

/**
 * Exception used to indicate that an invalid argument has been passed to an option.
 */
public class InvalidArgumentException extends CommandLineException
{
	private final String argument;
	private final CommandLineOption option;

	/**
	 * Creates a new InvalidArgumentException object.
	 * @param argument the argument text that caused the exception.
	 * @param option   the option the argument was provided to.
	 */
	public InvalidArgumentException(String argument, CommandLineOption option)
	{
		super("invalid command line argument provided");
		this.argument = argument;
		this.option = option;
	}

	/**
	 * Gets the invalid argument text.
	 * @return the invalid argument text.
	 */
	public String getArgument()
	{
		return argument;
	}

	/**
	 * Gets the option the argument was passed to.
	 * @return the option the argument was passed to.
	 */
	public CommandLineOption getOption()
	{
		return option;
	}
}

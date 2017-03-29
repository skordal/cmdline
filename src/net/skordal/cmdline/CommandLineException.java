// CmdLine - A library for parsing command line arguments
// (c) Kristian Klomsten Skordal 2017 <kristian.skordal@wafflemail.net>
// Report bugs and issues on <https://github.com/skordal/cmdline/issues>
package net.skordal.cmdline;

/**
 * Base exception class indicating invalid use of the command line interface.
 */
public class CommandLineException extends Exception
{
	/**
	 * Creates a new CommandLineException object.
	 * @param message exception message.
	 */
	public CommandLineException(String message)
	{
		super(message);
	}
}

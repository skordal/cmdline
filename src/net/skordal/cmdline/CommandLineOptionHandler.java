// CmdLine - A library for parsing command line arguments
// (c) Kristian Klomsten Skordal 2017 <kristian.skordal@wafflemail.net>
// Report bugs and issues on <https://github.com/skordal/cmdline/issues>
package net.skordal.cmdline;

/**
 * Interface for command line option handlers.
 */
@FunctionalInterface public interface CommandLineOptionHandler
{
	/**
	 * Handles a command line option.
	 * This function is called when an option has been recognized on the command line.
	 * @param option   The command line option object for the recognized option.
	 * @param argument An argument if the option requires it or {@code null} otherwise.
	 */
	public void handleOption(CommandLineOption option, String argument);
}

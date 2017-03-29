// CmdLine - A library for parsing command line arguments
// (c) Kristian Klomsten Skordal 2017 <kristian.skordal@wafflemail.net>
// Report bugs and issues on <https://github.com/skordal/cmdline/issues>
package net.skordal.cmdline;

/**
 * Command line argument validator.
 */
@FunctionalInterface public interface CommandLineArgumentValidator
{
	/**
	 * Validates a command line argument.
	 * @param argument Argument as specified on the command line.
	 * @return Returns {@code true} if the argument is valid and {@code false} otherwise.
	 */
	public boolean validateArgument(String argument);
}

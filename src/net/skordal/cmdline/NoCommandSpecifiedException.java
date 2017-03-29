// Papaya Embedded Compiler Toolchain
// (c) Kristian Klomsten Skordal 2017 <kristian.skordal@wafflemail.net>
// Report bugs and issues on <https://github.com/skordal/papaya/issues>
package net.skordal.cmdline;

/**
 *
 * @author kristian
 */
public class NoCommandSpecifiedException extends CommandLineException
{
	public NoCommandSpecifiedException()
	{
		super("no command was specified on the command line");
	}
}

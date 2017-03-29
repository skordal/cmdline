// Papaya Embedded Compiler Toolchain
// (c) Kristian Klomsten Skordal 2017 <kristian.skordal@wafflemail.net>
// Report bugs and issues on <https://github.com/skordal/papaya/issues>
package net.skordal.cmdline;

/**
 *
 * @author kristian
 */
public class UnrecognizedOptionException extends CommandLineException
{
	private final String option;

	public UnrecognizedOptionException(String option)
	{
		super("unrecognized command line option: " + option);
		this.option = option;
	}

	public String getOption()
	{
		return option;
	}
}

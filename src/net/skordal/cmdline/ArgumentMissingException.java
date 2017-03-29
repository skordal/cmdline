// CmdLine - A library for parsing command line arguments
// (c) Kristian Klomsten Skordal 2017 <kristian.skordal@wafflemail.net>
// Report bugs and issues on <https://github.com/skordal/cmdline/issues>
package net.skordal.cmdline;

/**
 * Exception used to indicate that an argument is missing.
 */
public class ArgumentMissingException extends CommandLineException
{
	/** The name of the option missing an argument. */
	private final String option;

	/**
	 * Constructs a new ArgumentMissingException.
	 * @param option name of the option that caused the exception.
	 */
	public ArgumentMissingException(String option)
	{
		super("command line option " + option + " is missing an argument");
		this.option = option;
	}

	/**
	 * Returns the name of the option that caused the exception.
	 * @return the name of the option that caused the exception.
	 */
	public String getOption()
	{
		return option;
	}
}

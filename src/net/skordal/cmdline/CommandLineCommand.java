// CmdLine - A library for parsing command line arguments
// (c) Kristian Klomsten Skordal 2017 <kristian.skordal@wafflemail.net>
// Report bugs and issues on <https://github.com/skordal/cmdline/issues>
package net.skordal.cmdline;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * Class representing a command for the command line.
 */
public abstract class CommandLineCommand implements Comparable<CommandLineCommand>
{
	private final LinkedHashSet<CommandLineOption> options = new LinkedHashSet<>();
	private final String command, description;
	private int longestOption;

	/**
	 * Constructs a new CommandLineCommand object.
	 * @param command     Command string used on the command line.
	 * @param description Description of the command used in help texts.
	 */
	public CommandLineCommand(String command, String description)
	{
		if(command == null || description == null)
			throw new NullPointerException("command or description is null in command line command object");

		this.command = command;
		this.description = description;
	}

	/**
	 * Adds an option to the command line command.
	 * @param option Option to add to the command.
	 */
	public final void addOption(CommandLineOption option)
	{
		options.add(option);
		if(option.getLongOption() != null && option.getLongOption().length() > longestOption)
			longestOption = option.getLongOption().length();
	}

	/**
	 * Gets the name of the command.
	 * @return the name of the command.
	 */
	public String getCommand()
	{
		return command;
	}

	/**
	 * Gets the description of the command.
	 * @return the description of the command.
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * Prints a list of the available options for this command.
	 */
	public void printOptions()
	{
		System.out.println(String.format("Options for \"%s\" command:", getCommand()));
		if(options.isEmpty())
		{
			System.out.println("  No options supported.");
		} else {
			for(CommandLineOption option : options)
			{
				int numSpaces;

				if(option.getLongOption() == null)
					numSpaces = longestOption + CommandLineParser.USAGE_COLUMN_INDENT;
				else
					numSpaces = longestOption - option.getLongOption().length() + CommandLineParser.USAGE_COLUMN_INDENT;

				System.out.print("  ");
				if(option.getShortOption() != null)
					System.out.print("-" + option.getShortOption() + ", ");
				else
					System.out.print("    ");

				if(option.getLongOption() != null)
					System.out.print("--" + option.getLongOption());

				for(int i = 0; i < numSpaces; ++i)
					System.out.print(" ");

				System.out.println(option.getDescription());
			}
		}
	}

	/**
	 * Compares this CommandLineCommand to another CommandLineCommand object.
	 * This is done by simply comparing the names of the commands, since the names uniquely
	 * identify the commands on the command line.
	 */
	@Override public int compareTo(CommandLineCommand other)
	{
		return command.compareTo(other.command);
	}

	/**
	 * Processes this command.
	 * This function is called when the command has been recognized by the command line parser.
	 * @param inputFiles a list of all input files provided on the command line.
	 */
	public abstract void processCommand(LinkedList<String> inputFiles);

	/**
	 * Returns the set of command line options associated with this command.
	 * @return the set of command line options associated with this command.
	 */
	Set<CommandLineOption> getOptions()
	{
		return options;
	}
}

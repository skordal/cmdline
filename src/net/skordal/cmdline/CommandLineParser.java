// CmdLine - A library for parsing command line arguments
// (c) Kristian Klomsten Skordal 2017 <kristian.skordal@wafflemail.net>
// Report bugs and issues on <https://github.com/skordal/cmdline/issues>
package net.skordal.cmdline;

import java.util.LinkedList;
import java.util.TreeSet;

/**
 * Class for parsing command line commands and arguments.
 */
public final class CommandLineParser
{
	/** Number of spaces used to separate options/commands and their descriptions in the application usage description. */
	static final int USAGE_COLUMN_INDENT = 4;

	private final TreeSet<CommandLineCommand> commands;
	private final TreeSet<CommandLineOption>  globalOptions;

	private final String appname, description, helpFooter;
	private int longestCommand = 0, longestOption = 0;

	private final LinkedList<String> inputFiles;

	private final CommandLineOption helpOption;

	/**
	 * Creates a new CommandLineParser object.
	 * @param appname     name of the application.
	 * @param description description of the application.
	 * @param helpFooter  footer for the usage information printouts.
	 */
	public CommandLineParser(String appname, String description, String helpFooter)
	{
		commands = new TreeSet<>();
		globalOptions = new TreeSet<>();

		this.appname = appname;
		this.description = description;
		this.helpFooter = helpFooter;

		inputFiles = new LinkedList<>();

		helpOption = new CommandLineOption('h', "help", CommandLineOption.NO_ARGUMENT,
				"Prints usage information", (o, a) -> { printUsage(); System.exit(1); });
		addGlobalOption(helpOption);
	}

	/**
	 * Prints usage information for the available commands.
	 */
	private void printCommandSummary()
	{
		System.out.println("Commands:");
		for(CommandLineCommand command : commands)
		{
			int numSpaces = longestCommand - command.getCommand().length() + USAGE_COLUMN_INDENT;

			System.out.print("  " + command.getCommand());
			for(int i = 0; i < numSpaces; ++i)
				System.out.print(" ");
			System.out.println(command.getDescription());
		}
	}

	/**
	 * Prints usage information for global options.
	 */
	private void printGlobalOptions()
	{
		System.out.println("Global options:");
		for(CommandLineOption option : globalOptions)
		{
			int numSpaces;

			if(option.getLongOption() == null)
				numSpaces = longestOption + USAGE_COLUMN_INDENT;
			else
				numSpaces = longestOption - option.getLongOption().length() + USAGE_COLUMN_INDENT;

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

	/**
	 * Prints command line usage information for a command.
	 * @param command the command to print usage information for, or {@code null} to print the complete usage summary.
	 */
	private void printUsage(CommandLineCommand command)
	{
		System.out.println(String.format("Usage: %s <COMMAND> [OPTIONS...] [INPUT FILE]", appname));
		System.out.println(description);
		System.out.println();

		if(command == null)
		{
			printCommandSummary();
			System.out.println();
		}

		if(command == null)
			printGlobalOptions();
		else
			command.printOptions();
		System.out.println();

		System.out.println("For options related to a specific command, use --help or -h as an option for the desired command.");

		System.out.println();
		System.out.println(helpFooter);
	}

	/**
	 * Prints command line usage information.
	 */
	public void printUsage()
	{
		printUsage(null);
	}

	/**
	 * Adds a command.
	 * @param command the command to add to the command line parser.
	 */
	public void addCommand(CommandLineCommand command)
	{
		commands.add(command);
		if(command.getCommand().length() > longestCommand)
			longestCommand = command.getCommand().length();
	}

	/**
	 * Adds a global option.
	 * Global options are options that apply to all available commands.
	 * @param option the option to add as a global option.
	 */
	public void addGlobalOption(CommandLineOption option)
	{
		globalOptions.add(option);
		if(option.getLongOption() != null && option.getLongOption().length() > longestOption)
			longestOption = option.getLongOption().length();
	}

	/**
	 * Parses the provided command line options.
	 * @param args command line arguments.
	 * @throws CommandLineException thrown if an error occurs while parsing the command line.
	 */
	public void parse(String[] args) throws CommandLineException
	{
		CommandLineCommand currentCommand = null;

		if(args.length == 0)
			printUsage();
		else {
			for(int i = 0; i < args.length; ++i)
			{
				CommandLineOption currentOption = null;

				if(args[i].startsWith("--"))
				{
					final String optionName = args[i].substring(2);

					currentOption = globalOptions.stream().filter(option -> option.getLongOption().equals(optionName))
						.findFirst().orElse(null);
					if(currentOption == helpOption && currentCommand != null)
					{
						printUsage(currentCommand);
						System.exit(1);
					} else {
						if(currentOption == null && currentCommand != null)
							currentOption = currentCommand.getOptions().stream().filter(option -> option.getLongOption().equals(optionName))
									.findFirst().orElse(null);

						if(currentOption == null)
							throw new UnrecognizedOptionException(optionName);

						String argument = null;
						if(currentOption.argumentRequired())
						{
							if(i + 1 < args.length && !args[i + 1].startsWith("-"))
								argument = args[++i];
							else
								throw new ArgumentMissingException(args[i]);
						} else if(currentOption.argumentOptional())
						{
							if(i + 1 < args.length && !args[i + 1].startsWith("-"))
								argument = args[++i];
						}

						currentOption.handle(argument);
					}
				} else if(args[i].startsWith("-"))
				{
					final Character optionChar = args[i].charAt(1);
					currentOption = globalOptions.stream().filter(option -> option.getShortOption().equals(optionChar))
							.findFirst().orElse(null);

					if(currentOption == helpOption && currentCommand != null)
					{
						printUsage(currentCommand);
						System.exit(1);
					} else {
						if(currentOption == null && currentCommand != null)
							currentOption = currentCommand.getOptions().stream().filter(option -> option.getShortOption().equals(optionChar))
									.findFirst().orElse(null);

						if(currentOption == null)
							throw new UnrecognizedOptionException(args[i]);

						String argument = null;
						if(currentOption.argumentRequired())
						{
							if(args[i].length() > 2)
								argument = args[i].substring(2);
							else if(i + 1 < args.length && !args[i + 1].startsWith("-"))
								argument = args[++i];
							else
								throw new ArgumentMissingException(args[i]);
						} else if(currentOption.argumentOptional())
						{
							if(args[i].length() > 2)
								argument = args[i].substring(2);
							else if(i + 1 < args.length && !args[i + 1].startsWith("-"))
								argument = args[++i];
						}

						currentOption.handle(argument);
					}

				} else {
					if(currentCommand != null)
						inputFiles.add(args[i]);
					else {
						final String lookupCommand = args[i];
						currentCommand = commands.stream().filter(command -> command.getCommand().equals(lookupCommand))
								.findFirst().orElse(null);
						if(currentCommand == null)
							throw new InvalidCommandException(args[i]);
					}
				}
			}
		}

		if(currentCommand == null)
			throw new NoCommandSpecifiedException();
		else if(currentCommand != null)
			currentCommand.processCommand(inputFiles);
	}
}

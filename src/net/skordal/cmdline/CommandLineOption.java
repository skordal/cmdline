// CmdLine - A library for parsing command line arguments
// (c) Kristian Klomsten Skordal 2017 <kristian.skordal@wafflemail.net>
// Report bugs and issues on <https://github.com/skordal/cmdline/issues>
package net.skordal.cmdline;

import java.util.*;

/**
 * Class representing a command line option.
 */
public class CommandLineOption implements Comparable<CommandLineOption>
{
	public final static int NO_ARGUMENT = 0;
	public final static int ARGUMENT_OPTIONAL = 1;
	public final static int ARGUMENT_REQUIRED = 2;

	private final Character shortOption;
	private final String    longOption;
	private final String    description;

	private final int                    argumentRequired;
	private CommandLineOptionHandler     handler;
	private CommandLineArgumentValidator validator;

	/**
	 * Creates a new command line option object.
	 * @param shortOption      Short option character, or @c null if there is no short option.
	 * @param longOption       Long option string, or @c null if there is not long option.
	 * @param argumentRequired Whether this option takes an argument, whether that argument is optional or whether the
	 *                         option does not take an argument.
	 * @param description      Description of the option, which is used when printing the option summary.
	 * @param handler          Handler called when the option has been recognized on the command line.
	 */
	public CommandLineOption(Character shortOption, String longOption, int argumentRequired,
			String description, CommandLineOptionHandler handler)
	{
		if(shortOption == null && longOption == null)
			throw new NullPointerException("both the long and short options are specified as null");
		if(description == null)
			throw new NullPointerException("the helptext is null in command line option object");

		if(argumentRequired != NO_ARGUMENT && argumentRequired != ARGUMENT_OPTIONAL && argumentRequired != ARGUMENT_REQUIRED)
			throw new IllegalArgumentException("invalid value for argument requirement parameter");

		this.shortOption = shortOption;
		this.longOption = longOption;
		this.argumentRequired = argumentRequired;
		this.description = description;
		this.handler = handler;

		validator = null;
	}

	/**
	 * Gets the character representing the short option.
	 * @return the character representing the option or {@code null} if no short option has been specified.
	 */
	public Character getShortOption()
	{
		return shortOption;
	}

	/**
	 * Gets the string representing the long option.
	 * @return the string representing the option or {@code null} if no long option has been specified.
	 */
	public String getLongOption()
	{
		return longOption;
	}

	/**
	 * Gets the description of the option.
	 * This is used when printing the help text for commands.
	 * @return the description of the option.
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * Gets whether this option requires an argument.
	 * @return {@code true} if this option requires and argument, {@code false} otherwise.
	 */
	public boolean argumentRequired()
	{
		return argumentRequired == ARGUMENT_REQUIRED;
	}

	/**
	 * Gets whether this option takes an optional argument.
	 * @return {@code true} if this option takes an optional argument, {@code false} otherwise.
	 */
	public boolean argumentOptional()
	{
		return argumentRequired == ARGUMENT_OPTIONAL;
	}

	/**
	 * Sets the argument validator function for this option.
	 * The argument validator is called before the option handler and allows checking the validity of
	 * the argument before handling it.
	 * @param validator the validator for the option.
	 * @see CommandLineArgumentValidator
	 * @see InvalidArgumentException
	 */
	public void setArgumentValidator(CommandLineArgumentValidator validator)
	{
		this.validator = validator;
	}

	/**
	 * Sets the handler for this option.
	 * The handler is called when the option is recognized on the command line.
	 * @param handler the handler for the option.
	 */
	public void setHandler(CommandLineOptionHandler handler)
	{
		this.handler = handler;
	}

	/**
	 * Handles the option.
	 * This is called by the command line parser when the option has been recognzied on the command line.
	 * @param argument the argument of the option.
	 * @throws InvalidArgumentException thrown if the option does not take an argument, but an argument is provided,
	 *                                  or if the argument validator reports that the argument is invalid.
	 * @see CommandLineArgumentValidator
	 */
	void handle(String argument) throws InvalidArgumentException
	{
		if(argument != null && (argumentOptional() || argumentRequired()))
			if(validator != null && !validator.validateArgument(argument))
				throw new InvalidArgumentException(argument, this);
		if(handler != null)
			handler.handleOption(this, argument);
	}

	/**
	 * Checks if this command line option is equal to another object.
	 * This is done by comparing whether the objects are of the same type, and if
	 * they are whether the short and/or long options matches each other.
	 * @param other the object to compare agains.
	 * @return {@code true} if the objects are equal, {@code false} otherwise.
	 */
	@Override public boolean equals(Object other)
	{
		if(!(other instanceof CommandLineOption))
			return false;

		CommandLineOption option = (CommandLineOption) other;
		boolean shortMatch = false, longMatch = false;
		if(shortOption != null && option.shortOption != null)
			shortMatch = option.shortOption.equals(shortOption);
		if(longOption != null && option.shortOption != null)
			longMatch = option.longOption.equals(longOption);
		return shortMatch || longMatch;
	}

	/**
	 * Compares two command line options.
	 * This is done by comparing the short and long options to each other.
	 * @return the lexicographical difference between the options.
	 */
	@Override public int compareTo(CommandLineOption option)
	{
		if(shortOption != null && option.shortOption != null)
			return shortOption.compareTo(option.shortOption);
		else if(longOption != null && option.longOption != null)
			return longOption.compareTo(option.longOption);
		else {
			Character a;
			String b;

			if(shortOption != null)
				return shortOption.compareTo(option.longOption.charAt(0));
			else
				return option.shortOption.compareTo(longOption.charAt(0));
		}
	}

	/**
	 * Returns the hash code of the object.
	 * @return the hash code of the object.
	 */
	@Override public int hashCode()
	{
		int hash = 7;
		if(shortOption != null)
			hash = 29 * hash + Objects.hashCode(shortOption);
		if(longOption != null)
			hash = 29 * hash + Objects.hashCode(longOption);
		hash = 29 * hash + Objects.hashCode(description);
		hash = 29 * hash + Objects.hashCode(argumentRequired);
		return hash;
	}
}

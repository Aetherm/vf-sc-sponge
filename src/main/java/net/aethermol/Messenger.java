package net.aethermol;

import static org.spongepowered.api.text.TextTemplate.*;

import org.slf4j.Logger;
import org.spongepowered.api.text.TextTemplate;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

public class Messenger
{
	private static CustomLogger cLogger;
	public final static TextTemplate JOIN_MESSAGE = of
				(
				    TextColors.YELLOW, TextStyles.ITALIC, "Welcome ",
				    arg("player").color(TextColors.AQUA).style(TextStyles.BOLD),
				    ", to ",
				    arg("server").color(TextColors.RED).style(TextStyles.BOLD),"!"
				);
	public final static TextTemplate FIRST_JOIN_MESSAGE = of
			(
			    TextColors.YELLOW, TextStyles.ITALIC, "Welcome ",
			    arg("player").color(TextColors.AQUA).style(TextStyles.BOLD), "! ",
			    "You have been chosen as a tester for ",
			    arg("server").color(TextColors.RED).style(TextStyles.BOLD),".\n",
			    "Since this is your first visit, please have a look at the signs at the spawn!"
			);
	
	public Messenger(CustomLogger cLogger)
	{
		Messenger.cLogger = cLogger;
	}
	
	public static Logger getLogger()
	{
		return cLogger.getLogger();
	}
}

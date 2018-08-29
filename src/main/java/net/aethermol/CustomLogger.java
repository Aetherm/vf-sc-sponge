package net.aethermol;

import com.google.inject.Inject;
import org.slf4j.Logger;

public class CustomLogger
{
	@Inject
	private Logger logger;
	
	public CustomLogger(Logger logger)
	{
		this.logger = logger;
	}
	
	@SuppressWarnings("unused")
	private void setLogger(Logger logger)
	{
		this.logger = logger;
	}
	
	public Logger getLogger()
	{
		return logger;
	}
}

package panelizer.main;

import java.nio.file.Paths;
import java.util.Hashtable;

public class Main {
	
	public static void main(String[] args)
	{
		Hashtable<String, Boolean> table = new Hashtable<String, Boolean>();
		
		for(String arg : args)
		{
			if(arg.equalsIgnoreCase("-ml") || arg.equalsIgnoreCase("-makeloopable"))
			{
				table.put("makeloopable", true);
				break;
			}
		}
		
		Panelizer p = new Panelizer(Paths.get(args[0]), table);
	}
}

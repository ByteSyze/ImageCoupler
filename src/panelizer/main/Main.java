package panelizer.main;

import java.nio.file.Paths;

public class Main {
	
	public static void main(String[] args)
	{
		Panelizer p = new Panelizer(Paths.get(args[0]));
	}
}

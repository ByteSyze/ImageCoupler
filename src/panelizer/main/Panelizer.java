package panelizer.main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.imageio.ImageIO;

public class Panelizer {
	
	List<BufferedImage> frames;
	
	double rowCount = 5; // Number of images per row
	
	int refWidth;
	int refHeight;
	
	public Panelizer(Path path, Hashtable<String, Boolean> options)
	{
		frames = new ArrayList<BufferedImage>();

		try (DirectoryStream<Path> stream = Files.newDirectoryStream(path,"*.png"))
		{
		    for (Path file: stream)
		    {
		    	if(!file.endsWith("panelized.png"))
		    		frames.add(ImageIO.read(file.toFile()));
		    }
		} catch (IOException | DirectoryIteratorException x)
		{
		    System.err.println(x);
		}
		
		BufferedImage img = frames.get(0); //Reference image for panelized image specs
		
		refWidth = img.getWidth();
		refHeight = img.getHeight();
		
		BufferedImage panelized;
		
		if(options.get("makeloopable"))
			panelized = new BufferedImage((int)(refWidth*rowCount), (int)(refHeight*2*(Math.ceil(frames.size()/rowCount))), BufferedImage.TYPE_INT_ARGB);
		else
			panelized = new BufferedImage((int)(refWidth*rowCount), (int)(refHeight*(Math.ceil(frames.size()/rowCount))), BufferedImage.TYPE_INT_ARGB);

		for(int i = 0; i < frames.size(); i++)
		{
			img = frames.get(i);
			
			int widthOffset = (int)(refWidth*(i%rowCount));
			int heightOffset =  (int)(refHeight*(Math.floor(i/rowCount)));
			System.out.println(heightOffset);
			
			for(int x = 0; x < refWidth; x++)
			{
				
				for(int y = 0; y < refHeight; y++)
				{
					panelized.setRGB(widthOffset+x, heightOffset+y, img.getRGB(x, y));
				}
			}
		}
		
		//If we need to make the image loopable, grab the images in reverse order and continue from where we left off.
		if(options.get("makeloopable"))
		{
			for(int i = 1; i < frames.size(); i++)
			{
				img = frames.get(frames.size()-i);
				
				int widthOffset = (int)(refWidth*(i%rowCount));
				int heightOffset =  (int)(refHeight*(Math.floor(i/rowCount)) + (refHeight*(frames.size()%rowCount)));
				System.out.println(heightOffset);
				
				for(int x = 0; x < refWidth; x++)
				{
					
					for(int y = 0; y < refHeight; y++)
					{
						panelized.setRGB(widthOffset+x, heightOffset+y, img.getRGB(x, y));
					}
				}
			}
		}
		
		try {
			ImageIO.write(panelized, "png", new File(path.toUri().getRawPath()+"panelized.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

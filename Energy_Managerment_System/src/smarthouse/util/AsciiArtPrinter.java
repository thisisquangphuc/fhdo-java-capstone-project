package smarthouse.util;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class AsciiArtPrinter {
    public static void printGroupName() {
        // Settings for image size
		int width = 100;  // Adjust as needed for proper rendering
		int height = 20;
		// Create a buffered image
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = image.createGraphics();

		// Configure the graphics context
		graphics.setColor(Color.BLACK); // Background color
		graphics.fillRect(0, 0, width, height);
		graphics.setColor(Color.WHITE); // Text color
		graphics.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 20));
		// graphics.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN , 10));

		// Render the text
		String text = "GROUP 8";
		FontMetrics fontMetrics = graphics.getFontMetrics();
		int x = (width - fontMetrics.stringWidth(text)) / 2; // Center horizontally
		int y = ((height - fontMetrics.getHeight()) / 2) + fontMetrics.getAscent(); // Center vertically
		graphics.drawString(text, x, y);

		graphics.dispose();
	
			// Render the image as ASCII
		for (int yPixel = 0; yPixel < height; yPixel++) {
			StringBuilder line = new StringBuilder();
			for (int xPixel = 0; xPixel < width; xPixel++) {
				line.append(image.getRGB(xPixel, yPixel) == -16777216 ? " " : "*");
			}

			// Print only non-empty lines
			if (!line.toString().trim().isEmpty()) {
				System.out.println(line);
			}
		}
    }

}

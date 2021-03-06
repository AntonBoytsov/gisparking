/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class ImageTransformer {

    public static BufferedImage scaleByWidth(BufferedImage original, int newWidth) {
        
        int w = original.getWidth();
        int h = original.getHeight();
        int newHeight = (int) (((double) newWidth / w) * h);

        BufferedImage image = new BufferedImage(newWidth, newHeight, original.getType());
        
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(original, 0, 0, newWidth, newHeight, null);
        g2.dispose();
        
        return image;

    }
    
    public static BufferedImage scaleByHeigth(BufferedImage original, int newHeight) {
        
        int w = original.getWidth();
        int h = original.getHeight();
        int newWidth = (int) (((double) newHeight / h) * w);

        BufferedImage image = new BufferedImage(newWidth, newHeight, original.getType());
        
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(original, 0, 0, newWidth, newHeight, null);
        g2.dispose();
        
        return image;

    }
    
}

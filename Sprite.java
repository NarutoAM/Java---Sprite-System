import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import javax.imageio.*;
import javafx.scene.transform.NonInvertibleTransformException;

// Class for easier sprite and walk animations
public class Sprite
{
    private AffineTransform at; // Transform properties of image
    private Image currentImage; // Current image displayed 
    private int x, y; // Coordinates of sprite

    public double rotationL;
    public boolean bLookingRight = true, bInverted = false;
    public Vector<Image> imgsRight = new Vector<Image>();
    public Vector<Image> imgsLeft = new Vector<Image>();

    /*
     * Initalize sprite 
     * @param xLocation x location of sprite
     * @param yLocation y location of sprite
     * @param name of image in local file
     */
    public Sprite(int xLocation, int yLocation) throws Exception
    {
        x = xLocation;
        y = yLocation;
        currentImage = null;
    }

    /*
     * Change image displayed
     * @param newImage New image to be changed to
     */
    public void setCurrentImage(Image newImage)
    {
        currentImage = newImage;
    }

    /*
     * Change image displayed
     * @param newImage Name of new image to be changed to
     */
    public void setCurrentImage(String newImage)
    {
        try { currentImage = ImageIO.read(getClass().getResource(newImage)); } catch(Exception e) {}
    }

    /*  
     *  Adds an image to animation images
     *  @param resourceToAdd name of resource to add in local file
     *  @param bRightAnim true: add to right animation array false: add to left animation array
     */
    public void addImage(String[] resourceToAdd, boolean bRightAnim)
    {
        try 
        { 
            if(bRightAnim)
            {
                for(int i = 0; i < Array.getLength(resourceToAdd); i++)
                    imgsRight.addElement(ImageIO.read(getClass().getResource(resourceToAdd[i]))); 
            }
            else
            {
                for(int i = 0; i < Array.getLength(resourceToAdd); i++)
                    imgsLeft.addElement(ImageIO.read(getClass().getResource(resourceToAdd[i]))); 
            }

            if(currentImage == null)
                currentImage = bRightAnim ? imgsRight.elementAt(0) : imgsLeft.elementAt(0);
        } catch(Exception e) {} 
    }

    /*
     * Removes an image from animation images
     * @param imgToRemove the image to remove 
     * @param bRightAnim true: remove image from right animation array, false: remove image from left animation array
     */
    public void removeImages(Image[] imgsToRemove, boolean bRightAnim)
    {
        if(bRightAnim)
        {
            for(int i = 0; i < Array.getLength(imgsToRemove); i++)
                imgsRight.removeElement(imgsToRemove[i]);
        }
        else
        {
            for(int i = 0; i < Array.getLength(imgsToRemove); i++)
                imgsLeft.removeElement(imgsToRemove[i]);
        }
    }

    /*
     * Removes an image from animation images
     * @param imgsToRemove names of local images to remove
     * @param bRightAnim true: remove image from right animation array, false: remove image from left animation array
     */
    public void removeImagesByString(String[] imgsToRemove, boolean bRightAnim)
    {
        try 
        {
            if(bRightAnim)
            {
                for(int i = 0; i < Array.getLength(imgsToRemove); i++)
                    imgsRight.removeElement( ImageIO.read(getClass().getResource(imgsToRemove[i])) );
            } 
            else
            {
                for(int i = 0; i < Array.getLength(imgsToRemove); i++)
                    imgsLeft.removeElement( ImageIO.read(getClass().getResource(imgsToRemove[i])) );
            }
        } catch(Exception e) {} 
    }

    /*
     * Moves the sprite in specidfied direction and specified pixels
     * @param move Direction to move sprite
     * @param moveLength Amount of pixels to move sprite
     */ 
    public void Move(EMoveType move, int moveLength)
    {
        switch(move)
        {
            case MT_RIGHT:
                if(bInverted)
                    x = x - moveLength;
                else
                    x = x + moveLength;

                bLookingRight = true;
                break;
            
            case MT_LEFT:
                if(bInverted)
                    x = x + moveLength;
                else
                    x = x - moveLength;

                bLookingRight = false;
                break;

            case MT_DOWN:
                y = y + moveLength;
                break;

            case MT_UP:
                y = y - moveLength;
                break;

            default: 
                break;
        }

        // Stop at borders
        if(y < 0)
            y = 0;
        if(x < 0 - 25)
            x = 0 - 25;
        if(x > Learn.frame.getWidth() - 100)
            x = Learn.frame.getWidth() - 100;     
        if(y > Learn.frame.getHeight())
            y = Learn.frame.getHeight();   
        
        // Switch image for animation
        if ( (bLookingRight && !bInverted) || (bLookingRight && bInverted) )
        {
            if(imgsRight.indexOf(currentImage) + 2 <= imgsRight.size())
                currentImage = imgsRight.elementAt(imgsRight.indexOf(currentImage) + 1);
            else
                currentImage = imgsRight.firstElement();
        }
        else if( (!bLookingRight && !bInverted) || (!bLookingRight && bInverted) )
        {
            if(imgsLeft.indexOf(currentImage) + 2 <= imgsLeft.size())
                currentImage = imgsLeft.elementAt(imgsLeft.indexOf(currentImage) + 1);
            else
                currentImage = imgsLeft.firstElement();
        }
    }

    public void setLocation(int xLoc, int yLoc)
    {
        x = xLoc;
        y = yLoc;
    }

    public void setAT(double xLocation, double yLocation, double rotation, double scalex, double scaley)
    {
        at = new AffineTransform();
        at.translate(xLocation, yLocation);
        at.rotate(rotation);
        at.scale(scalex, scaley);

        rotationL = Math.toDegrees(rotation);

        if( (rotationL > 90) || (rotationL < -90) )
            bInverted = true;
        else if( (rotationL < 90) || (rotationL > -90) )
            bInverted = false;
    }

    // Getter functions
    public int GetX() { return x; }
    public int GetY() { return y; }
    public Image GetImage() { return currentImage; }
    public AffineTransform GetAT() { return at; }

    // Direction for sprite to move
    public enum EMoveType
    {
        MT_RIGHT,
        MT_LEFT,
        MT_UP,
        MT_DOWN
    };
}
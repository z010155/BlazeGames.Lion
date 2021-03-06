package Interface.Components;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

/**
 *
 * @author Ashton
 */
public class Map extends Component
{
    private Image image;
    private int fullMapX, fullMapY;

    public Map(Image image, int X, int Y)
    {
        this.image = image;
        setLocation(X, Y);
        setSize(image.getWidth(), image.getHeight());
    }
    
    @Override
    public void Render(Graphics g, float ParentX, float ParentY, int ParentWidth, int ParentHeight)
    {
        setAbsoluteLocation((int)ParentX+getX(), (int)ParentY+getY(), 0);
        g.drawImage(image, getAbsoluteX(), getAbsoluteY());
    }
    
    public Image getImage()
    {
        return image;
    }

    public void setImage(Image image)
    {
        this.image = image;
        setSize(image.getWidth(), image.getHeight());
    }
    
    public int getFullMapX()
    {
        return fullMapX;
    }

    public void setFullMapX(int fullMapX)
    {
        this.fullMapX = fullMapX;
    }

    public int getFullMapY()
    {
        return fullMapY;
    }

    public void setFullMapY(int fullMapY)
    {
        this.fullMapY = fullMapY;
    }
}
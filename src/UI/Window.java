package UI;

import Game.Scene;
import UI.Components.Component;
import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

/**
 *
 * @author Gage Orsburn
 */
public class Window
{
    private float X, Y;
    private int Width, Height, StartX, StartY, Radius, FocusedComponent = -1;
    private boolean isFirstRender, isVisible, isMouseDown, isPinned;
    private String WindowTitle;
    private ArrayList<Component> Components = new ArrayList<>();
    private Input input;
    
    public Window(String WindowTitle, float X, float Y, int Width, int Height, int Radius,boolean ShowClose, boolean isPinned)
    {
        this.WindowTitle = WindowTitle;
        
        setLocation(X, Y);
        setSize(Width, Height, Radius);   
        
        this.isFirstRender = true;
        this.isVisible = true;
        this.isMouseDown = false;
        this.isPinned = isPinned;
        
        input = Scene.getInstance().input;
    }
    
    public Window(String WindowTitle, float X, float Y, int Width, int Height, boolean ShowClose, boolean isPinned)
    {
        this(WindowTitle, X, Y, Width, Height, 0, ShowClose, isPinned);
    }
    
    public Window(String WindowTitle, float X, float Y, int Width, int Height)
    {
        this(WindowTitle, X, Y, Width, Height, 0, true, false);
    }
    
    public void requestFocus(Component component)
    {
        for(int i = 0; i < Components.size(); i++)
            if(Components.get(i).equals(component))
                if(FocusedComponent != i)
                    FocusedComponent = i;
    }
    
    public void checkComponents(int X, int Y)
    {
        boolean insideComponent = false;
        
        for(Component component : Components)
            if(component.Contains(X, Y))
                insideComponent = true;
        
        if(!insideComponent)
            FocusedComponent = -1;
    }
    
    public void doAction(int ActionX, int ActionY)
    {      
        checkComponents(ActionX, ActionY);
        
        for(Component component : Components)
            if(component.Contains(ActionX, ActionY))
            {
                requestFocus(component);
                component.doAction();
            }
    }
    
    public void stopAction(int ActionX, int ActionY)
    {
        for(Component component : Components)
            if(component.Contains(ActionX, ActionY))
                component.stopAction();
    }
    
    public void mousePressed(int X, int Y)
    {
        checkComponents(X, Y);
        
        for(Component component : Components)
            if(component.Contains(X, Y))
            {
                requestFocus(component);
                component.mousePressed(X, Y);
            }
    }
    
    public void keyPressed(int Key, char Char)
    {
        //for(Component component : Components)
            //component.keyPressed(Key, Char);
        if(FocusedComponent != -1 && Components.size() > 0)
        {
            Component component = Components.get(FocusedComponent);
            component.keyPressed(Key, Char);
        }
    }
    
    public void addComponent(Component component)
    {
        Components.add(component);
    }
    
    public void removeComponent(Component component)
    {
        Components.remove(component);
    }
    
    public void setVisible(boolean isVisible)
    {
        this.isVisible = isVisible;
    }
    
    public boolean isVisible()
    {
        return isVisible;
    }
    
    public void setPinned(boolean isPinned)
    {
        this.isPinned = isPinned;
    }
    
    public boolean isPinned()
    {
        return isPinned;
    }
    
    public final void setLocation(float X, float Y)
    {
        this.X = X;
        this.Y = Y;
        
        this.StartX = (int)X;
        this.StartY = (int)Y;
    }
    
    public final void setSize(int Width, int Height, int Radius)
    {
        this.Width = Width;
        this.Height = Height;
        this.Radius = Radius;
    }
    
    private void Move(int X, int Y)
    {
        this.X += X;
        this.Y += Y;
        
        StartX = input.getMouseX();
        StartY = input.getMouseY();
        
        isFirstRender = false;
    }
    
    public boolean Contains(int PointX, int PointY)
    {
        if(PointX >= X && PointY >= Y && PointX <= (X+Width) && PointY <= (Y+Height))
            return true;
        else
            return false;
    }
    
    public boolean MenuContains(int PointX, int PointY)
    {
        if(PointX >= X+13 && PointY >= Y+7 && PointX <= (X-7+Width) && PointY <= (Y+29))
            return true;
        else
            return false;
    }
    
    public void Render(Graphics g)
    {
        if(isVisible)
        {
            if(input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON))
            {
                if(Contains(input.getMouseX(), input.getMouseY()))
                    WindowManager.requestFocus(this);
                
                if(MenuContains(input.getMouseX(), input.getMouseY()) || isMouseDown)
                {
                    isMouseDown = true;
                    
                    if(!isPinned)
                    {
                        if(isFirstRender)
                        {
                            StartX = input.getMouseX();
                            StartY = input.getMouseY();
                        }

                        Move(input.getMouseX() - StartX, input.getMouseY() - StartY);
                    }
                }
            }
            else
            {
                isMouseDown = false;
                isFirstRender = true;
            }
        
            g.setColor(new Color(24, 24, 24));
            g.drawRoundRect(X, Y, Width, Height, Radius);
            g.setColor(new Color(98, 98, 98));
            g.drawRoundRect(X + 1, Y + 1, Width - 2, Height - 2, Radius);
            g.setColor(new Color(7, 17, 26));
            g.fillRoundRect(X + 2, Y + 2, Width - 3, Height - 3, Radius);
            g.setColor(new Color(12, 32, 50));
            g.fillRoundRect(X + 7, Y + 7, Width - 13, Height - 13, Radius);
            
            if(Radius == 0)
            {
                //Draw Close Button
                
                g.setColor(new Color(7, 17, 26));
                g.drawLine(X + 3, Y + 29, (X + Width) - 3, Y + 29);
                g.drawLine(X + 3, Y + 28, (X + Width) - 3, Y + 28);
                g.drawLine(X + 3, Y + 27, (X + Width) - 3, Y + 27);
                g.drawLine(X + 3, Y + 26, (X + Width) - 3, Y + 26);

                g.setColor(Color.white);
                g.drawString(this.WindowTitle, X + 10, Y + 7);

                for(Component component : Components)
                    component.Render(g, this.X + 8, this.Y + 30, this.Width, this.Height);
            }
            else
            {
                for(Component component : Components)
                    component.Render(g, this.X, this.Y, this.Width, this.Height);
            }
        }
    }
}
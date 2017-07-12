import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.DebugGraphics;
import javax.swing.Painter;
import javax.imageio.*;
import java.io.*;
import java.lang.reflect.Array;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.util.*;
import java.awt.*;

public class Learn extends JPanel
{
    private double rotate = 0;
    private int moveAmount = 15;

    public int screenWidth = getWidth(), screenHeight = getHeight();

    public static int stage;
    public static JFrame frame = new JFrame("hi");
    public static Sprite naruto;

    // Constructer
    public Learn() throws Exception
    {
        // Initialize sprite
        naruto = new Sprite(100, 100);
        naruto.addImage(new String[] { "Naruto Right 1.png", "Naruto Right 2.png" }, true);
        naruto.addImage(new String[] { "Naruto Left 1.png", "Naruto Left 2.png" }, false);

        // Initialize stage
        stage = 1;

        frame.addKeyListener(new KeyClass());
    }

    // Paints to window
    public void paintComponent(Graphics graphic)
    {
        super.paintComponent(graphic);
        Graphics2D g = (Graphics2D)graphic;
        setOpaque(true);

        g.setBackground(Color.yellow);
        g.clearRect(0, 0, getWidth(), getHeight());

        switch(stage)
        {
            case 1:
            {
                g.drawString(">>>>>>>", getWidth()/2, getHeight()/2);
                if(naruto.GetX() > (int)Math.round(getWidth() * 0.75))
                {
                    stage++;
                    naruto.setLocation(0, getHeight()/2);
                }

                break;
            }

            case 2:
            {
                g.drawLine(naruto.GetX() + 50, naruto.GetY() + 100, (int)Math.round(getWidth() * 0.5), (int)Math.round( getHeight() ));

                break;
            }
        }

        if( (rotate > 360) || (rotate < -360) )
            rotate = 0;
        naruto.setAT(naruto.GetX(), naruto.GetY(), Math.toRadians(rotate), 1, 1);

        g.drawImage(naruto.GetImage(), naruto.GetAT(), this);

        System.out.print("\nX: " + naruto.GetAT().getTranslateX() + "\tY: " + naruto.GetAT().getTranslateY() + "\tR: " + rotate + "\tSpriteR: " + naruto.rotationL + "\tB: " + naruto.bInverted);

        repaint();
    }

    public static void main(String[] args) throws Exception
    {
        // Initialize window
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new Learn());
        frame.setBackground(Color.GREEN);
        frame.setFocusable(true);
        frame.setVisible(true);
    }

    // Handles key events
    public class KeyClass extends JFrame implements KeyListener
    {
        public Vector<Character> keys = new Vector<>();

        public KeyClass()
        {
            new Timer
            ( 100, 
                new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        if(keys.contains('a'))
                        {
                            if(!naruto.bInverted)
                                naruto.Move(Sprite.EMoveType.MT_LEFT, moveAmount);
                            else
                                naruto.Move(Sprite.EMoveType.MT_RIGHT, moveAmount);
                        }
                        if(keys.contains('d'))
                        {
                            if(!naruto.bInverted)
                                naruto.Move(Sprite.EMoveType.MT_RIGHT, moveAmount);
                            else
                                naruto.Move(Sprite.EMoveType.MT_LEFT, moveAmount);
                        }
                        if(keys.contains('w'))
                        {
                            rotate += 5;
                        }
                        if(keys.contains('s'))
                        {
                            rotate -= 5;
                        }
                    }
                }
            ).start();
        }

		@Override
		public void keyTyped(KeyEvent e) 
        {
		}

		@Override
		public void keyPressed(KeyEvent e) 
        {
            if(!keys.contains(e.getKeyChar()))
                keys.addElement(e.getKeyChar());
        }

		@Override
		public void keyReleased(KeyEvent e) 
        {
            keys.removeElement(e.getKeyChar());
            
            if(naruto.bLookingRight)
                naruto.setCurrentImage(naruto.imgsRight.firstElement());
            else
                naruto.setCurrentImage(naruto.imgsLeft.firstElement());
        }
    }
}
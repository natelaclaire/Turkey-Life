import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.Timer;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import java.util.Random;

public class TurkeyFrame3 extends JFrame implements ActionListener
{
   private Image turkey;
   private Image deadTurkey;
   private BorderLayout myLayout;
   private Canvas turkeys;
   private Container container; // frame container
   private int[][] turkeyLocation;
   private int[][] oldTurkeyLocation;
   private int maxCols = 30;
   private int maxRows = 30;
   private JButton repaintButton;
   private final boolean DEBUG = false;
   private Timer turkeyTimer;
   private JButton startButton;
   private JButton stopButton;
   private int margin = 0; // left margin

   // no-argument constructor
   public TurkeyFrame3()
   {
       super( "Turkey Demo" );
       myLayout = new BorderLayout(); 
       container = getContentPane(); // get content pane
       setLayout( myLayout ); // set JFrame layout      
       turkey = Toolkit.getDefaultToolkit().getImage("turkey_16.gif");
       deadTurkey = Toolkit.getDefaultToolkit().getImage("turkey-cooked_16.gif");
       
       /*
        addComponentListener(new ComponentListener() 
        {  
        // This method is called after the component's size changes
        public void componentResized(ComponentEvent evt) {
         Component c = (Component)evt.getSource();
              
              // Get new size
              Dimension newSize = c.getSize();
              margin = (newSize.width - (maxCols * 17)) / 2;
              JOptionPane.showMessageDialog(null,"Margin = "+margin+"\nGame Size: "+newSize.width);
          }
      });
      */
       
       

       setupGame();
      

   } // end TurkeyFrame constructor

   // handle button and timer events
   public void actionPerformed( ActionEvent event )
   { 
     if (event.getSource()==repaintButton || event.getSource()==turkeyTimer)
     {
         nextGeneration();
     }
     else if (event.getSource()==startButton)
     {
         turkeyTimer.start();
         stopButton.setEnabled(true);
         startButton.setEnabled(false);
         repaintButton.setEnabled(false);
         nextGeneration();
     }
     else if (event.getSource()==stopButton)
     {
         turkeyTimer.stop();
         stopButton.setEnabled(false);
         startButton.setEnabled(true);
         repaintButton.setEnabled(true);
     }
  
      //container.validate(); // re-lay out container
   } // end method actionPerformed
   
   public void populateGame()
   {
       turkeyLocation = new int[maxRows][maxCols];
       oldTurkeyLocation = new int[maxRows][maxCols];
       Random rand = new Random();
       rand.setSeed(1);
      
       for ( int i = 0; i < turkeyLocation.length; i++ )
       {
           for (int j = 0; j < turkeyLocation[i].length; j++)
           {
               if (rand.nextInt(3)==1)
               {
                   turkeyLocation[i][j] = 1;
               }
               else
               {
                   turkeyLocation[i][j] = 0;
               }
           }
       } // end for  
       
       oldTurkeyLocation = turkeyLocation;
       
   }
   
   public void nextGeneration()
   {
       int[][] temp = new int[maxRows][maxCols];
       int touchCount;
       int checkI;
       int checkJ;
       
       oldTurkeyLocation = turkeyLocation;
             
       for ( int i = 0; i < oldTurkeyLocation.length; i++ )
       {
           if (DEBUG) System.out.printf("---Examining row %d\n",i);
           for (int j = 0; j < oldTurkeyLocation[i].length; j++)
           {
               if (DEBUG) System.out.printf("---Examining column %d\n",j);
               touchCount = 0;
               
               /*
                *
                * Check each neighboring cell to determine how many are live.
                * Ensure that you don't go out of bounds and that you don't
                * include the current cell in the count. Use nested for loops
                * ranging from -1 to +1?
                * 
                */
               
               
               for ( checkI = i-1; checkI <= i+1; checkI++ ) // loop through rows
               {
                   if (checkI < 0 || checkI >= maxRows) 
                   {
                       // don't go out of bounds
                       if (DEBUG) System.out.printf("turkeyLocation[%d][*] is out of bounds\n",checkI);
                   }
                   else
                   {
                       for ( checkJ = j-1; checkJ <= j+1; checkJ++) // loop through columns
                       {
                           if (checkI == i && checkJ == j) 
                           {
                               // never count the current square
                               if (DEBUG) System.out.printf("turkeyLocation[%d][%d] is current square\n",checkI,checkJ);
                           } 
                           else
                           {
                               if (checkJ < 0 || checkJ >= maxCols) 
                               {
                                   // don't go out of bounds
                                   if (DEBUG) System.out.printf("turkeyLocation[%d][%d] is out of bounds\n",checkI,checkJ);
                               }
                               else
                               {
                                   if (oldTurkeyLocation[checkI][checkJ]==1) // neighbor
                                   {
                                       touchCount++; // increment touchCount
                                       if (DEBUG) System.out.printf("turkeyLocation[%d][%d] is neighbor\n",checkI,checkJ);
                                   }
                                   else
                                   {
                                       if (DEBUG) System.out.printf("turkeyLocation[%d][%d] is empty\n",checkI,checkJ);
                                   }
                               }
                           }
                       } // end for loop
                   }
               }
               
               if (oldTurkeyLocation[i][j]==1)
               {
                   if (touchCount==2 || touchCount==3)
                   {
                       temp[i][j] = 1;
                   }
                   else
                   {
                       temp[i][j] = 0;
                   }
               } 
               else // empty cell
               {
                   if (touchCount==3)
                   {
                       temp[i][j] = 1;
                   }
                   else
                   {
                       temp[i][j] = 0;
                   }
               }
               
           }
       } // end for  
       
       turkeyLocation = temp;
       
       turkeys.repaint();
       
   } // end nextGeneration()
   
   public void placeTurkeys()
   {
       turkeys = new Canvas() {
          public void paint(Graphics g){
              for ( int i = 0; i < turkeyLocation.length; i++ )
              {
                  for (int j = 0; j < turkeyLocation[i].length; j++)
                  {
                      if (turkeyLocation[i][j]==1) 
                      {
                          if (oldTurkeyLocation[i][j]==0)
                          {
                              g.setColor(Color.GREEN);
                              g.fillRect(margin+(j*17),i*17,16,16);
                          }
                          g.drawImage(turkey,margin+(j*17),i*17,this);
                      }
                      else
                      {
                          if (oldTurkeyLocation[i][j]==1)
                          {
                              g.drawImage(deadTurkey,margin+(j*17),i*17,this);
                              //g.setColor(Color.RED);
                              //g.fillRect(margin+(j*17),i*17,16,16);
                          }
                      }
                  }
         
              } // end for  
            
          }
      } ;

      
      add( turkeys, BorderLayout.CENTER ); // add image to JFrame
   }
   
   public void setupGame()
   {
       populateGame();
      
       placeTurkeys();
      
       JPanel buttonPanel = new JPanel(); // defaults to FlowLayout
               
       startButton = new JButton("Start");
       startButton.addActionListener(this);
       startButton.setEnabled(false);
       buttonPanel.add(startButton);
       
       stopButton = new JButton("Stop");
       stopButton.addActionListener(this);
       buttonPanel.add(stopButton);
       
       // Next Generation button calls the nextGeneration() method and should
       // only be enabled if the timer is stopped. When debug mode is enabled,
       // the timer is off by default so the Next Generation button is enabled.
       repaintButton = new JButton("Next Generation");
       repaintButton.addActionListener(this);
       if (!DEBUG) // disable unless in debug mode
           repaintButton.setEnabled(false);
       buttonPanel.add(repaintButton);
       
       add(buttonPanel, BorderLayout.PAGE_END);
       
       turkeyTimer = new Timer(600,this);
       if (!DEBUG) // don't automatically start the timer in debug mode
           turkeyTimer.start();
   }
   
   public static void main( String args[] )
   { 
      TurkeyFrame3 gridLayoutFrame = new TurkeyFrame3(); 
      gridLayoutFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
      gridLayoutFrame.setSize( 520, 560 ); // set frame size
      gridLayoutFrame.setVisible( true ); // display frame
      
   } // end main
} // end class TurkeyFrame3
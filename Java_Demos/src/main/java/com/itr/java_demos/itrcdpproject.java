import java.awt.event.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Scanner;

import javax.swing.*;

import com.genlogic.*;



public class itrcdpproject extends GlgJBean implements ActionListener 
{

    static final int NUM_VALUES = 6;
    boolean PerformUpdates = true;

    GlgAnimationValue [] animation_array = new GlgAnimationValue[ NUM_VALUES ];
    static boolean AntiAliasing = true;

    Timer timer = null;

    public itrcdpproject()
    {
       super();
       SetDResource( "$config/GlgAntiAliasing", AntiAliasing ? 1.0 : 0.0 );
    }
 
//////////////////////////////////////////////////////////////////////////
// Starts updates
//////////////////////////////////////////////////////////////////////////

    public void ReadyCallback( GlgObject viewport ) 
   {
      super.ReadyCallback( viewport );
      
      if( timer == null )
      {

         // Restart the timer after each update (instead of using repeats)
         // to avoid flooding the event queue with timer events on slow 
         // machines.

        timer = new Timer( 350, this );
        timer.setRepeats( false );
        timer.start();
     }
  }

////////////////////////////////////////////////////////////////////////////
    public static void main( final String [] arg )
   {
      SwingUtilities.
        invokeLater( new Runnable(){ public void run() { Main( arg ); } } );
   }

////////////////////////////////////////////////////////////////////////////

    public static void Main( final String[] args) 
    {

        class DemoQuit extends WindowAdapter
      {
         public void windowClosing( WindowEvent e ) { System.exit( 0 ); }
      } 

       
      JFrame frame = new JFrame( "ITR CDP Project" );

      frame.setResizable( true );
      frame.setSize( 750, 600 );
      frame.setLocation( 20, 20 );

      itrcdpproject controls = new itrcdpproject();
      frame.getContentPane().add( controls );

      frame.addWindowListener( new DemoQuit() );
      frame.setVisible( true );

      // Assign a drawing filename after the frame became visible and 
      // determined its client size to avoid unnecessary resizing of 
      // the drawing.
      // Loading the drawing triggers ReadyCallback which starts updates.
      //

      controls.SetDrawingName( "meter.g" );

    }

//////////////////////////////////////////////////////////////////////////////////

      int vel=0,pre=0,vol=0;
      static int count=0;

      public void UpdateMeter()
      { 

            if( timer == null )
            return;   // Prevents race conditions

            if( PerformUpdates )
            {
               try
                  {
                     if(++count == 21)
                         {

                           Thread.sleep(5000);
                           System.exit(0);

                         }
                           DatagramSocket ds=new DatagramSocket(9999);

                           byte[] packet=new byte[1024];

                           DatagramPacket dp=new DatagramPacket(packet,1024);

                           ds.receive(dp);

                           String data=new String(dp.getData(), 0,dp.getLength());

                           Scanner sc=new Scanner(data);

                           while(sc.hasNextInt())
                           {
                              vel=sc.nextInt();
                              pre=sc.nextInt();
                              vol=sc.nextInt();
                           }
                                    //Give the value according the input received

                                    animation_array[ 0 ] = new GlgAnimationValue( this,GlgAnimationValue.SIN,0, 2 ,vel ,vel, "vel/Value");
                                    animation_array[ 1 ] = new GlgAnimationValue( this,GlgAnimationValue.SIN,0, 2 ,vel ,vel, "velgraph/Chart/Plots/Plot#0/ValueEntryPoint" );
                                    
                                    animation_array[ 2 ] = new GlgAnimationValue( this,GlgAnimationValue.SIN,0, 2 ,pre ,pre, "pre/Value");
                                    animation_array[ 3 ] = new GlgAnimationValue( this,GlgAnimationValue.SIN,0, 2 ,pre ,pre, "pregraph/Chart/Plots/Plot#0/ValueEntryPoint" );
                                    
                                    animation_array[ 4 ] = new GlgAnimationValue( this,GlgAnimationValue.SIN,0, 2 ,vol ,vol, "vol/Value");
                                    animation_array[ 5 ] = new GlgAnimationValue( this,GlgAnimationValue.SIN,0, 2 ,vol ,vol, "volgraph/Chart/Plots/Plot#0/ValueEntryPoint" );    
                                   
                                    //Visit the Animation again to change the value of all the Graph

                                    animation_array[ 0 ].Iterate();
                                    animation_array[ 1 ].Iterate();
                                    animation_array[ 2 ].Iterate();
                                    animation_array[ 3 ].Iterate();
                                    animation_array[ 4 ].Iterate();
                                    animation_array[ 5 ].Iterate();
                                                                     
                                    Update();   // Show changes

                                  ds.close();

                           }
                        catch(Exception e)
                        {
                           System.out.println(e);
                        }
      }

      timer.start();   // Restart the update timer
   
   }

////////////////////////////////////////////////////////////////////////////

public void Start()
   {
      PerformUpdates = true;
      if( timer != null )
        timer.start();
   }

//////////////////////////////////////////////////////////////////////////
// ActionListener method to use the bean as update timer's ActionListener.
//////////////////////////////////////////////////////////////////////////

   public void actionPerformed( ActionEvent e )
   {
      UpdateMeter();
   }

}


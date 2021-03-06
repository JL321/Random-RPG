/** 
 * this template can be used for a start menu
 * for your final project
**/


//Imports
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingUtilities;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.BorderFactory;
import javax.swing.border.EmptyBorder;

  
class InstructionsV2 extends JFrame { 

  JFrame thisFrame;
  
  //Constructor - this runs first
  InstructionsV2() { 
    super("Instructions");
    this.thisFrame = this;  
    
    //configure the window
    this.setSize(1800,900);    
    this.setLocationRelativeTo(null); //start the frame in the center of the screen
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
    this.setResizable (false);
    
    //Display the frame without border and invisible
    this.setUndecorated(true);
    setBackground(new Color(0,0,0,0));
    
    
         
    //Create a Panel for stuff
    JPanel decPanel = new DecoratedPanel();
    decPanel.setBorder(new EmptyBorder(0, 0, 1800, 0));

    
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());
    mainPanel.setBackground(new Color(0, 0, 0, 0));
    mainPanel.setPreferredSize(new Dimension(1800,900));
    
    
    //Create JButtons for the bottomPanel

    JButton returnButton = new JButton(new ImageIcon("ReturnButton.png"));
    returnButton.setBackground(new Color(0, 0, 0, 0));
    returnButton.setRolloverIcon(new ImageIcon("ReturnButtonPressed.png"));
    returnButton.setBorder(BorderFactory.createEmptyBorder());
    returnButton.setFocusPainted(false);
    returnButton.addActionListener(new ReturnButtonListener());
    
    JButton page2Button = new JButton(new ImageIcon("ToPage2Button.png"));
    page2Button.setBackground(new Color(0, 0, 0, 0));
    page2Button.setRolloverIcon(new ImageIcon("ToPage2ButtonPressed.png"));
    page2Button.setBorder(BorderFactory.createEmptyBorder());
    page2Button.setFocusPainted(false);
    page2Button.addActionListener(new Page2ButtonListener());
    
    JPanel bottomPanel = new JPanel();
    bottomPanel.setBackground(new Color(0, 0, 0, 0));
    bottomPanel.add(returnButton);
    bottomPanel.add(page2Button);
    
    //Add all panels to the mainPanel according to border layout
    mainPanel.add(bottomPanel,BorderLayout.SOUTH);

    mainPanel.requestFocusInWindow(); //make sure the frame has focus   
    
    decPanel.add(mainPanel);
    //add the main panel to the frame
    this.add(decPanel);
    
    
    
    //Start the app
    this.setVisible(true);
    
  }
  
  
  //INNER CLASS - Overide Paint Component for JPANEL
  private class DecoratedPanel extends JPanel {
    
    DecoratedPanel() {
      this.setBackground(new Color(0,0,0,0));

      this.requestFocusInWindow(); //make sure the frame has focus
    }
    
    public void paintComponent(Graphics g) { 
      super.paintComponent(g);     
      Image pic = new ImageIcon( "InstructionsMenuCaveP1.png" ).getImage();
      g.drawImage(pic,0,0,null); 
    }
    
    
  }
  //This is an inner class that is used to detect a button press
  private class ReturnButtonListener implements ActionListener {  //this is the required class definition
    public void actionPerformed(ActionEvent event)  {  
      System.out.println("Returning to menu");
      new MenuUIV2(); //create a new FunkyFrame (another file that extends JFrame)
      thisFrame.dispose();
    }
  }
 
  private class Page2ButtonListener implements ActionListener {  //this is the required class definition
    public void actionPerformed(ActionEvent event)  {  
      System.out.println("Going to Instructions Page 2");
      new InstructionsPage2V2(); //create a new FunkyFrame (another file that extends JFrame)
      thisFrame.dispose();
    }
  }
  
  
  //Main method starts this application
 public static void main(String[] args) { 
   new InstructionsV2();

 }
}
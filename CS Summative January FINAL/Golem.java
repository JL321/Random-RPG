import java.io.*; 
import java.awt.image.*;
import javax.imageio.*;

public class Golem extends Enemy{
 
  Golem(int health, int defense, double speed, int attack, double x1, double y1, String status, int width, int height, int level, boolean type){
    super(health,defense,speed,attack,x1,y1,status,width,height,level, type);
    loadSprites();
  }

  public void loadSprites() {
    try {
      BufferedImage sheet;
      if (getTypeB() == true){
        sheet = ImageIO.read(new File("GolemBossSheet.png"));
      } else {
        sheet = ImageIO.read(new File("GolemSheet.png"));
      }
  
     final int width = 60;
     final int height = 60;
     final int rows = 1;
     final int cols = 4;
     
     for (int j = 0; j < rows; j++)
       for (int i = 0; i < cols; i++)
         changeSprite((j * cols) + i, sheet.getSubimage(i * width,j * height,width,height));
     } catch(Exception e) { System.out.println("error loading sheet");};
      
  }
  
//  public void draw(Graphics g) { 
//    g.setColor(Color.YELLOW); //There are many graphics commands that Java can use
//    g.fillRect(getX(), getY(),getW(),getH()); //notice the y is a variable that we control from our animate method          
//  }
  
}
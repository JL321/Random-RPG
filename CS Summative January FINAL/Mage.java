import java.io.*; 
import java.awt.image.*;
import javax.imageio.*;

public class Mage extends Hero{
  
  private double lastTime = System.nanoTime() - 1.0E9;
  private double currentTime = System.nanoTime(); //AOE effect duration
  private double lastTime3 = System.nanoTime() - 1.0E9;
  private double currentTime3 = System.nanoTime(); //Knockback duration
  Mage(int health, int defense, double speed, int attack, int x1, int y1, int width, int height){
    super(health,defense,speed,attack,x1,y1,width,height);
    loadSprites();
    setSpecialCount(6,1);
    setSpecialCount(4,2);
    setSpecialCount(5,3);
    setSpecialCount(8,4);
  }
  
  public boolean specialOne(){
    if (cdCheck1(getSpecialCount(1)) == true){
      return true;
    } else {
      return false;
    }
  }
  
  public boolean specialTwo(){
    if (cdCheck2(getSpecialCount(2)) == true){
      return true;
    } else {
      return false;
    }
  }
  
  public boolean specialThree(){
    if (cdCheck3(getSpecialCount(3)) == true){
      return true;
    } else {
      return false;
    }
  }
  
  public boolean specialFour(){
    if (cdCheck4(getSpecialCount(4)) == true){
      return true;
    } else {
      return false;
    }
  }
  
  public void statReverse(){
    setDamage(getDamageO());
  }
      
  
  public void statChange(int num){
    setDamage(getDamage()+num);
  }
  
  public void updateLastM3(){
    this.lastTime3 = System.nanoTime();
  }
  
  public boolean cdCheckM3(){
    this.currentTime3 = System.nanoTime();
    if ((this.currentTime3 - this.lastTime3)/1.0E9 >= 1){
      return true;
    } else {
      return false;
    }
  }
  
   public void updateLastM(){
    this.lastTime = System.nanoTime();
  }
  
  public boolean cdCheckM(){
    this.currentTime = System.nanoTime();
    if ((this.currentTime - this.lastTime)/1.0E9 >= 1){
      return true;
    } else {
      return false;
    }
  }
  
  public void loadSprites() {
    try {
     BufferedImage sheet = ImageIO.read(new File("MageSheet.png"));
  
     final int width = 60;
     final int height = 60;
     final int rows = 1;
     final int cols = 4;
     
     for (int j = 0; j < rows; j++)
       for (int i = 0; i < cols; i++)
         changeSprite((j * cols) + i, sheet.getSubimage(i * width,j * height,width,height));
     } catch(IOException e) { System.out.println("error loading sheet");};
      
  }
  

}
import java.awt.*;
import java.io.*; 
import java.awt.image.*;
import javax.imageio.*;

public class Knight extends Hero{
  
  Knight(int health, int defense, double speed, int attack, int x1, int y1, int width, int height){
    super(health,defense,speed,attack,x1,y1,width,height);
    loadSprites();
    setSpecialCount(3,1);
    setSpecialCount(5,2);
    setSpecialCount(6,3);
    setSpecialCount(8,4);
  }
  
  private long lastTimeS2 = System.nanoTime(); //Independent timers for attack 2,3, as a duration of attack needs to be applied alongside overall cooldwon
  private long currentTimeS2 = System.nanoTime();
  private long lastTimeS3 = System.nanoTime();
  private long currentTimeS3 = System.nanoTime();
  
  public int [] attack(int height){
    
    int heroX = getX();
    int heroY = getY();
    
    String lastDirection = getLDirection();
    int [] boxPoint = new int [2];
    
    if (lastDirection.equals("W")){
      boxPoint[0] = heroX + (60-height)/2; 
      boxPoint[1] = heroY - 60;
    } else if (lastDirection.equals("S")){
      boxPoint[0] = heroX + (60-height)/2;
      boxPoint[1] = heroY+60;
    } else if (lastDirection.equals("A")){
      boxPoint[0] = heroX-60;
      boxPoint[1] = heroY +(60-height)/2;
    } else if (lastDirection.equals("D")){
      boxPoint[0] = heroX+60;
      boxPoint[1] = heroY +(60-height)/2;
    }
    
    return boxPoint;
    
  }
  
  
  public boolean specialOne(){
    if (cdCheck1(getSpecialCount(1)) == true){
      return true;
    } else {
      return false;
    }
  }
  
  public void updateLastS2(){
    this.lastTimeS2 = System.nanoTime();
  }
  
  public boolean cdCheckS2(){ 
    this.currentTimeS2 = System.nanoTime();
    if ((this.currentTimeS2-this.lastTimeS2)/1.0E9 >= 0.5){ 
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
  
   public void updateLastS3(){
    this.lastTimeS3 = System.nanoTime();
  }
  
  public boolean cdCheckS3(){ 
    this.currentTimeS3 = System.nanoTime();
    if ((this.currentTimeS3-this.lastTimeS3)/1.0E9 >= 1){ 
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
    setDefense(getDefenseO());
  }
      
  
  public void statChange(int num){
    setDefense(getDefense()+num);
  }
  
  public void loadSprites() {
    try {
     BufferedImage sheet = ImageIO.read(new File("KnightSheet.png"));
  
     final int width = 60;
     final int height = 60;
     final int rows = 1;
     final int cols = 4;
     
     for (int j = 0; j < rows; j++)
       for (int i = 0; i < cols; i++)
         changeSprite((j * cols) + i, sheet.getSubimage(i * width,j * height,width,height));
     } catch(Exception e) { System.out.println("error loading sheet");};
      
  }
  
}
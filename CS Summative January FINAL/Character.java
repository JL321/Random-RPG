import java.awt.*;
import java.awt.image.*;

public class Character{
  
  private double speed;
  private double x1,y1;
  private int health, defense, attack, height, width;
  private double xDirection, yDirection, elapsedTime;
  private String direction = "STAND";
  private String lastDirection = "W";
  private int originalDefense;
  private int originalAttack;
  private double originalSpeed;
  private double bufferLast = System.nanoTime();
  private double bufferNow = System.nanoTime();
  private String lastDirectionL = "W"; //Lateral last direction
  private int currentSprite = 0;
  private BufferedImage[] sprites = new BufferedImage[5];
  private int coinC = 0;
  
  public void setCoin(int coinC){
    this.coinC = coinC;
  }
  
  public int getCoin(){
    return this.coinC;
  }
  
  public void move(String direction){
    this.direction = direction;
    if (!(direction.equals("STAND"))){
      if (direction.equals("A") || direction.equals("W") || direction.equals("D") || direction.equals("S")){
        this.lastDirectionL = direction;
      }
      this.lastDirection = direction;
    }
  }
  
  Rectangle boundingBox; //Alter to accustom for custom positions
  
  Character(int health, int defense, double speed, int attack, double x1, double y1, int width, int height){
    this.health = health;
    this.defense = defense;
    this.speed = speed;
    this.attack = attack;
    this.x1 = x1;
    this.y1 = y1;
    this.height = height; //Fix
    this.width = width;
    this.originalAttack = attack;
    this.originalDefense = defense;
    this.originalSpeed = speed;
    this.boundingBox = new Rectangle((int)x1,(int)y1,width,height);
  }
  
  public void updateBuffer(){
    this.bufferLast = System.nanoTime();
  } 
  
  public boolean bufferCheck(){
    this.bufferNow = System.nanoTime();
    if ((this.bufferNow - this.bufferLast)/1.0E9 >= 0.5){
      return true;
    } else {
      return false;
    }
  }
  
  public int getDamageO(){
    return this.originalAttack;
  }
  
  public int getDefenseO(){
    return this.originalDefense;
  }
  
  public double getSpeedO(){
    return this.originalSpeed;
  }
  
  public void setDamageO(int attack){
    this.originalAttack = attack;
  }
  
  public void setDefenseO(int defense){
    this.originalDefense = defense;
  }
  
  public int getDamage(){
    return this.attack;
  } 
  
  public int getDefense(){
    return this.defense;
  }
  
  public double getSpeed(){
    return this.speed;
  }
  
  public void setDamage(int attack){
    this.attack = attack;
  }
  
  public void setDefense(int defense){
    this.defense = defense;
  }
  
  public void setSpeed(double speed){
    this.speed = speed;
  }
    
  //Updates position based upon speed,xDirection,yDirection
  
  /*
   * update
   * Takes stored direction
   * Diagonal movement is divided by sqrt(2), as to move the same distance as vertical/horizontal movement
  */
  
  public void update(double time){
    
    if (this.direction.equals("W")){
      this.yDirection = -time*this.speed;
      this.xDirection = 0;
      if (this.y1+yDirection < 64){ //yLower bound
        this.yDirection = 0;
      }
    } else if (this.direction.equals("S")){
      this.yDirection = time*this.speed;
      this.xDirection = 0;
      if (this.y1+this.height+yDirection >= 836){ //y Upper bound
        this.yDirection = 0;
      }
    } else if (this.direction.equals("A")){
      this.yDirection = 0;
      this.xDirection = -time*this.speed;
      if (this.x1+xDirection < 214){//xLower bound
        this.xDirection = 0;
      }
    } else if (this.direction.equals("D")){
      this.yDirection = 0;
      this.xDirection = time*this.speed;
      if (this.x1+this.width+xDirection >= 1750){ //xHigher bound
        this.xDirection = 0;
      }
    } else if (this.direction.equals("AW")){
      this.yDirection = -time*this.speed/Math.sqrt(2);
      this.xDirection = -time*this.speed/Math.sqrt(2);
      if (this.x1+xDirection < 214){
        this.xDirection = 0;
      }
      if (this.y1+yDirection < 64){
        this.yDirection = 0;
      }
    } else if (this.direction.equals("DW")){
      this.yDirection = -time*this.speed/Math.sqrt(2);
      this.xDirection = time*this.speed/Math.sqrt(2);
      if (this.x1+xDirection+width > 1750){
        this.xDirection = 0;
      }
      if (this.y1+yDirection < 64){
        this.yDirection = 0;
      }
    } else if (this.direction.equals("AS")){
      this.yDirection = time*this.speed/Math.sqrt(2);
      this.xDirection = -time*this.speed/Math.sqrt(2);
      if (this.x1+xDirection < 214){
        this.xDirection = 0;
      }
      if (this.y1+yDirection+this.height > 836){
        this.yDirection = 0;
      }
    } else if (this.direction.equals("DS")){
      this.yDirection = time*this.speed/Math.sqrt(2);
      this.xDirection = time*this.speed/Math.sqrt(2);
      if (this.x1+xDirection+this.width > 1750){
        this.xDirection = 0;
      }
      if (this.y1+yDirection+this.height > 836){
        this.yDirection = 0;
      }
    } else {
      this.yDirection = 0;
      this.xDirection = 0;
    }
    
    this.x1 = this.x1+xDirection;
    this.y1 = this.y1+yDirection;
    
    
    this.boundingBox.x = (int)(Math.round(this.x1));
    this.boundingBox.y = (int)(Math.round(this.y1));
    
  }
  
  public int getHealth(){
    return this.health;
  }
 
  public String getLDirection(){
    return this.lastDirection;
  }
  
  public String getLDirectionL(){
    return this.lastDirectionL;
  }
  
  public void addHealth(int addH){
    this.health += addH;
  }
  
  public void setHealth(int health){
    this.health = health;
  }
  
  public int getX(){
    return (int)(Math.round(this.x1));
  }
  
  public void setX(int x1){
    this.x1 = x1;
  }
  
  public void setY(int y1){
    this.y1 = y1;
  }
  
  public int getY(){
    return (int)(Math.round(this.y1)); //Fix to get middle of the square
  }
  
  public int getXM(){
    return (int)(Math.round(this.x1+15)); //CHANGE FOR CHARACTER SIZE
  }
  
  public int getYM(){
    return (int)(Math.round(this.y1+15));
  }
  
  public int getH(){
    return this.height;
  }
  
  public int getW(){
    return this.width;
  }
  
  public BufferedImage[] getSprites(){
    return this.sprites;
  }
  
  public void changeSprite(int index, BufferedImage sprite){
    this.sprites[index] = sprite;
  }
  
  public BufferedImage getSprite(int index){
    return this.sprites[index];
  }
  
  public void draw(Graphics g) { 
    g.drawImage(getSprite(getCurrentSprite()), getX(), getY(), null);
  }
  
  public int getCurrentSprite(){
    return this.currentSprite;
  }
  
  public void changeCurrentSprite(int spriteNum){
    this.currentSprite = spriteNum;
  }
 
}
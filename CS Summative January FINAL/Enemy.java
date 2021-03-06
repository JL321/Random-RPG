import java.util.Random;
import java.util.ArrayList;

abstract class Enemy extends Character{
  
  private int burnCounter = 0;
  private String status;
  private double lastBurn = System.nanoTime() - 0.5*(1.0E9) ;
  private double currentBurn = System.nanoTime();
  private double currentSD = System.nanoTime();
  private double lastSD = System.nanoTime()-0.5*(1.0E9);
  private double collisionL = System.nanoTime()-0.25*(1.0E9);
  private double collisionC = System.nanoTime();
  private boolean bossType = false;
  
  Random rand = new Random();
  
  Enemy(int health, int defense, double speed, int attack, double x1, double y1, String status, int width, int height, int level, boolean bossType){
    super(health,defense,speed,attack,x1,y1,width,height);
    this.status = status;
    double nKnot = (30*Math.exp(-0.45))/(9+Math.exp(-0.45)); //Logistic function to model coin growth - capped at approximately level 20
    int coinM = (int)(Math.round(nKnot*30/(nKnot+(30-nKnot)*Math.exp(-0.45*level))));
    int min;
    if (coinM < 6){
      min = 1;
      setCoin(rand.nextInt(5)+1);
    } else {
      min = coinM-5;
      setCoin(rand.nextInt(6)+min);
    }
    this.bossType = bossType;
  }
  
  public void updateSD(){
    this.lastSD = System.nanoTime();
  }
  
  public boolean getTypeB(){
    return this.bossType;
  }
  
  public boolean sdCheck(){
    this.currentSD = System.nanoTime();
    if (((this.currentSD-this.lastSD)/1.0E9) >= 0.5){
      return true;
    } else {
      return false;
    }
  }
  
  public void updateCollisionL(){ 
    this.collisionL = System.nanoTime();
  }
  
  //Forces enemies to move back by 25 blocks when collision occurs 
  
  public boolean updateCollisionC(){
    this.collisionC = System.nanoTime();
    if ((this.collisionC-this.collisionL)/1.0E9 >= 0.25){
      return true;
    } else {
      return false;
    }
  }
      
  
  double decreaseSpeed = getSpeed()/10; //Intervals of speed decrease by archer's 3rd ability
  int slowCounter = 0; //Caps the largest slow 
  
  public String getStatus(){
    return this.status;
  }
 
  public void updateBurn(){
    this.lastBurn = System.nanoTime();
  }
  
  public boolean cdCheckB(){
    this.currentBurn = System.nanoTime();
    if ((this.currentBurn - this.lastBurn)/1.0E9 >= 0.5){
      return true;
    } else {
      return false;
    }
  }
  
  public void changeStatus(String status){
    this.status = status;
  }
  
  public int getCounter(){
    return this.burnCounter;
  }
  
  public void resetCounter(){
    this.burnCounter = 0;
  }
  
  public void increaseCounter(){
    this.burnCounter += 1;
  }
  
  public void slowSpeed(){
    if (this.slowCounter <= 5){ //Cap at 5 slows
      setSpeed(getSpeed()-this.decreaseSpeed);
      this.slowCounter += 1;
    }
  }
  
  public String randString(ArrayList <String> directionList){
    Random rand = new Random();
    return directionList.get(rand.nextInt(4)); //Randomly selects a direction from available positions (enemy - enemy knockback)
  }
  
  /* targetFinder
   * Using the current positions of the hero and the enemy, calculate for the angle between the hero and the enemy
   * Use angle to dictate movement, but splitting a cartesian plane into quarter circles
  */
  
  public void targetFinder(int xHero, int yHero){ //Trigonometry based function to find and move towards player location (angle referenced direction)
    int xLine = xHero - getXM();
    int yLine = -1*(yHero - getYM());
    double angle;
    
    if (xLine == 0 && yLine > 0){  
      angle = 90;
    } else if (xLine == 0 && yLine < 0){
      angle = 270;
    } else if (xLine == 0 && yLine == 0){
      angle = 361;
    } else {
      angle = Math.toDegrees(Math.atan(Math.abs(yLine)/Math.abs(xLine))); //Will return 0 for yLine is 0
    }
    
    if (xLine < 0 && yLine <= 0){
      angle += 180;
    } else if (xLine < 0 && yLine > 0){
      angle = 180 - angle;
    } else if (xLine > 0 && yLine < 0){
      angle = 360 - angle;
    }
    
    if (angle <= 45 && angle >= 0 || angle <= 360 && angle > 315){
      move("D");
      this.changeCurrentSprite(2);
    } else if (angle > 45 && angle <= 135){
      move("W");
      this.changeCurrentSprite(3);
    } else if (angle > 135 && angle <= 225){
      move("A");
      this.changeCurrentSprite(0);
    } else if (angle > 225 && angle <= 315){
      move("S");
      this.changeCurrentSprite(1);
    } else {
      move("STAND");
    }
    
  }
  
  //abstract void draw(Graphics g);
 
  public String [] removeDirection(String direction){
    if (direction.equals("A")){
      return new String[]{"A","AW","AS"};
    } else if (direction.equals("AW")){
      return new String[]{"AW","W","A"};
    }else if (direction.equals("W")){
      return new String[]{"AW","W","DW"};
    }else if (direction.equals("DW")){
      return new String[]{"DW","W","D"};
    }else if (direction.equals("D")){
      return new String[]{"DS","D","DW"};
    }else if (direction.equals("DS")){
      return new String[]{"D","DS","S"};
    }else if (direction.equals("AS")){
      return new String[]{"AS","S","A"};
    }else if (direction.equals("S")){
      return new String[]{"S","AS","DS"};
    }else if (direction.equals("W")){
      return new String[]{"DW","W","AW"};
    } else {
      return new String[3];
    }
  }
  
}
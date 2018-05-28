import java.util.ArrayList;

abstract class Hero extends Character{
  
  //Specials represent the current track time, last track time, and whether or not they can be currently used
  
  private long specialLast1, specialLast2, specialLast3, specialLast4, specialCurrent1, specialCurrent2, specialCurrent3, specialCurrent4;
  private boolean useSpecial1 = false;
  private boolean useSpecial2 = false; 
  private boolean useSpecial3 = false;
  private boolean useSpecial4 = false;
  private double specialCount1, specialCount2, specialCount3, specialCount4;
  private int [][] upgradesUsed = new int [2][6]; //Count upgrades and upgrade cost for the checkpoint
  private ArrayList <String> standDirection = new ArrayList <String> (); //Store all directions that the player cannot move into - occupied by an enemy
  
  
  
  Hero(int health, int defense, double speed, int attack, double x1, double y1, int width, int height){
    super(health,defense,speed,attack,x1,y1, width, height);
    this.specialLast1 = System.nanoTime(); //Initializing cool down
    this.specialLast2 = System.nanoTime();
    this.specialLast3 = System.nanoTime();
    this.specialLast4 = System.nanoTime();
    this.specialCurrent1 = System.nanoTime();
    this.specialCurrent2 = System.nanoTime();
    this.specialCurrent3 = System.nanoTime();
    this.specialCurrent4 = System.nanoTime();
    for (int i = 0; i < 6; i++){
      for (int j = 0; j < 2; j++){
        this.upgradesUsed[j][i] = 1;
      }
    }
  }
  
  private double currentBT = System.nanoTime(); //BT for Boost Time
  private double lastBT = System.nanoTime()-5*1.0E9;
  private double currentTime = System.nanoTime();
  private double lastTime = currentTime - 1.0E9;
  private boolean attackNow = false;
  
  public void addToSD(String direction){ //Enemy-hero Collision - adds the last directions of all enemies facing the hero, and prevents hero from moving into them
    boolean indicator = true;
    for (int i = 0; i < standDirection.size(); i++){
      if (direction.equals(standDirection.get(i))){
        indicator = false;
      }
    }
    if (indicator == true){
      standDirection.add(direction);
    }
  }
  
  public void resetSD(){ //Reset per turn
    for (int i = 0; i < standDirection.size(); i++){
      standDirection.remove(i);
    }
  }
  
  public void setSpecialCount(double coolDown, int attack){ // Set new cooldowns for abilities
    if (attack == 1){
      this.specialCount1 = coolDown;
    } else if (attack == 2){
      this.specialCount2 = coolDown;
    } else if (attack == 3){
      this.specialCount3 = coolDown;
    } else if (attack == 4){
      this.specialCount4 = coolDown;
    }
  }
  
  public double getSpecialCount(int attack){ // Retrieve the updated cooldowns (updated from the checkpoint)
    if (attack == 1){
      return this.specialCount1;
    } else if (attack == 2){
      return this.specialCount2;
    } else if (attack == 3){
      return this.specialCount3;
    } else if (attack == 4){
      return this.specialCount4;
    } else {
      return 0;
    }
  }
  
  //Evaluates 
  
  public boolean checkToSD(String direction){ //Checks for whether or not the player can move based upon an enemy-player collision system
    for (int i = 0; i < standDirection.size(); i++){
      if (direction.equals(standDirection.get(i))){
        return true;
      }
    }
    return false;
  }
  
  public void updateBT(){ //Resets counter for buff length
    this.lastBT = this.currentBT;
  }
  
  public boolean checkBT(){
    this.currentBT = System.nanoTime();
    if ((this.currentBT - this.lastBT)/1.0E9 <= 4){ //Buff lasts for 4 seconds 
      return true;
    } else {
      return false;
    }
  }
  
  abstract void statChange(int num); //Method for changing a buffed stat in the subclass - also used for development at checkpoints 
  
  abstract void statReverse(); //Every hero subclass changes a different characteristic, and thus has statReverse, statChange labelled as abstract
  
  //Allow a boolen to be true for every special, so that it may provide a "signal" to gameScreen for activation
  
  public void change1(boolean activate){
    this.useSpecial1 = activate;
  }
  
  public void change2(boolean activate){
    this.useSpecial2 = activate;
  }
  
  public void change3(boolean activate){
    this.useSpecial3 = activate;
  }
  
  public void change4(boolean activate){
    this.useSpecial4 = activate;
  }
  
  public boolean canUse1(){
    return this.useSpecial1;
  } 
  
  public boolean canUse2(){
    return this.useSpecial2;
  }
  
  public boolean canUse3(){
    return this.useSpecial3;
  }
  
  public boolean canUse4(){
    return this.useSpecial4;
  }
  
  //Reset ability timers
  
   public void updateLast1(){
    this.specialLast1 = System.nanoTime();
  }
  
  public void updateLast2(){
    this.specialLast2 = System.nanoTime();
  }
  
  public void updateLast3(){
    this.specialLast3 = System.nanoTime();
  }
  
  public void updateLast4(){
    this.specialLast4 = System.nanoTime();
  }
  
  public void updateLast(){
    this.lastTime = System.nanoTime();
  }
  
  public double timeS(int attack){
    if (attack == 1){
      return (this.specialCurrent1-this.specialLast1);
    } else if (attack == 2){
      return (this.specialCurrent2-this.specialLast2);
    } else if (attack == 3){
      return (this.specialCurrent3-this.specialLast3);
    } else if (attack == 4){
      return (this.specialCurrent4-this.specialLast4);
    } else {
      return 0;
    }
  }
  
  //Consistently update the currentTime of all special abilities for cooldown
  
  public void allCD(){
    this.specialCurrent1 = System.nanoTime();
    this.specialCurrent2 = System.nanoTime();
    this.specialCurrent3 = System.nanoTime();
    this.specialCurrent4 = System.nanoTime();
  }
  
  //4 Cool down checkers independent of one another
  
  public boolean cdCheck(double seconds){ 
    this.currentTime = System.nanoTime();
    if ((this.currentTime-this.lastTime)/1.0E9 >= seconds){ 
      return true;
    } else {
      return false;
    }
  }
  
  public boolean cdCheck1(double seconds){ 
    this.specialCurrent1 = System.nanoTime();
    if ((this.specialCurrent1-this.specialLast1)/1.0E9 >= seconds){ 
      return true;
    } else {
      return false;
    }
  }
  
  public boolean cdCheck2(double seconds){ 
    this.specialCurrent2 = System.nanoTime();
    if ((this.specialCurrent2-this.specialLast2)/1.0E9 >= seconds){ 
      return true;
    } else {
      return false;
    }
  }
  
  public boolean cdCheck3(double seconds){ 
    this.specialCurrent3 = System.nanoTime();
    if ((this.specialCurrent3-this.specialLast3)/1.0E9 >= seconds){ 
      return true;
    } else {
      return false;
    }
  }
  
  public boolean cdCheck4(double seconds){ 
    this.specialCurrent4 = System.nanoTime();
    if ((this.specialCurrent4-this.specialLast4)/1.0E9 >= seconds){ 
      return true;
    } else {
      return false;
    }
  }
  
  public void changeAttack(boolean attack){
    this.attackNow = attack;
  }
  
  public boolean getAttack(){
    return this.attackNow;
  }
  
  public int[][] getUpgradeInfo(){
    return this.upgradesUsed;
  }
  
  public void updateUpgradeInfo(int infoType, int lastPrice, int update){ // 0 = count, 1 = last price
    this.upgradesUsed[infoType][lastPrice] =  update;
  }
  
  abstract boolean specialOne();
  
  abstract boolean specialTwo();
  
  abstract boolean specialThree();
  
  abstract boolean specialFour();

}
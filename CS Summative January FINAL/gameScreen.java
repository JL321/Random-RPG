import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;
import java.util.ArrayList;
import java.util.Random;
import java.io.*; 

/*

class PlayerSprite3 { 
  
  public static void main(String[] args) { 
    GameWindow game= new GameWindow();  
  }
  
}

*/

class GameWindow extends JFrame { 
  
  private JFrame thisFrame;

  
  private BufferedImage [] images = new BufferedImage[4];

  public GameWindow(Hero player, int levelCount) { 
    System.out.println(levelCount);
    this.thisFrame = this;
    setTitle("Simple Game Loop Example");
    setSize(1800,900);  // set the size of my window to 400 by 400 pixels
    this.setLocationRelativeTo(null);
    setResizable(false);  // set my window to allow the user to resize it
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // set the window up to end the program when closed
    getContentPane().add( new GamePanel(player, levelCount));
    pack(); //makes the frame fit the contents
    setVisible(true);
    try {
      images[1] = ImageIO.read(new File("BuffCircle.png"));
      images[2] = ImageIO.read(new File("WinnerMessage.png"));
      images[3] = ImageIO.read(new File("LoserMessage.png"));
      if (player instanceof Knight){
        images[0] = ImageIO.read(new File("MapKnight.png"));
      } else if (player instanceof Ranger){
        images[0] = ImageIO.read(new File("MapRanger.png"));
      } else {
        images[0] = ImageIO.read(new File("MapMage.png"));
      }
    } catch(IOException e) { System.out.println("error loading image");};
  }
  
  class GamePanel extends JPanel implements KeyListener{
    
    //ArrayList of removable objects, alongside variable initialization
    
    ArrayList <Pellet> pelletC = new ArrayList <Pellet>();
    ArrayList <Pellet> pelletH = new ArrayList <Pellet>();
    ArrayList <Spirit> spiritList = new ArrayList <Spirit>();
    ArrayList <Skeleton> skeletonList = new ArrayList <Skeleton>();
    ArrayList <Ninja> ninjaList = new ArrayList <Ninja>();
    ArrayList <Golem> golemList = new ArrayList <Golem>();
    ArrayList <Sword> swordList = new ArrayList <Sword>();
    ArrayList <Rock> rockList = new ArrayList <Rock>();
    
   // private BufferedImage spriteLoss;
    
    private Clock clock;
    private Hero player;
    private boolean endGame = false;
    
    //Keys used for movement
    
    private boolean aKey = false;
    private boolean wKey = false;
    private boolean sKey = false;
    private boolean dKey = false;
    Random rand = new Random();
    private int levelCount;
    
    
    private String heroType;
    int enemyCount;
    
    public GamePanel(Hero player, int levelCount) {  
      setPreferredSize(new Dimension(1800,900));
      setFocusable(true);
      requestFocusInWindow();
      addKeyListener(this);
      clock = new Clock();
      this.enemyCount = 4 + (int)(levelCount/3);
      
      //Tile grid used for appropriate randomization
      
      int[][] entityGrid = new int [15][7];
      
      boolean place = true;
      //1 = Spirit 2 = Ninja 3 = Golem 4 = Skeleton 5= Rock
      
      //Boss creation on level 7, 14, 21
      
      if (levelCount == 7){
        
        entityGrid[2][3] = 6;
        golemList.add(new Golem(200+levelCount*7,40,2,5,214+102.4*2,64+110.2*3,"N",60,60,levelCount,true));
        
      } else if (levelCount == 14){
        
        entityGrid[14][3] = 6;
        spiritList.add(new Spirit(70+levelCount*2,3,3,7,214 + 102.4*14,64+110.2*3,"N",40,40,levelCount,true)); //Reincorporate boolean parameter
        
      } else if (levelCount == 21){
        
        entityGrid[10][3] = 6;
        ninjaList.add(new Ninja(40+levelCount*3,3,3.25,10,214+102.4*13,64+110.2*3,"N",40,40,levelCount,true));
        
      }
      
      //To ensure that at least 1 of every enemy type is created
      
      for (int i = 1; i < 5; i++){
        while (place == true){
          int storeX = rand.nextInt(14)+1;
          int storeY = rand.nextInt(7);
          if (entityGrid[storeX][storeY] == 0){
            entityGrid[storeX][storeY] = i;
            place = false;
          }
        }
        place = true;
      }
        
      //Creating the remaining enemies
      if (levelCount % 7 == 0){
        for (int i = 0; i < this.enemyCount - 5; i++){ //Accounting for the boss
          while (place == true){
            int storeX = rand.nextInt(14)+1;
            int storeY = rand.nextInt(7);
            if (entityGrid[storeX][storeY] == 0){
              entityGrid[storeX][storeY] = rand.nextInt(4)+1;
              place = false;
            }
          }
          place = true;
        }
      }else {
        for (int i = 0; i < this.enemyCount - 4; i++){
          while (place == true){
            int storeX = rand.nextInt(14)+1;
            int storeY = rand.nextInt(7);
            if (entityGrid[storeX][storeY] == 0){
              entityGrid[storeX][storeY] = rand.nextInt(4)+1;
              place = false;
            }
          }
          place = true;
        }
      }
      
      //Generating 3-5 rocks
      
      for (int i = 0; i < rand.nextInt(2)+4; i++){
        while (place == true){
          int storeX = rand.nextInt(15);
          int storeY = rand.nextInt(7);
          if (entityGrid[storeX][storeY] == 0){
            entityGrid[storeX][storeY] = 5;
            place = false;
          }
        }
        place = true;
      }
      
      //Using tiles to add towards the ArrayLists of varying enemy types
      
      for (int i = 0; i < 15; i ++){
        for (int j = 0; j < 7; j++){
          if (entityGrid[i][j] == 1){
            spiritList.add(new Spirit(30+levelCount*2,3,2.0,5,214 + 102.4*i,64+110.2*j,"N",40,40,levelCount,false)); //Reincorporate boolean parameter
          } else if (entityGrid[i][j] == 2){
            ninjaList.add(new Ninja(40+levelCount*3,3,2.5,5,214+102.4*i,64+110.2*j,"N",40,40,levelCount,false));
          } else if (entityGrid[i][j] == 3){
            golemList.add(new Golem(100+levelCount*7,10,1.5,3,214+102.4*i,64+110.2*j,"N",60,60,levelCount,false));
          } else if (entityGrid[i][j] == 4){
            skeletonList.add(new Skeleton(50+levelCount*2,6,2.25,3,214+102.4*i,64+110.2*j,"N",25,50,levelCount,false));
          } else if (entityGrid[i][j] == 5){
            rockList.add(new Rock((int)(214+102.4*i),(int)(64+110.2*j),50,50));
          }
        }
      }

      //Initialize player and conditions
      
      this.player = player;
      this.player.setX(214);
      this.player.setY(420);
      if (player instanceof Knight){
        this.heroType = "Knight";
      } else if (player instanceof Ranger){
        this.heroType = "Ranger";
      } else if (player instanceof Mage){
        this.heroType = "Mage";
      }
      this.levelCount = levelCount;

    }
   
    
    public void paintComponent(Graphics g) { 

      super.paintComponent(g); //required to ensure the panel is correctly redrawn
      //update the content
      if (player.getHealth() <= 0 && levelCount != 22){
        endGame = true;
        g.drawImage(images[3], 0, 0, null);
      }
      
      player.allCD();
      
      //Leads to end screen once level 22 is reached
      if (levelCount == 22){
        endGame = true;
        g.drawImage(images[2], 0, 0, null);
      }
      
      if (levelCount != 22 && player.getHealth() > 0){
        
        //Drawing the map and the pseudo-HUD
        if (levelCount != 22 && player.getHealth() > 0){
          g.drawImage(images[0], 0, 0, null);
          Font f = new Font("Times New Roman", Font.BOLD, 27);
          g.setFont(f);
          //Level
          g.setColor(Color.WHITE); //WHITE
          if (levelCount < 10){
            g.drawString("" + levelCount, 65, 70);
          } else {
            g.drawString("" + levelCount, 55, 70);
          }
          //Health
          g.setColor(Color.RED); //RED
          if (player.getHealth() == 100){
            g.drawString("" + player.getHealth(), 25, 174);
          } else {
            g.drawString("" + player.getHealth(), 33, 174);
          }
          //Coins
          g.setColor(Color.YELLOW); //YELLOW
          g.drawString("" + player.getCoin(), 60, 264);
          //Cooldowns
          Font h = new Font("Times New Roman", Font.BOLD, 23);
          g.setFont(h);
          g.setColor(Color.WHITE); //WHITE
          for (int i = 1; i < 5; i++){ 
            int printNum = 0;
            if ((int)(Math.round((player.getSpecialCount(i)*1.0E9-player.timeS(i))/1.0E9)) > 0){
              printNum = (int)(Math.round((player.getSpecialCount(i)*1.0E9-player.timeS(i))/1.0E9)); //Draws the cooldown of individual abilities
            } 
            g.drawString("Cooldown: " + printNum, 15, 430 + 140*(i-1));
          }
        }
        
        //Graphic for the power boost of the 4th ability 
        
        if (player instanceof Mage && player.getDamageO() != player.getDamage()){
          g.drawImage(images[1], player.getX(), player.getY(), null);
        } else if (player instanceof Ranger && player.getSpeed() != player.getSpeedO()){
          g.drawImage(images[1], player.getX(), player.getY(), null);
        } else if (player instanceof Knight && player.getDefense() != player.getDefenseO()){
          g.drawImage(images[1], player.getX(), player.getY(), null);
        }
        
        //Forces player to temporarily move back upon rock contact- instance isn't visible by the naked eye, but effectively simulates standing still without getting stuck
        
        for (int i = 0; i < rockList.size(); i++){
          if (player.boundingBox.intersects(rockList.get(i).boundingBox)){
            if (player.getLDirection().equals("W")){
              player.move("S");
            } else if (player.getLDirection().equals("S")){
              player.move("W");
            }else if (player.getLDirection().equals("A")){
              player.move("D");
            }else if (player.getLDirection().equals("D")){
              player.move("A");
            }else if (player.getLDirection().equals("AS")){
              player.move("DW");
            }else if (player.getLDirection().equals("AW")){
              player.move("DS");
            }else if (player.getLDirection().equals("DS")){
              player.move("AW");
            }else if (player.getLDirection().equals("DW")){
              player.move("AS");
            }
            player.update(clock.getElapsedTime());
            player.move("STAND");
          }
          rockList.get(i).draw(g);
        }
        
        //Parameters of movement
        
        if (player.getX() >1646 && (player.getY() >= 400 && player.getY() <= 500) && enemyCount == 0){
          System.out.println(levelCount);
          if (levelCount % 2 == 0 || levelCount % 7 == 0 && levelCount != 21){ //Won't trigger at ending
            player.move("STAND");
            new CheckpointUIV2(player,levelCount);
          } else {
            player.move("STAND");
            GameWindow game = new GameWindow(player,levelCount+1);
          }
          thisFrame.dispose();
        }
        if (player.checkToSD(player.getLDirection())){
          player.move("STAND");
        }
        clock.update();
        player.update(clock.getElapsedTime());
        player.resetSD();
        player.draw(g);
        
        g.setColor(Color.RED);
        g.fillRect(1706, 400, 195, 100);
        
        //SPECIAL ONE
        
        if (player.canUse1() == true && player instanceof Ranger){
          player.change1(false);
          
          //Volley in the 3 closest directions to the player's last direction
          
          if (player.getLDirection().equals("W")){
            pelletH.add(new Pellet(player.getXM(), player.getYM(),4,15,15,player.getDamage()+15,"W",clock.getElapsedTime(),"RangerN"));
            pelletH.add(new Pellet(player.getXM(), player.getYM(),4,15,15,player.getDamage()+15,"AW",clock.getElapsedTime(),"RangerN"));
            pelletH.add(new Pellet(player.getXM(), player.getYM(),4,15,15,player.getDamage()+15,"DW",clock.getElapsedTime(),"RangerN"));
          } else if (player.getLDirection().equals("A")){
            pelletH.add(new Pellet(player.getXM(), player.getYM(),4,15,15,player.getDamage()+15,"AS",clock.getElapsedTime(),"RangerN"));
            pelletH.add(new Pellet(player.getXM(), player.getYM(),4,15,15,player.getDamage()+15,"AW",clock.getElapsedTime(),"RangerN"));
            pelletH.add(new Pellet(player.getXM(), player.getYM(),4,15,15,player.getDamage()+15,"A",clock.getElapsedTime(),"RangerN"));
          } else if (player.getLDirection().equals("S")){
            pelletH.add(new Pellet(player.getXM(), player.getYM(),4,15,15,player.getDamage()+15,"S",clock.getElapsedTime(),"RangerN"));
            pelletH.add(new Pellet(player.getXM(), player.getYM(),4,15,15,player.getDamage()+15,"DS",clock.getElapsedTime(),"RangerN"));
            pelletH.add(new Pellet(player.getXM(), player.getYM(),4,15,15,player.getDamage()+15,"AS",clock.getElapsedTime(),"RangerN"));
          } else if (player.getLDirection().equals("D")){
            pelletH.add(new Pellet(player.getXM(), player.getYM(),4,15,15,player.getDamage()+15,"DW",clock.getElapsedTime(),"RangerN"));
            pelletH.add(new Pellet(player.getXM(), player.getYM(),4,15,15,player.getDamage()+15,"DS",clock.getElapsedTime(),"RangerN"));
            pelletH.add(new Pellet(player.getXM(), player.getYM(),4,15,15,player.getDamage()+15,"D",clock.getElapsedTime(),"RangerN"));
          } else if (player.getLDirection().equals("DS")){
            pelletH.add(new Pellet(player.getXM(), player.getYM(),4,15,15,player.getDamage()+15,"D",clock.getElapsedTime(),"RangerN"));
            pelletH.add(new Pellet(player.getXM(), player.getYM(),4,15,15,player.getDamage()+15,"DS",clock.getElapsedTime(),"RangerN"));
            pelletH.add(new Pellet(player.getXM(), player.getYM(),4,15,15,player.getDamage()+15,"S",clock.getElapsedTime(),"RangerN"));
          }else if (player.getLDirection().equals("DW")){
            pelletH.add(new Pellet(player.getXM(), player.getYM(),4,15,15,player.getDamage()+15,"D",clock.getElapsedTime(),"RangerN"));
            pelletH.add(new Pellet(player.getXM(), player.getYM(),4,15,15,player.getDamage()+15,"DW",clock.getElapsedTime(),"RangerN"));
            pelletH.add(new Pellet(player.getXM(), player.getYM(),4,15,15,player.getDamage()+15,"W",clock.getElapsedTime(),"RangerN"));
          }else if (player.getLDirection().equals("AS")){
            pelletH.add(new Pellet(player.getXM(), player.getYM(),4,15,15,player.getDamage()+15,"A",clock.getElapsedTime(),"RangerN"));
            pelletH.add(new Pellet(player.getXM(), player.getYM(),4,15,15,player.getDamage()+15,"AS",clock.getElapsedTime(),"RangerN"));
            pelletH.add(new Pellet(player.getXM(), player.getYM(),4,15,15,player.getDamage()+15,"S",clock.getElapsedTime(),"RangerN"));
          }else if (player.getLDirection().equals("AW")){
            pelletH.add(new Pellet(player.getXM(), player.getYM(),4,15,15,player.getDamage()+15,"A",clock.getElapsedTime(),"RangerN"));
            pelletH.add(new Pellet(player.getXM(), player.getYM(),4,15,15,player.getDamage()+15,"AW",clock.getElapsedTime(),"RangerN"));
            pelletH.add(new Pellet(player.getXM(), player.getYM(),4,15,15,player.getDamage()+15,"W",clock.getElapsedTime(),"RangerN"));
          }
        }
        
        //A is a pellet that expands into an AOE blast upon contact
        
        else if (player.canUse1() == true && player instanceof Mage){
          player.change1(false);
          pelletH.add(new Pellet(player.getXM(), player.getYM(),4,15,15,player.getDamage()+15,player.getLDirection(),clock.getElapsedTime(),"A"));
        }
        
        //SPECIAL TWO
        
        //Piercing negates a bit of enemy armor in damage calculation (P for piercing) 
          
        if (player.canUse2() == true && player instanceof Ranger){
          player.change2(false);
          pelletH.add(new Pellet(player.getXM(), player.getYM(),4,15,15,player.getDamage()+10,player.getLDirection(),clock.getElapsedTime(),"P"));
        }
        
        //Attacks in 3 squares ahead of the player
        
        if (player.canUse2() == true && player instanceof Knight){
          player.change2(false);
          swordList.add(new Sword(((Knight)player).attack(30)[0],((Knight)player).attack(30)[1],180,45,player.getLDirectionL(), player.getDamage()+30, "C")); //C for Cone
        }
        
        //Burns the enemy for a short period of time, doing continual damage
        
        if (player.canUse2() == true && player instanceof Mage){
          player.change2(false);
          pelletH.add(new Pellet(player.getXM(), player.getYM(),4,15,15,player.getDamage()-20,player.getLDirection(),clock.getElapsedTime(),"B"));
        }
        
        //SPECIAL THREE
        
        //Spinning attack
        
        if (player.canUse3() == true && player instanceof Knight){
          player.change3(false);
          swordList.add(new Sword(player.getX()-45,player.getY()-45,150,150,"STAND", player.getDamage()+15, "S")); //S for Spin
        }
        
        //Spinning Knockback attack
        
        if (player.canUse3() == true && player instanceof Mage){
          player.change3(false);
          swordList.add(new Sword(player.getXM()-45, player.getYM()-45,150,150,"STAND",0,"K"));
          ((Mage)player).updateLastM3();
        }
        
        //Checks for whether or not the boost offered by ability 4 should be reverted
        
        if (player.checkBT() == false){ 
          player.statReverse();
        }
        
        //Basic attacks
        
        if (player instanceof Knight){
          if (player.getAttack() == true){
            if (player.canUse1() == true){
              player.change1(false);
              player.changeAttack(false);
              swordList.add(new Sword(((Knight)player).attack(30)[0],((Knight)player).attack(20)[1],30,60,player.getLDirectionL(), player.getDamage()+50, "N"));
            } else {
              player.changeAttack(false);
              swordList.add(new Sword(((Knight)player).attack(30)[0],((Knight)player).attack(20)[1],30,60,player.getLDirectionL(), player.getDamage(), "N"));
            }
          } 
        } else if (player instanceof Ranger){
          if (player.getAttack() == true){
            player.changeAttack(false);
            if (((Ranger)player).durationThree() == true){
              pelletH.add(new Pellet(player.getXM(),player.getYM(),4,15,15,player.getDamage(),player.getLDirection(),clock.getElapsedTime(),"S"));
            } else {
              pelletH.add(new Pellet(player.getXM(),player.getYM(),4,15,15,player.getDamage(),player.getLDirection(),clock.getElapsedTime(),"RangerN"));
            }
          }
        } else if (player instanceof Mage){ //Currently identical to the spirit class, but will later require an additional parameter for its constructor (in terms of determining what to draw)
          if (player.getAttack() == true){
            player.changeAttack(false);
            pelletH.add(new Pellet(player.getXM(),player.getYM(),4,15,15,player.getDamage(),player.getLDirection(),clock.getElapsedTime(),"MageN"));
          }
        }
        
        //Considers options based on sword (AOE for mage is also a part of the class)
        
        for (int i = 0; i < swordList.size(); i++){
          if (((swordList.get(i)).getIndicator()).equals("S") || (swordList.get(i)).getIndicator().equals("K")){
            (swordList.get(i)).updateS3(player.getX(),player.getY());
          } else if (!((swordList.get(i)).getIndicator()).equals("M")){
            (swordList.get(i)).update(player.getX(),player.getY());
          } 
          (swordList.get(i)).draw(g);
          if (((swordList.get(i)).getIndicator()).equals("C") && ((Knight)player).cdCheckS2() == true){ //Independent timer required
            swordList.remove(i);
          } else if (player.cdCheck(0.5) == true && ((swordList.get(i)).getIndicator()).equals("N")){ 
            swordList.remove(i);
          } else if (player instanceof Knight){
            if (((Knight)player).cdCheckS3() == true && ((swordList.get(i)).getIndicator()).equals("S")){
              swordList.remove(i);
            }
          } else if (player instanceof Mage){
            if (((Mage)player).cdCheckM() == true && ((swordList.get(i)).getIndicator()).equals("M")){
              swordList.remove(i);
            } else if (((Mage)player).cdCheckM3() == true && ((swordList.get(i)).getIndicator()).equals("K")){
              swordList.remove(i);
            }
          } 
        } 
        
        //Checks for pellet collision- updates position too
        
        for (int i = 0; i < pelletH.size(); i++){
          (pelletH.get(i)).update(heroType);
          (pelletH.get(i)).draw(g);
          for (int j = 0; j < rockList.size(); j++){
            if (pelletH.get(i).boundingBox.intersects(rockList.get(j).boundingBox)){;
              pelletH.get(i).killP();
            }
          }
          if ((pelletH.get(i)).statusA() == false){
            pelletH.remove(i);
          }
        }
        
        //Spirit SECTION
        
        //Note that most of the functions of every enemy will be repeated - only a few key features will be mentioned per comment
        
        for (int j = 0; j < spiritList.size(); j++){
          
          if ((spiritList.get(j)).updateCollisionC() == true){ //Priotization of enemy-enemy collision
            (spiritList.get(j)).targetFinder(player.getXM(),player.getYM());
          }
          
          boolean randMove = false;
          String removeDirection = "STAND"; //Used for direction
          
          ArrayList <String> directionList = new ArrayList <String>();
          directionList.add("W");
          directionList.add("A");
          directionList.add("S");
          directionList.add("D");
          directionList.add("AW");
          directionList.add("AS");
          directionList.add("DW");
          directionList.add("DS");
          
          //List of available directions (used for enemy-enemy boundries)
          
          //Checks arrayList of every other class for class specific collision
          
          for (int i = 0; i < ninjaList.size(); i++){
            if ((ninjaList.get(i)).boundingBox.intersects((spiritList.get(j)).boundingBox)){
              randMove = true; //prevents further instances of checking
              removeDirection = (spiritList.get(j)).getLDirection(); //Marks the last direction of the spirit that led to the ninja (repeated for other enemies)
            }
          }
          
          if (randMove == false){
            for (int i = 0; i < golemList.size(); i++){
              if ((golemList.get(i)).boundingBox.intersects((spiritList.get(j)).boundingBox)){
                randMove = true;
                removeDirection = (spiritList.get(j)).getLDirection();
              }
            }
          }
          
          if (randMove == false){
            for (int i = 0; i < skeletonList.size(); i++){
              if ((skeletonList.get(i)).boundingBox.intersects((spiritList.get(j)).boundingBox)){
                randMove = true;
                removeDirection = (spiritList.get(j)).getLDirection();
              }
            }
          }
          
          if (randMove == false){
            for (int i = 0; i < spiritList.size(); i++){
              if (i != j){
                if ((spiritList.get(i)).boundingBox.intersects((spiritList.get(j)).boundingBox)){
                  randMove = true;
                  removeDirection = (spiritList.get(j)).getLDirection();
                }
              }
            }
          }
          
          String [] list = new String[3]; //Available positions
          list = (spiritList.get(j)).removeDirection(removeDirection);
          
          if (randMove == true){ //Chooses a direction to move to
            for (int i = 0; i < directionList.size(); i++){
              if (directionList.get(i).equals(list[0]) || directionList.get(i).equals(list[1]) || directionList.get(i).equals(list[2])){
                directionList.remove(i);
              }
            }
            (spiritList.get(j)).updateCollisionL();
            (spiritList.get(j)).move((spiritList.get(j)).randString(directionList));
          }
          
          //Priotized over the Hero
          
          //Checks for rock collision (similar system as Hero checks)
          
          for (int k = 0; k < rockList.size(); k++){
            if ((spiritList.get(j)).boundingBox.intersects(rockList.get(k).boundingBox)){
              if ((spiritList.get(j)).getLDirection().equals("W")){
                (spiritList.get(j)).move("S");
                (spiritList.get(j)).update(clock.getElapsedTime());
              } else if ((spiritList.get(j)).getLDirection().equals("S")){
                (spiritList.get(j)).move("W");
                (spiritList.get(j)).update(clock.getElapsedTime());
              }else if ((spiritList.get(j)).getLDirection().equals("A")){
                (spiritList.get(j)).move("D");
                (spiritList.get(j)).update(clock.getElapsedTime());
              }else if ((spiritList.get(j)).getLDirection().equals("D")){
                (spiritList.get(j)).move("A");
                (spiritList.get(j)).update(clock.getElapsedTime());
              }
              // (spiritList.get(j)).update(clock.getElapsedTime());
              (spiritList.get(j)).move("STAND");
            }
          }
          
          //If burned 8 times, go back to normal
          
          if ((spiritList.get(j)).getCounter() >= 8){
            (spiritList.get(j)).changeStatus("N");
            (spiritList.get(j)).resetCounter();
          }
          
          //Burning process
          
          if ((spiritList.get(j)).getStatus().equals("B") && (spiritList.get(j)).cdCheckB() == true){
            (spiritList.get(j)).addHealth((-player.getDamage())/8);
            (spiritList.get(j)).updateBurn();
            (spiritList.get(j)).increaseCounter();
          }
          
          //If within a set distance of player, stop moving 
          
          if (((Spirit)spiritList.get(j)).checkMove(player.getXM(),player.getYM(),200) == true){
            (spiritList.get(j)).move("STAND");
          }
          
          if ((spiritList.get(j)).cdCheck() == true){ //Attack
            (spiritList.get(j)).updateLast();
            this.pelletC.add(new Pellet((spiritList.get(j)).getXM(),(spiritList.get(j)).getYM(),3,15,15,(spiritList.get(j)).getDamage(),clock.getElapsedTime(),"N",player.getXM() - (spiritList.get(j)).getXM(), player.getYM() - (spiritList.get(j)).getYM()));
          }
          
          
          
          (spiritList.get(j)).update(clock.getElapsedTime());
          //draw the screen
          (spiritList.get(j)).draw(g);
          
          //Checks for sword collision
          
          for (int i = 0; i < swordList.size(); i++){
            
            if (((spiritList.get(j)).boundingBox).intersects((swordList.get(i)).boundingBox) && (swordList.get(i)).getIndicator().equals("K")){
              if (player.getLDirection().equals("W")){
                if ((spiritList.get(j)).getY() - 45 <= 64){ //Knockback restricts being pushed out of the arena
                  (spiritList.get(j)).setY(64);
                } else {
                  (spiritList.get(j)).setY((spiritList.get(j)).getY() - 45);
                }
              } else if (player.getLDirection().equals("S")){
                if ((spiritList.get(j)).getY() + 45 >= 836){
                  (spiritList.get(j)).setY(796);
                } else {
                  (spiritList.get(j)).setY((spiritList.get(j)).getY() + 45);
                }
              } else if (player.getLDirection().equals("A")){
                if ((spiritList.get(j)).getX() - 45 <= 214){
                  (spiritList.get(j)).setX(214);
                } else {
                  (spiritList.get(j)).setX((spiritList.get(j)).getX() - 45);
                }
              } else if (player.getLDirection().equals("D")){
                if ((spiritList.get(j)).getX() + 45 >= 1750){
                  (spiritList.get(j)).setX(1710);
                } else {
                  (spiritList.get(j)).setX((spiritList.get(j)).getX() + 45);
                }       
              }
            }
            else if (((spiritList.get(j)).boundingBox).intersects((swordList.get(i)).boundingBox) && (spiritList.get(j)).sdCheck() == true){
              (spiritList.get(j)).addHealth(-(int)(Math.round((double)(100-spiritList.get(j).getDefense())/100*(double)(swordList.get(i)).getDamage()))); //Inflicting damage
              (spiritList.get(j)).updateSD();
            }
            
          }
          
          //Pellet Scanning
          
          for (int i = 0; i < pelletH.size();i++){
            if (((spiritList.get(j)).boundingBox).intersects((pelletH.get(i)).boundingBox)){
              if ((pelletH.get(i)).getType().equals("P")){ //Piercing reduces armor to 90
                (spiritList.get(j)).addHealth(-(int)(Math.round((double)(90-spiritList.get(j).getDefense())/100*(double)(pelletH.get(i)).getDamage())));
                pelletH.remove(i);
              } else if ((pelletH.get(i)).getType().equals("RangerN") || (pelletH.get(i)).getType().equals("MageN")){
                (spiritList.get(j)).addHealth(-(int)(Math.round((double)(100-spiritList.get(j).getDefense())/100*(double)(pelletH.get(i)).getDamage())));
                pelletH.remove(i); // Normal Shot
              } else if ((pelletH.get(i)).getType().equals("S")){
                (spiritList.get(j)).addHealth(-(int)(Math.round((double)(90-spiritList.get(j).getDefense())/100*(double)(pelletH.get(i)).getDamage())));
                (spiritList.get(j)).slowSpeed(); // S = slow
                pelletH.remove(i);
              } else if ((pelletH.get(i)).getType().equals("A")){ 
                (spiritList.get(j)).addHealth(-(int)(Math.round((double)(100-spiritList.get(j).getDefense())/100*(double)(pelletH.get(i)).getDamage())));
                ((Mage)player).updateLastM();
                swordList.add(new Sword((pelletH.get(i)).getXM()-45, (pelletH.get(i)).getYM()-45, 150,150,"STAND",player.getDamage()+10, "M")); //Creates AOE
                pelletH.remove(i);
              } else if ((pelletH.get(i)).getType().equals("B")){ 
                (spiritList.get(j)).addHealth(-(int)(Math.round((double)(100-spiritList.get(j).getDefense())/100*(double)((pelletH.get(i)).getDamage()-5))));
                (spiritList.get(j)).changeStatus("B"); //Burn status
                (spiritList.get(j)).updateBurn();
                pelletH.remove(i);
              } 
            }
          }
          
          if ((spiritList.get(j)).getHealth() <= 0){//Remove archer
            enemyCount -= 1;
            player.setCoin(spiritList.get(j).getCoin()+player.getCoin()); //Player gains coins
            spiritList.remove(j); 
          }
          
        }
        
        //NINJA SECTION (Repeat of Archer minus cloaking)
        
        for (int a = 0; a < ninjaList.size(); a++){
          
          if ((ninjaList.get(a)).getCounter() >= 8){
            (ninjaList.get(a)).changeStatus("N");
            (ninjaList.get(a)).resetCounter();
          }
          
          if ((ninjaList.get(a)).getStatus().equals("B") && (ninjaList.get(a)).cdCheckB() == true){
            (ninjaList.get(a)).addHealth((-player.getDamage())/8);
            (ninjaList.get(a)).updateBurn();
            (ninjaList.get(a)).increaseCounter();
          }
          
          if (ninjaList.get(a).updateCollisionC() == true){ //Used for tracking temporary backward movement
            (ninjaList.get(a)).targetFinder(player.getXM(),player.getYM());
          }
          
          //COLLISION
          
          boolean randMove = false;
          String removeDirection = "STAND";
          ArrayList <String> directionList = new ArrayList <String>();
          directionList.add("W");
          directionList.add("A");
          directionList.add("S");
          directionList.add("D");
          directionList.add("AW");
          directionList.add("AS");
          directionList.add("DW");
          directionList.add("DS");
          
          for (int i = 0; i < spiritList.size(); i++){
            if ((spiritList.get(i)).boundingBox.intersects((ninjaList.get(a)).boundingBox)){
              randMove = true;
              removeDirection = (ninjaList.get(a)).getLDirection();
            }
          }
          
          if (randMove == false){
            for (int i = 0; i < golemList.size(); i++){
              if ((golemList.get(i)).boundingBox.intersects((ninjaList.get(a)).boundingBox)){
                randMove = true;
                removeDirection = (ninjaList.get(a)).getLDirection();
              }
            }
          }
          
          if (randMove == false){
            for (int i = 0; i < skeletonList.size(); i++){
              if ((skeletonList.get(i)).boundingBox.intersects((ninjaList.get(a)).boundingBox)){
                randMove = true;
                removeDirection = (ninjaList.get(a)).getLDirection();
              }
            }
          }
          
          if (randMove == false){
            for (int i = 0; i < ninjaList.size(); i++){
              if (i != a){
                if ((ninjaList.get(i)).boundingBox.intersects((ninjaList.get(a)).boundingBox)){
                  randMove = true;
                  removeDirection = (ninjaList.get(a)).getLDirection();
                }
              }
            }
          }
          
          String [] list = new String[3];
          list = (ninjaList.get(a)).removeDirection(removeDirection);
          
          if (randMove == true){  
            for (int i = 0; i < directionList.size(); i++){
              if (directionList.get(i).equals(list[0]) || directionList.get(i).equals(list[1]) || directionList.get(i).equals(list[2])){
                directionList.remove(i);
              }
            }
            (ninjaList.get(a)).move((ninjaList.get(a)).randString(directionList));
            (ninjaList.get(a)).updateCollisionL(); 
          }
          
          //COLLISION END
          
          for (int k = 0; k < rockList.size(); k++){
            if ((ninjaList.get(a)).boundingBox.intersects(rockList.get(k).boundingBox)){
              if ((ninjaList.get(a)).getLDirection().equals("W")){
                (ninjaList.get(a)).update(clock.getElapsedTime());
                (ninjaList.get(a)).move("S");
              } 
              if ((ninjaList.get(a)).getLDirection().equals("S")){
                (ninjaList.get(a)).update(clock.getElapsedTime());
                (ninjaList.get(a)).move("W");
              }
              if ((ninjaList.get(a)).getLDirection().equals("A")){
                (ninjaList.get(a)).update(clock.getElapsedTime());
                (ninjaList.get(a)).move("D");
              }
              if ((ninjaList.get(a)).getLDirection().equals("D")){
                (ninjaList.get(a)).update(clock.getElapsedTime());
                (ninjaList.get(a)).move("A");
              }
              (ninjaList.get(a)).move("STAND");
            }
          }
          
          
          if (((ninjaList.get(a)).boundingBox).intersects(player.boundingBox)){
            player.addToSD((ninjaList.get(a)).getLDirection());
            (ninjaList.get(a)).move("STAND");
            if ((ninjaList.get(a)).bufferCheck() == true){
              (ninjaList.get(a)).updateBuffer();
              player.addHealth(-(ninjaList.get(a)).getDamage());
            }
          }
          
          if ((ninjaList.get(a)).cdCheck() == false){ //3 second intervals of invisiblity, 1 second durection
            (ninjaList.get(a)).draw(g);
          }
          
          (ninjaList.get(a)).update(clock.getElapsedTime());
          
          for (int i = 0; i < swordList.size(); i++){
            
            if (((ninjaList.get(a)).boundingBox).intersects((swordList.get(i)).boundingBox) && (swordList.get(i)).getIndicator().equals("K")){
              if (player.getLDirection().equals("W")){
                if ((ninjaList.get(a)).getY() - 45 <= 64){
                  (ninjaList.get(a)).setY(64);
                } else {
                  (ninjaList.get(a)).setY((ninjaList.get(a)).getY() - 45);
                }
              } else if (player.getLDirection().equals("S")){
                if ((ninjaList.get(a)).getY() + 45 >= 836){
                  (ninjaList.get(a)).setY(796);
                } else {
                  (ninjaList.get(a)).setY((ninjaList.get(a)).getY() + 45);
                }
              } else if (player.getLDirection().equals("A")){
                if ((ninjaList.get(a)).getX() - 45 <= 214){
                  (ninjaList.get(a)).setX(214);
                } else {
                  (ninjaList.get(a)).setX((ninjaList.get(a)).getX() - 45);
                }
              } else if (player.getLDirection().equals("D")){
                if ((ninjaList.get(a)).getX() + 45 >= 1750){
                  (ninjaList.get(a)).setX(1710);
                } else {
                  (ninjaList.get(a)).setX((ninjaList.get(a)).getX() + 45);
                }       
              }
            } else if (((ninjaList.get(a)).boundingBox).intersects((swordList.get(i)).boundingBox) && (ninjaList.get(a)).sdCheck() == true){
              (ninjaList.get(a)).addHealth(-(int)(Math.round((double)(100-ninjaList.get(a).getDefense())/100*(double)(swordList.get(i)).getDamage())));
              (ninjaList.get(a)).updateSD();
            }
            
          }
          
          for (int i = 0; i < pelletH.size();i++){
            if (((ninjaList.get(a)).boundingBox).intersects((pelletH.get(i)).boundingBox)){
              if ((pelletH.get(i)).getType().equals("P")){
                (ninjaList.get(a)).addHealth(-(int)(Math.round((double)(90-ninjaList.get(a).getDefense())/100*(double)(pelletH.get(i)).getDamage())));
                pelletH.remove(i);
              } else if ((pelletH.get(i)).getType().equals("RangerN") || (pelletH.get(i)).getType().equals("MageN")){
                (ninjaList.get(a)).addHealth(-(int)(Math.round((double)(100-ninjaList.get(a).getDefense())/100*(double)(pelletH.get(i)).getDamage())));
                pelletH.remove(i);
              } else if ((pelletH.get(i)).getType().equals("S")){
                (ninjaList.get(a)).addHealth(-(int)(Math.round((double)(90-ninjaList.get(a).getDefense())/100*(double)(pelletH.get(i)).getDamage())));
                (ninjaList.get(a)).slowSpeed();
                pelletH.remove(i);
              } else if ((pelletH.get(i)).getType().equals("A")){
                (ninjaList.get(a)).addHealth(-(int)(Math.round((double)(100-ninjaList.get(a).getDefense())/100*(double)(pelletH.get(i)).getDamage())));
                ((Mage)player).updateLastM();
                swordList.add(new Sword((pelletH.get(i)).getXM()-45, (pelletH.get(i)).getYM()-45, 150,150,"STAND",player.getDamage()+10, "M"));
                pelletH.remove(i);
              } else if ((pelletH.get(i)).getType().equals("B")){
                (ninjaList.get(a)).addHealth(-(int)(Math.round((double)(100-ninjaList.get(a).getDefense())/100*(double)((pelletH.get(i)).getDamage()-5))));
                (ninjaList.get(a)).changeStatus("B");
                (ninjaList.get(a)).updateBurn();
                pelletH.remove(i);
              } 
            }
          }
          
          
          if ((ninjaList.get(a)).getHealth() <= 0){
            enemyCount -= 1;
            player.setCoin(ninjaList.get(a).getCoin()+player.getCoin());
            ninjaList.remove(a);
          }
          
        }
        
        //GOLEM SECTION
        
        for (int a = 0; a < golemList.size(); a++){
          
          if ((golemList.get(a)).getCounter() >= 8){
            (golemList.get(a)).changeStatus("N");
            (golemList.get(a)).resetCounter();
          }
          
          if ((golemList.get(a)).getStatus().equals("B") && (golemList.get(a)).cdCheckB() == true){
            (golemList.get(a)).addHealth((-player.getDamage())/8);
            (golemList.get(a)).updateBurn();
            (golemList.get(a)).increaseCounter();
          }
          
          if (golemList.get(a).updateCollisionC() == true){
            (golemList.get(a)).targetFinder(player.getXM(),player.getYM());
          }
          
          //COLLISION
          
          boolean randMove = false;
          String removeDirection = "STAND";
          ArrayList <String> directionList = new ArrayList <String>();
          directionList.add("W");
          directionList.add("A");
          directionList.add("S");
          directionList.add("D");
          directionList.add("AW");
          directionList.add("AS");
          directionList.add("DW");
          directionList.add("DS");
          
          for (int i = 0; i < spiritList.size(); i++){
            if ((spiritList.get(i)).boundingBox.intersects((golemList.get(a)).boundingBox)){
              randMove = true;
              removeDirection = (spiritList.get(i)).getLDirection();
            }
          }
          
          if (randMove == false){
            for (int i = 0; i < ninjaList.size(); i++){
              if ((ninjaList.get(i)).boundingBox.intersects((golemList.get(a)).boundingBox)){
                randMove = true;
                removeDirection = (golemList.get(a)).getLDirection();
              }
            }
          }
          
          if (randMove == false){
            for (int i = 0; i < skeletonList.size(); i++){
              if ((skeletonList.get(i)).boundingBox.intersects((golemList.get(a)).boundingBox)){
                randMove = true;
                removeDirection = (golemList.get(a)).getLDirection();
              }
            }
          }
          
          if (randMove == false){
            for (int i = 0; i < golemList.size(); i++){
              if (i != a){
                if ((golemList.get(i)).boundingBox.intersects((golemList.get(a)).boundingBox)){
                  randMove = true;
                  removeDirection = (golemList.get(a)).getLDirection();
                }
              }
            }
          }
          
          String [] list = new String[3];
          list = (golemList.get(a)).removeDirection(removeDirection);
          
          if (randMove == true){  
            for (int i = 0; i < directionList.size(); i++){
              if (directionList.get(i).equals(list[0]) || directionList.get(i).equals(list[1]) || directionList.get(i).equals(list[2])){
                directionList.remove(i);
              }
            }
            (golemList.get(a)).move((golemList.get(a)).randString(directionList));
            (golemList.get(a)).updateCollisionL();
          }
          
          //COLLISION END
          
          for (int k = 0; k < rockList.size(); k++){
            if ((golemList.get(a)).boundingBox.intersects(rockList.get(k).boundingBox)){
              if ((golemList.get(a)).getLDirection().equals("W")){
                (golemList.get(a)).move("S");
                (golemList.get(a)).update(clock.getElapsedTime());
              }
              if ((golemList.get(a)).getLDirection().equals("S")){
                (golemList.get(a)).move("W");
                (golemList.get(a)).update(clock.getElapsedTime());
              }
              if ((golemList.get(a)).getLDirection().equals("A")){
                (golemList.get(a)).move("D");
                (golemList.get(a)).update(clock.getElapsedTime());
              }
              if ((golemList.get(a)).getLDirection().equals("D")){
                (golemList.get(a)).move("A");
                (golemList.get(a)).update(clock.getElapsedTime());
              }
              //(golemList.get(a)).update(clock.getElapsedTime());
              (golemList.get(a)).move("STAND");
            }
          }
          
          
          if (((golemList.get(a)).boundingBox).intersects(player.boundingBox)){
            player.addToSD((golemList.get(a)).getLDirection());
            (golemList.get(a)).move("STAND");
            if ((golemList.get(a)).bufferCheck() == true){
              (golemList.get(a)).updateBuffer();
              player.addHealth(-(golemList.get(a)).getDamage());
            }
          }
          
          (golemList.get(a)).draw(g);
          
          (golemList.get(a)).update(clock.getElapsedTime());
          
          for (int i = 0; i < swordList.size(); i++){
            
            if (((golemList.get(a)).boundingBox).intersects((swordList.get(i)).boundingBox) && (swordList.get(i)).getIndicator().equals("K")){
              if (player.getLDirection().equals("W")){
                if ((golemList.get(a)).getY() - 45 <= 64){
                  (golemList.get(a)).setY(64);
                } else {
                  (golemList.get(a)).setY((golemList.get(a)).getY() - 45);
                }
              } else if (player.getLDirection().equals("S")){
                if ((golemList.get(a)).getY() + 45 >= 836){
                  (golemList.get(a)).setY(796);
                } else {
                  (golemList.get(a)).setY((golemList.get(a)).getY() + 45);
                }
              } else if (player.getLDirection().equals("A")){
                if ((golemList.get(a)).getX() - 45 <= 214){
                  (golemList.get(a)).setX(214);
                } else {
                  (golemList.get(a)).setX((golemList.get(a)).getX() - 45);
                }
              } else if (player.getLDirection().equals("D")){
                if ((golemList.get(a)).getX() + 45 >= 1750){
                  (golemList.get(a)).setX(1710);
                } else {
                  (golemList.get(a)).setX((golemList.get(a)).getX() + 45);
                }       
              }
            } else if (((golemList.get(a)).boundingBox).intersects((swordList.get(i)).boundingBox) && (golemList.get(a)).sdCheck() == true){
              (golemList.get(a)).addHealth(-(int)(Math.round((double)(100-golemList.get(a).getDefense())/100*(double)(swordList.get(i)).getDamage())));
              (golemList.get(a)).updateSD();
            }
            
          }
          
          for (int i = 0; i < pelletH.size();i++){
            if (((golemList.get(a)).boundingBox).intersects((pelletH.get(i)).boundingBox)){
              if ((pelletH.get(i)).getType().equals("P")){
                (golemList.get(a)).addHealth(-(int)(Math.round((double)(90-golemList.get(a).getDefense())/100*(double)(pelletH.get(i)).getDamage())));
                pelletH.remove(i);
              } else if ((pelletH.get(i)).getType().equals("RangerN") || (pelletH.get(i)).getType().equals("MageN")){
                (golemList.get(a)).addHealth(-(int)(Math.round((double)(100-golemList.get(a).getDefense())/100*(double)(pelletH.get(i)).getDamage())));
                pelletH.remove(i);
              } else if ((pelletH.get(i)).getType().equals("S")){
                (golemList.get(a)).addHealth(-(int)(Math.round((double)(90-golemList.get(a).getDefense())/100*(double)(pelletH.get(i)).getDamage())));
                (golemList.get(a)).slowSpeed();
                pelletH.remove(i);
              } else if ((pelletH.get(i)).getType().equals("A")){
                (golemList.get(a)).addHealth(-(int)(Math.round((double)(100-golemList.get(a).getDefense())/100*(double)(pelletH.get(i)).getDamage())));
                ((Mage)player).updateLastM();
                swordList.add(new Sword((pelletH.get(i)).getXM()-45, (pelletH.get(i)).getYM()-45, 150,150,"STAND",player.getDamage()+10, "M"));
                pelletH.remove(i);
              } else if ((pelletH.get(i)).getType().equals("B")){
                (golemList.get(a)).addHealth(-(int)(Math.round((double)(100-golemList.get(a).getDefense())/100*(double)((pelletH.get(i)).getDamage()-5))));
                (golemList.get(a)).changeStatus("B");
                (golemList.get(a)).updateBurn();
                pelletH.remove(i);
              } 
            }
          }
          
          
          if ((golemList.get(a)).getHealth() <= 0){
            enemyCount -= 1;
            player.setCoin(golemList.get(a).getCoin()+player.getCoin());
            golemList.remove(a);
          }
        }
        
        //SKELETON SECTION (Unique: Regeneration)
        
        for (int a = 0; a < skeletonList.size(); a++){
          
          if ((skeletonList.get(a)).getCounter() >= 8){
            (skeletonList.get(a)).changeStatus("N");
            (skeletonList.get(a)).resetCounter();
          }
          
          if ((skeletonList.get(a)).getStatus().equals("B") && (skeletonList.get(a)).cdCheckB() == true){
            (skeletonList.get(a)).addHealth((-player.getDamage())/8);
            (skeletonList.get(a)).updateBurn();
            (skeletonList.get(a)).increaseCounter();
          }
          
          if (!((skeletonList.get(a)).getStatus().equals("D")) && skeletonList.get(a).updateCollisionC() == true){
            (skeletonList.get(a)).targetFinder(player.getXM(),player.getYM());
          }
          
          //COLLISION
          
          boolean randMove = false;
          String removeDirection = "STAND";
          ArrayList <String> directionList = new ArrayList <String>();
          directionList.add("W");
          directionList.add("A");
          directionList.add("S");
          directionList.add("D");
          directionList.add("AW");
          directionList.add("AS");
          directionList.add("DW");
          directionList.add("DS");
          
          for (int i = 0; i < spiritList.size(); i++){
            if ((spiritList.get(i)).boundingBox.intersects((skeletonList.get(a)).boundingBox)){
              randMove = true;
              removeDirection = (skeletonList.get(a)).getLDirection();
            }
          }
          
          if (randMove == false){
            for (int i = 0; i < ninjaList.size(); i++){
              if ((ninjaList.get(i)).boundingBox.intersects((skeletonList.get(a)).boundingBox)){
                randMove = true;
                removeDirection = (skeletonList.get(a)).getLDirection();
              }
            }
          }
          
          if (randMove == false){
            for (int i = 0; i < golemList.size(); i++){
              if ((golemList.get(i)).boundingBox.intersects((skeletonList.get(a)).boundingBox)){
                randMove = true;
                removeDirection = (skeletonList.get(a)).getLDirection();
              }
            }
          }
          
          if (randMove == false){
            for (int i = 0; i < skeletonList.size(); i++){
              if (i != a){
                if ((skeletonList.get(i)).boundingBox.intersects((skeletonList.get(a)).boundingBox)){
                  randMove = true;
                  removeDirection = (skeletonList.get(a)).getLDirection();
                }
              }
            }
          }
          
          String [] list = new String[3];
          list = (skeletonList.get(a)).removeDirection(removeDirection);
          
          if (randMove == true && !((skeletonList.get(a)).getStatus().equals("D"))){  
            for (int i = 0; i < directionList.size(); i++){
              if (directionList.get(i).equals(list[0]) || directionList.get(i).equals(list[1]) || directionList.get(i).equals(list[2])){
                directionList.remove(i);
              }
            }
            (skeletonList.get(a)).move((skeletonList.get(a)).randString(directionList));
            (skeletonList.get(a)).updateCollisionL();
          }
          
          //COLLISION END
          
          for (int k = 0; k < rockList.size(); k++){
            if ((skeletonList.get(a)).boundingBox.intersects(rockList.get(k).boundingBox)){
              if ((skeletonList.get(a)).getLDirection().equals("W")){
                (skeletonList.get(a)).move("S");
                (skeletonList.get(a)).update(clock.getElapsedTime());
              }
              if ((skeletonList.get(a)).getLDirection().equals("S")){
                (skeletonList.get(a)).move("W");
                (skeletonList.get(a)).update(clock.getElapsedTime());
              }
              if ((skeletonList.get(a)).getLDirection().equals("A")){
                (skeletonList.get(a)).move("D");
                (skeletonList.get(a)).update(clock.getElapsedTime());
              }
              if ((skeletonList.get(a)).getLDirection().equals("D")){
                (skeletonList.get(a)).move("A");
                (skeletonList.get(a)).update(clock.getElapsedTime());
              }
              (skeletonList.get(a)).move("STAND");
            }
          }
          
          if (((skeletonList.get(a)).boundingBox).intersects(player.boundingBox) && !((skeletonList.get(a)).getStatus().equals("D"))){
            player.addToSD((skeletonList.get(a)).getLDirection());
            (skeletonList.get(a)).move("STAND");
            if ((skeletonList.get(a)).bufferCheck() == true){
              (skeletonList.get(a)).updateBuffer();
              player.addHealth(-(skeletonList.get(a)).getDamage());
            }
          }
          
          (skeletonList.get(a)).draw(g);
          
          (skeletonList.get(a)).update(clock.getElapsedTime());
          
          if ((skeletonList.get(a)).getStatus().equals("D") && (skeletonList.get(a)).cdCheckD() == true){
            (skeletonList.get(a)).changeStatus("N");
          }
          
          if (!(skeletonList.get(a)).getStatus().equals("D")){
            
            for (int i = 0; i < swordList.size(); i++){
              if (((skeletonList.get(a)).boundingBox).intersects((swordList.get(i)).boundingBox) && (swordList.get(i)).getIndicator().equals("K")){
                if (player.getLDirection().equals("W")){
                  if ((skeletonList.get(a)).getY() - 45 <= 64){
                    (skeletonList.get(a)).setY(64);
                  } else {
                    (skeletonList.get(a)).setY((skeletonList.get(a)).getY() - 45);
                  }
                } else if (player.getLDirection().equals("S")){
                  if ((skeletonList.get(a)).getY() + 45 >= 836){
                    (skeletonList.get(a)).setY(796);
                  } else {
                    (skeletonList.get(a)).setY((skeletonList.get(a)).getY() + 45);
                  }
                } else if (player.getLDirection().equals("A")){
                  if ((skeletonList.get(a)).getX() - 45 <= 214){
                    (skeletonList.get(a)).setX(214);
                  } else {
                    (skeletonList.get(a)).setX((skeletonList.get(a)).getX() - 45);
                  }
                } else if (player.getLDirection().equals("D")){
                  if ((skeletonList.get(a)).getX() + 45 >= 1750){
                    (skeletonList.get(a)).setX(1710);
                  } else {
                    (skeletonList.get(a)).setX((skeletonList.get(a)).getX() + 45);
                  }       
                }
              } else if (((skeletonList.get(a)).boundingBox).intersects((swordList.get(i)).boundingBox) && (skeletonList.get(a)).sdCheck() == true){
                (skeletonList.get(a)).addHealth(-(int)(Math.round((double)(100-skeletonList.get(a).getDefense())/100*(double)(swordList.get(i)).getDamage())));
                (skeletonList.get(a)).updateSD();
              }
              
            }
            
            for (int i = 0; i < pelletH.size();i++){
              if (((skeletonList.get(a)).boundingBox).intersects((pelletH.get(i)).boundingBox)){
                if ((pelletH.get(i)).getType().equals("P")){
                  (skeletonList.get(a)).addHealth(-(int)(Math.round((double)(90-skeletonList.get(a).getDefense())/100*(double)(pelletH.get(i)).getDamage())));
                  pelletH.remove(i);
                } else if ((pelletH.get(i)).getType().equals("RangerN") || (pelletH.get(i)).getType().equals("MageN")){
                  (skeletonList.get(a)).addHealth(-(int)(Math.round((double)(100-skeletonList.get(a).getDefense())/100*(double)(pelletH.get(i)).getDamage())));
                  pelletH.remove(i);
                } else if ((pelletH.get(i)).getType().equals("S")){
                  (skeletonList.get(a)).addHealth(-(int)(Math.round((double)(90-skeletonList.get(a).getDefense())/100*(double)(pelletH.get(i)).getDamage())));
                  (skeletonList.get(a)).slowSpeed();
                  pelletH.remove(i);
                } else if ((pelletH.get(i)).getType().equals("A")){
                  (skeletonList.get(a)).addHealth(-(int)(Math.round((double)(100-skeletonList.get(a).getDefense())/100*(double)(pelletH.get(i)).getDamage())));
                  ((Mage)player).updateLastM();
                  swordList.add(new Sword((pelletH.get(i)).getXM()-45, (pelletH.get(i)).getYM()-45, 150,150,"STAND",player.getDamage()+10, "M"));
                  pelletH.remove(i);
                } else if ((pelletH.get(i)).getType().equals("B")){
                  (skeletonList.get(a)).addHealth(-(int)(Math.round((double)(100-skeletonList.get(a).getDefense())/100*(double)((pelletH.get(i)).getDamage()-5))));
                  (skeletonList.get(a)).changeStatus("B");
                  (skeletonList.get(a)).updateBurn();
                  pelletH.remove(i);
                } 
              }
            }
          }
          
          
          if ((skeletonList.get(a)).getHealth() <= 0 && (skeletonList.get(a)).getCounterR() < 2){ //Remove skeleton, add back at same position
            (skeletonList.get(a)).addCounterR();
            (skeletonList.get(a)).getUpdateD();
            (skeletonList.get(a)).changeStatus("D"); //Not immediately killed - set to D status - D orevents for interactions with players, turns skeleton into block for enemies
            (skeletonList.get(a)).move("STAND");
            (skeletonList.get(a)).setHealth(60); //Defaults to 60 health per revival^
          } else if ((skeletonList.get(a)).getHealth() <= 0 && (skeletonList.get(a)).getCounterR() >= 2) { //4 revivals required
            enemyCount -= 1;
            player.setCoin(skeletonList.get(a).getCoin()+player.getCoin());
            skeletonList.remove(a);
          }
          
        }
        
        
        for(int i = 0; i < pelletC.size(); i++){
          (pelletC.get(i)).update("N");
          (pelletC.get(i)).draw(g);
          for (int j = 0; j < rockList.size(); j++){
            if (pelletC.get(i).boundingBox.intersects(rockList.get(j).boundingBox)){;
              (pelletC.get(i)).killP();
            }
          }
          if (((pelletC.get(i)).boundingBox).intersects(player.boundingBox)){
            player.addHealth((Math.round(-((pelletC.get(i)).getDamage()*(100-player.getDefense())/100))));
            pelletC.remove(i);
          } else if ((pelletC.get(i)).statusA() == false){
            pelletC.remove(i);
          }
        }
        
      }
      //request a repaint
      repaint();
    } 
    
    public void keyTyped(KeyEvent e) { //Meant for the main program - set to be for Hero (not the superclass character) 
      
    }
    
    public void keyReleased(KeyEvent e){
      
      //Defaulting to standing
      
      player.move("STAND");
      
      
     if (e.getKeyChar() == 'd' ){
        dKey = false;
      }
      if (e.getKeyChar() == 'a'){
        aKey = false;
      } 
      if (e.getKeyChar() == 'w' ){
        wKey = false;
      } 
      if (e.getKeyChar() == 's' ){
        sKey = false;
      } 
      
      //Making corrections, as letting go of space (sword) may trigger
      
      if (aKey == true && dKey == true){
        if (wKey == true){
          player.move("W"); //Analyzing for whether or not a movement is still viable
          player.changeCurrentSprite(3);
        } else if (sKey == true){
          player.move("S");
          player.changeCurrentSprite(1);
        } else {
          player.move("STAND");
        }
      } else if (wKey == true && sKey == true){
        if (aKey == true){
          player.move("A");
          player.changeCurrentSprite(0);
        } else if (dKey == true){
          player.move("D");
          player.changeCurrentSprite(2);
        } else {
          player.move("STAND");
        }
      } else if (wKey == true && aKey == true){
        player.move("AW");
      } else if (wKey == true && dKey == true){
        player.move("DW");
      } else if (sKey == true && dKey == true){
        player.move("DS");
      } else if (sKey == true && aKey == true){
        player.move("AS");
      } else if (aKey == true){
        player.move("A");
        player.changeCurrentSprite(0);
      } else if (wKey == true){
        player.move("W");
        player.changeCurrentSprite(3);
      } else if (sKey == true){
        player.move("S");
        player.changeCurrentSprite(1);
      } else if (dKey == true){
        player.move("D");
        player.changeCurrentSprite(2);
      }
      
    }
    
    public void keyPressed(KeyEvent e){
      
      if (e.getKeyChar() == 'd' ){ //Movement
        dKey = true;
      }
      if (e.getKeyChar() == 'a'){
        aKey = true;
      } 
      if (e.getKeyChar() == 'w' ){
        wKey = true;
      } 
      if (e.getKeyChar() == 's' ){
        sKey = true;
      } 
      if (e.getKeyChar() == 'u'){
        if (player.specialOne() == true){
          player.change1(true);
          player.updateLast1();
        }
      } else if (e.getKeyChar() == 'i'){ //Special attacks
        if (player.specialTwo() == true){
          player.change2(true);
          player.updateLast2();
          if (player instanceof Knight){
            ((Knight)player).updateLastS2();
          }
        }
      } else if (e.getKeyChar() == 'o'){
        if (player.specialThree() == true){
          player.change3(true);
          player.updateLast3();
          if (player instanceof Ranger){
            ((Ranger)player).updateLast3a();
          } else if (player instanceof Knight){
            ((Knight)player).updateLastS3();
          }
        }
      } else if (e.getKeyChar() == 'p'){
        System.out.println(player.getSpeed()+" + "+player.getSpeedO());
        if (player.specialFour() == true){
          player.updateLast4();
          player.statChange(20);
          player.updateBT();
        }
      }
      
      if (aKey == true && dKey == true){
        if (wKey == true){
          player.move("W");
          player.changeCurrentSprite(3);
        } else if (sKey == true){
          player.move("S");
          player.changeCurrentSprite(1);
        } else {
          player.move("STAND");
        }
      } else if (wKey == true && sKey == true){
        if (aKey == true){
          player.move("A");
          player.changeCurrentSprite(0);
        } else if (dKey == true){
          player.move("D");
          player.changeCurrentSprite(2);
        } else {
          player.move("STAND");
        }
      } else if (wKey == true && aKey == true){
        player.move("AW");
      } else if (wKey == true && dKey == true){
        player.move("DW");
      } else if (sKey == true && dKey == true){
        player.move("DS");
      } else if (sKey == true && aKey == true){
        player.move("AS");
      } else if (aKey == true){
        player.move("A");
        player.changeCurrentSprite(0);
      } else if (wKey == true){
        player.move("W");
        player.changeCurrentSprite(3);
      } else if (sKey == true){
        player.move("S");
        player.changeCurrentSprite(1);
      } else if (dKey == true){
        player.move("D");
        player.changeCurrentSprite(2);
      }
      
      if (e.getKeyCode() == KeyEvent.VK_SPACE){
        if (player.cdCheck(0.5) == true){ //Attacking delay
          player.changeAttack(true);
          player.updateLast();
        }
      }
      
      if (e.getKeyCode() == KeyEvent.VK_ENTER && endGame == true){ //Resets
        new MenuUIV2();
        thisFrame.dispose();
      }
    }
      
    
  }
  
}
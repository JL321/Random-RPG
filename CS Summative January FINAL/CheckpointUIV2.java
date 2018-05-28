import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class CheckpointUIV2 extends JFrame{
  
  private JFrame thisFrame;
  private int[][] upgradesSpent = new int[2][6];
  //private int coins;
  private JLabel playerCoins;
  private JLabel[] upgrades = new JLabel[6];
  private JButton[] addUpgrades = new JButton[6];
  private Hero player;
  private int levelCount;
  
  CheckpointUIV2(Hero player, int levelCount){
    super("Upgrades");
    //this.coins = player.getCoin();
    this.player = player;
    this.thisFrame = this;
    this.levelCount = levelCount;
    this.setSize(1000, 1000);
    this.setLocationRelativeTo(null); //Create frame in the center of the screen
    this.setResizable(false);
    
    //Display the frame without border and invisible
    this.setUndecorated(true);
    setBackground(new Color(0,0,0,0));
    
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());
    
    JButton continueButton = new JButton(new ImageIcon("CheckpointButton.png"));
    continueButton.setRolloverIcon(new ImageIcon("CheckpointButtonPressed.png"));
    continueButton.setBorder(BorderFactory.createEmptyBorder());
    continueButton.setFocusPainted(false);
    continueButton.addActionListener(new ContinueButtonListener());
    
    JPanel centerPanel = new JPanel();
    centerPanel.setLayout(new GridLayout(4,1));
    
    JPanel playerStats = new JPanel();
    playerStats.setLayout(new GridLayout(4, 2));
    
    playerCoins = new JLabel("Current coins: " +  player.getCoin());
    playerCoins.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
    
    JLabel blankSpace = new JLabel("");
    
    upgrades[0] = new JLabel("Attack upgrades purchased: " + ((player.getUpgradeInfo())[1][0]-1));
    upgrades[1] = new JLabel("Defence upgrades purchased: " + ((player.getUpgradeInfo())[1][1]-1));
    upgrades[2] = new JLabel("'U' Ablility upgrades purchased: " + ((player.getUpgradeInfo())[1][2]-1));
    upgrades[3] = new JLabel("'I' Ability upgrades purchased: " + ((player.getUpgradeInfo())[1][3]-1));
    upgrades[4] = new JLabel("'O' Ability upgrades purchased: " + ((player.getUpgradeInfo())[1][4]-1));
    upgrades[5] = new JLabel("'P' Ability upgrades purchased: " + ((player.getUpgradeInfo())[1][5]-1));
    
    JPanel topOfCenter = new JPanel();
    topOfCenter.setLayout(new GridLayout(1, 2));
    
    JPanel middleOfCenter = new JPanel();
    middleOfCenter.setLayout(new GridLayout(1, 2));
    
    JPanel bottomOfCenter = new JPanel();
    bottomOfCenter.setLayout(new GridLayout(1, 2));
    
    for (int i = 0; i < 6; i++){
      upgradesSpent[0][i] = 1;
      upgrades[i].setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
    }
    
    addUpgrades[0] = new JButton("<HTML>Add 3 attack: <br>" + (int)((player.getUpgradeInfo())[0][0]*1.5) + " coins</HTML>");
    addUpgrades[0].addActionListener(new AttackButtonListener());
    addUpgrades[1] = new JButton("<HTML>Add 2 defence: <br>" + (int)((player.getUpgradeInfo())[0][1]*1.5) + " coins</HTML>");
    addUpgrades[1].addActionListener(new DefenceButtonListener());
    addUpgrades[2] = new JButton("<HTML>Improve your 'U' ability: <br>" + (int)((player.getUpgradeInfo())[0][2]*1.5) + " coins</HTML>");
    addUpgrades[2].addActionListener(new Ability1ButtonListener());
    addUpgrades[3] = new JButton("<HTML>Improve your 'I' ability: <br>" + (int)((player.getUpgradeInfo())[0][3]*1.5) + " coins</HTML>");
    addUpgrades[3].addActionListener(new Ability2ButtonListener());
    addUpgrades[4] = new JButton("<HTML>Improve your 'O' ability: <br>" + (int)((player.getUpgradeInfo())[0][4]*1.5) + " coins</HTML>");
    addUpgrades[4].addActionListener(new Ability3ButtonListener());
    addUpgrades[5] = new JButton("<HTML>Improve your 'P' ability: <br>" + (int)((player.getUpgradeInfo())[0][5]*1.5) + " coins</HTML>");
    addUpgrades[5].addActionListener(new Ability4ButtonListener());
    for (int i = 0; i < 6; i++){
      addUpgrades[i].setFont(new Font("Comic Sans MS", Font.PLAIN, 28));
    }
    
    topOfCenter.add(addUpgrades[0]);
    topOfCenter.add(addUpgrades[1]);
    middleOfCenter.add(addUpgrades[2]);
    middleOfCenter.add(addUpgrades[3]);
    bottomOfCenter.add(addUpgrades[4]);
    bottomOfCenter.add(addUpgrades[5]);
    
    
    
    playerStats.add(playerCoins);
    playerStats.add(blankSpace);
    for (int i = 0; i < 6; i++){
      playerStats.add(upgrades[i]);
    }
    
    centerPanel.add(playerStats);
    centerPanel.add(topOfCenter);
    centerPanel.add(middleOfCenter);
    centerPanel.add(bottomOfCenter);
    
    
    
    //mainPanel.add(playerStats, BorderLayout.NORTH);
    mainPanel.add(centerPanel, BorderLayout.CENTER);
    mainPanel.add(continueButton, BorderLayout.SOUTH);
    
    //Add the main panel to the frame
    this.add(mainPanel);
    
    //Show the UI
    this.setVisible(true);
    
  }
  
  class ContinueButtonListener implements ActionListener {
    public void actionPerformed(ActionEvent event){  
      player.setHealth(100); //Replenishes health
      new GameWindow(player,levelCount+1);
      thisFrame.dispose();
    }
  }
  
  class AttackButtonListener implements ActionListener {
    public void actionPerformed(ActionEvent event){
      
      if (player.getCoin() >= (player.getUpgradeInfo())[0][0]){
        
        System.out.println("TEST");
        
        player.setCoin(player.getCoin()-((player.getUpgradeInfo())[0][0]));
        playerCoins.setText("Current coins: " + player.getCoin());
        player.setDamage(player.getDamage()+3);
        player.updateUpgradeInfo(0,0,(int)(Math.round((player.getUpgradeInfo())[0][0]*1.5 + 1)));
        addUpgrades[0].setText("<HTML>Add 1 attack: <br>" + ((player.getUpgradeInfo())[0][0]) + " coins</HTML>");
        player.setDamageO(player.getDamageO()+3);
        
        player.updateUpgradeInfo(1,0,(player.getUpgradeInfo())[1][0]+1);
        upgrades[0].setText("Attack upgrades purchased: " + (player.getUpgradeInfo())[1][0]);
        
      } else {
        addUpgrades[0].setText("<HTML>You don't have enough coins!<br>You need " + ((player.getUpgradeInfo())[0][0]-player.getCoin()) + " more coin(s)</HTML>");
      }
    }
  }
  
  class DefenceButtonListener implements ActionListener {
    public void actionPerformed(ActionEvent event){
      
      if (player.getCoin() >= (player.getUpgradeInfo())[0][1]){
        
        System.out.println("TEST");
        
        player.setCoin(player.getCoin()-((player.getUpgradeInfo())[0][1]));
        playerCoins.setText("Current coins: " + player.getCoin());
        player.setDefense(player.getDefense()+2);
        player.updateUpgradeInfo(0,1,(int)(Math.round((player.getUpgradeInfo())[0][1]*1.5 + 1))); 
        addUpgrades[1].setText("<HTML>Add 1 defence: <br>" + ((player.getUpgradeInfo())[0][1]) + " coins</HTML>");
        player.setDefenseO(player.getDefenseO()+2);
                                  
        player.updateUpgradeInfo(1,1,(player.getUpgradeInfo())[1][1]+1);
        upgrades[1].setText("Defence upgrades purchased: " + (player.getUpgradeInfo())[1][1]);                            
                                  
      } else {
        addUpgrades[1].setText("<HTML>You don't have enough coins!<br>You need " + ((player.getUpgradeInfo())[0][1]-player.getCoin()) + " more coin(s)</HTML>");
      }
      
    }
  }
  
  class Ability1ButtonListener implements ActionListener {
    public void actionPerformed(ActionEvent event){
      
      if (player.getCoin() >= (player.getUpgradeInfo())[0][2]){
        
        player.setCoin(player.getCoin()-((player.getUpgradeInfo())[0][2]));
        playerCoins.setText("Current coins: " + player.getCoin());
        player.updateUpgradeInfo(0,2,(int)(Math.round((player.getUpgradeInfo())[0][2]*1.5 + 1)));
        addUpgrades[2].setText("<HTML>Improve your 'U' ability: <br>" + ((player.getUpgradeInfo())[0][2]) + " coins</HTML>");
        
        player.setSpecialCount(player.getSpecialCount(1)-.15,1);
                                   
        player.updateUpgradeInfo(1,2,(player.getUpgradeInfo())[1][2]+1);
        upgrades[2].setText("'U' Ability upgrades purchased: " + (player.getUpgradeInfo())[1][2]);
        
      } else {
        addUpgrades[2].setText("<HTML>You don't have enough coins!<br>You need " + ((player.getUpgradeInfo())[0][2]-player.getCoin()) + " more coin(s)</HTML>");
      }
    }
  }
  
  class Ability2ButtonListener implements ActionListener {
    public void actionPerformed(ActionEvent event){
      
      if (player.getCoin() >= (player.getUpgradeInfo())[0][3]){
        
        player.setCoin(player.getCoin()-((player.getUpgradeInfo())[0][3]));
        playerCoins.setText("Current coins: " + player.getCoin());
        playerCoins.setText("Current coins: " + player.getCoin());
        player.updateUpgradeInfo(0,3,(int)(Math.round((player.getUpgradeInfo())[0][3]*1.5 + 1)));
        addUpgrades[3].setText("<HTML>Improve your 'I' ability: <br>" + ((player.getUpgradeInfo())[0][3]) + " coins</HTML>");
        
        player.setSpecialCount(player.getSpecialCount(2)-.15,2);
        
        player.updateUpgradeInfo(1,3,(player.getUpgradeInfo())[1][3]+1);
        upgrades[3].setText("'I' Ability upgrades purchased: " + (player.getUpgradeInfo())[1][3]);
      } else {
        addUpgrades[3].setText("<HTML>You don't have enough coins!<br>You need " + ((player.getUpgradeInfo())[0][3]-player.getCoin()) + " more coin(s)</HTML>");
      }
    }
  }
  
  class Ability3ButtonListener implements ActionListener {
    public void actionPerformed(ActionEvent event){
      
      if (player.getCoin() >= (player.getUpgradeInfo())[0][4]){
        
        player.setCoin(player.getCoin()-((player.getUpgradeInfo())[0][4]));
        playerCoins.setText("Current coins: " + player.getCoin());
        player.updateUpgradeInfo(0,4,(int)(Math.round((player.getUpgradeInfo())[0][4]*1.5 + 1))); 
        addUpgrades[4].setText("<HTML>Improve your 'O' ability: <br>" + ((player.getUpgradeInfo())[0][4]) + " coins</HTML>");
        
        player.setSpecialCount(player.getSpecialCount(3)-.15,3);
        
        upgrades[4].setText("'O' Ability upgrades purchased: " + (player.getUpgradeInfo())[1][4]);
        player.updateUpgradeInfo(1,4,(player.getUpgradeInfo())[1][4]+1);
      } else {
        addUpgrades[4].setText("<HTML>You don't have enough coins!<br>You need " + ((player.getUpgradeInfo())[0][4]-player.getCoin()) + " more coin(s)</HTML>");
      }
    }
  }
  
  class Ability4ButtonListener implements ActionListener {
    public void actionPerformed(ActionEvent event){
      
      if (player.getCoin() >= (player.getUpgradeInfo())[0][5]){
        
        player.setCoin(player.getCoin()-((player.getUpgradeInfo())[0][5]));
        playerCoins.setText("Current coins: " + player.getCoin());
        player.updateUpgradeInfo(0,5,(int)(Math.round((player.getUpgradeInfo())[0][5]*1.5 + 1))); 
        addUpgrades[5].setText("<HTML>Improve your 'P' ability: <br>" + ((player.getUpgradeInfo())[0][5]) + " coins</HTML>");
        
        player.setSpecialCount(player.getSpecialCount(4)-.15,4);
        
        upgrades[5].setText("'P' Ability upgrades purchased: " + (player.getUpgradeInfo())[1][5]);
        player.updateUpgradeInfo(1,5,(player.getUpgradeInfo())[1][5]+1);
      } else {
        addUpgrades[5].setText("<HTML>You don't have enough coins!<br>You need " + ((player.getUpgradeInfo())[0][5]-player.getCoin()) + " more coin(s)</HTML>");
      }
    }
  }
  
  
  //Main method starts and creates the CheckpointUI
}
/**Notes for Zombie Simulator
 *
 **The zombie simulator would work perfectly sometimes and other times have issues
 * 
 **It would kill off zombies and put them at the top left of the panel after a human would die for an unknown reason
 **Sometimes it would freeze the humans as well and call index out of bounds
 * 
 **I had no idea how to replicate either of these issues accurately and so i could not debug and narrow down where the issue was - hope its not too much of an issue, cheers!
 * 
 **Something cool -- shrink the panel as much as possible with live characters :-)
 **- Vaughn Carroll
 */

package zombiesimulator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.Timer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ZombieSimulator extends JPanel implements ActionListener {
    private JButton addHuman;
    private JButton addZombie;
    private DrawPanel drawPanel;
    private Timer timer;
    private List<Human> others = new ArrayList();

    public ZombieSimulator() {
        super(new BorderLayout());
                
        addHuman = new JButton("Add Human");
        addHuman.addActionListener((ActionListener)this);
        
        addZombie = new JButton("Add Zombie");
        addZombie.addActionListener((ActionListener)this);
        
        JPanel southPanel = new JPanel();
        
        southPanel.add(addHuman);
        southPanel.add(addZombie);
        
        add(southPanel, BorderLayout.SOUTH);
        
        drawPanel = new DrawPanel();
        
        add(drawPanel, BorderLayout.CENTER);
        
        timer = new Timer(10, this);
        timer.start();
    }
    
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        
        if(source == addHuman) {
            Human human = new Human(others, drawPanel.getWidth()/2, drawPanel.getHeight()/2);
            others.add(human);
            Thread humanThread = new Thread(human);
            human.isAlive = true;
            humanThread.start();
        }
        if(source == addZombie) {
            Zombie zombie = new Zombie(others, drawPanel.getWidth()/2, drawPanel.getHeight()/2);
            others.add(zombie);
            Thread zombieThread = new Thread(zombie);
            zombie.isAlive = true;
            zombieThread.start();
        }
        
        drawPanel.repaint();
    }

    class DrawPanel extends JPanel {
        Random random = new Random();
        
        public DrawPanel() {
            super();
            
            setPreferredSize(new Dimension(600, 600));
            setBackground(Color.WHITE);
        }
        
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            for(int i = 0; i < others.size(); i++) {
                Human.world_width = drawPanel.getWidth();
                Human.world_height = drawPanel.getHeight();
                
                others.get(i).draw(g);
            }
        }
    }
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Zombie Simulator");
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new ZombieSimulator());
        frame.pack();
        
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenDimension = tk.getScreenSize();
        Dimension frameDimension = frame.getSize();
        frame.setLocation((screenDimension.width-frameDimension.width)/2,
           (screenDimension.height-frameDimension.height)/2);
        frame.setVisible(true);
    }
}
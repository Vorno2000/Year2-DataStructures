package zombiesimulator;

import java.awt.Color;
import java.awt.Graphics;
import static java.lang.Math.sqrt;
import java.util.List;
import java.util.Random;

public class Human implements Runnable{
    protected double x, y;
    protected double dx, dy;
    int frames;
    protected double size = 25;
    protected boolean isAlive = false;
    public static int world_width, world_height;
    protected double max_speed;
    protected int sightDistance;
    protected int target = -1;
    protected double targDist;
    protected Random generator = new Random();
    
    protected List<Human> others;
    
    Human(List<Human> others, double x, double y) {
        this.others = others;
        max_speed = 2;
        
        this.world_width = (int)x*2;
        this.world_height = (int)y*2;
        
        this.x = x;
        this.y = y;
       
        dx = -2 + (max_speed + 2) * generator.nextDouble();
        dy = -2 + (max_speed + 2) * generator.nextDouble();
        
        sightDistance = (world_width + world_height)/4;
    }
    
    @Override
    public void run() {
        while(isAlive) {
            move();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void move() {
        for(int i = 0; i < others.size(); i++) {
            if(others.get(i) instanceof Zombie) {
                if(target == -1) target = i;
                
                double tempDist = sqrt((Math.pow((others.get(i).getX() - this.getX()), 2))+(Math.pow((others.get(i).getY() - this.getY()), 2)));
                targDist = sqrt((Math.pow((others.get(target).getX() - this.getX()), 2))+(Math.pow((others.get(target).getY() - this.getY()), 2)));
                
                if (tempDist < sightDistance && targDist > tempDist) target = i;
                targDist = sqrt((Math.pow((others.get(target).getX() - this.getX()), 2))+(Math.pow((others.get(target).getY() - this.getY()), 2)));
            }
        }
        if (target == -1) {
            if (frames > 50) {
                dx = -2 + (max_speed + 2) * generator.nextDouble();
                dy = -2 + (max_speed + 2) * generator.nextDouble();
                frames = 0;
            }
        }
        else {
            if(others.get(target) instanceof Zombie) ;
                else {
                    for(int z = 0; z < others.size(); z++) {
                        if(others.get(z) instanceof Zombie)
                            target = z;
                }
            }
            if(others.get(target) instanceof Zombie) {
                if(targDist <= sightDistance/2) {
                    dx = (((this.getX() - others.get(target).getX()) / targDist) * this.max_speed);
                    dy = (((this.getY() - others.get(target).getY()) / targDist) * this.max_speed);
                }
                else {
                    if (frames > 120) {
                        dx = -2 + (max_speed + 2) * generator.nextDouble();
                        dy = -2 + (max_speed + 2) * generator.nextDouble();
                        frames = 0;
                    }
                }
            }
        }
        
        if (x<=0) {
            this.x += 5;
            dx*=-1;
        }
        else if (x >= world_width-size) {
            this.x -= 5;
            dx*=-1;
        }
        if (y<=0) {
            this.y += 5;
            dy*=-1;
        }
        else if (y >= world_height-size) {
            this.y -= 5;
            dy*=-1;
        }
        
        this.x+=dx;
        this.y+=dy;
        frames++;
    }
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    public double getSize() {
        return size;
    }
    
    public synchronized void kill(int target) {
        
        if(others.get(target) instanceof Zombie);
        else {
            Zombie zombie;
            if(this.getX() > world_width || this.getY() > world_height)
                zombie = new Zombie(others, this.getX()-10, this.getY()-10);
            else if (this.getX() < 0 || this.getY() < 0)
                zombie = new Zombie(others, this.getX()+10, this.getY()+10);
            else
                zombie = new Zombie(others, this.getX(), this.getY());
            
            others.add(zombie);
            Thread zombieThread = new Thread(zombie);
            zombie.isAlive = true;
            zombieThread.start();

            others.get(target).isAlive = false;
            others.remove(this);
        }
    }
    
    public void draw(Graphics g) {
        g.setColor(Color.blue);
        g.fillOval((int)x, (int)y, (int)size, (int)size);
    }
}
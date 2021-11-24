package zombiesimulator;

import java.awt.Color;
import java.awt.Graphics;
import static java.lang.Math.sqrt;
import java.util.List;
import java.util.Random;

public class Zombie extends Human implements Runnable{
  
    public Zombie(List<Human> others, double x, double y) {
        super(others, x, y);
        
        dx = -1 + (max_speed + 1) * generator.nextDouble();
        dy = -1 + (max_speed + 1) * generator.nextDouble();
        
        max_speed = 1;
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
    
    @Override
    public void move() {
        int tempHumans = 0;
        for(int i = 0; i < others.size(); i++) {
            if(others.get(i) instanceof Zombie);
            else {
                tempHumans++;
                if(target == -1) target = i;
            }
        }
       
        if(tempHumans == 0) {
            if (frames > 50) {
                dx = -1 + (max_speed + 1) * generator.nextDouble();
                dy = -1 + (max_speed + 1) * generator.nextDouble();
                frames = 0;
                target = -1;
            }
        }
        else if(tempHumans > 0) {
            for(int i = 0; i < others.size(); i++) {
                if(others.get(target) instanceof Zombie) {
                    for(int z = 0; z < others.size(); z++) {
                        if(others.get(z) instanceof Zombie) ;
                        else {
                            this.target = z;
                            targDist = sqrt((Math.pow((others.get(target).getX() - this.getX()), 2))+(Math.pow((others.get(target).getY() - this.getY()), 2)));
                        }
                    }
                }
                if(others.get(i) instanceof Zombie) {
                }
                else {
                    
                    double tempDist = 0;
                    boolean match = false;
                    
                    if(this.getX() == others.get(target).getX() && this.getY() == others.get(target).getY()) match = true;
                    
                    if(!match) {
                        tempDist = sqrt((Math.pow((others.get(i).getX() - this.getX()), 2))+(Math.pow((others.get(i).getY() - this.getY()), 2)));
                        targDist = sqrt((Math.pow((others.get(target).getX() - this.getX()), 2))+(Math.pow((others.get(target).getY() - this.getY()), 2)));
                    }
                    if(tempDist < targDist) target = i;
                }
            }
            
            if(others.get(target) instanceof Zombie);
            else {
                dx = (((this.getX() - others.get(target).getX()) / targDist) * max_speed)*-1;
                dy = (((this.getY() - others.get(target).getY()) / targDist) * max_speed)*-1;
                
                if((this.getX()+this.getSize())-others.get(target).getX() >= 0 && (others.get(target).getX()+others.get(target).getSize()) - this.getX() >= 0)
                    if((this.getY()+this.getSize())-others.get(target).getY() >= 0 && (others.get(target).getY()+others.get(target).getSize()) - this.getY() >= 0) {
                        others.get(target).kill(target);
                        target = -1;
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
    
    @Override
    public double getX() {
        return this.x;
    }
    
    @Override
    public double getY() {
        return this.y;
    }
    
    @Override
    public double getSize() {
        return this.size;
    }
    
    @Override
    public void draw(Graphics g) {
        g.setColor(Color.red);
        g.fillRect((int)x, (int)y, (int)size, (int)size);
    }
}
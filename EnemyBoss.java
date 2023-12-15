import java.awt.Color;
import java.awt.Graphics;

public class EnemyBoss extends MovingObject{
    //int delaytime;
    EnemyBoss(int apWidth, int apHeight){
        super( apWidth, apHeight );
        w = 50;
        h = 20;
        hp = 60;
        delaytime = 5;
    }

    void move(Graphics buf, int apWidth, int apHeight){
        buf.setColor(Color.green);
        if(hp>0){
            //buf.fillOval(x - w, y - h, 2 * w, 2 * h);            
            x = x + dx;
            y = y + dy;
            if(y>apHeight + h){
                dy = -dy;
            }
            if(x < 0){
                dx = -dx;
              }
            if(y < 0){
                dy = -dy;
              }
            if((x + w/2)>apWidth){
                dx = -dx;
              }
        }
    }

    void revive(int apWidth, int apHeight){
        x = (int)(Math.random()*(apWidth-2*w) + w);
        y = -h;
        dy = 1;
        if(x<apWidth/2){
            dx = (int)(Math.random()*2);
        }
        else{
            dx = -(int)(Math.random()*2);
        }
        hp = 10;
    }
}

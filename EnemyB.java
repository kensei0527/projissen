import java.awt.*;
class EnemyB extends MovingObject {
    int[] xPoints = new int[5];
    int[] yPoints = new int[5];
    int n,r;
    double delta;
    EnemyB(int apWidth, int apHeight){
        super(apWidth, apHeight);
        hp = 2;
        n = 5;
        r = 18;
        w = 12;
        h = 12;
        delaytime = 20;
    //x  = (int) (Math.random()*apWidth);  // 画面の内部でランダム
    //y  = (int) (Math.random()*apHeight); // 画面の内部でランダム
        delta = (360.0 / (double)n ) * ( Math.PI / 180.0);
        for(int i= 0; i<n; i++) {
			xPoints[i] = (int)(Math.cos(-Math.PI/2.0 + delta * i)*r) + x;
			yPoints[i] = (int)(Math.sin(-Math.PI/2.0 + delta * i)*r) + y;
		}
    }

    void move(Graphics buf, int apWidth, int apHeight){
        buf.setColor(Color.blue);

        if(hp>0){
            //buf.fillOval(x - w, y - h, 2 * w, 2 * h);
            //buf.drawPolygon(xPoints,yPoints,n);
            x = x + dx;
            y = y + dy;
            for(int i= 0; i<n; i++) {
			xPoints[i] = (int)(Math.cos(-Math.PI/2.0 + delta * i)*r) + x;
			yPoints[i] = (int)(Math.sin(-Math.PI/2.0 + delta * i)*r) + y;
		}
            if(y>apHeight+h){
                hp = 0;
            }

            if(x<0){
                dx = -dx;
            }
            if((x + r)>apWidth){
                dx = -dx;
            }
            if(y<0){
                dy = -dy;
            }
        }
    }
    void revive(int apWidth, int apHeight){
        x = (int)(Math.random()*(apWidth-2*w)+w);
        y = -h;
        for(int i= 0; i<n; i++) {
			xPoints[i] = (int)(Math.cos(-Math.PI/2.0 + delta * i)*r) + x;
			yPoints[i] = (int)(Math.sin(-Math.PI/2.0 + delta * i)*r) + y;
		}
        dy = 1;
        if (x<apWidth/2)
        dx = (int)(Math.random()*2);
        else
        dx = -(int)(Math.random()*2);
        hp = 1;
    }
}

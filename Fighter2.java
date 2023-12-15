import java.awt.*;

class Fighter2 extends MovingObject {
  

  
  int delaytime; // 次の弾を発射するまでの待ち時間
  boolean clflag;
  // コンストラクタ
  Fighter2(int apWidth, int apHeight) {
    // super(); //省略可
    x  = (int)(apWidth/2);    // 画面の真中
    y  = (int)(apHeight*0.9); // 画面の下の方
    dx = 5;
    dy = 5;
    w  = 10;
    h  = 10;
    hp = 5;
    delaytime = 5; // 弾の発射待ち時間．毎回１ずつ減り，０で発射可能になる
  }
  void revive(int apWidth, int apHeight) {
  }
  void move(Graphics buf, int apWidth, int apHeight){
    buf.setColor(Color.blue);
    buf.fillRect(x - w, y - h, 2 * w, 2 * h);
    buf.drawString("HP "+ hp, this.x-15, this.y-30);
  }

  
}

import java.awt.*;

public class Character extends Frame {
    // ■ フィールド変数
  // s: キャラクタのサイズ （画像の大きさ（ピクセル））
  // cond1: キャラの右足が前 (1) か、揃っている（０）か、左足が前 (-1) か
  // cond2: キャラの向き（４種類）
  // cw, cwMax:  足の入れ替え速度
  int s=32, xp, yp, vx, vy, cond1=1, cond2=2, cw=0, cwMax=5;
  int imgW,imgH;
  boolean lf, rf, uf, df; // left, right, up, down
  Image cimg; // キャラクタ画像
  String filename = "/Users/furuyakensei/Downloads/キャラチップ/pipo-charachip011b.png";

  void cGetImage(int imgW,int imgH){
    cimg = getToolkit().getImage(filename);
    this.imgW = imgW;
    this.imgH = imgH;
  }

  void cDraw(Graphics g){
    int imgX,imgY;
    if(cond1 == 1) imgX = 0;    //右足
    else if(cond1 == 0) imgX = s;   //揃い
    else  {imgX = s*2;}    //左足
    imgY = cond2*s;
    g.drawImage(cimg, xp, yp, xp+s, yp+s, imgX, imgY, imgX+s, imgY+s,null);
  }

  void cUpdate(){
    if(vx>0){
        cond2 = 2;
    }
    else if(vx<0){
        cond2 = 1;
    }
    else if(vx==0 && vy>=0){
        cond2 = 0;
    }
    else {
        cond2 = 4;
    }
    
    if(cw<cwMax){
        cw++;
    }
    else{
        cond1 = cond1 * (-1);
        cw = 0;
    }
    xp = xp + vx;
    yp = yp + vy;
    if(xp + s/2 > imgW)   vx = -vx;
    else if(xp + s/2 < 0) vx = -vx;
    else if(yp > (s + imgH))  vy = -vy;
    else if(yp < 0)   {vy = -vy;}
    
}   
}

import java.awt.event.*;
import java.util.ArrayList;
import java.awt.*;

public class ShGameMaster2 extends Canvas implements MouseMotionListener,MouseListener {
      // ■ フィールド変数
  Image        buf;   // 仮の画面としての buffer に使うオブジェクト(Image クラス)
  Graphics     buf_gc;// buffer のグラフィックスコンテキスト (gc) 用オブジェクト
  Dimension    d;     // アプレットの大きさを管理するオブジェクト
  private int  imgW, imgH; // キャンバスの大きさ

  private int enmyAnum  = 10; // 敵Ａの数
  private int enmyBnum = 5;
  private int Bossnum = 1;
  //private int enmyBossnum = 1;
  private int ftrBltNum = 10; // 自機の弾の数
  private int enmyBltnum = 2;
  //private int zakoBltnum = 10;
  private int mode      = -1; // -1: タイトル画面，-2: ゲームオーバー，1〜 ゲームステージ
  private int i, j;
  private int diecount = 0;
  int flg = 0; 
  int flg2 = 0;

  //MovingObject Enmy[] = new MovingObject[enmyAnum + enmyBnum ];
  ArrayList<MovingObject> enmy = new ArrayList<MovingObject>();

  Fighter2       ftr;  // 自機
  EnemyA       queen;
  FighterBullet ftrBlt[] = new FighterBullet[ftrBltNum]; // 自機の弾
  
  FighterBullet enmyBlt[][] = new FighterBullet[enmyAnum+enmyBnum+Bossnum][enmyBltnum];
  Image bosspic = getToolkit().getImage("/Users/furuyakensei/Downloads/Pipoya RPG Monster Pack/陰影効果あり/pipo-boss004.png");
  Image eneApic = getToolkit().getImage("/Users/furuyakensei/Downloads/Pipoya RPG Monster Pack/陰影効果あり/pipo-enemy025.png");
  Image eneBpic = getToolkit().getImage("/Users/furuyakensei/Downloads/Pipoya RPG Monster Pack/陰影効果あり/pipo-enemy044c.png");
  Image backpic = getToolkit().getImage("/Users/furuyakensei/Downloads/110016.jpg");
  int backY;
  Image ftrpic = getToolkit().getImage("/Users/furuyakensei/Downloads/pipo-charachip023e.png");
  Image bulletpic = getToolkit().getImage("/Users/furuyakensei/Downloads/pipo-gate01a192.png");
  Image effectpic = getToolkit().getImage("/Users/furuyakensei/Downloads/ヒットエフェクト1/192/pipo-hiteffect001.png");
  Character queenpic = new Character();
  Image stpic = getToolkit().getImage("/Users/furuyakensei/Downloads/既存背景画像イラストタッチ/pipo-battlebg011b.jpg");
  Image gclpic = getToolkit().getImage("/Users/furuyakensei/Downloads/既存背景画像イラストタッチ/kaidou0331_800b.jpg");
  Image gopic = getToolkit().getImage("/Users/furuyakensei/Downloads/既存背景画像イラストタッチ/pipo-battlebg010b.jpg");
  // ■ コンストラクタ
  /**
   * ゲームの初期設定
   * ・描画領域(Image)とGC(Graphics)の作成
   * ・敵，自機，弾オブジェクトの作成
   */
  ShGameMaster2(int imgW, int imgH) { // コンストラクタ （アプレット本体が引数． ゲームの初期化を行う）
    this.imgW = imgW; // 引数として取得した描画領域のサイズをローカルなプライベート変数に代入
    this.imgH = imgH; // 引数として取得した描画領域のサイズをローカルなプライベート変数に代入
    setSize(imgW, imgH); // 描画領域のサイズを設定

    //addKeyListener(this);
    addMouseMotionListener(this);
    addMouseListener(this);

    ftr = new Fighter2(imgW, imgH); // 自機のオブジェクトを実際に作成
    queen = new EnemyA(imgW,imgH); //姫のオブジェクトを作成
    queen.y = imgH-(imgH/9); //姫の設定
    queen.hp = 5;
    queenpic.cGetImage(imgW, imgH); //姫の画像を貼り付け
    queenpic.xp = queen.x - 35;
    queenpic.yp = queen.y;
    queenpic.vx = queen.dx;
    queenpic.vy = queen.dy;
    backY = imgH;
    //backY = 0;
    for (i = 0; i < ftrBltNum; i++){     // 自機弾のオブジェクトを実際に作成
        ftrBlt[i] = new FighterBullet();
    }
    

    for(i = 0;i<enmyBlt.length;i++){  //敵の弾を作成
      if(i<enmyAnum){ //Aの弾
        for(j=0;j<enmyBltnum;j++){
          enmyBlt[i][j] = new FighterBullet(0, 1, 3);
        }
      }
      else if(enmyAnum <= i && i < enmyBnum){ //Bの弾
        for(j=0;j<enmyBltnum;j++){
          enmyBlt[i][j] = new FighterBullet(0, 1, 10);
        }
      }
      else{
        for(j=0;j<enmyBltnum;j++){  //ボスの弾
          enmyBlt[i][j] = new FighterBullet((int)Math.random()*6-3, 3, 10);
        }
      }
    }
    for(i = 0;i<enmyAnum;i++){    //enmyに敵Aを入れる
      enmy.add(new EnemyA(imgW, imgH));
    }
    for(i = 0;i<enmyBnum;i++){    //敵B
      enmy.add(new EnemyB(imgW, imgH));
    }
    for(i = 0;i<Bossnum;i++){   //ボス
      enmy.add(new EnemyBoss(imgW, imgH));
    }
    //wall = new Wall(imgW, imgH);
  }


  // ■ メソッド 
  // コンストラクタ内で createImage を行うと peer の関連で 
  // nullpointer exception が返ってくる問題を回避するために必要
  public void addNotify(){
    super.addNotify();
    buf = createImage(imgW, imgH); // buffer を画面と同サイズで作成
    buf_gc = buf.getGraphics();
  }

  // ■ メソッド (Canvas)
  public void paint(Graphics g) {
    buf_gc.setColor(Color.white);      // gc の色を白に
    buf_gc.fillRect(0, 0, imgW, imgH); // gc を使って白の四角を描く（背景の初期化）

    switch (mode) {
    case -2: // ゲームオーバー画面（クリックで画面切り替え
      buf_gc.drawImage(gopic,0,0, imgW, imgH, this);
      buf_gc.setColor(Color.black); // ゲームオーバー画面を描く
      buf_gc.drawString("      == Game over ==      ", imgW/2-80, imgH/2-20);
      buf_gc.drawString("       Hit SPACE key       ", imgW/2-80, imgH/2+20);
      
      break;
    case -1: // タイトル画面
      buf_gc.drawImage(stpic,0,0, imgW, imgH, this);
      buf_gc.setColor(Color.black); // タイトル画面を描く
      buf_gc.drawString(" == Shooting Game Title == ", imgW/2-80, imgH/2-20);
      buf_gc.drawString("Hit SPACE bar to start game", imgW/2-80, imgH/2+20);
      //buf_gc.drawImage(stpic,0,0, imgW, imgH, this);
      break;
    case -3:{ //ゲームクリア
      buf_gc.drawImage(gclpic,0,0, imgW, imgH, this);
      buf_gc.setColor(Color.black);
      buf_gc.drawString(" == Game Clear ==", imgW/2-80, imgH/2-20);   //クリア画面
      buf_gc.drawString("Nice Passion !!", imgW/2-80, imgH/2+20);
      
      break;
    }
    default: // ゲーム中
      // *** ランダムに敵を生成 *** 
      
      buf_gc.drawImage(backpic, 0, backY, imgW ,imgH,this);
    buf_gc.drawImage(backpic, 0, backY - imgH , imgW,imgH,this);
    backY = backY - 2;
    if(backY < 0){
      backY = imgH;
    }

      // *** 自分の弾を発射 ***
      if (ftr.clflag == true && ftr.delaytime == 0) { // もしスペースキーが押されていて＆待ち時間がゼロなら
        for (i = 0; i < ftrBltNum; i++) {      // 全部の弾に関して前から探査して
          if (ftrBlt[i].hp == 0 ) {             // 非アクティブの（死んでいる）弾があれば
            ftrBlt[i].revive(ftr.x, ftr.y);    // 自機から弾を発射して，
            ftr.delaytime = 5;                 // 自機の弾発射待ち時間を元に戻して，
            break;                             // for loop を抜ける
          }
        }
      } 
      else if (ftr.delaytime > 0)            // 弾を発射しない(出来ない)場合は
	        ftr.delaytime--;                       // 待ち時間を１減らす


      for(i = 0;i<enmy.size();i++){ //全部の敵に対して
        if(enmy.get(i).delaytime==0){
          for( j= 0;j<enmyBltnum;j++){ //一匹が出す弾の数だけ
            if(enmyBlt[i][j].hp==0 && enmy.get(i).hp > 0){
              enmyBlt[i][j].revive(enmy.get(i).x, enmy.get(i).y);
              enmy.get(i).delaytime =   10;
              break;
            }
          }
        }
        else if(enmy.get(i).delaytime>0){
          enmy.get(i).delaytime--;
        }
      }

      // *** 各オブジェクト間の衝突チェック ***/
  
  for(i = 0; i<enmy.size();i++) //全部のてきとファイターの衝突チェック
    if(enmy.get(i).hp>0){
      ftr.collisionCheck(enmy.get(i));
      for(j = 0;j<ftrBltNum; j++){
        if(ftrBlt[j].hp > 0){
          ftrBlt[j].collisionCheck(enmy.get(i));
        }
      }
    }
  
  for(i = 0;i<enmyBlt.length;i++){  //敵の弾とファイターの衝突チェっく
    for(j=0;j<enmyBltnum;j++){
      if(enmyBlt[i][j].hp > 0){
        //ftr.collisionCheck(enmyBlt[i][j]);
        if(ftr.collisionCheck(enmyBlt[i][j])){  //爆発のエフェクトのフラグ
          flg = 30;
        }
        if(queen.collisionCheck(enmyBlt[i][j])){
          flg2 = 30;
        }
      }
    }
  }
  if(flg>0){
    buf_gc.drawImage(effectpic, ftr.x-25, ftr.y-55, 40, 40, this);  //爆発のエフェクトの画像貼り付け
    flg--;
  }
  if(flg2>0){
    buf_gc.drawImage(effectpic, queenpic.xp, queenpic.yp-55, 40, 40, this);
    flg2--;
  }
      


      // *** 自機の生死を判断 ***
      if (ftr.hp <= 0 || queen.hp <= 0){  //プレイヤーが死ぬか姫が死んだらゲームオーバー
	      mode = -2; // ゲーム終了
      }
      diecount = 0;
      for(i = 0;i<enmy.size();i++){       //敵が全滅したらゲームクリア
        if(enmy.get(i).hp == 0){
          diecount++;
        }
      }
      if(diecount == enmy.size()){
        this.mode = -3;
      }

      // *** オブジェクトの描画＆移動 ***
      
      for (i = 0; i < ftrBltNum; i++){
	      ftrBlt[i].move(buf_gc, imgW, imgH);
      }
        ftr.move(buf_gc, imgW, imgH);
        queen.move(buf_gc, imgW, imgH);
        buf_gc.drawString("HP "+ queen.hp, queen.x-15, queen.y-30);
        queenpic.cDraw(buf_gc);
        queenpic.cUpdate();

      

      for(i = 0;i < enmyBlt.length;i++){
        for(j=0;j<enmyBltnum;j++){
          enmyBlt[i][j].move(buf_gc, imgW, imgH);
          if(i<enmyAnum && enmyBlt[i][j].hp>0){
            buf_gc.drawImage(bulletpic, enmyBlt[i][j].x-5, enmyBlt[i][j].y-5,8,8, this);
          }
          else if(i>=enmyAnum && i<enmy.size() && enmyBlt[i][j].hp>0){
            buf_gc.drawImage(bulletpic, enmyBlt[i][j].x-13, enmyBlt[i][j].y-13, 25, 25, this);
          }
        }
        
      }
      
      for(i = 0;i<enmy.size();i++){
        enmy.get(i).move(buf_gc, imgW, imgH);
        if(enmy.get(i).hp > 0){
          if(i == enmy.size()-1){
            buf_gc.drawImage(bosspic, enmy.get(i).x-70, enmy.get(i).y-25, 150,70, this);
          }
          else if(i>=enmyAnum && i<enmy.size()-1){
            buf_gc.drawImage(eneBpic, enmy.get(i).x-20, enmy.get(i).y-25, 50,50, this);
          }
          else if(i<enmyAnum){
            buf_gc.drawImage(eneApic, enmy.get(i).x-20, enmy.get(i).y-25, 50, 50, this);
          }
        }
      }

      buf_gc.drawImage(ftrpic, ftr.x-30, ftr.y-30, 50, 50, this);

    
  }
    g.drawImage(buf, 0, 0, this); // 表の画用紙に裏の画用紙 (buffer) の内容を貼り付ける
  
}

  // ■ メソッド (Canvas)
  public void update(Graphics gc) { // repaint() に呼ばれる
    paint(gc);
    //gc.drawImage(bosspic,80,30, this);
  }

  public void mouseDragged(MouseEvent e){
    //使わない
  }

  public void mouseMoved(MouseEvent e){
    
    ftr.x = e.getX();
    ftr.y = e.getY();
  }

  public void mouseEntered(MouseEvent e){}

  public void mouseClicked(MouseEvent e){
    ftr.clflag = true;
    if(this.mode != 1){
        this.mode++;
    }
  }

  public void mouseReleased(MouseEvent e){}

  public void mousePressed(MouseEvent e){}

  public void mouseExited(MouseEvent e){}

}

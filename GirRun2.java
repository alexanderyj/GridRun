import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Iterator;
import javax.imageio.*;
import java.awt.image.BufferedImage;
import javax.lang.model.util.ElementScanner6;
import java.awt.Graphics2D;

import java.io.*;
import java.awt.event.KeyEvent;
import java.util.Queue;
import java.util.LinkedList;
import java.util.ArrayList;

public class GirRun2 {
    public static void main(String[] args) {
        JFrame j = new JFrame();
        ByPanel m = new ByPanel();
        j.setSize(m.getSize());
        j.add(m);

        j.addKeyListener(m);
        j.addMouseListener(m);
        j.setVisible(true);

        j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

class C {
    public static final int GRID_WIDTH = 11;
    public static final int GRID_HEIGHT = 13;
    public static final int TILE_WIDTH = 120;
    public static final int TILE_HEIGHT = 120; 
    public static final int DEL_WALK = 10;
    public static final int DEL_DASH = 20;
    public static final int DEL_SHOOT = 3;
    public static final int DEL_NONE = 0;
    public static final int STEPSIZE_WALK = 1;
    public static final int STEPSIZE_DASH = 3;
    public static final int RAT_SPEED = 6;
    public static final int PIC_SIZE=50;
}

class ByPanel extends JPanel implements KeyListener, ActionListener, MouseListener {
    private Timer time;
    private Wuy boi;
    private boolean[][] grid;
    private Queue<Integer> del;
    private Queue<Integer> moves;
    private int x,y,dir,buff;
    private int dist,dcd,scd;
    private ArrayList<pAttack> bull;
    private ArrayList<Enemy> ee;
    private boolean moving,ready;
    //Hitbox bumper;
    int[] aim;
    private boolean tick,dashing;

    ByPanel() {
        boi = new Wuy();
        boi.spawn(6,6);
        time=new Timer(10, this);

        setSize(1440, 830);
        setVisible(true);
        grid = new boolean[][]{ {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
                                {false,true, true, true, true, true, true, true, true, false,true, true, true, true, true, true, true, true, true, false},
                                {false,true, true, true, true, true, true, true, true, false,true, true, true, false,true, true, true, true, true, false},
                                {false,true, true, false,false,true, true, true, true, false,false,false,true, false,true, true, true, true, true, false},
                                {false,true, true, false,true, true, true, true, true, false,true, true, true, false,true, true, true, true, true, false},
                                {false,true, true, true, true, true, true, true, true, false,true, true, true, false,false,false,true, true, true, false},
                                {false,true, true, true, true, false,true, false,true, false,true, true, true, true, true, true, true, true, false,false},
                                {false,true, true, true, true, true, true, true, true, false,true, true, true, true, false,false,true, true, false,false},
                                {false,true, true, true, true, true, true, true, true, false,true, true, true, true, true, true, true, true, false,false},
                                {false,true, true, true, true, true, true, true, true, false,true, true, true, true, true, true, false,true, true, false},
                               {false,false,false,false,false,false,false,false,false,false,true, true, true, true, true, true, true, true, true, false},
                               {false,true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false},
                               {false,true, true, true, true, true, true, true, true, false,false,false,true, true, true, true, true, true, true, false},
                               {false,true, true, true, true, true, true, false,true, true, true, false,true, true, true, true, true, true, true, false},
                               {false,true, true, true, false,true, true, false,true, true, true, false,true, true, true, true, false,true, true, false},
                               {false,true, true, true, false,true, true, false,false,false,true, true, true, true, true, true, false,true, true, false},
                               {false,true, true, true, false,true, true, true, true, true, true, true, true, true, true, true, false,true, true, false},
                               {false,true, true, true, true, true, true, false,false,false,true, true, true, true, true, true, false,true, true, false},
                               {false,true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false,true, true, false},
                                {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false}};
        del = new LinkedList<Integer>();
        del.add(C.DEL_NONE);
        moves = new LinkedList<Integer>();
        buff=0;
        dir=1;
        dist=0;
        dcd=0;
        scd=0;
        tick=false;
        dashing=false;
        moving = false;
        ready=false;
        bull = new ArrayList<pAttack>();
        //bumper = new Hitbox(840,295,50,255,0,0,"c");
        aim = new int[]{0,0};
        time.start();
        ee = new ArrayList<Enemy>(); 
        ee.add(new Rat(7,2,4,1,8));
        ee.add(new Rat(5,8,1,1,8));
        ee.add(new Rat(1,2,4,1,8));
        ee.add(new Rat(1,8,2,1,9));
        ee.add(new Rat(4,4,2,4,9));
        ee.add(new Rat(15,1,3,11,18));
        ee.add(new Rat(12,4,4,1,8));
        ee.add(new Rat(17,3,3,11,18));
        ee.add(new Rat(14,5,2,11,18));
        ee.add(new Rat(16,5,4,5,15));
        ee.add(new Rat(11,7,1,1,18));
        ee.add(new Rat(8,10,2,4,11));
        ee.add(new Rat(1,12,1,10,18));
        ee.add(new Rat(9,14,1,10,15));
        ee.add(new Rat(4,11,1,10,12));
        ee.add(new Rat(11,12,3,1,18));
        ee.add(new Rat(2,18,2,1,5));
        ee.add(new Rat(4,18,2,1,5));
        ee.add(new Rat(1,15,2,1,4));
        ee.add(new Rat(3,17,2,1,18));
        ee.add(new Rat(4,14,4,14,18));
        ee.add(new Rat(13,9,4,8,10));
        ee.add(new Rat(14,9,1,8,10));
        ee.add(new Rat(16,13,3,6,18));
        ee.add(new Rat(17,13,3,6,18));
        ee.add(new Rat(16,15,1,5,15));
        ee.add(new Rat(18,15,1,1,15));
        ee.add(new Rat(16,17,3,1,18));
        ee.add(new Rat(15,18,3,9,18));
        ee.add(new Rat(17,18,3,9,18));
        ee.add(new Rat(10,16,2,10,13));
        ee.add(new Rat(15,13,3,6,18));
    }
    private boolean nextGrid(int dir) {
        switch(dir)
        {
            case 1:
                return grid[boi.x][boi.y-1];
            case 2:
                return grid[boi.x+1][boi.y];
            case 3:
                return grid[boi.x-1][boi.y];
            case 4:
                return grid[boi.x][boi.y+1];
        }
        return false;
    }
    public void paintComponent(Graphics g) {
        if(!ready) {
            displayInstructions(g);
        } else if(boi.alive) {
            g.setColor(new Color(98, 98, 98));
            g.fillRect(0,0,1440,830);
            /*if(bumper.overlaps(boi.ded)) {
                bumper.color = new Color(0,255,0);
            }*/

            if(del.peek()!=0) {
                buff++;
                if(del.peek()==C.DEL_WALK) {
                    moving = nextGrid(dir);
                }
            } else {
                buff=0;
            }
            if(del.isEmpty()) {
                del.add(C.DEL_NONE);
                buff=0;
            }
            x=boi.x;
            y=boi.y;
            tick=true;

            for(int a=x-9; a<=x+9; a++) {
                for(int b=y-6; b<=y+6; b++) {
                    try{
                        if(grid[a][b]) {
                            g.setColor(new Color(190,190,190));
                        } else {
                            g.setColor(Color.BLACK);
                        }
                        if(dashing) {
                            int stepsize = 12*buff*dist/2;

                            switch(dir) {
                                case 1:
                                    g.fillRect((a-x+9)*C.TILE_WIDTH-420,(b-y+6)*C.TILE_HEIGHT-365+stepsize, C.TILE_WIDTH, C.TILE_HEIGHT);
                                    if(tick) {
                                        for(pAttack p:bull) {
                                            p.shift(0,12*dist/2);
                                        }
                                        for(Enemy e:ee) {
                                            e.shift(0,12*dist/2);
                                        }
                                        //bumper.shift(0,12*dist/2);
                                        tick=false;
                                    }
                                    break;
                                case 2:
                                    g.fillRect((a-x+9)*C.TILE_WIDTH-420-stepsize,(b-y+6)*C.TILE_HEIGHT-365, C.TILE_WIDTH, C.TILE_HEIGHT);
                                    if(tick) {
                                        for(pAttack p:bull) {
                                            p.shift(-12*dist/2,0);
                                        }
                                        for(Enemy e:ee) {
                                            e.shift(-12*dist/2,0);
                                        }
                                        //bumper.shift(-12*dist/2,0);
                                        tick=false;
                                    }
                                    break;
                                case 3:
                                    g.fillRect((a-x+9)*C.TILE_WIDTH-420+stepsize,(b-y+6)*C.TILE_HEIGHT-365, C.TILE_WIDTH, C.TILE_HEIGHT);
                                    if(tick) {
                                        for(pAttack p:bull) {
                                            p.shift(12*dist/2,0);
                                        }
                                        for(Enemy e:ee) {
                                            e.shift(12*dist/2,0);
                                        }
                                        //bumper.shift(12*dist/2,0);
                                        tick=false;
                                    }
                                    break;
                                case 4:
                                    g.fillRect((a-x+9)*C.TILE_WIDTH-420,(b-y+6)*C.TILE_HEIGHT-365-stepsize, C.TILE_WIDTH, C.TILE_HEIGHT);
                                    if(tick) {
                                        for(pAttack p:bull) {
                                            p.shift(0,-12*dist/2);
                                        }
                                        for(Enemy e:ee) {
                                            e.shift(0,-12*dist/2);
                                        }
                                        //bumper.shift(0,-12*dist/2);
                                        tick=false;
                                    }
                                    break;
                            }
                        } else {
                            int stepsize = 0;
                            if (moving)
                            {
                                stepsize = 12*buff;
                            }
                            switch(dir) {
                                case 1:
                                    g.fillRect((a-x+9)*C.TILE_WIDTH-420,(b-y+6)*C.TILE_HEIGHT-365+stepsize, C.TILE_WIDTH, C.TILE_HEIGHT);
                                    if (moving & tick) {
                                        for(pAttack p:bull) {
                                            p.shift(0,12);
                                        }
                                        for(Enemy e:ee) {
                                            e.shift(0,12);
                                        }
                                        //bumper.shift(0,12);
                                        tick=false;
                                    }
                                    break;
                                case 2:
                                    g.fillRect((a-x+9)*120-420-stepsize,(b-y+6)*120-365,120,120);
                                    if (moving & tick) {
                                        for(pAttack p:bull) {
                                            p.shift(-12,0);
                                        }
                                        for(Enemy e:ee) {
                                            e.shift(-12,0);
                                        }
                                        //bumper.shift(-12,0);
                                        tick=false;
                                    }
                                    break;
                                case 3:
                                    g.fillRect((a-x+9)*120-420+stepsize,(b-y+6)*120-365,120,120);
                                    if (moving & tick) {
                                        for(pAttack p:bull) {
                                            p.shift(12,0);
                                        }
                                        for(Enemy e:ee) {
                                            e.shift(12,0);
                                        }
                                        //bumper.shift(12,0);
                                        tick=false;
                                    }
                                    break;
                                case 4:
                                    g.fillRect((a-x+9)*120-420,(b-y+6)*120-365-stepsize,120,120);
                                    if (moving & tick) {
                                        for(pAttack p:bull) {
                                            p.shift(0,-12);
                                        }
                                        for(Enemy e:ee) {
                                            e.shift(0,-12);
                                        }
                                        //bumper.shift(0,-12);
                                        tick=false;
                                    }
                                    break;
                            }
                        }
                    } catch(Exception e) {}
                }
            }
            Iterator<pAttack> p_itr = bull.iterator();
            while(p_itr.hasNext()) {
                pAttack p = p_itr.next();
                boolean b_hit = false;
                Iterator<Enemy> e_itr = ee.iterator();
                Enemy e;
                while(e_itr.hasNext()) {
                    e = e_itr.next();
                    if(p.overlaps(e)) {
                        b_hit = true;
                        e.health--;
                        if(e.health<=0) {
                            e_itr.remove();
                            break;
                        }
                    }
                }
                if (b_hit)
                {
                    p_itr.remove();
                    break;
                }
            }
            for(pAttack p:bull) {
                p.update();
            }
            for(Enemy e:ee) {
                e.update();
            }
            try {
                if(dashing) {
                    /*for(pAttack p:bull) {
                        if(!p.tiles(boi.x,boi.y,dir,12*buff*dist/2,grid)) {
                            bull.remove(p);
                        }
                    }*/
                    Iterator<pAttack> b_itr = bull.iterator();
                    while(b_itr.hasNext()) {
                        pAttack p = b_itr.next();
                        if(!p.tiles(boi.x,boi.y,dir,12*buff*dist/2,grid)) {
                            b_itr.remove();
                            break;
                        }
                    }
                    /*if(!bumper.tiles(boi.x,boi.y,dir,12*buff*dist/2,grid)) {
                        bumper = new Hitbox(-1,-1,1,1,0,0,0,"s");
                    }*/
                } else {
                    Iterator<pAttack> a_itr = bull.iterator();
                    while(a_itr.hasNext()) {
                        pAttack p = a_itr.next();
                        if(!p.tiles(boi.x,boi.y,dir,12*buff,grid)) {
                            a_itr.remove();
                            break;
                        }
                    }
                    /*if(!bumper.tiles(boi.x,boi.y,dir,12*buff,grid)) {
                        bumper = new Hitbox(-1,-1,1,1,0,0,0,"s");
                    }*/
                }
            } catch(Exception e) {}
            for(pAttack p:bull) {
                p.show(g);
            }
            for(Enemy e:ee) {
                e.show(g);
            }
            //bumper.show(g);
            g.setFont(new Font("Courier",Font.PLAIN,24));
            g.setColor(new Color(255,0,0));
            g.drawString("Health: "+boi.health,20,20);
            if(dcd>0) {
                dcd--;
                g.drawString(""+dcd/100.0,20,48);
            }
            if(scd>0) {
                scd--;
            }
            if(!boi.invinc) {
                for(Enemy e:ee) {
                    if(boi.overlaps(e)) {
                        boi.health--;
                        boi.setI(150);
                        if(boi.health==0) {
                            boi.alive=false;
                        }
                    }
                }
            }
            if(dir==2) {
                boi.setDir(1);
            } else if(dir==3) {
                boi.setDir(0);
            }

            boi.update(g);
            
            if(buff == del.peek()) {
                fin(dir,del.peek());
                buff=C.DEL_NONE;
                try {
                    act(moves.poll(),true);
                } catch(Exception e) {}
                moving = false;
                del.remove();
                if (del.isEmpty()) {
                    del.add(C.DEL_NONE);
                }
            }
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0,0,1440,830);
            g.setColor(Color.RED);
            g.setFont(new Font("Courier", Font.BOLD, 60));
            g.drawString("YOU DIED", 540,385);
        }
    }
    public void mouseClicked(MouseEvent e) {    
    }
    public void mousePressed(MouseEvent e) {
        if(!ready) {
            ready=true;
        }
        if(scd==0) {
            int x = e.getX();
            int y = e.getY();
                aim[0]=x;
                aim[1]=y;
                shoot(false);
            return;
        }
    }
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if((keyCode==KeyEvent.VK_UP||keyCode==KeyEvent.VK_W)) {
            if(grid[boi.x+0][boi.y-1]) {
                mov(1,false);
            }
        }
        if((keyCode==KeyEvent.VK_RIGHT||keyCode==KeyEvent.VK_D)) {
            if(grid[boi.x+1][boi.y+0]) {
                mov(2,false);
            }
        }
        if((keyCode==KeyEvent.VK_LEFT||keyCode==KeyEvent.VK_A)) {
            if(grid[boi.x-1][boi.y-0]) {
                mov(3,false);
            }
        }
        if((keyCode==KeyEvent.VK_DOWN||keyCode==KeyEvent.VK_S)) {
            if(grid[boi.x+0][boi.y+1]) {
                mov(4,false);
            }
        }
        if(keyCode==KeyEvent.VK_SPACE) {
            dash(dir,false);
        }
    }
    public void act(int n, boolean v) {
        if(1<=n&&n<=4) {
            /*
            switch(n) {
                case 1:
                if(grid[boi.x+0][boi.y-1]) {
                    mov(n,v);
                }
                break;
                case 2:
                if(grid[boi.x+1][boi.y+0]) {
                    mov(n,v);
                }
                break;
                case 3:
                if(grid[boi.x-1][boi.y-0]) {
                    mov(n,v);
                }
                break;
                case 4:
                if(grid[boi.x-0][boi.y+1]) {
                    mov(n,v);
                }
                break;
            }*/
            mov(n,v);
        } else if(n==5) {
            dash(n,v);
        } else if(n==6) {
            shoot(true);
        }
    }
    public void shoot(boolean v) {
        if(v||del.peek()==0) {
            boi.setState(1);
            if(!v) {
                del.add(4);
                
                if(del.peek()==0) {
                    del.remove();
                }
            }
            //bull.add(new pAttack(aim[0],aim[1]));
            scd=40;
        } else {
            if(del.size()<4&&del.peek()-buff<9&&del.peek()!=0) {
                if(!v) {
                    del.add(3);
                }
                moves.add(6);
            }
        }
    }
    public void mov(int n,boolean v) {
        if(v||del.peek()==0) {
            boi.setState(0);
            switch(n) {
                case 1:
                    dir=1;
                    if(grid[boi.x+0][boi.y-1]) {
                        if(!v) {
                            del.add(10);
                            
                            if(del.peek()==0) {
                                del.remove();
                            }
                        }
                        dist=1;
                    } else {
                        dist=0;
                    }
                    break;
                case 2:
                    dir=2;
                    if(grid[boi.x+1][boi.y+0]) {
                        if(!v) {
                            del.add(10);
                            
                            if(del.peek()==0) {
                                del.remove();
                            }
                        }
                        dist=1;
                    } else {
                        dist=0;
                    }
                    break;
                case 3:
                    dir=3;
                    if(grid[boi.x-1][boi.y-0]) {
                        if(!v) {
                            del.add(10);
                            
                            if(del.peek()==0) {
                                del.remove();
                            }
                        }
                        dist=1;
                    } else {
                        dist=0;
                    }
                    break;
                case 4:
                    dir=4;
                    if(grid[boi.x+0][boi.y+1]) {
                        if(!v) {
                            del.add(10);
                            
                            if(del.peek()==0) {
                                del.remove();
                            }
                        }


                        dist=1;
                    } else {
                        dist=0;
                    }
                    break;      
            }
        } else {
            if(del.size()<4&&del.peek()-buff<9&&del.peek()!=0) {
                if(!v) {
                    del.add(10);
                }
                moves.add(n);
            }
        }
    }
    public void dash(int n, boolean v) {
        if(dcd==0&&(v||del.peek()==0)) {
            boi.setState(2);
            if(!v) {
                del.add(20);
                
                if(del.peek()==0) {
                    del.remove();
                }
            }
            boi.setI(20);
            switch(dir) {
                case 1:
                try {
                    if(grid[boi.x][boi.y-3]) {
                        dist=3;
                    } else if(grid[boi.x][boi.y-2]) {
                        dist=2;
                    } else if(grid[boi.x][boi.y-1]) {
                        dist=1;
                    } else {
                        dist=0;
                    }
                } catch (Exception e) {
                    try{
                        if(grid[boi.x][boi.y-2]) {
                            dist=2;
                        } else if(grid[boi.x][boi.y-1]) {
                            dist=1;
                        } else {
                            dist=0;
                        }
                    } catch(Exception f) {
                        if(grid[boi.x][boi.y-1]) {
                            dist=1;
                        } else {
                            dist=0;
                        }
                    }
                }
                dashing=true;
                break;
                case 2:
                try {
                    if(grid[boi.x+3][boi.y]) {
                        dist=3;
                    } else if(grid[boi.x+2][boi.y]) {
                        dist=2;
                    } else if(grid[boi.x+1][boi.y]) {
                        dist=1;
                    } else {
                        dist=0;
                    }
                } catch (Exception e) {
                    try{
                        if(grid[boi.x+2][boi.y]) {
                            dist=2;
                        } else if(grid[boi.x+1][boi.y]) {
                            dist=1;
                        } else {
                            dist=0;
                        }
                    } catch(Exception f) {
                        if(grid[boi.x+1][boi.y]) {
                            dist=1;
                        } else {
                            dist=0;
                        }
                    }
                }
                dashing=true;
                break;
                case 3:
                try {
                    if(grid[boi.x-3][boi.y]) {
                        dist=3;
                    } else if(grid[boi.x-2][boi.y]) {
                        dist=2;
                    } else if(grid[boi.x-1][boi.y]) {
                        dist=1;
                    } else {
                        dist=0;
                    }
                } catch (Exception e) {
                    try{
                        if(grid[boi.x-2][boi.y]) {
                            dist=2;
                        } else if(grid[boi.x-1][boi.y]) {
                            dist=1;
                        } else {
                            dist=0;
                        }
                    } catch(Exception f) {
                        if(grid[boi.x-1][boi.y]) {
                            dist=1;
                        } else {
                            dist=0;
                        }
                    }
                }
                dashing=true;
                break;
                case 4:
                try {
                    if(grid[boi.x][boi.y+3]) {
                        dist=3;
                    } else if(grid[boi.x][boi.y+2]) {
                        dist=2;
                    } else if(grid[boi.x][boi.y+1]) {
                        dist=1;
                    } else {
                        dist=0;
                    }
                } catch (Exception e) {
                    try{
                        if(grid[boi.x][boi.y+2]) {
                            dist=2;
                        } else if(grid[boi.x][boi.y+1]) {
                            dist=1;
                        } else {
                            dist=0;
                        }
                    } catch(Exception f) {
                        if(grid[boi.x][boi.y+1]) {
                            dist=1;
                        } else {
                            dist=0;
                        }
                    }
                }
                dashing=true;
                break;
            }
            dcd=200;
        } else {
            if(dcd==0&&del.size()<5&&del.peek()-buff<6&&del.peek()!=0) {
                if(!v) {
                    del.add(20);
                }
                moves.add(5);
            }
        }
    }
    public void keyReleased(KeyEvent e) {
        /*
        x=del.remove();
        while(!del.isEmpty()) {
            del.remove();
        }
        del.add(x);
        x=-1;
        y=-1;
        try {
            x=moves.poll();
            while(!moves.isEmpty()) {
                moves.remove();
            }
            if(x>0) {
                moves.add(x);
            }
        } catch(Exception a) {}
        try {
            x=moves.poll();
            y=moves.poll();
            while(!moves.isEmpty()) {
                moves.remove();
            }
            moves.add(x);
            moves.add(y);
        } catch(Exception a) {} */
        /*x=del.remove();
        try {
            y=del.remove();
            while(!del.isEmpty()) {
                del.remove();
            }
            del.add(x);
            del.add(y);
            x=moves.remove();
            y=moves.remove();
            while(!moves.isEmpty()) {
                moves.remove();
            }
            moves.add(x);
            moves.add(y);
        } catch(Exception t) {
            while(!del.isEmpty()) {
                del.remove();
            }
            del.add(x);
            x=-1;
            x=moves.poll();
            while(!moves.isEmpty()) {
                moves.remove();
            }
            if(x>0) {
                moves.add(x);
            }
        }*/
    }
    public void keyTyped(KeyEvent e) {}
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
    private void displayInstructions(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 1440, 830);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Courier",Font.PLAIN,20));
        g.drawString("WASD/Arrow Keys to move", 580, 340);
        g.drawString("Space to dash", 632, 370);
        g.drawString("Click to shoot", 626, 400);
        g.drawString("Click to start!", 623, 450);
    }
    public void fin(int d,int dd) {
        switch(d) {
            case 1:
            if(grid[boi.x][boi.y-dist]) {
                boi.y-=dist;
            }
            break;
            case 2:
            if(grid[boi.x+dist][boi.y]) {
                boi.x+=dist;
            }
            break;
            case 3:
            if(grid[boi.x-dist][boi.y]) {
                boi.x-=dist;
            }
            break;
            case 4:
            if(grid[boi.x][boi.y+dist]) {
                boi.y+=dist;
            }
            break;
        }
        if(dd==4) {
            //insert new shoot statement here in... a few weeks?
            bull.add(new pAttack(aim[0],aim[1]));
            //why doesn't this work i swear to godddddddddd
        }
        if(!dashing) {
            if(del.peek()==0) {
                switch(d) {
                    case 1:
                    //bumper.shift(0,12);
                    break;
                    case 2:
                    //bumper.shift(-12,0);
                    break;
                    case 3:
                    //bumper.shift(12,0);
                    break;
                    case 4:
                    //bumper.shift(0,-12);
                    break;
                }
            }
        } else {
            switch(d) {
                case 1:
                //bumper.shift(0,6*dist);
                break;
                case 2:
                //bumper.shift(-6*dist,0);
                break;
                case 3:
                //bumper.shift(6*dist,0);
                break;
                case 4:
                //bumper.shift(0,-6*dist);
                break;
            }
        }
        dist=0;
        if(dashing) {
            boi.setState(0);
        }
        dashing=false;
    }
}

class Wuy {
    int x,y;
    Hitbox ded;
    boolean rit;
    boolean invinc,alive,hit;
    Color t;
    int health,iFrames,dir,state,tick,ind;
    BufferedImage xx;
    BufferedImage[] active,idle,shoot,dash;

    public Wuy() {
        x=-1;
        y=-1;
        ded = null;
        rit=true;
        invinc=false;
        hit=false;
        t=new Color(58,100,120);
        idle = new BufferedImage[4];
        shoot = new BufferedImage[6];
        dash = new BufferedImage[3];
        active=idle;
        health=3;
        iFrames=0;
        state=0;
        tick=0;
        ind=0;
        dir=1;
        alive=true;
        try {
            xx = ImageIO.read(new File("guy-0.png"));
            idle[0]=xx;
            xx = ImageIO.read(new File("guy-1.png"));
            idle[1]=xx;
            xx = ImageIO.read(new File("guy-2.png"));
            idle[2]=xx;
            xx = ImageIO.read(new File("guy-3.png"));
            idle[3]=xx;

            xx = ImageIO.read(new File("pew-0.png"));
            shoot[0]=xx;
            xx = ImageIO.read(new File("pew-1.png"));
            shoot[1]=xx;
            xx = ImageIO.read(new File("pew-2.png"));
            shoot[2]=xx;
            xx = ImageIO.read(new File("pew-3.png"));
            shoot[3]=xx;
            xx = ImageIO.read(new File("pew-4.png"));
            shoot[4]=xx;
            xx = ImageIO.read(new File("pew-5.png"));
            shoot[5]=xx;

            xx = ImageIO.read(new File("dash-0.png"));
            dash[0]=xx;
            xx = ImageIO.read(new File("dash-1.png"));
            dash[1]=xx;
            xx = ImageIO.read(new File("dash-2.png"));
            dash[2]=xx;
        } catch(Exception e) {System.out.println("urthrowing");}
    }

    public void show(Graphics g) {
        ded.show(g);
    }

    public void setState(int x) {
        if(x!=state) {
            if(x==0) {
                active=idle;
            } else if(x==1) {
                active=shoot;
            } else if(x==2) {
                active=dash;
            }
            tick=0;
            ind=0;
        } else if(state==1)  {
            tick=0;
            ind=0;
        }
        state=x;
    }

    public void setDir(int d) {
        //i promise this does more later
        
        dir=d;
    }

    public boolean overlaps(Hitbox why) {
        return ded.overlaps(why);
    }

    public void spawn(int ex,int ey) {
        x=ex;
        y=ey;
        ded= new Hitbox(720,415,80,80,58,220,238, "s");
    }

    public void update(Graphics g) {
        if(iFrames>0) {
            invinc=true;
            iFrames--;
        } else {
            invinc=false;
        }
        tick++;
        if(tick==8) {
            tick=0;
            ind++;
            ind%=active.length;
            if(state==1 && ind==0) {
                setState(0);
            }
        }
        if(dir==0) {
            if(invinc) {
                xx = new BufferedImage(120,120,active[ind].getType());
                Graphics2D eeee = xx.createGraphics();
                AlphaComposite alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f);
                eeee.setComposite(alcom);
                eeee.drawImage(active[ind],0,0,null);
                eeee.dispose();
                ded.show(g,xx,-20,-20);
            } else {
                ded.show(g,active[ind],-20,-20);
            }
        } else {
            if(invinc) {
                xx = new BufferedImage(120,120,active[ind].getType());
                Graphics2D eeee = xx.createGraphics();
                AlphaComposite alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.4f);
                eeee.setComposite(alcom);
                eeee.drawImage(active[ind],120,0,-120,120,null);
                eeee.dispose();
                ded.show(g,xx,-20,-20);
            } else {
                xx = new BufferedImage(120,120,active[ind].getType());
                Graphics2D eeee = xx.createGraphics();
                eeee.drawImage(active[ind],120,0,-120,120,null);
                eeee.dispose();
                ded.show(g,xx,-20,-20);
            }
        }
    }
    public void setI(int x) {
        iFrames=Math.max(iFrames,x);
    }
}

class Hitbox {
    public int[] coords;
    public Color color;
    private String shape;

    public Hitbox(int x1,int y1,int l,int h, int r, int g, int b, String s) {
        shape=s;
        if(s.equals("s")) {
            coords = new int[4];
            coords[0]=x1;
            coords[1]=y1;
            coords[2]=l;
            coords[3]=h;
        }
        color = new Color(r,g,b);
    }

    public boolean tiles(int x, int y, int dir, int buff, boolean[][] grid) {
        int a=coords[0]; 
        int b=coords[1];
        int ind1=0;
        int ind2=0;
        ArrayList<Boolean> set = new ArrayList<Boolean>();
        switch(dir) {
            case 1:
                a=(a+60)%120; 
                if(a<0) {
                    a+=120; 
                }
                b=(b+5-buff)%120;
                if(b<0) {
                    b+=120;
                }
                ind1=(int) Math.floor((coords[0]-660)/120.0)+x;
                /*if(coords[0]-660<0) {
                    ind1--;
                }*/
                ind2=(int) Math.floor((coords[1]-355-buff)/120.)+y;
                /*if(coords[1]-355<0) {
                    ind2--;
                }*/
                break;
            case 2:
                a=(a+60-buff)%120;
                if(a<0) {
                    a+=120; 
                }
                b=(b+5)%120;
                if(b<0) {
                    b+=120;
                }
                ind1=(int) Math.floor((coords[0]-660-buff)/120.0)+x;
                /*if(coords[0]-660<0) {
                    ind1--;
                }*/
                ind2=(int) Math.floor((coords[1]-355)/120.0)+y;
                /*if(coords[1]-355<0) {
                    ind2--;
                }*/
                break;
            case 3:
                a=(a+60+buff)%120;
                if(a<0) {
                    a+=120; 
                }
                b=(b+5)%120;
                if(b<0) {
                    b+=120;
                }
                ind1=(int) Math.floor((coords[0]-660+buff)/120.0)+x;
                /*if(coords[0]-660<0) {
                    ind1--;
                }*/
                ind2=(int) Math.floor((coords[1]-355)/120.0)+y;
                /*if(coords[1]-355<0) {
                    ind2--;
                }*/
                break;
            case 4:
                a=(a+60)%120;
                if(a<0) {
                    a+=120; 
                }
                b=(b+5+buff)%120;
                if(b<0) {
                    b+=120;
                }
                ind1=(int) Math.floor((coords[0]-660)/120.0)+x;
                /*if(coords[0]-660<0) {
                    ind1--;
                }*/
                ind2=(int) Math.floor((coords[1]-355+buff)/120.0)+y;
                /*if(coords[1]-355<0) {
                    ind2--;
                }*/
        }
        /*
        System.out.println(coords[0]-660);
        System.out.println(ind1);
        System.out.println(coords[1]-355);
        System.out.println(ind2); */
        switch(shape) {
            case "c":
                set.add(grid[ind1][ind2]);
                if(coords[2]>a) {
                    set.add(grid[ind1-1][ind2]);
                }
                if(coords[2]>b) {
                    set.add(grid[ind1][ind2-1]);
                }
                if((Math.pow(a,2)+Math.pow(b,2))<Math.pow(coords[2],2)) {
                    set.add(grid[ind1-1][ind2-1]);
                }
                if(coords[2]>120-a) {
                    set.add(grid[ind1+1][ind2]);
                }
                if(coords[2]>120-b) {
                    set.add(grid[ind1][ind2+1]);
                }
                if(Math.pow(120-a,2)+Math.pow(120-b,2)<Math.pow(coords[2],2)) {
                    set.add(grid[ind1+1][ind2+1]);
                }
                if(Math.pow(120-a,2)+Math.pow(b,2)<Math.pow(coords[2],2)) {
                    set.add(grid[ind1+1][ind2-1]);
                }
                if(Math.pow(a,2)+Math.pow(120-b,2)<Math.pow(coords[2],2)) {
                    set.add(grid[ind1-1][ind2+1]);
                }
                break;
            case "s":
                set.add(grid[ind1][ind2]);
                if(coords[2]/2>a) {
                    set.add(grid[ind1-1][ind2]);
                }
                if(coords[3]/2>b) {
                    set.add(grid[ind1][ind2-1]);
                }
                if(coords[2]/2>120-a) {
                    set.add(grid[ind1+1][ind2]);
                }
                if(coords[3]/2>120-b) {
                    set.add(grid[ind1][ind2+1]);
                }
                if(coords[2]/2>a&&coords[3]/2>b) {
                    set.add(grid[ind1-1][ind2-1]);
                }
                if(coords[2]/2>120-a&&coords[3]/2>b) {
                    set.add(grid[ind1+1][ind2-1]);
                }
                if(coords[2]/2>120-a&&coords[3]/2>120-b) {
                    set.add(grid[ind1+1][ind2+1]);
                }
                if(coords[2]/2>a&&coords[3]/2>120-b) {
                    set.add(grid[ind1-1][ind2+1]);
                }
        }
        for(boolean g:set) {
            if(!g) {
                return g;
            }
        }
        return true;
    }

    public Hitbox(int x, int y, int r, int red, int g, int b, String c) {
        shape=c;
        coords = new int[3];
        coords[0]=x;
        coords[1]=y;
        coords[2]=r;
        color = new Color(red,g,b);
    }

    public void shift(int x,int y) {
        coords[0]+=x;
        coords[1]+=y;
    }

    public double dist(double angle) {
        if(shape.equals("c")) {
            return coords[2];
        } else {
            double t = Math.atan2(coords[3],coords[2]);
            if(t==Math.PI/2||t==Math.PI*3/2) {
            } else if(t==0) {
            }
            if(0<=angle&&angle<=t) {
                return Math.sqrt((Math.pow(coords[2]/2,2))+Math.pow(coords[2]/2.0*Math.tan(angle)/2,2));
            } else {
                return Math.sqrt((Math.pow(coords[3]/2,2))+Math.pow(coords[3]/2.0*Math.tan(Math.PI/2-angle),2));
            }
        }
    }

    public boolean overlaps(Hitbox b) {
        double pain = Math.atan2(Math.abs(b.coords[1]-coords[1]),Math.abs(b.coords[0]-coords[0]));
        pain%=Math.PI/2;
        if(b.dist(pain)+dist(pain)>Math.sqrt(Math.pow(b.coords[1]-coords[1],2)+Math.pow(b.coords[0]-coords[0],2))) {
            return true;
        }
        return false;
    }

    public void show(Graphics g) {
        g.setColor(color);
        if(shape.equals("s")) {
            g.fillRect(coords[0]-coords[2]/2,coords[1]-coords[3]/2,coords[2],coords[3]);
        }
        if(shape.equals("c")) {
            g.fillOval(coords[0]-coords[2],coords[1]-coords[2],2*coords[2],2*coords[2]);
        }
    }
    
    public void show(Graphics g,Color c) {
        g.setColor(c);
        if(shape.equals("s")) {
            g.fillRect(coords[0]-coords[2]/2,coords[1]-coords[3]/2,coords[2],coords[3]);
        }
        if(shape.equals("c")) {
            g.fillOval(coords[0]-coords[2],coords[1]-coords[2],2*coords[2],2*coords[2]);
        }
    }

    public void show(Graphics g, BufferedImage j) {
        if(shape.equals("s")) {
            g.drawImage(j,coords[0]-coords[2]/2,coords[1]-coords[3]/2,null);
        } else if(shape.equals("c")) {
            g.drawImage(j,coords[0]-coords[2],coords[1]-coords[2],null);
        }
    }
    public void show(Graphics g, BufferedImage j, int x, int y) {
        g.drawImage(j,coords[0]-coords[2]/2+x,coords[1]-coords[3]/2+y,null);
    }
}

class pAttack extends Hitbox {
    int[] flig;
    public final static int dmg=1;
    ArrayList<BufferedImage> pews;
    int ind;

    public pAttack(int x, int y) {
        super(720,415,40,255,255,0,"c");
        flig = new int[2];
        int yeeeeee = (int) Math.sqrt(Math.pow(x-720,2)+Math.pow(y-450,2));
        flig[0]=(x-720)*16/yeeeeee;
        flig[1]=(y-450)*16/yeeeeee;
        BufferedImage pew1=null;
        BufferedImage pew2=null;
        BufferedImage pew3=null;
        if(x-720<0) {
            try {
                pew1 = ImageIO.read(new File("pixil-frame-0.png"));
                pew2 = ImageIO.read(new File("pixil-frame-1.png"));
                pew3 = ImageIO.read(new File("pixil-frame-2.png"));
            } catch(Exception e) {System.out.println("urthrowing");}
        } else {
            try {
                pew1 = ImageIO.read(new File("bul0.png"));
                pew2 = ImageIO.read(new File("bul1.png"));
                pew3 = ImageIO.read(new File("bul2.png"));
            } catch(Exception e) {System.out.println("urthrowing");}
        }
        BufferedImage yee1 = new BufferedImage(C.PIC_SIZE,C.PIC_SIZE,pew1.getType());
        BufferedImage yee2 = new BufferedImage(C.PIC_SIZE,C.PIC_SIZE,pew2.getType());
        BufferedImage yee3 = new BufferedImage(C.PIC_SIZE,C.PIC_SIZE,pew3.getType());
        Graphics2D g1d = yee1.createGraphics();
        g1d.drawImage(pew1, 0, 0, C.PIC_SIZE, C.PIC_SIZE, null);
        g1d.dispose();
        Graphics2D g2d = yee2.createGraphics();
        g2d.drawImage(pew2, 0, 0, C.PIC_SIZE, C.PIC_SIZE, null);
        g2d.dispose();
        Graphics2D g3d = yee3.createGraphics();
        g3d.drawImage(pew3, 0, 0, C.PIC_SIZE, C.PIC_SIZE, null);
        g3d.dispose();
        pews = new ArrayList<BufferedImage>();
        pews.add(yee1);
        pews.add(yee2);
        pews.add(yee3);
        ind=0;
    }

    public void show(Graphics g) {
        g.drawImage(pews.get(ind),coords[0]-C.PIC_SIZE/2,coords[1]-C.PIC_SIZE/2,null);
        ind++;
        ind%=3;
    }
    public void update() {
        shift(flig[0],flig[1]);
    }
}

class Enemy extends Hitbox {
    int dmg;
    int health;
    public Enemy(int x, int y, int w, int h, int cont,int hp) {
        super(x, y, w, h, 27,119,9,"s");
        dmg=cont;
        health=hp;
    }

    public void shift(int x, int y) {
        super.shift(x,y);
    }

    public void update() {
        System.out.println("ya dun goofed");
    }
}

class Rat extends Enemy {
    int[] walk;
    int low;
    int high;
    int dir;
    int ind,tic,inder;
    ArrayList<BufferedImage> pews,pewsa,active;
    public Rat(int x, int y, int dir, int min, int max) {
        super((x-6)*120+720,(y-6)*120+415,100,100,10,5);
        this.dir=dir;
        BufferedImage pew1=null;
        BufferedImage pew2=null;
        try {
            pew1 = ImageIO.read(new File("rats-0.png"));
            pew2 = ImageIO.read(new File("rats-1.png"));
        } catch(Exception e) {System.out.println("urthrowing");}
        BufferedImage yee1 = new BufferedImage(C.PIC_SIZE*2,C.PIC_SIZE*2,pew1.getType());
        BufferedImage yee2 = new BufferedImage(C.PIC_SIZE*2,C.PIC_SIZE*2,pew2.getType());
        Graphics2D g1d = yee1.createGraphics();
        g1d.drawImage(pew1, 0, 0, C.PIC_SIZE*2, C.PIC_SIZE*2, null);
        g1d.dispose();
        Graphics2D g2d = yee2.createGraphics();
        g2d.drawImage(pew2, 0, 0, C.PIC_SIZE*2, C.PIC_SIZE*2, null);
        g2d.dispose();
        pews = new ArrayList<BufferedImage>();
        pews.add(yee1);
        pews.add(yee2);
        try {
            pew1 = ImageIO.read(new File("rats-0a.png"));
            pew2 = ImageIO.read(new File("rats-1a.png"));
        } catch(Exception e) {System.out.println("urthrowing");}
        yee1 = new BufferedImage(C.PIC_SIZE*2,C.PIC_SIZE*2,pew1.getType());
        yee2 = new BufferedImage(C.PIC_SIZE*2,C.PIC_SIZE*2,pew2.getType());
        Graphics2D g3d = yee1.createGraphics();
        g3d.drawImage(pew1, 0, 0, C.PIC_SIZE*2, C.PIC_SIZE*2, null);
        g3d.dispose();
        Graphics2D g4d = yee2.createGraphics();
        g4d.drawImage(pew2, 0, 0, C.PIC_SIZE*2, C.PIC_SIZE*2, null);
        g4d.dispose();
        pewsa = new ArrayList<BufferedImage>();
        pewsa.add(yee1);
        pewsa.add(yee2);
        ind=0;
        if(dir==1||dir==4) {
            inder=0;
            active=pews;
        } else {
            inder=1;
            active=pewsa;
        }
        tic=0;
        
        health=2;
        switch(dir) {
           case 1:
           walk = new int[]{0,-C.RAT_SPEED};
           low = 120 * (min-6) + 355;
           high = 120 * (max-6) + 475;
           break;
           case 2:
           walk = new int[]{C.RAT_SPEED,0};
           low = 120 * (min-6) + 660;
           high = 120 * (max-6) + 780;
           break;
           case 3:
           walk = new int[]{-C.RAT_SPEED,0};
           low = 120 * (min-6) + 660;
           high = 120 * (max-6) + 780;
           break;
           case 4:
           walk = new int[]{0,C.RAT_SPEED};
           low = 120 * (min-6) + 355;
           high = 120 * (max-6) + 475;
           break;
        }
    }

    public void shift(int x, int y) {
        if(dir==1||dir==4) {
            low+=y;
            high+=y;
        } else {
            low+=x;
            high+=x;
        }
        super.shift(x,y);
    }
    public void update() {
        super.shift(walk[0],walk[1]);
        if(dir==1||dir==4) {
            //System.out.println(low+" "+(coords[1]-50-C.RAT_SPEED));
            if(coords[1]-50-C.RAT_SPEED<=low || coords[1]+50+C.RAT_SPEED>=high) {
                walk[0]*=-1;
                walk[1]*=-1;
                inder+=1;
                inder%=2;
                if(inder==0) {
                    active=pews;
                } else {
                    active=pewsa;
                }
            }
        } else {
            if(coords[0]-50-C.RAT_SPEED<=low || coords[0]+50+C.RAT_SPEED>=high) {
                walk[0]*=-1;
                walk[1]*=-1;
                inder+=1;
                inder%=2;
                if(inder==0) {
                    active=pews;
                } else {
                    active=pewsa;
                }
            }
        }
        tic++;
        if(tic==20) {
            tic=0;
            ind++;
            ind%=2;
        }
    }
    public void show(Graphics g) {
        super.show(g,active.get(ind));
    }
}

/*class eAttack extends Hitbox {
    int[] flig;
    public static final int dmg=1;

    public eAttack(int x,int y) {
        super(x,y,40,255,0,0,"c");
    }

    public void update() {
        shift(flig[0],flig[1]);
    }
}*/
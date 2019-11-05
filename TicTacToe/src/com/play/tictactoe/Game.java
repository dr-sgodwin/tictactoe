package com.play.tictactoe;

import android.annotation.SuppressLint;
import android.content.Context; 
import android.graphics.Canvas; 
import android.graphics.Paint; 
import android.graphics.Paint.Style; 
import android.os.Handler; 
import android.os.Message; 
import android.view.MotionEvent; 
import android.view.View; 
import android.widget.Toast; 

@SuppressLint("HandlerLeak")
public class Game extends View{
	
	private Cell[][] singlesquare = null; 
    int x = 3; 
    int y = 3; 
    private int l; 
    private int a; 
    private boolean whatdrawn = false; 
    private int playerwin = 3; 
    private Paint pen; 
  
    Handler handler = new Handler() { 
        // @Override 
        public void handleMessage(Message msg) { 
            switch (msg.what) { 
            case 0: 
                invalidate(); 
                break; 
            case 1: 
                Toast.makeText(getContext(), "You Win!", Toast.LENGTH_SHORT).show(); 
                break; 
            case 2: 
                Toast.makeText(getContext(), "Computer Win!", Toast.LENGTH_SHORT).show(); 
                break; 
            case 3: 
                Toast.makeText(getContext(), "Draw!", Toast.LENGTH_SHORT).show(); 
                break; 
            default: 
                break; 
            } 
  
            super.handleMessage(msg); 
        } 
    };


    public int getGameSize() { 
        return x; 
    } 

    public Game(Context context) { 
        super(context); 
  
        pen = new Paint(); 
  
        this.pen.setARGB(255, 0, 0, 0); 
        this.pen.setAntiAlias(true); 
        this.pen.setStyle(Style.STROKE); 
        this.pen.setStrokeWidth(15); 
  
        l = this.getWidth(); 
        a = this.getHeight(); 
  
        singlesquare = new Cell[x][y]; 
  
        int xss = l / x; 
        int yss = a / y; 
  
        for (int z = 0; z < y; z++) { 
            for (int i = 0; i < x; i++) { 
                singlesquare[z][i] = new Empty(xss * i, z * yss); 
            } 
        } 
    } 

    @Override
    protected void onDraw(Canvas canvas) { 
        for (int i = 0; i < singlesquare.length; i++) { 
            for (int j = 0; j < singlesquare[0].length; j++) { 
                singlesquare[i][j].draw(canvas, getResources(), j, i, (this
                        .getWidth() + 3) 
                        / singlesquare.length, this.getHeight() 
                        / singlesquare[0].length); 
            } 
        } 
  
        int xs = this.getWidth() / x; 
        int ys = this.getHeight() / y; 
        for (int i = 0; i <= x; i++) { 
            canvas.drawLine(xs * i, 0, xs * i, this.getHeight(), pen); 
        } 
        for (int i = 0; i <= y; i++) { 
            canvas.drawLine(0, ys * i, this.getWidth(), ys * i, pen); 
        } 
  
        super.onDraw(canvas); 
    } 

    @Override
    public boolean onTouchEvent(MotionEvent event) { 
        int x_aux = (int) (event.getX() / (this.getWidth() / x)); 
        int y_aux = (int) (event.getY() / (this.getHeight() / y)); 
        drawimage(x_aux, y_aux); 
        return super.onTouchEvent(event); 
    } 
  
    public String getPiece(int player) { 
        switch (player) { 
        case 1: 
            return "x"; 
        case -1: 
            return "o"; 
        } 
        return null; 
    } 

    public void drawimage(int x_aux, int y_aux) { 
        Cell cel = null; 
        if (whatdrawn)  
        { 
            cel = new Cross(singlesquare[x_aux][y_aux].x,singlesquare[x_aux][y_aux].y); 
            whatdrawn = false; 
        }  
        else 
        { 
            cel = new Ball(singlesquare[x_aux][y_aux].x, singlesquare[x_aux][y_aux].y); 
            whatdrawn = true; 
        } 
  
        singlesquare[y_aux][x_aux] = cel; 
  
        handler.sendMessage(Message.obtain(handler, 0)); 
  
        if (validate_game()) { 
            if (whatdrawn) { 
                System.out.println("You Win"); 
                handler.sendMessage(Message.obtain(handler, 1)); 
            } else { 
                System.out.println("Computer Win"); 
                handler.sendMessage(Message.obtain(handler, 2)); 
            } 
            resizegame(x); 
              
        } else if (isFull()) { 
            System.out.println("Loose"); 
            handler.sendMessage(Message.obtain(handler, 3)); 
            resizegame(x); 
          
        } 
    } 
  
    private boolean validate_game() { 
        int counter = 0; 
        Cell previous = null; 
  
        for (int i = 0; i < singlesquare.length; i++) { 
            for (int j = 0; j < singlesquare[0].length; j++) { 
                System.out.print(singlesquare[i][j]); 
                if (!singlesquare[i][j].equals(previous) 
                        || singlesquare[i][j] instanceof Empty) { 
  
                    previous = singlesquare[i][j]; 
                    counter = 0; 
                } else { 
                    counter++; 
                } 
                if (counter >= getPlayerwin() - 1) { 
                    return true; 
                } 
  
            } 
            System.out.println(""); 
            previous = null; 
            counter = 0; 
        } 
  
        previous = null; 
        for (int j = 0; j < singlesquare[0].length; j++) { 
            for (int i = 0; i < singlesquare.length; i++) { 
                System.out.print(singlesquare[i][j]); 
                if (!singlesquare[i][j].equals(previous) 
                        || singlesquare[i][j] instanceof Empty) { 
                    previous = singlesquare[i][j]; 
                    counter = 0; 
                } else { 
                    counter++; 
                } 
  
                if (counter >= getPlayerwin() - 1) { 
                    return true; 
                } 
  
            } 
            System.out.println(""); 
            previous = null; 
            counter = 0; 
        } 
  
        previous = null; 
        for (int j = singlesquare[0].length - 1; j >= 0; j--) { 
            int yau = 0; 
            for (int z = j; z < singlesquare[0].length; z++) { 
                if (!singlesquare[yau][z].equals(previous) 
                        || singlesquare[yau][z] instanceof Empty) { 
                    previous = singlesquare[yau][z]; 
                    counter = 0; 
                } else { 
                    counter++; 
                } 
  
                if (counter >= getPlayerwin() - 1) { 
                    return true; 
                } 
                yau++; 
            } 
            counter = 0; 
            previous = null; 
        } 
  
        previous = null; 
        for (int j = 0; j < singlesquare[0].length; j++) { 
            int yau = 0; 
            for (int z = j; z >= 0; z--) { 
                if (!singlesquare[yau][z].equals(previous) 
                        || singlesquare[yau][z] instanceof Empty) { 
                    previous = singlesquare[yau][z]; 
                    counter = 0; 
                } else { 
                    counter++; 
                } 
  
                if (counter >= getPlayerwin() - 1) { 
                    return true; 
                } 
  
                yau++; 
            } 
            counter = 0; 
            previous = null; 
        } 
        return false; 
    }

    public boolean isFull() { 
        for (int i = 0; i < singlesquare.length; i++) { 
            for (int j = 0; j < singlesquare[0].length; j++) { 
                if (singlesquare[i][j] instanceof Empty) { 
                    return false; 
                } 
            } 
        } 
        return true; 
    } 

    public void resizegame(int s) { 
        x = s; 
        y = s; 
  
        singlesquare = new Cell[x][y]; 
  
        int xss = l / x; 
        int yss = a / y; 
  
        for (int z = 0; z < y; z++) { 
            for (int i = 0; i < x; i++) { 
                singlesquare[z][i] = new Empty(xss * i, z * yss); 
            } 
        } 
        handler.sendMessage(Message.obtain(handler, 0)); 
    } 
      
    public int getPlayerwin() { 
        return playerwin; 
    } 
    
}

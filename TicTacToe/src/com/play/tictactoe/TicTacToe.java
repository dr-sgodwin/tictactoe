package com.play.tictactoe;

import android.os.Bundle;
import android.app.Activity;

public class TicTacToe extends Activity {
	private Game game1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        game1 = new Game(this);
        setContentView(game1);
    }
    
}

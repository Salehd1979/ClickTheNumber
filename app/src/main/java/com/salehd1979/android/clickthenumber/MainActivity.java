package com.salehd1979.android.clickthenumber;

import android.os.*;
import android.support.v7.app.*;
import android.view.*;
import android.widget.*;
import java.util.concurrent.*;
import android.content.*;
import android.util.*;

public class MainActivity extends AppCompatActivity
implements View.OnClickListener
{
	private static final String TAG="MainActivity";

	private TextView mNumberTextView;
	private Button mButton_01;
	private Button mButton_02;
	private Button mButton_03;
	private SeekBar mSpeedSeekBar;
	private TextView mSpeedText;
	private TextView mResultText;
	private int winCount;
	private int failCount;
	private Button mResetButton;
	private TextView mTxtTimer;
	MyCount counter = new MyCount(50000, 1000);

	int random = ThreadLocalRandom.current ( ).nextInt ( 0, 10 );

	// This code to call handler every 15 seconds and stop it when activity not visible

    Handler h = new Handler ( );
    int delay = 1 * 1000; //1 second=1000 milisecond, 15*1000=15seconds
    Runnable runnable;

    @Override
    protected void onResume ()
	{
		//start handler as activity become visible

        h.postDelayed ( runnable = new Runnable ( ) {
						   public void run ()
						   {
							   //do something
							   updateNumberText ( );
							   updateButtonText ( );
							   h.postDelayed ( runnable, delay );
						   }
					   }, delay );

        super.onResume ( );
    }

    @Override
    protected void onPause ()
	{
        h.removeCallbacks ( runnable ); //stop handler when activity not visible
        super.onPause ( );
    }

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.main );

		winCount = 0;
		failCount = 0;

		mNumberTextView = (TextView) findViewById ( R.id.txt_number );
		mButton_01 = (Button) findViewById ( R.id.button_01 );
		mButton_01.setOnClickListener ( this );

		mButton_02 = (Button) findViewById ( R.id.button_02 );
		mButton_02.setOnClickListener ( this );

		mButton_03 = (Button) findViewById ( R.id.button_03 );
		mButton_03.setOnClickListener ( this );

		mSpeedSeekBar = (SeekBar) findViewById ( R.id.speed );
		mSpeedSeekBar.setProgress ( 1000 );
		mSpeedSeekBar.setOnSeekBarChangeListener ( new SeekBar.OnSeekBarChangeListener ( ){
				@Override
				public void onProgressChanged (SeekBar seekBar, int progresValue, boolean fromUser)
				{
					delay = progresValue ;
					mSpeedText.setText ( "Speed is " + Integer.toString ( delay ) + " ms" );

					//Toast.makeText ( getApplicationContext ( ), "Changing seekbar's progress", Toast.LENGTH_SHORT ).show ( );
				}



				@Override

				public void onStartTrackingTouch (SeekBar seekBar)
				{

					//Toast.makeText ( getApplicationContext ( ), "Started tracking seekbar", Toast.LENGTH_SHORT ).show ( );

				}



				@Override

				public void onStopTrackingTouch (SeekBar seekBar)
				{

					//Toast.makeText ( getApplicationContext ( ), "Stopped tracking seekbar", Toast.LENGTH_SHORT ).show ( );

				}

			} );

		mSpeedText = (TextView) findViewById ( R.id.txt_speed );

		mResultText = (TextView) findViewById ( R.id.txt_result );

		mResetButton = (Button) findViewById ( R.id.reset );
		mResetButton.setOnClickListener ( new View.OnClickListener ( ){
				@Override
				public void onClick (View view)
				{
					resetGame ( );
				}
			} );
			
		mTxtTimer = (TextView) findViewById(R.id.txt_time);

		

        counter.start();
		//resetGame ( );

    }

	@Override
	public void onClick (View view)
	{
		String toastMsg;
		if ( checkForWin ( view ) )
		{
			toastMsg = "You win!";
			winCount++;
		}
		else
		{
			toastMsg = "You loose!";
			failCount++;
		}

		mResultText.setText ( " You win " + Integer.toString ( winCount ) + " Times, and loose " 
							 + Integer.toString ( failCount ) + " Times!" );

		Toast.makeText ( this, toastMsg, Toast.LENGTH_SHORT ).show ( );
	}

	private void updateNumberText ()
	{
		int random = ThreadLocalRandom.current ( ).nextInt ( 0, 10 );
		String number = Integer.toString ( random );
		mNumberTextView.setText ( number );
	}


	private void updateButtonText ()
	{
		int random1 = ThreadLocalRandom.current ( ).nextInt ( 0, 10 );
		int random2 = ThreadLocalRandom.current ( ).nextInt ( 0, 10 );
		int random3 = ThreadLocalRandom.current ( ).nextInt ( 0, 10 );

		mButton_01.setText ( Integer.toString ( random1 ) );
		mButton_02.setText ( Integer.toString ( random2 ) );
		mButton_03.setText ( Integer.toString ( random3 ) );


	}

	private boolean checkForWin (View view)
	{


		String buttonText = ((Button) view).getText ( ).toString ( );
		String textViewText = mNumberTextView.getText ( ).toString ( );
		if ( buttonText.equals ( textViewText ) )
		{
			return true;
		}

		return false;
	}


	private void resetGame ()
	{
		counter.cancel();
		h.removeCallbacks ( runnable ); //stop handler
		
		winCount = 0;
		failCount = 0;
		mResultText.setText ( " You win " + Integer.toString ( winCount ) + " Times, and loose " 
							 + Integer.toString ( failCount ) + " Times!" );
	

        counter.start();
		
		h.postDelayed ( runnable = new Runnable ( ) {
						   public void run ()
						   {
							   //do something
							   updateNumberText ( );
							   updateButtonText ( );
							   h.postDelayed ( runnable, delay );
						   }
					   }, delay );
	}

	@Override
	public boolean onCreateOptionsMenu (Menu menu)
	{
		MenuInflater inflater = getMenuInflater ( );
		inflater.inflate ( R.menu.about, menu );
		return true;

	}

	@Override
	public boolean onOptionsItemSelected (MenuItem item)
	{

		switch ( item.getItemId ( ) )
		{
			case R.id.menu_about:
				Intent intent = AboutActivity.newIntent ( this );
				startActivity ( intent );
				return true;
			default:

				return super.onOptionsItemSelected ( item );
		}

	}

	@Override
	protected void onSaveInstanceState (Bundle outState)
	{
		// TODO: Implement this method
		super.onSaveInstanceState ( outState );
		outState.putInt ( "WinCount", winCount );
		outState.putInt ( "FailCount", failCount );
		outState.putInt ( "Speed", delay );
		Log.i ( TAG, "Saved Instance Status" );
		Log.i ( TAG, "Win is " + Integer.toString ( winCount ) );
		Log.i ( TAG, "Loose is " + Integer.toString ( failCount ) );
		Log.i ( TAG, "Speed " + Integer.toString ( delay ) );
	}

	@Override
	protected void onRestoreInstanceState (Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onRestoreInstanceState ( savedInstanceState );

		if ( savedInstanceState != null )
		{
			Log.i ( TAG, "savedInstanceState is not null" );
			winCount = savedInstanceState.getInt ( "WinCount", winCount );
			failCount =  savedInstanceState.getInt ( "FailCount", failCount );
			delay = savedInstanceState.getInt ( "Speed", delay );
			Log.i ( TAG, "Win is " + Integer.toString ( winCount ) );
			Log.i ( TAG, "Loose is " + Integer.toString ( failCount ) );
			Log.i ( TAG, "Speed " + Integer.toString ( delay ) );

		}
	}


	
	//countdowntimer is an abstract class, so extend it and fill in methods
    public class MyCount extends CountDownTimer{

        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            mTxtTimer.setText("done!");
			h.removeCallbacks ( runnable ); //stop handler
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mTxtTimer.setText("Left: " + millisUntilFinished/1000);

        }

    }



}

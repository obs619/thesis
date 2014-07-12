package com.cardgame.activities;

//import android.R;
import com.srpol.chordchat.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * @author Sharmaine
 * 
 *		This is the game screen when the personal screen is
 *		selected.
 */

public class PlayPersonalActivity extends Activity {
	
	private ListView listCards;
	private TextView txtError;
	private Button btnPlay;
	private Button btnPass;
	private LinearLayout layoutPassTo;
	private Spinner spinRecipient;
	private Button btnDone;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play_personal);
		
		listCards = (ListView) findViewById(R.id.listPersonalCards);
		txtError = (TextView) findViewById(R.id.txtPersonalError);
		btnPlay = (Button) findViewById(R.id.btnPersonalPlay);
		btnPass = (Button) findViewById(R.id.btnPersonalPass);
		layoutPassTo = (LinearLayout) findViewById(R.id.layoutPersonalPassTo);
		spinRecipient = (Spinner) findViewById(R.id.spinPersonalRecipient);
		btnDone = (Button) findViewById(R.id.btnPersonalDone);
		
		// TODO send and receive join game message
		
		// TODO receive cards
		// ArrayList<Card> lstCards = <received cards>
		
		// TODO populate listview with cards
		//try {
		//	ArrayAdapter<Card> cardAdapter = new ArrayAdapter<Card>(this, R.layout.list_item, lstCards);
		//	lstCards.setAdapter(cardAdapter);
		//}
		//catch(Exception ex) {
		//	ArrayAdapter<String> cardAdapter = new ArrayAdapter<String>(this, R.layout.list_item, new String[] {"No cards received."});
		//	lstCards.setAdapter(cardAdapter);
		//}
		
		// TODO receive list of players
		// TODO populate spinner with players
	}
	
	public void clickPlay(View v) {
		// TODO check if a card is selected, if true
		// TODO send the selected card
		
		// TODO if did NOT receive a reply
		txtError.setText("ERROR: ");
		txtError.setVisibility(View.VISIBLE);
		
		// TODO if did NOT select a card
		txtError.setText("Please select a card");
		txtError.setVisibility(View.VISIBLE);
	}
	
	public void clickPass(View v) {
		// TODO check if a card is selected, if true
		txtError.setVisibility(View.GONE);
		layoutPassTo.setVisibility(View.VISIBLE);
		btnDone.setVisibility(View.VISIBLE);
		
		// TODO else
		txtError.setText("Please select a card");
		txtError.setVisibility(View.VISIBLE);
	}
	
	public void clickDone(View v) {
		// TODO check if a recipient is selected
		// TODO send card to selected recipient
		
		// TODO check if a reply was received, if true
		txtError.setVisibility(View.GONE);
		layoutPassTo.setVisibility(View.GONE);
		btnDone.setVisibility(View.GONE);
		
		// TODO else
		txtError.setText("ERROR: ");
		txtError.setVisibility(View.VISIBLE);
	}
	
}

package com.mycompany.vestibularapp;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.mycompany.vestibularapp.Adapter.*;
import java.util.*;
import org.json.*;

public class QuestionsActivity extends Activity {

	private LinearLayout enunciadoContent;
	private TextView enunciadoText, authorText;
	private ListView alternativasLista;
	private Button questionsAnsweredViewButton;

	private Gui gui;

	private AlternativaAdapter alternativaAdapter;
	private JsonData jsonData;


	private JSONObject currentQuestionObj;
	private String theme = null, level = null;
	private int currentQuestion = 0;
	private String alternativaSelected = null;


//	the max count of question in the app (this is used in the free version of app)
	private static final int MAX_QUESTIONS = 5;
//	delay in seconds to the next question
	private int DELAY_NEXT_QUESTION = 1;
//	the count of questions
	private int QUESTIONS_COUNT = 0;

	private ArrayList<Integer> questionsAlreadyAnswered = new ArrayList<Integer>(); 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.questions);
		super.onCreate(savedInstanceState);


		gui = new Gui(QuestionsActivity.this);
		jsonData = new JsonData();

//		get the bundle of theme and level
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();


		theme = bundle.getString("theme");
		level = bundle.getString("level");


		try {
			currentQuestionObj = getCurrentQuestionObj();
		} catch(JSONException e) {}		


		Button managerBack = (Button) findViewById(R.id.questionsBack);
		managerBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View p1) {
				finish();
			}
		});
		
		questionsAnsweredViewButton = (Button) findViewById(R.id.questionsAnswered);
		questionsAnsweredViewButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View p1) {
				startActivity(new Intent(QuestionsActivity.this, QuestionsAnsweredActivity.class));
			}
		});

		//enunciado
		enunciadoContent = (LinearLayout) findViewById(R.id.questionsEnunciadoContent);
		enunciadoContent.setPadding(gui.getScreenWidth() / 40, gui.getScreenHeight() / 40, gui.getScreenWidth() / 40, gui.getScreenHeight() / 40);
		int enunciadoContentWidth = gui.getScreenWidth() / 2 + gui.getScreenWidth() / 3;

		gui.setLayoutParams((View) enunciadoContent, enunciadoContentWidth, LinearLayout.LayoutParams.WRAP_CONTENT, (gui.getScreenWidth() - enunciadoContentWidth) / 2, gui.getScreenHeight() / 30);

		enunciadoText = (TextView) findViewById(R.id.questionsEnunciadoText);
		authorText = (TextView) findViewById(R.id.questionsAuthorText);


		//init the list view
		alternativasLista = (ListView) findViewById(R.id.listaAlternativas);
		alternativasLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long p4) {
				Alternativas alternativa = alternativaAdapter.list.get(position);

//					remove all selected items
				for(int i = 0; i < alternativaAdapter.getCount(); i++) {
					if(alternativaAdapter.list.get(i).isSelected() == true) {
						alternativaAdapter.list.get(i).setSelected(false);
					}
				}

				alternativa.setSelected(true);
				alternativaSelected = Integer.toString(position);

				alternativaAdapter.notifyDataSetChanged();
			}
		});
		gui.setLayoutParams((View) alternativasLista, enunciadoContentWidth, LinearLayout.LayoutParams.WRAP_CONTENT, (gui.getScreenWidth() - enunciadoContentWidth) / 2, gui.getScreenHeight() / 10);

		//random the question
		randomNextQuestion();
		

		Button nextButton = (Button) findViewById(R.id.questionsButtonNext);
		nextButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View p1) {
//					verify if the selected is the correct and if isn't check the wrong with blue and the correct with green
				Alternativas alternativa = alternativaAdapter.list.get(Integer.parseInt(alternativaSelected));

				if(alternativa.isCorrect() == true) {
					alternativa.setClickedCorrect(true);
					alternativaAdapter.notifyDataSetChanged();
				} else {
					alternativa.setClickedCorrect(false);
					alternativaAdapter.notifyDataSetChanged();
				}
				alternativasLista.setEnabled(false);

//					put the current question to the array that was answered
				questionsAlreadyAnswered.add(currentQuestion);


//					delay to call the next question and random it
				p1.postDelayed(new Runnable() {
					@Override
					public void run() {
						randomNextQuestion();
					}
				}, DELAY_NEXT_QUESTION * 1000);
			}
		});
	}


//	return the current question obj. don't forget to set the currentQuestion variable value before to call this method
	private JSONObject getCurrentQuestionObj() throws JSONException {
		JSONObject obj;

		obj = new JSONObject(jsonData.loadAppJsonData());
//		theme
		JSONArray themeArray = obj.getJSONArray("theme");
		JSONObject themeObj = themeArray.getJSONObject(Integer.parseInt(theme));

//		nivel
		JSONArray nivelArray = themeObj.getJSONArray("nivel");
		JSONObject currentNivelObj = nivelArray.getJSONObject(Integer.parseInt(level));

//		questions
		JSONArray questionArray = currentNivelObj.getJSONArray("questions");
		JSONObject questionObj = questionArray.getJSONObject(currentQuestion);

//		set the count of question on the current theme and level
		QUESTIONS_COUNT = questionArray.length();

		return questionObj;
	}

//	update the ui with the current data
	private void updateUI() {
		try {
//			set the enunciado
			enunciadoText.setText(currentQuestionObj.getString("enunciado"));
			
//			set the author
			authorText.setText(currentQuestionObj.getString("author"));
			

//			alternativas
			ArrayList<Alternativas> alternativasList = new ArrayList<Alternativas>();

			for(int i = 0; i < 5; i++) {
				try {
					Alternativas alternativa = new Alternativas();
					JSONArray currentAlternativaArray = currentQuestionObj.getJSONArray("alternativa").getJSONArray(i);

					if(i == 0) {
						alternativa.setAlternativaLetter("A)");
					} else if(i == 1) {
						alternativa.setAlternativaLetter("B)");
					} else if(i == 2) {
						alternativa.setAlternativaLetter("C)");
					} else if(i == 3) {
						alternativa.setAlternativaLetter("D)");
					} else if(i == 4) {
						alternativa.setAlternativaLetter("E)");
					}

					alternativa.setAlternativaText(currentAlternativaArray.getString(0));
					alternativa.setCorrect(currentAlternativaArray.getBoolean(1));

					alternativasList.add(alternativa);

				} catch(JSONException e) {}
			}
			alternativaAdapter = new AlternativaAdapter(this, alternativasList);
			alternativasLista.setAdapter(alternativaAdapter);
		} catch(JSONException e) {}
	}


//	return if the question already was answered
	private boolean isQuestionAnswered(int index) {
		for(int b = 0; b < questionsAlreadyAnswered.size(); b++) {
			if(questionsAlreadyAnswered.get(b) == index)
				return true;
		}

		return false;
	}

	private void randomNextQuestion() {
		try {
			if(QUESTIONS_COUNT != 0) {
//				all questions answered
				if(questionsAlreadyAnswered.size() < QUESTIONS_COUNT) {
//					the limit of questions maked
					if(questionsAlreadyAnswered.size() < MAX_QUESTIONS) {
						int nextQuestion = (int) Math.floor(Math.random() * QUESTIONS_COUNT);								
						int lastQuestion = currentQuestion;
						currentQuestion = nextQuestion;

						while(nextQuestion == lastQuestion || isQuestionAnswered(nextQuestion) == true) {
							nextQuestion = (int) Math.floor(Math.random() * QUESTIONS_COUNT);
						}
						currentQuestion = nextQuestion;

						currentQuestionObj = getCurrentQuestionObj(); 

						alternativasLista.setEnabled(true);
						updateUI();
					} else {
						//when is on the limit of questions (version free)
						Toast.makeText(QuestionsActivity.this, "Buy the pro version to use the full app", Toast.LENGTH_SHORT).show();
					}
				} else {
//					when all quesitons was answered
					Toast.makeText(QuestionsActivity.this, "all questions answered", Toast.LENGTH_SHORT).show();
				}
			}
		} catch(JSONException e) {}
	}
}

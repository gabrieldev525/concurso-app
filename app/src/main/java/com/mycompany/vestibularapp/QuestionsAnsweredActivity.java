package com.mycompany.vestibularapp;

import android.app.*;
import android.os.*;
import android.widget.*;
import java.util.*;
import android.view.*;
import com.mycompany.vestibularapp.Adapter.*;

public class QuestionsAnsweredActivity extends Activity {

	private ArrayList<QuestionsAnswered> questionsList = new ArrayList<QuestionsAnswered>();
	private ListView questionsLista;
	private AnsweredAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.questions_answered);
		
		Button buttonBack = (Button) findViewById(R.id.question_answeredBack);
		buttonBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View p1) {
				finish();
			}
		});
		
		for(int i = 0; i < 5; i++) {
			QuestionsAnswered questions = new QuestionsAnswered();
			questions.setEnunciado("This is the question");
			questionsList.add(questions);
		}
		
		questionsLista = (ListView) findViewById(R.id.questions_answeredListView);
		questionsLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
				// TODO: Implement this method
			}
		});
		adapter = new AnsweredAdapter(this, questionsList);
		questionsLista.setAdapter(adapter);
		
		super.onCreate(savedInstanceState);
	}
		
}

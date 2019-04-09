package com.mycompany.vestibularapp;
import android.app.*;
import android.os.*;
import android.widget.*;
import org.json.*;
import android.view.View.*;
import android.view.*;
import android.content.*;

public class QuestionEditActivity extends Activity
{

	private Gui gui;
	private CheckBox alternativasCheck[];
	private EditText alternativas[] = new EditText[5];
	private EditText enunciado, author;
	private String theme, level;
	
	private JsonData jsonData;
	
	private Bundle bundle;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setContentView(R.layout.question_manager);
		super.onCreate(savedInstanceState);
		
		jsonData = new JsonData();
		
		Intent intent = getIntent();
		bundle = intent.getExtras();
		
		theme = bundle.getString("theme");
		level = bundle.getString("nivel");
		
//		instance the gui class
		gui = new Gui(QuestionEditActivity.this);
		
		Button managerBack = (Button) findViewById(R.id.question_managerBack);
		managerBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View p1)
			{
				finish();
			}
		});
		
		enunciado = (EditText) findViewById(R.id.question_managerEnunciado);
		enunciado.setText(bundle.getString("enunciado"));
		
		author = (EditText) findViewById(R.id.question_managerAuthor);
		author.setText(bundle.getString("author"));
		
//		alternativas
		int alternativasId[] = {
			R.id.question_managerAlternativa1EditText, R.id.question_managerAlternativa2EditText,
			R.id.question_managerAlternativa3EditText, R.id.question_managerAlternativa4EditText,
			R.id.question_managerAlternativa5EditText};
			
		for(int i = 0; i < alternativasId.length; i++) {
			alternativas[i] = (EditText) findViewById(alternativasId[i]);
			
			alternativas[i].setText(bundle.getString("alternativa" + (i + 1)));
			gui.setLayoutParams(alternativas[i], gui.getScreenWidth() - gui.getScreenWidth() / 8, LinearLayout.LayoutParams.WRAP_CONTENT, 0, 0);
			alternativas[i].setPadding(0, gui.getScreenHeight() / 30, 0, gui.getScreenHeight() / 30);
		}
		
//		check box
		alternativasCheck = new CheckBox[5];
		final int alternativasCheckId[] = {
			R.id.question_managerAlternativa1CheckBox, R.id.question_managerAlternativa2CheckBox,
			R.id.question_managerAlternativa3CheckBox, R.id.question_managerAlternativa4CheckBox,
			R.id.question_managerAlternativa5CheckBox};
		
		for(int i = 0; i < alternativasCheckId.length; i++) {
			alternativasCheck[i] = (CheckBox) findViewById(alternativasCheckId[i]);
			alternativasCheck[i].setId(i);
			alternativasCheck[i].setChecked(bundle.getBoolean("alternativaChecked" + (i + 1)));
			alternativasCheck[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton p1, boolean p2)
					{
						if(p2 == true) {
							for(int a = 0; a < alternativasCheckId.length; a++) {
								if(a != p1.getId()) {
									if(alternativasCheck[a].isChecked()) {
										alternativasCheck[a].setChecked(false);
									}
								}
							}
						}
					}
			});
		}
		
		
		Button buttonOk = (Button) findViewById(R.id.question_managerButtonOk);
		buttonOk.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View p1)
				{
//					if the enunciado is dont empty
					if(!enunciado.getText().toString().trim().isEmpty() && !author.getText().toString().trim().isEmpty()) {
						boolean alternativasEmpty = false;
						
//						this verify all less the last
						for(int i = 0; i < alternativas.length - 1; i++) {
							if(alternativas[i].getText().toString().trim().isEmpty()) {
								alternativasEmpty = true;
								break;
							}
						}
						
//						if the alternativas dont is empty
						if(!alternativasEmpty) {
							boolean checked = false;
							
							for(int a = 0; a < alternativasCheck.length; a++) {
								if(alternativasCheck[a].isChecked()) {
									checked = true;
								}
							}
							
//							if anyone alternativa is checked
							if(checked) {
								Object alternativasValues[][] = {
									{alternativas[0].getText().toString(), alternativasCheck[0].isChecked()},
									{alternativas[1].getText().toString(), alternativasCheck[1].isChecked()},
									{alternativas[2].getText().toString(), alternativasCheck[2].isChecked()},
									{alternativas[3].getText().toString(), alternativasCheck[3].isChecked()},
									{alternativas[4].getText().toString(), alternativasCheck[4].isChecked()}
								};
								
//								put the values on json
								editValuesOnJson(bundle.getInt("id"), enunciado.getText().toString(), author.getText().toString(), alternativasValues, "");
								
								Toast.makeText(QuestionEditActivity.this, "Editado com sucesso! Atualize a lista", Toast.LENGTH_SHORT).show();
								finish();
							}
						}
					}
				}
		});
	}
	
	private void editValuesOnJson(int id, String enunciado, String author, Object alter[][], String imagem) {
		try {
			JSONObject obj;
//			the json object dont exists
			if(jsonData.loadAppJsonData().trim().isEmpty()) 
				obj = new JSONObject();
			else 
				obj = new JSONObject(jsonData.loadAppJsonData());

//			if the questao array dont exists
			if(!obj.has("theme")) {
				JSONArray array = new JSONArray();
				obj.put("theme", array);
			}


			JSONArray themeArray = obj.getJSONArray("theme");

//									init the variables
			JSONObject themeObj = null;
			JSONArray nivelArray = null;
			JSONArray questionArray = null;



			if(theme != null) {
				themeObj = themeArray.getJSONObject(Integer.parseInt(theme));



				if(!themeObj.has("nivel")) {
					JSONArray array = new JSONArray();
					themeObj.put("nivel", array);
				}

				//			nivel
				nivelArray = themeObj.getJSONArray("nivel");
				JSONObject currentNivelObj = null;
				if(level != null) {
					currentNivelObj = nivelArray.getJSONObject(Integer.parseInt(level));

					//			questions
					if(!currentNivelObj.has("questions")) {
						JSONArray array = new JSONArray();
						currentNivelObj.put("questions", array);
					}

					questionArray = currentNivelObj.getJSONArray("questions");

				}



			}




//									remake all file

//									delete question
			if(theme != null && level != null) {
				JSONObject newObj = new JSONObject();
				JSONArray newThemeArray = new JSONArray();
				for(int i = 0; i < themeArray.length(); i++) {
					if(i == Integer.parseInt(theme)) {
//											new theme obj
						JSONObject newThemeObj = new JSONObject();
						newThemeObj.put("name", themeArray.getJSONObject(i).getString("name"));


//											new nivel array
						JSONArray newNivelArray = new JSONArray();

						for(int a = 0; a < nivelArray.length(); a++) {
							if(a == Integer.parseInt(level)) {
								JSONObject newNivelObj = new JSONObject();
								JSONArray newQuestionArray = new JSONArray();

								//questions
								for(int b = 0; b < questionArray.length(); b++) {
									if(b != id) {
										newQuestionArray.put(questionArray.getJSONObject(b));
									} else {
										//enunciado
										JSONObject questoesObj = new JSONObject();
										questoesObj.put("enunciado", enunciado); 
										
//										author
										questoesObj.put("author", author);

										//alternativas
										JSONArray alternativasArray = new JSONArray();
										for(int d = 0; d < 5; d++) {
											JSONArray alternativa = new JSONArray();
											alternativa.put(alter[d][0]);
											alternativa.put(alter[d][1]);
											alternativasArray.put(alternativa);
										}
										questoesObj.put("alternativa", alternativasArray);

										//			image
										questoesObj.put("imagem", imagem);
										
										
										
										newQuestionArray.put(questoesObj);
										
									}
								}
								
								newNivelObj.put("questions", newQuestionArray);
								newNivelArray.put(newNivelObj);

							} else {
								newNivelArray.put(nivelArray.getJSONObject(a));
							}
						}

						newThemeObj.put("nivel", newNivelArray);
						newThemeArray.put(newThemeObj);


					} else {
						newThemeArray.put(themeArray.getJSONObject(i));
					}
				}
				newObj.put("theme", newThemeArray);


				jsonData.saveAppJsonData(newObj);
				
			}
		} catch(JSONException e) {
			Toast.makeText(QuestionEditActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
}

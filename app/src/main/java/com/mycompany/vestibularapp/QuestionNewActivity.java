package com.mycompany.vestibularapp;
import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import org.json.*;

public class QuestionNewActivity extends Activity
{

	private Gui gui;
	private CheckBox alternativasCheck[];
	private EditText alternativas[] = new EditText[5];
	private EditText enunciado, author;
	private Spinner spinner;
	
	private JsonData jsonData;
	
	private String theme = null, level = null;
	
	private Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setContentView(R.layout.question_manager);
		super.onCreate(savedInstanceState);
		
		intent = getIntent();
		Bundle bundle = intent.getExtras();
		
		theme = bundle.getString("theme");
		level = bundle.getString("level");
		
		jsonData = new JsonData();
		
//		instance the gui class
		gui = new Gui(QuestionNewActivity.this);
		
		Button managerBack = (Button) findViewById(R.id.question_managerBack);
		managerBack.setOnClickListener(new OnClickListener() {
		 	@Override
			public void onClick(View p1)
			{
				finish();
			}
		});
		
		enunciado = (EditText) findViewById(R.id.question_managerEnunciado);
		author = (EditText) findViewById(R.id.question_managerAuthor);
		
//		alternativas
		int alternativasId[] = {
			R.id.question_managerAlternativa1EditText, R.id.question_managerAlternativa2EditText,
			R.id.question_managerAlternativa3EditText, R.id.question_managerAlternativa4EditText,
			R.id.question_managerAlternativa5EditText};
			
		for(int i = 0; i < alternativasId.length; i++) {
			alternativas[i] = (EditText) findViewById(alternativasId[i]);
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
//					if the enunciado and author is dont empty
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
								putValuesOnJson(enunciado.getText().toString(), author.getText().toString(), alternativasValues, "");
								
								Toast.makeText(QuestionNewActivity.this, "Criado com sucesso", Toast.LENGTH_SHORT).show();
								Intent intent = new Intent();
								setResult(RESULT_OK, intent);
								finish();
							}
						}
					}
				}
		});
	}
	
	
	
	private void putValuesOnJson(String enunciado, String author,  Object alter[][], String imagem) {
		try {
			JSONObject obj;
//			if the json object dont exists
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
			JSONObject themeObj = themeArray.getJSONObject(Integer.parseInt(theme));


//			nivel
			if(!themeObj.has("nivel")) {
				JSONArray array = new JSONArray();
				themeObj.put("nivel", array);
			}

			JSONArray nivelArray = themeObj.getJSONArray("nivel");
			JSONObject currentNivelObj = nivelArray.getJSONObject(Integer.parseInt(level));

//			questions
			if(!currentNivelObj.has("questions")) {
				JSONArray array = new JSONArray();
				currentNivelObj.put("questions", array);
			}

			JSONArray questionArray = currentNivelObj.getJSONArray("questions");

//			enunciado
			JSONObject questoesObj = new JSONObject();
			questoesObj.put("enunciado", enunciado); 
			
//			author
			questoesObj.put("author", author);
			
//			alternativa type
			questoesObj.put("type", "check");

//			alternativas
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
			
			questionArray.put(questoesObj);

//			save
			jsonData.saveAppJsonData(obj);

		} catch(JSONException e) {
			Toast.makeText(QuestionNewActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
}

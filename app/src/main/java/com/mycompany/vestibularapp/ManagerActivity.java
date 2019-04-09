package com.mycompany.vestibularapp;
import android.app.*;
import android.os.*;
import android.util.*;
import android.widget.*;
import com.mycompany.vestibularapp.Adapter.*;
import java.io.*;
import java.util.*;
import org.json.*;
import android.view.View.*;
import android.view.*;
import android.content.*;

public class ManagerActivity extends Activity
{

	private JsonData jsonData;
	private ManagerAdapter managerAdapter;
	
	private static final int UPDATE = 1;
	private ArrayList<Manager> managerList;
	private ListView managerLista;
	private TextView sessionText;
	
		
	private String theme = null, level = null, question = null;
	
	private EditText themeName;
	
	private Gui gui;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setContentView(R.layout.manager);
		super.onCreate(savedInstanceState);
		
		jsonData = new JsonData();
		gui = new Gui(ManagerActivity.this);
		
//		session text
		sessionText = (TextView) findViewById(R.id.managerQuestionSession);
		
//		topbar
		LinearLayout topBar = (LinearLayout) findViewById(R.id.managerTopBar);
		topBar.setPadding(0, gui.getScreenHeight() / 70, 0, gui.getScreenHeight() / 70);
		
		try {
			
			if(!new File(Environment.getExternalStorageDirectory() + "/" + jsonData.appJsonFileName).exists()) {
				jsonData.saveAppJsonData(new JSONObject());
			}			
			
			managerLista = (ListView) findViewById(R.id.managerQuestionList);
			managerLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> adapter, View view, int position, long p4)
					{
						if(theme == null) {
							sessionText.setText("Nivel");
							theme = Integer.toString(position);
							try {
								update();
							} catch (JSONException e){}
						} else if(theme != null && level == null) {
							sessionText.setText("Pergunta");
							level = Integer.toString(position);
							try {
								update();
							} catch (JSONException e){}
						}
					}
			});
			managerList = new ArrayList<Manager>();
			
			getData();

			managerAdapter = new ManagerAdapter(ManagerActivity.this, managerList);
			managerLista.setAdapter(managerAdapter);
			
			
		} catch (JSONException e) {}

		
		Button newQuestion = (Button) findViewById(R.id.managerNew);
		gui.setLayoutParams(newQuestion, gui.getScreenWidth() / 8, gui.getScreenHeight() / 14, gui.getScreenWidth() / 30, 0);
		newQuestion.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View p1)
				{
					
					
					
					
					
					if(theme == null) {
						//create theme
						AlertDialog.Builder dlgCreateTheme = new AlertDialog.Builder(ManagerActivity.this);
						dlgCreateTheme.setTitle("Criar tema");
						
						themeName = new EditText(ManagerActivity.this);
						themeName.setHint("Nome do tema");
						dlgCreateTheme.setCancelable(false);
						dlgCreateTheme.setView(themeName);
						
						dlgCreateTheme.setPositiveButton("ok", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface p1, int p2)
								{
									if(themeName.getText().toString().trim().isEmpty() == false) {
									try {
										JSONObject obj;
										if(jsonData.loadAppJsonData().trim().isEmpty())
											obj = new JSONObject();
										else 
											obj = new JSONObject(jsonData.loadAppJsonData());

										//if the theme array dont exists
										if(!obj.has("theme")) {
											JSONArray array = new JSONArray();
											obj.put("theme", array);
										}
										
										JSONArray themeArray = obj.getJSONArray("theme");
										JSONObject themeObj = new JSONObject();
										themeObj.put("name", themeName.getText().toString());
										
										themeArray.put(themeObj);
										
										jsonData.saveAppJsonData(obj);
										
										update();
									} catch (JSONException e) {}
									}
								}
						});
						dlgCreateTheme.setNegativeButton("cancelar", null);
						dlgCreateTheme.show();
						
						
						
						
						
						
						
						
						
					} else if(theme != null && level == null) {
						//add level
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
							JSONObject themeObj = themeArray.getJSONObject(Integer.parseInt(theme));


//			nivel
							if(!themeObj.has("nivel")) {
								JSONArray array = new JSONArray();
								themeObj.put("nivel", array);
							}

							JSONArray nivelArray = themeObj.getJSONArray("nivel");
							
							JSONObject newNivelObj = new JSONObject();
							nivelArray.put(newNivelObj);
							
							jsonData.saveAppJsonData(obj);
							
							update();
							
						} catch(JSONException e) {}
						
						
						
						
						
						
						
						
					} else if(theme != null && level != null && question == null) {
						//create question
						Intent i = new Intent(ManagerActivity.this, QuestionNewActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("theme", theme);
						bundle.putString("level", level);
						i.putExtras(bundle);
						startActivityForResult(i, UPDATE);
					}
				}
		});
		
		Button updateQuestion = (Button) findViewById(R.id.managerUpdate);
		gui.setLayoutParams(updateQuestion, gui.getScreenWidth() / 8, gui.getScreenHeight() / 14, gui.getScreenWidth() / 30, 0);
		updateQuestion.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View p1)
				{
					try {
						managerList = new ArrayList<Manager>();
						getData();
						managerAdapter = new ManagerAdapter(ManagerActivity.this, managerList);
						managerLista.setAdapter(managerAdapter);
						Toast.makeText(ManagerActivity.this, "atualizado", Toast.LENGTH_SHORT).show();
					} catch(JSONException e) {}
				}
		});
		
		Button managerBack = (Button) findViewById(R.id.managerBack);
		gui.setLayoutParams(managerBack, gui.getScreenWidth() / 8, gui.getScreenHeight() / 14, 0, 0);
		managerBack.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View p1)
				{
					finish();
				}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
//		if(requestCode == UPDATE) {
			if(resultCode == RESULT_OK) {
				try {
					managerList = new ArrayList<Manager>();
					getData();
					managerAdapter = new ManagerAdapter(ManagerActivity.this, managerList);
					managerLista.setAdapter(managerAdapter);
				} catch (JSONException e){}
			}
//		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private void update() throws JSONException {
		managerList = new ArrayList<Manager>();
		getData();
		managerAdapter = new ManagerAdapter(ManagerActivity.this, managerList);
		managerLista.setAdapter(managerAdapter);
	}
	
	private void getData() throws JSONException {
		JSONObject obj;
		
		
		
		
		
		if(theme == null) { //tema
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
			
			if(obj.has("theme")) {
				JSONArray themeArray = obj.getJSONArray("theme");
				for(int i = 0; i < themeArray.length(); i++) {
					JSONObject themeObj = themeArray.getJSONObject(i);
					
					Manager manager = new Manager();
					manager.setSession("theme");
					manager.setName(themeObj.getString("name"));
					manager.setId(i);
					manager.setTheme(theme);
					manager.setNivel(level);
					managerList.add(manager);
				}
			}
			
			jsonData.saveAppJsonData(obj);
			
			
			
			
			
		} else if(theme != null && level == null) {  //nivel
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
			JSONObject themeObj = themeArray.getJSONObject(Integer.parseInt(theme));
			
			
//			nivel
			if(!themeObj.has("nivel")) {
				JSONArray array = new JSONArray();
				themeObj.put("nivel", array);
			}
			
			JSONArray nivelArray = themeObj.getJSONArray("nivel");
			
			for(int i = 0; i < nivelArray.length(); i++) {
				JSONObject nivelObj = nivelArray.getJSONObject(i);
				
				Manager manager = new Manager();
				manager.setSession("nivel");
				manager.setName("Nivel " + (i + 1));
				manager.setId(i);
				manager.setTheme(theme);
				manager.setNivel(level);
				managerList.add(manager);
			}
			

			jsonData.saveAppJsonData(obj);
		}
		
		
		
		else if(theme != null && level != null && question == null) {
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
			
			for(int i = 0; i < questionArray.length(); i++) {
				JSONObject questionObj = questionArray.getJSONObject(i);

				Manager manager = new Manager();
				manager.setSession("question");
				manager.setEnunciado(questionObj.getString("enunciado"));
				manager.setAuthor(questionObj.getString("author"));
				manager.setNivel(level);
				manager.setTheme(theme);
				manager.setId(i);
				Object alternativas[][] = {
					{questionObj.getJSONArray("alternativa").getJSONArray(0).getString(0), questionObj.getJSONArray("alternativa").getJSONArray(0).getBoolean(1)},
					{questionObj.getJSONArray("alternativa").getJSONArray(1).getString(0), questionObj.getJSONArray("alternativa").getJSONArray(1).getBoolean(1)},
					{questionObj.getJSONArray("alternativa").getJSONArray(2).getString(0), questionObj.getJSONArray("alternativa").getJSONArray(2).getBoolean(1)},
					{questionObj.getJSONArray("alternativa").getJSONArray(3).getString(0), questionObj.getJSONArray("alternativa").getJSONArray(3).getBoolean(1)},
					{questionObj.getJSONArray("alternativa").getJSONArray(4).getString(0), questionObj.getJSONArray("alternativa").getJSONArray(4).getBoolean(1)}
				};
				manager.setAlternativas(alternativas);
				managerList.add(manager);
			}


			jsonData.saveAppJsonData(obj);
			
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			if(theme == null && level == null && question == null) {
				finish();
			} else if(theme != null && level == null && question == null) {
				sessionText.setText("Tema");
				theme = null;
				try {
					update();
				} catch (JSONException e) {}
			} else if(theme != null && level != null && question == null) {
				sessionText.setText("Nivel");
				level = null;
				try {
					update();
				} catch (JSONException e) {}
			} else if(theme != null && level != null && question != null) {
				question = null;
				try {
					update();
				} catch (JSONException e) {}
			}
		}
		return false;
	}
	
	
	
	
}

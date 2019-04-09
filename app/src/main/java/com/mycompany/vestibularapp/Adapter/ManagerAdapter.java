package com.mycompany.vestibularapp.Adapter;

import android.app.*;
import android.content.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.mycompany.vestibularapp.*;
import java.util.*;
import org.json.*;

public class ManagerAdapter extends BaseAdapter
{
	private Context context;
	private ArrayList<Manager> list;
	private Activity ctx;

	private Gui gui;
	private JsonData jsonData;

	public ManagerAdapter(Context context, ArrayList<Manager> listManager) {
		this.context = context;
		this.list = listManager;
		this.ctx = ctx;

		gui = new Gui((Activity) context);
		
		jsonData = new JsonData();
	}

	@Override
	public int getCount()
	{
		return list.size();
	}

	@Override
	public Object getItem(int p1)
	{
		return list.get(p1);
	}

	@Override
	public long getItemId(int p1)
	{
		return p1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		final Manager manager = list.get(position);
		View layout;
		LayoutInflater inflater;
		
		if(convertView == null){
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			layout = inflater.inflate(R.layout.list_manager_questions, null);
		}
		else{
			layout = convertView;
		}

		layout.setPadding(gui.getScreenWidth() / 50, gui.getScreenHeight() / 50, gui.getScreenWidth() / 50, gui.getScreenHeight() / 50);
		TextView enunciadoText = (TextView) layout.findViewById(R.id.listManagerQuestion);
//		TextView nivelText = (TextView) layout.findViewById(R.id.listManagerQuestionNivel);
		Button editQuestion = (Button) layout.findViewById(R.id.list_manager_questionsButtonEdit);
		Button deleteQuestion = (Button) layout.findViewById(R.id.list_manager_questionsButtonDelete);
		LinearLayout layout2 = (LinearLayout) layout.findViewById(R.id.list_manager_questionsLinearLayout);
		
		
//		manager
		gui.setLayoutParams((View) enunciadoText, gui.getScreenWidth() / 2 + gui.getScreenWidth() / 12, LinearLayout.LayoutParams.WRAP_CONTENT, 0, 0);
		if(manager.getSession().equals("theme") || manager.getSession().equals("nivel")) {
			enunciadoText.setText(manager.getName());
			
			if(manager.getSession().equals("nivel")) {
				editQuestion.setBackgroundDrawable(null);
				editQuestion.setClickable(false);
			}
//			nivelText.setText("");
		} else if(manager.getSession().equals("question")) {
			enunciadoText.setText(manager.getEnunciado());
		}
		
		
		
		
		gui.setLayoutParams(editQuestion, gui.getScreenWidth() / 6, gui.getScreenHeight() / 12, 0, 0);
		editQuestion.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View p1)
				{
					if(manager.getSession().equals("question")) {
					Intent intent = new Intent(context, QuestionEditActivity.class);
					intent.putExtra("enunciado", manager.getEnunciado());
					intent.putExtra("author", manager.getAuthor());
					intent.putExtra("nivel", manager.getNivel());
					intent.putExtra("theme", manager.getTheme());
					intent.putExtra("imagem", manager.getImagem());
					
					for(int i = 0; i < 5; i++) {
						intent.putExtra("alternativa" + (i + 1), (String) manager.getAlternativas()[i][0]);
						intent.putExtra("alternativaChecked" + (i + 1), (boolean) manager.getAlternativas()[i][1]);
					}
					intent.putExtra("id", manager.getId());
					
					context.startActivity(intent);	
					
					
					
					
					
					
					
					
					} else if(manager.getSession().equals("theme")) {
						
						
						AlertDialog.Builder dlgEdit = new AlertDialog.Builder(context);
						dlgEdit.setTitle("Editar nome do tema");
						final EditText newNameEdit = new EditText(context);
						newNameEdit.setHint("Novo nome");
						dlgEdit.setView(newNameEdit);
						dlgEdit.setCancelable(false);
						dlgEdit.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface p1, int p2)
								{
									if(!newNameEdit.getText().toString().trim().isEmpty()) {
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
										
										
										JSONObject currentThemeObj = themeArray.getJSONObject(manager.getId());
										JSONObject newThemeObj = new JSONObject();
										newThemeObj.put("name", newNameEdit.getText().toString());
										newThemeObj.put("nivel", currentThemeObj.getJSONArray("nivel"));
										
										
										
										JSONObject newObj = new JSONObject();
										JSONArray newThemeArray = new JSONArray();
										
										for(int i = 0; i < themeArray.length(); i++) {
											if(i != manager.getId()) {
												newThemeArray.put(themeArray.getJSONObject(i));
											} else {
												newThemeArray.put(newThemeObj);
											}
										}
										
										newObj.put("theme", newThemeArray);
										
										jsonData.saveAppJsonData(newObj);
										
										Toast.makeText(context, "Editado com sucesso! Atualize a lista", Toast.LENGTH_LONG).show();
									} catch(JSONException e) {}
									}
								}
								
							
						});
						dlgEdit.setNegativeButton("Cancelar", null);
						dlgEdit.show();
						
						
					}
				}
		});
		
		gui.setLayoutParams(deleteQuestion, gui.getScreenWidth() / 6, gui.getScreenHeight() / 12, gui.getScreenWidth() / 40, 0);
		deleteQuestion.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View p1)
				{
					AlertDialog.Builder dlgConfirm = new AlertDialog.Builder(context);
					dlgConfirm.setTitle("Deseja deletar a questão?");
					dlgConfirm.setMessage("Ao deletar a questão a mesma não poderá ser recuperada.");
					dlgConfirm.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface p1, int p2)
							{
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
									
									
									
									if(manager.getTheme() != null) {
										themeObj = themeArray.getJSONObject(Integer.parseInt(manager.getTheme()));
									
										
										
										if(!themeObj.has("nivel")) {
											JSONArray array = new JSONArray();
											themeObj.put("nivel", array);
										}

										//			nivel
										nivelArray = themeObj.getJSONArray("nivel");
										JSONObject currentNivelObj = null;
										if(manager.getNivel() != null) {
											currentNivelObj = nivelArray.getJSONObject(Integer.parseInt(manager.getNivel()));
										
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
									if(manager.getTheme() != null && manager.getNivel() != null) {
									JSONObject newObj = new JSONObject();
									JSONArray newThemeArray = new JSONArray();
									for(int i = 0; i < themeArray.length(); i++) {
										if(i == Integer.parseInt(manager.getTheme())) {
//											new theme obj
											JSONObject newThemeObj = new JSONObject();
											newThemeObj.put("name", themeArray.getJSONObject(i).getString("name"));
											
											
//											new nivel array
											JSONArray newNivelArray = new JSONArray();
											
											for(int a = 0; a < nivelArray.length(); a++) {
												if(a == Integer.parseInt(manager.getNivel())) {
													JSONObject newNivelObj = new JSONObject();
													JSONArray newQuestionArray = new JSONArray();
													
//													questions
													for(int b = 0; b < questionArray.length(); b++) {
														if(b != manager.getId()) {
															newQuestionArray.put(questionArray.getJSONObject(b));
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
								
									Toast.makeText(context, "Deletado com sucesso! atualize a lista", Toast.LENGTH_LONG).show();
									
									
									
									
									
									
									
									
//								delete level	
								} else if(manager.getTheme() != null && manager.getNivel() == null) {
									JSONObject newObj = new JSONObject();
									JSONArray newThemeArray = new JSONArray();
									for(int i = 0; i < themeArray.length(); i++) {
										if(i == Integer.parseInt(manager.getTheme())) {
//											new theme obj
											JSONObject newThemeObj = new JSONObject();
											newThemeObj.put("name", themeArray.getJSONObject(i).getString("name"));


//											new nivel array
											JSONArray newNivelArray = new JSONArray();

											for(int a = 0; a < nivelArray.length(); a++) {
												if(a == manager.getId()) {
													continue;
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
									
									Toast.makeText(context, "Deletado com sucesso! atualize a lista", Toast.LENGTH_LONG).show();


//								delete theme	
								} else if(manager.getTheme() == null) {
									JSONObject newObj = new JSONObject();
									JSONArray newThemeArray = new JSONArray();
									for(int i = 0; i < themeArray.length(); i++) {
										if(i == manager.getId()) {
											continue;
										} else {
											newThemeArray.put(themeArray.getJSONObject(i));
										}
									}
									newObj.put("theme", newThemeArray);


									jsonData.saveAppJsonData(newObj);
									
									Toast.makeText(context, "Deletado com sucesso! atualize a lista", Toast.LENGTH_LONG).show();
								}
								} catch(JSONException e) {
									Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
								}
							}
					});
					dlgConfirm.setNegativeButton("Cancelar", null);
					dlgConfirm.show();
				}
		});
		
		return layout;
	}


}

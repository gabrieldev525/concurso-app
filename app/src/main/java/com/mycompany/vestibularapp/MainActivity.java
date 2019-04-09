package com.mycompany.vestibularapp;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import android.support.v4.content.*;
import android.*;
import android.content.pm.*;
import android.support.v4.app.*;
import java.util.*;
import org.json.*;

public class MainActivity extends Activity 
{
	private static final int REQUEST_CODE = 10, REQUEST_CODE_START = 11, REQUEST_CODE_MANAGER = 12;
	
	private JsonData jsonData;
	
	private String theme = null, level = null;
	
	private Gui gui;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		gui = new Gui(MainActivity.this);
		
		jsonData = new JsonData();
		
		if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
				showDialogPermission(REQUEST_CODE);
			} else {
				ActivityCompat.requestPermissions(MainActivity.this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
			}
		}
			
		Button iniciar = (Button) findViewById(R.id.startButton);
		gui.setLayoutParams((View) iniciar, gui.getScreenWidth() / 2 - gui.getScreenWidth() / 20, LinearLayout.LayoutParams.WRAP_CONTENT, 0, 0);
		iniciar.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View p1)
				{
					if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
						if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
							showDialogPermission(REQUEST_CODE_START);
						} else {
							ActivityCompat.requestPermissions(MainActivity.this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_START);
						}
					} else {
//						select the theme
						
						ArrayList<String> themesList = new ArrayList<>();
						
						try {
							JSONObject obj = new JSONObject(jsonData.loadAppJsonData());
							JSONArray themeArray = obj.getJSONArray("theme");
							
							for(int i = 0; i < themeArray.length(); i++) {
								themesList.add(themeArray.getJSONObject(i).getString("name"));
							}
						}
						catch (JSONException e)
						{}
						
						CharSequence themes[] = new CharSequence[themesList.size()];
						for(int a = 0; a < themesList.size(); a++) {
							themes[a] = themesList.get(a);
						}
						
						
						AlertDialog.Builder dlgSelectTheme = new AlertDialog.Builder(MainActivity.this)
						.setTitle("Selecione o tema")
						.setItems(themes, new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface p1, int position)
								{
									theme = Integer.toString(position);
									
									ArrayList<String> levelList = new ArrayList<>();

									try {
										JSONObject obj = new JSONObject(jsonData.loadAppJsonData());
										JSONArray themeArray = obj.getJSONArray("theme");
										JSONObject themeObj = themeArray.getJSONObject(position);
										JSONArray nivelArray = themeObj.getJSONArray("nivel");
										
										for(int i = 0; i < nivelArray.length(); i++) {
											levelList.add((i + 1) + "");
										}
									}
									catch (JSONException e)
									{}

									CharSequence levels[] = new CharSequence[levelList.size()];
									for(int a = 0; a < levelList.size(); a++) {
										levels[a] = levelList.get(a);
									}
									
//									select nivel
									AlertDialog.Builder dlgSelectTheme = new AlertDialog.Builder(MainActivity.this)
									.setTitle("Selecione o Nivel")
									.setItems(levels, new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface p1, int positon2)
										{
//											select nivel
											level = Integer.toString(positon2);
											
											Intent i = new Intent(MainActivity.this, QuestionsActivity.class);
											Bundle bundle = new Bundle();
											bundle.putString("theme", theme);
											bundle.putString("level", level);
											i.putExtras(bundle);
											startActivity(i);
										}
									});
									dlgSelectTheme.show();
									
									
									
								}
						});
						dlgSelectTheme.show();
						
						
						
						
//						startActivity(new Intent(MainActivity.this, QuestionsActivity.class));
					}
				}
		});
		
		Button manager = (Button) findViewById(R.id.managerButton);
		gui.setLayoutParams((View) manager, gui.getScreenWidth() / 2 - gui.getScreenWidth() / 20, LinearLayout.LayoutParams.WRAP_CONTENT, 0, gui.getScreenHeight() / 30);
		manager.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View p1)
				{
					if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
						if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
							showDialogPermission(REQUEST_CODE_MANAGER);
						} else {
							ActivityCompat.requestPermissions(MainActivity.this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_MANAGER);
						}
					} else {
						startActivity(new Intent(MainActivity.this, ManagerActivity.class));
					}
				}
			});
    }
	
	private void showDialogPermission(final int requestCode) {
		AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
		dlg.setTitle("permissões");
		dlg.setMessage("A permission solicitada é necessaria para armazenar no seu dispositivo alguns dados do app. Deseja permitir?");
		dlg.setPositiveButton("sim", new DialogInterface.OnClickListener() {
		@Override
				public void onClick(DialogInterface p1, int p2)
				{
				ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
					}
				});
			dlg.setNegativeButton("não", null);
		dlg.setCancelable(false);
		dlg.show();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
	{
		for(int i = 0; i < permissions.length; i++) {
			if(permissions[i].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
				switch(requestCode) {
					case REQUEST_CODE_START:
						startActivity(new Intent(MainActivity.this, QuestionsActivity.class));
						break;
					case REQUEST_CODE_MANAGER:
						startActivity(new Intent(MainActivity.this, ManagerActivity.class));
						break;
				}
			}
		}
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}
	
	
}

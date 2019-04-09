package com.mycompany.vestibularapp;

import android.app.*;
import android.os.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;
import org.json.*;
import android.util.*;

public class JsonData
{
	public String loadJSONFromAsset(Activity ctx, String src) {
		String json = null;
		try {
			InputStream is = ctx.getAssets().open(src);
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			json = new String(buffer, "UTF-8");
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
		return json;
	}
	
	public String loadJSONFromSdcard(String src) {
		String jsonStr = null;
		
		try
		{
			File yourFile = new File(Environment.getExternalStorageDirectory(), src);
			FileInputStream stream = new FileInputStream(yourFile);
			try {
				FileChannel fc = stream.getChannel();
				MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

				jsonStr = Charset.defaultCharset().decode(bb).toString();
			}
			catch (Exception e) {
				e.printStackTrace();
			} finally {
				stream.close();
			}
		} catch (Exception a) {}
		
		return jsonStr;
	}
	
	public void saveJSONSdCard(String src, JSONObject obj) {
		try {
			Writer output = null;
			File file = new File(Environment.getExternalStorageDirectory() + "/" + src);
			output = new BufferedWriter(new FileWriter(file));
			output.write(obj.toString());
			output.close();			
//			Toast.makeText(getApplicationContext(), "Composition saved", Toast.LENGTH_LONG).show();
			Log.i("save", "sucefully");
		} catch (Exception e) {
			//Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	public static final String appJsonFileName = "questoes_concurso.json";
		
	public String loadAppJsonData() {
		return loadJSONFromSdcard(appJsonFileName);
	}
	
	public void saveAppJsonData(JSONObject obj) {
		saveJSONSdCard(appJsonFileName, obj);
	}
}

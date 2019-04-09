package com.mycompany.vestibularapp.Adapter;

import android.app.*;
import android.content.*;
import android.view.*;
import android.widget.*;
import com.mycompany.vestibularapp.*;
import java.util.*;

public class AnsweredAdapter extends BaseAdapter {

	private Context context;
	public ArrayList<QuestionsAnswered> list;

	private Gui gui;

	public QuestionsAnswered questions;

	public AnsweredAdapter(Context context, ArrayList<QuestionsAnswered> answered) {
		this.context = context;
		this.list = answered;

		gui = new Gui((Activity) context);
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int p1) {
		return list.get(p1);
	}

	@Override
	public long getItemId(int p1) {
		return p1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		questions = list.get(position);
		View layout;

		if(convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			layout = inflater.inflate(R.layout.list_questions_answered, null);
		} else {
			layout = convertView;
		}
		
		layout.setPadding(gui.getScreenWidth() / 50, gui.getScreenHeight() / 45, gui.getScreenWidth() / 50, gui.getScreenHeight() / 45);
		
		TextView enunciadoText = (TextView) layout.findViewById(R.id.list_questions_answeredTextView);
		enunciadoText.setText(questions.getEnunciado());
		
		return layout;
	}
	
}

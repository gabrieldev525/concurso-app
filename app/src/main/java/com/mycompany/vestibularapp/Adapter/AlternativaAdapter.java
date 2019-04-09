package com.mycompany.vestibularapp.Adapter;

import android.app.*;
import android.content.*;
import android.view.*;
import android.widget.*;
import com.mycompany.vestibularapp.*;
import java.util.*;
import android.graphics.*;

public class AlternativaAdapter extends BaseAdapter {
	private Context context;
	public ArrayList<Alternativas> list;

	private Gui gui;

	public Alternativas Alternativas;

	public AlternativaAdapter(Context context, ArrayList<Alternativas> Alternativas) {
		this.context = context;
		this.list = Alternativas;

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
		Alternativas = list.get(position);
		View layout;

		if(convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			layout = inflater.inflate(R.layout.lista_alternativas, null);
		} else {
			layout = convertView;
		}

		layout.setPadding(gui.getScreenWidth() / 50, gui.getScreenHeight() / 45, gui.getScreenWidth() / 50, gui.getScreenHeight() / 45);

//		selection
		if(Alternativas.isSelected() == true) {
			layout.setBackgroundColor(Color.parseColor("#e5e5e5"));
		} else {
			layout.setBackground(null);
		}

//		colors
		int correctColor = Color.parseColor("#5ef268");
		int wrongColor = Color.parseColor("#f76262");

//		verify if this alternativa was clicked
		if(Alternativas.isClickedCorrect() != null) {
			if(Alternativas.isClickedCorrect() == true) {
				layout.setBackgroundColor(correctColor);
			} else {
				layout.setBackgroundColor(wrongColor);
				for(int i = 0; i < getCount(); i++) {
					if(list.get(i).isCorrect() == true) {
						list.get(i).setClickedCorrect(true);
						notifyDataSetChanged();
					}
				}
			}
		}

//		alternativa letter
		TextView alternativaLetter = (TextView) layout.findViewById(R.id.alternativaLetter);
		alternativaLetter.setText(Alternativas.getAlternativaLetter());

		TextView alternativaContent = (TextView) layout.findViewById(R.id.alternativaTexto);
		alternativaContent.setText(Alternativas.getAlternativaText());
		return layout;
	}


}

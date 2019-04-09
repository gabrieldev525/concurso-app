package com.mycompany.vestibularapp;

import android.app.*;
import android.view.*;
import android.widget.TableRow.*;
import android.widget.*;

public class Gui
{
	private Activity ctx;
	
	public Gui(Activity ctx) {
		this.ctx = ctx;	
	}
	
	public void setLayoutParams(View v, int width, int height, int x, int y) {
		LayoutParams params = new TableRow.LayoutParams(width, height);
		params.setMargins(x, y, 0, 0);
		v.setLayoutParams(params);
	}
	
	public int getScreenWidth() {
		return (int) ctx.getWindowManager().getDefaultDisplay().getWidth();
	}
	
	public int getScreenHeight() {
		return (int) ctx.getWindowManager().getDefaultDisplay().getHeight();
	}
	
	//listview get item
	public View getViewByPosition(int pos, ListView listView) {
		final int firstListItemPosition = listView.getFirstVisiblePosition();
		final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

		if (pos < firstListItemPosition || pos > lastListItemPosition ) {
			return listView.getAdapter().getView(pos, null, listView);
		} else {
			final int childIndex = pos - firstListItemPosition;
			return listView.getChildAt(childIndex);
		}
	}
}

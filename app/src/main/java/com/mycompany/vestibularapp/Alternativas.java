package com.mycompany.vestibularapp;

import android.widget.*;

public class Alternativas
{
	private String alternativaLetter;
	private String alternativaText;
	private boolean correct = false;
	private Boolean clickedCorrect = null;
	private Boolean selected = false;

	public void setSelected(Boolean selected)
	{
		this.selected = selected;
	}

	public Boolean isSelected()
	{
		return selected;
	}

	public void setClickedCorrect(Boolean clickedCorrect)
	{
		this.clickedCorrect = clickedCorrect;
	}

	public Boolean isClickedCorrect()
	{
		return clickedCorrect;
	}

	public void setCorrect(boolean correct)
	{
		this.correct = correct;
	}

	public boolean isCorrect()
	{
		return correct;
	}

	public void setAlternativaText(String alternativaText)
	{
		this.alternativaText = alternativaText;
	}

	public String getAlternativaText()
	{
		return alternativaText;
	}

	public void setAlternativaLetter(String alternativaLetter)
	{
		this.alternativaLetter = alternativaLetter;
	}

	public String getAlternativaLetter()
	{
		return alternativaLetter;
	}
}

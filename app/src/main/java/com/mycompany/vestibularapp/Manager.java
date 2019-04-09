package com.mycompany.vestibularapp;

public class Manager
{
	private String enunciado = "";
	private String author = "";
	private String nivel = null;
	private int id;
	private Object alternativas[][];
	private String imagem;
	private String theme = null;
	
	private String session = null;
	
	private String name = "";

	public void setAuthor(String author)
	{
		this.author = author;
	}

	public String getAuthor()
	{
		return author;
	}

	public void setTheme(String theme)
	{
		this.theme = theme;
	}

	public String getTheme()
	{
		return theme;
	}
	
	
	public static abstract class OnClickList {
		abstract void setOnclickListener();
	}
	
	private OnClickList onClickList;

	public void setOnClickList(OnClickList onClickList)
	{
		Manager.this.onClickList = onClickList;
	}

	public OnClickList getOnClickList()
	{
		return Manager.this.onClickList;
	}
	
	
	
	

	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public void setSession(String session)
	{
		this.session = session;
	}

	public String getSession()
	{
		return session;
	}

	public void setAlternativas(Object[][] alternativas)
	{
		this.alternativas = alternativas;
	}

	public Object[][] getAlternativas()
	{
		return alternativas;
	}

	public void setImagem(String imagem)
	{
		this.imagem = imagem;
	}

	public String getImagem()
	{
		return imagem;
	}
	


	public void setEnunciado(String enunciado)
	{
		this.enunciado = enunciado;
	}

	public String getEnunciado()
	{
		return enunciado;
	}

	public void setNivel(String nivel)
	{
		this.nivel = nivel;
	}

	public String getNivel()
	{
		return nivel;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getId()
	{
		return id;
	}}

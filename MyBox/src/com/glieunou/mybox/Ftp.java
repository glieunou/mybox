package com.glieunou.mybox;

public class Ftp {
	
	private int id;
	
	private String nom;
	
	private String host; 
	
	private String user;
	
	private String pass;
	
	private int port;
	
	private int status;
	
	public Ftp(){
		
		this.id=0; this.nom=""; this.host=""; 
		
		this.user=""; this.pass=""; this.port=0; this.status=0;
		
	}
	
	public Ftp(String nom,String host,String user,String pass,int port,int status){
		
		this.nom=nom; this.host=host; this.user=user; this.pass=pass; 
		
		this.port=port; this.status=status;
	}
	
	public int GetId(){
		
		return this.id;
	}
	
	public void SetId(int id){
		
		this.id=id;
	}

	public String GetNom(){
		
		return this.nom;
	}
	
	public void SetNom(String nom){
		
		this.nom=nom;
	}
	
	public String GetHost(){
		
		return this.host;
	}
	
	public void SetHost(String host){
		
		this.host=host;
	}
	
	public String GetUser(){
		
		return this.user;
	}
	
	public void SetUser(String user){
		
		this.user=user;
	}
	
	public String GetPass(){
		
		return this.pass;
	}
	
	public void SetPass(String pass){
		
		this.pass=pass;
	}
	
	public int GetPort(){
		
		return this.port;
	}
	
	public void SetPort(int port){
		
		this.port=port;
	}
	
   public int GetStatus(){
		
		return this.status;
	}
	
	public void SetStatus(int status){
		
		this.status=status;
	}
	
}





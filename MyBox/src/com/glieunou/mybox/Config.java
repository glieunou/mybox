package com.glieunou.mybox;

public class Config {

	private int id;
	private int statu_secu;
	private String pass_secu;
	private int statu_synchro;
	private String gmail_cpte;
	private String key_connect;
	private int statu_share;
	
	public Config(){
		
		this.id=0; this.statu_secu=0; this.pass_secu=""; this.statu_synchro=0;
		
		this.gmail_cpte=""; this.key_connect=""; this.statu_share=0;
		
	}
	
	public Config(int id,int statu_secu,String pass_secu,int statu_synchro,String gmail_cpte,String key_connect,int statu_share){
		
		this.id=id; this.statu_secu=statu_secu; this.pass_secu=pass_secu; this.statu_synchro=statu_synchro;
		
		this.gmail_cpte=gmail_cpte; this.key_connect=key_connect; this.statu_share=statu_share;
		
	}
	
	public int getId() { return this.id; }
	
	public int getStatuSecu(){ return this.statu_secu; }
	
	public String getPassSecu() { return this.pass_secu; }
	
	public int getStatuSynchro() { return this.statu_synchro; }
	
	public String getGmailCpte() { return this.gmail_cpte; }
	
	public String getKeyConnect() { return this.key_connect; }
	
	public int getStatuShare() { return this.statu_share; }
	
	
	public void SetId(int arg) { this.id=arg; }
	
	public void setStatuSecu(int arg) { this.statu_secu=arg; }
	
	public void setPassSecu(String arg) { this.pass_secu=arg; }
	
	public void setStatuSynchro(int arg) { this.statu_synchro=arg; }
	
	public void setGmailCpte(String arg) { this.gmail_cpte=arg; }
	
	public void setKeyConnect(String arg) { this.key_connect=arg; }
	
	public void setStatuShare(int arg) { this.statu_share=arg; }
	
	
}


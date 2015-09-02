package com.glieunou.mybox;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Requete {
	
	private static final int VERSION_BDD=1;

    private static final String NOM_BDD="mybox.db";

    private static final String TABLE_FTP="ftp_server";
    
    private static final String TABLE_SETTING="configur";
	
	private static final String F_ID="f_id";
	
	private static final int NUM_ID=0;

    private static final String F_NOM="f_nom";
    
    private static final int NUM_NOM=1;

    private static final String F_HOST="f_host";
    
    private static final int NUM_HOST=2;

    private static final String F_USER="f_user";
    
    private static final int NUM_USER=3;

    private static final String F_PASS="f_pass";
    
    private static final int NUM_PASS=4;

    private static final String F_PORT="f_port";
    
    private static final int NUM_PORT=5;

    private static final String F_STATUS="f_status";
    
    private static final int NUM_STATUS=6;
	
    private static final String ID_SETTING="id_setting";
    
    private static final int NUM_ID_SETING=0;
    
    private static final String SECU_STATUS="secu_status";
    
    private static final int NUM_SECU_STATUS=1;
    
    private static final String PASS_SECU="pass_secu";
    
    private static final int NUM_PASS_SECU=2;
    
    private static final String SYNCHRO_STATUS="synchro_status";
    
    private static final int NUM_SYNCHRO_STATUS=3;
    
    private static final String GMAIL_ACCOUNT="gmail_account";
    
    private static final int NUM_GMAIL_ACCOUNT=4;
    
    private static final String KEY_CONNECT="key_connect";
    
    private static final int NUM_KEY_CONNECT=5;
    
    private static final String SHARING_STATUS="sharing_status";
    
    private static final int NUM_SHARING_STATUS=6;
    
    
    
    private SQLiteDatabase bdd;

    private DataBase ma_bdd;

    public Requete(Context context){

        // on crée la bdd et ses tables
        ma_bdd=new DataBase(context, NOM_BDD, null, VERSION_BDD);
    }


    public void open(){

        // ouverture de la BDD en écriture
        bdd=ma_bdd.getWritableDatabase();
    }
    
    public void close(){

        // on ferme l'accès à la BDD
        bdd.close();
    }


    public SQLiteDatabase getBdd(){

        return bdd;
    }
    
    public long insertFTP(Ftp ftp){

        // création d'un content values

        ContentValues values=new ContentValues();

      //values.put(F_ID,ftp.GetID());

        values.put(F_NOM,ftp.GetNom());

        values.put(F_HOST,ftp.GetHost());

        values.put(F_USER,ftp.GetUser());

        values.put(F_PASS,ftp.GetPass()); 

        values.put(F_PORT,ftp.GetPort());

        values.put(F_STATUS,ftp.GetStatus());

        return bdd.insert(TABLE_FTP,null,values);

    }

    public long insertConfig(Config para){

        // création d'un content values

        ContentValues values=new ContentValues();
        
        values.put(ID_SETTING,para.getId());
        
        values.put(SECU_STATUS,para.getStatuSecu());

        values.put(PASS_SECU,para.getPassSecu());

        values.put(SYNCHRO_STATUS,para.getStatuSynchro());

        values.put(GMAIL_ACCOUNT,para.getGmailCpte());
        
        values.put(KEY_CONNECT, para.getKeyConnect());
        
        values.put(SHARING_STATUS, para.getStatuShare());

        return bdd.insert(TABLE_SETTING,null,values);

    }
    
    public long updateFTP(Ftp ftp,int id){

        ContentValues values=new ContentValues();

        //values.put(W_ID,bourse.getID());

        values.put(F_NOM,ftp.GetNom());

        values.put(F_HOST,ftp.GetHost());

        values.put(F_USER,ftp.GetUser());

        values.put(F_PASS,ftp.GetPass()); 

        values.put(F_PORT,ftp.GetPort());

        values.put(F_STATUS,ftp.GetStatus());

        return bdd.update(TABLE_FTP,values, F_ID+"="+id,null);

    }
    
    public long updateConfig(Config para,int id){

        // création d'un content values

        ContentValues values=new ContentValues();
        
        values.put(SECU_STATUS,para.getStatuSecu());

        values.put(PASS_SECU,para.getPassSecu());

        values.put(SYNCHRO_STATUS,para.getStatuSynchro());

        values.put(GMAIL_ACCOUNT,para.getGmailCpte());
        
        values.put(KEY_CONNECT, para.getKeyConnect());
        
        values.put(SHARING_STATUS, para.getStatuShare());

        return bdd.update(TABLE_SETTING,values, ID_SETTING+"="+id,null);

    }
    
    
   public int RemoveFTP(int id){
    	
          return bdd.delete(TABLE_FTP, F_ID+"="+id,null);
    }
   
   public int RemoveConfig(int id){
   	
       return bdd.delete(TABLE_SETTING, ID_SETTING+"="+id,null);
   }
   
   
   public Ftp getFtp(int id){

       Cursor c=bdd.query(TABLE_FTP, new String[]{F_ID,F_NOM,F_HOST,F_USER,F_PASS,F_PORT,F_STATUS}, F_ID+"="+id, null,null,null,null);

       return cursorFTP(c);

   }


   public Ftp getFtp(String titre,String jour){

       Cursor c=bdd.query(TABLE_FTP, new String[]{F_ID,F_NOM,F_HOST,F_USER,F_PASS,F_PORT,F_STATUS}, F_NOM+"="+titre, null,null,null,null);

       return cursorFTP(c);
   }


   public Ftp cursorFTP(Cursor c){

       if(c.getCount()==0){

           return null;

       }

       else { c.moveToFirst();

           Ftp ftp=new Ftp();
           
           ftp.SetId(c.getInt(NUM_ID));

           ftp.SetNom(c.getString(NUM_NOM));

           ftp.SetHost(c.getString(NUM_HOST));

           ftp.SetUser(c.getString(NUM_USER));

           ftp.SetPass(c.getString(NUM_PASS));

           ftp.SetPort(c.getInt(NUM_PORT));

           ftp.SetStatus(c.getInt(NUM_STATUS));

           c.close();

           return ftp;

       }

   }
    
   public Cursor getAllFTP(){
   	
  	 Cursor c=bdd.query(TABLE_FTP, new String[]{F_ID,F_NOM,F_HOST,F_USER,F_PASS,F_PORT,F_STATUS}, null, null,null,null,null);

       return c;
  }
  
   public Config getConfig(int id){

       Cursor c=bdd.query(TABLE_SETTING, new String[]{ID_SETTING,SECU_STATUS,PASS_SECU,SYNCHRO_STATUS,GMAIL_ACCOUNT,KEY_CONNECT,SHARING_STATUS}, ID_SETTING+"="+id, null,null,null,null);

       return cursorConfig(c);

   }
   
   public Config cursorConfig(Cursor c){

       if(c.getCount()==0){

           return null;

       }

       else { c.moveToFirst();

           Config config=new Config();

           config.SetId(c.getInt(NUM_ID_SETING));
           
           config.setStatuSecu(c.getInt(NUM_SECU_STATUS));
           
           config.setPassSecu(c.getString(NUM_PASS_SECU));
           
           config.setStatuSynchro(c.getInt(NUM_SYNCHRO_STATUS));
           
           config.setGmailCpte(c.getString(NUM_GMAIL_ACCOUNT));
           
           config.setKeyConnect(c.getString(NUM_KEY_CONNECT));
           
           config.setStatuShare(c.getInt(NUM_SHARING_STATUS));
           
           c.close();

           return config;

       }

   }

    
    
    
}

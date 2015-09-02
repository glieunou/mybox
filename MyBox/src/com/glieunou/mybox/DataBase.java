package com.glieunou.mybox;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DataBase extends SQLiteOpenHelper {

	private static final String TABLE_FTP="ftp_server";
    
    private static final String TABLE_SETTING="configur";
	
	private static final String F_ID="f_id";

    private static final String F_NOM="f_nom";

    private static final String F_HOST="f_host";

    private static final String F_USER="f_user";

    private static final String F_PASS="f_pass";

    private static final String F_PORT="f_port";

    private static final String F_STATUS="f_status";
    
    
    private static final String ID_SETTING="id_setting";
    
    private static final String SECU_STATUS="secu_status";
    
    private static final String PASS_SECU="pass_secu";
    
    private static final String SYNCHRO_STATUS="synchro_status";
    
    private static final String GMAIL_ACCOUNT="gmail_account";
    
    private static final String KEY_CONNECT="key_connect";
    
    private static final String SHARING_STATUS="sharing_status";
    
    
    private static final String CREATE_F="CREATE TABLE "+TABLE_FTP+" ("+F_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+F_NOM+" TEXT NOT NULL, "+F_HOST+" TEXT NOT NULL, "+F_USER+" TEXT NOT NULL, "+F_PASS+" TEXT NOT NULL, "+F_PORT+" INTEGER NOT NULL,"+F_STATUS+" INTEGER NOT NULL); ";

    private static final String CREATE_S="CREATE TABLE "+TABLE_SETTING+" ("+ID_SETTING+" INTEGER PRIMARY KEY AUTOINCREMENT, "+SECU_STATUS+" INTEGER NOT NULL, "+PASS_SECU+" TEXT NOT NULL, "+SYNCHRO_STATUS+" INTEGER NOT NULL, "+GMAIL_ACCOUNT+" TEXT NOT NULL, "+KEY_CONNECT+" TEXT NOT NULL, "+SHARING_STATUS+" INTEGER NOT NULL ); "; 
    
    
    public DataBase(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
	
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_F); db.execSQL(CREATE_S);
        
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+TABLE_FTP);  db.execSQL("DROP TABLE IF EXISTS "+TABLE_SETTING);
        
        onCreate(db);

    }
}

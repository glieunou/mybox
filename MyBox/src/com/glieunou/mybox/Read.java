package com.glieunou.mybox;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;

public class Read extends Activity {
	
	private static final int CODE_ACTIVITE=6;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
	   
// récupération de l'intent clé envoyé !!
        
        Bundle objetbunble  = this.getIntent().getExtras();
        
        //On récupère les données du Bundle
        
        if (objetbunble != null && objetbunble.containsKey("lien")) {
        	
            String file1=this.getIntent().getStringExtra("key").toString();
            
            int cle = Integer.parseInt(this.getIntent().getStringExtra("key"));
            
            String exten=file1.substring(file1.lastIndexOf("."));
	    	  
            Intent intent = new Intent();
            
            intent.setAction(android.content.Intent.ACTION_VIEW);
            
            File file = new File(file1);
            
            String mimetype = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(exten);
            
            intent.setDataAndType(Uri.fromFile(file), mimetype);
            
            startActivity(intent);
               
        }else {
              
        	Intent intent=new Intent(Read.this,MainActivity.class);
        	
		    startActivityForResult(intent,CODE_ACTIVITE);
				
        }
	
	}
	
}


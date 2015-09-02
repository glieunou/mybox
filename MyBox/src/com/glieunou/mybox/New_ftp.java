package com.glieunou.mybox;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class New_ftp extends Activity {
	
	private EditText txt1,txt2,txt3,txt4,txt5;
	
	private TextView bt; private static final int CODE_ACTIVITE=1;
	
	private int cle; private Ftp cl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.server_page);
		
		txt1=(EditText)findViewById(R.id.editText1);
		txt2=(EditText)findViewById(R.id.editText2);
		txt3=(EditText)findViewById(R.id.editText3);
		txt4=(EditText)findViewById(R.id.editText4);
		txt5=(EditText)findViewById(R.id.editText5);
		
  // récupération de l'intent clé envoyé !!
        
        Bundle objetbunble  = this.getIntent().getExtras();
        
        final Requete req=new Requete(this); final Resources re=getResources();
        
        //On récupère les données du Bundle
        
        if (objetbunble != null && objetbunble.containsKey("key")) {
        	
               cle = Integer.parseInt(this.getIntent().getStringExtra("key"));
               
        }else {
               cle=0;
        }
		
        req.open();  cl=new Ftp();
        
        cl=req.getFtp(cle);
        
        req.close();
        
        if(cl!=null){
        	
        	txt1.setText(""+cl.GetNom());
        	txt2.setText(""+cl.GetHost());
        	txt3.setText(""+cl.GetUser());
        	txt4.setText(""+cl.GetPass());
        	txt5.setText(""+cl.GetPort());
        	
        }
        
        
        
		bt=(TextView)findViewById(R.id.textView1);
		
		
		bt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		
				String nom=txt1.getText().toString();
				String host=txt2.getText().toString();
				String user=txt3.getText().toString();
				String pass=txt4.getText().toString();
				String port=txt5.getText().toString();
				
				if(nom.isEmpty() || host.isEmpty() || user.isEmpty() || pass.isEmpty() || port.isEmpty() ){ 
					
					Toast.makeText(New_ftp.this, "Veuillez remplir tous les champs", Toast.LENGTH_LONG).show();

				} else {
					
					Ftp ftp=new Ftp(nom,host,user,pass,Integer.parseInt(port),0);
					
					req.open();
					
					if(cl==null) req.insertFTP(ftp);
					
					else req.updateFTP(ftp, cle);
					
					req.close();
					
					Toast.makeText(New_ftp.this, "Enregistrement réussi", Toast.LENGTH_LONG).show();

	                // retour au tableau de bord

	                Intent intent=new Intent(New_ftp.this,MainActivity.class);

	                startActivityForResult(intent,CODE_ACTIVITE);
					
					
				}
		 	
				
				
				
			}
		});
		
		
	}

}

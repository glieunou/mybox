package com.glieunou.mybox;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Setting extends Activity {

	private static final int CODE_ACTIVITE=10;
	
	private CheckBox b1,b2,b3;
	
	private EditText txt1,txt2; private TextView bt;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        
        b1=(CheckBox) findViewById(R.id.checkBox1);
        
        b2=(CheckBox) findViewById(R.id.checkBox2);
        
        b3=(CheckBox) findViewById(R.id.checkBox3);
        
        txt1=(EditText) findViewById(R.id.editText1);
        
        txt2=(EditText) findViewById(R.id.editText2);
        
        bt=(TextView) findViewById(R.id.textView1);
        
        
        // initialisation des valeurs des paramètres
        
        final Resources re=getResources();
        
        Config cf=new Config(); final Requete req=new Requete(this);
        
        req.open();
        
        cf=req.getConfig(1);
        
        if(cf==null){

            b1.setChecked(false); b2.setChecked(false); b3.setChecked(false);
        	
            txt1.setEnabled(false); txt2.setEnabled(false);
            
            txt1.setText(""); txt2.setText("");
        	 
        } else{
        	
        	if(cf.getStatuSecu()==1) { b1.setChecked(true); txt1.setEnabled(true); txt1.setText(cf.getPassSecu());  }
        	
        	else if(cf.getStatuSecu()==0) { b1.setChecked(false); txt1.setEnabled(false); txt1.setText(cf.getPassSecu());  }
        	
        	
        	if(cf.getStatuSynchro()==1){ b2.setChecked(true); txt2.setEnabled(true); txt2.setText(cf.getGmailCpte()); }
        	
        	else if(cf.getStatuSynchro()==0){ b2.setChecked(false); txt2.setEnabled(false); txt2.setText(cf.getGmailCpte()); }
        	
        	
        	if(cf.getStatuShare()==1) b3.setChecked(true); else if(cf.getStatuShare()==0) b3.setChecked(false);
        	
        }
        
        
        req.close();
        
      
        b1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
			 if(b1.isChecked()) txt1.setEnabled(true);
			 
			 else txt1.setEnabled(false);
				
			}
		});
        
        b2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
		    if(b2.isChecked()) txt2.setEnabled(true);
		    
		    else txt2.setEnabled(false);
				
			}
		});
        
        
        bt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				int c1,c2,c3; String t1,t2;
				
				if(b1.isChecked()) { c1=1; t1=txt1.getText().toString();  } else { c1=0; t1=""; }
				
				if(b2.isChecked()) { c2=1; t2=txt2.getText().toString(); } else { c2=0; t2=""; }
				
				if(b3.isChecked()) c3=1; else c3=0;
				

				Config cf1=new Config();
				
				Config cf2=new Config(1,c1,t1,c2,t2,"",c3);
				
				req.open();
		        
		        cf1=req.getConfig(1);
		        
		        if(cf1==null) { req.insertConfig(cf2); }
		        
		        else{ req.updateConfig(cf2, 1); }
		        	
		        req.close();
				
                String ch=re.getString(R.string.set_ok);
				
				Toast.makeText(Setting.this, "" + ch + "", Toast.LENGTH_LONG).show();
						
			}
		});
        
	}
	
	

	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        // Inflate the menu; this adds items to the action bar if it is present.
	        getMenuInflater().inflate(R.menu.main, menu);
	        return true;
	    }
	 
	  @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        // Handle item selection
	        switch (item.getItemId()) {
	        
	            case R.id.action_home:
	                home();
	                return true;
	                 
	            case R.id.action_settings:
	                setting();
	                return true;
	                
	            default:
	                return super.onOptionsItemSelected(item);
	        }
	    }
	    
	  public void setting(){
	     	
	 		Intent intent=new Intent(Setting.this,Setting.class);
	 			
	 	    			startActivityForResult(intent,CODE_ACTIVITE); 
	 	    }
	 	
	 	 public void home(){
	     	
	 		Intent intent=new Intent(Setting.this,MainActivity.class);
	 			
	 	    			startActivityForResult(intent,CODE_ACTIVITE); 
	 	    }
	    
}

package com.glieunou.mybox;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;

@SuppressLint("ValidFragment")
public class Fenetre3 extends DialogFragment {

public int cle; private static final int CODE_ACTIVITE=10;

	
	@SuppressLint("ValidFragment")
	public Fenetre3(int choix){
		
		cle=choix; 
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	   
		final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

	   
	    // Get the layout inflater
       LayoutInflater inflater = getActivity().getLayoutInflater();
       
	     // Inflate and set the layout for the dialog
	     // Pass null as the parent view because its going in the dialog layout
       
         final View row=inflater.inflate(R.layout.pass_open, null);
       

   	     final Resources re1=getResources();
         
	     builder.setView(row)
	     
	     .setTitle("Accès vérouillé")
	    
	     // Add action buttons
	     .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
	          @Override
               public void onClick(DialogInterface dialog, int id) {
	          // sign in the user ...
	        	  
	        	  final Requete req=new Requete(getActivity());
	        	  
	        	Config cf=new Config();
           	   
            	  req.open();
            	  
            	  cf=req.getConfig(1);
            	  
            	  if(cf!=null){
            		  
            		  EditText pass=(EditText) row.findViewById(R.id.editText1);
            		  
            		  if(cf.getPassSecu().equals(pass.getText().toString())){
            			
            			  dialog.dismiss();
            		
      				  Fenetre dialog2 = new Fenetre(cle);
      			    
      		          FragmentManager manager=getActivity().getSupportFragmentManager();
      		        
      		          dialog2.show(manager, "NoticeDialogFragment");
      				    
      			     }
            		  
            		  else Toast.makeText(getActivity(), re1.getString(R.string.pass_non), Toast.LENGTH_LONG).show();
            		  
            		  
            	  } else { dialog.cancel();  }
            	  
            	  req.close();
	        	  
	        	  
	           }
                })
	        
           .setNegativeButton("Non", new DialogInterface.OnClickListener() {
	             public void onClick(DialogInterface dialog, int id) {
	            	 
	             //LoginDialogFragment.this.getDialog().cancel();
	            	 
	            	 dialog.cancel();
	            	 
                   }
	             }); 
	     
	 return builder.create();
	
	}

	

	
}
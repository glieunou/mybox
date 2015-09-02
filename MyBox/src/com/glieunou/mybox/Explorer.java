package com.glieunou.mybox;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;


import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Explorer extends ListActivity {

	private String path; private int cle=0; private ProgressDialog pd;  private FTPClient client; 
	
 	public static String data=""; public static String exten=""; private static final int CODE_ACTIVITE=5;
	
	@Override
	  protected void onCreate(Bundle savedInstanceState) {
		
	    super.onCreate(savedInstanceState);
	    
	    setContentView(R.layout.explorer);
	    
	    
        // récupération de l'intent clé envoyé !!
        
        Bundle objetbunble  = this.getIntent().getExtras();
        
        final Requete req=new Requete(this); final Resources re=getResources();
        
        //On récupère les données du Bundle
        
        if (objetbunble != null && objetbunble.containsKey("key")) {
        	
               cle = Integer.parseInt(this.getIntent().getStringExtra("key"));
               
        }else {
               cle=0;
        }
        

	    // Use the current directory as title
	    path = "/";
	    
	    if (getIntent().hasExtra("path")) {
	    
	    	path = getIntent().getStringExtra("path");
	    
	    }
	    
	    setTitle(path);

	    // Read all files sorted into the values-array
	    List values = new ArrayList();
	    
	    File dir = new File(path);
	    
	    if (!dir.canRead()) {
	    	
	      setTitle(getTitle() + " (inaccessible)");
	    
	    }
	    
	    String[] list = dir.list();
	    
	    if (list != null) {
	    
	    	for (String file : list) {
	        
	    		if (!file.startsWith(".")) {
	          
	    			values.add(file);
	        }
	      }
	    }
	    
	    Collections.sort(values);

	    // Put the data into the list
	    ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_2, android.R.id.text1, values);
	    
	    setListAdapter(adapter);
	  }

	  @Override
	  protected void onListItemClick(ListView l, View v, int position, long id) {
	    
		  String filename = (String) getListAdapter().getItem(position);
	    
		  if (path.endsWith(File.separator)) {
	      
			  filename = path + filename;
	    
		  } else {
	      
			  filename = path + File.separator + filename;
	    }
	    if (new File(filename).isDirectory()) {
	    	
	      Intent intent = new Intent(this, Explorer.class);
	      
	      intent.putExtra("path", filename);
	      
	      startActivity(intent);
	    
	    } else {
	    
	    	// upload du fichier en question sur le serveur ftp selectionné
	    	
	    	FtpUpload runner = new FtpUpload();
	    	   
	     	  client=new FTPClient();
	     	   
	     	  try{
	     		
	             runner.execute(filename); 
	         	   
	     		Toast.makeText(this, "Début de l'upload... ", Toast.LENGTH_LONG).show();    		
	     	 		 
	     			 
	     	    } 
	     	
	     	 catch(Exception e){
	     			 
	     			Toast.makeText(this, "Erreur de connexion au serveur ", Toast.LENGTH_LONG).show();    		
	     	 		 
	     		 } 
	    	
	    	
	    }
	  }
	
	  private class FtpUpload extends AsyncTask<String, Void, Boolean>{

			@Override
			protected Boolean doInBackground(String... params) {
				// TODO Auto-generated method stub
				return UploadFile(params[0]);
				
			}
			
			protected void onPostExecute(Boolean result) {
				
				if (pd!=null) { pd.dismiss(); }
				
				if(result){
					
					Toast.makeText(Explorer.this, "Fichier uploader avec succès...", Toast.LENGTH_LONG).show();
					
					Bundle objetbundle1=new Bundle();
					
					objetbundle1.putString("key",""+cle);
		            
					Intent intent=new Intent(Explorer.this,Import_file.class);
		         	 
		            intent.putExtras(objetbundle1);
		 			
		 			startActivityForResult(intent,CODE_ACTIVITE); 
					
				} else {
					
					Toast.makeText(Explorer.this, "Impossible d'uploader le fichier", Toast.LENGTH_LONG).show();
				}
				
			}
			
			protected void onPreExecute() { 
			    
		    	pd = new ProgressDialog(Explorer.this); 
		    	
		    	pd.setTitle("Téléchargement..."); 
		    	
		    	pd.setMessage("Patientez svp."); 
		    	
		    	pd.setCancelable(false); 
		    	
		    	pd.setIndeterminate(true); 
		    	
		    	pd.show();
		    	
		    }
			  
		  }
	  
	  private boolean UploadFile(String fichier){
			
			final Requete req=new Requete(this);
	  	   
	    	  req.open();
	    	  
	    	  Ftp f=new Ftp();
	    	  
	    	  f=req.getFtp(cle);
	    	  
	    	  req.close();
	  		
	        FTPClient ftp=new FTPClient();
	        
	        int port=0;
	    	  
	    	  try{
	          
	          if(f.GetHost().isEmpty()) port=21; else port=f.GetPort();
	          
	          ftp.setConnectTimeout(10*1000); 
	          
	          ftp.connect(f.GetHost().toString(),port);
	          
	          if(ftp.isConnected()){ ftp.login(f.GetUser().toString(), f.GetPass().toString());
	          
	          ftp.enterLocalPassiveMode();
	          
	          // création du repertoire MyBox au cas ou il n'existe pas
	          
	          ftp.makeDirectory("MyBox_Glieunou");
	          
	          // on récupère le repertoire courant
			  String workspace=ftp.printWorkingDirectory().toString();
				  
			  ftp.changeWorkingDirectory("MyBox_Glieunou");
			  
			  boolean ret=false; Context context=getApplicationContext();
			  
			  // upload du fichier dans le dossier adéquat
			  
			  File fich=new File(fichier);
			  
			  FileInputStream srcFileStream = new FileInputStream(fich); 
			  
			  String tab[]=fichier.split("/"); int nb=tab.length; String filename=tab[nb-1];
			  
			  String current_folder="/MyBox_Glieunou/"+filename;
			  
			  ret= ftp.storeFile(current_folder,srcFileStream);
			  
			  srcFileStream.close();
			    
			  return ret;
				 
	            } else return false;
	           
	    	  } catch (SocketException e) {
	    		  
	    		  return false;
	              	 
	    	  }catch (UnknownHostException e) {
	    		  
	    		  return false;
	    		  
	    	  }  catch (IOException e) {
	    		  
	    		  return false;
	    		  
	          }  catch(NullPointerException e){
	        	  
	        	  return false;
	        	  
	          }catch(Exception e){
	        	  
	        	  return false;
	          }
		}
		
	
}


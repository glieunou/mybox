package com.glieunou.mybox;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

@SuppressLint("ValidFragment")
public class Fenetre extends DialogFragment {
	
	public static int cle; private static final int CODE_ACTIVITE=5;
	
	private String msg=""; private Context context; private ProgressDialog pd;
	
	 private FTPClient client;
	
	@SuppressLint("ValidFragment")
	public Fenetre(int choix){
		
		cle=choix; 
	}
	
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    
	    final Resources re=getResources();
	    
	    String[] ta={"Ouvrir le serveur","Modifier","Supprimer","Tester la connectivité","Protéger"};
	    
	    builder.setTitle("Action à faire")
	    
	           .setItems(ta, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int action) {
	               // The 'which' argument contains the index position
	               // of the selected item
	            	   
	            	// choix a faire en fonction de la valeur d'action
	                   
	                   Bundle objetbundle=new Bundle();
	                   
	                   final Resources re=getResources();
	                   
	              if(action==0){
	                  	 
	            	  Intent intent=new Intent(getActivity(),Import_file.class);
	                	
	                	objetbundle.putString("key",""+cle);
	                  	 
		                intent.putExtras(objetbundle);
	       				
	       				startActivityForResult(intent,CODE_ACTIVITE);
	                  	 
	                   } 
	                   
	                   else if(action==1){
	                	   
	                	Intent intent=new Intent(getActivity(),New_ftp.class);
	                	
	                	objetbundle.putString("key",""+cle);
	                  	 
		                intent.putExtras(objetbundle);
	       				
	       				startActivityForResult(intent,CODE_ACTIVITE);
	                     
	                   }
	              
	                   else if(action==2){
	                	   
	                	  final Requete req1=new Requete(getActivity());
	  					  
	  					  req1.open();
	  					  
	  					  Ftp f=new Ftp(); f=req1.getFtp(cle);  
						  
 						  if(f.GetStatus()==0) req1.RemoveFTP(cle);
	                	  
 						  req1.close();
	                  	 
 						 Intent intent=new Intent(getActivity(),MainActivity.class);
 						 
 						 objetbundle.putString("ret", "1");
 			         	 
 	 			          intent.putExtras(objetbundle);
 						 
 	       				  startActivityForResult(intent,CODE_ACTIVITE);
 	       				  
	                   }
	                   
	                   else if(action==3){
	                	   
	                	   // test de la connectivité au serveur
	                	   
	                	   FtpTask runner = new FtpTask();
	                	   
	                	   client=new FTPClient(); 
	                	   
	                	  try{
	                		
                           Toast.makeText(getActivity(), "Test de connexion en cours... ", Toast.LENGTH_LONG).show();    		
	                	 		 
	                		  runner.execute();
	                		  
	                	    client=runner.get();
	                			 
	                	    } 
	                	
	                	 catch(Exception e){
	                			 
	                			Toast.makeText(getActivity(), "Erreur de connexion au serveur ", Toast.LENGTH_LONG).show();    		
	                	 		 
	                		 } 
	                	  
		                  	 
	                   }
	              
	                   else if(action==4){
	                	   
	                	   final Requete req1=new Requete(getActivity());
		  					  
		  					  req1.open();
		  					  
		  					  Ftp f=new Ftp(); f=req1.getFtp(cle);  
							  
		  					  if(f.GetStatus()==0) f.SetStatus(1); else f.SetStatus(0);
		  					  
		  					  req1.updateFTP(f, cle);
		                	  
	 						  req1.close();
		                  	 
	 						 Intent intent=new Intent(getActivity(),MainActivity.class);
	 						 
	 						 objetbundle.putString("ret", "1");
	 			         	 
	 	 			         intent.putExtras(objetbundle);
	 						 
	 	       				 startActivityForResult(intent,CODE_ACTIVITE);
	 	       				  
	                   }
	            	   
	           }
	    });  
	    
	    return builder.create();
	}
	
	private class FtpTask extends AsyncTask<Context, Void, FTPClient> {
		
	    protected FTPClient doInBackground(Context... args) {
	    
	       return getServer();
	    }

	    protected void onPostExecute(FTPClient result) {
	    	
	      if(getActivity()!=null){
	    	
	    	if(result!=null){
	    		
	    		 if(result.isConnected()) Toast.makeText(getActivity(), "Connexion au serveur réussie", Toast.LENGTH_LONG).show();
	    		    
	    	}  else Toast.makeText(getActivity(), "Impossible de joindre le serveur", Toast.LENGTH_LONG).show();
	     
	    } else Toast.makeText(getActivity(), "Connexion serveur impossible ", Toast.LENGTH_LONG).show();
	    
	    }
	    protected void onPreExecute() { 
	    
	    
	    }
	    
	}
	
	private FTPClient getServer(){
		
		final Requete req=new Requete(getActivity());
  	   
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
          
          return ftp;  } else return null;
           
    	  } catch (SocketException e) {
    		  
    		  return null;
              	 
    	  }catch (UnknownHostException e) {
    		  
    		  return null;
    		  
    	  }  catch (IOException e) {
    		  
    		  return null;
    		  
          }  catch(NullPointerException e){
        	  
        	  return null;
        	  
          }catch(Exception e){
        	  
        	  return null;
          }
	}
}




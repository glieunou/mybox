package com.glieunou.mybox;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Import_file extends Activity {
	
	private int cle; private TextView bt,txt; private static final int CODE_ACTIVITE=3;
	
	private ProgressDialog pd;  private FTPClient client; 
	
	private ListView list; 
	
 	public static String data=""; public static String exten="";

	public ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.import_file);
		
		bt=(TextView)findViewById(R.id.textView1);
		
		list=(ListView)findViewById(R.id.listView1);
		
        // récupération de l'intent clé envoyé !!
        
        Bundle objetbunble  = this.getIntent().getExtras();
        
        final Requete req=new Requete(this); final Resources re=getResources();
        
        //On récupère les données du Bundle
        
        if (objetbunble != null && objetbunble.containsKey("key")) {
        	
               cle = Integer.parseInt(this.getIntent().getStringExtra("key"));
               
        }else {
               cle=0;
        }
     
        
      final FtpTask runner = new FtpTask();
      
      // création d'une délais de 5 secondes 
      
      Toast.makeText(Import_file.this, "Connexion en cours... ", Toast.LENGTH_LONG).show();    
      
    /*  Handler handler = new Handler();
      
      handler.postDelayed(new Runnable() {

          @Override
          public void run() {
        	  // code here
        	   
          }

      }, 5000); */
      
      
      client=new FTPClient();  
 	   
 	  // lancons l'opération 2 secondes après, le temps pour la page de s'afficher
 	  
 	  try{
 			
 		 runner.execute();
 		 
 	     runner.get();
 			 
 	    } 
 	
 	 catch(Exception e){
 			 
 			Toast.makeText(Import_file.this, "Erreur de connexion au serveur ", Toast.LENGTH_LONG).show();    		
 	 		 
 		 } 
        
 	 SimpleAdapter mSchedule = new SimpleAdapter (this.getBaseContext(), listItem, R.layout.affiche_file,
             new String[] {"img", "titre","poids", "key"}, new int[] {R.id.img, R.id.titre,R.id.poids, R.id.key});

 	 list.setAdapter(mSchedule);
 	 
 	 
 	 list.setOnItemClickListener(new OnItemClickListener(){
    	   
    	   @SuppressWarnings("unchecked")
           public void onItemClick(AdapterView<?> a, View v, int position, long id) {
    		   
               //on récupère la HashMap contenant les infos de notre item 
    		   
              HashMap<String, String> map1 = (HashMap<String, String>) list.getItemAtPosition(position);
              
             // ouverture du fichier avec l'application par défaut
              
              exten=map1.get("key"); String type="";
              
              data=map1.get("titre");
              
              FtpOpen tache=new FtpOpen();    tache.execute(data);
              
              
   	   }
       }); 
 	 
 	 
 	   bt.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			Bundle objetbundle1=new Bundle();
			
			objetbundle1.putString("key",""+cle);
            
			Intent intent=new Intent(Import_file.this,Explorer.class);
         	 
            intent.putExtras(objetbundle1);
 			
 			startActivityForResult(intent,CODE_ACTIVITE); 
			
		}
	});
 	  
	}

	
 		
  private class FtpOpen extends AsyncTask<String, Void, Boolean>{

	@Override
	protected Boolean doInBackground(String... params) {
		// TODO Auto-generated method stub
		return openFile(params[0]);
		
	}
	
	protected void onPostExecute(Boolean result) {
		
		if (pd!=null) { pd.dismiss(); }
		
		if(result){
			
			Toast.makeText(Import_file.this, "Ouverture du fichier...", Toast.LENGTH_LONG).show();
			
			Import_file.this.runOnUiThread(new Runnable(){
	    		  
	    		  @Override
                  public void run() {
	    			  
                      //update here
	    			  
	    			  final Requete req=new Requete(Import_file.this);
	    				
	    	    	  req.open();
	    	    	  
	    	    	  Ftp f=new Ftp();
	    	    	  
	    	    	  f=req.getFtp(cle);
	    	    	  
	    	    	  req.close();
	    	    	  
	    			  String file1=Environment.getExternalStorageDirectory().toString()+"/MyBox_Glieunou/"+f.GetHost()+"/"+data;
	    				
	    			  //String ext1=file1.substring(file1.lastIndexOf(".")); 
	    			  
	    			  File file = new File(file1);
	    			  
	    			  String ext1 = android.webkit.MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString());
	    			   
	                  String mimetype = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext1);
	                  
	                  Intent intent = new Intent(Intent.ACTION_VIEW);
	                  
	                  intent.setDataAndType(Uri.fromFile(file), mimetype);
	                  
	                 try {
                        
	                	  Import_file.this.startActivity(intent);
                    } 
                    catch (ActivityNotFoundException e) {
                    	
                        // Pas de Viewer
                    	Toast.makeText(Import_file.this, "Impossible d'ouvrir le fichier : "+e.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
	                   
	                  
                  }
	    		  
	    	  });
	    	  
	    	  
	    	  
              
             // Toast.makeText(Import_file.this, "Fichier télécharger : "+file1, Toast.LENGTH_LONG).show();
              
			
		} else {
			
			Toast.makeText(Import_file.this, "Impossible d'ouvrir le fichier", Toast.LENGTH_LONG).show();
		}
		
	}
	
	protected void onPreExecute() { 
	    
    	pd = new ProgressDialog(Import_file.this); 
    	
    	pd.setTitle("Ouverture..."); 
    	
    	pd.setMessage("Patientez svp."); 
    	
    	pd.setCancelable(false); 
    	
    	pd.setIndeterminate(true); 
    	
    	pd.show();
    	
    }
	  
  }
	
  private class FtpTask extends AsyncTask<Context, Void, FTPClient> {
		
	    protected FTPClient doInBackground(Context... args) {
	    
	       return getServer();
	    }

	    protected void onPostExecute(FTPClient result) {
	    	
	        //Where ftpClient is a instance variable in the main activity
	    	
	    if (pd!=null) { pd.dismiss(); }	 
	    	
	    	if(result!=null){
	    		
	    		Toast.makeText(Import_file.this, "Chargement du contenu...", Toast.LENGTH_LONG).show();
	    	}  
	    	
	    	else Toast.makeText(Import_file.this, "Impossible de joindre le serveur", Toast.LENGTH_LONG).show();
	     
	   
	    }
	    protected void onPreExecute() { 
	    
	       pd = new ProgressDialog(Import_file.this); 
	    	
	    	pd.setTitle("Connexion..."); 
	    	
	    	pd.setMessage("Patientez svp."); 
	    	
	    	pd.setCancelable(false); 
	    	
	    	pd.setIndeterminate(true); 
	    	
	    	pd.show();
	    	
	    }
	    
	}
	
    
	private FTPClient getServer(){
		
		final Requete req=new Requete(this);
  	   
    	  req.open();
    	  
    	  Ftp f=new Ftp();
    	  
    	  f=req.getFtp(cle);
    	  
    	  req.close();
  		
        FTPClient ftp=new FTPClient();
        
        int port=0;
    	  
    	  try{
    		  
    	 if(f.GetPort()==0) port=21; else port=f.GetPort();
          
          ftp.setConnectTimeout(10*1000); 
          
          ftp.connect(f.GetHost().toString(),port);
          
          if(test_ftp(f.GetHost(),port)){ ftp.login(f.GetUser().toString(), f.GetPass().toString());
          
          ftp.enterLocalPassiveMode();
          
          // création du repertoire MyBox au cas ou il n'existe pas
          
          ftp.makeDirectory("MyBox_Glieunou");
          
          // on récupère le repertoire courant
          
          ftp.changeWorkingDirectory("MyBox_Glieunou");
          
		  FTPFile[] arrayfile=ftp.listFiles();
			 
			 int taille=arrayfile.length;
			 
			 for(int i=0;i<taille;i++){
				 
				 String name=arrayfile[i].getName(); boolean isfile=arrayfile[i].isFile();
				 
				 String lien=arrayfile[i].getLink(); String poids="";
				 
				 if(isfile){
					 
					 HashMap<String, String> map; map=new HashMap<String, String>();
					 
					 map.put("titre",name); String poids_f="";
					 
				     if(arrayfile[i].getSize()<1024) poids_f=arrayfile[i].getSize()+ "o";
					 
					 else if(arrayfile[i].getSize()>=1024 && arrayfile[i].getSize()<1048576) poids_f=(int)(arrayfile[i].getSize()/1024)+" Ko";
					 
					 else if(arrayfile[i].getSize()>=1048576 && arrayfile[i].getSize()<1073741824) poids_f=(int)(arrayfile[i].getSize()/1048576)+" Mo";
					 
					 else if(arrayfile[i].getSize()>=1073741824) poids_f=(int)(arrayfile[i].getSize()/1073741824)+" Go";
					
					
				     poids=poids_f+" - "+arrayfile[i].getTimestamp().getTime();
					 
					 map.put("poids", poids);
					 
					 // ajout du fichier dans le listview après détermination de son extension
				     
					 if(name.lastIndexOf(".")>0){
						 
					     String ext=name.substring(name.lastIndexOf("."));
					 
					     if(ext.equals(".txt")) map.put("img",String.valueOf(R.drawable.txt)); 
					 
					     else if(ext.equals(".pdf")) map.put("img", String.valueOf(R.drawable.pdf));
					     
					     else if(ext.equals(".doc")||ext.equals(".docx")) map.put("img", String.valueOf(R.drawable.word)); 
					     
					     else if(ext.equals(".xls")||ext.equals(".xlsx")) map.put("img", String.valueOf(R.drawable.excel));
					     
					     else if(ext.equals(".ppt")||ext.equals(".pptx")) map.put("img", String.valueOf(R.drawable.power));
					     
					     else if(ext.equals(".jpg")||ext.equals(".png")||ext.equals(".jpeg")||ext.equals(".gif")||ext.equals(".bmp")) map.put("img", String.valueOf(R.drawable.fiche));
					     
					     else if(ext.equals(".mp3")||ext.equals(".wav")||ext.equals(".ogg")||ext.equals(".mid")||ext.equals(".midi")||ext.equals(".arm")) map.put("img", String.valueOf(R.drawable.son));
					     
					     else if(ext.equals(".mpeg")||ext.equals(".3gp")||ext.equals(".mp4")||ext.equals(".avi")) map.put("img", String.valueOf(R.drawable.video));
						 
					     else if(ext.equals(".jar")||ext.equals(".zip")||ext.equals(".rar")||ext.equals(".gz")) map.put("img", String.valueOf(R.drawable.winrar));
						 
					     else map.put("img",String.valueOf(R.drawable.fiche)); 
					     
					     map.put("key", ext);
					     
					 } else{
						
						 // au cas ou le fichier n'a pas d'extension
						  
						 map.put("img", String.valueOf(R.drawable.fiche)); map.put("key", "");
					 }
					 
					 listItem.add(map);
					 
				 }
				 
			 }
          
          
          return ftp;  }
          
          else {
        	  
              // liste des fichiers dans le dossier local !
    		  
    		  File folder;
    		  
    		  folder=new File(Environment.getExternalStorageDirectory().toString()+"/MyBox_Glieunou/"+f.GetHost());
    		  
    		  File file[] = folder.listFiles();
    		  
    		  
    		  for (int i=0; i < file.length; i++)
    		  
    		  {
    		  
    			String name=file[i].getName(); boolean isfile=file[i].isFile();
    			  
    			  int p=(int)(long)file[i].length(); 
    			  
    			  String lien=file[i].getAbsolutePath(); 
    			  
    			  if(isfile){
    				  
    				 HashMap<String, String> map; map=new HashMap<String, String>();
 					 
 					 map.put("titre",name); String poids_f=""; 
 					 
 					 
				     if(p<1024) poids_f=p+ "o";
					 
					 else if(p>=1024 && p<1048576) poids_f=(int)(p/1024)+" Ko";
					 
					 else if(p>=1048576 && p<1073741824) poids_f=(int)(p/1048576)+" Mo";
					 
					 else if(p>=1073741824) poids_f=(int)(p/1073741824)+" Go";
				     
				     Date lastModDate = new Date(file[i].lastModified());
					
				     String poids=poids_f+" - "+lastModDate.toString(); 
				     
				     map.put("poids", poids);
 					 
 					if(name.lastIndexOf(".")>0){
						 
					     String ext=name.substring(name.lastIndexOf("."));
					 
					     if(ext.equals(".txt")) map.put("img",String.valueOf(R.drawable.txt)); 
					 
					     else if(ext.equals(".pdf")) map.put("img", String.valueOf(R.drawable.pdf));
					     
					     else if(ext.equals(".doc")||ext.equals(".docx")) map.put("img", String.valueOf(R.drawable.word)); 
					     
					     else if(ext.equals(".xls")||ext.equals(".xlsx")) map.put("img", String.valueOf(R.drawable.excel));
					     
					     else if(ext.equals(".ppt")||ext.equals(".pptx")) map.put("img", String.valueOf(R.drawable.power));
					     
					     else if(ext.equals(".jpg")||ext.equals(".png")||ext.equals(".jpeg")||ext.equals(".gif")||ext.equals(".bmp")) map.put("img", String.valueOf(R.drawable.fiche));
					     
					     else if(ext.equals(".mp3")||ext.equals(".wav")||ext.equals(".ogg")||ext.equals(".mid")||ext.equals(".midi")||ext.equals(".arm")) map.put("img", String.valueOf(R.drawable.son));
					     
					     else if(ext.equals(".mpeg")||ext.equals(".3gp")||ext.equals(".mp4")||ext.equals(".avi")) map.put("img", String.valueOf(R.drawable.video));
						 
					     else if(ext.equals(".jar")||ext.equals(".zip")||ext.equals(".rar")||ext.equals(".gz")) map.put("img", String.valueOf(R.drawable.winrar));
						 
					     else map.put("img",String.valueOf(R.drawable.fiche)); 
					     
					     map.put("key", ext);
					     
					 } else{
						
						 // au cas ou le fichier n'a pas d'extension
						  
						 map.put("img", String.valueOf(R.drawable.fiche)); map.put("key", "");
					 }
					 
					 listItem.add(map);


    				  
    			  }
    			  
    			  
    		  
    		  }
    		  
    		
    		  return ftp; 
          }
          
           
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
	
	
private boolean test_ftp(String host,int port){
	
	 FTPClient ftp=new FTPClient();
      
 	  try{
 	
 	   ftp.setConnectTimeout(10*1000); 
       
       ftp.connect(host,port);
       
       if(ftp.isConnected()) return true;
    	
       else return false;
       
 	  }catch(Exception e){
    	  
    	  return false;
      }
}
	
 private boolean openFile(String fichier){
		
		final Requete req=new Requete(this);
  	   
    	  req.open();
    	  
    	  Ftp f=new Ftp();
    	  
    	  f=req.getFtp(cle);
    	  
    	  req.close();
  		
        FTPClient ftp=new FTPClient();
        
        int port=0; 
    	  
    	  try{
          
          if(f.GetPort()==0) port=21; else port=f.GetPort();
          
          ftp.setConnectTimeout(10*1000); 
          
          ftp.connect(f.GetHost().toString(),port);
          
          if(ftp.isConnected()){ ftp.login(f.GetUser().toString(), f.GetPass().toString());
          
          ftp.enterLocalPassiveMode();
          
          // création du repertoire MyBox au cas ou il n'existe pas
          
          ftp.makeDirectory("MyBox_Glieunou");
          
          // on récupère le repertoire courant) 
			  
	      ftp.changeWorkingDirectory("MyBox_Glieunou");
          
		  File folder;
		  
		  folder=new File(Environment.getExternalStorageDirectory().toString()+"/MyBox_Glieunou/"+f.GetHost());
		  
		  if(!folder.exists()) folder.mkdirs();
		  
		  folder.setWritable(true);
		  
		  File fich=new File(Environment.getExternalStorageDirectory().toString()+"/MyBox_Glieunou/"+f.GetHost()+"/"+fichier);
		  
		  BufferedOutputStream fos = null;
		  
		  boolean ret=false;
			
		  String current_folder="/MyBox_Glieunou/"+fichier;
		     
             FTPFile[] arrayfile=ftp.listFiles();
			 
			 int taille=arrayfile.length; long p=0;
			 
			 for(int i=0;i<taille;i++){
				 
				 String name=arrayfile[i].getName();
				 
				if(name.equals(fichier)) p=arrayfile[i].getSize();
				 
			 }
			 
			  // enregistrement du fichier dans  la mémoire
			  
			  FileOutputStream desFileStream = new FileOutputStream(fich.getAbsolutePath());
			  
			  fos = new BufferedOutputStream(desFileStream);
			   
			  
			  //File fich1;
			 
			  //do{ 
				  
			   ftp.setFileType(FTP.BINARY_FILE_TYPE);
				  
			   ret=ftp.retrieveFile(current_folder, fos); 
			   
			   //fich1=new File(Environment.getExternalStorageDirectory().toString()+"/MyBox_Glieunou/"+f.GetHost()+"/"+fichier);
			 	  
			 // } while(fich1.length()!=p && p!=0);
			  
			  
			  desFileStream.close();
			  
			  ret=true;
			  
		  
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

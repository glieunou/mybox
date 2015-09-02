package com.glieunou.mybox;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends FragmentActivity  {

	private TextView bt; private static final int CODE_ACTIVITE=1;
	
    private ListView listev; 
	
	private ArrayList<Ftp> listb; 
	
	protected ActionMode mActionMode;

	public int selectedItem = -1;

	private ArrayList<String> listItems;
	
	private Cursor cursor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		bt=(TextView) findViewById(R.id.textView1);
		
		bt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
                Intent intent=new Intent(MainActivity.this,New_ftp.class);
				
				startActivityForResult(intent,CODE_ACTIVITE);
				
			}
		});
		
		
       listev=(ListView)findViewById(R.id.listView1);
        
        listb=new ArrayList<Ftp>();
        
        listItems=new ArrayList<String>(); 
        
        final Requete req=new Requete(this); 
        
        req.open();
        
        cursor=req.getAllFTP(); cursor.moveToFirst();
        
        MyAdapter myAdapter = new MyAdapter(this, cursor); 
        
        listev.setAdapter(myAdapter);
        
        listev.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
        listev.setOnItemClickListener(new OnItemClickListener(){
     	   
     	   @SuppressWarnings("unchecked")
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
     		
     		  MyAdapter adapter = ( MyAdapter) listev.getAdapter();
     		   
     	        adapter.toggleSelection(position);
     	        
     	        Ftp f=new Ftp(); 
     	        
     	        req.open();
     	        
     	        f=req.getFtp(adapter.currentID(position));
     	        
     	        if(f.GetStatus()==1) { showNoticeDialog1(adapter.currentID(position)); }
     	        
     	        else { showNoticeDialog(adapter.currentID(position)); } 
     	        
     	        req.close();

    	   }
        }); 
        
        
        listev.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
        	 
        	   public boolean onItemLongClick (AdapterView parent, View view, int position, long id) {
        		  
  	        onListItemCheck(position); 
  	        
        	    return true;
          
        	   }
        	});
           
           
         
         req.close();
        
	}

	
	protected void onListItemClick(ListView l, View v, int position, long id) {	
	    
		if(mActionMode == null) {
        
			// no items selected, so perform item click actions
			
			listItems.clear();
        
		} else
            
			// add or remove selection for current list item
           
			onListItemCheck(position);	
       
	}	
    
	private void onListItemCheck(int position) {
		
	    MyAdapter adapter = ( MyAdapter) listev.getAdapter();
   
        adapter.toggleSelection(position);
        
        boolean hasCheckedItems = adapter.getSelectedCount() > 0;

        if (hasCheckedItems && mActionMode == null){
        
        	// there are some selected items, start the actionMode
            mActionMode = startActionMode(modeCallBack); 
           
            listItems.add(""+adapter.currentID(position)); 
        
        }
        
        else if (!hasCheckedItems && mActionMode != null) {
        
        	// there no selected items, finish the actionMode
            mActionMode.finish();  mActionMode=null;
           
            listItems.clear();
        
        }
        
        else if (hasCheckedItems && mActionMode != null) {
        	
        	if(adapter.todo(position)) { 
        		
        		listItems.add(""+adapter.currentID(position)); 
        	
        	}
        	
        	else { 
        		
        		for(int i=0;i<listItems.size();i++){
        		
        			if(listItems.get(i).equals(""+adapter.currentID(position))) listItems.remove(i);
        			
        		}
        		
        		}
        }
      
        
        if(mActionMode != null) mActionMode.setTitle(String.valueOf(adapter.getSelectedCount()) + " selected");
        
        
    }
	
	 
		public void showNoticeDialog(int cle) {
	    	
	        // Create an instance of the dialog fragment and show it
	    	
	        Fenetre dialog = new Fenetre(cle);
	    
	        FragmentManager manager=getSupportFragmentManager();
	        
	        dialog.show(manager, "NoticeDialogFragment");	
	        
	        
	    }
		
	    public void showNoticeDialog1(int cle) {
	    	
	        // Create an instance of the dialog fragment and show it
	    	
	        Fenetre3 dialog = new Fenetre3(cle);
	    
	        FragmentManager manager=getSupportFragmentManager();
	        
	        dialog.show(manager, "NoticeDialogFragment");	
	        
	        
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
	        
	        case R.id.action_settings: {
		    	 
	        	Intent intent=new Intent(MainActivity.this,Setting.class);
	     			
	     	    startActivityForResult(intent,CODE_ACTIVITE); 
		      
 		    	 return true;
 		             
 		            }
 		     
 		     case R.id.action_home:{
 		    	 
 		    	Intent intent=new Intent(MainActivity.this,MainActivity.class);
 	 			
	    		startActivityForResult(intent,CODE_ACTIVITE); 
 	            
 	            	return true; }
 	            	
 		     case R.id.action_refresh: {
 		    	 
 		    	Intent intent=new Intent(MainActivity.this,MainActivity.class);
 	 			
	    		startActivityForResult(intent,CODE_ACTIVITE); 
 		        
 	               return true; }
	                
	            default:
	                return super.onOptionsItemSelected(item);
	        }
	    }

	
	
	
	private ActionMode.Callback modeCallBack = new ActionMode.Callback() {

 	   public boolean onPrepareActionMode(ActionMode mode, Menu menu)   { 
 	    return false;
 	   }

 	  public void onDestroyActionMode(ActionMode mode) {
 		  
 		//home_unsel();
 		  
 	    mode = null;   
 	    
 	   }

 	   public boolean onCreateActionMode(ActionMode mode, Menu menu) {
 		   mode.setTitle("Options");
 		   mode.getMenuInflater().inflate(R.menu.main, menu);
 		   return true;
 	   }

 	   public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
 		   
 		   int id = item.getItemId();
 		   
 		   switch (id) {
 		   
 		 case R.id.action_settings: {
 		    	 
 		    	setting(); mode.finish();
		      
 		    	 return true;
 		             
 		            }
 		     
 		     case R.id.action_home:
 		    	 
 	            home();
 	            
 	            	return true;
 	            	
 		     case R.id.action_refresh:
 		    	 
 		    	  refresh(); mode.finish();
 		        
 	               return true;
 	                
 	            case R.id.action_protect:
 	               
 	            	pro_sel(); mode.finish();
 	            	
 	            	return true; 
 	             
 	            case R.id.action_del:
 	                
 	            	del_sel(); mode.finish();
 	            	
 	            	return true;   	
 	        
 	            default:
 		        	   
 	    		  mode.finish();
 	    		    	 
 		          return false;
 	   }
 	   
 	   }
 	};  
 
 	 public void setting(){
     	
 		Intent intent=new Intent(MainActivity.this,Setting.class);
 			
 	    			startActivityForResult(intent,CODE_ACTIVITE); 
 	    }
 	
 	 public void home(){
     	
 		Intent intent=new Intent(MainActivity.this,MainActivity.class);
 			
 	    			startActivityForResult(intent,CODE_ACTIVITE); 
 	    }
       
 	 public void refresh(){
     	
 		Intent intent=new Intent(MainActivity.this,MainActivity.class);
 			
 	    			startActivityForResult(intent,CODE_ACTIVITE); 
 	    }
 	 
 	public void pro_sel(){
	  	
 		  if(listItems.isEmpty()){
 			  
 		         Toast.makeText(MainActivity.this, "Sélection vide", Toast.LENGTH_LONG).show();		
 				  
 			  } else {
 				  

 				  final Requete req1=new Requete(MainActivity.this);
 				  
 				  req1.open();
 				  
 				  for(int i=0;i<listItems.size();i++) {
 					  
 					  Ftp f=new Ftp();
 					  
 					  f=req1.getFtp(Integer.parseInt(listItems.get(i)));
 					  
 					  if(f.GetStatus()==0) f.SetStatus(1); else f.SetStatus(0);
 					  
 					  req1.updateFTP(f, Integer.parseInt(listItems.get(i)));
 					  
 				  } 
 				  
 				  req1.close();
 				  
 				  Bundle objetbundle=new Bundle();
 				  
 				  Intent intent=new Intent(MainActivity.this,MainActivity.class);

 		          objetbundle.putString("ret", "1");
 		         	 
 		          intent.putExtras(objetbundle);
 					
 				  startActivityForResult(intent,CODE_ACTIVITE); 
 				  
 			  }
 		  
 	  }

 	 public void del_sel(){
 		  
 		  
 		  // création de l'alert dialog pour demander à l'utilisateur de confirmer son action
 		  
 		  AlertDialog.Builder alert=new AlertDialog.Builder(MainActivity.this);
 		  
 		  final Resources re1=getResources();
 		  
 		  alert.setTitle("Suppression");
 		  
 		  alert.setMessage("Confirmer vous votre choix ?");
 		  
 		  alert.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
 			
 			@Override
 			public void onClick(DialogInterface dialog, int which) {
 				// TODO Auto-generated method stub
 				
 				if(listItems.isEmpty()){
 					  
 			         Toast.makeText(MainActivity.this, "" +listItems.size() + " elements", Toast.LENGTH_LONG).show();		
 					  
 				  } else {
 					  

 					  final Requete req1=new Requete(MainActivity.this);
 					  
 					  req1.open();
 					  
 					  for(int i=0;i<listItems.size();i++) {
 						  
 						// on ne supprime que les bourses non protégés par un mot de passe  
 						  
 						Ftp f=new Ftp(); f=req1.getFtp(Integer.parseInt(listItems.get(i)));  
 						  
 						if(f.GetStatus()==0) req1.RemoveFTP(Integer.parseInt(listItems.get(i)));
 						  
 					  } 
 					  
 					  req1.close();
 					  
 					  Bundle objetbundle=new Bundle();
 					  
 					  Intent intent=new Intent(MainActivity.this,MainActivity.class);

 			          objetbundle.putString("ret", "1");
 			         	 
 			          intent.putExtras(objetbundle);
 						
 					  startActivityForResult(intent,CODE_ACTIVITE); 
 					  
 				  }
 				  	  
 				
 			}
 		  });
 		  
 		  alert.setNegativeButton("Non", new DialogInterface.OnClickListener() {
 			
 			@Override
 			public void onClick(DialogInterface dialog, int which) {
 				// TODO Auto-generated method stub
 				
 				Toast.makeText(MainActivity.this,"Opération annuléé", Toast.LENGTH_LONG).show();		
 				  
 				
 			}
 		});
 		  
 		AlertDialog dialog = alert.create();   dialog.show();
 	 
 	 }	  
 		  	
 
	// déclaration d'un adapter customizer
	 
	 public class MyAdapter extends BaseAdapter {

		 private Cursor cursor;
	 	 private Context context;
	 	 
	 	private SparseBooleanArray mSelectedItemsIds;

		 public MyAdapter(Context context, Cursor cursor) {
		    
			 super();
			 
		     this.context = context;
		     
		     this.cursor=cursor;
		     
		     mSelectedItemsIds = new SparseBooleanArray();
		 }

			 
		 public void toggleSelection(int position)
	     {
	         selectView(position, !mSelectedItemsIds.get(position));
	     }

	     public void removeSelection() {
	     
	    	 mSelectedItemsIds = new SparseBooleanArray();
	         
	    	 notifyDataSetChanged();
	     }

	     public void selectView(int position, boolean value)
	     {
	         if(value) { mSelectedItemsIds.put(position, value);   }
	         
	         else { mSelectedItemsIds.delete(position);  }
	         
	         notifyDataSetChanged();
	     }
	     
	     public int getSelectedCount() {
	         return mSelectedItemsIds.size();// mSelectedCount;
	     }
	     
	     
	     
	     public SparseBooleanArray getSelectedIds() {
	     
	    	 return mSelectedItemsIds;
	     
	     }
		 
		 
		 @Override
		 public int getCount() {
		     
			 return cursor.getCount();
		 }

		 @Override
		 public Object getItem(int position) {
		     
			 return position;
		 }

		 @Override
		 public long getItemId(int position) {
		 
			 return position;
		 }
		 

		 public int currentID(int position){
			 
			 cursor.moveToPosition(position);
			 
			 return Integer.parseInt(cursor.getString(cursor.getColumnIndex("f_id")));
			 
		 }
		 
		 public boolean todo(int position){
			 
			return mSelectedItemsIds.get(position);
		 }
		 
		 
		 @Override
		 public View getView(int position, View convertView, ViewGroup parent) {
			 
			 LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			 
		     View rowView = inflater.inflate(R.layout.affiche_ftp, parent, false);
				    
		     TextView text1 = (TextView) rowView.findViewById(R.id.titre);
		     TextView text2 = (TextView) rowView.findViewById(R.id.key);
		     ImageView img =  (ImageView) rowView.findViewById(R.id.img);
		     
		     cursor.moveToPosition(position);
		     
		     String et="";  final Resources re1=getResources();

		     text1.setText(cursor.getString(cursor.getColumnIndex("f_nom")));
		     text2.setText(""+cursor.getString(cursor.getColumnIndex("f_id")));
		   
		     int protect=cursor.getInt(cursor.getColumnIndex("f_status"));
			    
			 if(protect==1) { img.setVisibility(View.VISIBLE); img.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_secure)); }
			    
			 else {  img.setVisibility(View.VISIBLE); img.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_partial_secure)); }
			 
		      // icon.setImageResource(R.drawable.ic_launcher);
		     
		    // ((TextView) rowView).setText((CharSequence) getItem(position));

		   rowView.setBackgroundColor(mSelectedItemsIds.get(position)? 0x9934B5E4: Color.TRANSPARENT);
		      
		     
		     return rowView;
		 }

		 
		 }
	
}

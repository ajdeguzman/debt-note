package com.ajdeguzman.debtnote;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.widget.Toast;

public class ClassImportExportDB {
			Context ctx;
			public static final String PACKAGE_NAME = "com.ajdeguzman.debtnote";
			public static final String DATABASE_NAME = "dbdebt";
			int DEBT_FILE_SELECTED;
		public ClassImportExportDB(Context ctx){
			this.ctx = ctx;
		}
		public void importDB() {
	        try {
		            File sd = Environment.getExternalStorageDirectory();
			        final File directory = new File(sd + "/Debt Note");
			        File file[] = directory.listFiles();
		            final File data = Environment.getDataDirectory();
		                if (sd.canWrite()) {
			                final String backupDBPath = "export.debts";
		                	File temp = new File(directory + File.separator + backupDBPath);
			              if(temp.exists()){
			            	  
			            	  	AlertDialog.Builder bldr = new AlertDialog.Builder(ctx);
				      			bldr.setTitle("Restore Debt");
				      			bldr.setMessage("This will replace the existing debt file. Are you sure you want to restore debt?");
				      			bldr.setCancelable(true);
				      			bldr.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				      				@Override
				      				public void onClick(DialogInterface dialog, int which) {
				      					dialog.cancel();
				      				}
				      			});
				      			bldr.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				      				@Override
				      				public void onClick(DialogInterface dialog, int which) {
				      					String currentDBPath = "//data//" + PACKAGE_NAME + "//databases//" + DATABASE_NAME;
						                File backupDB = new File(data, currentDBPath);
						                File currentDB = new File(directory, backupDBPath);
						
							            FileChannel src;
										try {
											src = new FileInputStream(currentDB).getChannel();
								            FileChannel dst = new FileOutputStream(backupDB).getChannel();
								            dst.transferFrom(src, 0, src.size());
								            src.close();
								            dst.close();
										} catch (FileNotFoundException e) {
											e.printStackTrace();
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
							            Toast.makeText(ctx, "Debt Successfully Restored", Toast.LENGTH_LONG).show();
				      				}
				      			});
				      			final AlertDialog alt = bldr.create();
				      			alt.show(); 
			              }else{
			            	  displayMessage("Restore Failed", "No existing debt file to restore");
			              }
		        }
		    } catch (Exception e) {
		    	displayMessage("Restore Failed", e.getMessage().toString());
		    }
	}
	
	public void exportDB() {
	    try {
	        File sd = Environment.getExternalStorageDirectory();
	        File directory = new File(sd + "/Debt Note");
	        File data = Environment.getDataDirectory();
	
	        if (sd.canWrite()) {
	        	directory.mkdirs();
	            String currentDBPath = "//data//" + PACKAGE_NAME + "//databases//" + DATABASE_NAME;
	            String backupDBPath = "export.debts";
	            File currentDB = new File(data, currentDBPath);
	            File backupDB = new File(directory, backupDBPath);
	
	            FileChannel src = new FileInputStream(currentDB).getChannel();
	            FileChannel dst = new FileOutputStream(backupDB).getChannel();
	            dst.transferFrom(src, 0, src.size());
	            src.close();
	            dst.close();
	            Toast.makeText(ctx, "Debt Successfully Backed Up to: " + directory, Toast.LENGTH_LONG).show();
	        }
	    } catch (Exception e) {
	    	displayMessage("Backup Failed", e.getMessage().toString());
	    }
	}
	
	private void displayMessage(String title, String message) {
		
		AlertDialog.Builder bldr = new AlertDialog.Builder(ctx);
		bldr.setTitle(title);
		bldr.setCancelable(true);bldr.setMessage(message);
		bldr.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		final AlertDialog alt = bldr.create();
		alt.show();
	}
}

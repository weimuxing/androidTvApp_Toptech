package mstar.tvsetting.factory.until;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import mstar.tvsetting.factory.ui.designmenu.DesignMenuActivity;

public class CopyFileTask extends AsyncTask<File, Integer, Boolean> {
	private DesignMenuActivity designMenuActivity;
	private ProgressDialog progressDialog;

	public CopyFileTask(DesignMenuActivity designMenuActivity) {
		this.designMenuActivity = designMenuActivity;
	}

	@Override
	protected Boolean doInBackground(File... params) {
		// TODO Auto-generated method stub
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			inputStream = new FileInputStream(params[0]);
			outputStream = new FileOutputStream(params[1]);
			byte[] b = new byte[1024];
			int byteRead = 0;
			while((byteRead = inputStream.read(b)) !=-1){
				outputStream.write(b,0,byteRead);
			}
		} catch (FileNotFoundException e) {
			Log.d("chenl", "40");
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			Log.d("chenl", "43");
			e.printStackTrace();
			return false;
		}finally {
			try {
				if(inputStream != null){
					inputStream.close();
				}
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}

	@Override
	protected void onPreExecute() {
		progressDialog = new ProgressDialog(designMenuActivity);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage("Copying,please do not pull out the U disk.....");
		progressDialog.show();
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		if (result == true) {
			progressDialog.dismiss();
		}else{
			Toast.makeText(designMenuActivity, "failed", 0).show();
			progressDialog.dismiss();
		}
		
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

}

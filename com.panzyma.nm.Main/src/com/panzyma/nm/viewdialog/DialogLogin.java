package com.panzyma.nm.viewdialog;
  
import java.lang.reflect.Type;

import org.ksoap2.serialization.PropertyInfo;

import com.panzyma.nm.auxiliar.NMComunicacion;
import com.panzyma.nm.auxiliar.NMConfig;
import com.panzyma.nm.auxiliar.NMTranslate;
import com.panzyma.nm.auxiliar.Parameters;
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.controller.Controller;
import com.panzyma.nm.serviceproxy.Cliente;
import com.panzyma.nm.view.ViewConfiguracion;
import com.panzyma.nordismobile.R;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;   
import android.content.Intent;
import android.os.Handler;
import android.os.Message; 
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

@SuppressWarnings({"rawtypes","unused"})
public class DialogLogin extends Dialog  implements Handler.Callback
{
	private EditText txtenterprise;
	private EditText txtusername;
	private EditText txtpassword;
	private CheckBox chksetting;
	private Button signin; 
	private Button cancel;  
	private Controller controller;
	private OnButtonClickListener mButtonClickListener;
	private String TAG=DialogLogin.class.getSimpleName();
	private Context mycontext;	 
	private boolean admin;
	public interface OnButtonClickListener {
		public abstract void onButtonClick(boolean btn);
	}
	
    public void setOnDialogLoginButtonClickListener(OnButtonClickListener listener) {
		mButtonClickListener = listener;
	} 
 
	public DialogLogin(Context context,boolean _admin) 
	{
		super(context,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen); 
		try
		{ 
			mycontext=context;
			admin=_admin;
		    setContentView(R.layout.login);
			initComponents(); 
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void clean()
	{
		txtenterprise.setText("");
		txtusername.setText("");
		txtpassword.setText("");
	}

	public  String getNameUser()
	{ 
		return txtusername.getText().toString();
	} 
	
	public  String getPassword()
	{
		return txtpassword.getText().toString();
	} 
	
	public  String getEmpresa()
	{
		return txtenterprise.getText().toString();
	} 
	 
	public void setContentViewToDialog(int layout)
	{
		setContentView(layout); 
	 
	}
	
	public void initComponents()
	{	      
		this.setCancelable(true); 
	    this.setCanceledOnTouchOutside(true); 
	    
	    signin=((Button)findViewById(R.id.btnsignin)); 
	    cancel=((Button)findViewById(R.id.btncancel)); 
	    txtenterprise=((EditText)findViewById(R.id.etenterprise));  
	    txtusername=((EditText)findViewById(R.id.etusername)); 
	    txtpassword=((EditText)findViewById(R.id.etpassword));  
	    chksetting=((CheckBox)findViewById(R.id.chksetting)); 
	    if(admin){
	    	((TextView)findViewById(R.id.tvsign)).setText("Inicio de Session como Administrator");
		    txtenterprise.setEnabled(false);
		    txtusername.setEnabled(false); 
		    txtpassword.requestFocus();
	    }
	    
	    signin.setOnClickListener(new View.OnClickListener() 
		{ 
    	    @Override
			public void onClick(View v) 
			{   
	    		if(isValidInformation())
    	    	{     	    	 
			 	    try 
			 	    { 		 
			 	    	mButtonClickListener.onButtonClick(true);
			 	    } catch (Exception e) { 
						e.printStackTrace();
					}
				} 
			} 
		}
	    );
	    cancel.setOnClickListener(new View.OnClickListener() 
		{				
			@Override
			public void onClick(View v) 
			{ 
				mButtonClickListener.onButtonClick(false);
				FINISH_ACTIVITY();
			} 
		}
	    );
	    
	    
	} 
	
	public boolean isValidInformation()
	{
		String msg = "";
		
        if (txtenterprise.getText().toString().trim().length()==0){
        		msg = "Ingrese el nombre de la Empresa en la cual labora.";
        		txtenterprise.setError(msg);
        		txtenterprise.requestFocus();
        		return false;
        }
        else if (txtusername.getText().toString().trim().length()==0){
                msg = "Ingrese un usuario válido.";
                txtusername.setError(msg);
                txtusername.requestFocus();
                return false;
	    }
        else if (txtpassword.getText().toString().trim().length()==0){
                msg = "La contraseña invalida";     
                txtpassword.setError(msg);
                txtpassword.requestFocus();
                return false;
        }  
		return true;
	}
	
	@Override
	public boolean handleMessage(Message arg0) 
	{ 
		return false;
	}

	private void FINISH_ACTIVITY()
	{  
		Log.d(TAG, "Activity quitting"); 
		this.dismiss();
	}  
	
	
}

package com.panzyma.nm;
 
import java.util.List;

import com.panzyma.nm.auxiliar.CustomDialog; 
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.NotificationMessage;
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.controller.Controller;
import com.panzyma.nm.view.ViewCliente;
import com.panzyma.nm.view.ViewConfiguracion;
import com.panzyma.nm.view.ViewPedidoEdit;
import com.panzyma.nm.view.ViewProducto; 
import com.panzyma.nm.viewdialog.DialogLogin; 
import com.panzyma.nordismobile.R;
import static com.panzyma.nm.controller.ControllerProtocol.ALERT_DIALOG;
import static com.panzyma.nm.controller.ControllerProtocol.NOTIFICATION;
import static com.panzyma.nm.controller.ControllerProtocol.ERROR;

import android.annotation.SuppressLint; 
import android.content.Context;
import android.content.Intent; 
import android.os.Bundle; 
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View; 
import android.view.WindowManager;  
import android.widget.Toast;

@SuppressLint("ShowToast")@SuppressWarnings("rawtypes")
public class Main extends DashBoardActivity implements Handler.Callback{
  
 
	Display display;
	Context context; 
	public Controller controller;
	CustomDialog cd;
	String TAG=Main.class.getSimpleName();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        setHeader(getString(R.string.HomeActivityTitle), false, true);
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        display = wm.getDefaultDisplay();
        context=this;
        controller=new Controller();
        controller.addOutboxHandler(new Handler(this));         
        if((savedInstanceState!=null)?savedInstanceState.getBoolean("dl_visible"):false)
        	callDialogLogin();
    }
    
    @Override
	protected void onDestroy() {	 
		super.onDestroy();
		if(SessionManager.dl!=null) 
			SessionManager.dl.dismiss();
		FINISH_COMPONENT();
	}
    
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) 
    {
	      if (keyCode == KeyEvent.KEYCODE_BACK) 
	      {        	
	    	  	FINISH_ACTIVITY();
	            return true;
	      }
        return super.onKeyUp(keyCode, event); 
    } 

    
	@Override
	protected void onSaveInstanceState(Bundle bundle) 
	{ 
		super.onSaveInstanceState(bundle);
		
		if(SessionManager.dl!=null) 
			if(SessionManager.dl.isShowing())
				bundle.putBoolean("dl_visible",true); 
		else
			bundle.putBoolean("dl_visible",false);  
		
		//FINISH_COMPONENT();
	}
    
    /**
     * Button click handler on Main activity
     * @param v
     */
    public void onButtonClicker(View v)
    {
    	final Intent intent;
    	
    	switch (v.getId()) 
    	{
    	
	    	case R.id.hbtnpedido:
						    		intent = new Intent(this,ViewPedidoEdit.class); 
									startActivity(intent);
									break;
	    	case R.id.hbtnrecibocollector: 
									break;
	    	case R.id.hbtndevolucion: 
									break;
			case R.id.hbtncliente:
									intent = new Intent(this, ViewCliente.class);
									startActivity(intent);
									break;
			case R.id.hbtnproducto:
									intent = new Intent(this, ViewProducto.class);
									startActivity(intent);
									break;
				
			case R.id.hbtnconfiguracion:			
									 boolean pedirLogin = true; 
								       if (pedirLogin) callDialogLogin(); break;	
			default:
									break;
		}
    }

    @SuppressLint("ParserError")
	public void callDialogLogin()
    {
    	
    	Thread t = new Thread(new Runnable()
    	{ 
			public void run()
	        {
				try
	            { 
					SessionManager.setContext(Main.this,controller);
					if(SessionManager.SignIn(false))  
					{ 
						FINISH_COMPONENT();
						Intent intent = new Intent(context, ViewConfiguracion.class);
						intent.putExtra("isEditActive", SessionManager.isAdmin());
						startActivity(intent);  
					}
					else
						Log.d(TAG, "Error in login"); 
					
				}
	            catch (Exception e)
	            {
	                e.printStackTrace();
	                dialog("",SessionManager.getErrorAuntentication(),ALERT_DIALOG);
	            } 
	
	         }
	
	    });
	     t.start();
    } 
    
	public Toast ToastMessage(String msg,int duration)
	{
		Toast toast= Toast.makeText(this,msg,duration);  
		toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0); 
		return toast;
	}	  
	
	public  CustomDialog  dialog(final String tittle,final String msg,final int type)
	{		
       return new CustomDialog(context,tittle,msg,false,type); 
	}

    @Override
	public boolean handleMessage(Message msg) 
	{	
		
		if(cd!=null && cd.isShowing())
					cd.dismiss();	
		switch (msg.what) 
		{	 
			case NOTIFICATION:				
				NotificationMessage message=((NotificationMessage)msg.obj);
				(cd=dialog(message.getTittle(),message.getMessage()+message.getCause(),ALERT_DIALOG)).show();
				return true;
			case ERROR: 
				ErrorMessage error=((ErrorMessage)msg.obj);
				(cd=dialog(error.getTittle(),error.getMessage()+error.getCause(),ALERT_DIALOG)).show();
				return true;	
					
		}
		return false;
	}
    @SuppressWarnings("unchecked")
	private void FINISH_ACTIVITY()
	{
		List<Handler> handlers=controller.getoutboxHandlers();
		
		for (Handler handler :handlers)  
			  controller.removeOutboxHandler(handler); 
		controller.dispose();
		if(cd!=null && cd.isShowing())
			cd.dismiss();	
		Log.d(TAG, "Activity quitting");
		finish();
	}  
    
    @SuppressWarnings("unchecked")
	private void FINISH_COMPONENT()
	{
		List<Handler> handlers=controller.getoutboxHandlers();
		for (Handler handler :handlers)  
			  controller.removeOutboxHandler(handler);  
		controller.dispose();
		if(SessionManager.getController()!=null)
			SessionManager.setController(controller); 
		
		
		if(cd!=null && cd.isShowing())
			cd.dismiss();	 
	}  
    
}
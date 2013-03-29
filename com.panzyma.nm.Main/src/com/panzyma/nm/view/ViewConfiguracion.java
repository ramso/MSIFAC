package com.panzyma.nm.view;
 
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static com.panzyma.nm.controller.ControllerProtocol.LOAD_DATA;
import static com.panzyma.nm.controller.ControllerProtocol.ALERT_DIALOG;
import static com.panzyma.nm.controller.ControllerProtocol.C_DATA;
import static com.panzyma.nm.controller.ControllerProtocol.C_UPDATE_FINISHED;
import static com.panzyma.nm.controller.ControllerProtocol.C_UPDATE_IN_PROGRESS;
import static com.panzyma.nm.controller.ControllerProtocol.C_UPDATE_STARTED;
import static com.panzyma.nm.controller.ControllerProtocol.ERROR;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_DATA_FROM_LOCALHOST;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_DATA_FROM_SERVER;
import static com.panzyma.nm.controller.ControllerProtocol.ID_SINCRONIZE_PARAMETROS;
import static com.panzyma.nm.controller.ControllerProtocol.ID_SALVAR_CONFIGURACION;
import static com.panzyma.nm.controller.ControllerProtocol.ID_SINCRONIZE_CATALOGOSBASICOS;
import static com.panzyma.nm.controller.ControllerProtocol.ID_SINCRONIZE_CLIENTES;
import static com.panzyma.nm.controller.ControllerProtocol.ID_SINCRONIZE_PRODUCTOS;
import static com.panzyma.nm.controller.ControllerProtocol.ID_SINCRONIZE_PROMOCIONES;
import static com.panzyma.nm.controller.ControllerProtocol.ID_SINCRONIZE_TODOS;
import static com.panzyma.nm.controller.ControllerProtocol.ID_CERRAR;

import com.panzyma.nm.DashBoardActivity;
import com.panzyma.nm.Main;
import com.panzyma.nm.CBridgeM.BConfiguracionM;
import com.panzyma.nm.auxiliar.CustomDialog;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.NMNetWork;
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.auxiliar.ThreadPool;
import com.panzyma.nm.controller.Controller;
import com.panzyma.nm.menu.ActionItem;
import com.panzyma.nm.menu.QuickAction;
import com.panzyma.nm.serviceproxy.Usuario;
import com.panzyma.nm.viewmodel.vmConfiguracion;
import com.panzyma.nordismobile.R;

@SuppressLint("ShowToast")
@SuppressWarnings({"unchecked","rawtypes"})
public class ViewConfiguracion extends DashBoardActivity implements Handler.Callback 
{
	
	String TAG=ViewConfiguracion.class.getSimpleName();
	QuickAction quickAction; 
	Controller controller;
	Display display;
	ProgressDialog pd;
	Context context;  
	boolean isEditActive;
	private EditText txtURL;
	private EditText txtEmpresa;
	private EditText txtUsuario; 
	private EditText txtDispositivoID; 
	private String user_name;
	private String passwd;
	private String enterprise;
	private String url_server;
	private String deviceid;
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{ 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_configuracion);
		setHeader(getString(R.string.ConfiguracionActivityTitle),true, false); 
		isEditActive=getIntent().getExtras().getBoolean("isEditActive");
		 try 
		    { 		
			 	context=this;
			    controller = new Controller(new BConfiguracionM(),this); 
				controller.addOutboxHandler(new Handler(this)); 
				controller.getInboxHandler().sendEmptyMessage(LOAD_DATA); 
				SessionManager.setContext(ViewConfiguracion.this,controller); 
				WindowManager wm = (WindowManager) this.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
	            display = wm.getDefaultDisplay(); 
				pd = ProgressDialog.show(this, "Espere por favor", "Trayendo Info...", true, false);  		       
		        initComponents(); 
		       
		        
			}catch (Exception e) { 
				e.printStackTrace();
				dialog("Error Message",e.getMessage()+"\n Cause:"+e.getCause(),ALERT_DIALOG).show();
			}	
	} 
	
	public String getUserName(){
		return txtUsuario.getText().toString().trim();
	}
	
	public void setUserName(String user){
		this.user_name=user;
	}
	
	public void setPasswd(String passwd){
		this.passwd=passwd;
	}
	
	public void setEnterprise(String enterprise){
		this.enterprise=enterprise;
	}
	public String getUrlServer(){
		return this.url_server;
	}
	public void setUrlServer(String url_server){
		this.url_server=url_server;
	}
	public void setDeviceId(String deviceid){
		this.deviceid=deviceid;
	}
	@Override
	public boolean handleMessage(final Message msg) {
		Log.d(TAG, "Received message: " + msg);
		 
		switch (msg.what) 
		{		 
			case C_DATA:  setData((vmConfiguracion)msg.obj);return true;
			
			case C_UPDATE_STARTED:
		    case C_UPDATE_IN_PROGRESS: 
									pd.dismiss();
									pd = ProgressDialog.show(context,"",msg.obj.toString(), true, false);  	
									return true; 
			
			case C_UPDATE_FINISHED: pd.dismiss();  
									buildToastMessage(msg.obj.toString(), Toast.LENGTH_LONG).show();	
									Log.d(TAG, "C_UPDATE_FINISHED: " + msg.obj.toString());
									return true;
			case ERROR:
									pd.dismiss();
									ErrorMessage error=((ErrorMessage)msg.obj);
									dialog(error.getTittle(),error.getMessage()+error.getCause(),ALERT_DIALOG).show();				 
				return true;	
									
		}
		return false;
	}
	
	private void setData(vmConfiguracion setting)
	{
		txtURL.setText(setting.getAppServerURL());
		txtEmpresa.setText(setting.getEnterprise());
		txtUsuario.setText(setting.getNameUser());
		txtDispositivoID.setText(setting.getDeviceId());
		
		this.setUrlServer(setting.getAppServerURL()); 
		this.setDeviceId(setting.getEnterprise());
		this.setUserName(setting.getNameUser());
		this.setPasswd(setting.getPassword());
	}
		 
	@Override
    public boolean onKeyUp(int keyCode, KeyEvent event) 
    {
        if (keyCode == KeyEvent.KEYCODE_MENU) 
        {        	
        	Button Menu = (Button)findViewById(R.id.btn_menu);
        	quickAction.show(Menu,display,true);
            return true;
        }
        return super.onKeyUp(keyCode, event); 
    } 
	
	private void initComponents()
	{   
		initMenu();
	    txtURL=(EditText)findViewById(R.id.cfgtextv_detalleurlws);
		txtEmpresa=(EditText)findViewById(R.id.cfgtextv_detallecodempresa);
	    txtUsuario=(EditText)findViewById(R.id.cfgtextv_detalleuser); 
	    txtDispositivoID=(EditText)findViewById(R.id.cfgtextv_detalledeviceid);
	    
	    txtURL.setEnabled(isEditActive);
	    txtEmpresa.setEnabled(isEditActive);
	    txtUsuario.setEnabled(isEditActive); 
	    
	    pd.dismiss();
	}
	
	public void initMenu()
    {  
        quickAction = new QuickAction(this, QuickAction.VERTICAL,1);
		if(isEditActive)
			quickAction.addActionItem((new ActionItem(ID_SALVAR_CONFIGURACION, "Salvar Configuración")));
        quickAction.addActionItem((new ActionItem(ID_SINCRONIZE_PARAMETROS, "Sincronizar Parametros")));
		quickAction.addActionItem((new ActionItem(ID_SINCRONIZE_CATALOGOSBASICOS, "Sincronizar Catalogos Básicos")));
        quickAction.addActionItem((new ActionItem(ID_SINCRONIZE_CLIENTES, "Sincronizar Clientes")));         
        quickAction.addActionItem((new ActionItem(ID_SINCRONIZE_PRODUCTOS, "Sincronizar Productos")));
        quickAction.addActionItem((new ActionItem(ID_SINCRONIZE_PROMOCIONES, "Sincronizar Promociones")));
        quickAction.addActionItem(null);
        quickAction.addActionItem((new ActionItem(ID_SINCRONIZE_TODOS, "Sincronizar Todo")));
        quickAction.addActionItem(null);
        quickAction.addActionItem((new ActionItem(ID_CERRAR, "Cerrar")));
        
        quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() 
		{			
			@Override
			public void onItemClick(QuickAction source, int pos, int actionId) 
			{				
				ActionItem actionItem = quickAction.getActionItem(pos);    
				if (actionId == ID_SALVAR_CONFIGURACION) 
					salvarConfiguracion(); 
				else if (actionId == ID_SINCRONIZE_PARAMETROS) 
						controller.getInboxHandler().sendEmptyMessage(ID_SINCRONIZE_PARAMETROS);
				else if (actionId == ID_SINCRONIZE_CATALOGOSBASICOS) 
						controller.getInboxHandler().sendEmptyMessage(ID_SINCRONIZE_CATALOGOSBASICOS);
				else if (actionId == ID_SINCRONIZE_CLIENTES) 
						controller.getInboxHandler().sendEmptyMessage(ID_SINCRONIZE_CLIENTES);
				else if (actionId == ID_SINCRONIZE_PRODUCTOS) 
						controller.getInboxHandler().sendEmptyMessage(ID_SINCRONIZE_PRODUCTOS);
				else if (actionId == ID_SINCRONIZE_PROMOCIONES) 
						controller.getInboxHandler().sendEmptyMessage(ID_SINCRONIZE_PROMOCIONES);
				else if (actionId == ID_SINCRONIZE_TODOS) 
						controller.getInboxHandler().sendEmptyMessage(LOAD_DATA_FROM_SERVER);
				else if (actionId == ID_CERRAR) 
					FINISH_ACTIVITY();
				else  
				    Toast.makeText(getApplicationContext(), actionItem.getTitle() + " selected", Toast.LENGTH_SHORT).show();
				 
			}
		});
		
		quickAction.setOnDismissListener(new QuickAction.OnDismissListener() 
		{			
			@Override
			public void onDismiss() { 
			}
		});
    	
        
        
     }
	
	private void salvarConfiguracion()
	{
		if(isValidInformation())
		{
			
			try 
			{
				if (String.valueOf(txtURL.getText()).trim().compareTo(this.getUrlServer()) != 0) 
				{       
					new Thread(new Runnable()
					{
						private Object lock;

						public void run()
					    {
							try
					        { 
								String credenciales=SessionManager.getCredenciales();
						 		Usuario user=BConfiguracionM.GET_DATAUSER(credenciales, getUserName());
								if(user!=null)
									BConfiguracionM.GET_DEVISE_PREFIX(credenciales,NMNetWork.getDeviceId(context));
								else
									dialog("","El usuario configurado es inválido.",ALERT_DIALOG);
 
									 /*vmConfiguracion vmc=new vmConfiguracion();
									 Message message = null;
									 message.what=ID_SALVAR_CONFIGURACION;
									 controller.getInboxHandler().sendMessage(message); */  
								
									 
								
							}
					        catch (Exception e)
					        {
					            e.printStackTrace(); 
					        } 

					     }

					});
					
	            } 
				
				
			} catch (Exception e) { 
				e.printStackTrace();
			} 
			
		}

	}
	
	public boolean isValidInformation()
	{ 
		
        if (txtURL.getText().toString().trim().length()==0){ 
        		txtURL.setError( "Ingrese el nombre o IP del servidor Nordis.");
        		txtURL.requestFocus();
        		return false;
        }
        else if (txtEmpresa.getText().toString().trim().length()==0){ 
                txtEmpresa.setError("Ingrese el código de la empresa.");
                txtEmpresa.requestFocus();
                return false;
	    }
        else if (txtUsuario.getText().toString().trim().length()==0){ 
                txtUsuario.setError("Ingrese el nombre del usuario.");
                txtUsuario.requestFocus();
                return false;
        }  
        
		return true;
	}
	
	private void FINISH_ACTIVITY()
	{
		List<Handler> handlers=controller.getoutboxHandlers();
		for (Handler handler :handlers)  
			  controller.removeOutboxHandler(handler); 
		controller.dispose();
		Log.d(TAG, "Activity quitting");
		finish();
	}  
	
	@SuppressLint("ShowToast")
	public Toast buildToastMessage(String msg,int duration)
	{
		Toast toast= Toast.makeText(context,msg,duration);  
		toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0); 
		return toast;
	}	
	
	public  Dialog dialog(String tittle,String msg,int type)
	{
		return new CustomDialog(this,tittle,msg,false,type);	    
	}
	
}

package com.panzyma.nm.auxiliar; 

import java.lang.reflect.Type;

import org.ksoap2.serialization.PropertyInfo; 
import org.ksoap2.serialization.SoapPrimitive;

import android.annotation.SuppressLint;
import android.app.Activity;  
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;   

import com.panzyma.nm.controller.Controller;
import com.panzyma.nm.viewdialog.DialogLogin; 
import com.panzyma.nm.viewdialog.DialogLogin.OnButtonClickListener; 

import static com.panzyma.nm.controller.ControllerProtocol.ERROR;
import static com.panzyma.nm.controller.ControllerProtocol.NOTIFICATION;

@SuppressLint("ParserError")@SuppressWarnings("rawtypes")
public class SessionManager
{

	private static  String empresa;
	private static  String nameuser; 
	private static  String password; 
	private static  boolean islogged;
	
	private static  Activity context;
	private static Controller controller;
	
    private static final int AUT_FALLIDA = 0; 
    private static final int AUT_EXITOSA = 1; 
    private static final int AUT_USER_NO_EXIST = 2; 
    private static final int AUT_PWD_INVALIDO = 3;  
    private static Parameters params;
    private static String errormessage=""; 
    private static boolean isOK;
    private static boolean _esAdmin;
    
    static Object lock=new Object(); 
    
    public static DialogLogin  dl;
	private static ThreadPool pool;
    
	public SessionManager(){};
	
	public static String getNameUser()
	{
		return nameuser;
	}
	public static void setNameUser(String _nameuser)
	{
		nameuser=_nameuser;
	}
	public static String getPassword()
	{
		return password;
	}
	public static void setPassword(String _password)
	{
		password=_password;
	}
	public static String getEmpresa()
	{
		return empresa;
	}
	public static void setEmpresa(String _empresa)
	{
		empresa=_empresa;
	}
	public static boolean isAdmin() 
	{
	       return _esAdmin;
    }
	 
	public static void setContext(Activity _context, Controller _controller)
	{
		context=_context;
		controller=_controller;
		if(pool==null)
			pool = new ThreadPool(2);
	}
	
	public static Controller getController()
    {
		return controller;
    }
	public static void setController(Controller _controller)
    {
		controller=_controller;
    }
	
	public  static void setLogged(boolean value){
		islogged=value;
	}
	public  static boolean isLogged(){
		return islogged;
	}
	public static String getCredenciales(){
		
		if (!islogged) 
			if(!SessionManager.SignIn(false))
				return ""; 
         
        return nameuser + "||" + password + "||" + empresa; 
	} 
	public static void setErrorAuntentication(String _error){
		errormessage=_error;
	}

	public static String getErrorAuntentication(){
		return errormessage;	
	}
	public synchronized static boolean SignIn(final boolean admin)
    {
        context.runOnUiThread(new Runnable()
        {
            public void run()
            {
            	if(dl==null)
            	{
            		dl=new DialogLogin(context,admin);
            		dl.setOnDialogLoginButtonClickListener(new OnButtonClickListener(){
					public void onButtonClick(boolean btn) { 
						isOK=btn;
						lock.notify();
						//dl.dismiss();   
					}}); 
            		dl.setOnDismissListener(new OnDismissListener()
	                {
	                    public void onDismiss(DialogInterface dialog)
	                    {
	                        synchronized(lock){                            
	                        	lock.notify();
	                        }
	
	                    }
	                });
            	}
            	else
            		dl.clean();
            	dl.show(); 
            }
        });

        synchronized(lock)
        {
            try {
            	lock.wait();
			} catch (InterruptedException e) { 
				e.printStackTrace();
			}
        }
        if(isOK)
        {
        	context.runOnUiThread(new Runnable()
	        {
	            public void run()
	            {		         
	            	SessionManager.login();  
	            }	        
	        });        	
	        synchronized(lock)
	        {
	            try {
	            	lock.wait();
				} catch (InterruptedException e) { 
					e.printStackTrace();
				}
	        }
        }
        
        return SessionManager.isLogged();
    }
	  
	@SuppressLint("ParserError")
	public  static boolean  login() 
	{  		
		
		final String empresa=dl.getEmpresa();
		final String nombreusuario=dl.getNameUser();
		final String password=dl.getPassword();
	    params=new Parameters((new String[]{"CodEmpresa","UserName","Password"}),
				 (new Object[]{nombreusuario+"||"+password+"||"+empresa}),
				 (new Type[]{PropertyInfo.STRING_CLASS,PropertyInfo.STRING_CLASS,PropertyInfo.STRING_CLASS})); 
		SessionManager.setLogged(false); 
		SessionManager.setErrorAuntentication("");		
		try 
		{ 				
			
			pool.execute(new Runnable()
			{
				@Override
				public void run() 
				{ 
					if(NMNetWork.isPhoneConnected(context,controller) && NMNetWork.CheckConnection(controller))					
					{
						try 
						{
								pool.execute(new Runnable()
								{
									@Override
									public void run()
									{ 
										
										int res;
										
										try 
										{
											res = Integer.parseInt(((SoapPrimitive)NMComunicacion.InvokeMethod(params.getParameters(),NMConfig.URL,NMConfig.NAME_SPACE,NMConfig.MethodName.LoginUser)).toString());
											if (res == AUT_EXITOSA)
											{
												 params=new Parameters((new String[]{"Credentials","Rol"}),
														 (new Object[]{(nombreusuario+"||"+password+"||"+empresa),"ADMIN"}),
														 (new Type[]{PropertyInfo.STRING_CLASS,PropertyInfo.STRING_CLASS})); 
											
												 _esAdmin=Boolean.parseBoolean(((SoapPrimitive)NMComunicacion.InvokeMethod(params.getParameters(),NMConfig.URL,NMConfig.NAME_SPACE,NMConfig.MethodName.UserHasRol)).toString());
												SessionManager.setEmpresa(dl.getEmpresa());
												SessionManager.setNameUser(dl.getNameUser());
												SessionManager.setPassword(dl.getPassword());
												SessionManager.setLogged(true);
												unlock();
											}
											else if(res == AUT_USER_NO_EXIST)
												sendErrorMessage(new ErrorMessage("Error en la Autenticación","Login: Usuario desconocido.","")); 
											else if (res == AUT_PWD_INVALIDO) 
												sendErrorMessage(new ErrorMessage("Error en la Autenticación","Login:Contraseña inválida.","")); 
											else if (res == AUT_FALLIDA)
												sendErrorMessage(new ErrorMessage("Error en la Autenticación","Login:Fallo de autenticación.","")); 
											 
										}catch (Exception e) {
											sendErrorMessage(new ErrorMessage("Error en la Autenticación","Login: Fallo en la conexión con el servidor de aplicaciones.\r\n",e.toString()));
											e.printStackTrace();
										}
									}
									
								});
								
						} catch (InterruptedException e) { 
							e.printStackTrace();
						} 
						controller._notifyOutboxHandlers(NOTIFICATION, 0, 0, new NotificationMessage("","Validando Credenciales.","")); 
							
					}
					else
						unlock();
				 }
			});	
			controller._notifyOutboxHandlers(NOTIFICATION, 0, 0, new NotificationMessage("","Probando Conexion.",""));   

		}
	    catch (Exception e) {  
			e.printStackTrace();
			sendErrorMessage(new ErrorMessage("Error en la Autenticación","Login: Fallo en la conexión con el servidor de aplicaciones.\r\n",e.toString()));
		} 
		return SessionManager.isLogged();
	}
	 
	public static void unlock()
	{
		context.runOnUiThread(new Runnable()
	     {
	            public void run()
	            {
	            	synchronized(lock){                            
	            		if(SessionManager.isLogged())
	            			lock.notify();
                    }
	            }
	      });
	}
	public static void sendErrorMessage(ErrorMessage error)
	{
		try 
		{
			pool.execute(new Runnable()
			{
				@Override
				public void run() 
				{
					unlock();
				}
			}
			);
			Processor.notifyToView(controller,ERROR,0,0,error); 
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) { 
			e.printStackTrace();
		}
		
	}
    //Chequea el estado de la conexión con el servidor de aplicaciones de Nordis
    public static boolean CheckConnection() 
    {
         return NMNetWork.CheckConnection(controller);
    }
     
	
}

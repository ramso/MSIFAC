package com.panzyma.nm.CBridgeM;

import static com.panzyma.nm.controller.ControllerProtocol.LOAD_DATA;
import static com.panzyma.nm.controller.ControllerProtocol.C_UPDATE_FINISHED;
import static com.panzyma.nm.controller.ControllerProtocol.C_UPDATE_STARTED;
import static com.panzyma.nm.controller.ControllerProtocol.C_UPDATE_IN_PROGRESS;
import static com.panzyma.nm.controller.ControllerProtocol.ERROR;
import static com.panzyma.nm.controller.ControllerProtocol.ID_SALVAR_CONFIGURACION;
import static com.panzyma.nm.controller.ControllerProtocol.ID_SINCRONIZE_CATALOGOSBASICOS;
import static com.panzyma.nm.controller.ControllerProtocol.ID_SINCRONIZE_PARAMETROS;
import static com.panzyma.nm.controller.ControllerProtocol.ID_SINCRONIZE_CLIENTES;
import static com.panzyma.nm.controller.ControllerProtocol.ID_SINCRONIZE_PRODUCTOS;
import static com.panzyma.nm.controller.ControllerProtocol.ID_SINCRONIZE_PROMOCIONES;
import static com.panzyma.nm.controller.ControllerProtocol.ID_SINCRONIZE_TODOS;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Message;
import android.util.Log;
  
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.NMNetWork;
import com.panzyma.nm.auxiliar.Processor;
import com.panzyma.nm.auxiliar.ThreadPool;
import com.panzyma.nm.controller.Controller; 
import com.panzyma.nm.model.ModelCliente;
import com.panzyma.nm.model.ModelConfiguracion;
import com.panzyma.nm.model.ModelProducto;
import com.panzyma.nm.serviceproxy.Cliente;
import com.panzyma.nm.serviceproxy.Producto;
import com.panzyma.nm.serviceproxy.Usuario;
import com.panzyma.nm.view.ViewConfiguracion;
import com.panzyma.nm.viewmodel.vmConfiguracion;


@SuppressWarnings({"rawtypes","unused"})
public class BConfiguracionM {

	String TAG=BConfiguracionM.class.getSimpleName();
	ViewConfiguracion view; 
	Controller controller;
	ThreadPool pool;
	int view_activated;
	ArrayList<Cliente> LCliente;
	ArrayList<Producto> LProducto;
	SharedPreferences pref;
	SharedPreferences.Editor edit;
	
	public BConfiguracionM(){}
	
	public BConfiguracionM(Controller c,ViewConfiguracion view)
	{
    	this.controller=c;  
    	this.view=view;
    	this.pool = new ThreadPool(2);
    	view_activated=1;
    	LCliente=new ArrayList<Cliente>();
    	LProducto=new ArrayList<Producto>();

    }
	
	public boolean handleMessage(Message msg) 
	{
		switch (msg.what) 
		{  
		
			case LOAD_DATA: 
					LOAD_DATA();
					return true; 
			case ID_SALVAR_CONFIGURACION: 
					SINCRONIZE_PARAMETROS();
					return true; 
			case ID_SINCRONIZE_PARAMETROS: 
					SINCRONIZE_PARAMETROS();
					return true;    
			case ID_SINCRONIZE_CATALOGOSBASICOS: 
					SINCRONIZE_CATALOGOSBASICOS();
					return true;  
			case ID_SINCRONIZE_CLIENTES: 
					SINCRONIZE_CLIENTES();
					return true;   
			case ID_SINCRONIZE_PRODUCTOS: 
					SINCRONIZE_PRODUCTOS();
					return true;   
			case ID_SINCRONIZE_PROMOCIONES: 
					SINCRONIZE_CATALOGOSBASICOS();
					return true;   
			case ID_SINCRONIZE_TODOS: 
					SINCRONIZE_CATALOGOSBASICOS();
					return true;   
		}
		return false;
	}
	
	private void LOAD_DATA()
	{
		try
		{
			 
			Processor.send_DataSourceToView(ModelConfiguracion.getVMConfiguration(view), controller);
			 			 
		}
		catch(Exception e)
		{
			try {
				Processor.notifyToView(controller,ERROR,0,0,"CheckConnection :"+"\n error en la comunicación con el servidor de aplicaciones \n Causa:"+e.toString());
			} catch (Exception e1) { 
				e1.printStackTrace();
			}
		}
		
		
	}
	
	public static Usuario GET_DATAUSER(String Credentials,String LoginUsuario) throws Exception
	{
		return ModelConfiguracion.getDatosUsuario(Credentials, LoginUsuario);
	}
	public static Usuario GET_DEVISE_PREFIX(String Credentials,String LoginUsuario) throws Exception
	{
		return ModelConfiguracion.getDatosUsuario(Credentials, LoginUsuario);
	}
	private void SALVAR_CONFIGURACION()
	{
		try
		{
			if(NMNetWork.isPhoneConnected(view,controller) && NMNetWork.CheckConnection(controller))
			{  
				
			}
			 			 
		}
		catch(Exception e)
		{
			try {
				Processor.notifyToView(controller,ERROR,0,0,"CheckConnection :"+"\n error en la comunicación con el servidor de aplicaciones \n Causa:"+e.toString());
			} catch (Exception e1) { 
				e1.printStackTrace();
			}
		}
	}
	
	private void SINCRONIZE_PARAMETROS()
	{
		try
		{
			
		}
		catch(Exception e)
		{
			
		}
		
	}
	
	private void SINCRONIZE_CATALOGOSBASICOS()
	{
		try
		{
			
		}
		catch(Exception e)
		{
			e.printStackTrace();  
    	    try {
    	    	Processor.notifyToView(controller,ERROR,0,0,new ErrorMessage("Error interno salva guardando datos en la BDD",e.toString(),"\n Causa: "+e.getCause()));
			} catch (Exception e1) { 
				e1.printStackTrace();
			} 
		}
		
	}
	
	private void SINCRONIZE_CLIENTES()
	{
		try
		{ 
			pool.execute(  new Runnable()
            {
            	@Override
				public void run() 
				{
					try 
					{
						if(NMNetWork.isPhoneConnected(view,controller) && NMNetWork.CheckConnection(controller))
					    {
									Integer page=1;
								    while(true)
									{ 					    	 
								       ArrayList<Cliente> modelcliente=ModelCliente.getArrayCustomerFromServer("sa||nordis09||dp","areyes",page,30); 
									   if(modelcliente.size()!=0)
									   {   
										   LCliente.addAll(modelcliente); 
										   Processor.notifyToView(controller,C_UPDATE_IN_PROGRESS, 0, 0,"Sincronizando Clientes \npágina:"+page.toString()+" ...");
									   	   page++;
									   }
									   else  
										   break; 					  
									} 	
								    onSave_From_LocalHost(LCliente,ID_SINCRONIZE_CLIENTES);
								    Processor.notifyToView(controller,C_UPDATE_FINISHED,0,0,"Los clientes han sido sincronizados exitosamente");
							 					
						}
	  						 				 
					}  
					catch (Exception e) 
			        { 
						e.printStackTrace();
						try {
							Processor.notifyToView(controller,ERROR,0,0,new ErrorMessage("Error en la sincronización de clientes con el servidor",e.getMessage(),"\n Causa: "+e.getCause()));
						} catch (Exception e1) { 
							e1.printStackTrace();
						} 
					} 
				}
            }); 
			Processor.notifyToView(controller,C_UPDATE_STARTED, 0, 0,"Sincronizando Clientes"); 		
			 
		}     
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	private void SINCRONIZE_PRODUCTOS()
	{
		try 
		{ 
			    pool.execute
			    (  new Runnable()
					{
						@Override
						public void run() 
						{
							try 
							{
								
								if(NMNetWork.isPhoneConnected(view,controller) && NMNetWork.CheckConnection(controller))
								{ 
									Integer page=1;
								    while(true)
									{ 					    	 
								       ArrayList<Producto> modelproducto=ModelProducto.getArrayProductoFromServer("sa||nordis09||dp","areyes",page,50); 
									   if(modelproducto.size()!=0)							      
									   {	 
										   LProducto.addAll(modelproducto); 
										   Log.d(TAG,"C_UPDATE_IN_PROGRESS on SINCRONIZE_PRODUCTOS");
										   Processor.notifyToView(controller,C_UPDATE_IN_PROGRESS, 0, 0,"Sincronizando Productos \npágina: "+page.toString()+" ...");
									       page++;
									   }
									   else  
										   break; 	 
									} 
								    onSave_From_LocalHost(LProducto,ID_SINCRONIZE_PRODUCTOS);
								    Processor.notifyToView(controller,C_UPDATE_FINISHED,0,0,"Los productos han sido sincronizados exitosamente");
								}
						 		
							}
							catch (Exception e) 
					        { 
								e.printStackTrace();
								try {
									Processor.notifyToView(controller,ERROR,0,0,new ErrorMessage("Error en la sincronización de produstos con el servidor",e.getMessage(),"\n Causa: "+e.getCause()));
								} catch (Exception e1) { 
									e1.printStackTrace();
								} 
						    } 
						}
						
						
					}
			    );
			Processor.notifyToView(controller,C_UPDATE_STARTED, 0, 0, "Sincronizando Productos");
			
		} catch (Exception e) { 
			e.printStackTrace();
		}
		
	}
	
	private void SINCRONIZE_PROMOCIONE()
	{
		try
		{
			
		}
		catch(Exception e)
		{
			
		}
		
	}
	
	private void SINCRONIZE_TODOS()
	{
		try
		{
			
		}
		catch(Exception e)
		{
			
		}
		
	}
	
	private synchronized void onSave_From_LocalHost(final ArrayList<?> objL, final int ID)
	{		
		try
		{ 
			
		    pool.execute
			(
			  new Runnable() 
			  {				  
				@SuppressWarnings("unchecked")
				@Override
				public void run() 
				{						
					try
					{      
						switch (ID) 
						{  
							case ID_SINCRONIZE_PARAMETROS: break;
							
							case ID_SINCRONIZE_CATALOGOSBASICOS:break;
							
							case ID_SINCRONIZE_CLIENTES: 	ModelCliente.RegisterCustomer_From_LocalHost((ArrayList<Cliente>)objL, view.getApplicationContext()); 
															break;
															
							case ID_SINCRONIZE_PRODUCTOS:   ModelProducto.RegisterProduct_From_LocalHost((ArrayList<Producto>)objL, view.getApplicationContext());
															break;
							case ID_SINCRONIZE_PROMOCIONES: break;
							
							case ID_SINCRONIZE_TODOS:    break;
						}						 						
					} 
			    	catch(Exception e)
			    	{
			    	    e.printStackTrace();  
			    	    try {
			    	    	Processor.notifyToView(controller,ERROR,0,0,new ErrorMessage("Error interno salva guardando datos en la BDD",e.toString(),"\n Causa: "+e.getCause()));
						} catch (Exception e1) 
						{ 
							e1.printStackTrace();
						} 
			    	}  
				}
			  }
		    );
		} 
		catch(Exception e)
    	{
    	    e.printStackTrace();  
    	}
	}  
	
}

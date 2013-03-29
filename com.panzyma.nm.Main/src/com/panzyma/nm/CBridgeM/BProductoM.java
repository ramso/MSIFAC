package com.panzyma.nm.CBridgeM; 

import static com.panzyma.nm.controller.ControllerProtocol.C_UPDATE_STARTED;
import static com.panzyma.nm.controller.ControllerProtocol.ERROR;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_DATA_FROM_LOCALHOST;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_DATA_FROM_SERVER; 
import static com.panzyma.nm.controller.ControllerProtocol.UPDATE_ITEM_FROM_SERVER;
import static com.panzyma.nm.controller.ControllerProtocol.C_UPDATE_FINISHED;

import java.util.ArrayList;

import android.os.Message;
import android.util.Log;
 
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.NMNetWork;
import com.panzyma.nm.auxiliar.Processor;
import com.panzyma.nm.auxiliar.ThreadPool;
import com.panzyma.nm.controller.Controller;   
import com.panzyma.nm.model.ModelProducto; 
import com.panzyma.nm.serviceproxy.Producto;
import com.panzyma.nm.view.ViewProducto;
@SuppressWarnings("rawtypes")
public class BProductoM {
 
	Controller controller; 
    ThreadPool pool;
    ViewProducto view;
	String TAG=BClienteM.class.getSimpleName();
	boolean OK=false; 
	ArrayList<Producto> obj=new ArrayList<Producto>(); 
	public BProductoM(){}
	
	
	public BProductoM(Controller c,ViewProducto view)
	{
    	this.controller=c;  
    	this.view=view;
    	this.pool = new ThreadPool(2);
    }  
	
	public boolean handleMessage(Message msg) throws Exception 
	{
		switch (msg.what) 
		{  
			case LOAD_DATA_FROM_LOCALHOST: 
					onLoadALLData_From_LocalHost();
					return true;		
			case LOAD_DATA_FROM_SERVER:  
				    onLoadALLData_From_Server(); 
					return true; 						
			case UPDATE_ITEM_FROM_SERVER:  
					onUpdateItem_From_Server(); 
					return true; 	    
					
		}
		return false;
	}
	
	
	private void onLoadALLData_From_LocalHost()
	{
		try 
		{   
			pool.execute
			(
				new Runnable()
				{
	
					@Override
					public void run() 
					{ 
						
						try
						{
							 
							Processor.send_ViewProductoToView(ModelProducto.getArrayCustomerFromLocalHost(view.getContentResolver()),controller);						
						}
						catch (Exception e) 
						{
							Log.e(TAG, "Error in the update thread", e);
							try {
								Processor.notifyToView(controller,ERROR,0,0,new ErrorMessage("Error interno en la sincronizaci�n con la BDD",e.toString(),"\n Causa: "+e.getCause()));
							} catch (Exception e1) { 
								e1.printStackTrace();
							}  
						}
					}
				}
			);
			
		}  
		catch (Exception e) 
        { 
			e.printStackTrace();
		} 
	}
	private void onLoadALLData_From_Server() throws Exception
	{
		
		try {
			this.pool.execute(  new Runnable()
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
									   obj.addAll(modelproducto); 
									   Processor.builAndsend_ViewProductoToView(modelproducto, controller);
								       page++;
								   }
								   else  
									   break; 	 
								} 
							    onSave_From_LocalHost(obj);
							    Processor.notifyToView(controller,C_UPDATE_FINISHED,0,0,null);
						}
						
					}
					catch (Exception e) 
			        { 
						e.printStackTrace();
						try {
							Processor.notifyToView(controller,ERROR,0,0,new ErrorMessage("Error en la sincronizaci�n con el servidor",e.toString(),"\n Causa: "+e.getCause()));
						} catch (Exception e1) { 
							e1.printStackTrace();
						} 
				    } 
				}
				
				
			});
			Processor.notifyToView(controller,C_UPDATE_STARTED, 0, 0, null);
		} catch (InterruptedException e) { 
			e.printStackTrace();
		}
		
	}
	private void onUpdateItem_From_Server()
	{
		
	}
	 
	private void onSave_From_LocalHost(final ArrayList<Producto> objL)
	{		
		try
		{
			this.pool.execute
			(
			  new Runnable() 
			  {				  
				@Override
				public void run() 
				{						
					try
					{      
						ModelProducto.RegisterProduct_From_LocalHost(objL, view.getApplicationContext()); 						
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
			  }
		    );
		}
		
		catch(Exception e)
    	{
    	    e.printStackTrace();  
    	}
	}  
}

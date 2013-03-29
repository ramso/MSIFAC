package com.panzyma.nm.auxiliar;

import java.lang.reflect.InvocationTargetException;
import android.annotation.SuppressLint;
import android.os.Message;
import android.util.Log;
import static com.panzyma.nm.controller.ControllerProtocol.*;
import android.os.Handler;

public final class UpdatingState<T> implements ControllerState {

	private static final String TAG = UpdatingState.class.getSimpleName();
	
	private final T controller;
	private final Thread updateThread; 
	
	public UpdatingState(T controller)  
	{
		this.controller = controller;

		// Remember, the model is thread-safe in our example so we can modify
		// it from multiple threads
		updateThread = new Thread("Model Update") 
		{ 
			@Override
			public void run() 
			{
				T controller = UpdatingState.this.controller;
				try 
				{
					   Object model=controller.getClass().getMethod("getModel", new Class<?>[0]).invoke(controller,new Object[0]);
					   model.getClass().getMethod("updateData", new Class<?>[0]).invoke(model,new Object[0]);
					 				 
				} 
				catch (Throwable t) 
				{
					Log.e(TAG, "Error in the update thread", t);
				} finally 
				{
					notifyControllerOfCompletion();
				}
			}
		};
		updateThread.start();
        try 
        {
			controller.getClass().getMethod("notifyOutboxHandlers",int.class,int.class,int.class,Object.class).invoke(controller,C_UPDATE_STARTED,0,0,null);
		}  
        catch (IllegalArgumentException e) 
		{ 
			e.printStackTrace();
		} 
		catch (SecurityException e) 
		{ 
			e.printStackTrace();
		} 
		catch (IllegalAccessException e) 
		{ 
			e.printStackTrace();
		} 
		catch (InvocationTargetException e) 
		{ 
			e.printStackTrace();
		} 
		catch (NoSuchMethodException e) 
		{ 
			e.printStackTrace();
		}
	}
	
	@SuppressLint("ParserError")
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void notifyControllerOfCompletion() 
	{
		// this method will be called from the background thread.
		// avoid Controller synchronization - do this in the inbox thread
		// by using Handler.post()
		
			 try 
			 {
				    
				  ((Handler)controller.getClass().getMethod("getInboxHandler", new Class<?>[0]).invoke(controller, new Object[0])).post(
					  new Runnable() 
					  {						
						@Override
						public void run() 
						{
							
							try
							{   
								controller.getClass().getMethod("changeState",new Class<?>[0]).invoke(controller,new ReadyState(controller));
								controller.getClass().getMethod("notifyOutboxHandlers",int.class,int.class,int.class,Object.class).invoke(C_UPDATE_FINISHED,0,0,null);
							}
							catch (IllegalArgumentException e) 
							{ 
								e.printStackTrace();
							} 
							catch (SecurityException e) 
							{ 
								e.printStackTrace();
							} 
							catch (IllegalAccessException e) 
							{ 
								e.printStackTrace();
							} 
							catch (InvocationTargetException e) 
							{ 
								e.printStackTrace();
							} 
							catch (NoSuchMethodException e) 
							{ 
								e.printStackTrace();
							}
						}	  
					
				});
				
				
			}  
		    catch (IllegalArgumentException e) 
			{ 
				e.printStackTrace();
			} 
			catch (SecurityException e) 
			{ 
				e.printStackTrace();
			} 
			catch (IllegalAccessException e) 
			{ 
				e.printStackTrace();
			} 
			catch (InvocationTargetException e) 
			{ 
				e.printStackTrace();
			} 
			catch (NoSuchMethodException e) 
			{ 
				e.printStackTrace();
			}
			 
	}
	
	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case V_REQUEST_QUIT:
			onRequestQuit();
			return true;
		}
		// ignore all other messages
		return false;
	}

	private void onRequestQuit() {
		
		try 
		{
			updateThread.interrupt();
			controller.getClass().getMethod("quit",new Class<?>[0]).invoke(controller,new Object[0]);
			
		} 
		catch (IllegalArgumentException e) 
		{ 
			e.printStackTrace();
		} 
		catch (SecurityException e) 
		{ 
			e.printStackTrace();
		} 
		catch (IllegalAccessException e) 
		{ 
			e.printStackTrace();
		} 
		catch (InvocationTargetException e) 
		{ 
			e.printStackTrace();
		} 
		catch (NoSuchMethodException e) 
		{ 
			e.printStackTrace();
		}
	}
}

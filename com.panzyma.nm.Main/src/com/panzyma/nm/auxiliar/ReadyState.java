package com.panzyma.nm.auxiliar; 
import static com.panzyma.nm.controller.ControllerProtocol.*;

import java.lang.reflect.InvocationTargetException;
import android.os.Message;

public final class ReadyState<T> implements ControllerState {

	private final T controller;
	
	public ReadyState(T controller) {
		this.controller = controller;
	}
	
	@Override
	public final boolean handleMessage(Message msg) {
		switch (msg.what) 
		{
			case V_REQUEST_QUIT:
				onRequestQuit();
				return true;
			case V_REQUEST_UPDATE:
				onRequestUpdate();
				return true;
			/*case LOAD_DATA: 
				onLoadData(); 
				return true;*/
		}
		return false;
	} 
	private void onLoadData()
	{		
		try 
		{ 
			
			Object model=controller.getClass().getMethod("getModel", new Class<?>[0]).invoke(controller,new Object[0]); 
			Object data=model.getClass().getMethod("getData", new Class<?>[0]).invoke(model,new Object[0]);
			controller.getClass().getMethod("notifyOutboxHandlers",int.class,int.class,int.class,Object.class).invoke(controller,C_DATA,0,0,data);
			
		} 
		catch (IllegalArgumentException e) 
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
        catch (SecurityException e) 
        { 
			e.printStackTrace();
		} 
		catch (NoSuchMethodException e) 
		{ 
			e.printStackTrace();
		} 
		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void onRequestUpdate() {
		// we can't just call model.updateState() here because it will block
		// the inbox thread where this processing is happening.
		// thus we change the state to UpdatingState that will launch and manage
		// a background thread that will do that operation
		try 
		{
			controller.getClass().getMethod("changeState",new Class<?>[0]).invoke(controller,new UpdatingState(controller));
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

	private void onRequestQuit() {
		//controller.quit();
		try 
		{ 
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

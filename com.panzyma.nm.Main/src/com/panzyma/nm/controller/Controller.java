package com.panzyma.nm.controller;
  
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List; 

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

@SuppressWarnings("hiding")
public class Controller<T, U>
{
	T bridge;
	U view;
	private static final String TAG = Controller.class.getSimpleName(); 
	private final HandlerThread inboxHandlerThread;
	private final Handler inboxHandler;
	private final List<Handler> outboxHandlers = new ArrayList<Handler>();
	  	
	
	@SuppressWarnings({"unchecked"})
	public Controller(T VbridgeM,U view) throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException
	{			 
		inboxHandlerThread = new HandlerThread("Controller Inbox"); 
		inboxHandlerThread.setPriority(Thread.MAX_PRIORITY);
		inboxHandlerThread.start();
		 
		this.view=view;
		
		this.bridge=(T) VbridgeM.getClass().getConstructor(this.getClass(),view.getClass()).newInstance(this,view);
		
		
		inboxHandler = new Handler(inboxHandlerThread.getLooper()) {
			@Override
			public void handleMessage(Message msg) {
				try {
					Controller.this.handleMessage(msg);
				} catch (SecurityException e) { 
					e.printStackTrace();
				} catch (IllegalAccessException e) { 
					e.printStackTrace();
				} catch (InvocationTargetException e) { 
					e.printStackTrace();
				} catch (NoSuchMethodException e) { 
					e.printStackTrace();
				}
			}
		};
		Log.d("Controller","constructor Controller after inboxHandler y de instanciar BClienteM");
	}
 
	public Controller()
	{			 
		inboxHandlerThread = new HandlerThread("Controller Inbox"); 
		inboxHandlerThread.setPriority(Thread.MAX_PRIORITY);
		inboxHandlerThread.start();  
		inboxHandler = new Handler(inboxHandlerThread.getLooper()) {
			@Override
			public void handleMessage(Message msg) {
				try {
					Controller.this.handleMessage(msg);
				} catch (SecurityException e) { 
					e.printStackTrace();
				} catch (IllegalAccessException e) { 
					e.printStackTrace();
				} catch (InvocationTargetException e) { 
					e.printStackTrace();
				} catch (NoSuchMethodException e) { 
					e.printStackTrace();
				}
			}
		};
		Log.d("Controller","constructor Controller after inboxHandler y de instanciar BClienteM");
	}
	
	public final void dispose() {
		inboxHandlerThread.getLooper().quit();
	}
	
	public final Handler getInboxHandler() {
		return inboxHandler;
	}
	
	public final void addOutboxHandler(Handler handler) {
		outboxHandlers.add(handler);
	}

	public final void removeOutboxHandler(Handler handler) {
		outboxHandlers.remove(handler);
	}
	public  List<Handler> getoutboxHandlers()
	{
		return outboxHandlers;
	}
	
	public final <T> void notifyOutboxHandlers(int what, int arg1, int arg2, ArrayList<T> obj) {
		if (outboxHandlers.isEmpty()) {
			Log.w(TAG, String.format("No outbox handler to handle outgoing message (%d)", what));
		} else {
			for (Handler handler : outboxHandlers) {
				Message msg = Message.obtain(handler, what, arg1, arg2, obj);
				msg.sendToTarget();
			}
		}
	}
	public final void _notifyOutboxHandlers(int what, int arg1, int arg2, Object obj) {
		if (outboxHandlers.isEmpty()) {
			Log.w(TAG, String.format("No outbox handler to handle outgoing message (%d)", what));
		} else {
			for (Handler handler : outboxHandlers) {
				Message msg = Message.obtain(handler, what, arg1, arg2, obj);
				msg.sendToTarget();
			}
		}
	}
	private void handleMessage(Message msg) throws SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Log.d(TAG, "Received message: " + msg);
		boolean result=(Boolean) this.bridge.getClass().getMethod("handleMessage", Message.class).invoke(bridge, msg);
		if (!result) {
			Log.w(TAG, "Unknown message: " + msg);
		}
	}
	  
 

}

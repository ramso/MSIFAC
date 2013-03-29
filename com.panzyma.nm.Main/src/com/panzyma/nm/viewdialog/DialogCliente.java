package com.panzyma.nm.viewdialog;

import static com.panzyma.nm.controller.ControllerProtocol.ALERT_DIALOG;
import static com.panzyma.nm.controller.ControllerProtocol.C_DATA;
import static com.panzyma.nm.controller.ControllerProtocol.ERROR;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_DATA_FROM_LOCALHOST;

import java.util.ArrayList;
import java.util.List;

import com.panzyma.nm.CBridgeM.BClienteM;
import com.panzyma.nm.auxiliar.CustomDialog;
import com.panzyma.nm.auxiliar.ErrorMessage; 
import com.panzyma.nm.controller.Controller;
import com.panzyma.nm.menu.QuickAction; 
import com.panzyma.nm.serviceproxy.Cliente;
import com.panzyma.nm.view.ViewCliente;
import com.panzyma.nm.view.ViewPedidoEdit;
import com.panzyma.nm.view.adapter.GenericAdapter;
import com.panzyma.nm.view.viewholder.ClienteViewHolder;
import com.panzyma.nm.viewmodel.vmCliente;
import com.panzyma.nordismobile.R;

import android.widget.LinearLayout;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity; 
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
@SuppressWarnings({"rawtypes","unused","unchecked"})
public class DialogCliente extends Dialog  implements Handler.Callback
{
	private Context mcontext;  
	private GenericAdapter adapter; 
	
	private Controller controller; 
	private vmCliente cliente_selected; 
	private ProgressDialog pd;
	private Button Menu; 
	private QuickAction quickAction; 
	private Display display;
	private static final String TAG = ViewCliente.class.getSimpleName(); 
	ListView lvcliente;	
	TextView gridheader;
	private int positioncache=-1; 
	private OnButtonClickListener mButtonClickListener;
	
	public interface OnButtonClickListener {
		public abstract void onButtonClick(vmCliente cliente);
	}
	
    public void setOnDialogClientButtonClickListener(OnButtonClickListener listener) {
		mButtonClickListener = listener;
	} 
	
	public DialogCliente(ViewPedidoEdit vpe,int theme) 
	{
		super(vpe,theme);

		try 
        {   
			setContentView(R.layout.maincliente);  
        	mcontext=this.getContext();  
			controller= new Controller(new BClienteM(),this); 
			controller.addOutboxHandler(new Handler(this));
			WindowManager wm = (WindowManager) vpe.getSystemService(Context.WINDOW_SERVICE);
            display = wm.getDefaultDisplay();
			pd = ProgressDialog.show(vpe, "Espere por favor", "Trayendo Info...", true, false); 
	        controller.getInboxHandler().sendEmptyMessage(LOAD_DATA_FROM_LOCALHOST); 
	        initComponents();
	        
        }catch (Exception e) { 
			e.printStackTrace();
			buildCustomDialog("Error !!!","Error Message:"+e.getMessage()+"\n Cause:"+e.getCause(),ALERT_DIALOG).show();			  
		}	 
      
	}

	@Override
	public boolean handleMessage(Message msg) 
	{ 
		switch (msg.what) 
		{		 
			case C_DATA:   
				
				LoadData((ArrayList<vmCliente>)((msg.obj==null)?new ArrayList<vmCliente>():msg.obj),C_DATA);
				return true;
			case ERROR:
				pd.dismiss();
				ErrorMessage error=((ErrorMessage)msg.obj);
				buildCustomDialog(error.getTittle(),error.getMessage()+error.getCause(),ALERT_DIALOG).show();				 
				return true;		
		}
		return false;
	}
	
	public void initComponents()
	{
		LinearLayout.LayoutParams layoutParams;
	    LinearLayout viewroot=((LinearLayout)findViewById(R.id.lmaincliente)); 
	    LinearLayout llheader=((LinearLayout)findViewById(R.id.llheader));
	    LinearLayout llbody=((LinearLayout)findViewById(R.id.llbody));
	    viewroot.setBackgroundResource(R.drawable.bgdialog2); 

	    layoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
	    layoutParams.setMargins(2, 2, 1,0); 
	    llheader.setLayoutParams(layoutParams);
	    layoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
	    layoutParams.setMargins(2, 0, 1,1); 
	    llbody.setLayoutParams(layoutParams);
	    
	    
	    lvcliente = (ListView) findViewById(R.id.lvcliente);	
	    gridheader=(TextView) findViewById(R.id.ctextv_gridheader); 
	    ViewStub stub = (ViewStub) findViewById(R.id.vsHeader);
	    ((ViewGroup) lvcliente.getParent()).removeView(stub);
	    
		EditText filterEditText = (EditText) findViewById(R.id.EditText_Client); 
	    filterEditText.addTextChangedListener(
	    new TextWatcher() 
	    {
	        @Override
	        public void onTextChanged(CharSequence s, int start, int before, int count) 
	        {
	            adapter.getFilter().filter(s.toString());
	        }
	        @Override
	        public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
	        @Override
	        public void afterTextChanged(Editable s) {}
	    }); 
	}
	 
	public void LoadData(final ArrayList<vmCliente> Lcliente, int what)
	{     									
		try
		{
			if(Lcliente.size()!=0)
			{
				gridheader.setText("Listado de Clientes("+Lcliente.size()+")");
				adapter=new GenericAdapter(mcontext,ClienteViewHolder.class,Lcliente,R.layout.gridcliente);				 
				lvcliente.setAdapter(adapter);
				lvcliente.setOnItemClickListener(new OnItemClickListener() 
		        {
		            @Override
		            public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
		            { 		  
		            	if((parent.getChildAt(positioncache))!=null)						            							            		
		            		(parent.getChildAt(positioncache)).setBackgroundResource(android.R.color.transparent);						            	 
		            	positioncache=position;				            	
		            	cliente_selected=(vmCliente) adapter.getItem(position);				
		            	adapter.setSelectedPosition(position); 
		            	view.setBackgroundDrawable(mcontext.getResources().getDrawable(R.drawable.action_item_selected));					            	 
		            	mButtonClickListener.onButtonClick(cliente_selected);
		            	FINISH_ACTIVITY();
		            }
		        }); 								
			    lvcliente.setOnItemLongClickListener(new OnItemLongClickListener()
			    {
					@Override
					public boolean onItemLongClick(AdapterView<?> parent, View view,int position, long id) 
					{  											 
						if((parent.getChildAt(positioncache))!=null)						            							            		
		            		(parent.getChildAt(positioncache)).setBackgroundResource(android.R.color.transparent);						            	 
		            	positioncache=position;				            	
		            	cliente_selected=(vmCliente) adapter.getItem(position);				
		            	adapter.setSelectedPosition(position); 
		            	view.setBackgroundDrawable(mcontext.getResources().getDrawable(R.drawable.action_item_selected));
		            	mButtonClickListener.onButtonClick(cliente_selected);
		            	FINISH_ACTIVITY();
		            	//quickAction.show(view,display,false);
						return true;
					}
			        	
			    });
	            buildToastMessage("sincronizaci�n exitosa",Toast.LENGTH_SHORT).show();
				
			} 
		}
	    catch(Exception e)
		{ 
			buildCustomDialog("Error !!!","Error Message:"+e.getMessage()+"\n Cause:"+e.getCause(),ALERT_DIALOG).show();
		}
	    pd.dismiss();
	} 
	
	@SuppressLint("ShowToast")
	public Toast buildToastMessage(String msg,int duration)
	{
		Toast toast= Toast.makeText(getContext(),msg,duration);  
		toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0); 
		return toast;
	}
	
	public  Dialog buildCustomDialog(String tittle,String msg,int type)
	{
		return new CustomDialog(getContext(),tittle,msg,false,type);	    
	}
	
	private void FINISH_ACTIVITY()
	{
		List<Handler> handlers=controller.getoutboxHandlers();
		for (Handler handler :handlers)  
			  controller.removeOutboxHandler(handler); 
		controller.dispose();
		Log.d(TAG, "Activity quitting"); 
		this.dismiss();
	}  
}

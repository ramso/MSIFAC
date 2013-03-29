package com.panzyma.nm.viewdialog;

import static com.panzyma.nm.controller.ControllerProtocol.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.ksoap2.serialization.PropertyInfo;

import com.panzyma.nm.CBridgeM.BClienteM;
import com.panzyma.nm.auxiliar.CustomDialog;
import com.panzyma.nm.auxiliar.DateUtil;
import com.panzyma.nm.auxiliar.Parameters;
import com.panzyma.nm.auxiliar.Pool;
import com.panzyma.nm.auxiliar.Pool.PoolObjectFactory;
import com.panzyma.nm.controller.Controller;
import com.panzyma.nm.serviceproxy.CCCliente;
import com.panzyma.nm.serviceproxy.Factura;
import com.panzyma.nm.view.adapter.GenericAdapter;
import com.panzyma.nm.view.viewholder.CNotaViewHolder;
import com.panzyma.nm.view.viewholder.ClienteViewHolder;
import com.panzyma.nm.view.viewholder.FacturaViewHolder;
import com.panzyma.nordismobile.R;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context; 
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

 @SuppressLint({ "ResourceAsColor", "ResourceAsColor" })
@SuppressWarnings({"rawtypes","unused","unchecked"})
public class DialogCuentasPorCobrar extends Dialog implements Handler.Callback
{   
	private GenericAdapter adapter;
	private ProgressBar progress; 
	private int positioncache=0; 
	private Context mcontext; 
	private Controller mcontroller=null; 
	private String TAG=DialogCuentasPorCobrar.class.getSimpleName(); 
	private long _idSucursal;
	private int fechaFinFac = 0;
    private int fechaInicFac = 0;
    private String estadoFac = "TODOS";
    private boolean soloFacturasConSaldo = true;
    
    private int fechaFinPedidos = 0;
    private int fechaInicPedidos = 0;
    private String estadoPedidos = "TODOS";
    
    private int fechaFinRCol = 0;
    private int fechaInicRCol = 0;
    private String estadoRCol = "TODOS";
    
    private int fechaFinNC = 0;
    private int fechaInicNC = 0;
    private String estadoNC = "AUTORIZADA";
    
    private int fechaFinND = 0;
    private int fechaInicND = 0;
    private String estadoND = "AUTORIZADA"; 
    
	public DialogCuentasPorCobrar(Context context,int theme,CCCliente obj,long idSucursal) {
		super(context,theme); 
		setContentView(R.layout.cuentas_x_cobrar);     
    	mcontext =this.getContext();  
    	try
    	{
    		mcontroller= new Controller(new BClienteM(),DialogCuentasPorCobrar.this); 
			mcontroller.addOutboxHandler(new Handler(DialogCuentasPorCobrar.this));
		    progress=(ProgressBar)this.findViewById(R.id.cxcprogressbar);
    		mcontroller.getInboxHandler().sendEmptyMessage(LOAD_FACTURASCLIENTE_FROM_SERVER); 
    		_idSucursal = idSucursal;
            
            //Inicializando periodo por defecto de pedidos
            fechaFinPedidos = DateUtil.getToday();
            String s = new String(fechaFinPedidos + "");
            fechaInicPedidos = Integer.parseInt(s.substring(0, 6) + "01"); 
            fechaFinRCol = fechaFinPedidos;
            fechaInicRCol = fechaInicPedidos; 
            LoadCliente(obj);  
       	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    		progress.setVisibility(View.INVISIBLE);
    		buildCustomDialog("Error !!!","Error Message:"+e.getMessage()+"\n Cause:"+e.getCause(),ALERT_DIALOG).show();
    	}
    	
	} 
	public void LoadCliente(final CCCliente objL)
	{   

		try	
		{
			
			
			if(objL.getIdCliente()!=0)
			{				
				((TextView) findViewById(R.id.cctextv_detallecliente)).setText(objL.getNombreCliente());
				((TextView) findViewById(R.id.cctextv_detallesaldo)).setText(String.valueOf(objL.getSaldoActual()));
				((TextView) findViewById(R.id.cctextv_detalledisponible)).setText(String.valueOf(objL.getDisponible()));
				((TextView) findViewById(R.id.cctextv_detallelimitecredito)).setText(String.valueOf(objL.getLimiteCredito()));				
			}
						
		}
		catch(Exception e)
		{
			e.printStackTrace();
			progress.setVisibility(View.INVISIBLE);
			buildCustomDialog("Error on LoadCliente!!!","Error Message:"+e.getMessage()+"\n Cause:"+e.getCause(),ALERT_DIALOG).show();
			
		}
	}
 
	@SuppressWarnings("static-access")
	public void LoadFacturas(ArrayList<Factura> data)
	{
		try
		{
			TextView gridheader=(TextView) findViewById(R.id.cxctextv_header2); 
			ListView lvfacturas = (ListView)findViewById(R.id.cxclvgeneric);
			if(data.size()!=0)
			{				
				
			TextView detalleFact=(TextView) findViewById(R.id.cxctextv_detalle_generico);
				
			    adapter = new GenericAdapter(mcontext,FacturaViewHolder.class,data,R.layout.factura); 
			    lvfacturas.setAdapter(adapter);
				adapter.setSelectedPosition(positioncache=0); 
				lvfacturas.setOnItemClickListener(new OnItemClickListener() 
		        {
		            @Override
		            public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
		            { 		  
		            	if((parent.getChildAt(positioncache))!=null)						            							            		
		            		(parent.getChildAt(positioncache)).setBackgroundResource(android.R.color.transparent);						            	 
		            	positioncache=position; 				
		            	adapter.setSelectedPosition(position);  
		            	view.setBackgroundDrawable(mcontext.getResources().getDrawable(R.drawable.action_item_selected));
		            	Toast.makeText(mcontext, "Click", Toast.LENGTH_LONG);
		            	//buildCustomDialog("message !!!","Click",ALERT_DIALOG).show();
		            }
		        });
				
			String s = "Mostrando facturas: ";
		        if (soloFacturasConSaldo) s = s + " con saldo pendiente.";
		        if (fechaInicFac > 0)
		            s = s + " desde " + DateUtil.idateToStr(fechaInicFac);
		        if (fechaFinFac > 0)
		            s = s + " hasta " + DateUtil.idateToStr(fechaFinFac);
		        if (fechaInicFac + fechaFinFac > 0) s = s + ".";
		        if (estadoFac.compareTo("TODOS") != 0)
		            s = s + " con estado " + estadoFac + ".";
		        detalleFact.setText(s);
		        gridheader.setText("Facturas del Cliente("+data.size()+")"); 
	          
	            
			}
			else
			{
				gridheader.setText("Facturas del Cliente(0)");
				Log.d(TAG,"ViewCliente setData");
				TextView txtenty=(TextView) findViewById(R.id.cctxtview_enty); 
	            txtenty.setVisibility(txtenty.VISIBLE);
	            lvfacturas.setEmptyView(txtenty); 
			}
			progress.setVisibility(progress.INVISIBLE);
			
			
			
			 
		}
		catch(Exception e)
		{
			e.printStackTrace();
			buildCustomDialog("Error on LoadFacturas !!!","Error Message:"+e.getMessage()+"\n Cause:"+e.getCause(),ALERT_DIALOG).show();
			progress.setVisibility(progress.INVISIBLE);
		}
	}
	
	@Override
	public boolean handleMessage(Message msg) { 
		switch (msg.what) 
		{
			case C_FACTURACLIENTE:LoadFacturas((ArrayList<Factura>)msg.obj);
		}
		return false;
	}

	public Parameters get_FacturaParameters()
	{ 
	     return new Parameters(
				 (new String[]{"Credentials","idSucursal","fechaInic","fechaFin","mostrarTodasSucursales","soloConSaldo","codEstado"}),
				 (new Object[]{"sa||nordis09||dp",_idSucursal,fechaInicFac,fechaFinFac,false,soloFacturasConSaldo,estadoFac}),
				 (new Type[]{PropertyInfo.STRING_CLASS,PropertyInfo.LONG_CLASS,PropertyInfo.INTEGER_CLASS,PropertyInfo.INTEGER_CLASS,PropertyInfo.BOOLEAN_CLASS,
						 PropertyInfo.BOOLEAN_CLASS,PropertyInfo.STRING_CLASS})); 		 
	}
	public  Dialog buildCustomDialog(String tittle,String msg,int type)
	{
		return new CustomDialog(getContext(),tittle,msg,false,type);	    
	}
}

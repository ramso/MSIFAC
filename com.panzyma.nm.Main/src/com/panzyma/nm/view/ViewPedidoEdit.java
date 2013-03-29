package com.panzyma.nm.view;

import static com.panzyma.nm.controller.ControllerProtocol.ALERT_DIALOG;
import static com.panzyma.nm.controller.ControllerProtocol.C_DATA; 
import static com.panzyma.nm.controller.ControllerProtocol.ERROR; 

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.panzyma.nm.DashBoardActivity;
import com.panzyma.nm.CBridgeM.BPedidoM;
import com.panzyma.nm.auxiliar.CustomDialog;
import com.panzyma.nm.auxiliar.DateUtil;
import com.panzyma.nm.auxiliar.ErrorMessage; 
import com.panzyma.nm.controller.Controller;
import com.panzyma.nm.menu.ActionItem;
import com.panzyma.nm.menu.QuickAction; 
import com.panzyma.nm.serviceproxy.Pedido;
import com.panzyma.nm.view.adapter.GenericAdapter; 
import com.panzyma.nm.viewdialog.DialogCliente;
import com.panzyma.nm.viewdialog.DialogCliente.OnButtonClickListener; 
import com.panzyma.nm.viewmodel.vmCliente;
import com.panzyma.nordismobile.R;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;  
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ViewPedidoEdit extends DashBoardActivity implements Handler.Callback
{ 
	public EditText tbxFecha;
	public EditText tbxNumReferencia;
	public EditText tbxNombreDelCliente;
	public TextView tbxPrecio;
	public Spinner tbxTipoVenta;
	public View gridDetallePedido;	
	
	private Controller controller;
	private GenericAdapter adapter;   
	private ProgressDialog pd;
	private Button Menu; 
	private QuickAction quickAction ; 
	private int positioncache=-1;
	private Display display;
	private static final String TAG = ViewCliente.class.getSimpleName();
	private ViewPedidoEdit me;
	private Pedido pedido;
    private vmCliente cliente;
	
	private static final int ID_SELECCIONAR_CLIENTE  = 1;
	private static final int ID_CONSULTAR_CUENTAS_X_COBRAR = 2;
	private static final int ID_AGREGAR_PRODUCTOS= 3;
	private static final int ID_EDITAR_PRODUCTO= 4;
	private static final int ID_ELIMINAR_PRODUCTO = 5;	
	private static final int ID_CONSULTAR_BONIFICACIONES  = 6;
	private static final int ID_CONSULTAR_LISTA_PRECIOS = 7;
	private static final int ID_CONDICIONES_Y_NOTAS= 8;
	private static final int ID_APLICAR_PROMOCIONES= 9; 
	private static final int ID_DESAPLICAR_PROMOCIONES= 10;	
	private static final int ID_EXONERAR_IMPUESTO = 11;
	private static final int ID_GUARDAR= 12;
	private static final int ID_ENVIAR= 13; 
	private static final int ID_IMPRIMIR_COMPROBANTE= 14;
	private static final int ID_CERRAR = 15; 
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{	 
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.pedido_edit); 
		setHeader(getString(R.string.PedidoActivityTitle),true, false); 
		
		try 
	    {
	    	me=this;
			controller= new Controller(new BPedidoM(),this); 
			controller.addOutboxHandler(new Handler(this)); 
	       // controller.getInboxHandler().sendEmptyMessage(LOAD_DATA_FROM_LOCALHOST);  	   
	        WindowManager wm = (WindowManager) this.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
            display = wm.getDefaultDisplay();
	        initComponent();
	        
		}catch (Exception e) { 
			e.printStackTrace();
			buildCustomDialog("Error Message",e.getMessage()+"\n Cause:"+e.getCause(),ALERT_DIALOG).show();
		}	
		
	}

	public void initComponent()
	{ 	
	    tbxFecha=(EditText) findViewById(R.id.pddetextv_detalle_fecha);
	    tbxNumReferencia=(EditText) findViewById(R.id.pdddetextv_detalle_numref);
		tbxNombreDelCliente=(EditText) findViewById(R.id.pddtextv_detallecliente);
		tbxPrecio=(TextView) findViewById(R.id.pddtextv_detalleprecio);;
		tbxTipoVenta=(Spinner) findViewById(R.id.pddcombox_detalletipo);
		gridDetallePedido=findViewById(R.id.pddgrilla); 
		if(pedido==null)
		{
			pedido = new Pedido();
            cliente = null;
            pedido.setCodEstado("REGISTRADO");
            pedido.setId(0);          
            pedido.setFecha(DateUtil.d2i(Calendar.getInstance().getTime()));
            pedido.setNumeroCentral(0);
            pedido.setNumeroMovil(0);
          //  pedido.setObjVendedorID(DataStore.getUsuario().getId());
            pedido.setTipo("CR"); //Crédito
            pedido.setExento(false);
            pedido.setAutorizacionDGI("");
		}
		
		//Fecha del Pedido
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        if (pedido.getFecha() == 0)   
            pedido.setFecha(DateUtil.d2i(Calendar.getInstance().getTime()));   
       // tbxFecha.setText(pedido.getFecha());
      /*  if (pedido.getNumeroMovil() > 0)
            txtNumReferencia.setText(Ventas.getNumeroPedido(pedido.getNumeroMovil()));*/
		
		initMenu();		
		
	}
	
	public void initMenu()
    { 
       
		quickAction = new QuickAction(this, QuickAction.VERTICAL,1);	
	    quickAction.addActionItem(new ActionItem(ID_SELECCIONAR_CLIENTE, "Seleccionar Cliente"));
		quickAction.addActionItem(new ActionItem(ID_AGREGAR_PRODUCTOS, "Agregar Productos"));
		quickAction.addActionItem(new ActionItem(ID_CONDICIONES_Y_NOTAS, "Agregar Condición Y Nota"));		
		quickAction.addActionItem(null);
		quickAction.addActionItem(new ActionItem(ID_EDITAR_PRODUCTO, "Editar Producto"));
		quickAction.addActionItem(new ActionItem(ID_ELIMINAR_PRODUCTO, "Eliminar Productos"));		
        quickAction.addActionItem(null);		
        quickAction.addActionItem(new ActionItem(ID_CONSULTAR_CUENTAS_X_COBRAR, "Consultar Cuentas X Cobrar"));
        quickAction.addActionItem(new ActionItem(ID_CONSULTAR_BONIFICACIONES, "Consultar Bonificaciones"));
        quickAction.addActionItem(new ActionItem(ID_CONSULTAR_LISTA_PRECIOS, "Consultar Lista de Precios"));        
        quickAction.addActionItem(null);        
        quickAction.addActionItem(new ActionItem(ID_APLICAR_PROMOCIONES, "Aplicar Promociones"));
        quickAction.addActionItem(new ActionItem(ID_DESAPLICAR_PROMOCIONES, "Des Aplicar Promociones"));
        quickAction.addActionItem(new ActionItem(ID_EXONERAR_IMPUESTO, "Aplicar Exoneración de Impuesto"));        
        quickAction.addActionItem(null);        
        quickAction.addActionItem(new ActionItem(ID_GUARDAR, "Guardar"));
        quickAction.addActionItem(new ActionItem(ID_ENVIAR, "Enviar"));        
        quickAction.addActionItem(null);        
        quickAction.addActionItem(new ActionItem(ID_IMPRIMIR_COMPROBANTE, "Imprimir Comprobante")); 
        quickAction.addActionItem(null);
        quickAction.addActionItem(new ActionItem(ID_CERRAR, "Cerrar"));
         
		quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() 
		{			
			@Override
			public void onItemClick(QuickAction source, int pos, int actionId) 
			{				
				ActionItem actionItem = quickAction.getActionItem(pos);    
				if (actionId ==ID_SELECCIONAR_CLIENTE)
				{ 
					DialogCliente dc=new DialogCliente(me,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
					dc.setOnDialogClientButtonClickListener
					(new OnButtonClickListener()
						{

							@Override
							public void onButtonClick(vmCliente _cliente) 
							{ 
								cliente=_cliente;
								tbxNombreDelCliente.setText(cliente.getNombreCliente());
							}
						}
					); 
					Window window = dc.getWindow(); 
				    window.setGravity(Gravity.CENTER); 
				    window.setLayout(display.getWidth()-40,display.getHeight()-110);  
					dc.show();
				}	
				else if (actionId == ID_AGREGAR_PRODUCTOS)
				{
					
				}				
				else if (actionId == ID_AGREGAR_PRODUCTOS)
				{
					Toast.makeText(getApplicationContext(), actionItem.getTitle() + " selected", Toast.LENGTH_SHORT).show();
				}
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
				quickAction.dismiss();
			}
		});
    	
    	
    }    
	
	@Override
    public boolean onKeyUp(int keyCode, KeyEvent event) 
    {
        if (keyCode == KeyEvent.KEYCODE_MENU) 
        {        	
        	Menu = (Button)gridDetallePedido.findViewById(R.id.btnmenu);
        	quickAction.show(Menu,display,true);
            return true;
        }
        return super.onKeyUp(keyCode, event); 
    } 
	
	@Override
	public boolean handleMessage(Message msg) {


		switch (msg.what) 
		{		 
			case C_DATA:   
				
					setData((ArrayList<Pedido>)((msg.obj==null)?new ArrayList<Pedido>():msg.obj),C_DATA);
					return true;
 		
			case ERROR:		
				
					pd.dismiss();
					ErrorMessage error=((ErrorMessage)msg.obj);
					buildCustomDialog(error.getTittle(),error.getMessage()+error.getCause(),ALERT_DIALOG).show();				 
					return true;						
		}
		
		
		return false;
	}
	
	public void setData(ArrayList<Pedido> Lpedido, int what)
	{
		
	}
 
	public  Dialog buildCustomDialog(String tittle,String msg,int type)
	{
		return new CustomDialog(this.getApplicationContext(),tittle,msg,false,type);		 		 
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

}

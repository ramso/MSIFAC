package com.panzyma.nm.model;

import java.lang.reflect.Type;
import java.util.ArrayList;
 
import org.ksoap2.serialization.PropertyInfo;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;  

import com.panzyma.nm.auxiliar.NMTranslate;
import com.panzyma.nm.serviceproxy.CCCliente;
import com.panzyma.nm.serviceproxy.Cliente;
import com.panzyma.nm.serviceproxy.Factura;
import com.panzyma.nm.viewmodel.vmCliente;
import com.panzyma.nm.auxiliar.NMComunicacion;
import com.panzyma.nm.auxiliar.NMConfig;
import com.panzyma.nm.auxiliar.Parameters;
import com.panzyma.nm.datastore.DatabaseProvider;

public class ModelCliente 
{  
	static String TAG=ModelCliente.class.getSimpleName();
	
	/*Method del modulo de Cliente pertenecientes a la pantalla principal del modulo*/
	public ModelCliente() {} 
	 
	public synchronized static ArrayList<Cliente> getArrayCustomerFromServer(String Credencials,String UsuarioVendedor,Integer page,Integer rowpage)throws Exception
	 { 
		ArrayList<Cliente> modelcliente =new ArrayList<Cliente>();  
		ArrayList<Parameters> arrayparams=new ArrayList<Parameters>();
		String[] paramname=new String[]{"Credentials","UsuarioVendedor","page","rowsPerPage"}; 
		Object[] values=new Object[]{Credencials,UsuarioVendedor,page,rowpage}; 
		Type[] type=new Type[]{PropertyInfo.STRING_CLASS,PropertyInfo.STRING_CLASS,PropertyInfo.INTEGER_CLASS,PropertyInfo.INTEGER_CLASS};
		 
		for(int i=0;i<4;i++)
		{
			Parameters params=new Parameters();
			params.setName(paramname[i]);
			params.setValue(values[i]);
			params.setType(type[i]);  
			arrayparams.add(params); 
		} 
		ArrayList<Cliente> modelclienteL =  NMTranslate.ToCollection( ( NMComunicacion.InvokeMethod (arrayparams,NMConfig.URL,NMConfig.NAME_SPACE,NMConfig.MethodName.GetClientesPaged)),Cliente.class);
		if(modelclienteL.size()!=0)
			modelcliente.addAll(modelclienteL);  
		 return modelcliente;
	 }
	
	public synchronized static Cliente getCustomerFromServer(String Credentials,long idSucursal) throws Exception
	{
	    ArrayList<Parameters> arrayparams=new ArrayList<Parameters>();
		String[] paramname=new String[]{"Credentials","idSucursal"}; 
		Object[] values=new Object[]{Credentials,idSucursal}; 
		Type[] type=new Type[]{PropertyInfo.STRING_CLASS,PropertyInfo.LONG_CLASS};		 
		for(int i=0;i<2;i++)
		{
			Parameters params=new Parameters();
			params.setName(paramname[i]);
			params.setValue(values[i]);
			params.setType(type[i]);  
			arrayparams.add(params); 
		}  			 
		return   (NMTranslate.ToObject(( NMComunicacion.InvokeMethod(arrayparams,NMConfig.URL,NMConfig.NAME_SPACE,NMConfig.MethodName.GetCliente)),new Cliente()));		  
	 }
	
	public synchronized static ArrayList<vmCliente> getArrayCustomerFromLocalHost(ContentResolver content)throws Exception
	{  		 
		 String[] projection = new String[] {NMConfig.Cliente.IdCliente,NMConfig.Cliente.IdSucursal,
				 							 NMConfig.Cliente.NombreCliente,NMConfig.Cliente.Codigo,
				 							 NMConfig.Cliente.Ubicacion};
 
		 ArrayList<vmCliente> arrayclient=new ArrayList<vmCliente>(); 
		 Cursor cur = content.query(DatabaseProvider.CONTENT_URI_CLIENTE,
							        projection, //Columnas a devolver
							        null,       //Condición de la query
							        null,       //Argumentos variables de la query
							        null);  
		 if (cur.moveToFirst()) 
		 {  
				
	            do{
	            	
	               
	            	arrayclient.add(
	            			         new vmCliente(
							                		Long.parseLong(cur.getString(cur.getColumnIndex(projection[0]))),
							                		Long.parseLong(cur.getString(cur.getColumnIndex(projection[1]))),
							                		cur.getString(cur.getColumnIndex(projection[2])),
							                		cur.getString(cur.getColumnIndex(projection[3])),
							                		cur.getString(cur.getColumnIndex(projection[4]))
	                		                       )
	            	                );
	            }while (cur.moveToNext());
		 }   
		 
		 return arrayclient;
	}
	
	public synchronized static void RegisterCustomer_From_LocalHost(final ArrayList<Cliente> objL,Context ctn)throws Exception
	{   
		DatabaseProvider.InsertCustomerCollection(objL,ctn);
	}
	
	public synchronized static void UpdateCustomer_From_LocalHost(final ArrayList<Cliente> objL,Context ctn) throws Exception
	{  
		DatabaseProvider.UpdateCustomer(objL,ctn); 
	}  
	
	/*Method del modulo de Cliente pertenecientes a la pantalla Ficha del Cliente*/ 	
	public synchronized static CCCliente getFichaCustomerFromServer(String Credentials,long idSucursal) throws Exception
	{
		Parameters params=new Parameters((new String[]{"Credentials","idSucursal"}),
										 (new Object[]{Credentials,idSucursal}),
										 (new Type[]{PropertyInfo.STRING_CLASS,PropertyInfo.LONG_CLASS}));
		return  NMTranslate.ToObject(NMComunicacion.InvokeMethod(params.getParameters(),NMConfig.URL,NMConfig.NAME_SPACE,NMConfig.MethodName.GetCCCliente),new CCCliente());		 
	} 
	
	public synchronized static ArrayList<Factura> getFacturasClienteFromServer(Parameters params) throws Exception
	{		
		return  NMTranslate.ToCollection(NMComunicacion.InvokeMethod(params.getParameters(),NMConfig.URL,NMConfig.NAME_SPACE,NMConfig.MethodName.TraerFacturasCliente),Factura.class);	 
	}  
	 
}

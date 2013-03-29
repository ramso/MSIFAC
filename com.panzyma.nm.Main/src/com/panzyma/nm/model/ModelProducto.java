package com.panzyma.nm.model;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.ksoap2.serialization.PropertyInfo;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;

import com.panzyma.nm.auxiliar.NMComunicacion;
import com.panzyma.nm.auxiliar.NMConfig; 
import com.panzyma.nm.auxiliar.NMTranslate;
import com.panzyma.nm.auxiliar.Parameters; 
import com.panzyma.nm.datastore.DatabaseProvider;
import com.panzyma.nm.serviceproxy.Producto;
import com.panzyma.nm.viewmodel.vmProducto;

public class ModelProducto 
{
	static Object lock=new Object();
    static String TAG=ModelProducto.class.getSimpleName();
   public ModelProducto() {}
   
   public synchronized static ArrayList<Producto> getArrayProductoFromServer(String Credentials,String UsuarioVendedor,Integer page,Integer rowpage)throws Exception
   {  
	   Parameters params=new Parameters((new String[]{"Credentials","UsuarioVendedor","page","rowsPerPage"}),
					 (new Object[]{Credentials,UsuarioVendedor,page,rowpage}),
					 (new Type[]{PropertyInfo.STRING_CLASS,PropertyInfo.STRING_CLASS,PropertyInfo.INTEGER_CLASS,PropertyInfo.INTEGER_CLASS})); 
	 
	   return  NMTranslate.ToCollection(NMComunicacion.InvokeMethod(params.getParameters(),NMConfig.URL,NMConfig.NAME_SPACE,NMConfig.MethodName.GetProductosPaged),Producto.class);
   }
   
	public  static ArrayList<vmProducto> getArrayCustomerFromLocalHost(ContentResolver content)throws Exception
	{  		 
		 String[] projection = new String[] {NMConfig.Producto.Id,
				 							 NMConfig.Producto.Codigo,
				 							 NMConfig.Producto.Nombre,
				 							 NMConfig.Producto.Disponible};
		 int count=0;
		 ArrayList<vmProducto> a_vmprod=new ArrayList<vmProducto>(); 
		 Cursor cur = content.query(DatabaseProvider.CONTENT_URI_PRODUCTO,
							        projection, //Columnas a devolver
							        null,       //Condición de la query
							        null,       //Argumentos variables de la query
							        null);  
		 if (cur.moveToFirst()) 
		 {  
	            do{ 
	            	a_vmprod.add(new vmProducto( Long.parseLong(cur.getString(cur.getColumnIndex(projection[0]))),
	            								 cur.getString(cur.getColumnIndex(projection[1])),
	            								 cur.getString(cur.getColumnIndex(projection[2])),
	            								 Integer.parseInt(cur.getString(cur.getColumnIndex(projection[3]))))
	            	                           );
	            }while (cur.moveToNext());
		 }   
		 
		 return a_vmprod;
	}
   
   
   public  static void RegisterProduct_From_LocalHost(final ArrayList<Producto> objL,Context ctn)throws Exception
   {   
	   synchronized(lock)
	   {
		   DatabaseProvider.InsertProductCollection(objL,ctn);
	   } 
   }
   
   
}

package com.panzyma.nm.datastore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.panzyma.nm.auxiliar.NMConfig;
import com.panzyma.nm.serviceproxy.CCNotaCredito;
import com.panzyma.nm.serviceproxy.CCNotaDebito;
import com.panzyma.nm.serviceproxy.Cliente;
import com.panzyma.nm.serviceproxy.DescuentoProveedor;
import com.panzyma.nm.serviceproxy.Factura;
import com.panzyma.nm.serviceproxy.Lote;
import com.panzyma.nm.serviceproxy.MontoProveedor;
import com.panzyma.nm.serviceproxy.Producto;
import com.panzyma.nm.serviceproxy.PromocionCobro;

import android.annotation.SuppressLint;
import android.content.ContentProvider; 
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class DatabaseProvider extends ContentProvider 
{ 
	//Definición del CONTENT_URI
	public static final String CONTEXT_PREFIX="content://";
	public static final String AUTHORITY="com.panzyma.nm.datastore.databaseprovider";
	public static final String CONTENT_URI="content://com.panzyma.nm.datastore.databaseprovider"; 
	
	public static final Uri CONTENT_URI_CLIENTE = Uri.parse(CONTENT_URI+"/cliente");
	public static final Uri CONTENT_URI_FACTURA = Uri.parse(CONTENT_URI+"/factura");
	public static final Uri CONTENT_URI_PROMOCIONCOBRO = Uri.parse(CONTENT_URI+"/promocioncobro");
	public static final Uri CONTENT_URI_MONTOPROVEEDOR = Uri.parse(CONTENT_URI+"/montoproveedor");
	public static final Uri CONTENT_URI_CCNOTACREDITO = Uri.parse(CONTENT_URI+"/ccnotacredito");
	public static final Uri CONTENT_URI_CCNOTADEBITO = Uri.parse(CONTENT_URI+"/ccnotadebito");
	public static final Uri CONTENT_URI_DESCUENTOPROVEEDOR = Uri.parse(CONTENT_URI+"/descuentoproveedor");
	
	public static final Uri CONTENT_URI_PRODUCTO = Uri.parse(CONTENT_URI+"/producto");
	public static final Uri CONTENT_URI_LOTE = Uri.parse(CONTENT_URI+"/lote");
	
	
	//Necesario para UriMatcher
	private static final int CLIENTE = 1;
	private static final int CLIENTE_ID = 2;
	private static final int FACTURA=3;
	private static final int FACTURA_ID=4;
	private static final int PROMOCIONCOBRO = 5;
	private static final int PROMOCIONCOBRO_ID = 6;
	private static final int MONTOPROVEEDOR=7;
	private static final int MONTOPROVEEDOR_ID=8;
	private static final int CCNOTACREDITO=9;
	private static final int CCNOTACREDITO_ID=10;
	private static final int CCNOTADEBITO = 11;
	private static final int CCNOTADEBITO_ID =12;
	private static final int DESCUENTOPROVEEDOR=13;
	private static final int DESCUENTOPROVEEDOR_ID=14;
	private static final int CONTENT_URI_LOCALID=15;
	
	private static final int PRODUCTO=16;
	private static final int PRODUCTO_ID=17;
	private static final int LOTE=18;
	private static final int LOTE_ID=19;
	
	
	private static final UriMatcher uriMatcher;
	
	//Base de datos
	private NM_SQLiteHelper dbhelper;
	private SQLiteDatabase db;
	
	private static final String DATABASE_NAME = "NM";
	private static final int BD_VERSION = 1;
	
	private static final String TABLA_CLIENTE = "Cliente";
	private static final String TABLA_FACTURA = "Factura";
	private static final String TABLA_PROMOCIONCOBRO = "PromocionCobro";
	private static final String TABLA_MONTOPROVEEDOR = "MontoProveedor";
	private static final String TABLA_CCNOTACREDITO = "CCNotaCredito";
	private static final String TABLA_CCNOTADEBITO = "CCNotaDebito";
	private static final String TABLA_DESCUENTOPROVEEDOR = "DescuentoProveedor";
	
	private static final String TABLA_PRODUCTO = "Producto";
	private static final String TABLA_LOTE = "Lote";
	
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, "cliente", CLIENTE);
		uriMatcher.addURI(AUTHORITY, "cliente/#", CLIENTE_ID);
		
		uriMatcher.addURI(AUTHORITY, "factura", FACTURA);
		uriMatcher.addURI(AUTHORITY, "factura/#", FACTURA_ID);
		
		uriMatcher.addURI(AUTHORITY, "promocioncobro", PROMOCIONCOBRO );
		uriMatcher.addURI(AUTHORITY, "promocioncobro/#", PROMOCIONCOBRO_ID);
		
		uriMatcher.addURI(AUTHORITY, "montoproveedor", MONTOPROVEEDOR);
		uriMatcher.addURI(AUTHORITY, "montoproveedor/#", MONTOPROVEEDOR_ID);
		
		uriMatcher.addURI(AUTHORITY, "ccnotacredito", CCNOTACREDITO);
		uriMatcher.addURI(AUTHORITY, "ccnotacredito/#", CCNOTACREDITO_ID);
		
		uriMatcher.addURI(AUTHORITY, "ccnotadebito", CCNOTADEBITO );
		uriMatcher.addURI(AUTHORITY, "ccnotadebito/#",CCNOTADEBITO_ID);
		
		uriMatcher.addURI(AUTHORITY, "descuentoproveedor", DESCUENTOPROVEEDOR);
		uriMatcher.addURI(AUTHORITY, "descuentoproveedor/#", DESCUENTOPROVEEDOR_ID);
		
		
		
		uriMatcher.addURI(AUTHORITY, "producto", PRODUCTO);
		uriMatcher.addURI(AUTHORITY, "producto/#", PRODUCTO_ID);
	}
	
	@Override
	public boolean onCreate() 
	{		
		dbhelper = new NM_SQLiteHelper(getContext(), DATABASE_NAME, null, BD_VERSION); 
		return true;
	}
	 
	private SQLiteDatabase getOrOpenDataBase()
	{
		SQLiteDatabase bdd=null;
		if(this.db!=null && db.isOpen())
			bdd=db; 
		else if(dbhelper!=null)
			bdd=dbhelper.getWritableDatabase();
		else
		{
		   NM_SQLiteHelper d = new NM_SQLiteHelper(getContext(), DATABASE_NAME, null, BD_VERSION);
		   bdd=d.getWritableDatabase();
		}
		return bdd;
	}
	
	
	@SuppressWarnings({ "rawtypes"})
	@Override
	public Cursor query(Uri uri, String[] columns, String selection, String[] selectionArgs, String sortOrder)
	{ 		 
		Cursor c = null; 
		List<Map.Entry> coll=FromWhere(uri);
		String TABLE_NAME_L=(coll.size()>0)?coll.get(0).getValue().toString():null;
		
		String where =selection;
		if(( coll.get(1).getKey())==String.valueOf(CONTENT_URI_LOCALID))
			where=coll.get(1).getValue().toString();  
		SQLiteDatabase db = getOrOpenDataBase();		
		c = db.query(TABLE_NAME_L, columns, where,selectionArgs, null, null, sortOrder);
		 
		return c;
	}
    
	
   
	public static void InsertCustomerCollection(ArrayList<Cliente> objL,Context cnt) throws Exception
	{		 
		NM_SQLiteHelper d = new NM_SQLiteHelper(cnt, DATABASE_NAME, null, BD_VERSION);
		SQLiteDatabase bdd=d.getWritableDatabase();
		bdd.beginTransaction();
	    DeleteALLCustomerRecords(bdd);
		RegisterCustomer(objL,bdd);
		bdd.setTransactionSuccessful(); 
	 
		if(bdd!=null || (bdd.isOpen()))
		{				
			bdd.endTransaction();
			bdd.close();
		}  
	} 
	
	
	public static void InsertProductCollection(ArrayList<Producto> objL,Context cnt) throws Exception
	{		 
		NM_SQLiteHelper d = new NM_SQLiteHelper(cnt, DATABASE_NAME, null, BD_VERSION);
		SQLiteDatabase bdd=d.getWritableDatabase();
		bdd.beginTransaction(); 	 
		DeleteALLProductRecords(bdd);
		RegisterProduct(objL,bdd);
		bdd.setTransactionSuccessful(); 
	 
		if(bdd!=null || (bdd.isOpen()))
		{				
			bdd.endTransaction();
			bdd.close();
		}  
	} 
	public static void RegisterProduct(ArrayList<Producto> objL,SQLiteDatabase bdd) throws Exception
	{
		ContentValues values;   
		for(Producto prod:objL)
		{												
		    values  = new ContentValues();
		    
	        values.put(NMConfig.Producto.Id, prod.getId());
		    values.put(NMConfig.Producto.Codigo, prod.getCodigo());
		    values.put(NMConfig.Producto.Nombre, prod.getNombre());
		    values.put(NMConfig.Producto.EsGravable, prod.isEsGravable());
		    values.put(NMConfig.Producto.ListaPrecios, prod.getListaPrecios());
		    values.put(NMConfig.Producto.ListaBonificaciones, prod.getListaBonificaciones());
		    values.put(NMConfig.Producto.CatPrecios, prod.getCatPrecios());
		    values.put(NMConfig.Producto.Disponible, prod.getDisponible());
		    values.put(NMConfig.Producto.PermiteDevolucion, prod.isPermiteDevolucion());
		    values.put(NMConfig.Producto.LoteRequerido, prod.isLoteRequerido());
		    values.put(NMConfig.Producto.ObjProveedorID, prod.getObjProveedorID());
		    values.put(NMConfig.Producto.DiasAntesVen, prod.getDiasAntesVen());
		    values.put(NMConfig.Producto.DiasDespuesVen, prod.getDiasDespuesVen()); 
		    bdd.insert(TABLA_PRODUCTO, null, values);
		    for(Lote lote:prod.getListaLotes())
		    {
		    	values  = new ContentValues();
		    	values.put(NMConfig.Producto.Lote.Id, lote.getId());
			    values.put(NMConfig.Producto.Lote.NumeroLote,lote.getNumeroLote());
			    values.put(NMConfig.Producto.Lote.FechaVencimiento,lote.getFechaVencimiento());
			    values.put(NMConfig.Producto.ObjProductoID,prod.getId());
			    bdd.insert(TABLA_LOTE, null, values);
		    }
		    
		}
	}
	
	
	public static void  UpdateCustomer(ArrayList<Cliente> objL,Context cnt) throws Exception
	{	  
		NM_SQLiteHelper d = new NM_SQLiteHelper(cnt, DATABASE_NAME, null, BD_VERSION);
		SQLiteDatabase bdd=d.getWritableDatabase();
		bdd.beginTransaction(); 	
		
		DeleteCustomer(objL,bdd);
	    RegisterCustomer(objL,bdd);	 
		bdd.setTransactionSuccessful();  
		
		if(bdd!=null || (bdd.isOpen()))
		{				
			bdd.endTransaction();
			bdd.close();
		} 
	}
	
    public static void RegisterCustomer(ArrayList<Cliente> objL,SQLiteDatabase bdd) throws Exception
	{
		ContentValues values;   
		for(Cliente client:objL)
		{												
		    values  = new ContentValues(); 
			values.put(NMConfig.Cliente.IdCliente,client.getIdCliente());
			values.put(NMConfig.Cliente.NombreCliente,client.getNombreCliente());
			values.put(NMConfig.Cliente.IdSucursal,client.getIdSucursal());
			values.put(NMConfig.Cliente.Codigo,client.getCodigo());
			values.put(NMConfig.Cliente.CodTipoPrecio,client.getCodTipoPrecio());
			values.put(NMConfig.Cliente.DesTipoPrecio,client.getDesTipoPrecio());
			values.put(NMConfig.Cliente.objPrecioVentaID,client.getObjPrecioVentaID());
			values.put(NMConfig.Cliente.objCategoriaClienteID,client.getObjCategoriaClienteID());
			values.put(NMConfig.Cliente.objTipoClienteID,client.getObjTipoClienteID());
			values.put(NMConfig.Cliente.AplicaBonificacion,client.getAplicaBonificacion());
			values.put(NMConfig.Cliente.PermiteBonifEspecial,client.getPermiteBonifEspecial());
			values.put(NMConfig.Cliente.PermitePrecioEspecial,client.getPermitePrecioEspecial());
			values.put(NMConfig.Cliente.UG,client.getUG());
			values.put(NMConfig.Cliente.Ubicacion,client.getUbicacion());
			values.put(NMConfig.Cliente.NombreLegalCliente,client.getNombreLegalCliente());
			values.put(NMConfig.Cliente.AplicaOtrasDeducciones,client.getAplicaOtrasDeducciones());
			values.put(NMConfig.Cliente.MontoMinimoAbono,client.getMontoMinimoAbono());
			values.put(NMConfig.Cliente.PlazoDescuento,client.getPlazoDescuento());
			values.put(NMConfig.Cliente.PermiteDevolucion,client.getPermiteDevolucion());
			bdd.insert(TABLA_CLIENTE, null, values);									 
			if(client.getFacturasPendientes()!=null)
				for(Factura fact: client.getFacturasPendientes())
				{
					values  = new ContentValues();
					values.put(NMConfig.Cliente.Factura.Id,fact.getId());
					values.put(NMConfig.Cliente.Factura.NombreSucursal,fact.getNombreSucursal());
					values.put(NMConfig.Cliente.Factura.NoFactura,fact.getNoFactura());
					values.put(NMConfig.Cliente.Factura.Tipo,fact.getTipo());
					values.put(NMConfig.Cliente.Factura.NoPedido,fact.getNoPedido());
					values.put(NMConfig.Cliente.Factura.CodEstado,fact.getCodEstado());
					values.put(NMConfig.Cliente.Factura.Estado,fact.getEstado());
					values.put(NMConfig.Cliente.Factura.Fecha,fact.getFecha());
					values.put(NMConfig.Cliente.Factura.FechaVencimiento,fact.getFechaVencimiento());
					values.put(NMConfig.Cliente.Factura.FechaAppDescPP,fact.getFechaAppDescPP());
					values.put(NMConfig.Cliente.Factura.Dias,fact.getDias());
					values.put(NMConfig.Cliente.Factura.TotalFacturado,fact.getTotalFacturado());
					values.put(NMConfig.Cliente.Factura.Abonado,fact.getAbonado());
					values.put(NMConfig.Cliente.Factura.Descontado,fact.getAbonado());
					values.put(NMConfig.Cliente.Factura.Retenido,fact.getAbonado());
					values.put(NMConfig.Cliente.Factura.Otro,fact.getAbonado());
					values.put(NMConfig.Cliente.Factura.Saldo,fact.getSaldo());
					values.put(NMConfig.Cliente.Factura.Exenta,fact.getAbonado());
					values.put(NMConfig.Cliente.Factura.SubtotalFactura,fact.getSubtotalFactura());
					values.put(NMConfig.Cliente.Factura.DescuentoFactura,fact.getDescuentoFactura());
					values.put(NMConfig.Cliente.Factura.ImpuestoFactura,fact.getImpuestoFactura());
					values.put(NMConfig.Cliente.Factura.PuedeAplicarDescPP,fact.getPuedeAplicarDescPP());
					values.put(NMConfig.Cliente.objSucursalID,client.getIdSucursal());
					bdd.insert(TABLA_FACTURA, null, values);
					if(fact.getDetallePromocionCobro()!=null)
						for(PromocionCobro prom:fact.getDetallePromocionCobro())
						{
							values  = new ContentValues();
							values.put(NMConfig.Cliente.Factura.PromocionCobro.FacturasAplicacion, prom.getFacturasAplicacion());
							values.put(NMConfig.Cliente.Factura.PromocionCobro.TipoDescuento, prom.getTipoDescuento());
							values.put(NMConfig.Cliente.Factura.PromocionCobro.Descuento, prom.getDescuento());
							values.put(NMConfig.Cliente.Factura.objFacturaID, fact.getId());
							bdd.insert(TABLA_PROMOCIONCOBRO, null, values);
						}
					if(fact.getDetalleMontoProveedor()!=null)
						for(MontoProveedor monprov:fact.getDetalleMontoProveedor())
						{
							values  = new ContentValues();
							values.put(NMConfig.Cliente.Factura.MontoProveedor.ObjProveedorID, monprov.getObjProveedorID());
							values.put(NMConfig.Cliente.Factura.MontoProveedor.CodProveedor, monprov.getCodProveedor());
							values.put(NMConfig.Cliente.Factura.MontoProveedor.Monto, monprov.getMonto());
							values.put(NMConfig.Cliente.Factura.objFacturaID, fact.getId());
							bdd.insert(TABLA_MONTOPROVEEDOR, null, values);
						}
					
				}
			if(client.getNotasCreditoPendientes()!=null)
				for(CCNotaCredito ccnotac:client.getNotasCreditoPendientes())
				{ 
					values  = new ContentValues();
					values.put(NMConfig.Cliente.CCNotaCredito.Id, ccnotac.getId());
					values.put(NMConfig.Cliente.CCNotaCredito.NombreSucursal, ccnotac.getNombreSucursal());
					values.put(NMConfig.Cliente.CCNotaCredito.Estado, ccnotac.getEstado());
					values.put(NMConfig.Cliente.CCNotaCredito.Numero,ccnotac.getNumero());
					values.put(NMConfig.Cliente.CCNotaCredito.Fecha, ccnotac.getFecha());
					values.put(NMConfig.Cliente.CCNotaCredito.FechaVence, ccnotac.getFechaVence());
					values.put(NMConfig.Cliente.CCNotaCredito.Concepto, ccnotac.getConcepto());
					values.put(NMConfig.Cliente.CCNotaCredito.Monto,ccnotac.getMonto());
					values.put(NMConfig.Cliente.CCNotaCredito.NumRColAplic, ccnotac.getNumRColAplic());
					values.put(NMConfig.Cliente.CCNotaCredito.CodEstado, ccnotac.getCodEstado());
					values.put(NMConfig.Cliente.CCNotaCredito.Descripcion, ccnotac.getDescripcion());
					values.put(NMConfig.Cliente.CCNotaCredito.Referencia, ccnotac.getReferencia());
					values.put(NMConfig.Cliente.CCNotaCredito.CodConcepto, ccnotac.getCodConcepto());
					values.put(NMConfig.Cliente.objSucursalID,client.getIdSucursal());
					bdd.insert(TABLA_CCNOTACREDITO, null, values);
				}
			if(client.getNotasDebitoPendientes()!=null)
				for(CCNotaDebito ccnotad:client.getNotasDebitoPendientes())
				{
					values  = new ContentValues();
					values.put(NMConfig.Cliente.CCNotaDebito.Id, ccnotad.getId());
					values.put(NMConfig.Cliente.CCNotaDebito.NombreSucursal, ccnotad.getNombreSucursal());
					values.put(NMConfig.Cliente.CCNotaDebito.Estado, ccnotad.getEstado());
					values.put(NMConfig.Cliente.CCNotaDebito.Numero,ccnotad.getNumero());
					values.put(NMConfig.Cliente.CCNotaDebito.Fecha, ccnotad.getFecha());
					values.put(NMConfig.Cliente.CCNotaDebito.FechaVence, ccnotad.getFechaVence());
					values.put(NMConfig.Cliente.CCNotaDebito.Dias, ccnotad.getDias());
					values.put(NMConfig.Cliente.CCNotaDebito.Concepto, ccnotad.getConcepto());
					values.put(NMConfig.Cliente.CCNotaDebito.Monto,ccnotad.getMonto());
					values.put(NMConfig.Cliente.CCNotaDebito.MontoAbonado, ccnotad.getMontoAbonado());
					values.put(NMConfig.Cliente.CCNotaDebito.Saldo, ccnotad.getSaldo());
					values.put(NMConfig.Cliente.CCNotaDebito.CodEstado, ccnotad.getCodEstado());
					values.put(NMConfig.Cliente.CCNotaDebito.Descripcion, ccnotad.getDescripcion()); 
					values.put(NMConfig.Cliente.objSucursalID,client.getIdSucursal());
					bdd.insert(TABLA_CCNOTADEBITO, null, values);
				}
			if(client.getDescuentosProveedor()!=null)
				for(DescuentoProveedor descprov:client.getDescuentosProveedor())
				{
					values  = new ContentValues();
					values.put(NMConfig.Cliente.DescuentoProveedor.ObjProveedorID, descprov.getObjProveedorID());
					values.put(NMConfig.Cliente.DescuentoProveedor.PrcDescuento, descprov.getPrcDescuento());
					values.put(NMConfig.Cliente.objSucursalID,client.getIdSucursal());
					bdd.insert(TABLA_DESCUENTOPROVEEDOR, null, values);
				}
		} 
		
	}  
	
	
	public static void DeleteCustomer(ArrayList<Cliente> objL,SQLiteDatabase bdd) throws Exception
    {  
			for(Cliente client:objL)
			{	
				
				bdd.delete(TABLA_DESCUENTOPROVEEDOR, NMConfig.Cliente.objSucursalID+"="+String.valueOf(client.getIdSucursal()),null);
				bdd.delete(TABLA_CCNOTADEBITO, NMConfig.Cliente.objSucursalID+"="+String.valueOf(client.getIdSucursal()),null);
				bdd.delete(TABLA_CCNOTACREDITO, NMConfig.Cliente.objSucursalID+"="+String.valueOf(client.getIdSucursal()),null);
				if(client.getFacturasPendientes()!=null)
				{
					for(Factura fact: client.getFacturasPendientes())
					{
						bdd.delete(TABLA_MONTOPROVEEDOR, NMConfig.Cliente.Factura.objFacturaID+"="+String.valueOf(fact.getId()),null);
						bdd.delete(TABLA_PROMOCIONCOBRO, NMConfig.Cliente.Factura.objFacturaID+"="+String.valueOf(fact.getId()),null);
					}
				}
				bdd.delete(TABLA_FACTURA, NMConfig.Cliente.objSucursalID+"="+String.valueOf(client.getIdSucursal()),null);
				bdd.delete(TABLA_CLIENTE, NMConfig.Cliente.IdSucursal+"="+String.valueOf(client.getIdSucursal()),null);			
			}	 
		 
    }
   
	public static void DeleteALLCustomerRecords(SQLiteDatabase bdd)
    {
    	bdd.delete(TABLA_DESCUENTOPROVEEDOR, null,null);
    	bdd.delete(TABLA_CCNOTADEBITO, null,null);
    	bdd.delete(TABLA_CCNOTACREDITO, null,null);
    	bdd.delete(TABLA_MONTOPROVEEDOR, null,null);
    	bdd.delete(TABLA_PROMOCIONCOBRO, null,null); 
		bdd.delete(TABLA_FACTURA, null,null);
		bdd.delete(TABLA_CLIENTE, null,null);  
    }
	public static void DeleteALLProductRecords(SQLiteDatabase bdd)
    {  
		bdd.delete(TABLA_PRODUCTO, null,null);
		bdd.delete(TABLA_LOTE, null,null);  
    }
	@SuppressWarnings("rawtypes")
	@Override
	public int update(Uri uri, ContentValues values,String selection, String[] selectionArgs) 
	{
		
		int cont;
		//Si es una consulta a un ID concreto construimos el WHERE
		 
		SQLiteDatabase db = dbhelper.getWritableDatabase();
		List<Map.Entry> coll=FromWhere(uri);
		String TablaName=coll.get(0).getValue().toString();
		String where =coll.get(1).getValue().toString();
		
		cont = db.update(TablaName, values, where, selectionArgs);
		
		return cont;
	}
	
	@SuppressWarnings({ "rawtypes"})
	@SuppressLint({ "ParserError", "UseSparseArrays" })
	public List<Map.Entry> FromWhere(Uri uri)
	{
		
		HashMap<Integer,String> dictionary=new HashMap<Integer,String>();
		ArrayList<Map.Entry> e=new ArrayList<Map.Entry>(2); 
		 
		switch(uriMatcher.match(uri))
		{
			case CLIENTE:	   			dictionary.put(CLIENTE,TABLA_CLIENTE); 
										dictionary.put(CONTENT_URI_LOCALID,CONTENT_URI_CLIENTE.toString());
							   			break;
							   		
			case CLIENTE_ID:   			dictionary.put(CLIENTE,TABLA_CLIENTE);
										dictionary.put(CLIENTE+1,"IdCliente=" + uri.getLastPathSegment()); 
										break;
									
			case FACTURA:				dictionary.put(FACTURA,TABLA_FACTURA); 
										dictionary.put(CONTENT_URI_LOCALID,CONTENT_URI_FACTURA.toString());
										break;
									
			case FACTURA_ID:        	dictionary.put(FACTURA, TABLA_FACTURA);
										dictionary.put(FACTURA+1,"Id=" + uri.getLastPathSegment());  
										break; 
									
			case PROMOCIONCOBRO: 		dictionary.put(PROMOCIONCOBRO, TABLA_PROMOCIONCOBRO);
										dictionary.put(CONTENT_URI_LOCALID,CONTENT_URI_PROMOCIONCOBRO.toString());
										break;     
									
			case PROMOCIONCOBRO_ID:		dictionary.put(PROMOCIONCOBRO,TABLA_PROMOCIONCOBRO);
										dictionary.put(PROMOCIONCOBRO+1,"objFacturaID=" + uri.getLastPathSegment()); 
										break;
									
			case MONTOPROVEEDOR:		dictionary.put(MONTOPROVEEDOR, TABLA_MONTOPROVEEDOR); 
										dictionary.put(CONTENT_URI_LOCALID,CONTENT_URI_MONTOPROVEEDOR.toString());
										break;
									
			case MONTOPROVEEDOR_ID: 	dictionary.put(MONTOPROVEEDOR, TABLA_MONTOPROVEEDOR); 
										dictionary.put(MONTOPROVEEDOR+1,"objFacturaID=" + uri.getLastPathSegment()); 
										break;
									
			case CCNOTACREDITO:			dictionary.put(CCNOTACREDITO, TABLA_CCNOTACREDITO);
										dictionary.put(CONTENT_URI_LOCALID,CONTENT_URI_CCNOTACREDITO.toString());
										break;
									
			case CCNOTACREDITO_ID:  	dictionary.put(CCNOTACREDITO, TABLA_CCNOTACREDITO);
										dictionary.put(CCNOTACREDITO+1,"objSucursalID=" + uri.getLastPathSegment());  
										break;
									
			case CCNOTADEBITO:			dictionary.put(CCNOTADEBITO,TABLA_CCNOTADEBITO);
										dictionary.put(CONTENT_URI_LOCALID,CONTENT_URI_CCNOTADEBITO.toString());
										break;
										
			case CCNOTADEBITO_ID:   	dictionary.put(CCNOTADEBITO,TABLA_CCNOTADEBITO);
										dictionary.put(CCNOTADEBITO+1,"objSucursalID=" + uri.getLastPathSegment()); 
										break;
									
			case DESCUENTOPROVEEDOR:	dictionary.put(DESCUENTOPROVEEDOR, TABLA_DESCUENTOPROVEEDOR);
										dictionary.put(CONTENT_URI_LOCALID,CONTENT_URI_DESCUENTOPROVEEDOR.toString());
										break;
			case DESCUENTOPROVEEDOR_ID: dictionary.put(DESCUENTOPROVEEDOR, TABLA_DESCUENTOPROVEEDOR);
										dictionary.put(DESCUENTOPROVEEDOR+1,"objSucursalID=" + uri.getLastPathSegment());
										break; 
										
										
			case PRODUCTO:				dictionary.put(PRODUCTO, TABLA_PRODUCTO);
										dictionary.put(CONTENT_URI_LOCALID,CONTENT_URI_PRODUCTO.toString());
										break;
			case PRODUCTO_ID:           dictionary.put(PRODUCTO, TABLA_PRODUCTO);
										dictionary.put(PRODUCTO+1,"Id=" + uri.getLastPathSegment());
										break;
			case LOTE:				    dictionary.put(PRODUCTO, TABLA_LOTE);
										dictionary.put(CONTENT_URI_LOCALID,CONTENT_URI_PRODUCTO.toString());
										break;
			case LOTE_ID: 			    dictionary.put(LOTE, TABLA_LOTE);
										dictionary.put(LOTE+1,"NumeroLote=" + uri.getLastPathSegment());
										break;
			
		} 
		Iterator it = dictionary.entrySet().iterator();
		while (it.hasNext()) 
			e.add((Map.Entry)it.next()); 
		
		return e;
		
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		
		int cont;
		//Si es una consulta a un ID concreto construimos el WHERE 
		List<Map.Entry> coll=FromWhere(uri);
		String TablaName=coll.get(0).getValue().toString();
		String where = selection + coll.get(1).getValue().toString();		
		SQLiteDatabase db = dbhelper.getWritableDatabase();
		
		cont = db.delete(TablaName, where, selectionArgs);
		
		return cont;
	}
	 	
	@Override
	public String getType(Uri uri) 
	{
	 
		switch(uriMatcher.match(uri))
		{		 
			case CLIENTE:case FACTURA:case PROMOCIONCOBRO:case MONTOPROVEEDOR:case CCNOTACREDITO:case CCNOTADEBITO:case DESCUENTOPROVEEDOR:case PRODUCTO:case LOTE:
				 return "vnd.android.cursor.dir/vnd"+AUTHORITY;
			case CLIENTE_ID:case FACTURA_ID:case PROMOCIONCOBRO_ID:case MONTOPROVEEDOR_ID:case CCNOTACREDITO_ID:case CCNOTADEBITO_ID:case DESCUENTOPROVEEDOR_ID:case PRODUCTO_ID:case LOTE_ID:
				 return "vnd.android.cursor.item/vnd"+AUTHORITY; 									
		    default:throw new IllegalArgumentException("Invalid Uri: "+ uri);
		}  
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) { 
		return null;
	}
}

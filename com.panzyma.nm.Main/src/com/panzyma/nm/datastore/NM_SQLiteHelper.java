package com.panzyma.nm.datastore;
 
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
 
 
public class NM_SQLiteHelper extends SQLiteOpenHelper 
{
	private static  String DATABASE_NAME = "NM";
	Context mycontext;
    String sqlCliente=       "CREATE TABLE IF NOT EXISTS Cliente (IdCliente BLOB PRIMARY KEY  NOT NULL  UNIQUE , NombreCliente TEXT, IdSucursal BLOB NOT NULL,"+ 
						 	 "Codigo TEXT, CodTipoPrecio TEXT, DesTipoPrecio TEXT, objPrecioVentaID BLOB,  objCategoriaClienteID BLOB,"+
						     "objTipoClienteID BLOB, AplicaBonificacion INTEGER, PermiteBonifEspecial INTEGER, PermitePrecioEspecial INTEGER, UG TEXT,"+
						     "Ubicacion TEXT, NombreLegalCliente TEXT, AplicaOtrasDeducciones INTEGER, MontoMinimoAbono REAL, PlazoDescuento INTEGER, PermiteDevolucion INTEGER);";
    
	    String sqlFactura=   "CREATE TABLE IF NOT EXISTS Factura (Id BLOB PRIMARY KEY  NOT NULL UNIQUE,NombreSucursal TEXT,"+
	                         "NoFactura TEXT,Tipo TEXT,NoPedido TEXT,CodEstado TEXT,"+
						     "Estado TEXT,Fecha TEXT,FechaVencimiento TEXT,FechaAppDescPP TEXT,"+
						     "Dias INTEGER,TotalFacturado REAL,Abonado REAL,Descontado REAL,"+
						     "Retenido REAL,Otro REAL,Saldo REAL,Exenta INTEGER,SubtotalFactura REAL,"+
						     "DescuentoFactura REAL,ImpuestoFactura REAL,PuedeAplicarDescPP INTEGER,objSucursalID BLOB,"+
	    				     "FOREIGN KEY(objSucursalID) REFERENCES Cliente(IdSucursal));";
    
    String sqlPromocionCobro="CREATE TABLE IF NOT EXISTS PromocionCobro (FacturasAplicacion TEXT, TipoDescuento TEXT, Descuento TEXT," +
    						 "objFacturaID BLOB, FOREIGN KEY(objFacturaID) REFERENCES Factura(Id));";
    
    String sqlMontoProveedor="CREATE TABLE IF NOT EXISTS MontoProveedor (_ID INTEGER PRIMARY KEY AUTOINCREMENT,ObjProveedorID BLOB, " +
    						 "CodProveedor TEXT, Monto REAL, objFacturaID BLOB, FOREIGN KEY(objFacturaID) REFERENCES Factura(Id));";
    
    String sqlCCNotaCredito= "CREATE TABLE IF NOT EXISTS CCNotaCredito (Id BLOB PRIMARY KEY  NOT NULL , NombreSucursal TEXT,"+
    						 "Estado TEXT, Numero TEXT, Fecha TEXT, FechaVence TEXT,"+
    						 "Concepto TEXT, Monto REAL, NumRColAplic TEXT, CodEstado TEXT,"+ 
    						 "Descripcion TEXT, Referencia INTEGER, CodConcepto TEXT, objSucursalID BLOB,"+
    					     "FOREIGN KEY(objSucursalID) REFERENCES Cliente(IdSucursal));";
    
    String sqlCCNotaDebito=  "CREATE TABLE IF NOT EXISTS CCNotaDebito (Id BLOB PRIMARY KEY  NOT NULL, NombreSucursal TEXT,"+
    					     "Estado TEXT, Numero TEXT, Fecha TEXT, FechaVence TEXT, Dias INTEGER,"+ 
    					     "Concepto TEXT, Monto REAL, MontoAbonado REAL, Saldo REAL, CodEstado TEXT,"+ 
    					     "Descripcion TEXT, objSucursalID BLOB,"+
    					     "FOREIGN KEY(objSucursalID) REFERENCES Cliente(IdSucursal));";
    
    String sqlDescuentoProveedor="CREATE TABLE IF NOT EXISTS DescuentoProveedor (_ID INTEGER PRIMARY KEY AUTOINCREMENT,ObjProveedorID BLOB, PrcDescuento REAL," +
    							 "objSucursalID BLOB,FOREIGN KEY(objSucursalID) REFERENCES Cliente(IdSucursal));";
    
    String sqlLote="CREATE TABLE IF NOT EXISTS Lote (Id BLOB,NumeroLote TEXT, FechaVencimiento INTEGER,ObjProductoID BLOB,FOREIGN KEY(ObjProductoID) REFERENCES Producto(Id));";
    
    String sqlProducto="CREATE TABLE IF NOT EXISTS Producto (Id BLOB PRIMARY KEY  NOT NULL,Codigo TEXT, Nombre TEXT," +
			 												"EsGravable INTEGER,ListaPrecios TEXT,ListaBonificaciones TEXT,CatPrecios TEXT,Disponible INTEGER,"+
			 												"PermiteDevolucion INTEGER,LoteRequerido INTEGER,ObjProveedorID BLOB,DiasAntesVen INTEGER,DiasDespuesVen INTEGER );";

    
    
    String sqlDrop_Cliente=			  "DROP TABLE IF EXISTS Cliente";
    String sqlDrop_Factura=			  "DROP TABLE IF EXISTS Factura";
    String sqlDrop_PromocionCobro=	  "DROP TABLE IF EXISTS PromocionCobro";
    String sqlDrop_MontoProveedor=	  "DROP TABLE IF EXISTS MontoProveedor";
    String sqlDrop_CCNotaCredito=	  "DROP TABLE IF EXISTS CCNotaCredito";
    String sqlDrop_CCNotaDebito=	  "DROP TABLE IF EXISTS CCNotaDebito";
    String sqlDrop_DescuentoProveedor="DROP TABLE IF EXISTS DescuentoProveedor";  
    String sqlDrop_Producto=		  "DROP TABLE IF EXISTS Producto";
	String sqlDrop_Lote=			  "DROP TABLE IF EXISTS Lote";
	
    String sqlDeleteDesProv=  "DELETE FROM DescuentoProveedor";
    String sqlDeleteND=       "DELETE FROM CCNotaDebito";
    String sqlDeleteNC=       "DELETE FROM CCNotaCredito";
    String sqlDeleteMontProv= "DELETE FROM MontoProveedor";
    String sqlDeletePromCobro="DELETE FROM PromocionCobro";
    String sqlDeleteFactura=  "DELETE FROM Factura";
    String sqlDeleteCliente=  "DELETE FROM Cliente"; 
    String sqlDeleteProducto= "DELETE FROM Producto";
    String sqlDeleteLote=     "DELETE FROM Lote";
	     
    public NM_SQLiteHelper(Context contexto, String nombre, CursorFactory factory, int version) 
    { 
        super(contexto, nombre, factory, version);
        this.mycontext=contexto;
        DATABASE_NAME=nombre;
    } 
    @Override
    public void onCreate(SQLiteDatabase db) 
    { 
    	CREATE_TABLES(db);
    }
    
    @Override
	public synchronized void close() {
		 
		super.close();
	}
    
	@Override
	public void onOpen(SQLiteDatabase db) {
		 
		super.onOpen(db);
	}
	
    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva)
    {
    	DROP_TABLES(db);
        CREATE_TABLES(db); 
    }
    
    public void CREATE_TABLES(SQLiteDatabase db)
    { 
    	try 
    	{ 
    		if(!db.isOpen())
    			db=this.mycontext.openOrCreateDatabase("data/data/"+mycontext.getPackageName()+"/databases/"+DATABASE_NAME, SQLiteDatabase.CREATE_IF_NECESSARY, null);
    		      		    
		    db.execSQL(sqlCliente);
			db.execSQL(sqlFactura);
			db.execSQL(sqlPromocionCobro);
			db.execSQL(sqlMontoProveedor);
			db.execSQL(sqlCCNotaCredito);
			db.execSQL(sqlCCNotaDebito);
			db.execSQL(sqlDescuentoProveedor); 
			db.execSQL(sqlProducto);  
			db.execSQL(sqlLote);
			
				
				
        } 
    	catch (SQLException e) 
        {
            e.printStackTrace();
        }
    	catch(Exception e)
    	{
    	    e.printStackTrace();  
    	} 

    }
    
    public void DROP_TABLES(SQLiteDatabase db)
    {
    	try 
    	{  
    		db.execSQL(sqlDrop_Cliente);
            db.execSQL(sqlDrop_Factura);
            db.execSQL(sqlDrop_PromocionCobro);
            db.execSQL(sqlDrop_MontoProveedor);
            db.execSQL(sqlDrop_CCNotaCredito);
            db.execSQL(sqlDrop_CCNotaDebito);
            db.execSQL(sqlDrop_DescuentoProveedor);  
            db.execSQL(sqlDrop_Lote); 
            db.execSQL(sqlDrop_Producto);
        } 
    	catch (SQLException e) 
        {
            e.printStackTrace();
        }
    	catch(Exception e)
    	{
    	    e.printStackTrace();  
    	}   	  
    	
    }
}
 

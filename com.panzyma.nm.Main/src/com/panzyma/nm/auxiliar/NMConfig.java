package com.panzyma.nm.auxiliar;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import com.panzyma.nm.serviceproxy.CCNotaCredito;
import com.panzyma.nm.serviceproxy.CCNotaDebito;
import com.panzyma.nm.serviceproxy.DescuentoProveedor;
import com.panzyma.nm.serviceproxy.Factura;
import com.panzyma.nm.serviceproxy.Lote;
import com.panzyma.nm.serviceproxy.MontoProveedor;
import com.panzyma.nm.serviceproxy.PromocionCobro;

@SuppressWarnings("unused")
public class NMConfig
{   
	public static final String NAME_SPACE = "http://www.panzyma.com/";
    public static final String URL = "http://www.panzyma.com/nordisserverprod/mobileservice.asmx";
    
    public static class MethodName
    { 	
    	public static final String CheckConnection="CheckConnection";
    	public static final String LoginUser="LoginUser";
    	public static final String UserHasRol="UserHasRol";
    	public static final String GetDevicePrefix="GetDevicePrefix";
    	public static final String GetDatosUsuario="GetDatosUsuario";
    	public static final String GetCliente="GetCliente"; 
	    public static final String GetClientesPaged = "GetClientesPaged";
	    public static final String GetCCCliente="GetCCCliente";
	    public static final String TraerFacturasCliente="TraerFacturasCliente";
	    public static final String GetProductosPaged="GetProductosPaged"; 
	}
	public static final class Cliente
	{  		
		public static final String Credentials="Credentials";
		public static final String UsuarioVendedor="Credentials";
		public static final String Page="UsuarioVendedor";
		public static final String RowsPerPage="RowsPerPage";     		
		
		public static final java.lang.String IdCliente="IdCliente";
		public static final java.lang.String NombreCliente="NombreCliente";
		public static final java.lang.String IdSucursal="IdSucursal";
		public static final java.lang.String Codigo="Codigo";
		public static final java.lang.String CodTipoPrecio="CodTipoPrecio";
		public static final java.lang.String DesTipoPrecio="DesTipoPrecio";
		public static final java.lang.String objPrecioVentaID="objPrecioVentaID";
		public static final java.lang.String objCategoriaClienteID="objCategoriaClienteID";
		public static final java.lang.String objTipoClienteID="objTipoClienteID";
		public static final java.lang.String AplicaBonificacion="AplicaBonificacion";
		public static final java.lang.String PermiteBonifEspecial="PermiteBonifEspecial";
		public static final java.lang.String PermitePrecioEspecial="PermitePrecioEspecial";
		public static final java.lang.String UG="UG";
		public static final java.lang.String Ubicacion="Ubicacion";
		public static final java.lang.String NombreLegalCliente="NombreLegalCliente"; 
		public static final java.lang.String AplicaOtrasDeducciones="AplicaOtrasDeducciones";
		public static final java.lang.String MontoMinimoAbono="MontoMinimoAbono";
		public static final java.lang.String PlazoDescuento="PlazoDescuento";
		public static final java.lang.String PermiteDevolucion="PermiteDevolucion";
		public static final java.lang.String objSucursalID="objSucursalID";
		public static final class Factura
		{
			public static final java.lang.String Id="Id";
			public static final java.lang.String NombreSucursal="NombreSucursal";
			public static final java.lang.String NoFactura="NoFactura";	    
		    public static final java.lang.String Tipo="Tipo";
		    public static final java.lang.String NoPedido="NoPedido";
		    public static final java.lang.String CodEstado="CodEstado";
		    public static final java.lang.String Estado="Estado";
		    public static final java.lang.String Fecha="Fecha";
		    public static final java.lang.String FechaVencimiento="FechaVencimiento";
		    public static final java.lang.String FechaAppDescPP="FechaAppDescPP";
		    public static final java.lang.String Dias="Dias";
		    public static final java.lang.String TotalFacturado="TotalFacturado";
		    public static final java.lang.String Abonado="Abonado";
		    public static final java.lang.String Descontado="Descontado";
		    public static final java.lang.String Retenido="Retenido";
		    public static final java.lang.String Otro="Otro";
		    public static final java.lang.String Saldo="Saldo";
		    public static final java.lang.String Exenta="Exenta";	   
		    public static final java.lang.String SubtotalFactura="SubtotalFactura";
		    public static final java.lang.String DescuentoFactura="DescuentoFactura";
		    public static final java.lang.String ImpuestoFactura="ImpuestoFactura";
		    public static final java.lang.String PuedeAplicarDescPP="PuedeAplicarDescPP"; 
		    public static final java.lang.String objFacturaID="objFacturaID";
		    
		    public static final class PromocionCobro
		    {
				public static final java.lang.String FacturasAplicacion="FacturasAplicacion";
				public static final java.lang.String TipoDescuento="TipoDescuento";
				public static final java.lang.String Descuento="Descuento";
		    }
		    public static final class MontoProveedor
		    { 
				public static final java.lang.String ObjProveedorID="ObjProveedorID";
		        public static final java.lang.String CodProveedor="CodProveedor";
		        public static final java.lang.String Monto="Monto"; 
		    }
		    
		}
		public static final class CCNotaCredito
		{
			public static final java.lang.String Id="Id";
			public static final java.lang.String NombreSucursal="NombreSucursal";
			public static final java.lang.String Estado="Estado";
			public static final java.lang.String Numero="Numero";
			public static final java.lang.String Fecha="Fecha";
			public static final java.lang.String FechaVence="FechaVence";
			public static final java.lang.String Concepto="Concepto";
			public static final java.lang.String Monto="Monto";
			public static final java.lang.String NumRColAplic="NumRColAplic";
			public static final java.lang.String CodEstado="CodEstado";
			public static final java.lang.String Descripcion="Descripcion";
			public static final java.lang.String Referencia="Referencia";
			public static final java.lang.String CodConcepto="CodConcepto"; 
		}
		public static final class CCNotaDebito
		{
			public static final java.lang.String Id="Id";
			public static final java.lang.String NombreSucursal="NombreSucursal";
			public static final java.lang.String Estado="Estado";
			public static final java.lang.String Numero="Numero";
			public static final java.lang.String Fecha="Fecha";
			public static final java.lang.String FechaVence="FechaVence";
			public static final java.lang.String Dias="Dias";
			public static final java.lang.String Concepto="Concepto";
			public static final java.lang.String Monto="Monto";
			public static final java.lang.String MontoAbonado="MontoAbonado";
			public static final java.lang.String Saldo="Saldo";
			public static final java.lang.String CodEstado="CodEstado";
		    public static final java.lang.String Descripcion="Descripcion"; 
		} 
		public static final class DescuentoProveedor
		{
			public static final java.lang.String ObjProveedorID="ObjProveedorID";
			public static final java.lang.String PrcDescuento="PrcDescuento";  
		} 
		
	}
	public static final class Pedido
	{
		
	}
	public static final class Producto
	{
		 public static final java.lang.String Id="Id";
		 public static final java.lang.String Codigo="Codigo";
		 public static final java.lang.String Nombre="Nombre";
		 public static final java.lang.String EsGravable="EsGravable";
		 public static final java.lang.String ListaPrecios="ListaPrecios";
		 public static final java.lang.String ListaBonificaciones="ListaBonificaciones";
		 public static final java.lang.String CatPrecios="CatPrecios";
		 public static final java.lang.String Disponible="Disponible"; 
		 public static final java.lang.String PermiteDevolucion="PermiteDevolucion";
		 public static final java.lang.String LoteRequerido="LoteRequerido";
		 public static final java.lang.String ObjProveedorID="ObjProveedorID";
		 public static final java.lang.String DiasAntesVen="DiasAntesVen";
		 public static final java.lang.String DiasDespuesVen="DiasDespuesVen";
		 public static final java.lang.String ObjProductoID="ObjProductoID";
		 public static final class Lote
		 {
			public static final java.lang.String Id="Id";
			public static final java.lang.String NumeroLote="NumeroLote";
			public static final java.lang.String FechaVencimiento="FechaVencimiento";
		 }
	}
	public static final class Usuario
	{
		 public static final java.lang.String Id="Id";
		 public static final java.lang.String Login="Login";
		 public static final java.lang.String Nombre="Nombre";
		 public static final java.lang.String Sexo="Sexo";
		 public static final java.lang.String AccedeModuloPedidos="AccedeModuloPedidos"; 
		 public static final java.lang.String PuedeEditarPrecioAbajo="PuedeEditarPrecioAbajo";
		 public static final java.lang.String PuedeEditarPrecioArriba="PuedeEditarPrecioArriba";
		 public static final java.lang.String PuedeEditarBonifAbajo="PuedeEditarBonifAbajo"; 
		 public static final java.lang.String PuedeEditarBonifArriba="PuedeEditarBonifArriba";
		 public static final java.lang.String IsAdmin="IsAdmin";
		 public static final java.lang.String PuedeCrearPedido="PuedeCrearPedido";
		 public static final java.lang.String PuedeConsultarPedido="PuedeConsultarPedido";
		 public static final java.lang.String Codigo="Codigo";
		 public static final java.lang.String PuedeEditarDescPP="PuedeEditarDescPP";
	}
}

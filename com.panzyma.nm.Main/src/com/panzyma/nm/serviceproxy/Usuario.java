// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JSR-172 Reference Implementation wscompile 1.0, using: JAX-RPC Standard Implementation (1.1, build R59)

package com.panzyma.nm.serviceproxy;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import com.panzyma.nm.auxiliar.NMConfig;
 

public class Usuario implements KvmSerializable{
    protected long Id;
    protected java.lang.String Login;
    protected java.lang.String Nombre;
    protected java.lang.String Sexo;
    protected boolean AccedeModuloPedidos;
    protected boolean PuedeEditarPrecioAbajo;
    protected boolean PuedeEditarPrecioArriba;
    protected boolean PuedeEditarBonifAbajo;
    protected boolean PuedeEditarBonifArriba;
    protected boolean IsAdmin;
    protected boolean PuedeCrearPedido;
    protected boolean PuedeConsultarPedido;
    protected java.lang.String Codigo;
    protected boolean PuedeEditarDescPP;
    
    public Usuario() {
    }
    
    public Usuario(long id, java.lang.String login, java.lang.String nombre, java.lang.String sexo, boolean accedeModuloPedidos, boolean puedeEditarPrecioAbajo, boolean puedeEditarPrecioArriba, boolean puedeEditarBonifAbajo, boolean puedeEditarBonifArriba, boolean isAdmin, boolean puedeCrearPedido, boolean puedeConsultarPedido, java.lang.String codigo, boolean puedeEditarDescPP) {
        this.Id = id;
        this.Login = login;
        this.Nombre = nombre;
        this.Sexo = sexo;
        this.AccedeModuloPedidos = accedeModuloPedidos;
        this.PuedeEditarPrecioAbajo = puedeEditarPrecioAbajo;
        this.PuedeEditarPrecioArriba = puedeEditarPrecioArriba;
        this.PuedeEditarBonifAbajo = puedeEditarBonifAbajo;
        this.PuedeEditarBonifArriba = puedeEditarBonifArriba;
        this.IsAdmin = isAdmin;
        this.PuedeCrearPedido = puedeCrearPedido;
        this.PuedeConsultarPedido = puedeConsultarPedido;
        this.Codigo = codigo;
        this.PuedeEditarDescPP = puedeEditarDescPP;
    }
    
    public long getId() {
        return Id;
    }
    
    public void setId(long id) {
        this.Id = id;
    }
    
    public java.lang.String getLogin() {
        return Login;
    }
    
    public void setLogin(java.lang.String login) {
        this.Login = login;
    }
    
    public java.lang.String getNombre() {
        return Nombre;
    }
    
    public void setNombre(java.lang.String nombre) {
        this.Nombre = nombre;
    }
    
    public java.lang.String getSexo() {
        return Sexo;
    }
    
    public void setSexo(java.lang.String sexo) {
        this.Sexo = sexo;
    }
    
    public boolean isAccedeModuloPedidos() {
        return AccedeModuloPedidos;
    }
    
    public void setAccedeModuloPedidos(boolean accedeModuloPedidos) {
        this.AccedeModuloPedidos = accedeModuloPedidos;
    }
    
    public boolean isPuedeEditarPrecioAbajo() {
        return PuedeEditarPrecioAbajo;
    }
    
    public void setPuedeEditarPrecioAbajo(boolean puedeEditarPrecioAbajo) {
        this.PuedeEditarPrecioAbajo = puedeEditarPrecioAbajo;
    }
    
    public boolean isPuedeEditarPrecioArriba() {
        return PuedeEditarPrecioArriba;
    }
    
    public void setPuedeEditarPrecioArriba(boolean puedeEditarPrecioArriba) {
        this.PuedeEditarPrecioArriba = puedeEditarPrecioArriba;
    }
    
    public boolean isPuedeEditarBonifAbajo() {
        return PuedeEditarBonifAbajo;
    }
    
    public void setPuedeEditarBonifAbajo(boolean puedeEditarBonifAbajo) {
        this.PuedeEditarBonifAbajo = puedeEditarBonifAbajo;
    }
    
    public boolean isPuedeEditarBonifArriba() {
        return PuedeEditarBonifArriba;
    }
    
    public void setPuedeEditarBonifArriba(boolean puedeEditarBonifArriba) {
        this.PuedeEditarBonifArriba = puedeEditarBonifArriba;
    }
    
    public boolean isIsAdmin() {
        return IsAdmin;
    }
    
    public void setIsAdmin(boolean isAdmin) {
        this.IsAdmin = isAdmin;
    }
    
    public boolean isPuedeCrearPedido() {
        return PuedeCrearPedido;
    }
    
    public void setPuedeCrearPedido(boolean puedeCrearPedido) {
        this.PuedeCrearPedido = puedeCrearPedido;
    }
    
    public boolean isPuedeConsultarPedido() {
        return PuedeConsultarPedido;
    }
    
    public void setPuedeConsultarPedido(boolean puedeConsultarPedido) {
        this.PuedeConsultarPedido = puedeConsultarPedido;
    }
    
    public java.lang.String getCodigo() {
        return Codigo;
    }
    
    public void setCodigo(java.lang.String codigo) {
        this.Codigo = codigo;
    }
    
    public boolean isPuedeEditarDescPP() {
        return PuedeEditarDescPP;
    }
    
    public void setPuedeEditarDescPP(boolean puedeEditarDescPP) {
        this.PuedeEditarDescPP = puedeEditarDescPP;
    }
    
    @Override
	public int getPropertyCount() { 
		return 14;
	}
    
	@Override
	public Object getProperty(int _index) { 
		 switch(_index)  
		 {
	        case 0: return Long.valueOf(Id);
	        case 1: return Login; 
	        case 2: return Nombre;
	        case 3: return Sexo;  
	        case 4: return  Boolean.valueOf(AccedeModuloPedidos);
	        case 5: return Boolean.valueOf(PuedeEditarPrecioAbajo);
	        case 6: return Boolean.valueOf(PuedeEditarPrecioArriba);
	        case 7: return  Boolean.valueOf(PuedeEditarBonifAbajo);
	        case 8: return Boolean.valueOf(PuedeEditarBonifArriba);
	        case 9: return Boolean.valueOf(IsAdmin);
	        case 10: return Boolean.valueOf(PuedeCrearPedido); 
	        case 11: return Boolean.valueOf(PuedeConsultarPedido); 
	        case 12: return Codigo;
	        case 13: return Boolean.valueOf(PuedeEditarDescPP);
	     } 
		 
	    return null;
	}
	
	@Override
	public void setProperty(int _index, Object _obj) { 
		
		switch(_index)  
		{
	        case 0: Id = Long.parseLong(_obj.toString()); break;
	        case 1: Login = (java.lang.String) _obj; break;
	        case 2: Nombre = (java.lang.String)_obj; break;
	        case 3: Sexo = (java.lang.String) _obj; break;
	        case 4: AccedeModuloPedidos = "true".equals(_obj.toString()); break;
	        case 5: PuedeEditarPrecioAbajo = "true".equals(_obj.toString()); break;
	        case 6: PuedeEditarPrecioArriba = "true".equals(_obj.toString()); break;
	        case 7: PuedeEditarBonifAbajo = "true".equals(_obj.toString()); break;
	        case 8: PuedeEditarBonifArriba = "true".equals(_obj.toString()); break;
	        case 9: IsAdmin = "true".equals(_obj.toString()); break;
	        case 10: PuedeCrearPedido = "true".equals(_obj.toString()); break;
	        case 11: PuedeConsultarPedido = "true".equals(_obj.toString()); break;
	        case 12: Codigo = (java.lang.String) _obj; break;
	        case 13: PuedeEditarDescPP ="true".equals(_obj.toString()); break; 
        }  
		
	}
	
	@Override
	public void getPropertyInfo(int _index, Hashtable _table, PropertyInfo _info) 
	{ 
		
		switch(_index)  
		{
	        case 0:
	            _info.name = NMConfig.Usuario.Id;
	            _info.type = Long.class; break;
	        case 1:
	            _info.name = NMConfig.Usuario.Login;
	            _info.type = java.lang.String.class; break;
	        case 2:
	        	_info.name = NMConfig.Usuario.Nombre;
	            _info.type = java.lang.String.class; break;
	        case 3:
	        	_info.name = NMConfig.Usuario.Sexo;
	            _info.type = java.lang.String.class; break; 
	        case 4:
	        	_info.name = NMConfig.Usuario.AccedeModuloPedidos;
	            _info.type = java.lang.Boolean.class; break;
	        case 5:
	        	_info.name = NMConfig.Usuario.PuedeEditarBonifAbajo;
	            _info.type = java.lang.Boolean.class; break;
	        case 6:
	        	_info.name = NMConfig.Usuario.PuedeEditarPrecioArriba;
	            _info.type = java.lang.Boolean.class; break;
	        case 7:
	        	_info.name = NMConfig.Usuario.PuedeEditarBonifAbajo;
	            _info.type = java.lang.Boolean.class; break;
	        case 8:
	        	_info.name = NMConfig.Usuario.PuedeEditarBonifArriba;
	            _info.type = java.lang.Boolean.class; break;
	        case 9:
	        	_info.name = NMConfig.Usuario.IsAdmin;
	            _info.type = java.lang.Boolean.class; break;
	        case 10:
	        	_info.name = NMConfig.Usuario.PuedeCrearPedido;
	            _info.type = java.lang.Boolean.class; break;
	        case 11:
	        	_info.name = NMConfig.Usuario.PuedeConsultarPedido;
	            _info.type = java.lang.Boolean.class; break;
	        case 12:
	            _info.name = NMConfig.Usuario.Codigo;
	            _info.type = java.lang.String.class; break;
	        case 13:
	        	_info.name = NMConfig.Usuario.PuedeEditarDescPP;
	            _info.type = java.lang.Boolean.class; break; 
		
		}

	
	}
	
}

package com.panzyma.nm.serviceproxy;

import android.annotation.SuppressLint;

import java.io.Serializable;
import java.util.Hashtable;

import org.ksoap2.serialization.PropertyInfo; 

public final class PromocionCobro implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -5278627536189254153L;

	private java.lang.String FacturasAplicacion;

    private java.lang.String TipoDescuento;

    private float Descuento;

    public PromocionCobro() {
        
    }
    public void setFacturasAplicacion(java.lang.String facturasAplicacion) {
        this.FacturasAplicacion = facturasAplicacion;
    }

    public java.lang.String getFacturasAplicacion() {
        return this.FacturasAplicacion;
    }

    public void setTipoDescuento(java.lang.String tipoDescuento) {
        this.TipoDescuento = tipoDescuento;
    }

    public java.lang.String getTipoDescuento() {
        return this.TipoDescuento;
    }

    public void setDescuento(float descuento) {
        this.Descuento = descuento;
    }

    public float getDescuento() {
        return this.Descuento;
    }
 
	public int getPropertyCount() {
        return 3;
    }
 
	@SuppressLint("UseValueOf")
	public Object getProperty(int _index) {
        switch(_index)  {
        case 0: return FacturasAplicacion;
        case 1: return TipoDescuento;
        case 2: return new Float(Descuento);
        }
        return null;
    }


	public void setProperty(int _index, Object _obj) {
        switch(_index)  {
        case 0: FacturasAplicacion = (java.lang.String) _obj; break;
        case 1: TipoDescuento = (java.lang.String) _obj; break;
        case 2: Descuento = Float.parseFloat(_obj.toString()); break;
        }
    }

	@SuppressWarnings("rawtypes")
	public void getPropertyInfo(int _index, Hashtable _table, PropertyInfo _info) {
        switch(_index)  {
        case 0:
            _info.name = "FacturasAplicacion";
            _info.type = java.lang.String.class; break;
        case 1:
            _info.name = "TipoDescuento";
            _info.type = java.lang.String.class; break;
        case 2:
            _info.name = "Descuento";
            _info.type = Float.class; break;
        }
    }

}

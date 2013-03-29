package com.panzyma.nm.model;

import java.lang.reflect.Type;

import org.ksoap2.serialization.PropertyInfo;

import android.content.Context;
import android.content.SharedPreferences; 

import com.panzyma.nm.auxiliar.NMComunicacion;
import com.panzyma.nm.auxiliar.NMConfig;
import com.panzyma.nm.auxiliar.NMTranslate;
import com.panzyma.nm.auxiliar.Parameters;
import com.panzyma.nm.serviceproxy.Cliente;
import com.panzyma.nm.serviceproxy.Usuario;
import com.panzyma.nm.viewmodel.vmConfiguracion;

public class ModelConfiguracion {

	static SharedPreferences pref;
	static SharedPreferences.Editor edit;

	public ModelConfiguracion(){}
	
	public  static Usuario getDatosUsuario(String Credentials,String LoginUsuario) throws Exception
	{
		Parameters params=new Parameters((new String[]{"Credentials","LoginUsuario"}),
				 (new Object[]{Credentials,LoginUsuario}),
				 (new Type[]{PropertyInfo.STRING_CLASS,PropertyInfo.STRING_CLASS}));
		return  (NMTranslate.ToObject(( NMComunicacion.InvokeMethod(params.getParameters(),NMConfig.URL,NMConfig.NAME_SPACE,NMConfig.MethodName.GetDatosUsuario)),new Usuario()));		
    }
	
	public  static int getGET_DEVISE_PREFIX(String Credentials,String PIN) throws Exception
	{
		Parameters params=new Parameters((new String[]{"Credentials","PIN"}),
				 (new Object[]{Credentials,PIN}),
				 (new Type[]{PropertyInfo.STRING_CLASS,PropertyInfo.STRING_CLASS}));
		return  (Integer) ( NMComunicacion.InvokeMethod(params.getParameters(),NMConfig.URL,NMConfig.NAME_SPACE,NMConfig.MethodName.GetDevicePrefix));		
    }
	
	public static vmConfiguracion getVMConfiguration(Context view)
	{ 
		pref=view.getSharedPreferences("ViewConfiguracion",Context.MODE_PRIVATE);  
		
		return vmConfiguracion.setConfiguration(
										 pref.getString("url_server","http://www.panzyma.com/nordisserverprod"), 
										 pref.getString("device_id",""),
										 pref.getString("enterprise","dp"), 
										 pref.getString("name_user",""),
										 pref.getString("password",""));
	}
}

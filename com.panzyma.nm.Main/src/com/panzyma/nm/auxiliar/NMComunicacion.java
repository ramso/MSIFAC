package com.panzyma.nm.auxiliar;
import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.panzyma.nm.serviceproxy.CCCliente;

public class NMComunicacion {
 
 	public NMComunicacion(){} 
 	
	@SuppressWarnings("unused")
	public static synchronized boolean DeviceHasConnection(Context context)
	{		
		
		boolean hasConnectedWifi = false;
		boolean hasConnectedMobile = false;
		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo) 
		{
			if (ni.getTypeName().equalsIgnoreCase("wifi"))
				if (ni.isConnected())
					hasConnectedWifi = true;
			if (ni.getTypeName().equalsIgnoreCase("mobile"))
				if (ni.isConnected())
					hasConnectedMobile = true;
		}
	    
		return true;
		
	}
	
	public static synchronized Object InvokeMethod(ArrayList<Parameters> params,String URL,String NAME_SPACE,String METHOD_NAME)throws Exception
    { 
        SoapObject request =new SoapObject(NAME_SPACE,METHOD_NAME); 
        for(PropertyInfo pinfo:params) 
        	request.addProperty(pinfo);   
        SoapSerializationEnvelope envelope = GetEnvelope(request);
        return  MakeCall(URL,envelope,NAME_SPACE,METHOD_NAME);
    }
 
	
    public static synchronized SoapSerializationEnvelope GetEnvelope(SoapObject Soap)throws Exception
    {
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(Soap);
        envelope.addMapping(Soap.getNamespace(),CCCliente.class.getSimpleName(),CCCliente.class);
        return envelope;
    } 
    
	public static synchronized Object MakeCall(String URL, SoapSerializationEnvelope Envelope, String NAMESPACE, String METHOD_NAME)throws Exception
    {   
	        HttpTransportSE ht = new HttpTransportSE(URL);
	        ht.call(NAMESPACE+METHOD_NAME, Envelope);
        return  Envelope.getResponse(); 
    }
    
	
}

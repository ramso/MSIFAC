package com.panzyma.nm.viewmodel;

public class vmConfiguracion {
 
    private  java.lang.String URL_SERVER;
    private  java.lang.String DEVICE_ID; 
    private  java.lang.String ENTERPRISE;
    private  java.lang.String NAME_USER;
    private  java.lang.String PASSWORD;
     
	public static vmConfiguracion setConfiguration(java.lang.String url_server,
						   java.lang.String device,
						   java.lang.String enterprise,
						   java.lang.String name_user,
						   java.lang.String passwd)
	{
		vmConfiguracion vmonfig=new vmConfiguracion();
		vmonfig.URL_SERVER=url_server;
		vmonfig.DEVICE_ID=device;
		vmonfig.ENTERPRISE=enterprise;
		vmonfig.NAME_USER=name_user;
		vmonfig.PASSWORD=passwd;
		return vmonfig;
	}
	
	public  String getAppServerURL(){
		return URL_SERVER;
	}
	public  String getDeviceId(){
		return DEVICE_ID;
	}
	public  String getEnterprise(){
		return ENTERPRISE;
	}
	public  String getNameUser(){
		return NAME_USER;
	}
	public  String getPassword(){
		return PASSWORD;
	}
}

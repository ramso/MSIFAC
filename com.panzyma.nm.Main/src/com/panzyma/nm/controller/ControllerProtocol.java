package com.panzyma.nm.controller;

public interface ControllerProtocol {
	
	int ID_SALVAR_CONFIGURACION  = 1;
    int ID_SINCRONIZE_PARAMETROS  = 2;
    int ID_SINCRONIZE_CATALOGOSBASICOS= 3; 
    int ID_SINCRONIZE_CLIENTES= 4;
    int ID_SINCRONIZE_PRODUCTOS = 5;	
    int ID_SINCRONIZE_PROMOCIONES = 6;
    int ID_SINCRONIZE_TODOS = 7;
    int ID_CERRAR = 8; 
	
	int V_REQUEST_QUIT = 101; // empty
	int V_REQUEST_UPDATE = 102; // empty
	int V_REQUEST_DATA = 103; // empty

	int C_QUIT = 201; // empty
	int C_UPDATE_STARTED = 202; // empty
	int C_UPDATE_FINISHED = 203; // empty
	int C_UPDATE_ITEM_FINISHED=205;
	int C_DATA = 204; // obj = (ModelData) data
	int C_FICHACLIENTE=206;
	int C_FACTURACLIENTE=207;
	int C_SETTING_DATA=208;
	int C_UPDATE_IN_PROGRESS=209;
	
	int LOAD_DATA=300;  
    int LOAD_DATA_FROM_LOCALHOST = 301;
    int LOAD_DATA_FROM_SERVER = 302;
    int UPDATE_ITEM_FROM_SERVER =303;
    int LOAD_FICHACLIENTE_FROM_SERVER=304; 
    int LOAD_FACTURASCLIENTE_FROM_SERVER=305;    
    
    int SAVE_DATA_FROM_LOCALHOST=400;
    int UPDATE_DATA_FROM_LOCALHOST=401;
    int DELETE_DATA_FROM_LOCALHOST=402;    
    
    int ERROR=600;
    int NOTIFICATION=601;
    int ERROR_INSERT=602;
    int ERROR_UPDATE=603;    
    int ERROR_DELETE=604;
    
    int ALERT_DIALOG=1;
    int CONFIRMATION_DIALOG=2;
    
}

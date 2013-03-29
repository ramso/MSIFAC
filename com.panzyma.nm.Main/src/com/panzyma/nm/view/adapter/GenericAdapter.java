package com.panzyma.nm.view.adapter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import com.panzyma.nm.serviceproxy.CNota;
import com.panzyma.nm.view.viewholder.FacturaViewHolder; 
import com.panzyma.nm.view.viewholder.ProductoViewHolder;
import com.panzyma.nordismobile.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable; 
import android.widget.HorizontalScrollView;

@SuppressWarnings("unused")
public class GenericAdapter<E, V> extends BaseAdapter implements Filterable {
	   
	private int layoutid;
	private Class<V> viewclass;
	private LayoutInflater inflater; 
	private List<E> items;
	private List<E> mOriginalValues;
	private Context context;
	private int selectedPos=0; 
	
	public GenericAdapter(Context c,Class<V> viewclass,List<E> items,int... layoutid) { 		 
		this.context=c;
		this.inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		this.items = items; 
		this.viewclass=viewclass;
		this.layoutid=layoutid[0];
		notifyDataSetChanged();
	}	
	public void AddAllToListViewDataSource(List<E> obj)
	{
		items.addAll(obj); 
		this.notifyDataSetChanged();
	}
	public List<E> getData()
	{
		return this.items;
	}
	@Override
	public int getCount() {		
		return items.size();
	}
	public void clearItems()
	{ 
		items.clear();
	}
	public void setSelectedPosition(int pos)
	{
		selectedPos = pos;  
	}	
    public int getSelectedPosition(){
		return selectedPos;
	}    
	@Override
	public Object getItem(int position) {
		return items.get(position);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	protected void invokeView(View v,V viewHolder){
		try {
			Field fs[] = viewHolder.getClass().getFields();
			for (Field f : fs) {
				InvokeView a = f.getAnnotation(InvokeView.class);
				if(a!=null)
				{
					int id = a.viewId();
					f.set(viewHolder, v.findViewById(id));
				} 

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}	
	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertview, ViewGroup parent) 
	{   
		V viewHolder = null;
		try
		{
			if (null== convertview)
			{
				convertview=this.inflater.inflate(layoutid,null);
				viewHolder=viewclass.newInstance(); 
				invokeView(convertview,viewHolder);
				convertview.setTag(viewHolder);
				//if(viewHolder.getClass().getSimpleName()==ProductoViewHolder.class.getSimpleName())
				//viewHolder.getClass().getMethod("setListItemEventHandler",Object.class).invoke(viewHolder,items.get(position));
				
			}
			else
				viewHolder=(V)convertview.getTag();
			
			if(getSelectedPosition()==position)
				convertview.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.action_item_selected));			 
			else
				convertview.setBackgroundResource(android.R.color.transparent); 
		    
		    viewHolder.getClass().getMethod("mappingData",Object.class).invoke(viewHolder,items.get(position));
		
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
		return convertview;
	} 
	@Override
	public Filter getFilter() {

		Filter filter = new Filter() 
        {

			@Override
			protected FilterResults performFiltering(CharSequence constraint) { 
				
				 FilterResults results = new FilterResults(); 
                 List<E> FilteredArrList = new ArrayList<E>();
				
                 if (mOriginalValues  == null) 
                	 mOriginalValues  = new ArrayList<E>(items); // guardar los datos originales en  mOriginalValues
                 if (constraint == null || constraint.length() == 0) 
                 {
                     // setear los valores originales a returnar  
                     results.count = mOriginalValues.size();
                     results.values = mOriginalValues;
                 } 
                 else 
                 {
                     constraint = constraint.toString().toLowerCase();
                     for (int i = 0; i < mOriginalValues.size(); i++) 
                     {
                    	try 
                    	{ 
                    		E data = mOriginalValues.get(i);                    		
							Object obj=data.getClass().getMethod("isMatch", CharSequence.class).invoke(data, constraint);
							if(Boolean.valueOf(obj.toString()))
								FilteredArrList.add(data);
							results.count = FilteredArrList.size();
		                    results.values = FilteredArrList;
							
						} catch (Exception e) {
							e.printStackTrace();
						} 
                     }
                 }

                 
			   return results;
			}

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) { 
				 items = (List<E>) results.values; //contiene los datos filtrados
                 notifyDataSetChanged();  //notificar al base adapter que hay nuevo valores que han sido filtrados
			}
			
        };
		
		
		return filter;
	}  
  
}

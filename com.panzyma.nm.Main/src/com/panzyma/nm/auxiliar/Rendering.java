package com.panzyma.nm.auxiliar;
	import android.view.View;
	import android.view.ViewGroup;
	import android.widget.TextView;
public class Rendering {



	// Scales the contents of the given view so that it completely fills the given
	// container on one axis (that is, we're scaling isotropically).
	private void scaleContents(View rootView, View container)
	{
		// Compute the scaling ratio
		float xScale = (float)container.getWidth() / rootView.getWidth();
		float yScale = (float)container.getHeight() / rootView.getHeight();
		float scale = Math.min(xScale, yScale);
		// Scale our contents
		scaleViewAndChildren(rootView, scale);
	}
	// Scale the given view, its contents, and all of its children by the given factor.
	public static void scaleViewAndChildren(View root, float scale)
	{
	    // Retrieve the view's layout information
	     ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
	    // Scale the view itself
	    if (layoutParams.width != ViewGroup.LayoutParams.FILL_PARENT && layoutParams.width != ViewGroup.LayoutParams.WRAP_CONTENT)
	       layoutParams.width *= scale; 
	    
		if (layoutParams.height != ViewGroup.LayoutParams.FILL_PARENT && layoutParams.height != ViewGroup.LayoutParams.WRAP_CONTENT)
		   layoutParams.height *= scale; 
		// If this view has margins, scale those too
		if (layoutParams instanceof ViewGroup.MarginLayoutParams)
		{
			ViewGroup.MarginLayoutParams marginParams =
			(ViewGroup.MarginLayoutParams)layoutParams;
			marginParams.leftMargin *= scale;
			marginParams.rightMargin *= scale;
			marginParams.topMargin *= scale;
			marginParams.bottomMargin *= scale;
		}
		// Set the layout information back into the view
		root.setLayoutParams(layoutParams);
	    //	www.vanteon.com
		//Automatically Scaling Android Apps for Multiple Screens page 10
		// Scale the view's padding
		root.setPadding(
						(int)(root.getPaddingLeft() * scale),
						(int)(root.getPaddingTop() * scale),
						(int)(root.getPaddingRight() * scale),
						(int)(root.getPaddingBottom() * scale));
		// If the root view is a TextView, scale the size of its text
		if (root instanceof TextView)
		{
			TextView textView = (TextView)root;
			textView.setTextSize(textView.getTextSize() * scale);
		}
		// If the root view is a ViewGroup, scale all of its children recursively
		if (root instanceof ViewGroup)
		{
		   ViewGroup groupView = (ViewGroup)root;
		   for (int cnt = 0; cnt < groupView.getChildCount(); ++cnt)
		     scaleViewAndChildren(groupView.getChildAt(cnt), scale);
		}
	} 
}


 



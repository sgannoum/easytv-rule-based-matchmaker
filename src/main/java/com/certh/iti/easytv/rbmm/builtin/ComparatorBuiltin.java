package com.certh.iti.easytv.rbmm.builtin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.regex.Pattern;

import org.apache.jena.graph.Node_Literal;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

abstract public class ComparatorBuiltin extends BaseBuiltin {
	
	protected int compareTo(Node_Literal v1, Node_Literal v2) {
		
		Object obj1 = v1.getLiteralValue();
				
		if(Integer.class.isInstance(obj1)) {
			Integer d1 = Number.class.cast(obj1).intValue();
			Integer d2 = Number.class.cast(v2.getLiteralValue()).intValue();

			return d1.compareTo(d2);
		} else if(Long.class.isInstance(obj1)) {
			Long d1 = Number.class.cast(obj1).longValue();
			Long d2 = Number.class.cast(v1.getLiteralDatatype().parse((String) v2.getLiteralValue())).longValue();

			return d1.compareTo(d2); 
		} else if(Double.class.isInstance(obj1)) {
			Double d1 = Number.class.cast(obj1).doubleValue();
			Double d2 = Number.class.cast(v2.getLiteralValue()).doubleValue();

			return d1.compareTo(d2); 
		} else if(Float.class.isInstance(obj1)) {
			Float d1 = Number.class.cast(obj1).floatValue();
			Float d2 = Number.class.cast(v2.getLiteralValue()).floatValue();

			return d1.compareTo(d2);
		} else if(Byte.class.isInstance(obj1)) {
			Byte d1 = Number.class.cast(obj1).byteValue();
			Byte d2 = Number.class.cast(v2.getLiteralValue()).byteValue();

			return d1.compareTo(d2);
		} else if(Date.class.isInstance(obj1)) {
			Date d1 = (Date) obj1;
			Date d2 = null;
			
			Pattern iso_8601 = Pattern.compile("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{3}Z");  
			Pattern simpleFormat = Pattern.compile("\\d{2}:\\d{2}:\\d{2}");  
			Pattern miniSimpleFormat = Pattern.compile("\\d{2}:\\d{2}");  
			
			String timeStr = (String) v2.getLiteralValue();
			
			
			if(iso_8601.matcher(timeStr).matches()) {
				d2 = Date.from( Instant.parse(timeStr));
			} else {
				if(miniSimpleFormat.matcher(timeStr).matches())  
					timeStr +=":00";
				
				if(simpleFormat.matcher(timeStr).matches()) 
				{	
					try {
						d2 = new SimpleDateFormat("HH:mm:ss").parse(timeStr);
					} catch (ParseException e) {}
					
				} else {
					throw new IllegalArgumentException("Wrong time format: "+ timeStr);
				}
			}

			return d1.compareTo(d2);
		} 
		else if(String.class.isInstance(obj1)) {
			String d1 = (String) obj1;
			String d2 = (String) v2.getLiteralValue();

			return d1.compareTo(d2);
		}
		
		return -1;
		
	}

}

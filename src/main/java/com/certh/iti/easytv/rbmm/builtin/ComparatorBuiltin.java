package com.certh.iti.easytv.rbmm.builtin;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Date;

import org.apache.jena.graph.Node_Literal;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;

abstract public class ComparatorBuiltin extends BaseBuiltin {
	
	protected int compareTo(Node_Literal v1, Node_Literal v2) {
		
		Object obj1 = v1.getLiteralValue();
				
		if(Integer.class.isInstance(obj1)) {
			Integer d1 = (Integer) obj1;
			Integer d2 = (Integer) v2.getLiteralValue();

			return d1.compareTo(d2);
		} else if(Long.class.isInstance(obj1)) {
			Long d1 = (Long) obj1;
			Long d2 = (Long) v1.getLiteralDatatype().parse((String) v2.getLiteralValue());

			return d1.compareTo(d2); 
		} else if(Double.class.isInstance(obj1)) {
			Double d1 = (Double) obj1;
			Double d2 = (Double) v2.getLiteralValue();

			return d1.compareTo(d2); 
		} else if(Float.class.isInstance(obj1)) {
			Float d1 = (Float) obj1;
			Float d2 = (Float) v2.getLiteralValue();

			return d1.compareTo(d2);
		} else if(Byte.class.isInstance(obj1)) {
			Byte d1 = (Byte) obj1;
			Byte d2 = (Byte) v2.getLiteralValue();

			return d1.compareTo(d2);
		} else if(Date.class.isInstance(obj1)) {
			Date d1 = (Date) obj1;
			Date d2 = (Date) v1.getLiteralDatatype().parse((String) v2.getLiteralValue());

			return d1.compareTo(d2);
		} else if(Instant.class.isInstance(obj1)) {
			Instant d1 = (Instant) obj1;
			Instant d2 = Instant.parse( (CharSequence) v2.getLiteralValue().toString());
		
			return d1.compareTo(d2);
		}  
		else if(String.class.isInstance(obj1)) {
			String d1 = (String) obj1;
			String d2 = (String) v2.getLiteralValue();
			
			try {
				Date date1 =  Date.from( Instant.parse(d1));
				Date date2 =  Date.from( Instant.parse(d2));
				return date1.compareTo(date2);
			} catch(DateTimeParseException e) {}

			return d1.compareTo(d2);
		}
		
		return -1;
		
	}

}

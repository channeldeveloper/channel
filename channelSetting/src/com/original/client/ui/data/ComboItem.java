package com.original.client.ui.data;

import java.io.Serializable;

public class ComboItem implements Serializable, Cloneable
{
	private static final long serialVersionUID = -9150541557874073564L;
	private Object id;
	private Object name;
	
	public static final ComboItem DEFAULT = new ComboItem(null, null);
	
	public ComboItem() {
		// TODO Auto-generated constructor stub
	}

	public ComboItem(Object id, Object name) {
		this.id = id;
		this.name = name;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if (obj == this)
			return true;

		if (obj instanceof ComboItem) {
			ComboItem ci = (ComboItem) obj;
			return ci.id == id || (ci.id != null && ci.id.equals(id));
		}
		return false;
	}
	
	@Override
	protected ComboItem clone()  {
		// TODO Auto-generated method stub
		ComboItem clone = null;
		try {
			clone = (ComboItem) super.clone();
		} catch (CloneNotSupportedException ex) {

		}
		return clone;
	}
	
	@Override
	public String toString() {
		return String.valueOf(name);
	}
	
	public Object getId() {
		return id;
	}
	public void setId(Object id) {
		this.id = id;
	}
	
	public Object getName() {
		return name;
	}
	public void setName(Object name) {
		this.name = name;
	}

}

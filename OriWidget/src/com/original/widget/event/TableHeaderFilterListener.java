package com.original.widget.event;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public abstract class TableHeaderFilterListener implements DocumentListener{

        private int column =  -1;
        public TableHeaderFilterListener(){
            super();
        }

        public TableHeaderFilterListener(int _column){
            super();
            column = _column;
        }

        public int getColumn(){
            return this.column;
        }
        
	@Override
	public void changedUpdate(DocumentEvent e) {
		FilterTextChanged(e);
		
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		FilterTextChanged(e);
		
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		FilterTextChanged(e);
		
	}
	
	public abstract void FilterTextChanged(DocumentEvent e);

}

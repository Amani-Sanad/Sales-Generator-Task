/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SIGModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Amani
 */
public class HeaderSideTableModel  extends AbstractTableModel {

    private List<HeaderSide> invList;
    private DateFormat datefm = new SimpleDateFormat("dd-MM-yyyy");
    
    public HeaderSideTableModel(List<HeaderSide> invList) {
        this.invList = invList;
    }

    public List<HeaderSide> getinvList() {
        return invList;
    }
    
    
    @Override
    public int getRowCount() {
        return invList.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "Invoice Num";
            case 1:
                return "Customer Name";
            case 2:
                return "Invoice Date";
            case 3:
                return "Invoice Total";
            default:
                return "";
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Integer.class;
            case 1:
                return String.class;
            case 2:
                return String.class;
            case 3:
                return Double.class;
            default:
                return Object.class;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        HeaderSide row = invList.get(rowIndex);
        
        switch (columnIndex) {
            case 0:
                return row.getInvNum();
            case 1:
                return row.getCustomerName();
            case 2:
                return datefm.format(row.getInvDate());
            case 3:
                return row.getInvTotal();
            default:
                return "";
        }
        
    }
    
}
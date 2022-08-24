
package SIGModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Amani
 */
public class LineSideTableModel extends AbstractTableModel {

    private List<LineSide> invLines;
    private DateFormat datefm = new SimpleDateFormat("dd-MM-yyyy");
    
    public LineSideTableModel(List<LineSide> invLines) {
        this.invLines = invLines;
    }

    public List<LineSide> getLineSide() {
        return invLines;
    }
    
    
    @Override
    public int getRowCount() {
        return invLines.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "Item Name";
            case 1:
                return "Item Price";
            case 2:
                return "Count";
            case 3:
                return "Line Total";
            default:
                return "";
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return String.class;
            case 1:
                return Double.class;
            case 2:
                return Integer.class;
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
        LineSide row = invLines.get(rowIndex);
        
        switch (columnIndex) {
            case 0:
                return row.getItemName();
            case 1:
                return row.getItemPrice();
            case 2:
                return row.getItemCount();
            case 3:
                return row.getLineTotal();
            default:
                return "";
        }
        
    }
    
}

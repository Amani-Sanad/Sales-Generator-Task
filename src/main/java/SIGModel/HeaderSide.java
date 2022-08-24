
package SIGModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Amani
 */
public class HeaderSide {
    private int invNum;
    private String customerName;
    private Date invDate;
    private ArrayList<LineSide> lines;  

    public HeaderSide(int invNum, String customerName, Date invDate) {
        this.invNum = invNum;
        this.customerName = customerName;
        this.invDate = invDate;
    }

    public Date getInvDate() {
        return invDate;
    }

    public void setInvDate(Date invDate) {
        this.invDate = invDate;
    }

    public int getInvNum() {
        return invNum;
    }

    public void setInvNum(int invNum) {
        this.invNum = invNum;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @Override
    public String toString() {
        String str = "HeaderSide{" + "invNum=" + invNum + ", customerName=" + customerName + ", invDate=" + invDate + '}';
        for (LineSide line : getLines()) {
            str += "\n\t" + line;
        }
        return str;
    }

    public ArrayList<LineSide> getLines() {
        if (lines == null)
            lines = new ArrayList<>();                                          // lazy create
        return lines;
    }

    public void setLines(ArrayList<LineSide> lines) {
        this.lines = lines;
    }

    public double getInvTotal() {
        double total = 0.0;
        for (LineSide line : getLines()) {
            total += line.getLineTotal();
        }
        return total;
    }
    
    public void addInvLine(LineSide line) {
        getLines().add(line);
    }
    
    public String getDataAsCSV() {
        DateFormat datefm = new SimpleDateFormat("dd-MM-yyyy");
        return "" + getInvNum() + "," + datefm.format(getInvDate()) + "," + getCustomerName();
    }
    
}

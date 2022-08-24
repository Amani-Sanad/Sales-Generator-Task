
package SIGController;

import SIGModel.HeaderSide;
import SIGModel.HeaderSideTableModel;
import SIGModel.LineSide;
import SIGModel.LineSideTableModel;
import SIGView.HeaderDialog;
import SIGView.InvoiceFrame;
import SIGView.LineDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Amani
 */
public class InvoiceController implements ActionListener, ListSelectionListener  {
    private InvoiceFrame frame;
    private DateFormat datefm = new SimpleDateFormat("dd-MM-yyyy");
    
    public InvoiceController(InvoiceFrame frame) {
        this.frame = frame;
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e) {

        switch (e.getActionCommand()) {
            case "CreateNewInvoice":
                displayNewInvoiceDialog();
                break;
            case "DeleteInvoice":
                deleteInvoice();
                break;
            case "CreateNewLine":
                displayNewLineDialog();
                break;
            case "DeleteLine":
                deleteLine();
                break;
            case "LoadFile":
                loadFile();
                break;
            case "SaveFile":
                saveData();
                break;
            case "createInvCancel":
                createInvCancel();
                break;
            case "createInvOK":
                createInvOK();
                break;
            case "createLineCancel":
                createLineCancel();
                break;
            case "createLineOK":
                createLineOK();
                break;
        }
    }

    private void loadFile() {
        JOptionPane.showMessageDialog(frame, "Select header file first!", "Dialog", JOptionPane.WARNING_MESSAGE);
        JFileChooser openFile = new JFileChooser();
        int result = openFile.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File headerFile = openFile.getSelectedFile();
            try {
                FileReader headerFr = new FileReader(headerFile);
                BufferedReader headerBr = new BufferedReader(headerFr);
                String headerLine = null;

                while ((headerLine = headerBr.readLine()) != null) {
                    String[] headerParts = headerLine.split(",");
                    String invNumStr = headerParts[0];
                    String invDateStr = headerParts[1];
                    String custName = headerParts[2];

                    int invNum = Integer.parseInt(invNumStr);
                    Date invDate = datefm.parse(invDateStr);

                    HeaderSide inv = new HeaderSide(invNum, custName, invDate);
                    frame.getinvList().add(inv);
                }

                JOptionPane.showMessageDialog(frame, "Now select lines file!", "Dialog !!", JOptionPane.WARNING_MESSAGE);
                result = openFile.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File linesFile = openFile.getSelectedFile();
                    BufferedReader linesBr = new BufferedReader(new FileReader(linesFile));
                    String linesLine = null;
                    while ((linesLine = linesBr.readLine()) != null) {
                        String[] lineParts = linesLine.split(",");
                        String invNumStr = lineParts[0];
                        String itemName = lineParts[1];
                        String itemPriceStr = lineParts[2];
                        String itemCountStr = lineParts[3];

                        int invNum = Integer.parseInt(invNumStr);
                        double itemPrice = Double.parseDouble(itemPriceStr);
                        int itemCount = Integer.parseInt(itemCountStr);
                        HeaderSide headerSide = findInvoiceByNum(invNum);
                        LineSide invLine = new LineSide(itemName, itemPrice, itemCount, headerSide);
                        headerSide.getLines().add(invLine);
                    }
                    frame.setHeaderSideTableModel (new HeaderSideTableModel (frame.getinvList()));
                    frame.getheaderSideTable().setModel(frame.getHeaderSideTableModel ());
                    frame.getheaderSideTable().validate();
                }
                System.out.println("Check");
            } catch (ParseException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Date Format Error\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Number Format Error\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "File Error\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Read Error\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        displayInvoices();
    }

    private void saveData() {
        String headers = "";
        String lines = "";
        for (HeaderSide headerSide : frame.getinvList()) {
            headers += headerSide.getDataAsCSV();
            headers += "\n";
            for (LineSide line : headerSide.getLines()) {
                lines += line.getDataAsCSV();
                lines += "\n";
            }
        }
        JOptionPane.showMessageDialog(frame, " Select file to save header data first!", "Dialog", JOptionPane.WARNING_MESSAGE);
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File headerFile = fileChooser.getSelectedFile();
            try {
                FileWriter hFW = new FileWriter(headerFile);
                hFW.write(headers);
                hFW.flush();
                hFW.close();

                JOptionPane.showMessageDialog(frame, " Now select file to save lines data!", "Dialog", JOptionPane.WARNING_MESSAGE);
                result = fileChooser.showSaveDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File linesFile = fileChooser.getSelectedFile();
                    FileWriter lFW = new FileWriter(linesFile);
                    lFW.write(lines);
                    lFW.flush();
                    lFW.close();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        JOptionPane.showMessageDialog(frame, " saved successfully", "Success", JOptionPane.INFORMATION_MESSAGE);

    }

    private HeaderSide findInvoiceByNum(int invNum) {
        HeaderSide headerSide = null;
        for (HeaderSide inv : frame.getinvList()) {
            if (invNum == inv.getInvNum()) {
                headerSide = inv;
                break;
            }
        }
        return headerSide;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        System.out.println(" Selected!");
        headerSideTableRowSelected();
    }

    private void headerSideTableRowSelected() {
        int selectedRowIndex = frame.getheaderSideTable().getSelectedRow();
        if (selectedRowIndex >= 0) {
            HeaderSide row = frame.getHeaderSideTableModel ().getinvList().get(selectedRowIndex);
            frame.getcustomerNametxf().setText(row.getCustomerName());
            frame.getinvoiveDatetxf().setText(datefm.format(row.getInvDate()));
            frame.getinvoiceNumbLbl1().setText("" + row.getInvNum());
            frame.getinvoiceTotalLbl1().setText("" + row.getInvTotal());
            ArrayList<LineSide> lines = row.getLines();
            frame.setLineSideTableModel (new LineSideTableModel (lines));
            frame.getlineSideTable().setModel(frame.getLineSideTableModel ());
            frame.getLineSideTableModel ().fireTableDataChanged();
        }
    }

    private void displayNewInvoiceDialog() {
        frame.setHeaderDialog(new HeaderDialog(frame));
        frame.getHeaderDialog().setVisible(true);
    }

    private void displayNewLineDialog() {
        frame.setLineDialog(new LineDialog(frame));
        frame.getLineDialog().setVisible(true);
    }

    private void createInvCancel() {
        frame.getHeaderDialog().setVisible(false);
        frame.getHeaderDialog().dispose();
        frame.setHeaderDialog(null);
    }

    private void createInvOK() {
        String custName = frame.getHeaderDialog().getCustNameField().getText();
        String invDateStr = frame.getHeaderDialog().getInvDateField().getText();
        frame.getHeaderDialog().setVisible(false);
        frame.getHeaderDialog().dispose();
        frame.setHeaderDialog(null);
        try {
            Date invDate = datefm.parse(invDateStr);
            int invNum = getNextInvoiceNum();
            HeaderSide headerSide = new HeaderSide(invNum, custName, invDate);
            frame.getinvList().add(headerSide);
            frame.getHeaderSideTableModel ().fireTableDataChanged();
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(frame, "Wrong date format", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        displayInvoices();
    }

    private int getNextInvoiceNum() {
        int max = 0;
        for (HeaderSide headerSide : frame.getinvList()) {
            if (headerSide.getInvNum() > max) {
                max = headerSide.getInvNum();
            }
        }
        return max + 1;
    }

    private void createLineCancel() {
        frame.getLineDialog().setVisible(false);
        frame.getLineDialog().dispose();
        frame.setLineDialog(null);
    }

    private void createLineOK() {
        String itemName = frame.getLineDialog().getItemNameField().getText();
        String itemCountStr = frame.getLineDialog().getItemCountField().getText();
        String itemPriceStr = frame.getLineDialog().getItemPriceField().getText();
        frame.getLineDialog().setVisible(false);
        frame.getLineDialog().dispose();
        frame.setLineDialog(null);
        int itemCount = Integer.parseInt(itemCountStr);
        double itemPrice = Double.parseDouble(itemPriceStr);
        int headerIndex = frame.getheaderSideTable().getSelectedRow();
        HeaderSide invoice = frame.getHeaderSideTableModel ().getinvList().get(headerIndex);

        LineSide LineSide = new LineSide(itemName, itemPrice, itemCount, invoice);
        invoice.addInvLine(LineSide);
        frame.getLineSideTableModel ().fireTableDataChanged();
        frame.getHeaderSideTableModel ().fireTableDataChanged();
        frame.getinvoiceTotalLbl1().setText("" + invoice.getInvTotal());
        displayInvoices();
    }

    private void deleteInvoice() {
        int invIndex = frame.getheaderSideTable().getSelectedRow();
        HeaderSide headerSide = frame.getHeaderSideTableModel ().getinvList().get(invIndex);
        frame.getHeaderSideTableModel ().getinvList().remove(invIndex);
        frame.getHeaderSideTableModel ().fireTableDataChanged();
        frame.setLineSideTableModel (new LineSideTableModel (new ArrayList<LineSide>()));
        frame.getlineSideTable().setModel(frame.getLineSideTableModel ());
        frame.getLineSideTableModel ().fireTableDataChanged();
        frame.getcustomerNametxf().setText("");
        frame.getinvoiveDatetxf().setText("");
        frame.getinvoiceNumbLbl1().setText("");
        frame.getinvoiceTotalLbl1().setText("");
        displayInvoices();
    }

    private void deleteLine() {
        int lineIndex = frame.getlineSideTable().getSelectedRow();
        LineSide line = frame.getLineSideTableModel ().getLineSide().get(lineIndex);
        frame.getLineSideTableModel ().getLineSide().remove(lineIndex);
        frame.getLineSideTableModel ().fireTableDataChanged();
        frame.getHeaderSideTableModel ().fireTableDataChanged();
        frame.getinvoiceTotalLbl1().setText("" + line.getHeader().getInvTotal());
        displayInvoices();
    }

    private void displayInvoices() {
        System.out.println("--------------------------");
        for (HeaderSide headerSide : frame.getinvList()) {
            System.out.println(headerSide);
        }
        System.out.println("--------------------------");
    }
    
}

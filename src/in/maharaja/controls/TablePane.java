package in.maharaja.controls;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.security.InvalidParameterException;

/**
 * Created by Prateek on 08-11-2015.
 */
public class TablePane extends JPanel{
    private JTable myTable;
    private DefaultTableModel tableModel;

    public TablePane(int width, int height){
        super();

        setSize( new Dimension(width, height));
        setLayout( new BorderLayout(10, 10) );
        myTable = new JTable();
        tableModel = new DefaultTableModel();
        JScrollPane scrollPane = new JScrollPane( myTable );
//        scrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED );
        myTable.setFillsViewportHeight( true );
        add( scrollPane, BorderLayout.CENTER );

        setBorder( new EmptyBorder(20, 20, 20, 20));

        myTable.setModel( tableModel );
    }

    public void setColumnHeaders(Object[] columnHeaders){
        tableModel.setColumnIdentifiers( columnHeaders );
        tableModel.fireTableDataChanged();
    }

    public void setData(Object[][] data){
        int rowCount = tableModel.getRowCount();

        for(int i = rowCount - 1; i >= 0; i--){
            tableModel.removeRow(i);
        }

        for(int i = 0; i< data.length; i++){
            tableModel.addRow(data[i]);
        }

        tableModel.fireTableDataChanged();
    }

    public void addRow(Object[] data){
        tableModel.addRow(data);
        tableModel.fireTableDataChanged();
    }

    public void setColumnSize(int[] width){
        if( width.length != tableModel.getColumnCount() )
            throw new InvalidParameterException("Size for " + width.length + " columns Provided, " + tableModel.getColumnCount() + " Columns are available " );

        for(int i=0; i<tableModel.getColumnCount(); i++){
            myTable.getColumnModel().getColumn(i).setPreferredWidth(width[i]);
        }
    }

    public void setDataAt(Object data, int row, int column){
        tableModel.setValueAt(data, row, column);
        tableModel.fireTableCellUpdated(row, column);
    }

    public String getValueAt(int row, int column){
        return tableModel.getValueAt(row, column).toString();
    }

    public int[] findValue(String value){
        for(int i=0;i<myTable.getColumnCount();i++){
            for(int j=0;j<myTable.getRowCount();j++){
                if( value.compareToIgnoreCase( getValueAt(i, j) ) == 0  )
                    return new int[]{i, j};
            }
        }

        return new int[]{-1, -1};
    }


}
package in.maharaja.gui;

import in.maharaja.controls.TablePane;
import in.maharaja.utilities.TxtWriter;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Prateek on 30-07-2015.
 */
public class DailyReport extends AbstractFrame {
    private JTextField fileNameField;
    private JButton browseFileButton;
    private TablePane resultTable;
    private final Object[] columnHeader = {"Products", "#", "Amount"};

    public DailyReport(){
        super("Daily Report");

        fileNameField = new JTextField();
        fileNameField.setPreferredSize(new Dimension(250,28));
        fileNameField.setEditable(false);
        fileNameField.setText("D:\\COUNTER\\" + TxtWriter.getFileName());

        browseFileButton = new JButton("Select File");

        resultTable = new TablePane(390, 300);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setPreferredSize(new Dimension(430, 300));

        JPanel contentPane = new JPanel();
        setContentPane( contentPane );

        registerVariable("FileNameField", fileNameField);
        registerVariable("BrowseButton", browseFileButton);


        Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((int) (screenDimension.getWidth() / 2) - (this.getWidth() / 2), (int) (screenDimension.getHeight() / 2) - (this.getHeight() / 2));

    }

    public void createGUI(){
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));

        JPanel topPanel = new JPanel();
        JPanel bottomPanel = new JPanel();

        topPanel.setPreferredSize(new Dimension(390, 50));

        topPanel.setLayout(new FlowLayout());
        topPanel.add(new JLabel("FileName"));
        topPanel.add(fileNameField);
        topPanel.add(browseFileButton);

        getContentPane().add(topPanel);
        getContentPane().add(resultTable);

        resultTable.setColumnHeaders(columnHeader);
        resultTable.setColumnSize(new int[]{220, 80, 120});

        registerVariable("Table", resultTable);
    }

    public void showGUI(){
        this.pack();
        this.setVisible( true );
    }
}
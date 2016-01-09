package in.maharaja.gui;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Created by MPONS040401 on 11/29/2015.
 */
public class AddProduct extends AbstractFrame {

    private JList<String> productList;

    public AddProduct(){
        super("Add New Product");
        setSize(new Dimension(300, 500) );
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE );

        productList = new JList<>();
        productList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
    }

    public void createGUI(){

        setLayout( new MigLayout() );

        JScrollPane scrollPane = new JScrollPane(productList);
        scrollPane.setHorizontalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED );
        add( scrollPane, "dock west");

        add(new JLabel("Name"));

        JTextField nameField = new JTextField(30);
        add( nameField );
        add(new JLabel("Rate") );

        JTextField rateField = new JTextField(30);
        add(rateField);
        add(new JLabel("Limited") );

        JCheckBox limited = new JCheckBox("Is Limited");
        add(limited);

        registerVariable("Product Name", nameField);
        registerVariable("Rate", rateField);
    }

    public void showGUI(){
        setVisible( true );
    }
}

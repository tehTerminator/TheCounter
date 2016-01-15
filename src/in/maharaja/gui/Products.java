package in.maharaja.gui;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

/**
 * Created by MPONS040401 on 14-01-2016.
 */
public class Products extends AbstractFrame {

    private JList<String> productList;

    public Products() {
        super("Products");
        setLayout(new MigLayout());
        setSize(new Dimension(280, 300));

        setResizable(false);

    }

    public void createGUI() {
        JButton addBtn = new JButton("Add"),
                editBtn = new JButton("Edit"),
                delBtn = new JButton("Delete"),
                refBtn = new JButton("Refresh");

        add(addBtn);
        add(editBtn);
        add(delBtn);
        add(refBtn, "wrap");

        registerVariable("Add Button", addBtn);
        registerVariable("Edit Button", editBtn);
        registerVariable("Delete Button", delBtn);
        registerVariable("Refresh Button", refBtn);


        productList = new JList<>();
        productList.setVisibleRowCount(-1);
        productList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        JScrollPane scrollPane = new JScrollPane(productList);
        scrollPane.setMinimumSize(new Dimension(260, 200));
        add(scrollPane, "dock south");

        registerVariable("Product List", productList);
    }

    public void showGUI() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public void addData(Vector<String> data) {
        DefaultListModel listModel = new DefaultListModel();
        productList.setModel(listModel);
        listModel.clear();
        for (String s : data) {
            listModel.addElement(s);
        }
    }
}

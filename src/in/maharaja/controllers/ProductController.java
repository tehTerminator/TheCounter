package in.maharaja.controllers;

import in.maharaja.gui.Products;
import in.maharaja.sql.Connector;

import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

/**
 * Controller for Product Form
 */
public class ProductController extends Controller<Products> {

    public ProductController(Products app) {
        super(app);

        app.createGUI();
        registerEvents();
        app.showGUI();

    }

    @Override
    public void registerEvents() {
        getApp().registerEvent("Refresh Button", (ActionListener) e -> reloadProducts());
    }

    private void reloadProducts() {
        try {
            Connector con = new Connector("sa", "");
            Statement stmt = con.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM COUNTER.PRODUCTS");

            Vector<String> productList = new Vector<>();

            while (rs.next()) {
                productList.add(rs.getString("TITLE"));
            }

            getApp().addData(productList);
        } catch (Exception ex) {
            getApp().showError("SQL Exception", ex.toString());
        }
    }
}

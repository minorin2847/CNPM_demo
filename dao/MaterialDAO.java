package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import model.Material;

public class MaterialDAO extends DAO {

    public MaterialDAO() {
        super();
    }

    public static ArrayList<Material> searchMaterial(String keyword) {
        ArrayList<Material> result = new ArrayList<Material>();
        String sql = "SELECT id, name, unitPrice FROM tbl_Material WHERE name LIKE ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Material material = new Material();
                material.setId(rs.getInt("id"));
                material.setName(rs.getString("name"));
                material.setUnitPrice(rs.getInt("unitPrice"));
                result.add(material);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    
}

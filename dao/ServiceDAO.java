package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import model.Service;

public class ServiceDAO extends DAO {
    
    public ServiceDAO() {
        super();
    }

    public static ArrayList<Service> searchService(String keyword) {
        ArrayList<Service> result = new ArrayList<Service>();
        String sql = "SELECT id, name, unitPrice FROM tbl_Service WHERE name LIKE ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Service service = new Service();
                service.setId(rs.getInt("id"));
                service.setName(rs.getString("name"));
                service.setUnitPrice(rs.getInt("unitPrice"));
                result.add(service);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}

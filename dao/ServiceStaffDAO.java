package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import model.ServiceStaff;

public class ServiceStaffDAO extends DAO {

    public ServiceStaffDAO() {
        super();
    }

    public static ArrayList<ServiceStaff> getServiceStaffs() {
        ArrayList<ServiceStaff> result = new ArrayList<ServiceStaff>();
        String sql = "SELECT id, name, phone FROM tbl_ServiceStaff WHERE status = 'Active'";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ServiceStaff serviceStaff = new ServiceStaff();
                serviceStaff.setId(rs.getInt("id"));
                serviceStaff.setName(rs.getString("name"));
                serviceStaff.setPhone(rs.getString("phone"));
                result.add(serviceStaff);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}

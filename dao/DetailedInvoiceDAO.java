package dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import model.CustomerSlot;
import model.DetailedInvoice;
import model.DetailedInvoiceMaterial;
import model.DetailedInvoiceService;
import model.Material;
import model.Service;
import model.ServiceStaff;

public class DetailedInvoiceDAO extends DAO {

    public DetailedInvoiceDAO() {
        super();
    }

    public static DetailedInvoice getDetailedInvoice(CustomerSlot slot, Date date) {
        // Customer slot only includes slot ID, customer name and customer phone
        DetailedInvoice result = new DetailedInvoice();
        String sql = 
        /* Service table */
        "SELECT di.id, " + 
        "'Service' AS type, NULL AS quantity, dis.price, s.name, ss.name AS staff_name, dis.price AS total " +
        "FROM tbl_DetailedInvoice di " +
        "INNER JOIN tbl_CustomerSlot cs ON di.idtblCustomerSlot = cs.id " +
        "INNER JOIN tbl_Slot sl ON cs.idtblSlot = sl.id " +
        "INNER JOIN tbl_Customer c ON cs.idtblCustomer = c.id " +
        "INNER JOIN tbl_DetailedInvoiceService dis ON dis.idtblDetailedInvoice = di.id " +
        "INNER JOIN tbl_Service s ON dis.idtblService = s.id " +
        "INNER JOIN tbl_ServiceStaff ss ON dis.idtblServiceStaff = ss.id " +
        "WHERE sl.id = ? AND c.name = ? AND c.phone = ? AND di.date = ? AND di.status = 'Pending' " +

        "UNION ALL " +

        /* Material */
        "SELECT di.id, " + 
        "'Material' AS type, dim.quantity AS quantity, dim.price, m.name, NULL AS staff_name, dim.quantity*dim.price AS total " +
        "FROM tbl_DetailedInvoice di " +
        "INNER JOIN tbl_CustomerSlot cs ON di.idtblCustomerSlot = cs.id " +
        "INNER JOIN tbl_Slot sl ON cs.idtblSlot = sl.id " +
        "INNER JOIN tbl_Customer c ON cs.idtblCustomer = c.id " +
        "INNER JOIN tbl_DetailedInvoiceMaterial dim ON dim.idtblDetailedInvoice = di.id " +
        "INNER JOIN tbl_Material m ON dim.idtblMaterial = m.id " +
        "WHERE sl.id = ? AND c.name = ? AND c.phone = ? AND di.date = ? AND di.status = 'Pending'";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, slot.getSlot().getId());
            ps.setString(2, slot.getCustomer().getName());
            ps.setString(3, slot.getCustomer().getPhone());
            ps.setString(4, sdf.format(LocalDate.now()));
            ps.setInt(5, slot.getSlot().getId());
            ps.setString(6, slot.getCustomer().getName());
            ps.setString(7, slot.getCustomer().getPhone());
            ps.setString(8, sdf.format(LocalDate.now()));

            ResultSet rs = ps.executeQuery();
            int total = 0;
            while (rs.next()) {
                if (result.getId() == 0) {
                    result.setId(rs.getInt("di.id"));
                    result.setCustomerSlot(slot);
                }
                if (rs.getString("type") == "Service") {
                    DetailedInvoiceService dis = new DetailedInvoiceService();
                    dis.setPrice(rs.getInt("dis.price"));
                    Service s = new Service();
                    s.setName(rs.getString("s.name"));
                    dis.setService(s);
                    ServiceStaff ss = new ServiceStaff();
                    ss.setName(rs.getString("staff_name"));
                    dis.setServiceStaff(ss);
                    dis.setTotal(rs.getInt("total"));
                    total += dis.getTotal();
                    result.getServices().add(dis);
                } else if (rs.getString("type") == "Material") {
                    DetailedInvoiceMaterial dim = new DetailedInvoiceMaterial();
                    dim.setPrice(rs.getInt("dim.price"));
                    dim.setQuantity(rs.getInt("dim.quantity"));
                    Material m = new Material();
                    m.setName(rs.getString("m.name"));
                    dim.setMaterial(m);
                    dim.setTotal(rs.getInt("total"));
                    total += dim.getTotal();
                    result.getMaterials().add(dim);
                }
            }
            if (total > 0) result.setTotal(total);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    
}

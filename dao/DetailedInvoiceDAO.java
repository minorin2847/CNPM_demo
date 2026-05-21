package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

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
                "SELECT di.id AS invoice_id, " +
                        "'Service' AS type, NULL AS quantity, " +
                        "dis.price AS item_price, s.id AS item_id, s.name AS item_name, " +
                        "ss.id AS staff_id, ss.name AS staff_name " +
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
                        "SELECT di.id AS invoice_id, " +
                        "'Material' AS type, dim.quantity AS quantity, " +
                        "dim.price AS item_price, m.id AS item_id, m.name AS item_name, " +
                        "NULL AS staff_id, NULL AS staff_name " +
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
            ps.setString(4, sdf.format(new Date()));
            ps.setInt(5, slot.getSlot().getId());
            ps.setString(6, slot.getCustomer().getName());
            ps.setString(7, slot.getCustomer().getPhone());
            ps.setString(8, sdf.format(new Date()));

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (result.getId() == 0) {
                    result.setId(rs.getInt("invoice_id"));
                    result.setCustomerSlot(slot);
                }
                if ("Service".equals(rs.getString("type"))) {
                    DetailedInvoiceService dis = new DetailedInvoiceService();
                    dis.setPrice(rs.getInt("item_price"));
                    Service s = new Service();
                    s.setId(rs.getInt("item_id"));
                    s.setName(rs.getString("item_name"));
                    dis.setService(s);
                    ServiceStaff ss = new ServiceStaff();
                    ss.setId(rs.getInt("staff_id"));
                    ss.setName(rs.getString("staff_name"));
                    dis.setServiceStaff(ss);
                    result.getServices().add(dis);
                } else if ("Material".equals(rs.getString("type"))) {
                    DetailedInvoiceMaterial dim = new DetailedInvoiceMaterial();
                    dim.setPrice(rs.getInt("item_price"));
                    dim.setQuantity(rs.getInt("quantity"));
                    Material m = new Material();
                    m.setId(rs.getInt("item_id"));
                    m.setName(rs.getString("item_name"));
                    dim.setMaterial(m);
                    result.getMaterials().add(dim);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static boolean saveDetailedInvoice(DetailedInvoice invoice) {
        // Check if invoice is valid
        if (invoice == null || invoice.getId() == 0 || invoice.getCustomerSlot() == null) {
            return false;
        }

        boolean result = false;
        try {
            // Disable auto commit
            con.setAutoCommit(false);

            // Delete all services and insert again
            String deleteDetailedInvoiceServices = "DELETE FROM tbl_DetailedInvoiceService WHERE idtblDetailedInvoice = ?";
            PreparedStatement deleteServicesStatement = con.prepareStatement(deleteDetailedInvoiceServices);
            deleteServicesStatement.setInt(1, invoice.getId());
            deleteServicesStatement.executeUpdate();

            String insertDetailedInvoiceService = "INSERT INTO tbl_DetailedInvoiceService(price, idtblDetailedInvoice, idtblService, idtblServiceStaff) VALUES (?, ?, ?, ?)";
            PreparedStatement insertServiceStatement = con.prepareStatement(insertDetailedInvoiceService);
            for (DetailedInvoiceService dis : invoice.getServices()) {
                insertServiceStatement.setInt(1, dis.getPrice());
                insertServiceStatement.setInt(2, invoice.getId());
                insertServiceStatement.setInt(3, dis.getService().getId());
                insertServiceStatement.setInt(4, dis.getServiceStaff().getId());
                insertServiceStatement.addBatch();
            }
            insertServiceStatement.executeBatch();

            // Delete all materials and insert again
            String deleteDetailedInvoiceMaterials = "DELETE FROM tbl_DetailedInvoiceMaterial WHERE idtblDetailedInvoice = ?";
            PreparedStatement deleteMaterialsStatement = con.prepareStatement(deleteDetailedInvoiceMaterials);
            deleteMaterialsStatement.setInt(1, invoice.getId());
            deleteMaterialsStatement.executeUpdate();

            String insertDetailedInvoiceMaterial = "INSERT INTO tbl_DetailedInvoiceMaterial(quantity, price, idtblDetailedInvoice, idtblMaterial) VALUES (?, ?, ?, ?)";
            PreparedStatement insertMaterialStatement = con.prepareStatement(insertDetailedInvoiceMaterial);
            for (DetailedInvoiceMaterial dim : invoice.getMaterials()) {
                insertMaterialStatement.setInt(1, dim.getQuantity());
                insertMaterialStatement.setInt(2, dim.getPrice());
                insertMaterialStatement.setInt(3, invoice.getId());
                insertMaterialStatement.setInt(4, dim.getMaterial().getId());
                insertMaterialStatement.addBatch();
            }
            insertMaterialStatement.executeBatch();

            // Update customer slot status to 'Finished'
            String updateCustomerSlotStatus = "UPDATE tbl_CustomerSlot SET status = 'Finished' WHERE id = ?";
            PreparedStatement updateCustomerSlotStatement = con.prepareStatement(updateCustomerSlotStatus);
            updateCustomerSlotStatement.setInt(1, invoice.getCustomerSlot().getId());
            updateCustomerSlotStatement.executeUpdate();

            // Update invoice status to 'Paid'
            String updateInvoiceStatus = "UPDATE tbl_DetailedInvoice SET status = 'Paid' WHERE id = ?";
            PreparedStatement updateInvoiceStatement = con.prepareStatement(updateInvoiceStatus);
            updateInvoiceStatement.setInt(1, invoice.getId());
            updateInvoiceStatement.executeUpdate();

            // Commit transaction
            con.commit();
            result = true;
        } catch (Exception e) {
            e.printStackTrace();

            if (con != null) {
                // Rollback if error
                try {
                    con.rollback();
                } catch (Exception rollbackException) {
                    rollbackException.printStackTrace();
                }
            }
        } finally {
            // Restore auto commit
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                } catch (Exception autocommitException) {
                    autocommitException.printStackTrace();
                }
            }
        }

        return result;
    }
}

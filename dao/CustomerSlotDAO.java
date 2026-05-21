package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import model.Customer;
import model.CustomerSlot;
import model.Slot;

public class CustomerSlotDAO extends DAO {

    public CustomerSlotDAO() {
        super();
    }

    public static ArrayList<CustomerSlot> getCustomerSlots() {
        ArrayList<CustomerSlot> customerSlots = new ArrayList<CustomerSlot>();
        String sql = "SELECT cs.id AS customer_slot_id, s.id AS slot_id, c.name AS customer_name, c.phone AS customer_phone FROM tbl_CustomerSlot cs "
                +
                "INNER JOIN tbl_Slot s ON cs.idtblSlot = s.id " +
                "INNER JOIN tbl_Customer c ON cs.idtblCustomer = c.id " +
                "WHERE cs.status = 'Pending'";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Slot slot = new Slot();
                Customer customer = new Customer();
                CustomerSlot customerSlot = new CustomerSlot();

                customerSlot.setId(rs.getInt("customer_slot_id"));
                slot.setId(rs.getInt("slot_id"));
                customer.setName(rs.getString("customer_name"));
                customer.setPhone(rs.getString("customer_phone"));
                customerSlot.setSlot(slot);
                customerSlot.setCustomer(customer);
                customerSlots.add(customerSlot);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return customerSlots;
    }

}

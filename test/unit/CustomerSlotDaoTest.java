package test.unit;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import dao.CustomerSlotDAO;
import model.CustomerSlot;

public class CustomerSlotDaoTest {
    CustomerSlotDAO csd = new CustomerSlotDAO();

    @Test
    public void testGetCustomerSlots() {
        ArrayList<CustomerSlot> slots = CustomerSlotDAO.getCustomerSlots();
        Assert.assertNotNull(slots);
        Assert.assertTrue(slots.size() > 0);
        
        for (CustomerSlot cs : slots) {
            Assert.assertNotNull(cs.getSlot());
            Assert.assertNotNull(cs.getCustomer());
            Assert.assertTrue(cs.getSlot().getId() > 0);
            Assert.assertNotNull(cs.getCustomer().getName());
            Assert.assertNotNull(cs.getCustomer().getPhone());
        }
    }
}

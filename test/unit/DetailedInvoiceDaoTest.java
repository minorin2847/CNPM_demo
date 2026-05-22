package test.unit;

import java.sql.Connection;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import dao.DAO;
import dao.DetailedInvoiceDAO;
import model.Customer;
import model.CustomerSlot;
import model.DetailedInvoice;
import model.Slot;

public class DetailedInvoiceDaoTest {
    DetailedInvoiceDAO did = new DetailedInvoiceDAO();

    @Test
    public void testGetDetailedInvoice() {
        CustomerSlot slot = new CustomerSlot();
        slot.setId(1);
        
        Slot s = new Slot();
        s.setId(1);
        slot.setSlot(s);
        
        Customer c = new Customer();
        c.setId(1);
        c.setName("Nguyen Van A");
        c.setPhone("0123456789");
        slot.setCustomer(c);
        
        Date date = new Date(); // DetailedInvoiceDAO uses current date internally
        
        DetailedInvoice invoice = DetailedInvoiceDAO.getDetailedInvoice(slot, date);
        Assert.assertNotNull(invoice);
        Assert.assertEquals(1, invoice.getId());
        Assert.assertTrue(invoice.getServices().size() > 0);
        Assert.assertTrue(invoice.getMaterials().size() > 0);
    }
    
    @Test
    public void testSaveDetailedInvoice() {
        Connection con = DAO.con;
        try {
            con.setAutoCommit(false);
            
            CustomerSlot slot = new CustomerSlot();
            slot.setId(1);
            
            Slot s = new Slot();
            s.setId(1);
            slot.setSlot(s);
            
            Customer c = new Customer();
            c.setId(1);
            c.setName("Nguyen Van A");
            c.setPhone("0123456789");
            slot.setCustomer(c);
            
            Date date = new Date();
            DetailedInvoice invoice = DetailedInvoiceDAO.getDetailedInvoice(slot, date);
            
            if (invoice != null && invoice.getId() > 0) {
                boolean result = DetailedInvoiceDAO.saveDetailedInvoice(invoice);
                Assert.assertTrue(result);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                con.rollback();
                con.setAutoCommit(true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}

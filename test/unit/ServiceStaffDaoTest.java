package test.unit;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import dao.ServiceStaffDAO;
import model.ServiceStaff;

public class ServiceStaffDaoTest {
    ServiceStaffDAO ssd = new ServiceStaffDAO();

    @Test
    public void testGetServiceStaffs() {
        ArrayList<ServiceStaff> staffs = ServiceStaffDAO.getServiceStaffs();
        Assert.assertNotNull(staffs);
        Assert.assertTrue(staffs.size() > 0);
        
        for (ServiceStaff staff : staffs) {
            Assert.assertTrue(staff.getId() > 0);
            Assert.assertNotNull(staff.getName());
            Assert.assertNotNull(staff.getPhone());
        }
    }
}

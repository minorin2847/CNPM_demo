package view.receptionist.detailedinvoiceitem;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dao.ServiceStaffDAO;
import model.DetailedInvoiceService;
import model.ServiceStaff;
import model.User;
import view.receptionist.DetailedInvoiceFrm;

public class EditDetailedInvoiceServiceFrm extends JFrame implements ActionListener {
	private User user;
	private DetailedInvoiceService dis;
	private DetailedInvoiceFrm parentFrm;
	private JTextField txtId, txtName, txtType, txtPrice, txtTotal;
	private JComboBox<ServiceStaff> cbStaff;
	private JButton btnCancel, btnSave;
	private boolean isNew;

	public EditDetailedInvoiceServiceFrm(User user, DetailedInvoiceService dis, DetailedInvoiceFrm parentFrm) {
		super("Edit invoice item");
		this.user = user;
		this.dis = dis;
		this.parentFrm = parentFrm;
		this.isNew = !parentFrm.getInvoice().getServices().contains(dis);

		JPanel mainPane = new JPanel();
		mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.PAGE_AXIS));

		JLabel lblTitle = new JLabel("Edit invoice item");
		lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblTitle.setFont(lblTitle.getFont().deriveFont(20.0f));
		mainPane.add(Box.createRigidArea(new Dimension(0, 10)));
		mainPane.add(lblTitle);
		mainPane.add(Box.createRigidArea(new Dimension(0, 20)));

		// Form fields
		JPanel formPane = new JPanel(new GridLayout(6, 2, 5, 5));
		formPane.setMaximumSize(new Dimension(300, 200));
		
		formPane.add(new JLabel("ID:"));
		txtId = new JTextField(String.valueOf(dis.getService().getId()));
		txtId.setEditable(false);
		formPane.add(txtId);

		formPane.add(new JLabel("Name:"));
		txtName = new JTextField(dis.getService().getName());
		txtName.setEditable(false);
		formPane.add(txtName);

		formPane.add(new JLabel("Type:"));
		txtType = new JTextField("Service");
		txtType.setEditable(false);
		formPane.add(txtType);

		formPane.add(new JLabel("Service staff:"));
		ArrayList<ServiceStaff> staffList = ServiceStaffDAO.getServiceStaffs();
		cbStaff = new JComboBox<ServiceStaff>(staffList.toArray(new ServiceStaff[0]));
		// Custom renderer for cbStaff if needed, or override toString in ServiceStaff
		// Assume ServiceStaff has a good toString() or we can just use the object
		if (dis.getServiceStaff() != null) {
			for (int i = 0; i < cbStaff.getItemCount(); i++) {
				if (cbStaff.getItemAt(i).getId() == dis.getServiceStaff().getId()) {
					cbStaff.setSelectedIndex(i);
					break;
				}
			}
		}
		formPane.add(cbStaff);

		formPane.add(new JLabel("Price:"));
		txtPrice = new JTextField(String.valueOf(dis.getPrice()));
		formPane.add(txtPrice);

		formPane.add(new JLabel("Total:"));
		txtTotal = new JTextField(String.valueOf(dis.getPrice()));
		txtTotal.setEditable(false);
		formPane.add(txtTotal);

		// Key listener to auto-update total
		txtPrice.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				txtTotal.setText(txtPrice.getText());
			}
		});

		mainPane.add(formPane);

		// Buttons
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(this);
		btnSave = new JButton("Save");
		btnSave.addActionListener(this);
		
		buttonPane.add(Box.createHorizontalGlue());
		buttonPane.add(btnCancel);
		buttonPane.add(Box.createRigidArea(new Dimension(20, 0)));
		buttonPane.add(btnSave);
		buttonPane.add(Box.createHorizontalGlue());

		mainPane.add(Box.createRigidArea(new Dimension(0, 20)));
		mainPane.add(buttonPane);
		mainPane.add(Box.createRigidArea(new Dimension(0, 10)));

		this.setSize(400, 350);
		this.setLocation(250, 50);
		this.add(mainPane, BorderLayout.CENTER);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ((e.getSource() instanceof JButton) && (((JButton) e.getSource()).equals(btnCancel))) {
			this.dispose();
		} else if ((e.getSource() instanceof JButton) && (((JButton) e.getSource()).equals(btnSave))) {
			try {
				int price = Integer.parseInt(txtPrice.getText());
				dis.setPrice(price);
				dis.setServiceStaff((ServiceStaff) cbStaff.getSelectedItem());
				
				if (isNew) {
					parentFrm.getInvoice().getServices().add(dis);
				}
				parentFrm.loadDataToTable();
				this.dispose();
			} catch (NumberFormatException ex) {
				// Handle invalid input
			}
		}
	}
}

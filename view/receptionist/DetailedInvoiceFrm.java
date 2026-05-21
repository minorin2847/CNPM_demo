package view.receptionist;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import dao.DetailedInvoiceDAO;
import model.CustomerSlot;
import model.DetailedInvoice;
import model.DetailedInvoiceMaterial;
import model.DetailedInvoiceService;
import model.User;
import view.receptionist.detailedinvoiceitem.AddDetailedInvoiceItemFrm;
import view.receptionist.detailedinvoiceitem.ConfirmDeleteDetailedInvoiceItemFrm;
import view.receptionist.detailedinvoiceitem.EditDetailedInvoiceMaterialFrm;
import view.receptionist.detailedinvoiceitem.EditDetailedInvoiceServiceFrm;
import view.user.ReceptionistHomeFrm;

public class DetailedInvoiceFrm extends JFrame implements ActionListener {
	private User user;
	private DetailedInvoice invoice;
	private JTable tblInvoice;
	private DefaultTableModel tmInvoice;
	private JButton btnAdd, btnCancel, btnConfirm;
	private JLabel lblTotal;
	private int totalAmount = 0;

	public DetailedInvoiceFrm(User user, CustomerSlot customerSlot) {
		super("Invoice details");
		this.user = user;

		// Fetch invoice from DB
		invoice = DetailedInvoiceDAO.getDetailedInvoice(customerSlot, new Date(System.currentTimeMillis()));

		JPanel mainPane = new JPanel();
		mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.PAGE_AXIS));

		JLabel lblTitle = new JLabel("Invoice details");
		lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblTitle.setFont(lblTitle.getFont().deriveFont(24.0f));
		mainPane.add(Box.createRigidArea(new Dimension(0, 10)));
		mainPane.add(lblTitle);
		mainPane.add(Box.createRigidArea(new Dimension(0, 10)));

		// Customer info
		JPanel infoPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
		String custName = customerSlot != null && customerSlot.getCustomer() != null
				? customerSlot.getCustomer().getName()
				: "N/A";
		String custPhone = customerSlot != null && customerSlot.getCustomer() != null
				? customerSlot.getCustomer().getPhone()
				: "N/A";
		infoPane.add(new JLabel("<html>Name: " + custName + "<br/>Phone number: " + custPhone + "</html>"));
		mainPane.add(infoPane);
		mainPane.add(Box.createRigidArea(new Dimension(0, 10)));

		// Table for Invoice Items
		String[] columnNames = { "ID", "Name", "Type", "Service staff", "Unit price", "Qty", "Money", " ",
				" " };
		tmInvoice = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tblInvoice = new JTable(tmInvoice);
		tblInvoice.setFillsViewportHeight(true);

		loadDataToTable();

		// Click listener for Edit/Delete
		tblInvoice.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int column = tblInvoice.getColumnModel().getColumnIndexAtX(e.getX());
				int row = e.getY() / tblInvoice.getRowHeight();
				if (row < tblInvoice.getRowCount() && row >= 0 && column < tblInvoice.getColumnCount() && column >= 0) {
					Object value = tblInvoice.getValueAt(row, column);
					if ("Edit".equals(value)) {
						String type = tblInvoice.getValueAt(row, 2).toString();

						if ("Material".equals(type)) {
							// Find the model object
							DetailedInvoiceMaterial selectedMaterial = invoice.getMaterials()
									.get(getMaterialIndex(row));
							(new EditDetailedInvoiceMaterialFrm(user, selectedMaterial, DetailedInvoiceFrm.this))
									.setVisible(true);
						} else {
							DetailedInvoiceService selectedService = invoice.getServices().get(getServiceIndex(row));
							(new EditDetailedInvoiceServiceFrm(user, selectedService, DetailedInvoiceFrm.this))
									.setVisible(true);
						}
					} else if ("Delete".equals(value)) {
						String itemName = tblInvoice.getValueAt(row, 1).toString();
						String type = tblInvoice.getValueAt(row, 2).toString();
						Object itemToDelete = null;
						if ("Material".equals(type)) {
							itemToDelete = invoice.getMaterials().get(getMaterialIndex(row));
						} else {
							itemToDelete = invoice.getServices().get(getServiceIndex(row));
						}
						(new ConfirmDeleteDetailedInvoiceItemFrm(DetailedInvoiceFrm.this, itemName, itemToDelete))
								.setVisible(true);
					}
				}
			}
		});

		JScrollPane scrollPane = new JScrollPane(tblInvoice);
		mainPane.add(scrollPane);

		// Add new item button
		JPanel addPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
		btnAdd = new JButton("+ Add new materials/services");
		btnAdd.addActionListener(this);
		addPane.add(btnAdd);
		mainPane.add(addPane);

		// Total
		JPanel totalPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		lblTotal = new JLabel("Total: " + totalAmount + " VND");
		lblTotal.setFont(lblTotal.getFont().deriveFont(16.0f));
		totalPane.add(lblTotal);
		mainPane.add(totalPane);

		// Buttons
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(this);
		btnConfirm = new JButton("Confirm");
		btnConfirm.addActionListener(this);

		buttonPane.add(Box.createHorizontalGlue());
		buttonPane.add(btnCancel);
		buttonPane.add(Box.createRigidArea(new Dimension(20, 0)));
		buttonPane.add(btnConfirm);
		buttonPane.add(Box.createHorizontalGlue());

		mainPane.add(buttonPane);
		mainPane.add(Box.createRigidArea(new Dimension(0, 10)));

		this.setSize(800, 500);
		this.setLocation(200, 10);
		this.add(mainPane, BorderLayout.CENTER);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	public void loadDataToTable() {
		tmInvoice.setRowCount(0);
		totalAmount = 0;
		if (invoice != null) {
			for (DetailedInvoiceMaterial m : invoice.getMaterials()) {
				int rowTotal = m.getPrice() * m.getQuantity();
				totalAmount += rowTotal;
				tmInvoice.addRow(new Object[] {
						m.getMaterial().getId(), m.getMaterial().getName(), "Material", "", m.getPrice(),
						m.getQuantity(), rowTotal, "Edit", "Delete"
				});
			}
			for (DetailedInvoiceService s : invoice.getServices()) {
				int rowTotal = s.getPrice();
				totalAmount += rowTotal;
				tmInvoice.addRow(new Object[] {
						s.getService().getId(), s.getService().getName(), "Service", s.getServiceStaff().getName(),
						s.getPrice(), 1, rowTotal, "Edit", "Delete"
				});
			}
		}
		if (lblTotal != null) {
			lblTotal.setText("Total: " + totalAmount + " VND");
		}
	}

	private int getMaterialIndex(int rowIndex) {
		int index = -1;
		int count = 0;
		for (int i = 0; i < tmInvoice.getRowCount(); i++) {
			if ("Material".equals(tmInvoice.getValueAt(i, 2))) {
				if (i == rowIndex)
					return count;
				count++;
			}
		}
		return index;
	}

	private int getServiceIndex(int rowIndex) {
		int index = -1;
		int count = 0;
		for (int i = 0; i < tmInvoice.getRowCount(); i++) {
			if ("Service".equals(tmInvoice.getValueAt(i, 2))) {
				if (i == rowIndex)
					return count;
				count++;
			}
		}
		return index;
	}

	public void updateInvoice(DetailedInvoice invoice) {
		this.invoice = invoice;
		loadDataToTable();
	}

	public DetailedInvoice getInvoice() {
		return invoice;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ((e.getSource() instanceof JButton) && (((JButton) e.getSource()).equals(btnCancel))) {
			(new SlotSelectionFrm(user)).setVisible(true);
			this.dispose();
		} else if ((e.getSource() instanceof JButton) && (((JButton) e.getSource()).equals(btnAdd))) {
			(new AddDetailedInvoiceItemFrm(user, this, invoice)).setVisible(true);
		} else if ((e.getSource() instanceof JButton) && (((JButton) e.getSource()).equals(btnConfirm))) {
			if (DetailedInvoiceDAO.saveDetailedInvoice(invoice)) {
				JOptionPane.showMessageDialog(this, "Invoice processed successfully!");
				(new ReceptionistHomeFrm(user)).setVisible(true);
				this.dispose();
			} else {
				JOptionPane.showMessageDialog(this, "Failed to process invoice!");
			}
		}
	}
}

package view.receptionist;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

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

import model.User;
import model.CustomerSlot;
import dao.CustomerSlotDAO;
import view.user.ReceptionistHomeFrm;

public class SlotSelectionFrm extends JFrame implements ActionListener {
	private JButton btnBack, btnNext;
	private JTable tblSlot;
	private User user;
	private DefaultTableModel tmSlot;
	private ArrayList<CustomerSlot> listSlot;
	private CustomerSlot selectedSlot;

	public SlotSelectionFrm(User user) {
		super("Select Customer Slot");
		this.user = user;

		JPanel listPane = new JPanel();
		listPane.setLayout(new BoxLayout(listPane, BoxLayout.PAGE_AXIS));

		JLabel lblTitle = new JLabel("Select customer slot");
		lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblTitle.setFont(lblTitle.getFont().deriveFont(20.0f));
		listPane.add(Box.createRigidArea(new Dimension(0, 10)));
		listPane.add(lblTitle);
		listPane.add(Box.createRigidArea(new Dimension(0, 20)));

		// Table to display slots
		String[] columnNames = {"Slot ID", "Customer name", "Customer phone number"};
		tmSlot = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tblSlot = new JTable(tmSlot);
		tblSlot.setFillsViewportHeight(true);
		tblSlot.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		
		// Load data from DB
		listSlot = CustomerSlotDAO.getCustomerSlots();
		for (CustomerSlot cs : listSlot) {
			tmSlot.addRow(new Object[]{cs.getSlot().getId(), cs.getCustomer().getName(), cs.getCustomer().getPhone()});
		}

		// Add Mouse Listener to highlight/select a row and store selectedSlot
		tblSlot.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int row = tblSlot.getSelectedRow();
				if (row >= 0) {
					selectedSlot = listSlot.get(row);
				}
			}
		});

		JScrollPane scrollPane = new JScrollPane(tblSlot);
		listPane.add(scrollPane);
		listPane.add(Box.createRigidArea(new Dimension(0, 20)));

		// Buttons
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		btnBack = new JButton("Back");
		btnBack.addActionListener(this);
		btnNext = new JButton("Next");
		btnNext.addActionListener(this);
		
		buttonPane.add(Box.createHorizontalGlue());
		buttonPane.add(btnBack);
		buttonPane.add(Box.createRigidArea(new Dimension(20, 0)));
		buttonPane.add(btnNext);
		buttonPane.add(Box.createHorizontalGlue());

		listPane.add(buttonPane);
		listPane.add(Box.createRigidArea(new Dimension(0, 10)));

		this.setSize(600, 400);
		this.setLocation(200, 10);
		this.add(listPane, BorderLayout.CENTER);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ((e.getSource() instanceof JButton) && (((JButton) e.getSource()).equals(btnBack))) {
			(new ReceptionistHomeFrm(user)).setVisible(true);
			this.dispose();
		} else if ((e.getSource() instanceof JButton) && (((JButton) e.getSource()).equals(btnNext))) {
			if (selectedSlot == null) {
				JOptionPane.showMessageDialog(this, "Please select a customer slot first!");
				return;
			}
			(new DetailedInvoiceFrm(user, selectedSlot)).setVisible(true);
			this.dispose();
		}
	}
}

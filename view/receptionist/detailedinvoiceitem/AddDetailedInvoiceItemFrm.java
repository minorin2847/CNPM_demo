package view.receptionist.detailedinvoiceitem;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import dao.MaterialDAO;
import dao.ServiceDAO;
import model.DetailedInvoice;
import model.DetailedInvoiceMaterial;
import model.DetailedInvoiceService;
import model.Material;
import model.Service;
import model.User;
import view.receptionist.DetailedInvoiceFrm;

public class AddDetailedInvoiceItemFrm extends JFrame implements ActionListener {
	private User user;
	private DetailedInvoiceFrm parentFrm;
	private DetailedInvoice invoice;
	private JTextField txtKeyword;
	private JComboBox<String> cbType;
	private JButton btnSearch, btnCancel, btnNext;
	private JTable tblResult;
	private DefaultTableModel tmResult;
	
	private ArrayList<Material> listMaterial;
	private ArrayList<Service> listService;
	private Object selectedItem;

	public AddDetailedInvoiceItemFrm(User user, DetailedInvoiceFrm parentFrm, DetailedInvoice invoice) {
		super("Add new materials/services");
		this.user = user;
		this.parentFrm = parentFrm;
		this.invoice = invoice;

		JPanel mainPane = new JPanel();
		mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.PAGE_AXIS));

		JLabel lblTitle = new JLabel("Add new materials/services");
		lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblTitle.setFont(lblTitle.getFont().deriveFont(20.0f));
		mainPane.add(Box.createRigidArea(new Dimension(0, 10)));
		mainPane.add(lblTitle);
		mainPane.add(Box.createRigidArea(new Dimension(0, 10)));

		// Search pane
		JPanel searchPane = new JPanel(new FlowLayout());
		txtKeyword = new JTextField(15);
		searchPane.add(txtKeyword);
		searchPane.add(new JLabel("Type: "));
		cbType = new JComboBox<String>(new String[]{"Material", "Service"});
		searchPane.add(cbType);
		btnSearch = new JButton("Search");
		btnSearch.addActionListener(this);
		searchPane.add(btnSearch);
		mainPane.add(searchPane);

		// Result table
		String[] columnNames = {"ID", "Name", "Type", "Unit price"};
		tmResult = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tblResult = new JTable(tmResult);
		tblResult.setFillsViewportHeight(true);
		tblResult.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

		tblResult.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int row = tblResult.getSelectedRow();
				if (row >= 0) {
					String type = cbType.getSelectedItem().toString();
					if ("Material".equals(type) && listMaterial != null) {
						selectedItem = listMaterial.get(row);
					} else if ("Service".equals(type) && listService != null) {
						selectedItem = listService.get(row);
					}
				}
			}
		});

		JScrollPane scrollPane = new JScrollPane(tblResult);
		mainPane.add(scrollPane);

		// Buttons
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(this);
		btnNext = new JButton("Next");
		btnNext.addActionListener(this);
		
		buttonPane.add(Box.createHorizontalGlue());
		buttonPane.add(btnCancel);
		buttonPane.add(Box.createRigidArea(new Dimension(20, 0)));
		buttonPane.add(btnNext);
		buttonPane.add(Box.createHorizontalGlue());

		mainPane.add(Box.createRigidArea(new Dimension(0, 10)));
		mainPane.add(buttonPane);
		mainPane.add(Box.createRigidArea(new Dimension(0, 10)));

		this.setSize(600, 400);
		this.setLocation(200, 10);
		this.add(mainPane, BorderLayout.CENTER);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ((e.getSource() instanceof JButton) && (((JButton) e.getSource()).equals(btnSearch))) {
			String keyword = txtKeyword.getText().trim();
			String type = cbType.getSelectedItem().toString();
			tmResult.setRowCount(0);
			selectedItem = null;

			if ("Material".equals(type)) {
				listMaterial = MaterialDAO.searchMaterial(keyword);
				for (Material m : listMaterial) {
					tmResult.addRow(new Object[]{m.getId(), m.getName(), "Material", m.getUnitPrice()});
				}
			} else {
				listService = ServiceDAO.searchService(keyword);
				for (Service s : listService) {
					tmResult.addRow(new Object[]{s.getId(), s.getName(), "Service", s.getUnitPrice()});
				}
			}
		} else if ((e.getSource() instanceof JButton) && (((JButton) e.getSource()).equals(btnCancel))) {
			this.dispose();
		} else if ((e.getSource() instanceof JButton) && (((JButton) e.getSource()).equals(btnNext))) {
			if (selectedItem == null) {
				JOptionPane.showMessageDialog(this, "Please select an item first!");
				return;
			}
			
			String type = cbType.getSelectedItem().toString();
			if ("Material".equals(type)) {
				DetailedInvoiceMaterial dim = new DetailedInvoiceMaterial();
				dim.setMaterial((Material) selectedItem);
				dim.setPrice(((Material) selectedItem).getUnitPrice());
				dim.setQuantity(1);
				(new EditDetailedInvoiceMaterialFrm(user, dim, parentFrm)).setVisible(true);
			} else {
				DetailedInvoiceService dis = new DetailedInvoiceService();
				dis.setService((Service) selectedItem);
				dis.setPrice(((Service) selectedItem).getUnitPrice());
				(new EditDetailedInvoiceServiceFrm(user, dis, parentFrm)).setVisible(true);
			}
			this.dispose();
		}
	}
}

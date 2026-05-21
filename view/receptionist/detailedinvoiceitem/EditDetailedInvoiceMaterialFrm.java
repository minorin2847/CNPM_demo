package view.receptionist.detailedinvoiceitem;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.DetailedInvoiceMaterial;
import model.User;
import view.receptionist.DetailedInvoiceFrm;

public class EditDetailedInvoiceMaterialFrm extends JFrame implements ActionListener {
	private User user;
	private DetailedInvoiceMaterial dim;
	private DetailedInvoiceFrm parentFrm;
	private JTextField txtId, txtName, txtType, txtPrice, txtQuantity, txtTotal;
	private JButton btnCancel, btnSave;
	private boolean isNew;

	public EditDetailedInvoiceMaterialFrm(User user, DetailedInvoiceMaterial dim, DetailedInvoiceFrm parentFrm) {
		super("Edit invoice item");
		this.user = user;
		this.dim = dim;
		this.parentFrm = parentFrm;
		// Need to get materials from parentFrm invoice
		this.isNew = !parentFrm.getInvoice().getMaterials().contains(dim);

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
		txtId = new JTextField(String.valueOf(dim.getMaterial().getId()));
		txtId.setEditable(false);
		formPane.add(txtId);

		formPane.add(new JLabel("Name:"));
		txtName = new JTextField(dim.getMaterial().getName());
		txtName.setEditable(false);
		formPane.add(txtName);

		formPane.add(new JLabel("Type:"));
		txtType = new JTextField("Material");
		txtType.setEditable(false);
		formPane.add(txtType);

		formPane.add(new JLabel("Price:"));
		txtPrice = new JTextField(String.valueOf(dim.getPrice()));
		formPane.add(txtPrice);

		formPane.add(new JLabel("Quantity:"));
		txtQuantity = new JTextField(String.valueOf(dim.getQuantity()));
		formPane.add(txtQuantity);

		formPane.add(new JLabel("Total:"));
		txtTotal = new JTextField(String.valueOf(dim.getPrice() * dim.getQuantity()));
		txtTotal.setEditable(false);
		formPane.add(txtTotal);

		// Key listener to auto-update total
		KeyAdapter updateKeyAdapter = new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				updateTotal();
			}
		};
		txtPrice.addKeyListener(updateKeyAdapter);
		txtQuantity.addKeyListener(updateKeyAdapter);

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

	private void updateTotal() {
		try {
			int price = Integer.parseInt(txtPrice.getText());
			int qty = Integer.parseInt(txtQuantity.getText());
			txtTotal.setText(String.valueOf(price * qty));
		} catch (NumberFormatException ex) {
			txtTotal.setText("0");
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ((e.getSource() instanceof JButton) && (((JButton) e.getSource()).equals(btnCancel))) {
			this.dispose();
		} else if ((e.getSource() instanceof JButton) && (((JButton) e.getSource()).equals(btnSave))) {
			try {
				int price = Integer.parseInt(txtPrice.getText());
				int qty = Integer.parseInt(txtQuantity.getText());
				dim.setPrice(price);
				dim.setQuantity(qty);
				
				if (isNew) {
					parentFrm.getInvoice().getMaterials().add(dim);
				}
				parentFrm.loadDataToTable();
				this.dispose();
			} catch (NumberFormatException ex) {
				// Handle invalid input
			}
		}
	}
}

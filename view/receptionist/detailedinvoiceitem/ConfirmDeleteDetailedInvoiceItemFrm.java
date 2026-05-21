package view.receptionist.detailedinvoiceitem;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.DetailedInvoiceMaterial;
import model.DetailedInvoiceService;
import view.receptionist.DetailedInvoiceFrm;

public class ConfirmDeleteDetailedInvoiceItemFrm extends JFrame implements ActionListener {
	private DetailedInvoiceFrm parentFrm;
	private Object itemToDelete;
	private JButton btnOk, btnCancel;

	public ConfirmDeleteDetailedInvoiceItemFrm(DetailedInvoiceFrm parentFrm, String itemName, Object itemToDelete) {
		super("Confirm delete");
		this.parentFrm = parentFrm;
		this.itemToDelete = itemToDelete;

		JPanel mainPane = new JPanel();
		mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.PAGE_AXIS));

		JLabel lblMessage = new JLabel("<html>Are you sure you want to remove <font color='red'>" + itemName + "</font>?</html>");
		lblMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblMessage.setFont(lblMessage.getFont().deriveFont(16.0f));
		mainPane.add(Box.createRigidArea(new Dimension(0, 20)));
		mainPane.add(lblMessage);
		mainPane.add(Box.createRigidArea(new Dimension(0, 20)));

		// Buttons
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		btnOk = new JButton("OK");
		btnOk.addActionListener(this);
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(this);
		
		buttonPane.add(Box.createHorizontalGlue());
		buttonPane.add(btnOk);
		buttonPane.add(Box.createRigidArea(new Dimension(20, 0)));
		buttonPane.add(btnCancel);
		buttonPane.add(Box.createHorizontalGlue());

		mainPane.add(buttonPane);
		mainPane.add(Box.createRigidArea(new Dimension(0, 20)));

		this.setSize(350, 150);
		this.setLocation(300, 150);
		this.add(mainPane, BorderLayout.CENTER);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ((e.getSource() instanceof JButton) && (((JButton) e.getSource()).equals(btnCancel))) {
			this.dispose();
		} else if ((e.getSource() instanceof JButton) && (((JButton) e.getSource()).equals(btnOk))) {
			if (itemToDelete instanceof DetailedInvoiceMaterial) {
				parentFrm.getInvoice().getMaterials().remove(itemToDelete);
			} else if (itemToDelete instanceof DetailedInvoiceService) {
				parentFrm.getInvoice().getServices().remove(itemToDelete);
			}
			parentFrm.loadDataToTable();
			this.dispose();
		}
	}
}

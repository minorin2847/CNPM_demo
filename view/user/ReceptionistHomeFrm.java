package view.user;

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
import model.User;
import view.receptionist.SlotSelectionFrm;

public class ReceptionistHomeFrm extends JFrame implements ActionListener {
	private JButton btnProcessPayment;
	private User user;

	public ReceptionistHomeFrm(User user) {
		super("Receptionist home");
		this.user = user;

		JPanel listPane = new JPanel();
		listPane.setLayout(new BoxLayout(listPane, BoxLayout.PAGE_AXIS));

		JPanel lblPane = new JPanel();
		lblPane.setLayout(new BoxLayout(lblPane, BoxLayout.LINE_AXIS));
		lblPane.add(Box.createRigidArea(new Dimension(350, 0)));
		JLabel lblUser = new JLabel("Logged in as: " + user.getFullName());
		lblUser.setAlignmentX(Component.RIGHT_ALIGNMENT);
		lblPane.add(lblUser);
		listPane.add(lblPane);
		listPane.add(Box.createRigidArea(new Dimension(0, 20)));

		JLabel lblHome = new JLabel("Receptionist's home");
		lblHome.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblHome.setFont(lblHome.getFont().deriveFont(28.0f));
		listPane.add(lblHome);
		listPane.add(Box.createRigidArea(new Dimension(0, 20)));

		btnProcessPayment = new JButton("Process payment");
		btnProcessPayment.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnProcessPayment.addActionListener(this);
		listPane.add(btnProcessPayment);
		listPane.add(Box.createRigidArea(new Dimension(0, 10)));

		this.setSize(600, 300);
		this.setLocation(200, 10);
		this.add(listPane, BorderLayout.CENTER);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ((e.getSource() instanceof JButton) && (((JButton) e.getSource()).equals(btnProcessPayment))) {
			(new SlotSelectionFrm(user)).setVisible(true);
			this.dispose();
		}
	}
}

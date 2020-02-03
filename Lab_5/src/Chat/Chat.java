package Chat;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Scrollbar;


public class Chat extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JButton buttonPosalji;
	private JTextArea textArea;
	JButton buttonPostavke;
	
	private Socket clientSocket; 
	private PrintWriter pw; 
	private BufferedReader br;
	
	private static final Logger log = LoggerFactory.getLogger(Chat.class);
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Chat frame = new Chat();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private void connect(){ 
		try {			
			
			clientSocket = new Socket(UserConfig.getHost(), UserConfig.getPort());
			
			pw = new PrintWriter(clientSocket.getOutputStream());
			br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			String response; 
			try {
				
			response = br.readLine(); 
			if (textArea.getText().length()>0)
				textArea.append("\n");
			textArea.append(response); 
			textField.setText(null);
			} catch (IOException e) 
			{ 
				
				log.error("Greška kod èitanja inicijalnog odgovora", e);
				JOptionPane.showMessageDialog(textArea, "Greška kod èitanja inicijalnog odgovora", "Greška!", JOptionPane.ERROR_MESSAGE); 
				}
		} catch (UnknownHostException e)
		{ 
			
			log.error("Nepoznati host", e); 
			this.dispose();
		} catch (IOException e)
		{ 
			
			log.error("IO iznimka", e); 
			this.dispose();
		}
			
		//lab7
		textArea.setText(Baza.showPoruke());
		
}
	
	private void send(){ 
		pw.println(textField.getText()); 
		if (pw.checkError()) {
		JOptionPane.showMessageDialog(textArea, "Greška kod slanja poruke",
		"Greška!", JOptionPane.ERROR_MESSAGE); 
		}
		String response;
		
		try {
		response = br.readLine(); 
		if (textArea.getText().length()>0) 
			textArea.append("\n");
		textArea.append(response);
		textField.setText(null);
		} catch (IOException e) { 
			//...=>
			log.error("Greška kod èitanja", e); 
			JOptionPane.showMessageDialog(textArea, "Greška kod èitanja odgovora",
		"Greška!", JOptionPane.ERROR_MESSAGE);
			}
		
		log.info("Data Sent");
		
		}

	/**
	 * Create the frame.
	 */
	public Chat() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		SpringLayout sl_contentPane = new SpringLayout();
		contentPane.setLayout(sl_contentPane);
				
		buttonPosalji = new JButton("Pošalji");
		sl_contentPane.putConstraint(SpringLayout.SOUTH, buttonPosalji, 0, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, buttonPosalji, -10, SpringLayout.EAST, contentPane);
		buttonPosalji.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {	
				String text  = textField.getText();
				textArea.setText(textArea.getText().trim()+"\n"+UserConfig.getKorisnik()+": "+text);
				textField.setText("");
				
				send();
				//lab7
				Baza.insertMessage(UserConfig.getKorisnik(),text);
			}
		});		
			
			
		contentPane.add(buttonPosalji);
		
		
		textArea = new JTextArea();
		sl_contentPane.putConstraint(SpringLayout.NORTH, textArea, 10, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, textArea, 10, SpringLayout.WEST, contentPane);
		textArea.setEditable(false);
		contentPane.add(textArea);
		
		
		buttonPostavke = new JButton("Postavke");
		sl_contentPane.putConstraint(SpringLayout.SOUTH, textArea, -30, SpringLayout.NORTH, buttonPostavke);
		sl_contentPane.putConstraint(SpringLayout.EAST, textArea, 34, SpringLayout.EAST, buttonPostavke);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, buttonPostavke, 0, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, buttonPostavke, -10, SpringLayout.WEST, buttonPosalji);
		
		buttonPostavke.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Postavke dialog = new Postavke();
				dialog.setVisible(true);
				UserConfig.loadParams();
				
			}
		});
		contentPane.add(buttonPostavke);
		
		textField = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.SOUTH, textField, 0, SpringLayout.SOUTH, contentPane);		
		sl_contentPane.putConstraint(SpringLayout.EAST, textField, -10, SpringLayout.WEST, buttonPostavke);
		sl_contentPane.putConstraint(SpringLayout.WEST, textField, 0, SpringLayout.WEST, contentPane);
		contentPane.add(textField);
		textField.setColumns(10);
		connect();
	}
}

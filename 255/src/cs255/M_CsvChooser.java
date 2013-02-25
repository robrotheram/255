import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JFileChooser;

public class test extends JFrame {
  
	private JTextField m_delimiterFeild, m_FilePath;
	private JButton m_ButtonAppy, m_ButtonCancel,m_ButtonBrowse;
	private JPanel m_BrowsePanel, m_ButtonPannel, m_ChoosePannel;
	private EventHandler handle;
	private JLabel m_Description;
	private JTextArea m_SampleData;
	
	
	public test (){
		init();
	}
	
	public void init(){
		handle = new EventHandler();
		this.setBounds(400,400,400,600);
		this.setBackground(Color.BLUE);
		this.setLayout(new BorderLayout());
		
		m_BrowsePanel = new JPanel();
		m_BrowsePanel.setBounds(0,0,400,120);
		m_BrowsePanel.setLayout(new FlowLayout());
		
		m_ChoosePannel = new JPanel();
		m_ChoosePannel.setBounds(0,0,400,120);
		m_ChoosePannel.setLayout(new FlowLayout());
		
		m_ButtonPannel = new JPanel();
		m_ButtonPannel.setBounds(0,0,400,120);
		m_ButtonPannel.setLayout(new FlowLayout());
		
		
		m_Description = new JLabel();
		m_Description.setText("Choose Delimiter:");	
		
		m_delimiterFeild = new JTextField();
		m_delimiterFeild.setColumns(10);
		m_delimiterFeild.setText(",");
		
		m_ChoosePannel.add(m_Description);
		m_ChoosePannel.add(m_delimiterFeild);
		
		
		
		m_FilePath = new JTextField();
		m_FilePath.setColumns(20);
		
		m_ButtonBrowse = new JButton("Browser for csv file");
		m_ButtonBrowse.addActionListener(handle);
		
		m_ButtonAppy = new JButton("Apply");
		m_ButtonAppy.addActionListener(handle);
		
		m_ButtonCancel = new JButton("Cancel");
		m_ButtonCancel.addActionListener(handle);
		
		m_BrowsePanel.add(m_FilePath);
		m_BrowsePanel.add(m_ButtonBrowse);
		
		
		
		this.add(m_BrowsePanel, BorderLayout.NORTH);
		this.add(m_ChoosePannel, BorderLayout.CENTER);
		
		
		//this.add(m_delimiterFeild);
		
		//this.pack();
	}
	
	private class EventHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			if(event.getSource() == m_ButtonBrowse){
				System.out.println("it has been preesd");
				m_FilePath.setText((getFile()));
			}
			
		}
		
	}
	
	  private String getFile(){
	        File f =null;
	        JFileChooser fileChooser = new JFileChooser();
	        int returnValue = fileChooser.showOpenDialog(null);
	        if (returnValue == JFileChooser.APPROVE_OPTION) {
	          f = fileChooser.getSelectedFile();
	          System.out.println(f.getName());
	        }
	        return f.getAbsolutePath();
	  }
	
	public static void main(String[] args){
		test t = new test();
		t.setVisible(true);
		
	}

}

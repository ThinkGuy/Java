import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

/**
 * 文件排序系统。
 * 
 * @author 
 *
 */
public class FileUtils {

	/**
	 * 用于存储分类后的文件。 
	 * key:后缀名， value:StringBuilder存储对应的文件。
	 */
	private HashMap<String, StringBuilder> resultMap = new HashMap<String, StringBuilder>();
	
	/**
	 * 监听文件目录。
	 * 
	 * @param dir 目录。
	 * @throws IllegalAccessException 访问非法异常。
	 */
	public void listenDirectory(File dir) throws IllegalAccessException {
		if (!dir.exists()) {
			throw new IllegalAccessException("目录" + dir + "不存在。");
		}

		if (!dir.isDirectory()) {
			throw new IllegalArgumentException(dir + "不是目录");
		}
		
		String[] fileNames = dir.list();
		resultMap.put("all", new StringBuilder());    //默认所有文件。
		resultMap.put("folder", new StringBuilder()); //文件夹形式。
		
		//后缀。
		String suffix;
		for (String fileName : fileNames) {
			resultMap.get("all").append(fileName + "\n");
			if (fileName.indexOf(".") > 0) {
				suffix = fileName.substring(fileName.indexOf("."), fileName.length());
				
				if (!resultMap.containsKey(suffix)) {
					StringBuilder stringBuilder = new StringBuilder();
					stringBuilder.append(fileName + "\n");
					resultMap.put(suffix, stringBuilder);
				} else {
					resultMap.get(suffix).append(fileName + "\n");
				}
			} else {
				resultMap.get("folder").append(fileName + "\n");
			}
		}
		
		buildGUI();
	}
	
	/**
	 * 搭建GUI。
	 */
	public void buildGUI() {
		final JTextArea fileList = new JTextArea();
		fileList.setText(resultMap.get("all").toString());
		String[] likes = new String[resultMap.keySet().size()];
		resultMap.keySet().toArray(likes);
		final JComboBox combox = new JComboBox(likes);
		
		JFrame frm = new JFrame();
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(6, 10, 10, 10));
		contentPane.setLayout(new BorderLayout(5, 5));
		contentPane.add(combox, BorderLayout.NORTH);
		
		JPanel pane = new JPanel();
		pane.setLayout(new BorderLayout(8, 8));

		JLabel label = new JLabel("File lists");
		label.setFont(new Font("Serif", Font.PLAIN, 16));

		fileList.setForeground(new Color(140, 171, 226));
		fileList.setBackground(new Color(0, 0, 0));
		fileList.setSelectedTextColor(new Color(87, 49, 134));

		JScrollPane scrollPane = new JScrollPane(fileList);
		scrollPane.setColumnHeaderView(label);
		
		pane.add(scrollPane, BorderLayout.CENTER);
		contentPane.add(pane, BorderLayout.CENTER);
		frm.add(contentPane);
		frm.setBounds(500, 300, 300, 400);
		frm.setVisible(true);
		
		//JComboBox事件监听。
		combox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // 获取组合框的itme
                    String item = (String) combox.getItemAt(combox.getSelectedIndex());
                    fileList.setText(resultMap.get(item).toString());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
	}

	public static void main(String[] args) {
		String path = "D:/";
		try {
			new FileUtils().listenDirectory(new File(path));
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
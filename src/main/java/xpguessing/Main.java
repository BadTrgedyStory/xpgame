package xpguessing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Main {

	private static JTextField inputField;
    private static JTextArea outputArea;
    private static JTextArea people;
    
    public static void main(String[] args) {
    	Server server = new Server();
        //frame
        JFrame frame = new JFrame("XP Gusser");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        //panel
        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel,server);

        // 显示窗口
        frame.setVisible(true);
    }
    
    
    static void placeComponents(JPanel panel, Server server) {
        panel.setLayout(null);
        
        people = new JTextArea();
        people.setBounds(120, 20, 150, 50);
        panel.add(people);

        // 开始游戏按钮
        JButton startButton = new JButton("开始游戏");
        startButton.setBounds(10, 20, 100, 25);
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                server.server();
                StringBuilder player = new StringBuilder();
                server.getPool().forEach(name -> {
                	player.append(server.getUserEmail().get(name));
                	player.append(" ");});
                people.setText("已提交:\n"+ player.toString());
            }
        });
        panel.add(startButton);

        // 抽取按钮和输出窗口
        JButton extractButton = new JButton("抽取");
        extractButton.setBounds(10, 80, 80, 25);
        extractButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                outputArea.setText("抽取到的字符串");
            }
        });
        panel.add(extractButton);

        outputArea = new JTextArea();
        outputArea.setBounds(120, 80, 250, 100);
        panel.add(outputArea);

        // 检查按钮和输入框
        JButton checkButton = new JButton("检查");
        checkButton.setBounds(10, 195, 80, 25);
        checkButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String inputText = inputField.getText();
                // 在这里处理输入框中的内容
            }
        });
        panel.add(checkButton);

        inputField = new JTextField(20);
        inputField.setBounds(100, 195, 165, 25);
        panel.add(inputField);

        // 新游戏按钮
        JButton newGameButton = new JButton("新游戏");
        newGameButton.setBounds(10, 230, 100, 25);
        // 在这里添加新游戏的逻辑
        panel.add(newGameButton);
    }
}

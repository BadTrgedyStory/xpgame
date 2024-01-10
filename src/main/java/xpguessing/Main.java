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
    private static JTextArea resultCheck;
    
    public static void main(String[] args) {
    	Server server = new Server();
        //frame
        JFrame frame = new JFrame("XP Guesser");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 500);

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
        people.setLineWrap(true);
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
                outputArea.setText(server.poll());
            }
        });
        panel.add(extractButton);

        outputArea = new JTextArea();
        outputArea.setLineWrap(true);
        outputArea.setBounds(120, 80, 250, 100);
        panel.add(outputArea);
        
        resultCheck = new JTextArea();
        resultCheck.setLineWrap(true);
        resultCheck.setBounds(10, 265, 250, 100);
	    panel.add(resultCheck);
        
        // 检查按钮和输入框
        JButton checkButton = new JButton("投票");
        checkButton.setBounds(10, 195, 80, 25);
        checkButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String input = inputField.getText();
                if(server.check(input)) {
                	resultCheck.setText("答案正确!");
                }
                else {
                	resultCheck.setText("答案错误\n" + input + "的xp是：" + 
                server.getXp().get(server.getUserName().get(input)));
                }
            }
        });
        panel.add(checkButton);

        inputField = new JTextField(20);
        inputField.setBounds(100, 195, 165, 25);
        panel.add(inputField);
        
        //杀人按钮
        JButton killButton = new JButton("杀人");
        killButton.setBounds(10, 230, 80, 25);
        killButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e) {
        		server.server();
        		StringBuilder result = new StringBuilder();
        		if(server.getDeath().equals("n")) {
        			resultCheck.setText("目前本轮无人伤亡");
        		}
        		else {
        		result.append("昨晚被杀的人是：");
        		result.append(server.getDeath());
        		result.append("\n");
        		result.append("他的xp是：");
        		result.append(server.getXp().get(server.getUserName().get(server.getDeath())));
        		resultCheck.setText(result.toString());
        		server.resetDeath("n");
        		}
        	}
        });
        panel.add(killButton);

        // 新游戏按钮
        JButton newGameButton = new JButton("新游戏");
        newGameButton.setBounds(10, 380, 100, 25);
        newGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	server.Reset();
                people.setText("");
                outputArea.setText("");
                resultCheck.setText("");
                inputField.setText("");
                
            }
        });
        panel.add(newGameButton);
    }
}

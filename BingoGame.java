package AwtSwing;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Random;
import javax.sound.sampled.*;
import javax.swing.*;

public class BingoGame {
	static JPanel panelNorth; // �޴� ȭ��
	static JPanel panelCenter; // ���� ȭ��
	static JLabel labelMessage; // �޴� �޽���
	static JButton[] buttons = new JButton[16]; // ī�� ��ư
	static String[] images = { "image01.png", "image02.png", "image03.png", "image04.png", "image05.png", "image06.png",
			"image07.png", "image08.png", "image01.png", "image02.png", "image03.png", "image04.png", "image05.png",
			"image06.png", "image07.png", "image08.png" }; // ī�� �̹���
	static int openCount = 0; // ī�� ���� Ƚ�� (0~2)
	static int buttonIndexSave1 = 0; // ù��° ���� ī�� �ε��� (0~15)
	static int buttonIndexSave2 = 0; // �ι�° ���� ī�� �ε��� (0~15)
	static Timer timer; // ��� �ð��� Ÿ�̸�
	static int tryCount = 0; // �õ� Ƚ��
	static int successCount = 0; // ������ Ƚ�� (0~8)

	static class MyFrame extends JFrame implements ActionListener {
		public MyFrame(String title) {
			// ���̾ƿ� ����
			super(title);
			this.setLayout(new BorderLayout());
			this.setSize(400, 500);
			this.setVisible(true);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			initUI(this); // UI ����
			mixCard(); // ī�� ������ ���� �Լ�

			this.pack();
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (openCount == 2) {
				return;
			}

			JButton btn = (JButton) e.getSource();
			int index = getButtonIndex(btn);
			btn.setIcon(changeImage(images[index]));

			openCount++;
			if (openCount == 1) {
				buttonIndexSave1 = index; // ù��° ī�� ����
			} else if (openCount == 2) {
				buttonIndexSave2 = index; // �ι�° ī�� ����
				tryCount++;
				labelMessage.setText("Find Same Image! " + "Try " + tryCount);

				boolean isBingo = checkCard(buttonIndexSave1, buttonIndexSave2);
				if (isBingo) {
					playSound("bingo.wav");
					openCount = 0;
					successCount++;
					if (successCount == 8) {
						labelMessage.setText("Game Over! " + "Try " + tryCount);
					}
				} else {
					backToQuestion();
				}
			}
		}

		public int getButtonIndex(JButton btn) {
			int index = 0;
			for (int i = 0; i < 16; i++) {
				if (buttons[i] == btn) {
					index = i;
				}
			}
			return index;
		}

		public void initUI(MyFrame myFrame) {
			panelNorth = new JPanel();
			panelNorth.setPreferredSize(new Dimension(400, 100));
			panelNorth.setBackground(Color.BLACK);
			labelMessage = new JLabel("Find Same Image! " + "Try 0");
			labelMessage.setPreferredSize(new Dimension(400, 100));
			labelMessage.setForeground(Color.WHITE);
			labelMessage.setFont(new Font("HoonWhitecatR", Font.BOLD, 20));
			labelMessage.setHorizontalAlignment(JLabel.CENTER);
			panelNorth.add(labelMessage);
			myFrame.add("North", panelNorth);

			panelCenter = new JPanel();
			panelCenter.setLayout(new GridLayout(4, 4));
			panelCenter.setPreferredSize(new Dimension(400, 400));
			for (int i = 0; i < 16; i++) {
				buttons[i] = new JButton();
				buttons[i].setPreferredSize(new Dimension(100, 100));
				buttons[i].setIcon(changeImage("card.png"));
				buttons[i].addActionListener(myFrame);
				panelCenter.add(buttons[i]);
			}
			myFrame.add("Center", panelCenter);
		}

		public ImageIcon changeImage(String filename) {
			ImageIcon icon = new ImageIcon("./Image/" + filename);
			Image originImage = icon.getImage();
			Image changedImage = originImage.getScaledInstance(80, 80, Image.SCALE_SMOOTH); // ũ�� ����
			ImageIcon iconNew = new ImageIcon(changedImage);
			return iconNew;
		}

		public void mixCard() {
			Random rand = new Random();
			for (int i = 0; i < 1000; i++) {
				int random = rand.nextInt(15) + 1; // 1~15
				String temp = images[0];
				images[0] = images[random];
				images[random] = temp;
			}
		}

		public boolean checkCard(int index1, int index2) {
			if (index1 == index2) {
				return false;
			}
			if (images[index1].equals(images[index2])) {
				return true;
			} else {
				return false;
			}
		}

		public void backToQuestion() {
			timer = new Timer(1000, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("Timer");

					playSound("fail.wav");
					openCount = 0;
					buttons[buttonIndexSave1].setIcon(changeImage("card.png"));
					buttons[buttonIndexSave2].setIcon(changeImage("card.png"));
					timer.stop();
				}
			});
			timer.start();
		}

		public void playSound(String filename) {
			File file = new File("./Sound/" + filename);
			if (file.exists()) {
				try {
					AudioInputStream stream = AudioSystem.getAudioInputStream(file);
					Clip clip = AudioSystem.getClip();
					clip.open(stream);
					clip.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("File Not Found!");
			}
		}
	}

	public static void main(String[] args) {
		new MyFrame("Bingo Game");
	}
}

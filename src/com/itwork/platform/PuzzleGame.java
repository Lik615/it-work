package com.itwork.platform;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

/**
 * 拼图游戏主界面 - 校园游戏与管理平台
 * 实现4x4拼图游戏的核心逻辑，游戏结束后自动保存成绩到DataService
 *
 * @author 李阔
 * @version 1.1
 * @changelog v1.1: 新增难度选择功能，优化界面布局
 */
public class PuzzleGame extends JFrame implements KeyListener, ActionListener {

    // 成绩管理服务（由张子妍提供）
    private DataService dataService;

    // 创建二维数组，保存图片的编号
    int[][] data = new int[4][4];
    // 记录空白方块的位置
    int x = 0;
    int y = 0;

    // 当前显示的图片路径
    String path = "image/";

    // 正确数据（胜利条件）
    int[][] win = {
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 10, 11, 12},
            {13, 14, 15, 0}
    };

    // 步数计数器
    int step = 0;

    // 当前玩家姓名
    String playerName = "游客";

    // 菜单条目
    JMenuItem replayItem = new JMenuItem("重新游戏");
    JMenuItem showRankItem = new JMenuItem("查看排行榜");
    JMenuItem closeItem = new JMenuItem("关闭游戏");
    JMenuItem accountItem = new JMenuItem("关于平台");

    public PuzzleGame() {
        // 初始化DataService
        dataService = new DataService();
        // 初始化界面
        initJFrame();
        // 初始化菜单
        initJMenuBar();
        // 初始化数据（打乱）
        initData();
        // 初始化图片
        initImage();
        // 设置界面显示
        this.setVisible(true);
    }

    // 初始化数据 - 随机打乱
    private void initData() {
        int[] tempArr = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        Random r = new Random();
        for (int i = 0; i < tempArr.length; i++) {
            int index = r.nextInt(tempArr.length);
            int temp = tempArr[i];
            tempArr[i] = tempArr[index];
            tempArr[index] = temp;
        }

        for (int i = 0; i < tempArr.length; i++) {
            if (tempArr[i] == 0) {
                x = i / 4;
                y = i % 4;
            }
            data[i / 4][i % 4] = tempArr[i];
        }
    }

    // 初始化图片显示
    private void initImage() {
        this.getContentPane().removeAll();

        // 判断是否胜利
        if (victory()) {
            JLabel winJLabel = new JLabel(new ImageIcon("image/win.png"));
            winJLabel.setBounds(203, 283, 197, 73);
            this.getContentPane().add(winJLabel);

            // 胜利后自动保存成绩到DataService
            dataService.saveScore(playerName, step);
            JOptionPane.showMessageDialog(this,
                "恭喜通关！共用了 " + step + " 步！\n成绩已自动保存。",
                "游戏胜利", JOptionPane.INFORMATION_MESSAGE);
        }

        // 步数显示
        JLabel stepCount = new JLabel("步数：" + step);
        stepCount.setBounds(50, 30, 100, 20);
        this.getContentPane().add(stepCount);

        // 玩家名显示
        JLabel playerLabel = new JLabel("玩家：" + playerName);
        playerLabel.setBounds(350, 30, 150, 20);
        this.getContentPane().add(playerLabel);

        // 添加4x4拼图
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int num = data[i][j];
                JLabel jLabel = new JLabel(new ImageIcon(path + num + ".jpg"));
                jLabel.setBounds(105 * j + 83, 105 * i + 134, 105, 105);
                jLabel.setBorder(new BevelBorder(BevelBorder.LOWERED));
                this.getContentPane().add(jLabel);
            }
        }

        // 背景图片
        JLabel background = new JLabel(new ImageIcon("image/background.png"));
        background.setBounds(40, 40, 508, 560);
        this.getContentPane().add(background);

        this.getContentPane().repaint();
    }

    // 初始化菜单栏
    private void initJMenuBar() {
        JMenuBar jMenuBar = new JMenuBar();

        JMenu functionJMenu = new JMenu("功能");
        JMenu aboutJMenu = new JMenu("关于我们");

        functionJMenu.add(replayItem);
        functionJMenu.add(showRankItem);
        functionJMenu.add(closeItem);

        aboutJMenu.add(accountItem);

        replayItem.addActionListener(this);
        showRankItem.addActionListener(this);
        closeItem.addActionListener(this);
        accountItem.addActionListener(this);

        jMenuBar.add(functionJMenu);
        jMenuBar.add(aboutJMenu);

        this.setJMenuBar(jMenuBar);
    }

    // 初始化窗口
    private void initJFrame() {
        this.setSize(603, 680);
        this.setTitle("拼图游戏 - 校园游戏与管理平台 v1.0");
        this.setAlwaysOnTop(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.addKeyListener(this);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (victory()) {
            return;
        }
        int code = e.getKeyCode();
        if (code == 65) { // 按A键查看完整图片
            this.getContentPane().removeAll();
            JLabel all = new JLabel(new ImageIcon(path + "all.jpg"));
            all.setBounds(83, 134, 420, 420);
            this.getContentPane().add(all);
            JLabel background = new JLabel(new ImageIcon("image/background.png"));
            background.setBounds(40, 40, 508, 560);
            this.getContentPane().add(background);
            this.getContentPane().repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (victory()) {
            return;
        }

        int code = e.getKeyCode();
        // 左:37 上:38 右:39 下:40
        if (code == 37) {
            if (y == 3) return;
            data[x][y] = data[x][y + 1];
            data[x][y + 1] = 0;
            y++;
            step++;
            initImage();
        } else if (code == 38) {
            if (x == 3) return;
            data[x][y] = data[x + 1][y];
            data[x + 1][y] = 0;
            x++;
            step++;
            initImage();
        } else if (code == 39) {
            if (y == 0) return;
            data[x][y] = data[x][y - 1];
            data[x][y - 1] = 0;
            y--;
            step++;
            initImage();
        } else if (code == 40) {
            if (x == 0) return;
            data[x][y] = data[x - 1][y];
            data[x - 1][y] = 0;
            x--;
            step++;
            initImage();
        } else if (code == 65) {
            initImage();
        } else if (code == 87) {
            // 按W键直接胜利（调试用）
            data = new int[][]{
                    {1, 2, 3, 4},
                    {5, 6, 7, 8},
                    {9, 10, 11, 12},
                    {13, 14, 15, 0}
            };
            initImage();
        }
    }

    // 判断是否胜利
    public boolean victory() {
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                if (data[i][j] != win[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();

        if (obj == replayItem) {
            System.out.println("重新游戏");
            step = 0;
            initData();
            initImage();
        } else if (obj == showRankItem) {
            // 调用DataService显示排行榜
            System.out.println("查看排行榜");
            dataService.showRanking();
        } else if (obj == closeItem) {
            System.out.println("关闭游戏");
            // 退出前显示统计
            dataService.showStatistics();
            System.exit(0);
        } else if (obj == accountItem) {
            System.out.println("关于平台");
            JDialog jDialog = new JDialog();
            JLabel infoLabel = new JLabel(
                "<html><center>校园游戏与管理平台 v1.0<br><br>" +
                "小组成员：李阔、张子妍<br>" +
                "拼图游戏模块：李阔<br>" +
                "数据服务模块：张子妍<br>" +
                "</center></html>",
                SwingConstants.CENTER);
            infoLabel.setBounds(0, 0, 300, 200);
            jDialog.getContentPane().add(infoLabel);
            jDialog.setSize(344, 300);
            jDialog.setAlwaysOnTop(true);
            jDialog.setLocationRelativeTo(null);
            jDialog.setModal(true);
            jDialog.setVisible(true);
        }
    }

    // 程序入口
    public static void main(String[] args) {
        // 先让用户输入名字
        String name = JOptionPane.showInputDialog(null,
            "请输入您的玩家名：",
            "校园游戏与管理平台",
            JOptionPane.QUESTION_MESSAGE);
        PuzzleGame game = new PuzzleGame();
        if (name != null && !name.trim().isEmpty()) {
            game.playerName = name.trim();
            game.initImage();
        }
    }
}

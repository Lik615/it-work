package likuo;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

/**
 * 拼图游戏主界面
 * 实现4x4拼图游戏的核心逻辑，包括图片切割、随机打乱、键盘移动控制、
 * 步数统计、胜利判断等功能
 *
 * @author 李阔
 */
public class PuzzleGame extends JFrame implements KeyListener, ActionListener {
    // 创建二维数组，保存图片的编号
    int[][] data = new int[4][4];
    // 记录空白方块的位置
    int x = 0;
    int y = 0;

    // 定义一个变量，记录当前显示的图片路径
    String path = "image/";

    // 定义一个二维数组，存储正确数据（胜利条件）
    int[][] win = {
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 10, 11, 12},
            {13, 14, 15, 0}
    };

    // 定义变量，用来计量步数
    int step = 0;

    // 菜单条目对象
    JMenuItem replayItem = new JMenuItem("重新游戏");
    JMenuItem reLoginItem = new JMenuItem("重新登录");
    JMenuItem closeItem = new JMenuItem("关闭游戏");
    JMenuItem accountItem = new JMenuItem("公众号");

    public PuzzleGame() {
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

    //初始化数据
    private void initData() {
        //定义一个数组
        int []tempArr = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
        //打乱数据顺序
        Random r = new Random();
        for (int i = 0; i < tempArr.length; i++) {
            int index = r.nextInt(tempArr.length);
            int temp = tempArr[i];
            tempArr[i] = tempArr[index];
            tempArr[index] = temp;
        }

        for (int i = 0; i < tempArr.length; i++) {
            if (tempArr[i] == 0){
                x = i / 4;
                y = i % 4;
            }
            data[i / 4][i % 4] = tempArr[i];
        }
    }

    //初始化图片
    //添加图片时按照二维数组的顺序添加图片
    private void initImage() {
        //清空界面
        this.getContentPane().removeAll();

        if(victory()){
            //添加胜利图标
            JLabel winJLable = new JLabel(new ImageIcon("image/win.png"));
            winJLable.setBounds(203, 283, 197, 73);
            this.getContentPane().add(winJLable);
        }

        JLabel stepCount = new JLabel("步数：" + step);
        stepCount.setBounds(50, 30, 100, 20);
        this.getContentPane().add(stepCount);

        //添加4行
        for (int i = 0; i < 4; i++) {
            //在一行添加4张
            for (int j = 0; j < 4; j++) {
                //获取当前要加载的序号
                int num = data[i][j];
                //创建JLabel（管理容器）
                JLabel jLabel = new JLabel(new ImageIcon(path + num + ".jpg"));
                //指定位置
                jLabel.setBounds(105 * j + 83, 105 * i + 134, 105, 105);
                //给图片添加边框
                jLabel.setBorder(new BevelBorder(BevelBorder.LOWERED));
                //添加图片到容器
                this.getContentPane().add(jLabel);
            }
        }

        //添加背景图片
        JLabel background = new JLabel(new ImageIcon("image/background.png"));
        background.setBounds(40, 40, 508, 560);
        //把背景图片添加到界面当中
        this.getContentPane().add(background);

        //刷新界面
        this.getContentPane().repaint();
    }

    //初始化菜单
    private void initJMenuBar() {
        //创建菜单对象
        JMenuBar jMenuBar = new JMenuBar();

        //创建菜单的两个选项
        JMenu functionJMenu = new JMenu("功能");
        JMenu aboutJMenu = new JMenu("关于我们");

        //将每一个条目添加到选项
        functionJMenu.add(replayItem);
        functionJMenu.add(reLoginItem);
        functionJMenu.add(closeItem);

        aboutJMenu.add(accountItem);

        //给条目绑定事件
        replayItem.addActionListener(this);
        reLoginItem.addActionListener(this);
        closeItem.addActionListener(this);
        accountItem.addActionListener(this);

        //将菜单里面的两个选项加到菜单当中
        jMenuBar.add(functionJMenu);
        jMenuBar.add(aboutJMenu);

        //将整个菜单添加到界面
        this.setJMenuBar(jMenuBar);
    }

    //初始化界面
    private void initJFrame() {
        //设置界面宽高
        this.setSize(603, 680);
        //设置界面标题
        this.setTitle("拼图游戏单机版 v1.0");
        //设置置顶
        this.setAlwaysOnTop(true);
        //设置居中
        this.setLocationRelativeTo(null);
        //设置关闭
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //取消默认居中
        this.setLayout(null);
        //给整个界面添加键盘监听事件
        this.addKeyListener(this);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
    //按下不松时会调用这个方法
    @Override
    public void keyPressed(KeyEvent e) {
        //判断游戏是否胜利
        if(victory()){
            return;
        }
        int code = e.getKeyCode();
        if (code == 65){
            //把界面中所有的图片都删除
            this.getContentPane().removeAll();
            //加载第一张完整的图片
            JLabel all = new JLabel(new ImageIcon(path + "all.jpg"));
            all.setBounds(83, 134, 420, 420);
            this.getContentPane().add(all);
            //添加背景图片
            JLabel background = new JLabel(new ImageIcon("image/background.png"));
            background.setBounds(40, 40, 508, 560);
            //把背景图片添加到界面当中
            this.getContentPane().add(background);

            //刷新界面
            this.getContentPane().repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //判断游戏是否胜利
        if(victory()){
            return;
        }

        //对上下左右的判断
        //左37右39上38下40
        int code = e.getKeyCode();
        if (code == 37){
            //处理最左边无法左移的报错
            if (y == 3){
                return;
            }
            System.out.println("向左移动");
            data[x][y] = data[x][y+1];
            data[x][y+1] = 0;
            y++;
            //每移动一次，计数器自增一次。
            step++;

            initImage();
        } else if (code == 38) {
            System.out.println("向上移动");
            //处理最下方无法上移的报错
            if (x == 3){
                return;
            }
            data[x][y] = data[x+1][y];
            data[x+1][y] = 0;
            x++;
            //每移动一次，计数器自增一次。
            step++;
            initImage();
        } else if (code == 39) {
            //处理最右边无法右移的报错
            if (y == 0){
                return;
            }
            System.out.println("向右移动");
            data[x][y] = data[x][y-1];
            data[x][y-1] = 0;
            y--;
            //每移动一次，计数器自增一次。
            step++;
            initImage();
        }else if(code == 40){
            //处理最上方无法下移的报错
            if (x == 0){
                return;
            }
            System.out.println("向下移动");
            data[x][y] = data[x-1][y];
            data[x-1][y] = 0;
            x--;
            //每移动一次，计数器自增一次。
            step++;
            initImage();
        }else if(code == 65){
            initImage();
        }else if(code == 87){
            data = new int[][]{
                    {1,2,3,4},
                    {5,6,7,8},
                    {9,10,11,12},
                    {13,14,15,0}
            };
            initImage();
        }
    }

    //判断data数组是否与win数组一致
    //如果相同返回true 否则返回false
    public boolean victory(){
        for(int i = 0; i < data.length; i++){
            //i:依次表示data的索引
            //data[i]:表示data的元素
            for (int j = 0; j < data[i].length; j++){
                if (data[i][j] != win[i][j]){
                    //只要有一个不一样则返回false
                    return false;
                }
            }
        }
        //循环结束表示数组遍历完毕，全部一样返回true
        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();

        //判断
        if (obj == replayItem){
            System.out.println("重新游戏");
            //计数器清零
            step = 0;
            //再次打乱二维数组
            initData();
            //重新加载图片
            initImage();
        }else if(obj == reLoginItem){
            System.out.println("重新登录");
            //关闭当前界面
            this.setVisible(false);
            // 打开登录界面
            new PuzzleGame();
        }else if(obj == closeItem){
            System.out.println("关闭游戏");
            System.exit(0);
        }else if(obj == accountItem){
            System.out.println("公众号");

            //创建一个弹窗对象
            JDialog jDialog = new JDialog();
            //创建一个管理图片对象
            JLabel jLabel = new JLabel(new ImageIcon("image/about.png"));
            jLabel.setBounds(0, 0, 258, 258);
            //把图片添加到弹窗
            jDialog.getContentPane().add(jLabel);
            //给弹窗设置大小
            jDialog.setSize(344,344);
            //让弹窗置顶
            jDialog.setAlwaysOnTop(true);
            //让弹窗居中
            jDialog.setLocationRelativeTo(null);
            //弹框不关闭无法操作下面的界面
            jDialog.setModal(true);
            //让弹窗显示出来
            jDialog.setVisible(true);

        }
    }

    /**
     * 程序入口
     */
    public static void main(String[] args) {
        new PuzzleGame();
    }
}

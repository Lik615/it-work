package com.itwork.platform;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * 数据服务模块 - 校园游戏与管理平台
 * 提供游戏成绩的保存、排行榜显示、数据统计等功能
 * 被PuzzleGame.java调用，实现游戏数据持久化管理
 *
 * @author 张子妍
 * @version 1.0
 */
public class DataService {

    /**
     * 成绩记录内部类
     * 存储每次游戏的玩家名、步数和完成时间
     */
    public static class ScoreRecord {
        private String playerName;
        private int steps;
        private String gameTime;

        public ScoreRecord(String playerName, int steps) {
            this.playerName = playerName;
            this.steps = steps;
            // 记录当前时间
            this.gameTime = java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }

        public String getPlayerName() {
            return playerName;
        }

        public int getSteps() {
            return steps;
        }

        public String getGameTime() {
            return gameTime;
        }

        @Override
        public String toString() {
            return String.format("玩家: %-10s | 步数: %-4d | 时间: %s",
                playerName, steps, gameTime);
        }
    }

    // 所有游戏成绩记录
    private ArrayList<ScoreRecord> scoreRecords;

    // 统计信息
    private int totalGames;
    private int bestScore;
    private String bestPlayer;

    public DataService() {
        scoreRecords = new ArrayList<>();
        totalGames = 0;
        bestScore = Integer.MAX_VALUE;
        bestPlayer = "暂无";
    }

    /**
     * 保存游戏成绩
     * @param playerName 玩家姓名
     * @param steps 通关步数
     */
    public void saveScore(String playerName, int steps) {
        ScoreRecord record = new ScoreRecord(playerName, steps);
        scoreRecords.add(record);
        totalGames++;

        // 更新最佳成绩
        if (steps < bestScore) {
            bestScore = steps;
            bestPlayer = playerName;
        }

        System.out.println("[DataService] 成绩已保存: " + record);
    }

    /**
     * 获取所有成绩记录
     * @return 成绩记录列表
     */
    public ArrayList<ScoreRecord> getAllScores() {
        return new ArrayList<>(scoreRecords);
    }

    /**
     * 按步数升序排序的成绩记录
     * @return 排序后的成绩列表
     */
    public ArrayList<ScoreRecord> getSortedScores() {
        ArrayList<ScoreRecord> sorted = new ArrayList<>(scoreRecords);
        Collections.sort(sorted, new Comparator<ScoreRecord>() {
            @Override
            public int compare(ScoreRecord s1, ScoreRecord s2) {
                return Integer.compare(s1.getSteps(), s2.getSteps());
            }
        });
        return sorted;
    }

    /**
     * 显示排行榜（弹出对话框）
     */
    public void showRanking() {
        if (scoreRecords.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                "暂无游戏记录！\n快去玩一局拼图游戏吧！",
                "排行榜",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        ArrayList<ScoreRecord> sorted = getSortedScores();
        StringBuilder sb = new StringBuilder();
        sb.append("========== 拼图游戏排行榜 ==========\n");
        sb.append(String.format("%-4s %-10s %-6s %s\n", "排名", "玩家", "步数", "完成时间"));
        sb.append("------------------------------------\n");

        int rank = 1;
        for (ScoreRecord record : sorted) {
            sb.append(String.format("%-4d %s\n", rank, record));
            rank++;
            if (rank > 10) break; // 只显示前十名
        }

        sb.append("------------------------------------\n");
        sb.append(String.format("总游戏次数: %d 次\n", totalGames));
        sb.append(String.format("最佳成绩: %d 步 (玩家: %s)\n", bestScore, bestPlayer));

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new java.awt.Dimension(450, 300));

        JOptionPane.showMessageDialog(null,
            scrollPane,
            "游戏排行榜",
            JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * 显示统计信息
     */
    public void showStatistics() {
        if (scoreRecords.isEmpty()) {
            System.out.println("[DataService] 暂无游戏数据");
            return;
        }

        ArrayList<ScoreRecord> sorted = getSortedScores();

        System.out.println("\n========== 游戏数据统计 ==========");
        System.out.println("总游戏次数: " + totalGames);
        System.out.println("最佳成绩: " + bestScore + " 步 (玩家: " + bestPlayer + ")");

        // 计算平均步数
        int totalSteps = 0;
        for (ScoreRecord record : scoreRecords) {
            totalSteps += record.getSteps();
        }
        double avgSteps = (double) totalSteps / scoreRecords.size();
        System.out.printf("平均步数: %.1f 步\n", avgSteps);

        // 显示最近5局
        System.out.println("\n--- 最近5局记录 ---");
        int start = Math.max(0, scoreRecords.size() - 5);
        for (int i = start; i < scoreRecords.size(); i++) {
            System.out.println(scoreRecords.get(i));
        }

        // 显示TOP3
        System.out.println("\n--- TOP 3 ---");
        for (int i = 0; i < Math.min(3, sorted.size()); i++) {
            System.out.println("第" + (i + 1) + "名: " + sorted.get(i));
        }
        System.out.println("==================================\n");
    }

    /**
     * 获取指定玩家的游戏历史
     * @param playerName 玩家姓名
     * @return 该玩家的成绩列表
     */
    public ArrayList<ScoreRecord> getPlayerHistory(String playerName) {
        ArrayList<ScoreRecord> result = new ArrayList<>();
        for (ScoreRecord record : scoreRecords) {
            if (record.getPlayerName().equals(playerName)) {
                result.add(record);
            }
        }
        return result;
    }

    /**
     * 获取指定玩家的平均步数
     * @param playerName 玩家姓名
     * @return 平均步数，如无记录返回-1
     */
    public double getPlayerAverageSteps(String playerName) {
        ArrayList<ScoreRecord> history = getPlayerHistory(playerName);
        if (history.isEmpty()) {
            return -1;
        }
        int total = 0;
        for (ScoreRecord record : history) {
            total += record.getSteps();
        }
        return (double) total / history.size();
    }

    /**
     * 获取总游戏次数
     * @return 总次数
     */
    public int getTotalGames() {
        return totalGames;
    }

    /**
     * 获取最佳成绩步数
     * @return 最佳步数
     */
    public int getBestScore() {
        return bestScore == Integer.MAX_VALUE ? -1 : bestScore;
    }

    /**
     * 获取最佳玩家名
     * @return 玩家名
     */
    public String getBestPlayer() {
        return bestPlayer;
    }
}

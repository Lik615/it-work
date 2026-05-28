# 校园游戏与管理平台

## 项目简介
本项目是实验3"项目质量计划与基于Git的配置管理"的代码仓库，实现一个统一的Java桌面应用。

项目包含两个协作模块，由小组成员分工开发：
- **拼图游戏模块**（李阔）：基于Java Swing的4x4拼图游戏GUI
- **数据服务模块**（张子妍）：游戏成绩保存、排行榜、数据统计

两个模块位于同一包 `com.itwork.platform` 下，PuzzleGame 在游戏胜利时自动调用 DataService 保存成绩。

## 小组成员
- 李阔（PuzzleGame.java - 304行）
- 张子妍（DataService.java - 246行）

## 项目结构
```
it-work/
├── src/
│   └── com/
│       └── itwork/
│           └── platform/
│               ├── PuzzleGame.java   # 拼图游戏主界面（李阔）
│               └── DataService.java  # 数据服务模块（张子妍）
└── README.md
```

## 代码命名规则
- 包名：全小写，使用域名倒序（com.itwork.platform）
- 类名：PascalCase（大驼峰命名法，如 PuzzleGame）
- 方法名：camelCase（小驼峰命名法，如 saveScore）
- 常量：UPPER_SNAKE_CASE（全大写下划线分隔）
- 变量名：camelCase（小驼峰命名法）

## 分支说明
- `main`：主分支，存放稳定版本代码
- `likuo`：李阔的开发分支
- `zhangziyan`：张子妍的开发分支

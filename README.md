# Exam Father 考试软件

## 简介

本软件是拿来练手的手机软件，参考[驾考宝典的模仿软件](https://github.com/MRwangqi/Driving-test)，使用 Kotlin 语言改写的。放弃了显示图片的功能，功能很不完善。

小飞鱼(xfy)公司之无聊(wl)办公室开发。[下载地址1](https://gitee.com/bubifengyun/ExamFather)，[下载地址2](https://github.com/bubifengyun/ExamFather)

## 代码结构

## 关于数据库的制作

之前学习网站开发，用惯了XAMPP，数据库也都是 mariadb 数据库。暂时不知道怎么在安卓手机里调用该数据库，只好改用 sqlite3 了。数据库转换软件为 [mysql2sqlite](https://github.com/dumblob/mysql2sqlite)，完成后放于*app/src/main/assets/exams.db*。由于本人技术太菜，在测试时，把*exams.db*放在手机内部共享存储空间的根目录下。

### 关于答案的制作

对于填空题、简单题和综述题，答案放在数据库表 `tb_exams` 的 `ans_area` 里。对于判断题、单选题和多选题，答案放在数据库表 `tb_exams` 的 `ans_chars` 里。判断题用大写字母`V`和`X`表示对错。单选和多选使用`ABCD`四个字母表示答案。但是在程序执行过程中，为了偷懒，判断题、单选题和多选题的答案变成了数字。对于判断题对与错直接对应`0`和`1`，单选题`ABCD`直接对应`1234`，对于多选题采用二进制，改变方法如下，

<table border="1" cellpadding="1" cellspacing="1">
	<tbody>
		<tr>
			<td>答案情况</td>
			<td>包含D</td>
			<td>包含C</td>
			<td>包含B</td>
			<td>包含A</td>
			<td>数字形式</td>
		</tr>
		<tr>
			<td>A</td>
			<td>0</td>
			<td>0</td>
			<td>0</td>
			<td>1</td>
			<td>1</td>
		</tr>
		<tr>
			<td>B</td>
			<td>0</td>
			<td>0</td>
			<td>1</td>
			<td>0</td>
			<td>2</td>
		</tr>
		<tr>
			<td>CD</td>
			<td>1</td>
			<td>1</td>
			<td>0</td>
			<td>0</td>
			<td>12</td>
		</tr>
		<tr>
			<td>ABCD</td>
			<td>1</td>
			<td>1</td>
			<td>1</td>
			<td>1</td>
			<td>15</td>
		</tr>
	</tbody>
</table>

## 许可证

支持国产，就用木兰2许可证了。部分代码版权归属[原作者](https://github.com/MRwangqi)。在此表示感谢。

## 下一步打算

没啥好打算的。

package javademo;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/*从excel表格获取点位名称*/
public class OpenExcel {

	static GetPath gp;
	private static String path = gp.getAppPath(OpenExcel.class);
	static String sFilePath = path + "/点位.xls";// excel表格路径
	static List<String> project = new ArrayList<>();// excel表格中里工作簿存放的顺序

	// 存储点位信息对象
	public static class Data {
		private String Name;// 点位名称
		private String Ip;// ip
		private String Area;// 乡镇

		public Data(String name, String ip, String area) {
			this.Name = name;
			this.Ip = ip;
			this.Area = area;
		}

		public String getName() {
			return Name;
		}

		public String getIp() {
			return Ip;
		}

		public String getArea() {
			return Area;
		}

	}

	public static void GetExcelNum(String str) throws BiffException, IOException {
		// 1、构造excel文件输入流对象
		InputStream is = new FileInputStream(str);
		// 2、声明工作簿对象
		Workbook rwb = Workbook.getWorkbook(is);
		// 3、获得工作簿的个数,对应于一个excel中的工作表个数
		int num = rwb.getNumberOfSheets();
		for (int i = 0; i < num; i++) {
			Sheet oFirstSheet = rwb.getSheet(i);// 使用索引形式获取工作表
			if (oFirstSheet.getRows() >= 1) {
				project.add(oFirstSheet.getName());
			}
		}
		// System.out.println(project.size());
	}

	public static List<Data> OpenExcel(String name, String str) throws BiffException, IOException {
		// 1、构造excel文件输入流对象
		InputStream is = new FileInputStream(str);
		// 2、声明工作簿对象
		Workbook rwb = Workbook.getWorkbook(is);
		// 3、获得工作簿的个数,对应于一个excel中的工作表个数
		rwb.getNumberOfSheets();

		Sheet oFirstSheet = rwb.getSheet(name);// 使用索引形式获取第一个工作表，也可以使用rwb.getSheet(sheetName);其中sheetName表示的是工作表的名称
		// System.out.println("工作表名称：" + oFirstSheet.getName());
		int rows = oFirstSheet.getRows();// 获取工作表中的总行数
		int columns = oFirstSheet.getColumns();// 获取工作表中的总列数
		List<Data> list = new ArrayList();
		for (int i = 1; i < rows; i++) {
			list.add(new Data(oFirstSheet.getCell(0, i).getContents(), oFirstSheet.getCell(1, i).getContents(),
					oFirstSheet.getCell(2, i).getContents()));
			// oFirstSheet.getCell(0,i);//需要注意的是这里的getCell方法的参数，第一个是指定第几列，第二个参数才是指定第几行
		}
		return (list);
	}

	public static void main(String[] args) throws IOException, RowsExceededException, WriteException, BiffException {

		try {
			project.clear();// 每次线程运行时先清空list
			GetExcelNum(sFilePath);// 筛选，把有效表格放入list
			for (int i = 0; i < project.size(); i++) {
				List<Data> list = OpenExcel(project.get(i), sFilePath);
				for (int j = 0; j < list.size(); j++) {
					Data data = (Data) list.get(j);
					System.out.print(data.getName() + "     ");
					System.out.print(data.getIp() + "     ");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static List<Data> GetCameraIpAndName()
			throws IOException, RowsExceededException, WriteException, BiffException {
		try {
			project.clear();// 每次线程运行时先清空list
			GetExcelNum(sFilePath);// 筛选，把有效表格放入list
			List<Data> list = OpenExcel(project.get(0), sFilePath);
			return (list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
}

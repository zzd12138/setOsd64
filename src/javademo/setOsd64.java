package javademo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

import javademo.OpenExcel.Data;
import jxl.read.biff.BiffException;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class setOsd64 {
	private static Logger logger = Logger.getLogger(setOsd64.class);
	// private static HCNetSDK.NET_DVR_DEVICEINFO_V30 m_strDeviceInfo;// 设备信息
	private static HCNetSDK.NET_DVR_NETCFG_V30 m_strNetConfig;// 网络参数
	static HCNetSDK hCNetSDK = HCNetSDK.INSTANCE;
	private static HCNetSDK.NET_DVR_PICCFG_V40 m_struPicCfg;// 图片参数
	private static HCNetSDK.NET_DVR_SHOWSTRING_V30 m_strShowString;// osd参数
	private static String cameraName = "";
	static GetPath gp;
	private static String path = gp.getAppPath(setOsd64.class);
	private static short port = 8000;// 端口
	private static int user;
	private static HCNetSDK sdk;
	private static HCNetSDK.NET_DVR_DEVICEINFO m_strDeviceInfo;// 设备信息
	private static String area;// 乡镇

	public static final int ISAPI_DATA_LEN = 1024 * 1024;
	public static final int ISAPI_STATUS_LEN = 4 * 4096;
	public static final int BYTE_ARRAY_LEN = 1024;

	private static String strURL;// 透传链接
	private static String strInbuffer;// 透传xml

	public static void getPicturedisposition(List<Data> cameraList, List<String> password)
			throws UnsupportedEncodingException {
		// 初始化设备
		System.out.println("开始运行2");
		sdk = HCNetSDK.INSTANCE;
		System.out.println(sdk);
		// 是否初始化成功
		System.out.println("开始运行3");
		if (!sdk.NET_DVR_Init()) {
			System.out.println("初始化失败");
			logger.info("初始化失败");
			return;
		}
		// 启用SDK写日志
		hCNetSDK.NET_DVR_SetLogToFile(3, "..\\sdkLog", false);
		System.out.println("初始化成功");
		// 注册设备
		int user = -1;
		System.out.println("注册成功");
		// 设备信息
		m_strDeviceInfo = new HCNetSDK.NET_DVR_DEVICEINFO();
		System.out.println("返回编号成功");
		// 返回一个用户编号

		// m_strShowString = getCameraOsd((String) template.get(0), (String)
		// template.get(1));// 获取osd模板
		// m_struPicCfg = getCameraPicSetting((String) template.get(0), (String)
		// template.get(1));// 获取通道图片配置模板

		for (int i = 0; i < cameraList.size(); ++i) {
			Data data = (Data) cameraList.get(i);
			String ip = data.getIp();
			cameraName = data.getName();
			area = data.getArea();
			user = -1;
			int j = 0;

			while (user < 0 & j < password.size()) {
				user = sdk.NET_DVR_Login(ip, port, "admin", (String) password.get(j), m_strDeviceInfo);
				// user = Login_V40(ip, (short) 8000, "admin", (String) password.get(j));
				j++;
			}
			if (user < 0) {
				logger.info(ip + "登录失败");
				continue;
			}
			logger.info(ip + "登录成功");

			// setOsd.cleanCameraNameOsd(user);
			// setCameraName(user, 1, cameraName, ip, m_struPicCfg);
			// setCameraOsd(user, "洪合", ip, m_strShowString);
			// setGbOsd(user, "高照", ip);
			setCameraName(user, cameraName, ip);

			/* 设置为国标模式 */
			XmlStr str = XmlFiles.setGB();
			strURL = str.strURL;
			strInbuffer = str.strInbuffer;
			TouChuan(ip, user, strURL, strInbuffer);

			/* 国标模式下OSD设置 */
			str = XmlFiles.gbOsd(area, cameraName);
			strURL = str.strURL;
			strInbuffer = str.strInbuffer;
			TouChuan(ip, user, strURL, strInbuffer);

			// System.out.println(ip+" "+"通道名称："+ChannelName+" mac地址："+sPhyAddress);
			// System.out.println(ip+" "+"通道名称："+ChannelName);
			// logger.info(ip+" "+"通道名称："+ChannelName);
		}
	}
	/*
	 * // 注销设备 sdk.NET_DVR_Logout(user); // 释放SDK sdk.NET_DVR_Cleanup(); }
	 */

	public static List<String> txt2String(File file) {// 读取txt文件

		StringBuilder result = new StringBuilder();
		List<String> ips = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));// 构造一个BufferedReader类来读取文件
			String s = null;
			while ((s = br.readLine()) != null) {// 使用readLine方法，一次读一行
				ips.add(s);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ips;
	}

	/*************************************************
	 * 函数描述: 设置通道名称
	 *************************************************/
	public static void setCameraName(int lUserID, String cameraName, String ip) {

		HCNetSDK.NET_DVR_PICCFG_V30 m_struPicCfg = new HCNetSDK.NET_DVR_PICCFG_V30();
		// IntByReference ibrBytesReturned1 = new IntByReference(0);// 获取图片参数

		m_struPicCfg = new HCNetSDK.NET_DVR_PICCFG_V30();
		IntByReference ibrBytesReturned1 = new IntByReference(0);// 获取图片参数 m_struPicCfg.write();
		Pointer lpPicConfig = m_struPicCfg.getPointer();
		boolean getDVRConfigSuc2 = hCNetSDK.NET_DVR_GetDVRConfig(lUserID, 1002, 1, lpPicConfig, m_struPicCfg.size(),
				ibrBytesReturned1);
		m_struPicCfg.read();
		if (getDVRConfigSuc2) {
			logger.info(ip + "获取通道信息成功");
		}

		else {
			logger.info(ip + "获取通道信息失败");
		}

		m_struPicCfg.read(); //
		m_struPicCfg.dwSize = m_struPicCfg.size();

		m_struPicCfg.read();
		try {
			cameraName = cameraName + "\0";
			m_struPicCfg.sChanName = cameraName.getBytes("GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		m_struPicCfg.write();
		Pointer lpPicConfig2 = m_struPicCfg.getPointer();
		boolean setDVRConfigSuc = hCNetSDK.NET_DVR_SetDVRConfig(lUserID, 1003, 1, lpPicConfig2, m_struPicCfg.size());
		// m_struPicCfg.read();
		if (!setDVRConfigSuc) {
			logger.info(ip + "保存通道名称失败");
			logger.info(ip + "错误代码" + hCNetSDK.NET_DVR_GetLastError());
		} else {
			logger.info(ip + "保存通道名称成功");
		}

	}

	/*************************************************
	 * 函数描述: 设置通道名称(复制模板信息)
	 *************************************************/
	public static void setCameraName(int lUserID, int showName, String cameraName, String ip,
			HCNetSDK.NET_DVR_PICCFG_V40 m_struPicCfg) {

		m_struPicCfg.read();
		try {
			cameraName = cameraName + "\0";
			m_struPicCfg.sChanName = cameraName.getBytes("GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		m_struPicCfg.write();
		Pointer lpPicConfig = m_struPicCfg.getPointer();
		boolean setDVRConfigSuc = hCNetSDK.NET_DVR_SetDVRConfig(lUserID, HCNetSDK.NET_DVR_SET_PICCFG_V40, 1,
				lpPicConfig, m_struPicCfg.size());
		// m_struPicCfg.read();
		if (!setDVRConfigSuc) {
			logger.info(ip + "保存通道名称失败");
			logger.info(ip + "错误代码" + hCNetSDK.NET_DVR_GetLastError());
		} else {
			logger.info(ip + "保存通道名称成功");
		}

	}

	/*************************************************
	 * 函数描述: 设置osd叠加(复制模板信息)
	 *************************************************/
	public static void setCameraOsd(int lUserID, String area, String ip,
			HCNetSDK.NET_DVR_SHOWSTRING_V30 m_strShowString) {
		String[] osdName = { "浙江", "嘉兴", "秀洲", area };
		for (int i = 0; i < osdName.length; i++) {
			m_strShowString.struStringInfo[i].wShowString = (short) 1;// 是否显示叠加 0：不显示 1：显示
			try {
				m_strShowString.struStringInfo[i].sString = osdName[i].getBytes("GBK");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		m_strShowString.write();

		Pointer lpShowString = m_strShowString.getPointer();
		m_strShowString.read();
		boolean setDVRConfigSuc = hCNetSDK.NET_DVR_SetDVRConfig(lUserID, 1031, 1, lpShowString, m_strShowString.size());
		// m_strShowString.read();
		if (setDVRConfigSuc == false) {
			logger.info(ip + "设置osd失败");
			logger.info(ip + "错误代码" + hCNetSDK.NET_DVR_GetLastError());
		} else {
			logger.info(ip + "设置osd成功");
		}
		// }

	}

	/*************************************************
	 * 函数描述: 国标osd叠加
	 *************************************************/
	public static void setGbOsd(int lUserID, String area, String ip) {

		// 获取设备osd信息
		System.out.println(ip + "开始国标osd设置");
		m_strShowString = new HCNetSDK.NET_DVR_SHOWSTRING_V30();
		IntByReference ibrBytesReturned = new IntByReference(0);// 获取显示字符参数
		m_strShowString.write();
		Pointer lpStringConfig = m_strShowString.getPointer();
		boolean getDVRConfigSuc = hCNetSDK.NET_DVR_GetDVRConfig(lUserID, HCNetSDK.NET_DVR_GET_SHOWSTRING_V30, 1,
				lpStringConfig, m_strShowString.size(), ibrBytesReturned);
		m_strShowString.read();
		if (getDVRConfigSuc != true) {
			logger.info(ip + "获取osd失败");
			logger.info(ip + "错误代码" + hCNetSDK.NET_DVR_GetLastError());
		} else {
			logger.info(ip + "获取osd成功");
		}

		// 设置设备osd信息
		String test = "测试";
		String[] osdName = { "浙江", "嘉兴", "秀洲", area };
		for (int i = 0; i < osdName.length; i++) {
			m_strShowString.struStringInfo[i].wShowString = (short) 1;// 是否显示叠加 0：不显示 1：显示
			try {
				m_strShowString.struStringInfo[i].sString = osdName[i].getBytes("GBK");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			m_strShowString.struStringInfo[i].wStringSize = (short) m_strShowString.struStringInfo[i].sString.length;
		}

		try {
			m_strShowString.struStringInfo[4].wShowString = (short) 1;
			m_strShowString.struStringInfo[4].sString = test.getBytes("GBK");
			m_strShowString.struStringInfo[4].wStringSize = (short) m_strShowString.struStringInfo[4].sString.length;
		} catch (UnsupportedEncodingException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}

		m_strShowString.dwSize = m_strShowString.size();
		m_strShowString.write();

		Pointer lpShowString = m_strShowString.getPointer();
		m_strShowString.read();
		boolean setDVRConfigSuc = hCNetSDK.NET_DVR_SetDVRConfig(lUserID, 1031, 1, lpShowString, m_strShowString.size());
		// m_strShowString.read();
		if (setDVRConfigSuc == false) {
			logger.info(ip + "设置osd失败");
			logger.info(ip + "错误代码" + hCNetSDK.NET_DVR_GetLastError());
		} else {
			logger.info(ip + "设置osd成功");
		}
		// }

	}

	/*************************************************
	 * 函数描述: 获取模板osd信息
	 *************************************************/
	public static HCNetSDK.NET_DVR_SHOWSTRING_V30 getCameraOsd(String ip, String password) {
		logger.info(ip + "开始获取模板osd信息");
		user = -1;
		m_strDeviceInfo = new HCNetSDK.NET_DVR_DEVICEINFO();
		user = sdk.NET_DVR_Login(ip, port, "admin", password, m_strDeviceInfo);
		if (user < 0) {
			logger.info(ip + "登录失败");
		} else {
			logger.info(ip + "登录成功");
		}

		// 获取设备osd信息
		m_strShowString = new HCNetSDK.NET_DVR_SHOWSTRING_V30();
		IntByReference ibrBytesReturned = new IntByReference(0);// 获取显示字符参数
		m_strShowString.write();
		Pointer lpStringConfig = m_strShowString.getPointer();
		boolean getDVRConfigSuc = hCNetSDK.NET_DVR_GetDVRConfig(user, HCNetSDK.NET_DVR_GET_SHOWSTRING_V30, 1,
				lpStringConfig, m_strShowString.size(), ibrBytesReturned);
		m_strShowString.read();
		if (getDVRConfigSuc != true) {
			logger.info(ip + "获取模板osd失败");
			logger.info(ip + "错误代码" + hCNetSDK.NET_DVR_GetLastError());
		} else {
			logger.info(ip + "获取模板osd成功");
		}
		return m_strShowString;
	}

	/*************************************************
	 * 函数描述: 获取模板通道图片配置信息
	 *************************************************/
	public static HCNetSDK.NET_DVR_PICCFG_V40 getCameraPicSetting(String ip, String password) {
		logger.info(ip + "开始获取模板通道图片配置信息");
		user = -1;
		m_strDeviceInfo = new HCNetSDK.NET_DVR_DEVICEINFO();
		user = sdk.NET_DVR_Login(ip, port, "admin", password, m_strDeviceInfo);
		if (user < 0) {
			logger.info(ip + "登录失败");
		} else {
			logger.info(ip + "登录成功");
		}

		// 获取设备通道配置信息
		m_struPicCfg = new HCNetSDK.NET_DVR_PICCFG_V40();
		IntByReference ibrBytesReturned1 = new IntByReference(0);// 获取图片参数 m_struPicCfg.write();
		Pointer lpPicConfig = m_struPicCfg.getPointer();
		boolean getDVRConfigSuc2 = hCNetSDK.NET_DVR_GetDVRConfig(user, HCNetSDK.NET_DVR_GET_PICCFG_V40, 1, lpPicConfig,
				m_struPicCfg.size(), ibrBytesReturned1);
		m_struPicCfg.read();
		if (getDVRConfigSuc2) {
			logger.info(ip + "获取模板通道图片信息成功");
		} else {
			logger.info(ip + "获取模板通道图片信息失败");
		}

		return m_struPicCfg;
	}

	/**
	 * 设备登录V40 与V30功能一致
	 *
	 * @param ip   设备IP
	 * @param port SDK端口，默认设备的8000端口
	 * @param user 设备用户名
	 * @param psw  设备密码
	 */
	public static int Login_V40(String ip, short port, String user, String psw) {
		// 注册
		HCNetSDK.NET_DVR_USER_LOGIN_INFO m_strLoginInfo = new HCNetSDK.NET_DVR_USER_LOGIN_INFO();// 设备登录信息
		HCNetSDK.NET_DVR_DEVICEINFO_V40 m_strDeviceInfo = new HCNetSDK.NET_DVR_DEVICEINFO_V40();// 设备信息

		String m_sDeviceIP = ip;// 设备ip地址
		m_strLoginInfo.sDeviceAddress = new byte[HCNetSDK.NET_DVR_DEV_ADDRESS_MAX_LEN];
		System.arraycopy(m_sDeviceIP.getBytes(), 0, m_strLoginInfo.sDeviceAddress, 0, m_sDeviceIP.length());

		String m_sUsername = user;// 设备用户名
		m_strLoginInfo.sUserName = new byte[HCNetSDK.NET_DVR_LOGIN_USERNAME_MAX_LEN];
		System.arraycopy(m_sUsername.getBytes(), 0, m_strLoginInfo.sUserName, 0, m_sUsername.length());

		String m_sPassword = psw;// 设备密码
		m_strLoginInfo.sPassword = new byte[HCNetSDK.NET_DVR_LOGIN_PASSWD_MAX_LEN];
		System.arraycopy(m_sPassword.getBytes(), 0, m_strLoginInfo.sPassword, 0, m_sPassword.length());

		m_strLoginInfo.wPort = port;
		m_strLoginInfo.bUseAsynLogin = false; // 是否异步登录：0- 否，1- 是
//        m_strLoginInfo.byLoginMode=1;  //ISAPI登录
		m_strLoginInfo.write();

		int iUserID = hCNetSDK.NET_DVR_Login_V40(m_strLoginInfo, m_strDeviceInfo);
		if (iUserID == -1) {
			System.out.println("登录失败，错误码为" + hCNetSDK.NET_DVR_GetLastError());
			return iUserID;
		} else {
			System.out.println(ip + ":设备登录成功！");
			return iUserID;
		}
	}

	/*************************************************
	 * 函数描述: 透传国标osd参数
	 * 
	 * @throws UnsupportedEncodingException
	 *************************************************/
	public static void TouChuan(String ip, int lUserID, String strURL, String strInbuffer)
			throws UnsupportedEncodingException {
		HCNetSDK.NET_DVR_XML_CONFIG_INPUT struXMLInput = new HCNetSDK.NET_DVR_XML_CONFIG_INPUT();
		struXMLInput.read();
		struXMLInput.dwSize = struXMLInput.size();
		int iURLlen = strURL.length();
		HCNetSDK.BYTE_ARRAY ptrUrl = new HCNetSDK.BYTE_ARRAY(iURLlen);
		System.arraycopy(strURL.getBytes(), 0, ptrUrl.byValue, 0, strURL.length());
		ptrUrl.write();
		struXMLInput.lpRequestUrl = ptrUrl.getPointer();
		struXMLInput.dwRequestUrlLen = iURLlen;
		byte[] byInContent = strInbuffer.getBytes("UTF-8");
		int iInBufLen = byInContent.length;
		if (iInBufLen == 0) {
			struXMLInput.lpInBuffer = null;
			struXMLInput.dwInBufferSize = 0;
			struXMLInput.write();
		} else {
			HCNetSDK.BYTE_ARRAY ptrInBuffer = new HCNetSDK.BYTE_ARRAY(iInBufLen);
			ptrInBuffer.read();

			ptrInBuffer.byValue = byInContent;

			// ptrInBuffer.byValue = hexStrToByteArray(strInbuffer);

			ptrInBuffer.write();

			struXMLInput.lpInBuffer = ptrInBuffer.getPointer();
			struXMLInput.dwInBufferSize = iInBufLen;
			struXMLInput.write();

		}
		HCNetSDK.BYTE_ARRAY ptrStatusByte = new HCNetSDK.BYTE_ARRAY(ISAPI_STATUS_LEN);
		ptrStatusByte.read();

		HCNetSDK.BYTE_ARRAY ptrOutByte = new HCNetSDK.BYTE_ARRAY(ISAPI_DATA_LEN);
		ptrOutByte.read();

		HCNetSDK.NET_DVR_XML_CONFIG_OUTPUT struXMLOutput = new HCNetSDK.NET_DVR_XML_CONFIG_OUTPUT();
		struXMLOutput.read();
		struXMLOutput.dwSize = struXMLOutput.size();
		struXMLOutput.lpOutBuffer = ptrOutByte.getPointer();
		struXMLOutput.dwOutBufferSize = ptrOutByte.size();
		struXMLOutput.lpStatusBuffer = ptrStatusByte.getPointer();
		struXMLOutput.dwStatusSize = ptrStatusByte.size();
		struXMLOutput.write();

		if (!hCNetSDK.NET_DVR_STDXMLConfig(lUserID, struXMLInput, struXMLOutput)) {
			int iErr = hCNetSDK.NET_DVR_GetLastError();
			logger.info(ip + "透传失败，错误号：" + iErr);
			return;

		} else {
			struXMLOutput.read();
			ptrOutByte.read();
			ptrStatusByte.read();
			String strOutXML = new String(ptrOutByte.byValue).trim();
			System.out.println("返回参数" + strOutXML);
			String strStatus = new String(ptrStatusByte.byValue).trim();
			logger.info(ip + "返回状态" + strStatus);
			logger.info(ip + "透传成功");
		}
	}

	public static void getXml(File file) {
		Document document = null;
		try {
			SAXReader saxReader = new SAXReader();
			document = saxReader.read(file); // 读取XML文件,获得document对象
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		System.out.println(document);
	}

	/*************************************************
	 * 函数描述:txt文件转String
	 *************************************************/
	public static String readFileContent(String fileName) {
		File file = new File(fileName);
		BufferedReader reader = null;
		StringBuffer sbf = new StringBuffer();
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempStr;
			while ((tempStr = reader.readLine()) != null) {
				sbf.append(tempStr + "\n");
			}
			reader.close();
			return sbf.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return sbf.toString();
	}

	public static byte[] hexStrToByteArray(String str) {
		if (str == null) {
			return null;
		}
		if (str.length() == 0) {
			return new byte[0];
		}
		byte[] byteArray = new byte[str.length() / 2];
		for (int i = 0; i < byteArray.length; i++) {
			String subStr = str.substring(2 * i, 2 * i + 2);
			byteArray[i] = ((byte) Integer.parseInt(subStr, 16));
		}
		return byteArray;
	}

	public static void main(String[] args) throws RowsExceededException, WriteException, BiffException, IOException {
		// TODO Auto-generated method stub
		// task to run goes here

		// File file = new File(path + "/模板.txt");// 第一行ip，第二行密码
		File file2 = new File(path + "/password.txt");
		// File file3 = new File(path + "/xml.txt");
		List<Data> cameraIpAndName = OpenExcel.GetCameraIpAndName();
		// List<String> template = txt2String(file);
		List<String> password = txt2String(file2);
		// System.out.print("获取文件成功");

		getPicturedisposition(cameraIpAndName, password);

	}

}

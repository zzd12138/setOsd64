package javademo;

public class XmlFiles {

	/*************************************************
	 * 函数描述: 国标OSD链接和XML字符设置 area：乡镇 cameraName：相机通道名称
	 *************************************************/
	static XmlStr gbOsd(String area, String cameraName) {
		String strURL = "PUT /ISAPI/System/Video/inputs/channels/1/overlays";
		String gbOsdXml = "<VideoOverlay>\r\n" + "    <normalizedScreenSize>\r\n"
				+ "        <normalizedScreenWidth>704</normalizedScreenWidth>\r\n"
				+ "        <normalizedScreenHeight>576</normalizedScreenHeight>\r\n" + "    </normalizedScreenSize>\r\n"
				+ "    <attribute>\r\n" + "        <transparent>false</transparent>\r\n"
				+ "        <flashing>false</flashing>\r\n" + "    </attribute>\r\n"
				+ "    <fontSize>adaptive</fontSize>\r\n" + "    <frontColorMode>auto</frontColorMode>\r\n"
				+ "    <alignment>GB</alignment>\r\n" + "    <boundary>1</boundary>\r\n" + "    <TextOverlayList>\r\n"
				+ "        <TextOverlay>\r\n" + "            <id>1</id>\r\n" + "            <enabled>true</enabled>\r\n"
				+ "            <positionX>0</positionX>\r\n" + "            <positionY>0</positionY>\r\n"
				+ "            <displayText>浙江</displayText>\r\n" + "        </TextOverlay>\r\n"
				+ "        <TextOverlay>\r\n" + "            <id>2</id>\r\n" + "            <enabled>true</enabled>\r\n"
				+ "            <positionX>0</positionX>\r\n" + "            <positionY>0</positionY>\r\n"
				+ "            <displayText>嘉兴</displayText>\r\n" + "        </TextOverlay>\r\n"
				+ "        <TextOverlay>\r\n" + "            <id>3</id>\r\n" + "            <enabled>true</enabled>\r\n"
				+ "            <positionX>0</positionX>\r\n" + "            <positionY>0</positionY>\r\n"
				+ "            <displayText>秀洲</displayText>\r\n" + "        </TextOverlay>\r\n"
				+ "        <TextOverlay>\r\n" + "            <id>4</id>\r\n" + "            <enabled>true</enabled>\r\n"
				+ "            <positionX>0</positionX>\r\n" + "            <positionY>0</positionY>\r\n"
				+ "            <displayText>" + area + "</displayText>\r\n" + "        </TextOverlay>\r\n"
				+ "        <TextOverlay>\r\n" + "            <id>5</id>\r\n" + "            <enabled>true</enabled>\r\n"
				+ "            <positionX>0</positionX>\r\n" + "            <positionY>0</positionY>\r\n"
				+ "            <displayText>" + cameraName + "</displayText>\r\n" + "        </TextOverlay>\r\n"
				+ "        <TextOverlay>\r\n" + "            <id>6</id>\r\n"
				+ "            <enabled>false</enabled>\r\n" + "            <positionX>0</positionX>\r\n"
				+ "            <positionY>0</positionY>\r\n" + "            <displayText />\r\n"
				+ "        </TextOverlay>\r\n" + "        <TextOverlay>\r\n" + "            <id>7</id>\r\n"
				+ "            <enabled>false</enabled>\r\n" + "            <positionX>0</positionX>\r\n"
				+ "            <positionY>0</positionY>\r\n" + "            <displayText />\r\n"
				+ "        </TextOverlay>\r\n" + "        <TextOverlay>\r\n" + "            <id>8</id>\r\n"
				+ "            <enabled>false</enabled>\r\n" + "            <positionX>0</positionX>\r\n"
				+ "            <positionY>0</positionY>\r\n" + "            <displayText />\r\n"
				+ "        </TextOverlay>\r\n" + "    </TextOverlayList>\r\n" + "    <DateTimeOverlay>\r\n"
				+ "        <enabled>true</enabled>\r\n" + "        <positionY>545</positionY>\r\n"
				+ "        <positionX>0</positionX>\r\n" + "        <dateStyle>YYYY-MM-DD</dateStyle>\r\n"
				+ "        <timeStyle>24hour</timeStyle>\r\n" + "        <displayWeek>false</displayWeek>\r\n"
				+ "    </DateTimeOverlay>\r\n" + "    <channelNameOverlay>\r\n" + "        <enabled>false</enabled>\r\n"
				+ "        <positionY>65</positionY>\r\n" + "        <positionX>512</positionX>\r\n"
				+ "    </channelNameOverlay>\r\n" + "</VideoOverlay>";

		XmlStr str = new XmlStr(strURL, gbOsdXml);

		return str;
	}

	/*************************************************
	 * 函数描述: 设置OSD至国标模式
	 *************************************************/
	static XmlStr setGB() {
		String strURL = "PUT /ISAPI/System/Video/inputs/channels/1/overlays";
		String gbOsdXml = "<VideoOverlay><alignment>GB</alignment></VideoOverlay>";
		XmlStr str = new XmlStr(strURL, gbOsdXml);
		return str;
	}
}

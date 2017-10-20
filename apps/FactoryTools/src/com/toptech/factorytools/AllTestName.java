package com.toptech.factorytools;

public class AllTestName {
	public final static String PACKNAME = "com.toptech.factorytools";
	public final static String ClASSNAME = "com.toptech.factorytools.FactoryShow";
	public static final String PREFERENCES_INPUT_SOURCE = "INPUT_SOURCE";
	public static final String PREFERENCES_PREVIOUS_INPUT_SOURCE = "PREVIOUS_INPUT_SOURCE";
	public static final String PREFERENCES_TV_SETTING = "TvSetting";
	public static final String PREFERENCES_IS_AUTOSCAN_LAUNCHED = "autoTuningLaunchedBefore";
	
	public final static int MESSAGE_TEST_VOLTAGE = 0x81;
	public final static int MESSAGE_INPUT_SOURCE_DVBS = 0x828000;  //
	public final static int MESSAGE_INPUT_SOURCE_DTV = 0x828100;  //dvbt
	public final static int MESSAGE_INPUT_SOURCE_ATV = 0x828400;
	public final static int MESSAGE_INPUT_SOURCE_AV1 = 0x828500;
	public final static int MESSAGE_INPUT_SOURCE_AV2 = 0x828600;

	public final static int MESSAGE_INPUT_SOURCE_YUPBR = 0x828900;
	public final static int MESSAGE_INPUT_SOURCE_YUPBR2 = 0x828A00;
	public final static int MESSAGE_INPUT_SOURCE_HDMI1 = 0x828C00;
	public final static int MESSAGE_INPUT_SOURCE_HDMI2 = 0x828D00;

	public final static int MESSAGE_INPUT_SOURCE_HDMI3 = 0x828E00;
	public final static int MESSAGE_INPUT_SOURCE_HDMI4 = 0x828F00;
	public final static int MESSAGE_INPUT_SOURCE_VGA = 0x829000;

	public final static int MESSAGE_INPUT_SOURCE_USB0_1 = 0x829100;
	public final static int MESSAGE_INPUT_SOURCE_USB2 = 0x829200;
	public final static int MESSAGE_INPUT_SOURCE_SCART1 = 0x829300;
	public final static int MESSAGE_INPUT_SOURCE_SCART2 = 0x829400;
	public final static int MESSAGE_INPUT_SOURCE_ATV_CABLE = 0x829500;
	public final static int MESSAGE_INPUT_SOURCE_ATV_AIR = 0x829600;
	
	
	public final static int MESSAGE_KEYPED_TEST = 0x83FF00;
	public final static int MESSAGE_FACTORY_RESTORE = 0x840000;
	public final static int MESSAGE_CI_INFO = 0x850000;

	public final static int MESSAGE_UPDATA_HDCP_KEY = 0x868080;
	public final static int MESSAGE_UPDATA_CI_PLUS_KEY = 0x868180;
	public final static int MESSAGE_UPDATA_MAC_KEY = 0x868280;
	public final static int MESSAGE_UPDATA_MAC_KEY_BY_USB = 0x868380;

	public final static int MESSAGE_ETHERNET_TEST = 0x870200;
	public final static int MESSAGE_WIFI_TEST = 0x880200;
	public final static int MESSAGE_LNB_13V = 0x898000;

	public final static int MESSAGE_LNB_18V = 0x898100;
	public final static int MESSAGE_LNB_OFF = 0x898200;

	public final static int MESSAGE_USB_TEST_USB1 = 0x8A8000;
	public final static int MESSAGE_USB_TEST_USB2 = 0x8A8100;
	public final static int MESSAGE_USB_TEST_USB3 = 0x8A8200;
	public final static int MESSAGE_USB_TEST_USB4 = 0x8A8300;
	
	public final static int MESSAGE_SDcard_TEST = 0x8E0000;
	public final static int MESSAGE_SW_Version = 0x8F0000;

	public final static int MESSAGE_UPGRADE_SW = 0x8B0000; //no function now
	public final static int MESSAGE_IR_TEST = 0x8C0000;

	public final static int MESSAGE_FEEDBACK = 0xFFFFFF;
	
	public static final int CONNECT_INPUT_SOURCE_TYPE = 5;
	
	public static final int TEST_MODE_OTHERS =0;
	public static final int TEST_MODE_MAC =1;
	public static final int TEST_MODE_HDCPKEY=2;
	public static final int TEST_MODE_CI_PLUS_KEY=3;
}

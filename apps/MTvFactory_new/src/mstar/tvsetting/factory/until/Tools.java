package mstar.tvsetting.factory.until;

import java.io.BufferedReader;
import java.io.FileReader;
import android.content.Context;
import android.widget.Toast;

public class Tools {
	static String harwareName = null;

	private static Toast toast;

    public static void showToast(Context context,  String content) {
        if (toast == null) {
            toast = Toast.makeText(context,content,  Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }

	public static String getHarwareName() {
		if (harwareName == null) {
			harwareName = parseHardwareName();
		}
		return harwareName;
	}

	private static String parseHardwareName() {
		String str = null;
		try {
			FileReader reader = new FileReader("/proc/cpuinfo");
			BufferedReader br = new BufferedReader(reader);
			while ((str = br.readLine()) != null) {
				if (str.indexOf("Hardware") >= 0) {
					break;
				}
			}
			if (str != null) {
				String s[] = str.split(":", 2);
				str = s[1].trim().toLowerCase();
			}
			br.close();
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

}

package com.sarxos.gadacz;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;


/**
 * Some useful window toolkit.
 * 
 * @author Bartosz Firyn (SarXos)
 */
public class GadaczToolkit {

	/**
	 * Center window on the screen
	 * 
	 * @param w - window to be centered
	 */
	public static void setCenteredWindow(Window w) {
		Toolkit t = Toolkit.getDefaultToolkit();
		Dimension size = t.getScreenSize();
		w.setLocation(
				new Point(
						size.width / 2 - w.getPreferredSize().width / 2,
						size.height / 2 - w.getPreferredSize().height / 2
				)
		);
	}
}

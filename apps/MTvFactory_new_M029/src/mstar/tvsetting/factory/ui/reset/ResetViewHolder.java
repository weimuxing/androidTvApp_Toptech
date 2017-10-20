package mstar.tvsetting.factory.ui.reset;

import mstar.tvsetting.factory.desk.IFactoryDesk;
import mstar.tvsetting.factory.ui.designmenu.DesignMenuActivity;

public class ResetViewHolder {
	private DesignMenuActivity mResetActivity;
	private IFactoryDesk factoryManager;

	private ResetViewHolder(DesignMenuActivity designMenuActivity1, IFactoryDesk factoryManager) {
		mResetActivity = designMenuActivity1;
		this.factoryManager = factoryManager;
	}
}

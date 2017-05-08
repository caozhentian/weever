package baidumapsdk.demo.search.poi;

import com.baidu.mapapi.search.core.PoiInfo;

public class AddressSelectVM {

	private boolean isSrc    ; 
	
	private PoiInfo mPoiInfo ;
	
	public AddressSelectVM(boolean isSrc, PoiInfo mPoiInfo) {
		this.isSrc = isSrc;
		this.mPoiInfo = mPoiInfo;
	}

	public boolean isSrc() {
		return isSrc;
	}

	public void setSrc(boolean isSrc) {
		this.isSrc = isSrc;
	}

	public PoiInfo getmPoiInfo() {
		return mPoiInfo;
	}

	public void setmPoiInfo(PoiInfo mPoiInfo) {
		this.mPoiInfo = mPoiInfo;
	}
	
	

}

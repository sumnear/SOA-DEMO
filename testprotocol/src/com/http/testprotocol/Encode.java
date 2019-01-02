package com.http.testprotocol;

/**
 * @author sumnear
 *
 */

public enum Encode {
	//值
	GBK((byte)0), UTF8((byte)1);

	private byte value = 1;
	
	Encode(byte value){
		this.value = value;
	}
	
	public byte getValue(){
		return value;
	}
	
}

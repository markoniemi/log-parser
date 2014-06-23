package com.tieto.parser.model;

import lombok.Data;
import lombok.ToString;


/** 
 * Represents clear code of phone call. 
 */
@Data
@ToString
public class ClearCode {
	private int id;
	private int neId;
	private int share;
	/** Type of the clear code. Values below 300h are successful calls. */
	private int type;
	/** Name of the clear code. */
	private String info;
	private int occurrences;
	private int signalling;
	private int ring;
	private int speech;
}

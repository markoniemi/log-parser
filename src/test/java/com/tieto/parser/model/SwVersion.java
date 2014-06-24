package com.tieto.parser.model;

import lombok.Data;

/**
 * Represents a software version of a unit. Does not correspond to any database
 * table. Instead the version is stored in UnitInfo.swVersion field.
 */
@Data
public class SwVersion {
	private String version;
	private String unitName;
	/**
	 * since several units may have a same name, HealthView must assign a
	 * unique name for units
	 */
	private String uniqueUnitName;
	private String name;
	private String status;
	private String info;
}

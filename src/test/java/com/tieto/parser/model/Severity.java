package com.tieto.parser.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Enumeration that contains all possible alarm severity values.
 */
@RequiredArgsConstructor
public enum Severity {
	UNKNOWN(-1, "unknown"), OK(0, "ok"), WARNING(1, "warning"), MINOR(2,
			"minor"), MAJOR(3, "major"), CRITICAL(4, "critical");
	@Getter
	@NonNull
	private final int severity;
	@Getter
	@NonNull
	private final String name;
}

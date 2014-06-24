//package com.tieto.parser;
//
//import java.util.List;
//
//import com.tieto.parser.ParserData;
//import com.tieto.parser.Block;
//
///**
// * Block that reads the MML-command version from output.
// */
//public class VersionRecord extends Block {
//
//	public void parse(ParserData parserData, String input, String clsName)
//			 {
//		if (input == null) {
//			return;
//		}
//		if (this.className != null) {
//			clsName = this.className;
//		}
//		List<String> versionFields = super.splitInput(input);
//		TextParser defaultRecord = null;
//		if (versionFields.size() > 0) {
//			String versionField = versionFields.get(0);
//			boolean recordFound = false;
//			for (Block record : textParsers) {
//				if (record.getVersions() == null) {
//					defaultRecord = record;
//				} else {
//					for (String version : record.getVersions()) {
//						if (versionField != null
//								&& versionField.equals(version)) {
//							recordFound = true;
//							record.parse(parserData, input, clsName);
//						}
//					}
//				}
//			}
//			if (!recordFound && defaultRecord != null) {
//				defaultRecord.parse(parserData, input, clsName);
//			}
//		}
//	}
// }

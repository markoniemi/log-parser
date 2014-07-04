package com.tieto.parser;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import com.tieto.parser.model.Alarm;
import com.tieto.parser.model.CabinetInfo;
import com.tieto.parser.model.ClearCode;
import com.tieto.parser.model.CpuLoad;
import com.tieto.parser.model.LicenseInfo;
import com.tieto.parser.model.Severity;
import com.tieto.parser.model.SwPackage;
import com.tieto.parser.model.SwVersion;
import com.tieto.parser.model.UnitInfo;

public class ParserTest {
    @Test
    public void testUnitInfo() throws Exception {
        ParserManager parserManager = new ParserManager("src/test/resources/config/parsers.xml");
        Parser parser = parserManager.getParser("unitInfoMaster");
        @SuppressWarnings("unchecked")
        List<UnitInfo> list = (List<UnitInfo>) parser
                .parse(readFile("src/test/resources/mml-output/MML-ZWTIU-MSC-4001"));
        Assert.assertNotNull(list);
        Assert.assertEquals(166, list.size());
        for (UnitInfo unitInfo : list) {
            Assert.assertNotNull(unitInfo.getName());
        }
        UnitInfo unitInfo = (UnitInfo) list.get(0);
        Assert.assertEquals("BDCU-0", unitInfo.getName());
        Assert.assertEquals("1A", unitInfo.getCabinet());
        Assert.assertEquals("005-08", unitInfo.getLocation());
    }

    @Test
    public void testMgwUnitInfo() throws Exception {
        ParserManager parserManager = new ParserManager("src/test/resources/config/parsers.xml");
        Parser parser = parserManager.getParser("unitInfoIPA");
        @SuppressWarnings("unchecked")
        List<UnitInfo> list = (List<UnitInfo>) parser
                .parse(readFile("src/test/resources/mml-output/MML-ZWFIPS-MGW-4201"));
        Assert.assertNotNull(list);
        Assert.assertEquals(50, list.size());
        for (UnitInfo unitInfo : list) {
            Assert.assertNotNull(unitInfo.getLocation());
        }
        UnitInfo unitInfo = (UnitInfo) list.get(0);
        Assert.assertEquals("SFU-0", unitInfo.getName());
        Assert.assertEquals("01", unitInfo.getCabinet());
        Assert.assertEquals("1-01", unitInfo.getLocation());
    }

    @Test
    public void testCabinetLayout() throws Exception {
        ParserManager parserManager = new ParserManager("src/test/resources/config/parsers.xml");
        Parser parser = parserManager.getParser("cabinetLayoutMsc");
        @SuppressWarnings("unchecked")
        List<CabinetInfo> list = (List<CabinetInfo>) parser
                .parse(readFile("src/test/resources/mml-output/MML-ZWTIJ-MSC-4001"));
        Assert.assertNotNull(list);
        Assert.assertEquals(10, list.size());
        for (CabinetInfo cabinet : list) {
            Assert.assertNotNull(cabinet.getName());
        }
        CabinetInfo cabinet = (CabinetInfo) list.get(0);
        Assert.assertEquals("1A", cabinet.getName());
        Assert.assertEquals("MOMC", cabinet.getType());
    }

    @Test
    public void testMgwCabinetLayout() throws Exception {
        ParserManager parserManager = new ParserManager("src/test/resources/config/parsers.xml");
        Parser parser = parserManager.getParser("cabinetLayoutIPA");
        @SuppressWarnings("unchecked")
        List<CabinetInfo> list = (List<CabinetInfo>) parser
                .parse(readFile("src/test/resources/mml-output/MML-ZWFIJ-MGW-4201"));
        Assert.assertNotNull(list);
        Assert.assertEquals(1, list.size());
        for (CabinetInfo cabinet : list) {
            Assert.assertNotNull(cabinet.getName());
        }
        CabinetInfo cabinet = (CabinetInfo) list.get(0);
        Assert.assertEquals("01", cabinet.getName());
        Assert.assertEquals("IC186_B", cabinet.getType());
    }

    @Test
    public void testUnitState() throws Exception {
        ParserManager parserManager = new ParserManager("src/test/resources/config/parsers.xml");
        Parser parser = parserManager.getParser("unitStateMsc");
        @SuppressWarnings("unchecked")
        List<UnitInfo> list = (List<UnitInfo>) parser
                .parse(readFile("src/test/resources/mml-output/MML-ZUSI-MSC-4001"));
        Assert.assertNotNull(list);
        Assert.assertEquals(143, list.size());
        for (UnitInfo unitInfo : list) {
            Assert.assertNotNull(unitInfo.getName());
        }
        UnitInfo unitInfo = (UnitInfo) list.get(0);
        Assert.assertEquals("OMU-0", unitInfo.getName());
        Assert.assertEquals("SE-OU", unitInfo.getState());
        Assert.assertEquals("FLTY", unitInfo.getInfo());
    }

    @Test
    public void testUnitStateMgw() throws Exception {
        ParserManager parserManager = new ParserManager("src/test/resources/config/parsers.xml");
        Parser parser = parserManager.getParser("unitStateIPA");
        @SuppressWarnings("unchecked")
        List<UnitInfo> list = (List<UnitInfo>) parser
                .parse(readFile("src/test/resources/mml-output/MML-ZUSI-MGW-4201"));
        Assert.assertNotNull(list);
        Assert.assertEquals(34, list.size());
        for (UnitInfo unitInfo : list) {
            Assert.assertNotNull(unitInfo.getName());
        }
        UnitInfo unitInfo = (UnitInfo) list.get(0);
        Assert.assertEquals("OMU-0", unitInfo.getName());
        Assert.assertEquals("WO-EX", unitInfo.getState());
        Assert.assertEquals("-", unitInfo.getInfo());
    }

    @Test
    public void testAlarm() throws Exception {
        ParserManager parserManager = new ParserManager("src/test/resources/config/parsers.xml");
        Parser parser = parserManager.getParser("alarmMsc");
        @SuppressWarnings("unchecked")
        List<Alarm> list = (List<Alarm>) parser
                .parse(readFile("src/test/resources/mml-output/MML-ZAHO-MSC-4001"));
        Assert.assertNotNull(list);
        Assert.assertEquals(195, list.size());
        for (Alarm alarm : list) {
            Assert.assertNotNull("" + alarm.getAlarmNumber(), alarm.getUnitName());
            Assert.assertNotNull("" + alarm.getAlarmNumber(), alarm.getTime());
            Assert.assertNotNull("" + alarm.getAlarmNumber(), alarm.getSeverity());
        }
        Alarm alarm = (Alarm) list.get(0);
        Assert.assertEquals(2915, alarm.getAlarmNumber());
        Assert.assertEquals("ET-478", alarm.getUnitName());
        Assert.assertEquals(Severity.MAJOR, alarm.getSeverity());
    }

    @Test
    public void testAlarmMgw() throws Exception {
        ParserManager parserManager = new ParserManager("src/test/resources/config/parsers.xml");
        Parser parser = parserManager.getParser("alarmIPA");
        @SuppressWarnings("unchecked")
        List<Alarm> list = (List<Alarm>) parser
                .parse(readFile("src/test/resources/mml-output/MML-ZAAP-MGW-4201"));
        Assert.assertNotNull(list);
        Assert.assertEquals(47, list.size());
        for (Alarm alarm : list) {
            Assert.assertNotNull("" + alarm.getAlarmNumber(), alarm.getUnitName());
            Assert.assertNotNull("" + alarm.getAlarmNumber(), alarm.getTime());
            Assert.assertNotNull("" + alarm.getAlarmNumber(), alarm.getSeverity());
        }
        Alarm alarm = (Alarm) list.get(0);
        Assert.assertEquals(2915, alarm.getAlarmNumber());
        Assert.assertEquals("NIWU-2", alarm.getUnitName());
        Assert.assertEquals(Severity.MAJOR, alarm.getSeverity());
    }

    @Test
    public void testCpuLoad() throws Exception {
        ParserManager parserManager = new ParserManager("src/test/resources/config/parsers.xml");
        Parser parser = parserManager.getParser("cpuLoadMsc");
        @SuppressWarnings("unchecked")
        List<CpuLoad> list = (List<CpuLoad>) parser
                .parse(readFile("src/test/resources/mml-output/MML-ZDOI-MSC-4001"));
        Assert.assertNotNull(list);
        Assert.assertEquals(36, list.size());
        for (CpuLoad cpuLoad : list) {
            Assert.assertNotNull(cpuLoad.getCpuLoad());
        }
        CpuLoad cpuLoad = (CpuLoad) list.get(0);
        Assert.assertEquals(Integer.valueOf(48), cpuLoad.getCpuLoad());
        Assert.assertEquals("OMU-1", cpuLoad.getUnitName());
    }

    @Test
    public void testClearCode() throws Exception {
        ParserManager parserManager = new ParserManager("src/test/resources/config/parsers.xml");
        Parser parser = parserManager.getParser("clearCodeMsc");
        @SuppressWarnings("unchecked")
        List<ClearCode> list = (List<ClearCode>) parser
                .parse(readFile("src/test/resources/mml-output/MML-ZTUTCLR-MSC-4001"));
        Assert.assertNotNull(list);
        Assert.assertEquals(8, list.size());
        for (ClearCode clearCode : list) {
            Assert.assertNotNull(clearCode.getInfo(), clearCode.getInfo());
            Assert.assertNotNull(clearCode.getInfo(), clearCode.getSignalling());
            Assert.assertNotNull(clearCode.getInfo(), clearCode.getRing());
            Assert.assertNotNull(clearCode.getInfo(), clearCode.getSpeech());
            Assert.assertNotNull(clearCode.getInfo(), clearCode.getType());
        }
        ClearCode clearCode = (ClearCode) list.get(0);
        Assert.assertEquals(4, clearCode.getSignalling());
        Assert.assertEquals(0, clearCode.getRing());
        Assert.assertEquals(47342, clearCode.getSpeech());
        Assert.assertEquals(0, clearCode.getType());
        Assert.assertEquals("NORMAL END OF THE CALL", clearCode.getInfo());
    }

    @Test
    public void testLicense() throws Exception {
        ParserManager parserManager = new ParserManager("src/test/resources/config/parsers.xml");
        Parser parser = parserManager.getParser("licenseMsc");
        @SuppressWarnings("unchecked")
        List<LicenseInfo> list = (List<LicenseInfo>) parser
                .parse(readFile("src/test/resources/mml-output/MML-ZWTILICFULL-MSC-4001"));
        Assert.assertNotNull(list);
        Assert.assertEquals(37, list.size());
        for (LicenseInfo license : list) {
            Assert.assertNotNull(license.getName());
        }
        LicenseInfo license = (LicenseInfo) list.get(0);
        Assert.assertEquals("MSGLIC61249", license.getCode());
        Assert.assertEquals("L.1249 MSS Basic SW capacity", license.getName());
    }

    @Test
    public void testLicenseSgsn() throws Exception {
        ParserManager parserManager = new ParserManager("src/test/resources/config/parsers.xml");
        Parser parser = parserManager.getParser("licenseMsc");
        @SuppressWarnings("unchecked")
        List<LicenseInfo> list = (List<LicenseInfo>) parser
                .parse(readFile("src/test/resources/mml-output/MML-ZWTILICFULL-SGSN-4101"));
        Assert.assertNotNull(list);
        Assert.assertEquals(37, list.size());
        for (LicenseInfo license : list) {
            Assert.assertNotNull(license.getName());
        }
        LicenseInfo license = (LicenseInfo) list.get(0);
        Assert.assertEquals("MSGLIC61249", license.getCode());
        Assert.assertEquals("L.1249 MSS Basic SW capacity", license.getName());
    }

    @Test
    public void testSwVersion() throws Exception {
        ParserManager parserManager = new ParserManager("src/test/resources/config/parsers.xml");
        Parser parser = parserManager.getParser("swVersionMsc");
        @SuppressWarnings("unchecked")
        List<SwVersion> list = (List<SwVersion>) parser
                .parse(readFile("src/test/resources/mml-output/MML-ZWQORUN-MSC-4001"));
        Assert.assertNotNull(list);
        for (SwVersion swVersion : list) {
            Assert.assertNotNull(swVersion.getUnitName(), swVersion.getVersion());
        }
        Assert.assertEquals(25, list.size());
        SwVersion swVersion = (SwVersion) list.get(0);
        Assert.assertEquals("OMU-0", swVersion.getUnitName());
        Assert.assertEquals("TELENOR", swVersion.getName());
        Assert.assertEquals("ME 9.1-0", swVersion.getVersion());
    }

    @Test
    public void testSwVersionAUB143W() throws Exception {
        ParserManager parserManager = new ParserManager("src/test/resources/config/parsers.xml");
        Parser parser = parserManager.getParser("swVersionMsc");
        @SuppressWarnings("unchecked")
        List<SwVersion> list = (List<SwVersion>) parser
                .parse(readFile("src/test/resources/mml-output/MML-ZWQORUN-MGW-AUB143W"));
        Assert.assertNotNull(list);
        for (SwVersion swVersion : list) {
            Assert.assertNotNull(swVersion.getUnitName(), swVersion.getVersion());
        }
        Assert.assertEquals(273, list.size());
        SwVersion swVersion = (SwVersion) list.get(0);
        Assert.assertEquals("OMU-0", swVersion.getUnitName());
        Assert.assertEquals("U4GUA400", swVersion.getName());
        Assert.assertEquals("U4 7.4-0", swVersion.getVersion());
    }

    @Test
    public void testSwPackage() throws Exception {
        ParserManager parserManager = new ParserManager("src/test/resources/config/parsers.xml");
        Parser parser = parserManager.getParser("swPackageMsc");
        @SuppressWarnings("unchecked")
        List<SwPackage> list = (List<SwPackage>) parser
                .parse(readFile("src/test/resources/mml-output/MML-ZWQOCR-MSC-4001"));
        Assert.assertNotNull(list);
        Assert.assertEquals(6, list.size());
        for (SwPackage swPackage : list) {
            Assert.assertNotNull(swPackage.getName(), swPackage.getVersion());
        }
        SwPackage swPackage = (SwPackage) list.get(0);
        Assert.assertEquals("TELENOR", swPackage.getName());
        Assert.assertEquals("ME 9.1-0", swPackage.getVersion());
        Assert.assertEquals(true, swPackage.getDef());
        Assert.assertEquals(true, swPackage.getAct());
    }

    @Test
    public void testSwPackage4007() throws Exception {
        ParserManager parserManager = new ParserManager("src/test/resources/config/parsers.xml");
        Parser parser = parserManager.getParser("swPackageMsc");
        @SuppressWarnings("unchecked")
        List<SwPackage> list = (List<SwPackage>) parser
                .parse(readFile("src/test/resources/mml-output/MML-ZWQOCR-MSC-4007"));
        Assert.assertNotNull(list);
        Assert.assertEquals(6, list.size());
        for (SwPackage swPackage : list) {
            Assert.assertNotNull(swPackage.getName(), swPackage.getVersion());
        }
        SwPackage swPackage = (SwPackage) list.get(0);
        Assert.assertEquals("FB110603", swPackage.getName());
        Assert.assertEquals("MF 4.2-0", swPackage.getVersion());
        Assert.assertEquals(false, swPackage.getDef());
        Assert.assertEquals(true, swPackage.getAct());
    }

    protected String readFile(String filename) throws IOException {
        return FileUtils.readFileToString(new File(filename));
    }
}

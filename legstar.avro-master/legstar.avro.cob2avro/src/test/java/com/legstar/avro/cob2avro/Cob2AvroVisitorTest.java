package com.legstar.avro.cob2avro;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.junit.Before;
import org.junit.Test;

import com.legstar.base.context.CobolContext;
import com.legstar.base.context.EbcdicCobolContext;
import com.legstar.base.type.CobolType;
import com.legstar.base.type.composite.CobolChoiceType;
import com.legstar.base.utils.HexUtils;
import com.legstar.base.visitor.FromCobolChoiceStrategy;

public class Cob2AvroVisitorTest extends AbstractTest {

    private static final boolean CREATE_REFERENCE = false;

    private CobolContext cobolContext;

    @Before
    public void setUp() {
        setCreateReferences(CREATE_REFERENCE);
        cobolContext = new EbcdicCobolContext();
    }

    @Test
    public void testConvertFlat01() {
        Schema schema = getSchema("flat01");
        Cob2AvroVisitor visitor = new Cob2AvroVisitor(
                cobolContext,
                HexUtils.decodeHex("F0F0F1F0F4F3D5C1D4C5F0F0F0F0F4F3404040404040404040400215000F"),
                schema);
        visitor.visit(new legstar.test.avro.flat01.CobolFlat01Record());
        check(avro2Json((GenericRecord) visitor.getResultObject()),
                "result.json");
        assertEquals(30, visitor.getLastPos());

        byte[] data = avroSerializeGeneric((GenericRecord) visitor
                .getResultObject());
        avroReadSpecific(legstar.test.avro.flat01.Flat01Record.class, data);
    }

    @Test
    public void testConvertFlat02() {
        Schema schema = getSchema("flat02");
        Cob2AvroVisitor visitor = new Cob2AvroVisitor(
                cobolContext,
                HexUtils.decodeHex("F0F0F0F0F6F2D5C1D4C5F0F0F0F0F6F2404040404040404040400310000F003E001F0014000F000C"),
                schema);
        visitor.visit(new legstar.test.avro.flat02.CobolFlat02Record());
        check(avro2Json((GenericRecord) visitor.getResultObject()),
                "result.json");
        assertEquals(40, visitor.getLastPos());

        byte[] data = avroSerializeGeneric((GenericRecord) visitor
                .getResultObject());
        avroReadSpecific(legstar.test.avro.flat02.Flat02Record.class, data);
    }

    @Test
    public void testCob2AvroStru01() {

        Schema schema = getSchema("stru01");
        Cob2AvroVisitor visitor = new Cob2AvroVisitor(
                cobolContext,
                HexUtils.decodeHex("F0F0F0F0F6F2D5C1D4C5F0F0F0F0F6F2404040404040404040400310000F003EC1C2"),
                schema);

        visitor.visit(new legstar.test.avro.stru01.CobolStru01Record());
        check(avro2Json((GenericRecord) visitor.getResultObject()),
                "result.json");
        assertEquals(34, visitor.getLastPos());

        byte[] data = avroSerializeGeneric((GenericRecord) visitor
                .getResultObject());
        avroReadSpecific(legstar.test.avro.stru01.Stru01Record.class, data);

    }

    @Test
    public void testCob2AvroStru03() {

        Schema schema = getSchema("stru03");
        Cob2AvroVisitor visitor = new Cob2AvroVisitor(
                cobolContext,
                HexUtils.decodeHex("F0F0F0F0F6F2D5C1D4C5F0F0F0F0F6F2404040404040404040400310000F003EC1C2001FC1C20014C1C2000FC1C2000CC1C2"),
                schema);

        visitor.visit(new legstar.test.avro.stru03.CobolStru03Record());
        check(avro2Json((GenericRecord) visitor.getResultObject()),
                "result.json");
        assertEquals(50, visitor.getLastPos());

        byte[] data = avroSerializeGeneric((GenericRecord) visitor
                .getResultObject());
        avroReadSpecific(legstar.test.avro.stru03.Stru03Record.class, data);

    }

    @Test
    public void testCob2AvroStru04() {

        Schema schema = getSchema("stru04");
        Cob2AvroVisitor visitor = new Cob2AvroVisitor(
                cobolContext,
                HexUtils.decodeHex("0190000F00090006C2C5C5C2C4C40001900FC2C2C5C4C5C30000950F0003000000020013000CC2C4C2C1C5C40003800FC1C5C2C2C4C10001900F000600000005001C0013C1C5C2C5C1C30005700FC4C2C3C3C3C20002850F0009000000080023750F"),
                schema);

        visitor.visit(new legstar.test.avro.stru04.CobolStru04Record());
        check(avro2Json((GenericRecord) visitor.getResultObject()),
                "result.json");
        assertEquals(98, visitor.getLastPos());

        byte[] data = avroSerializeGeneric((GenericRecord) visitor
                .getResultObject());
        avroReadSpecific(legstar.test.avro.stru04.Stru04Record.class, data);

    }

    @Test
    public void testCob2AvroAlltypes() {

        Schema schema = getSchema("alltypes");
        Cob2AvroVisitor visitor = new Cob2AvroVisitor(cobolContext,
                HexUtils.decodeHex("c1c2c3c4" + "01020000" + "fc5c" + "000f"
                        + "0001343a" + "000001c4" + "0000000000004532456d"
                        + "0000000000007800056f"
                        + "0000000000000000087554907654321c"
                        + "0000000000000000000564678008321f" + "000007545f"
                        + "45543ae9" + "361677a4590fab60" + "c1c2c3c4"
                        + "c1c2c3c4" + "40404040" + "40404040" + "fc5c"
                        + "fc5c" + "000f" + "000f" + "0001343a" + "0001343a"
                        + "000001c4" + "000001c4" + "0000000000004532456d"
                        + "0000000000004532456d" + "0000000000007800056f"
                        + "0000000000007800056f"
                        + "0000000000000000087554907654321c"
                        + "0000000000000000087554907654321c"
                        + "0000000000000000000564678008321f"
                        + "0000000000000000000564678008321f" + "000007545f"
                        + "000007545f" + "45543ae9" + "45543ae9"
                        + "361677a4590fab60" + "361677a4590fab60"), schema);

        visitor.visit(new legstar.test.avro.alltypes.CobolAlltypesRecord());
        check(avro2Json((GenericRecord) visitor.getResultObject()),
                "result.json");
        assertEquals(267, visitor.getLastPos());

        byte[] data = avroSerializeGeneric((GenericRecord) visitor
                .getResultObject());
        avroReadSpecific(legstar.test.avro.alltypes.AlltypesRecord.class, data);

    }

    @Test
    public void testCob2AvroCustdat() {

        Schema schema = getSchema("custdat");
        Cob2AvroVisitor visitor = new Cob2AvroVisitor(
                cobolContext,
                HexUtils.decodeHex("F0F0F0F0F0F1D1D6C8D540E2D4C9E3C840404040404040404040C3C1D4C2D9C9C4C7C540E4D5C9E5C5D9E2C9E3E8F4F4F0F1F2F5F6F500000002F1F061F0F461F1F1000000000023556C5C5C5C5C5C5C5C5C5CF1F061F0F461F1F1000000000023556C5C5C5C5C5C5C5C5C5C"),
                schema);

        visitor.visit(new legstar.test.avro.custdat.CobolCustomerData());
        check(avro2Json((GenericRecord) visitor.getResultObject()),
                "result.json");
        assertEquals(108, visitor.getLastPos());

        byte[] data = avroSerializeGeneric((GenericRecord) visitor
                .getResultObject());
        avroReadSpecific(legstar.test.avro.custdat.CustomerData.class, data);

    }

    @Test
    public void testCob2AvroRdef01() {

        Schema schema = getSchema("rdef01");
        Cob2AvroVisitor visitor = new Cob2AvroVisitor(
                cobolContext,
                HexUtils.decodeHex("0000D5C1D4C5F0F0F0F0F0F50000D5C1D4C5F0F0F0F0F2F1"),
                schema);

        visitor.visit(new legstar.test.avro.rdef01.CobolRdef01Record());
        check(avro2Json((GenericRecord) visitor.getResultObject()),
                "result.json");
        assertEquals(12, visitor.getLastPos());

        byte[] data = avroSerializeGeneric((GenericRecord) visitor
                .getResultObject());
        avroReadSpecific(legstar.test.avro.rdef01.Rdef01Record.class, data);

    }

    @Test
    public void testCob2AvroRdef01Strategy() {

        Schema schema = getSchema("rdef01");
        byte[] hostData = HexUtils.decodeHex("00010250000F40404040404000010260000F404040404040");
        Cob2AvroVisitor visitor = new Cob2AvroVisitor(
                cobolContext,
                hostData,
                0, hostData.length, new FromCobolChoiceStrategy() {

                    public CobolType choose(String choiceFieldName,
                            CobolChoiceType choiceType,
                            Map < String, Object > variables, byte[] hostData,
                            int start, int length) {
                        int select = ((Number) variables.get("comSelect"))
                                .intValue();
                        switch (select) {
                        case 0:
                            return choiceType.getAlternatives().get(
                                    "ComDetail1");
                        case 1:
                            return choiceType.getAlternatives().get(
                                    "ComDetail2");
                        default:
                            return null;
                        }
                    }

                    public Set < String > getVariableNames() {
                        Set < String > variables = new HashSet < String >();
                        variables.add("comSelect");
                        return variables;
                    }

                }, schema);

        visitor.visit(new legstar.test.avro.rdef01.CobolRdef01Record());
        check(avro2Json((GenericRecord) visitor.getResultObject()),
                "result.json");
        assertEquals(6, visitor.getLastPos());

        byte[] data = avroSerializeGeneric((GenericRecord) visitor
                .getResultObject());
        avroReadSpecific(legstar.test.avro.rdef01.Rdef01Record.class, data);

    }

    @Test
    public void testCob2AvroRdef02() {

        Schema schema = getSchema("rdef02");
        Cob2AvroVisitor visitor = new Cob2AvroVisitor(
                cobolContext,
                HexUtils.decodeHex("C1C2C3D1D2D30000D5C1D4C5F0F0F0F0F0F50260000F"),
                schema);

        visitor.visit(new legstar.test.avro.rdef02.CobolRdef02Record());
        check(avro2Json((GenericRecord) visitor.getResultObject()),
                "result.json");
        assertEquals(22, visitor.getLastPos());

        byte[] data = avroSerializeGeneric((GenericRecord) visitor
                .getResultObject());
        avroReadSpecific(legstar.test.avro.rdef02.Rdef02Record.class, data);

    }

    @Test
    public void testCob2AvroRdef02Alt() {

        Schema schema = getSchema("rdef02");
        Cob2AvroVisitor visitor = new Cob2AvroVisitor(
                cobolContext,
                HexUtils.decodeHex("00001361588C0000D5C1D4C5F0F0F0F0F0F50261588F"),
                schema);

        visitor.visit(new legstar.test.avro.rdef02.CobolRdef02Record());
        check(avro2Json((GenericRecord) visitor.getResultObject()),
                "result.json");
        assertEquals(22, visitor.getLastPos());

        byte[] data = avroSerializeGeneric((GenericRecord) visitor
                .getResultObject());
        avroReadSpecific(legstar.test.avro.rdef02.Rdef02Record.class, data);

    }

    @Test
    public void testCob2AvroRdef03DefaultStrategyFirstAlternative() {

        Schema schema = getSchema("rdef03");
        Cob2AvroVisitor visitor = new Cob2AvroVisitor(cobolContext,
                HexUtils.decodeHex("0002F1F2F3F4F50000000000"), schema);

        visitor.visit(new legstar.test.avro.rdef03.CobolRdef03Record());
        check(avro2Json((GenericRecord) visitor.getResultObject()),
                "result.json");
        assertEquals(12, visitor.getLastPos());

        byte[] data = avroSerializeGeneric((GenericRecord) visitor
                .getResultObject());
        avroReadSpecific(legstar.test.avro.rdef03.Rdef03Record.class, data);

    }

    @Test
    public void testCob2AvroRdef03DefaultStrategySecondAlternative() {

        Schema schema = getSchema("rdef03");
        Cob2AvroVisitor visitor = new Cob2AvroVisitor(cobolContext,
                HexUtils.decodeHex("00010250000F"), schema);

        visitor.visit(new legstar.test.avro.rdef03.CobolRdef03Record());
        check(avro2Json((GenericRecord) visitor.getResultObject()),
                "result.json");
        assertEquals(6, visitor.getLastPos());

        byte[] data = avroSerializeGeneric((GenericRecord) visitor
                .getResultObject());
        avroReadSpecific(legstar.test.avro.rdef03.Rdef03Record.class, data);

    }

    @Test
    public void testCob2AvroRdef03CustomStrategy() {

        Schema schema = getSchema("rdef03");
        byte[] hostData = HexUtils.decodeHex("0002F1F2F3F4F50000000000");
        Cob2AvroVisitor visitor = new Cob2AvroVisitor(cobolContext,
                hostData, 0, hostData.length,
                new Rdef03ObjectFromHostChoiceStrategy(), schema);

        visitor.visit(new legstar.test.avro.rdef03.CobolRdef03Record());
        check(avro2Json((GenericRecord) visitor.getResultObject()),
                "result.json");
        assertEquals(7, visitor.getLastPos());

        byte[] data = avroSerializeGeneric((GenericRecord) visitor
                .getResultObject());
        avroReadSpecific(legstar.test.avro.rdef03.Rdef03Record.class, data);

    }

    @Test
    public void testCob2AvroArdo01EmptyVariableArray() {

        Schema schema = getSchema("ardo01");
        Cob2AvroVisitor visitor = new Cob2AvroVisitor(
                cobolContext,
                HexUtils.decodeHex("F0F0F0F0F6F2D5C1D4C5F0F0F0F0F6F2404040404040404040400000"),
                schema);

        visitor.visit(new legstar.test.avro.ardo01.CobolArdo01Record());
        check(avro2Json((GenericRecord) visitor.getResultObject()),
                "result.json");
        assertEquals(28, visitor.getLastPos());

        byte[] data = avroSerializeGeneric((GenericRecord) visitor
                .getResultObject());
        avroReadSpecific(legstar.test.avro.ardo01.Ardo01Record.class, data);

    }

    @Test
    public void testCob2AvroArdo01OneItemVariableArray() {

        Schema schema = getSchema("ardo01");
        Cob2AvroVisitor visitor = new Cob2AvroVisitor(
                cobolContext,
                HexUtils.decodeHex("F0F0F0F0F6F2D5C1D4C5F0F0F0F0F6F2404040404040404040400001000000000023556C"),
                schema);

        visitor.visit(new legstar.test.avro.ardo01.CobolArdo01Record());
        check(avro2Json((GenericRecord) visitor.getResultObject()),
                "result.json");
        assertEquals(36, visitor.getLastPos());

        byte[] data = avroSerializeGeneric((GenericRecord) visitor
                .getResultObject());
        avroReadSpecific(legstar.test.avro.ardo01.Ardo01Record.class, data);

    }

    @Test
    public void testCob2AvroArdo01FullVariableArray() {

        Schema schema = getSchema("ardo01");
        Cob2AvroVisitor visitor = new Cob2AvroVisitor(
                cobolContext,
                HexUtils.decodeHex("F0F0F0F0F6F2D5C1D4C5F0F0F0F0F6F2404040404040404040400005000000000023556C000000000023656C000000000023756C000000000023856C000000000023956C"),
                schema);

        visitor.visit(new legstar.test.avro.ardo01.CobolArdo01Record());
        check(avro2Json((GenericRecord) visitor.getResultObject()),
                "result.json");
        assertEquals(68, visitor.getLastPos());

        byte[] data = avroSerializeGeneric((GenericRecord) visitor
                .getResultObject());
        avroReadSpecific(legstar.test.avro.ardo01.Ardo01Record.class, data);

    }

    @Test
    public void testCob2AvroCflt01() {

        Schema schema = getSchema("cflt01");
        Cob2AvroVisitor visitor = new Cob2AvroVisitor(
                cobolContext,
                HexUtils.decodeHex("F1F2F3F4F5F6F7F8F9F0F1F2F3F4F5F6F7F8C1C2C3C4C5C1C2C3C4C5C6C7C8C9C0C1C2C3C4C5C6C7C8D1D2D3D4D5"),
                schema);

        visitor.visit(new legstar.test.avro.cflt01.CobolCflt01Record());
        check(avro2Json((GenericRecord) visitor.getResultObject()),
                "result.json");
        assertEquals(46, visitor.getLastPos());

        byte[] data = avroSerializeGeneric((GenericRecord) visitor
                .getResultObject());
        avroReadSpecific(legstar.test.avro.cflt01.Cflt01Record.class, data);

    }

    @Test
    public void testCob2AvroOptl01AllAbsent() {

        Schema schema = getSchema("optl01");
        Cob2AvroVisitor visitor = new Cob2AvroVisitor(
                cobolContext,
                HexUtils.decodeHex("F0F0F0F0F0F0"),
                schema);

        visitor.visit(new legstar.test.avro.optl01.CobolOptl01Record());
        check(avro2Json((GenericRecord) visitor.getResultObject()),
                "result.json");
        assertEquals(6, visitor.getLastPos());

        byte[] data = avroSerializeGeneric((GenericRecord) visitor
                .getResultObject());
        avroReadSpecific(legstar.test.avro.optl01.Optl01Record.class, data);

    }

    @Test
    public void testCob2AvroStructPresentStringAbsent() {

        Schema schema = getSchema("optl01");
        Cob2AvroVisitor visitor = new Cob2AvroVisitor(
                cobolContext,
                HexUtils.decodeHex("F0F0F1F0F0F0F1F2F3F4F5F6F7F8F9F0F1F2F3F4F5F6F7F8C1C2C3C4C5"),
                schema);

        visitor.visit(new legstar.test.avro.optl01.CobolOptl01Record());
        check(avro2Json((GenericRecord) visitor.getResultObject()),
                "result.json");
        assertEquals(29, visitor.getLastPos());

        byte[] data = avroSerializeGeneric((GenericRecord) visitor
                .getResultObject());
        avroReadSpecific(legstar.test.avro.optl01.Optl01Record.class, data);

    }

    @Test
    public void testCob2AvroStructAbsentStringPresent() {

        Schema schema = getSchema("optl01");
        Cob2AvroVisitor visitor = new Cob2AvroVisitor(
                cobolContext,
                HexUtils.decodeHex("F0F0F0F0F0F1D1D1D1D1D1D1D1D1D1D1D1D1D1D1D1D1D1D1D1D1D1D1D1D1D1D1D1D1D1D1D2D3"),
                schema);

        visitor.visit(new legstar.test.avro.optl01.CobolOptl01Record());
        check(avro2Json((GenericRecord) visitor.getResultObject()),
                "result.json");
        assertEquals(38, visitor.getLastPos());

        byte[] data = avroSerializeGeneric((GenericRecord) visitor
                .getResultObject());
        avroReadSpecific(legstar.test.avro.optl01.Optl01Record.class, data);

    }

    @Test
    public void testCob2AvroStructPresentStringPresent() {

        Schema schema = getSchema("optl01");
        Cob2AvroVisitor visitor = new Cob2AvroVisitor(
                cobolContext,
                HexUtils.decodeHex("F0F0F1F0F0F1F1F2F3F4F5F6F7F8F9F0F1F2F3F4F5F6F7F8C1C2C3C4C5D1D1D1D1D1D1D1D1D1D1D1D1D1D1D1D1D1D1D1D1D1D1D1D1D1D1D1D1D1D1D2D3"),
                schema);

        visitor.visit(new legstar.test.avro.optl01.CobolOptl01Record());
        check(avro2Json((GenericRecord) visitor.getResultObject()),
                "result.json");
        assertEquals(61, visitor.getLastPos());

        byte[] data = avroSerializeGeneric((GenericRecord) visitor
                .getResultObject());
        avroReadSpecific(legstar.test.avro.optl01.Optl01Record.class, data);

    }

    private class Rdef03ObjectFromHostChoiceStrategy implements
            FromCobolChoiceStrategy {

        public CobolType choose(String choiceFieldName,
                CobolChoiceType choiceType, Map < String, Object > variables,
                byte[] hostData, int start, int length) {

            int select = ((Number) variables.get("comSelect")).intValue();

            switch (select) {
            case 0:
                return choiceType.getAlternatives().get("comDetail1");
            case 1:
                return choiceType.getAlternatives().get("comDetail2");
            case 2:
                return choiceType.getAlternatives().get("comDetail3");
            default:
                return null;

            }
        }

        public Set < String > getVariableNames() {
            Set < String > varNames = new HashSet < String >();
            varNames.add("comSelect");
            return varNames;
        }

    }

}

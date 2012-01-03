/*
 * An XML Jasper interface; Takes XML data from the standard input
 * and uses JRXmlDataSource to generate Jasper reports in the
 * specified output format using the specified compiled Jasper design.
 * 
 * Inspired by the xmldatasource sample application 
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;

/**
 * @author Jonas Schwertfeger (jonas at schwertfeger dot ch)
 * @version $Id: XmlJasperInterface.java 97 2005-11-23 14:48:15Z js $
 * 
 * @author Paulo Pessoa
 */
public class XmlJasperInterface {

    private static final String TYPE_PDF = "pdf";
    private static final String TYPE_XML = "xml";
    private static final String TYPE_RTF = "rtf";
    private static final String TYPE_XLS = "xls";
    private static final String TYPE_CSV = "csv";
    private static final String TYPE_HTML = "html";
    
    private String outputType;
    private String compiledDesign;
    private String selectCriteria;
    private String xmlFilename;

    public static void main(String[] args) {
        String outputType = null;
        String compiledDesign = null;
        String selectCriteria = null;
        String xmlFilename = null;

        if (args.length < 2) {
            printUsage();
            return;
        }

        for (int k = 0; k < args.length; ++k) {
            if (args[k].startsWith("-o:")) { //output format
                outputType = args[k].substring(3); 
            } else if (args[k].startsWith("-f:")) { //filename jasper
                compiledDesign = args[k].substring(3);
            } else if (args[k].startsWith("-x:")) { //select criteria path for xml
                selectCriteria = args[k].substring(3);
            } else if (args[k].startsWith("-d:")) { //data in XML full filename
                xmlFilename = args[k].substring(3);
            }
        }
        XmlJasperInterface jasperInterface = null;
        if (selectCriteria == null) {
            jasperInterface = new XmlJasperInterface(outputType, compiledDesign);
        } else if (xmlFilename == null) {
            jasperInterface = new XmlJasperInterface(outputType, compiledDesign, selectCriteria);
        } else {
            jasperInterface = new XmlJasperInterface(outputType, compiledDesign, selectCriteria, xmlFilename);
        }

        if (!jasperInterface.report()) {
            System.exit(1);
        }
    }

    public XmlJasperInterface(
            String outputType,
            String compiledDesign,
            String selectCriteria,
            String xmlFilename) {
        this.outputType = outputType;
        this.compiledDesign = compiledDesign;
        this.selectCriteria = selectCriteria;
        this.xmlFilename = xmlFilename;
    }

    public XmlJasperInterface(
            String outputType,
            String compiledDesign,
            String selectCriteria) {
        this.outputType = outputType;
        this.compiledDesign = compiledDesign;
        this.selectCriteria = selectCriteria;
    }

    public XmlJasperInterface(
            String outputType,
            String compiledDesign) {
        this.outputType = outputType;
        this.compiledDesign = compiledDesign;
    }

    public boolean report() {
        try {
            InputStream in = System.in;
            PrintStream out = System.out;
            
            if (xmlFilename!=null){
                File file = new File(xmlFilename);
                in = new FileInputStream(file);
                
                String outFilename = "";
                if (compiledDesign.matches(".*\\.jasper$")) 
                    outFilename = compiledDesign.substring(0, compiledDesign.indexOf(".")+1) + outputType;
                else
                    outFilename = compiledDesign +"."+ outputType;
                out = new PrintStream(new File(outFilename));
            }
            
            JasperPrint jasperPrint = (this.selectCriteria != null)
                        ? JasperFillManager.fillReport(compiledDesign, null, new JRXmlDataSource(in, selectCriteria))
                        : JasperFillManager.fillReport(compiledDesign, null, new JRXmlDataSource(in));
            
            return export(jasperPrint, out);
            
        } catch (FileNotFoundException e) {
            System.out.println(e.toString());
            return false;
        } catch (JRException e) {
            System.out.println(e.toString());
            return false;
        }
    }

    private boolean export(JasperPrint jasperPrint, PrintStream out) {
        try{
            if (TYPE_PDF.equals(outputType)) {
                JasperExportManager.exportReportToPdfStream(jasperPrint, out);
            } else if (TYPE_XML.equals(outputType)) {
                JasperExportManager.exportReportToXmlStream(jasperPrint, out);
            } else if (TYPE_RTF.equals(outputType)) {
                JRRtfExporter rtfExporter = new JRRtfExporter();
                rtfExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                rtfExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
                rtfExporter.exportReport();
            } else if (TYPE_XLS.equals(outputType)) {
                JRXlsExporter xlsExporter = new JRXlsExporter();
                xlsExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                xlsExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
                xlsExporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
                xlsExporter.exportReport();
            } else if (TYPE_CSV.equals(outputType)) {
                JRCsvExporter csvExporter = new JRCsvExporter();
                csvExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                csvExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
                csvExporter.exportReport();
            } else if (TYPE_HTML.equals(outputType)) {
                JRHtmlExporter htmlExporter = new JRHtmlExporter();
                htmlExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                htmlExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
                htmlExporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, false);
                htmlExporter.exportReport();
            } else {
                printUsage();
            }
            
        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }
        return true;
    }
    
    private static void printUsage() {
        System.out.println("XmlJasperInterface usage:");
        System.out.println("\tjava XmlJasperInterface -oOutputType -fCompiledDesign -xSelectExpression < input.xml > report\n");
        System.out.println("\tOutput types:\t\tpdf | xml | rtf | xls | csv | html");
        System.out.println("\tCompiled design:\tFilename of the compiled Jasper design");
        System.out.println("\tSelect expression:\tXPath expression that specifies the select criteria");
        System.out.println("\t\t\t\t(See net.sf.jasperreports.engine.data.JRXmlDataSource for further information)");
        System.out.println("\tStandard input:\t\tXML input data");
        System.out.println("\tStandard output:\tReport generated by Jasper");
    }
}

package com.example.demo.jasper;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;

@RestController
public class Controller {
	
	private String template = "C:\\Users\\dev03\\Desktop\\Blank_A4.jrxml";
	private String templateSub = "C:\\Users\\dev03\\Desktop\\Blank_A4_1.jrx";
	private String templateSub1 = "C:\\Users\\dev03\\Desktop\\Blank_A4_1.jrxml";
	private String relatorio = "C:\\Users\\dev03\\Desktop\\Blank_WA4.pdf";
	private String imagem = "C:\\Users\\dev03\\Desktop\\imagem.jpg";
	
	@GetMapping(name="gerar")
	public ResponseEntity gerar() throws Exception {
		final InputStream reportInputStream1 = Files.newInputStream(Paths.get(templateSub1), StandardOpenOption.READ);
        final JasperDesign jasperDesign1 = JRXmlLoader.load(reportInputStream1);
        JasperCompileManager.compileReportToFile(jasperDesign1, templateSub);
        
		final InputStream reportInputStream = Files.newInputStream(Paths.get(template), StandardOpenOption.READ);
        final JasperDesign jasperDesign = JRXmlLoader.load(reportInputStream);
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
		
		
		
		Model model =  new Model();
		model.setNome("Michael");
		
		Cobertura cobertura1 = new Cobertura();
		cobertura1.setDescricao("desc1");
		
		Cobertura cobertura2 = new Cobertura();
		cobertura2.setDescricao("desc2");
		model.setCoberturas(Arrays.asList(cobertura1,cobertura2));
		
		final JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(Arrays.asList(model));
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("assinatura", Files.newInputStream(Paths.get(imagem), StandardOpenOption.READ));
		parameters.put("caminhoTemplate", new File(templateSub));
        
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        JRPdfExporter exporter = new JRPdfExporter();
        
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(
          new SimpleOutputStreamExporterOutput(relatorio));
         
        SimplePdfReportConfiguration reportConfig
          = new SimplePdfReportConfiguration();
        reportConfig.setSizePageToContent(true);
        reportConfig.setForceLineBreakPolicy(false);
         
        SimplePdfExporterConfiguration exportConfig
          = new SimplePdfExporterConfiguration();
        exportConfig.setMetadataAuthor("baeldung");
        exportConfig.setEncrypted(true);
        exportConfig.setAllowedPermissionsHint("PRINTING");
         
        exporter.setConfiguration(reportConfig);
        exporter.setConfiguration(exportConfig);
         
        exporter.exportReport();
        
        System.out.println("8");
        
        reportInputStream.close();
		return ResponseEntity.ok("ok");
	}

}

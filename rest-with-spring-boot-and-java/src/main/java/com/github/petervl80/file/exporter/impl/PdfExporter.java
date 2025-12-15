package com.github.petervl80.file.exporter.impl;

import com.github.petervl80.data.dto.PersonDTO;
import com.github.petervl80.file.exporter.contract.PersonExporter;
import com.github.petervl80.service.QRCodeService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.*;

@Component
public class PdfExporter implements PersonExporter {

    @Autowired
    private QRCodeService qrCodeService;

    @Override
    public Resource exportPeople(List<PersonDTO> people) throws Exception {
        try (InputStream inputStream = getClass().getResourceAsStream("/templates/people.jrxml");
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            if (inputStream == null) throw new RuntimeException("Template file not found: /templates/people.jrxml");

            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(people);
            Map<String, Object> parameters = new HashMap<>();

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

            return new ByteArrayResource(outputStream.toByteArray());
        }
    }

    @Override
    public Resource exportPerson(PersonDTO person) throws Exception {
        try (InputStream mainTemplateStream = getClass().getResourceAsStream("/templates/person.jrxml");
             InputStream subReportStream = getClass().getResourceAsStream("/templates/books.jrxml");
             InputStream qrCodeStream = qrCodeService
                     .generateQRCodeImage(person.getProfileUrl(), 200, 200);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            if (mainTemplateStream == null)
                throw new RuntimeException("Template file not found: /templates/person.jrxml");
            if (subReportStream == null) throw new RuntimeException("Template file not found: /templates/books.jrxml");

            JasperReport mainReport = JasperCompileManager.compileReport(mainTemplateStream);
            JasperReport subReport = JasperCompileManager.compileReport(subReportStream);

            JRBeanCollectionDataSource subReportDataSource = new JRBeanCollectionDataSource(person.getBooks());

            String path = Objects.requireNonNull(getClass().getResource("/templates/books.jasper")).getPath();

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("SUB_REPORT_DATA_SOURCE", subReportDataSource);
            parameters.put("BOOK_SUB_REPORT", subReport);
            parameters.put("SUB_REPORT_DIR", path);
            parameters.put("QR_CODE_IMAGE", qrCodeStream);

            JRBeanCollectionDataSource mainDataSource = new JRBeanCollectionDataSource(Collections.singletonList(person));

            JasperPrint jasperPrint = JasperFillManager.fillReport(mainReport, parameters, mainDataSource);

            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

            return new ByteArrayResource(outputStream.toByteArray());
        }
    }
}

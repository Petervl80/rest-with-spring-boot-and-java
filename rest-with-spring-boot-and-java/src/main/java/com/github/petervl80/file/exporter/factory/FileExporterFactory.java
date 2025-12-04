package com.github.petervl80.file.exporter.factory;

import com.github.petervl80.exception.BadRequestException;
import com.github.petervl80.file.exporter.MediaTypes;
import com.github.petervl80.file.exporter.contract.FileExporter;
import com.github.petervl80.file.exporter.impl.CsvExporter;
import com.github.petervl80.file.exporter.impl.XlsxExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class FileExporterFactory {

    private final Logger logger = LoggerFactory.getLogger(FileExporterFactory.class);

    @Autowired
    private ApplicationContext context;

    public FileExporter getExporter(String acceptHeader) throws Exception {
        if(acceptHeader.equalsIgnoreCase(
                MediaTypes.APPLICATION_XLSX_VALUE)) {
            return context.getBean(XlsxExporter.class);
        } else if (acceptHeader.endsWith(MediaTypes.APPLICATION_CSV_VALUE)) {
            return context.getBean(CsvExporter.class);
        } else {
            throw new BadRequestException("Unsupported File Format");
        }
    }
}

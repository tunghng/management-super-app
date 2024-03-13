package com.im.contact.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.im.contact.dto.model.ContactExportDto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ExcelGenerator {
    private ContactExportDto contactExport;
    private String language;
    private XSSFWorkbook workbook;
    private Sheet sheet;
    private Font fontHeader;
    private Font fontNomal;
    private Map<String, Map<String, String>> languageMap;
    private Map<Integer, Integer> columnMap;

    public ExcelGenerator(
            ContactExportDto contactExport,
            String language
    ) throws IOException {
        this.contactExport = contactExport;
        if (language == null || (!language.equalsIgnoreCase("en-US")
                && !language.equalsIgnoreCase("vi-VN"))) {
            language = "vi-VN";
        }
        this.columnMap = new HashMap<>();
        this.language = language;
        ObjectMapper mapper = new ObjectMapper();
        languageMap = mapper.readValue(
                getClass().getResourceAsStream("/data/translate.json"),
                HashMap.class);
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet(languageMap.get("sheetName").get(language));
        fontHeader = workbook.createFont();
        fontHeader.setBold(true);
        fontHeader.setFontName("Arial");
        fontNomal = workbook.createFont();
        fontNomal.setFontName("Arial");
    }

    private void writeDescriptions() {
        Row row = sheet.createRow(0);
        CellStyle titleStyle = workbook.createCellStyle();
        CellStyle valueStyle = workbook.createCellStyle();
        titleStyle.setFont(fontHeader);
        titleStyle.setAlignment(HorizontalAlignment.LEFT);
        valueStyle.setFont(fontNomal);
        valueStyle.setAlignment(HorizontalAlignment.LEFT);
        createCell(row, 0, languageMap.get("description1").get(language), titleStyle);
        createCell(row, 1, contactExport.getTotalElements(), valueStyle);
        row = sheet.createRow(1);
        createCell(row, 0, languageMap.get("description2").get(language), titleStyle);

        String startDate = null;
        String endDate = null;
        if (contactExport.getStartTs() != null) {
            startDate = Instant.ofEpochMilli(contactExport.getStartTs())
                    .atZone(ZoneId.systemDefault()).toLocalDateTime()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        if (contactExport.getEndTs() != null) {
            endDate = Instant.ofEpochMilli(contactExport.getEndTs())
                    .atZone(ZoneId.systemDefault()).toLocalDateTime()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        createCell(row, 1,
                startDate
                        + " - " + endDate,
                valueStyle);
    }

    private void writeHeader() {
        Row row = sheet.createRow(2);
        CellStyle style = workbook.createCellStyle();
        style.setFont(fontHeader);
        style.setAlignment(HorizontalAlignment.LEFT);
        byte[] lightBlue = {(byte) 213, (byte) 235, (byte) 246};
        style.setFillForegroundColor(new XSSFColor(lightBlue));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        for (int i = 0; i < contactExport.getTotalAttributes(); i++) {
            String attribute = contactExport.getAttributes().get(i);
            createCell(row, i, languageMap.get(attribute).get(language), style);
            columnMap.put(i, languageMap.get(attribute).get(language).length());
        }
    }

    private void writeData() {
        AtomicInteger rowCount = new AtomicInteger(3);
        CellStyle style = workbook.createCellStyle();
        style.setFont(fontNomal);
        contactExport.getData().forEach(contact -> {
            Row row = sheet.createRow(rowCount.getAndIncrement());
            for (int i = 0; i < contact.length; i++) {
                if (contact[i] instanceof LocalDateTime) {
                    LocalDateTime inputDateTime = (LocalDateTime) contact[i];
                    String outputDate = inputDateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                    createCell(row, i, outputDate, style);
                    continue;
                }
                createCell(row, i, contact[i], style);
                int length = String.valueOf(contact[i]).length();
                if (length > columnMap.get(i)) {
                    columnMap.put(i, length);
                }
            }
        });
        for (int i = 0; i < contactExport.getTotalAttributes(); i++) {
            sheet.setColumnWidth(i, (columnMap.get(i) + 4) * 256);
        }
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Float) {
            cell.setCellValue((Float) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
        } else {
            cell.setCellValue(String.valueOf(value));
        }
        cell.setCellStyle(style);
    }

    public ByteArrayOutputStream generate() throws IOException {
        writeDescriptions();
        writeHeader();
        writeData();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        return outputStream;
    }
}
